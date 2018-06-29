package com.mjun.mtransition;

import android.view.View;

/**
 * @author huijun.zhj
 * {@link MTransitionPage#setContainer(View, ITransitPrepareListener)}参数所用
 * {@link MTransitionPage#setContainer(View, ITransitPrepareListener)} parameters used
 */
public interface ITransitPrepareListener {
    /**
     * 当 {@link MTransitionPage} 设置好Container时回调
     * Callback when {@link MTransitionPage} sets Container
     *
     * @param container
     */
    void onPrepare(MTransitionView container);
}
