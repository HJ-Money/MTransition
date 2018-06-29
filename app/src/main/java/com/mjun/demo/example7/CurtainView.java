package com.mjun.demo.example7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import com.mjun.mtransition.ITransitional;

public class CurtainView extends View implements ITransitional {

    private Bitmap bitmap;
    private Bitmap shadowMask;
    private Paint paint;
    private Shader maskShader;
    private int maxAlpha = 0xFF;

    private int width, height;
    private int centerX, centerY;

    private int bitmapWidth = 40;
    private int bitmapHeight = 7;

    private int touchX;
    private int touchY;

    private final static int insDistance = 30;

    private boolean newApiFlag;

    private int delayOffsetX;

    private AccelerateInterpolator interpolator;

    public final static int DIRECTION_LEFT = 0;
    public final static int DIRECTION_RIGHT = 1;

    private int direction = DIRECTION_RIGHT;

    private Handler handler = new Handler();
    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            delayOffsetX += (touchX - delayOffsetX) * 0.3F;

            handler.postDelayed(this, 20);
            invalidate();
        }
    };

    private float[] verts;
    private int[] colors;

    public CurtainView(Context context) {
        this(context, null);
    }

    public CurtainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurtainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        handler.post(delayRunnable);

        newApiFlag = Build.VERSION.SDK_INT >= 18;

        interpolator = new AccelerateInterpolator();

        verts = new float[(bitmapWidth + 1) * (bitmapHeight + 1) * 2];
        colors = new int[(bitmapWidth + 1) * (bitmapHeight + 1)];

        setupMask();

    }

    private void setupMask(){
        if (!newApiFlag && bitmap != null) {

            // 硬件加速不支持drawBitmapMesh的colors绘制的情况下,在原bitmap的上层覆盖一个半透明带阴影的bitmap以实现阴影功能
            //when API level lower than 18,the arguments of drawBitmapMesh method won't work when hardware accelerate is activated,
            //so we cover a transparent layer on the top of the origin bitmap to create a shadow effect

            shadowMask =
                    Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                            Bitmap.Config.ARGB_8888);

            Canvas maskCanvas = new Canvas(shadowMask);

            float singleWave = bitmap.getWidth() / bitmapWidth * 6.28F;
            int blockPerWave = (int) (singleWave / (bitmap.getWidth() / bitmapWidth));

            if (blockPerWave % 2 == 0)
                blockPerWave++;

            float offset =
                    (float) ((bitmap.getWidth() / singleWave - Math.floor(bitmap.getWidth()
                            / singleWave)) * singleWave) + singleWave / 2;

            int[] colors = new int[blockPerWave];
            float[] offsets = new float[blockPerWave];


            float perOffset = 1.0F / blockPerWave;

            int halfWave = (int) Math.floor((float) blockPerWave / 2.0F);

            int perAlpha = maxAlpha / (halfWave - 1);

            for (int i = -halfWave; i < halfWave + 1; i++) {
                int ii = halfWave - Math.abs(i);
                int iii = i + halfWave;
                colors[iii] =
                        (int) (perAlpha * Math.sin((float) ii / (float) blockPerWave * 3.14F)) << 24;

                offsets[iii] = perOffset * iii;
            }

            maskShader =
                    new LinearGradient(offset, 0, singleWave + offset, 0, colors, offsets,
                            Shader.TileMode.REPEAT);

            paint.setShader(maskShader);
            maskCanvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
            paint.setShader(null);

        }
    }

    public void setTexture(int bitmapRes){
        setTexture(BitmapFactory.decodeResource(getResources(), bitmapRes));
    }

    public void setTexture(Bitmap bitmap){
        this.bitmap = bitmap;

        if(shadowMask == null){
            setupMask();
        }

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        centerX = width / 2;
        centerY = height / 2;
    }

    public void flip(int x, int y){
        touchX = x;
        touchY = y;

        invalidate();
    }

    public void flip(float progress){
        int width = getMeasuredWidth();
        if (direction == DIRECTION_LEFT) {
            flip((int) (width * progress), 0);
        } else {
            flip((int) (-width * progress), 0);
        }
    }

    public void percentageFlip(float x, float y){
        touchX = (int) (width * x);
        touchY = (int) (height * y);

        invalidate();
    }

    public void setDirection(int direction){
        this.direction = direction;

        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(this.bitmap != null){
            int index = 0;

            float ratio = (float) touchX / (float) width;
            float gap = 60.0F * (direction == DIRECTION_LEFT ? ratio : (1 - ratio));
            int alpha = 0;
            for (int y = 0; y <= bitmapHeight; y++) {

                float fy = height / bitmapHeight * y;
                float longDisSide = touchY > height - touchY ? touchY : height - touchY;
                float longRatio = Math.abs(fy - touchY) / longDisSide;

                longRatio = interpolator.getInterpolation(longRatio);

                float realWidth = longRatio * (touchX - delayOffsetX);
                float xBlock = (float) width / (float) bitmapWidth;

                for (int x = 0; x <= bitmapWidth; x++) {

                    ratio = (touchX - realWidth) / (float) width;

                    switch(direction){
                        case DIRECTION_LEFT:
                            verts[index * 2] = (bitmapWidth - x) * xBlock * ratio + (x * xBlock);
                            break;
                        case DIRECTION_RIGHT:
                            verts[index * 2] =  x * xBlock * ratio;
                            break;
                    }

                    float realHeight = height - ((float) Math.sin(x * 0.5F - Math.PI) * gap + gap);

                    float offsetY = realHeight / bitmapHeight * y;

                    verts[index * 2 + 1] = (height - realHeight) / 2 + offsetY;

                    int color;

                    int channel = 255 - (int) (height - realHeight) * 2;
                    if (channel < 255) {
                        alpha = (int) ((255 - channel) / 120.0F * maxAlpha) * 4;
                    }
                    if (newApiFlag) {
                        channel = channel < 0 ? 0 : channel;
                        channel = channel > 255 ? 255 : channel;

                        color = 0xFF000000 | channel << 16 | channel << 8 | channel;

                        colors[index] = color;
                    }

                    index += 1;
                }
            }

            canvas.drawBitmapMesh(bitmap, bitmapWidth, bitmapHeight, verts, 0, colors, 0, null);
            if (!newApiFlag) {
                alpha = alpha > 255 ? 255 : alpha;
                alpha = alpha < 0 ? 0 : alpha;
                paint.setAlpha(alpha);
                canvas.drawBitmapMesh(shadowMask, bitmapWidth, bitmapHeight, verts, 0, null, 0, paint);
                paint.setAlpha(255);
            }
        }
    }

    @Override
    public void onTransitProgress(long playTime, float progress) {
        flip(progress);
    }

    @Override
    public void onTransitStart() {

    }

    @Override
    public void onTransitEnd() {

    }
}