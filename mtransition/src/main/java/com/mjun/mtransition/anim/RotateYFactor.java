package com.mjun.mtransition.anim;

/**
 * @author huijun.zhj
 */
public class RotateYFactor extends BaseAnimFactor<Integer> {

    int mFrom = 0;
    int mTo = 0;

    @Override
    public Integer getCurrentAnimValue(long playTime, float animProgress) {
        float progress = mInterpolator.getInterpolation(getAnimProgress(playTime, animProgress));
        int result = (int) ((mTo - mFrom) * progress + mFrom);
        return result;
    }

    public void setFrom(int mFrom) {
        this.mFrom = mFrom;
    }

    public void setTo(int mTo) {
        this.mTo = mTo;
    }
}
