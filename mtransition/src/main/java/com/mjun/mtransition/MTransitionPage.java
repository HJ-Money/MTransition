package com.mjun.mtransition;

import java.util.*;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * @author huijun.zhj
 *
 * 包含其中一个页面的动画数据
 * 数据基本单元是{@link MTransitionView}
 *
 * Contains Transition data for FromPage Or ToPage
 * The basic unit of data is {@link MTransitionView}
 */
public class MTransitionPage implements ITransitional {

    /**
     * 页面最外层View对应的{@link MTransitionView}
     * The {@link MTransitionView} corresponding to the outermost View of the page
     */
    private MTransitionView mContainer;
    /**
     * 存储需要做动画的所有View对应的{@link MTransitionView}
     * Store {@link MTransitionView} for Page
     */
    private Map<String, MTransitionView> mViewMap;
    private FrameLayout mContentView;
    /**
     * 是否准备好去执行动画，只有调用{@link #setContainer(View, ITransitPrepareListener)}方法后，该值才为true
     * NOTE：如果该值为false的时候执行动画，则会报错
     *
     * Whether it is ready to start Transition, this value is
     * only true after calling the {@link #setContainer(View, ITransitPrepareListener)} method
     * NOTE:If the Transition is executed when the value is false, an error will be reported
     */
    private boolean mPrepared = false;
    private boolean mIsFromPage = false;

    private ITransitPrepareListener mListener;

    public MTransitionPage(boolean isFromPage, ITransitPrepareListener listener) {
        mIsFromPage = isFromPage;
        mViewMap = new HashMap<>();
        mListener = listener;
    }

    /**
     * 动画执行时回调
     * Callback when Transition is executed
     * @param playTime  动画已经执行的时长
     * @param progress  动画已经执行的进度
     */
    @Override
    public void onTransitProgress(long playTime, float progress) {
        mContainer.onTransitProgress(playTime, progress);
        Iterator<String> iterator = mViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            MTransitionView child = mViewMap.get(iterator.next());
            child.onTransitProgress(playTime, progress);
        }
    }

    /**
     * 动画开始时回调
     * Callback when Transition start
     */
    @Override
    public void onTransitStart() {
        Iterator<String> iterator = mViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            MTransitionView child = mViewMap.get(iterator.next());
            if (child != null) {
                if (child.getSourceView() != null && child.getSourceView() != getContainer().getSourceView()) {
                    child.getSourceView().setVisibility(View.INVISIBLE);
                }
                if (child.mIsTarget) {
                    child.setVisibility(View.INVISIBLE);
                }
            }
        }
        //getContainer().getSourceView().setVisibility(View.INVISIBLE);
        getContainer().contentHasChanged();
    }

    /**
     * 动画结束时回调
     * Callback when Transition end
     */
    @Override
    public void onTransitEnd() {
        //getContainer().getSourceView().setVisibility(View.VISIBLE);
        Iterator<String> iterator = mViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            MTransitionView child = mViewMap.get(iterator.next());
            if (child != null) {
                if (child.getSourceView() != null && child.getSourceView() != getContainer().getSourceView()) {
                    child.getSourceView().setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT < 21) {
                        // 5.0以下
                        child.getSourceView().postInvalidate();
                    }
                }
                if (child.mIsTarget) {
                    child.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    boolean isFullSize(MTransitionView view) {
        if (view.getWidth() == getContainer().getWidth() && view.getHeight() == getContainer().getHeight()) {
            return true;
        }
        return false;
    }

    void invalidate() {
        mContentView.invalidate();
        getContainer().invalidate();
        Iterator<String> iterator = mViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            MTransitionView child = mViewMap.get(iterator.next());
            if (child != null) {
                child.invalidate();
            }
        }
    }

    /**
     * 设置当前动画页的最外层View
     * NOTE：必须要调用该方法之后，才能调用{@link MTransition#start()}或者{@link MTransition#reverse()}或者该类的其他方法,否则会报错
     *
     * Set the Container View of the page
     * NOTE:You must call this method before you can call {@link MTransition#start()} or
     * {@link MTransition#reverse()} or other methods of this class, or you will get an error.
     *
     * @param container
     * @param listener
     */
    public void setContainer(@NonNull final View container, @Nullable final ITransitPrepareListener listener) {
        if (container == null) {
            throw new RuntimeException("param container must be not null");
        }
        /**
         * FIXME：下面代码是用来消除Android自带的Activity转场动画，但是由于华为手机，就是EMUI！！！不知道什么原因对于activityOpenEnterAnimation、activityOpenExitAnimation
         * 等属性不起任何作用，其他手机都正常。所以暂时注释掉下面代码，改为由开发者主动调用{@link MTranstionUtil#removeActivityAnimation(Activity)}来取消Activity动画
         */
        //if (container.getContext() instanceof Activity) {
        //    if (mIsFromPage) {
        //        MTranstionUtil.removeActivityCloseAnimation((Activity)container.getContext());
        //    } else {
        //        MTranstionUtil.removeActivityOpenAnimation((Activity)container.getContext());
        //    }
        //}
        setContainer(container);
        if (container.getMeasuredWidth() != 0 && container.getMeasuredHeight() != 0) {
            prepared(container, listener);
        } else {
            container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    container.getViewTreeObserver().removeOnPreDrawListener(this);
                    prepared(container, listener);
                    return false;
                }
            });
        }
    }

    private void setContainer(final View container) {
        mContentView = new FrameLayout(container.getContext());
        mContainer = new MTransitionView(container);
        mContainer.setBelongPage(this);
        mContainer.setBgColor(Color.WHITE);
        mContentView.addView(mContainer.getContentView());
    }

    private void prepared(final View container, @Nullable final ITransitPrepareListener listener) {
        if (container.getParent() != null) {
            int[] parentLocaiton = new int[2];
            ((View) container.getParent()).getLocationInWindow(parentLocaiton);
            mContainer.setParentLocation(parentLocaiton);
        }
        mPrepared = true;
        mListener.onPrepare(mContainer);
        if (listener != null) {
            listener.onPrepare(mContainer);
        }
    }

    public MTransitionView getContainer() {
        return mContainer;
    }

    /**
     * 添加需要做动画的View
     * NOTE:动画View的深度层次跟添加顺序有关，先添加的在底层，后添加的在顶层，但是可以通过{@link MTransitionView#below(MTransitionView)}
     * 或者{@link MTransitionView#above(MTransitionView)} 调整顺序
     *
     * Add Views that need to transit during Transition
     * NOTE: The depth level of views is related to the order of additions, first added at the
     * bottom level, and later added at the top level, but can be adjusted by
     * {@link MTransitionView#below(MTransitionView)} or {@link MTransitionView#above(MTransitionView)}
     *
     * @param name  View的名字，用于存储时作为Key
     * @param view  需要做动画的View
     * @return  根据View生成一个对应的 {@link MTransitionView}实例
     */
    public MTransitionView addTransitionView(String name, View view) {
        if (mContainer == null) {
            throw new IllegalArgumentException("must setContainer at first");
        }
        if (mViewMap.containsKey(name)) {
            throw new IllegalArgumentException("duplicate view name");
        }
        if (view == mContainer.getSourceView()) {
            mViewMap.put(name, mContainer);
            return mContainer;
        }
        MTransitionView transitionView = new MTransitionView(view);
        transitionView.setBelongPage(this);
        mViewMap.put(name, transitionView);
        mContentView.addView(transitionView.getContentView());
        transitionView.setParentLocation(mContainer.mLocation);
        return transitionView;
    }

    /**
     * 获取name对应的 {@link MTransitionView}实例
     * Get  {@link MTransitionView} instance by name
     *
     * @param name View的名字
     * @return  {@link MTransitionView}实例
     */
    public MTransitionView getTransitionView(String name) {
        return mViewMap.get(name);
    }

    void clear() {
        mPrepared = false;
        mContentView = null;
        mContainer = null;
        mViewMap.clear();
    }

    FrameLayout getContentView() {
        return mContentView;
    }

    void updateLocation() {
        if (mContainer == null) {
            throw new IllegalArgumentException("must setContainer at first");
        }
        mContainer.updateLocation();
        Iterator<String> iterator = mViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            MTransitionView child = mViewMap.get(iterator.next());
            if (child != null) {
                child.updateLocation();
                if (child.mTargetView != null) {
                    child.mTargetView.updateLocation();
                    child.transitTo(child.mTargetView, child.mChangeSize);
                }
            }
        }
    }

    /**
     * 是否已经准备好去执行场景切换动画
     * Is Prepare to excute Transiton
     *
     * @return
     */
    public boolean isPrepared() {
        return mPrepared;
    }

    void aboveOn(MTransitionView src, MTransitionView target) {
        int index = getChildIndex(target.getContentView());
        try {
            mContentView.addView(src.getContentView(), index + 1);
        } catch (Exception e) {
            e.printStackTrace();
            mContentView.addView(src.getContentView());
        }
        src.setBelongPage(this);
    }

    void belowTo(MTransitionView src, MTransitionView target) {
        int index = getChildIndex(target.getContentView());
        try {
            mContentView.addView(src.getContentView(), index);
        } catch (Exception e) {
            e.printStackTrace();
            mContentView.addView(src.getContentView());
        }
        src.setBelongPage(this);
    }

    int getChildIndex(View target) {
        int index = -1;
        int childcount = mContentView.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View child = mContentView.getChildAt(i);
            if (child == target) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 设置该页面的所有 {@link MTransitionView}的动画时长
     * Set the Transition duration of all {@link MTransitionView} for this page
     *
     * @param duration
     */
    void setDuration(long duration) {
        if (mContainer == null) {
            throw new IllegalArgumentException("must setContainer at first");
        }
        mContainer.setDuration(duration, false);
        Iterator<String> iterator = mViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            MTransitionView child = mViewMap.get(iterator.next());
            if (child != null) {
                child.setDuration(duration, false);
            }
        }
    }

    long getMaxAnimTime() {
        if (mContainer == null) {
            throw new IllegalArgumentException("must setContainer at first");
        }
        long maxAnimTime = 0L;
        maxAnimTime = Math.max(maxAnimTime, mContainer.getMaxAnimTime());
        Iterator<String> iterator = mViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            MTransitionView child = mViewMap.get(iterator.next());
            if (child != null) {
                maxAnimTime = Math.max(maxAnimTime, child.getMaxAnimTime());
            }
        }
        return maxAnimTime;
    }
}
