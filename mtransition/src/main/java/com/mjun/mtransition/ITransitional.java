package com.mjun.mtransition;

/**
 * @author huijun.zhj
 * 动画元素的基础接口，自定义动画必须要实现该接口，请参考Demo5,6,7
 * Basic interface for animated elements,custom Transition must implement this interface,Please refer to Demo5,6,7
 */
public interface ITransitional {

    /**
     * 动画执行时回调
     * Callback when Transition is executed
     * @param playTime  动画已经执行的时长 The time of long the Transition has been performed
     * @param progress  动画已经执行的进度 The progress of the Transition has been performed
     */
    void onTransitProgress(long playTime, float progress);

    /**
     * 动画开始时回调
     * Callback when Transition start
     */
    void onTransitStart();

    /**
     * 动画结束时回调
     * Callback when Transition end
     */
    void onTransitEnd();
}
