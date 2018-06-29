package com.mjun.mtransition.anim;

import android.view.animation.Interpolator;

/**
 * @author huijun.zhj
 *
 * 存储动画路径数据
 * Store animation path datas
 */
public class Transition {

    private AlphaFactor mAlpha = new AlphaFactor();
    private TranslateXFactor mTransX = new TranslateXFactor();
    private TranslateYFactor mTransY = new TranslateYFactor();
    private ScaleXFactor mScaleX = new ScaleXFactor();
    private ScaleYFactor mScaleY = new ScaleYFactor();
    private RotateXFactor mRotateX = new RotateXFactor();
    private RotateYFactor mRotateY = new RotateYFactor();
    private RotateZFactor mRotateZ = new RotateZFactor();

    public void setDuration(long duration, boolean byUser) {
        mAlpha.setDuration(duration, byUser);
        mTransX.setDuration(duration, byUser);
        mTransY.setDuration(duration, byUser);
        mScaleX.setDuration(duration, byUser);
        mScaleY.setDuration(duration, byUser);
        mRotateX.setDuration(duration, byUser);
        mRotateY.setDuration(duration, byUser);
        mRotateZ.setDuration(duration, byUser);
    }

    public void setStartDelay(long delay) {
        mAlpha.setStartDelay(delay);
        mTransX.setStartDelay(delay);
        mTransY.setStartDelay(delay);
        mScaleX.setStartDelay(delay);
        mScaleY.setStartDelay(delay);
        mRotateX.setStartDelay(delay);
        mRotateY.setStartDelay(delay);
        mRotateZ.setStartDelay(delay);
    }

    public long getMaxAnimTime() {
        long animTime = 0l;
        animTime = Math.max(animTime, mAlpha.getAnimTime());
        animTime = Math.max(animTime, mTransX.getAnimTime());
        animTime = Math.max(animTime, mTransY.getAnimTime());
        animTime = Math.max(animTime, mScaleX.getAnimTime());
        animTime = Math.max(animTime, mScaleY.getAnimTime());
        animTime = Math.max(animTime, mRotateX.getAnimTime());
        animTime = Math.max(animTime, mRotateY.getAnimTime());
        animTime = Math.max(animTime, mRotateZ.getAnimTime());
        return animTime;
    }
    // ------------------------------ alpha ----------------------------------
    public void alphaFrom(float from) {
        mAlpha.setFrom(from);
    }

    public void alphaTo(float to) {
        mAlpha.setTo(to);
    }

    public float getAlpha(long playTime, float progress) {
        return mAlpha.getCurrentAnimValue(playTime, progress);
    }

    public void alphaDuration(int duration) {
        mAlpha.setDuration(duration);
    }

    public void alphaStartDelay(int delay) {
        mAlpha.setStartDelay(delay);
    }

    public void alphaInterpolator(Interpolator interpolator) {
        mAlpha.setInterpolator(interpolator);
    }
    // ------------------------------ alpha ----------------------------------

    // ------------------------------ translateX ----------------------------------
    public void translateXFrom(int from) {
        mTransX.setFrom(from);
    }

    public void translateXTo(int to) {
        mTransX.setTo(to);
    }

    public int getTranslateX(long playTime, float progress) {
        return mTransX.getCurrentAnimValue(playTime, progress);
    }

    public void translateXDuration(int duration) {
        mTransX.setDuration(duration);
    }

    public void translateXStartDelay(int delay) {
        mTransX.setStartDelay(delay);
    }

    public void translateXInterpolator(Interpolator interpolator) {
        mTransX.setInterpolator(interpolator);
    }
    // ------------------------------ translateX ----------------------------------

    // ------------------------------ translateY ----------------------------------
    public void translateYFrom(int from) {
        mTransY.setFrom(from);
    }

    public void translateYTo(int to) {
        mTransY.setTo(to);
    }

    public int getTranslateY(long playTime, float progress) {
        return mTransY.getCurrentAnimValue(playTime, progress);
    }

    public void translateYDuration(int duration) {
        mTransY.setDuration(duration);
    }

    public void translateYStartDelay(int delay) {
        mTransY.setStartDelay(delay);
    }

    public void translateYInterpolator(Interpolator interpolator) {
        mTransY.setInterpolator(interpolator);
    }
    // ------------------------------ translateY ----------------------------------

    // ------------------------------ scaleX ----------------------------------
    public void scaleXFrom(float from) {
        mScaleX.setFrom(from);
    }

    public void scaleXTo(float to) {
        mScaleX.setTo(to);
    }

    public float getScaleX(long playTime, float progress) {
        return mScaleX.getCurrentAnimValue(playTime, progress);
    }

    public void scaleXDuration(int duration) {
        mScaleX.setDuration(duration);
    }

    public void scaleXStartDelay(int delay) {
        mScaleX.setStartDelay(delay);
    }

    public void scaleXInterpolator(Interpolator interpolator) {
        mScaleX.setInterpolator(interpolator);
    }
    // ------------------------------ scaleX ----------------------------------

    // ------------------------------ scaleY ----------------------------------
    public void scaleYFrom(float from) {
        mScaleY.setFrom(from);
    }

    public void scaleYTo(float to) {
        mScaleY.setTo(to);
    }

    public float getScaleY(long playTime, float progress) {
        return mScaleY.getCurrentAnimValue(playTime, progress);
    }

    public void scaleYDuration(int duration) {
        mScaleY.setDuration(duration);
    }

    public void scaleYStartDelay(int delay) {
        mScaleY.setStartDelay(delay);
    }

    public void scaleYInterpolator(Interpolator interpolator) {
        mScaleY.setInterpolator(interpolator);
    }
    // ------------------------------ scaleY ----------------------------------

    // ------------------------------ rotateX ----------------------------------
    public void rotateXFrom(int from) {
        mRotateX.setFrom(from);
    }

    public void rotateXTo(int to) {
        mRotateX.setTo(to);
    }

    public int getRotateX(long playTime, float progress) {
        return mRotateX.getCurrentAnimValue(playTime, progress);
    }

    public void rotateXDuration(int duration) {
        mRotateX.setDuration(duration);
    }

    public void rotateXStartDelay(int delay) {
        mRotateX.setStartDelay(delay);
    }

    public void rotateXInterpolator(Interpolator interpolator) {
        mRotateX.setInterpolator(interpolator);
    }
    // ------------------------------ rotateX ----------------------------------

    // ------------------------------ rotateY ----------------------------------
    public void rotateYFrom(int from) {
        mRotateY.setFrom(from);
    }

    public void rotateYTo(int to) {
        mRotateY.setTo(to);
    }

    public int getRotateY(long playTime, float progress) {
        return mRotateY.getCurrentAnimValue(playTime, progress);
    }

    public void rotateYDuration(int duration) {
        mRotateY.setDuration(duration);
    }

    public void rotateYStartDelay(int delay) {
        mRotateY.setStartDelay(delay);
    }

    public void rotateYInterpolator(Interpolator interpolator) {
        mRotateY.setInterpolator(interpolator);
    }
    // ------------------------------ rotateY ----------------------------------

    // ------------------------------ rotateZ ----------------------------------
    public void rotateZFrom(int from) {
        mRotateZ.setFrom(from);
    }

    public void rotateZTo(int to) {
        mRotateZ.setTo(to);
    }

    public int getRotateZ(long playTime, float progress) {
        return mRotateZ.getCurrentAnimValue(playTime, progress);
    }

    public void rotateZDuration(int duration) {
        mRotateZ.setDuration(duration);
    }

    public void rotateZStartDelay(int delay) {
        mRotateZ.setStartDelay(delay);
    }

    public void rotateZInterpolator(Interpolator interpolator) {
        mRotateZ.setInterpolator(interpolator);
    }
    // ------------------------------ rotateZ ----------------------------------
}
