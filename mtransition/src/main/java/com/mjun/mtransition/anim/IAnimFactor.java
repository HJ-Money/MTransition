package com.mjun.mtransition.anim;

import android.view.animation.LinearInterpolator;

/**
 * @author huijun.zhj
 */
public interface IAnimFactor<T> {

    LinearInterpolator sBaseInterpolator = new LinearInterpolator();

    T getCurrentAnimValue(long playTime, float animProgress);
}
