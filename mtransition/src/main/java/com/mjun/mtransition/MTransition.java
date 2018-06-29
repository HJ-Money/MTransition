package com.mjun.mtransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.mjun.mtransition.bundle.MTransitionBundle;
import com.mjun.mtransition.view.CloneView;
import junit.framework.Assert;

/**
 * @author huijun.zhj
 *
 * 用于存储场景切换动画的所有数据
 * Used to store All data of Transition
 */
public class MTransition implements ITransitPrepareListener {

    /**
     * 该实例的名称
     */
    private String mName;

    private FrameLayout mWrapperView;
    private CloneView mTempGroundLayer;
    /**
     * 存储场景切换动画中，FromPage的动画数据
     * FromPage's transition data
     */
    private MTransitionPage mFromPage;
    /**
     * 存储场景切换动画中，ToPage的动画数据
     * ToPage's transition data
     */
    private MTransitionPage mToPage;
    /**
     * 动画驱动
     * Animator
     */
    private ValueAnimator mAnimator;
    /**
     * 动画是否反向执行
     * Whether the transition is executed in reverse
     */
    private boolean mReversing = false;
    private float mLastProgress = 0f;

    /**
     * 动画监听器
     * Transition Listener
     */
    private OnTransitListener mListener;

    /**
     * 用于传递动画所需要的数据，从FromPage到ToPage的传递
     * The data used to pass during Transition, passing from FromPage to ToPage
     */
    private MTransitionBundle mBundle;

    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    private boolean startWhenPrepared = false;

    MTransition(String name) {
        mName = name;
        mFromPage = new MTransitionPage(true, this);
        mToPage = new MTransitionPage(false, this);
        TransitionListener animListener = new TransitionListener();
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.addListener(animListener);
        mAnimator.addUpdateListener(animListener);
        mBundle = new MTransitionBundle();
    }

    /**
     * 获取FromPage的动画数据
     * Get FromPage's Transition data
     *
     * @return {@link MTransition}实例对象
     */
    public @NonNull MTransitionPage fromPage() {
        return mFromPage;
    }

    /**
     * 获取ToPage的动画数据
     * Get ToPage's Transition data
     *
     * @return {@link MTransition}实例对象
     */
    public @NonNull MTransitionPage toPage() {
        return mToPage;
    }

    /**
     * 从头开始执行场景切换动画
     * NOTE：必须先调用{@link MTransitionPage#setContainer(View, ITransitPrepareListener)}之后，才能调用该方法,否则会报错
     *
     * Start the Transition
     * NOTE: You must call {@link MTransitionPage#setContainer(View, ITransitPrepareListener)} before
     * calling this method; or will throw an exception
     */
    public void start() {
        checkContainerSetted();
        if (isPrepared()) {
            start(0f);
        } else {
            startWhenPrepared = true;
        }
    }

    /**
     * 从一定进度开始执行场景切换动画
     * Start the Transition from a certain progress
     *
     * @param progress 动画执行的起步进度，取值范围[0,1]
     */
    private void start(float progress) {
        if (duringTransition() || isActivityFinishing()) {
            return;
        }
        Log.e("lifecycle", "start : " + progress);
        mReversing = false;
        mLastProgress = progress;
        preTranistion(true, progress != 0f);
        //mFromPage.getContentView().setAlpha(0.99f);
        if (Build.VERSION.SDK_INT <= 21) {
            // 5.0以下
            mAnimator.setFloatValues(progress, 1f);
        }
        if (progress == 0f) {
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("lifecycle", "post start : ");
                    mAnimator.start();
                }
            });
        } else {
            mAnimator.start();
        }
    }

    /**
     * 从当前动画进度反向执行动画
     * NOTE：必须先调用{@link MTransitionPage#setContainer(View, ITransitPrepareListener)}之后，才能调用该方法,否则会报错
     *
     * Reverse the Transiton from current progress
     * NOTE: You must call {@link MTransitionPage#setContainer(View, ITransitPrepareListener)} before
     * calling this method; or will throw an exception
     */
    public void reverse() {
        checkContainerSetted();
        if (isActivityFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT <= 21) {
            // 5.0以下
            mAnimator.setFloatValues(0f, mLastProgress);
        }
        if (!duringTransition()) {
            preTranistion(false, true);
            mReversing = true;
            mAnimator.reverse();
        } else {
            if (!mReversing) {
                mReversing = true;
                mAnimator.reverse();
            }
        }
    }

    private boolean isActivityFinishing() {
        return isActivityFinishing(mFromPage) || isActivityFinishing(mToPage);
    }

    /**
     * MTransitionPage 对应的Activity是否正在finish
     *
     * Whether the Activity corresponding to MTransitionPage is finishing
     *
     * @param page
     * @return
     */
    private boolean isActivityFinishing(MTransitionPage page) {
        if (page.getContainer().getSourceView().getContext() instanceof Activity) {
            return ((Activity) page.getContainer().getSourceView().getContext()).isFinishing();
        }
        return false;
    }

    /**
     * 如果需要跟手滑动执行动画，那么必须在开始跟手的时候执行该方法
     * 具体请参考Demo示例
     *
     * If you need to follow the touch event to perform the Transiton,
     * you must call this method at the beginning
     * For details, see the Demos
     */
    public void onBeginDrag() {
        Log.e("lifecycle", "onBeginDrag : " + mLastProgress);
        mReversing = false;
        preTranistion(false, true);
        mToPage.onTransitStart();
        mFromPage.onTransitStart();
        if (Build.VERSION.SDK_INT <= 21) {
            // 5.0以下
            mAnimator.setFloatValues(0f, 1f);
        }
    }

    private void preTranistion(boolean addGroundLayer, boolean needPreAdd) {
        checkPrepared();
        cancel();
        removeAllPage();
        ViewGroup rootView = getRootView(mToPage);
        initWrapperView(rootView);
        if (addGroundLayer) {
            addTempGroundLayer();
        }
        if (needPreAdd) {
            addPageContentView(mFromPage);
            addPageContentView(mToPage);
        }
        updateLocation();
        setProgress(mLastProgress);
    }

    private void addTempGroundLayer() {
        removeTempGroundLayer();
        mTempGroundLayer = new CloneView(mWrapperView.getContext());
        mTempGroundLayer.setUseBitmap(true);
        mTempGroundLayer.setSourceView(mFromPage.getContainer().getSourceView());
        mWrapperView.addView(mTempGroundLayer);
    }

    private void removeTempGroundLayer() {
        if (mTempGroundLayer != null && mTempGroundLayer.getParent() != null) {
            ((ViewGroup) mTempGroundLayer.getParent()).removeView(mTempGroundLayer);
        }
    }

    /**
     * 从当前的动画进度，正向执行结束，一般在跟手操作使用，{@link #onBeginDrag()}
     *
     * Continue Transition from the current progress,
     * generally used when  you need to follow the touch event, {@link #onBeginDrag()}
     */
    public void gotoFloor() {
        start(mLastProgress);
    }

    /**
     * 从当前的动画进度，反向执行结束，一般在跟手操作使用，{@link #onBeginDrag()}
     *
     * Reverse Transition from the current progress,
     * generally used when  you need to follow the touch event, {@link #onBeginDrag()}
     */
    public void gotoCeil() {
        reverse();
    }

    /**
     * FromPage和ToPage都必须先执行{@link MTransitionPage#setContainer(View, ITransitPrepareListener)}
     *
     * Both FromPage and ToPage must execute {@link MTransitionPage#setContainer(View, ITransitPrepareListener)} first
     */
    private void checkPrepared() {
        if (!mFromPage.isPrepared() || !mToPage.isPrepared()) {
            throw new RuntimeException("cannot run transition before fromPage and toPage prepared!");
        }
    }

    private boolean isPrepared() {
        return mFromPage.isPrepared() && mToPage.isPrepared();
    }

    private void checkContainerSetted() {
        if (mFromPage.getContainer() == null || mToPage.getContainer() == null) {
            throw new RuntimeException("cannot run transition before fromPage and toPage setContainer!");
        }
    }

    private boolean isContainerSetted() {
        return mFromPage.getContainer() != null && mToPage.getContainer() != null;
    }

    private ViewGroup getRootView(MTransitionPage page) {
        return (ViewGroup) page.getContainer().getSourceView().getRootView().findViewById(android.R.id.content);
    }

    private void initWrapperView(ViewGroup rootView) {
        if (mWrapperView == null) {
            mWrapperView = new FrameLayout(rootView.getContext()) {

                // 不允许点击
                @Override
                public boolean onInterceptTouchEvent(MotionEvent ev) {
                    return true;
                }

                @Override
                public boolean onTouchEvent(MotionEvent event) {
                    return true;
                }
            };
            mWrapperView.setBackgroundColor(Color.WHITE);
        }
        if (mWrapperView.getParent() == null) {
            rootView.addView(mWrapperView);
        }
    }

    /**
     * 清除保存的动画数据
     * Clear all data of Transition
     */
    public void clear() {
        mToPage.clear();
        mFromPage.clear();
    }

    /**
     * 设置当前动画的进度
     *
     * Set current Transition progress
     *
     * @param progress 动画执行进度，取值范围[0, 1]
     */
    public void setProgress(float progress) {
        Log.e("lifecycle", "setProgress : " + progress);
        if (!mReversing) {
            mAnimator.setCurrentPlayTime((long) (progress * mAnimator.getDuration()));
        }
    }

    /**
     * 取消场景切换动画
     *
     * Cancel Transition
     */
    public void cancel() {
        Log.e("lifecycle", "cancel : ");
        mAnimator.cancel();
    }

    private boolean duringTransition() {
        return mAnimator.isRunning();
    }

    /**
     * 统一设置所有动画的动画时长，如果之前已经在{@link BaseAnimFactor#setDuration(long)}单独设置过，则以单独设置为准
     * NOTE：必须先调用{@link MTransitionPage#setContainer(View, ITransitPrepareListener)}之后，才能调用该方法,否则会报错
     *
     * Set the duration of all animations uniformly. If you have previously set
     * it separately in {@link BaseAnimFactor#setDuration(long)}, the individual settings will prevail.
     * NOTE: You must call {@link MTransitionPage#setContainer(View, ITransitPrepareListener)} before
     * calling this method; or will throw an exception
     *
     * @param duration 动画时长
     */
    public void setDuration(long duration) {
        mFromPage.setDuration(duration);
        mToPage.setDuration(duration);
        long maxAnimTime = Math.max(duration, mFromPage.getMaxAnimTime());
        maxAnimTime = Math.max(maxAnimTime, mToPage.getMaxAnimTime());
        mAnimator.setDuration(maxAnimTime);
    }

    /**
     * 获取整个场景动画时长
     * Get the duration of Transition
     *
     * @return 动画时长
     */
    public long getDuration() {
        return mAnimator.getDuration();
    }

    /**
     * 场景切换动画是否正在执行
     * Wheater Transition is running
     *
     * @return
     */
    public boolean isRunning() {
        return mAnimator.isRunning();
    }

    void updateLocation() {
        mFromPage.updateLocation();
        mToPage.updateLocation();
    }

    @Override
    public void onPrepare(MTransitionView container) {
        if (isPrepared() && startWhenPrepared) {
            startWhenPrepared = false;
            start();
        }
    }

    private class TransitionListener extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationCancel(Animator animation) {
            Log.e("lifecycle", "onAnimationCancel : " + animation);
            removeAllPage();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            Log.e("lifecycle", "onAnimationEnd : " + animation);
            if (!mReversing) {
                removeAllPage();
                mToPage.onTransitEnd();
                mFromPage.onTransitEnd();
            } else {
                if (getRootView(mToPage) == getRootView(mFromPage)) {
                    // 同一个Activity的要移除View
                    removeAllPage();
                } else {
                    addTempGroundLayer();
                }
                mFromPage.onTransitEnd();
            }
            if (mListener != null) {
                mListener.onTransitEnd(MTransition.this, mReversing);
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
            Log.e("lifecycle", "onAnimationStart : " + ((ValueAnimator)animation).getAnimatedValue());
            Assert.assertNotNull(mWrapperView);
            mToPage.onTransitStart();
            mFromPage.onTransitStart();
            if (mListener != null) {
                mListener.onTransitStart(MTransition.this, mReversing);
            }
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    removeTempGroundLayer();
                    addPageContentView(mFromPage);
                    addPageContentView(mToPage);
                }
            });
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Log.e("lifecycle", "onAnimationUpdate : " + animation.getAnimatedValue());
            float progress = (float) animation.getAnimatedValue();
            mLastProgress = progress;
            long playTime = (long) (progress * getDuration());
            mFromPage.onTransitProgress(playTime, progress);
            mToPage.onTransitProgress(playTime, progress);
            if (mListener != null) {
                mListener.onTransitProgress(MTransition.this, mReversing, progress);
            }
        }
    }

    private void removeAllPage() {
        Log.e("lifecycle", "removeAllPage : ");
        removePageContentView(mToPage);
        removePageContentView(mFromPage);
        if (mWrapperView != null && mWrapperView.getParent() instanceof ViewGroup) {
            ((ViewGroup) mWrapperView.getParent()).removeView(mWrapperView);
            mWrapperView = null;
        }
    }

    private void removePageContentView(MTransitionPage page) {
        if (page.getContentView() != null && page.getContentView().getParent() instanceof ViewGroup) {
            ((ViewGroup) page.getContentView().getParent()).removeView(page.getContentView());
        }
    }

    private void addPageContentView(MTransitionPage page) {
        if (page.getContentView() != null && page.getContentView().getParent() == null && mWrapperView != null) {
            mWrapperView.addView(page.getContentView());
        }
    }

    /**
     * 获取当前MTransition对应的名字
     *
     * Get the name of Transition
     *
     * @return 当前MTransition对应的名字
     */
    public String getName() {
        return mName;
    }

    /**
     * 设置动画监听器
     *
     * Set the OnTransitListener to Transition
     *
     * @param listener {@link OnTransitListener}
     */
    public void setOnTransitListener(OnTransitListener listener) {
        this.mListener = listener;
    }

    /**
     * 获取传递数据的Bundle
     *
     * Get the Bundle Passing Data
     *
     * @return {@link MTransitionBundle}，用法如{@link android.os.Bundle}一致
     */
    public MTransitionBundle getBundle() {
        return mBundle;
    }
}
