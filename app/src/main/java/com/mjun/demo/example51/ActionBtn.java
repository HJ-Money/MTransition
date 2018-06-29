package com.mjun.demo.example51;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.mjun.mtransition.ITransitional;

/**
 * Created by huijun on 2018/4/8.
 */

public class ActionBtn extends View implements ITransitional {

    private int mBgColor = 0xfffee388;
    private String mText = "Enter";

    private int mBgColor2 = 0xffFF576B;
    private String mText2 = "Exit";

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mProgress = 0;

    public ActionBtn(Context context) {
        super(context);
    }

    public ActionBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int alpha = 255 - (int) (mProgress * 255);
        canvas.save();
        // 画背景
        mPaint.setColor(mBgColor);
        canvas.drawCircle(width / 2, height / 2, width / 2, mPaint);
        mPaint.setColor(mBgColor2);
        mPaint.setAlpha(alpha);
        canvas.drawCircle(width / 2, height / 2, width / 2, mPaint);
        // 画文字
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(alpha);
        mPaint.setTextSize(width / 5);
        int textWidth = (int) mPaint.measureText(mText);
        canvas.drawText(mText, (width - textWidth) / 2, height / 2 + width / 10, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(255 - alpha);
        textWidth = (int) mPaint.measureText(mText2);
        canvas.drawText(mText2, (width - textWidth) / 2, height / 2 + width / 10, mPaint);
        canvas.restore();
    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
    }

    @Override
    public void onTransitProgress(long playTime, float progress) {
        setProgress(progress);
    }

    @Override
    public void onTransitStart() {

    }

    @Override
    public void onTransitEnd() {

    }
}
