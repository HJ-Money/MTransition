package com.mjun.mtransition.anim;

/**
 * @author huijun.zhj
 */
public class ScaleXFactor extends BaseAnimFactor<Float> {

    float mFrom = 1f;
    float mTo = 1f;

    @Override
    public Float getCurrentAnimValue(long playTime, float animProgress) {
        float progress = mInterpolator.getInterpolation(getAnimProgress(playTime, animProgress));
        float result = (mTo - mFrom) * progress + mFrom;
        return result;
    }

    public void setFrom(float mFrom) {
        this.mFrom = mFrom;
    }

    public void setTo(float mTo) {
        this.mTo = mTo;
    }
}
