package com.mjun.mtransition.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class CloneView extends View {
    private View mSourceView;
    private Drawable mDrawable;
    private Bitmap mBitmap;
    private Paint mPaint = new Paint();
    private Rect mBitmapSrcRect = new Rect();
    private Rect mBitmapDstRect = new Rect();
    private boolean mUseBtmap;
    private int mBgColor = -999;

    private boolean mContentChanged = true;

    public CloneView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mUseBtmap = false;
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
    }

    public void setUseBitmap(boolean useBitmap) {
        mUseBtmap = useBitmap;
    }

    public void setSourceView(View view) {
        mSourceView = view;
    }

    private Bitmap createBitmap(View view, int width, int height) {
        if (width == 0 || height == 0) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
        } catch (Throwable ta) {
            ta.printStackTrace();
        }
        return bitmap;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        if (mBgColor != -999) {
            canvas.drawColor(mBgColor);
        }
        createBitmap();
        if (mUseBtmap) {
            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            }
        } else {
            if (mSourceView != null) {
                mSourceView.draw(canvas);
            }
        }
    }

    private void createBitmap() {
        if (mUseBtmap && mContentChanged) {
            Bitmap bmp = createBitmap(mSourceView, mSourceView.getWidth(), mSourceView.getHeight());
            if (bmp != null) {
                mBitmap = bmp;
                mBitmapSrcRect.set(0, 0, bmp.getWidth(), bmp.getHeight());
                mDrawable = new BitmapDrawable(getContext().getResources(), bmp);
            }
            mContentChanged = false;
        }
    }

    public void setSourceDrawableRes(Drawable drawable) {
        if (drawable != null) {
            mDrawable = drawable;
            if (mDrawable != null) {
                mBitmapSrcRect.set(0, 0, mDrawable.getIntrinsicWidth(),
                    mDrawable.getIntrinsicHeight());
            }
        }
    }

    public View getSourceView() {
        return mSourceView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSourceView.getMeasuredWidth(), mSourceView.getMeasuredHeight());
    }

    public void hasChanged() {
        mContentChanged = true;
    }

    public void setBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }
}
