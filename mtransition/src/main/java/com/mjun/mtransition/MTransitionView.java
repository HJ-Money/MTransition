package com.mjun.mtransition;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import com.mjun.mtransition.anim.Transition;
import com.mjun.mtransition.view.CloneView;
import junit.framework.Assert;

/**
 * @author huijun.zhj
 *
 * 动画数据的基本单位，每一个MTransitionView都有对应的一个View
 * 如果你的View需要在场景切换时做动画，则通过 {@link MTransitionPage#addTransitionView(String, View)}生成 {@link MTransitionView}实例
 * 同时可以调用该类的其他函数，如 {@link #alpha(float, float)}、 {@link #translateX(int, int)}等api，设置执行的动画效果
 *
 * The basic unit of Transiton data, each MTransitionView has a corresponding View
 * If your View needs to transit during scene change(Activity change), generate a {@link MTransitionView}
 * instance via {@link MTransitionPage#addTransitionView(String, View)}
 * And you  can call other methods of this class, such as {@link #alpha(float, float)},
 * {@link #translateX(int, int)} and other api, to set the Transition effect
 */
public class MTransitionView implements ITransitional {

    /**
     * 存储该View执行的动画路径数据
     * Store the Transition path data of the View
     */
    private Transition mTransition = new Transition();
    private View mContent = null;
    /**
     * 该MTransitionView对应真正的View
     *
     * The view which the MTransitionView corresponds to
     */
    private View mSourceView = null;
    /**
     * {@link #mSourceView} 在屏幕上的位置信息
     * {@link #mSourceView} location on the screen
     */
    int[] mLocation = new int[2];
    int[] mParentLocation = null;
    int mInitTransX = 0;
    int mInitTransY = 0;

    /**
     * 调用 {@link #transitTo(MTransitionView)} 所需要
     * Required for calling {@link #transitTo(MTransitionView)}
     */
    MTransitionView mTargetView;
    /**
     * 该MTransitionView是否其他MTransitionView的动画目标
     * Whether the MTransitionView is the transit target of other MTransitionView
     */
    boolean mIsTarget = false;
    boolean mChangeSize = false;

    MTransitionPage mBelongPage;

    public MTransitionView(View source) {
        Assert.assertNotNull(source);
        mSourceView = source;
        mContent = new CloneView(source.getContext());
        // TODO:处理有replace的情况
        ((CloneView) mContent).setSourceView(source);
        source.getLocationInWindow(mLocation);
    }

    /**
     * 动画执行时回调
     * Callback when Transition is executed
     * @param playTime  动画已经执行的时长 The time of long the Transition has been performed
     * @param progress  动画已经执行的进度 The progress of the Transition has been performed
     */
    @Override
    public void onTransitProgress(long playTime, float progress) {
        Log.d("zhj2", "onTransitProgress : " + this + "   " + mContent);
        float alpha = mTransition.getAlpha(playTime, progress);
        mContent.setAlpha(alpha);
        float scaleX = mTransition.getScaleX(playTime, progress);
        mContent.setScaleX(scaleX);
        float scaleY = mTransition.getScaleY(playTime, progress);
        mContent.setScaleY(scaleY);
        int transX = mTransition.getTranslateX(playTime, progress);
        mContent.setTranslationX(mInitTransX + transX);
        int transY = mTransition.getTranslateY(playTime, progress);
        mContent.setTranslationY(mInitTransY + transY);
        int rotateX = mTransition.getRotateX(playTime, progress);
        mContent.setRotationX(rotateX);
        int rotateY = mTransition.getRotateY(playTime, progress);
        mContent.setRotationY(rotateY);
        int rotateZ = mTransition.getRotateZ(playTime, progress);
        mContent.setRotation(rotateZ);
        if (mContent instanceof ITransitional) {
            ((ITransitional) mContent).onTransitProgress(playTime, progress);
        }
        invalidate();
    }

    void invalidate() {
        mContent.invalidate();
    }

    /**
     * 动画开始时回调
     * Callback when Transition start
     */
    @Override
    public void onTransitStart() {
    }

    /**
     * 动画结束时回调
     * Callback when Transition end
     */
    @Override
    public void onTransitEnd() {
    }

    /**
     * @see #replaceBy(ITransitional, LayoutParams)
     */
    public MTransitionView replaceBy(ITransitional transView) {
        return replaceBy(transView, null);
    }

    /**
     * 如果你的动画需要自定义，则需要把自定义的动画在{@link ITransitional}实现后，调用该方法，替换原生的View
     * NOTE:必须先调用该方法才可以调用below、above方法调整顺序
     *
     * If your Transition needs customization, you need to call this method after the
     * {@link ITransitional} implementation of the custom view and replace the native View
     * see the Demo5,6,7
     * NOTE: This method must be called before you call the below and above methods to adjust the order
     *
     * @param transView     实现自定义动画的View
     * @param lp      LayoutParams只需要设置宽高即可，其他参数都会被忽略（WRAP_CONTENT,MATCH_PATENT都可用）；如果该值为null，则默认取被替换的View的宽高
     * @return  对应的 {@link MTransitionView}实例
     */
    public MTransitionView replaceBy(ITransitional transView, @NonNull LayoutParams lp) {
        if (!(transView instanceof View)) {
            throw new IllegalArgumentException("transView 必须是View");
        }
        if (mContent.getParent() != null) {
            ViewGroup parent = ((ViewGroup) mContent.getParent());
            parent.removeView(mContent);
            mContent = (View) transView;
            if (lp != null) {
                parent.addView(mContent, new LayoutParams(lp.width, lp.height));
            } else {
                int width = mSourceView.getMeasuredWidth();
                int height = mSourceView.getMeasuredHeight();
                parent.addView(mContent, new LayoutParams(width, height));
            }
            if (mParentLocation != null) {
                setParentLocation(mParentLocation);
            }
        }
        return this;
    }

    /**
     * 默认情况下，动画的元素是通过重新绘制一遍，让View在动画中展示
     * 但是存在一些特殊情况，重新绘制会没有效果，这时可以尝试调用这个方法，设置为true
     * 该方法让动画元素不是直接绘制出来，而是绘制到一张Bitmap上，再用Bitmap绘制出来
     *
     * @param b     是否需要用Bitmap绘制
     * @return
     */
    public MTransitionView setUseBitmap(boolean b) {
        if (mContent instanceof CloneView) {
            ((CloneView) mContent).setUseBitmap(b);
        }
        return this;
    }

    /**
     * 设置旋转或者放大的中心点
     * @see View#setPivotX(float)
     */
    public MTransitionView setPivotX(float pivotX) {
        mContent.setPivotX(pivotX);
        return this;
    }

    /**
     * 设置旋转或者放大的中心点
     * @see View#setPivotY(float)
     */
    public MTransitionView setPivotY(float pivotY) {
        mContent.setPivotY(pivotY);
        return this;

    }

    /**
     * 返回对应的View
     * @return
     */
    public View getSourceView() {
        return mSourceView;
    }

    View getContentView() {
        return mContent;
    }

    void setParentLocation(int[] parentLocation) {
        mParentLocation = parentLocation;
        mInitTransX = mLocation[0];
        mInitTransY = mLocation[1] - mParentLocation[1];
        mContent.setTranslationX(mInitTransX);
        mContent.setTranslationY(mInitTransY);
    }

    void contentHasChanged() {
        if (mContent instanceof CloneView) {
            ((CloneView) mContent).hasChanged();
        }
    }

    /**
     * @see #transitTo(MTransitionView, boolean)
     */
    public MTransitionView transitTo(MTransitionView view) {
        return transitTo(view, true);
    }

    /**
     * 以 view 作为目标，按照它的位置和大小，进行位移动画和缩放动画（只会按照宽度等比缩放）
     *
     * @param view  动画的目标View
     * @param changSize     是否要做缩放动画，缩放到view对应的大小
     * @return
     */
    public MTransitionView transitTo(MTransitionView view, boolean changSize) {
        mTargetView = view;
        mTargetView.mIsTarget = true;
        mChangeSize = changSize;
        translateTo(view);
        if (changSize) {
            changSizeTo(view);
        }
        return this;
    }

    /**
     * 以 view 作为目标，按照它的位置，进行位移动画
     *
     * @param view  动画的目标View
     * @return
     */
    public MTransitionView translateTo(MTransitionView view) {
        translateXTo(mTargetView.mLocation[0] - mLocation[0]);
        translateYTo(mTargetView.mLocation[1] - mLocation[1]);
        return this;
    }

    /**
     * 以 view 作为目标，按照它的大小，进行缩放动画，但只会按照宽度等比缩放
     *
     * @param view  动画的目标View
     * @return
     */
    public MTransitionView changSizeTo(MTransitionView view) {
        int targetWidth = 0;
        if (view.getSourceView() != null) {
            targetWidth = view.getSourceView().getWidth();
        } else {
            targetWidth = view.getContentView().getWidth();
        }
        int selfWidth = 0;
        if (getSourceView() != null) {
            selfWidth = getSourceView().getWidth();
        } else {
            selfWidth = getContentView().getWidth();
        }
        if (targetWidth != 0 && selfWidth != 0) {
            setPivotX(0);
            setPivotY(0);
            float scale = (float) targetWidth / selfWidth;
            scaleXTo(scale);
            scaleYTo(scale);
        }
        return this;
    }

    void setVisibility(int visibility) {
        mContent.setVisibility(visibility);
    }

    void updateLocation() {
        if (getSourceView() != null) {
            getSourceView().getLocationInWindow(mLocation);
            mContent.setScrollX(getSourceView().getScrollX());
            mContent.setScrollY(getSourceView().getScrollY());
        }
        if (mParentLocation != null) {
            setParentLocation(mParentLocation);
        }
    }

    /**
     * 设置动画View的背景颜色，在某些情况下，调用这个方法可以让动画更加完美
     * NOTE:默认是白色的
     *
     * @param color
     * @return
     */
    public MTransitionView setBgColor(int color) {
        if (mContent instanceof CloneView) {
            ((CloneView) mContent).setBgColor(color);
        }
//        mContent.setBackgroundColor(color);
        return this;
    }

    // ----------------------------------- 动画元素 start ----------------------------------

    /**
     * 设置透明度动画
     *
     * @param from  取值范围[0, 1]
     * @param to    取值范围[0, 1]
     * @return
     */
    public MTransitionView alpha(float from, float to) {
        mTransition.alphaFrom(from);
        mTransition.alphaTo(to);
        return this;
    }

    /**
     * @see #alpha(float, float)
     */
    public MTransitionView alphaFrom(float from) {
        mTransition.alphaFrom(from);
        return this;
    }

    /**
     * @see #alpha(float, float)
     */
    public MTransitionView alphaTo(float to) {
        mTransition.alphaTo(to);
        return this;
    }

    /**
     * 设置 {@link #alpha(float, float)} 动画的时长
     *
     * @param duration  动画时长
     * @return
     */
    public MTransitionView alphaDuration(int duration) {
        mTransition.alphaDuration(duration);
        return this;
    }

    /**
     * 设置 {@link #alpha(float, float)} 动画的延时启动时间
     *
     * @param delay     动画的延时启动时间
     * @return
     */
    public MTransitionView alphaStartDelay(int delay) {
        mTransition.alphaStartDelay(delay);
        return this;
    }

    /**
     * 设置 {@link #alpha(float, float)} 动画的差值器
     *
     * @param interpolator  动画差值器
     * @return
     */
    public MTransitionView alphaInterpolator(Interpolator interpolator) {
        mTransition.alphaInterpolator(interpolator);
        return this;
    }

    /**
     * 设置X轴位移动画
     *
     * @param from  动画开始时的X值
     * @param to    动画结束时的X值
     * @return
     */
    public MTransitionView translateX(int from, int to) {
        mTransition.translateXFrom(from);
        mTransition.translateXTo(to);
        return this;
    }

    /**
     * @see #translateX(int, int)
     */
    public MTransitionView translateXFrom(int from) {
        mTransition.translateXFrom(from);
        return this;
    }

    /**
     * @see #translateX(int, int)
     */
    public MTransitionView translateXTo(int to) {
        mTransition.translateXTo(to);
        return this;
    }

    /**
     * 设置 {@link #translateX(int, int)} 动画的时长
     *
     * @param duration  动画时长
     * @return
     */
    public MTransitionView translateXDuration(int duration) {
        mTransition.translateXDuration(duration);
        return this;
    }

    /**
     * 设置 {@link #translateX(int, int)} 动画的延时启动时间
     *
     * @param delay     动画的延时启动时间
     * @return
     */
    public MTransitionView translateXStartDelay(int delay) {
        mTransition.translateXStartDelay(delay);
        return this;
    }

    /**
     * 设置 {@link #translateX(int, int)} 动画的差值器
     *
     * @param interpolator  动画差值器
     * @return
     */
    public MTransitionView translateXInterpolator(Interpolator interpolator) {
        mTransition.translateXInterpolator(interpolator);
        return this;
    }


    /**
     * 设置Y轴位移动画
     *
     * @param from  动画开始时的Y值
     * @param to    动画结束时的Y值
     * @return
     */
    public MTransitionView translateY(int from, int to) {
        mTransition.translateYFrom(from);
        mTransition.translateYTo(to);
        return this;
    }

    /**
     * @see #translateY(int, int)
     */
    public MTransitionView translateYFrom(int from) {
        mTransition.translateYFrom(from);
        return this;
    }

    /**
     * @see #translateY(int, int)
     */
    public MTransitionView translateYTo(int to) {
        mTransition.translateYTo(to);
        return this;
    }

    /**
     * 设置 {@link #translateY(int, int)} 动画的时长
     *
     * @param duration  动画时长
     * @return
     */
    public MTransitionView translateYDuration(int duration) {
        mTransition.translateYDuration(duration);
        return this;
    }

    /**
     * 设置 {@link #translateY(int, int)} 动画的延时启动时间
     *
     * @param delay     动画的延时启动时间
     * @return
     */
    public MTransitionView translateYStartDelay(int delay) {
        mTransition.translateYStartDelay(delay);
        return this;
    }

    /**
     * 设置 {@link #translateY(int, int)} 动画的差值器
     *
     * @param interpolator  动画差值器
     * @return
     */
    public MTransitionView translateYInterpolator(Interpolator interpolator) {
        mTransition.translateYInterpolator(interpolator);
        return this;
    }

    /**
     * 设置X轴的缩放动画
     *
     * @param from  取值范围[0, 1]
     * @param to    取值范围[0, 1]
     * @return
     */
    public MTransitionView scaleX(float from, float to) {
        mTransition.scaleXFrom(from);
        mTransition.scaleXTo(to);
        return this;
    }

    /**
     * @see #scaleX(float, float)
     */
    public MTransitionView scaleXFrom(float from) {
        mTransition.scaleXFrom(from);
        return this;
    }

    /**
     * @see #scaleX(float, float)
     */
    public MTransitionView scaleXTo(float to) {
        mTransition.scaleXTo(to);
        return this;
    }

    /**
     * 设置 {@link #scaleX(float, float)} 动画的时长
     *
     * @param duration  动画时长
     * @return
     */
    public MTransitionView scaleXDuration(int duration) {
        mTransition.scaleXDuration(duration);
        return this;
    }

    /**
     * 设置 {@link #scaleX(float, float)} 动画的延时启动时间
     *
     * @param delay     动画的延时启动时间
     * @return
     */
    public MTransitionView scaleXStartDelay(int delay) {
        mTransition.scaleXStartDelay(delay);
        return this;
    }

    /**
     * 设置 {@link #scaleX(float, float)} 动画的差值器
     *
     * @param interpolator  动画差值器
     * @return
     */
    public MTransitionView scaleXInterpolator(Interpolator interpolator) {
        mTransition.scaleXInterpolator(interpolator);
        return this;
    }

    /**
     * 设置Y轴的缩放动画
     *
     * @param from  取值范围[0, 1]
     * @param to    取值范围[0, 1]
     * @return
     */
    public MTransitionView scaleY(float from, float to) {
        mTransition.scaleYFrom(from);
        mTransition.scaleYTo(to);
        return this;
    }

    /**
     * @see #scaleY(float, float)
     */
    public MTransitionView scaleYFrom(float from) {
        mTransition.scaleYFrom(from);
        return this;
    }

    /**
     * @see #scaleY(float, float)
     */
    public MTransitionView scaleYTo(float to) {
        mTransition.scaleYTo(to);
        return this;
    }

    /**
     * 设置 {@link #scaleY(float, float)} 动画的时长
     *
     * @param duration  动画时长
     * @return
     */
    public MTransitionView scaleYDuration(int duration) {
        mTransition.scaleYDuration(duration);
        return this;
    }

    /**
     * 设置 {@link #scaleY(float, float)} 动画的延时启动时间
     *
     * @param delay     动画的延时启动时间
     * @return
     */
    public MTransitionView scaleYStartDelay(int delay) {
        mTransition.scaleYStartDelay(delay);
        return this;
    }

    /**
     * 设置 {@link #scaleY(float, float)} 动画的差值器
     *
     * @param interpolator  动画差值器
     * @return
     */
    public MTransitionView scaleYInterpolator(Interpolator interpolator) {
        mTransition.scaleYInterpolator(interpolator);
        return this;
    }

    /**
     * 设置X轴的旋转动画
     *
     * @param from
     * @param to
     * @return
     */
    public MTransitionView rotateX(int from, int to) {
        mTransition.rotateXFrom(from);
        mTransition.rotateXTo(to);
        return this;
    }

    /**
     * @see #rotateX(int, int)
     */
    public MTransitionView rotateXFrom(int from) {
        mTransition.rotateXFrom(from);
        return this;
    }

    /**
     * @see #rotateX(int, int)
     */
    public MTransitionView rotateXTo(int to) {
        mTransition.rotateXTo(to);
        return this;
    }

    /**
     * 设置 {@link #rotateX(int, int)} 动画的时长
     *
     * @param duration  动画时长
     * @return
     */
    public MTransitionView rotateXDuration(int duration) {
        mTransition.rotateXDuration(duration);
        return this;
    }

    /**
     * 设置 {@link #rotateX(int, int)} 动画的延时启动时间
     *
     * @param delay     动画的延时启动时间
     * @return
     */
    public MTransitionView rotateXStartDelay(int delay) {
        mTransition.rotateXStartDelay(delay);
        return this;
    }

    /**
     * 设置 {@link #rotateX(int, int)} 动画的差值器
     *
     * @param interpolator  动画差值器
     * @return
     */
    public MTransitionView rotateXInterpolator(Interpolator interpolator) {
        mTransition.rotateXInterpolator(interpolator);
        return this;
    }


    /**
     * 设置Y轴的旋转动画
     *
     * @param from
     * @param to
     * @return
     */
    public MTransitionView rotateY(int from, int to) {
        mTransition.rotateYFrom(from);
        mTransition.rotateYTo(to);
        return this;
    }

    /**
     * @see #rotateY(int, int)
     */
    public MTransitionView rotateYFrom(int from) {
        mTransition.rotateYFrom(from);
        return this;
    }

    /**
     * @see #rotateY(int, int)
     */
    public MTransitionView rotateYTo(int to) {
        mTransition.rotateYTo(to);
        return this;
    }

    /**
     * 设置 {@link #rotateY(int, int)} 动画的时长
     *
     * @param duration  动画时长
     * @return
     */
    public MTransitionView rotateYDuration(int duration) {
        mTransition.rotateYDuration(duration);
        return this;
    }

    /**
     * 设置 {@link #rotateY(int, int)} 动画的延时启动时间
     *
     * @param delay     动画的延时启动时间
     * @return
     */
    public MTransitionView rotateYStartDelay(int delay) {
        mTransition.rotateYStartDelay(delay);
        return this;
    }

    /**
     * 设置 {@link #rotateY(int, int)} 动画的差值器
     *
     * @param interpolator  动画差值器
     * @return
     */
    public MTransitionView rotateYInterpolator(Interpolator interpolator) {
        mTransition.rotateYInterpolator(interpolator);
        return this;
    }

    /**
     * 设置Z轴的旋转动画
     *
     * @param from
     * @param to
     * @return
     */
    public MTransitionView rotate(int from, int to) {
        mTransition.rotateZFrom(from);
        mTransition.rotateZTo(to);
        return this;
    }

    /**
     * @see #rotate(int, int)
     */
    public MTransitionView rotateFrom(int from) {
        mTransition.rotateZFrom(from);
        return this;
    }

    /**
     * @see #rotate(int, int)
     */
    public MTransitionView rotateTo(int to) {
        mTransition.rotateZTo(to);
        return this;
    }

    /**
     * 设置 {@link #rotate(int, int)} 动画的时长
     *
     * @param duration  动画时长
     * @return
     */
    public MTransitionView rotateDuration(int duration) {
        mTransition.rotateZDuration(duration);
        return this;
    }

    /**
     * 设置 {@link #rotate(int, int)} 动画的延时启动时间
     *
     * @param delay     动画的延时启动时间
     * @return
     */
    public MTransitionView rotateStartDelay(int delay) {
        mTransition.rotateZStartDelay(delay);
        return this;
    }

    /**
     * 设置 {@link #rotate(int, int)} 动画的差值器
     *
     * @param interpolator  动画差值器
     * @return
     */
    public MTransitionView rotateInterpolator(Interpolator interpolator) {
        mTransition.rotateZInterpolator(interpolator);
        return this;
    }

    // ----------------------------------- 动画元素 end ----------------------------------

    /**
     * 设置该动画元素深度层次顺序，置于target的上层
     *
     * @param target    基准动画元素
     * @return
     */
    public MTransitionView above(@NonNull MTransitionView target) {
        if (mContent.getParent() != null) {
            ((ViewGroup) mContent.getParent()).removeView(mContent);
        }
        target.getBelongPage().aboveOn(this, target);
        return this;
    }

    /**
     * 设置该动画元素深度层次顺序，置于target的下层
     *
     * @param target    基准动画元素
     * @return
     */
    public MTransitionView below(@NonNull MTransitionView target) {
        if (mContent.getParent() != null) {
            ((ViewGroup) mContent.getParent()).removeView(mContent);
        }
        target.getBelongPage().belowTo(this, target);
        return this;
    }

    void setBelongPage(MTransitionPage mBelongPage) {
        this.mBelongPage = mBelongPage;
    }

    /**
     * 获取包含该实例的MTransitionPage
     *
     * @return 包含该实例的MTransitionPage实例
     */
    public @NonNull MTransitionPage getBelongPage() {
        return mBelongPage;
    }

    public MTransitionView setDuration(long duration) {
        return setDuration(duration, true);
    }

    MTransitionView setDuration(long duration, boolean byUser) {
        mTransition.setDuration(duration, byUser);
        return this;
    }

    public MTransitionView setStartDelay(long delay) {
        mTransition.setStartDelay(delay);
        return this;
    }

    long getMaxAnimTime() {
        return mTransition.getMaxAnimTime();
    }

    /**
     * 获取View的屏幕位置信息
     *
     * @return
     */
    public int[] getLocation() {
        return mLocation;
    }

    /**
     * 让该View在场景切换动画过程中隐藏不显示
     */
    public void hideDuringTrasition() {
        alpha(0f, 0f);
    }

    /**
     * 获取该View的宽度
     * @return
     */
    public int getWidth() {
        return getSourceView().getMeasuredWidth();
    }

    /**
     * 获取该View的高度
     * @return
     */
    public int getHeight() {
        return getSourceView().getMeasuredHeight();
    }
}
