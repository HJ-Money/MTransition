package com.mjun.mtransition.anim;

import android.view.animation.Interpolator;

/**
 * @author huijun.zhj
 */
public class BaseAnimFactor<T> implements IAnimFactor {

    Interpolator mInterpolator = IAnimFactor.sBaseInterpolator;
    long mDuration = 0;
    long mStartDelay = 0;
    boolean mHasSet = false;

    @Override
    public T getCurrentAnimValue(long playTime, float animProgress) {
        return null;
    }

    public void setInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    public void setDuration(long duration) {
        setDuration(duration, true);
    }

    void setDuration(long duration, boolean byUser) {
        if (!mHasSet) {
            mHasSet = true;
        } else if (!byUser) {
            return;
        }
        mDuration = duration;
    }

    public void setStartDelay(long startDelay) {
        this.mStartDelay = startDelay;
    }

    /**
     * 获取真实的动画进度，考虑Duration和StartDelay的因素
     * @param playTime
     * @param progress
     * @return
     */
    float getAnimProgress(long playTime, float progress) {
        if (mDuration == 0) {
            return progress;
        }
        long time = playTime;
        if (mStartDelay > 0) {
            time = playTime - mStartDelay;
        }
        if (time < 0) {
            // 还没开始动画
            return 0f;
        } else if (time > mDuration) {
            // 动画已经结束
            return 1f;
        } else {
            return time / (float) mDuration;
        }
    }

    long getAnimTime() {
        return mDuration + mStartDelay;
    }
}
