package com.mjun.mtransition;

/**
 * @author huijun.zhj
 *
 * 对{@link OnTransitListener}空实现
 * Empty implementation of {@link OnTransitListener}
 */
public class TransitListenerAdapter implements OnTransitListener {

    @Override
    public void onTransitStart(MTransition transition, boolean reverse) {

    }

    @Override
    public void onTransitProgress(MTransition transition, boolean reverse, float progress) {

    }

    @Override
    public void onTransitEnd(MTransition transition, boolean reverse) {

    }
}
