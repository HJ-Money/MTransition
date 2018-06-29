package com.mjun.mtransition;

/**
 * @author huijun.zhj
 *
 * 动画监听器，通过{@link MTransition#setOnTransitListener(OnTransitListener)}设置
 * Set via {@link MTransition#setOnTransitListener(OnTransitListener)}
 */
public interface OnTransitListener {
    /**
     * 当动画开始时回调
     * Callback when Transition start
     *
     * @param transition    动画对应的{@link MTransition}实例
     * @param reverse       动画是否反向进行
     */
    void onTransitStart(MTransition transition, boolean reverse);

    /**
     * 动画进度回调
     * Callback when Transition progress updated
     *
     * @param transition    动画对应的{@link MTransition}实例
     * @param reverse       动画是否反向进行
     * @param progress      当前的动画进度
     */
    void onTransitProgress(MTransition transition, boolean reverse, float progress);

    /**
     * 当动画结束时回调
     * Callback when Transition end
     *
     * @param transition    动画对应的{@link MTransition}实例
     * @param reverse       动画是否反向进行
     */
    void onTransitEnd(MTransition transition, boolean reverse);
}
