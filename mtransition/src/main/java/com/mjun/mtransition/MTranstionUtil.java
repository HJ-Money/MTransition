package com.mjun.mtransition;

import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

/**
 * @author huijun.zhj
 * 工具类
 */
public class MTranstionUtil {

    /**
     * 保存Acitivty的Window动画设置
     */
    private static Map<Class, Integer> sAnimationMap = new HashMap<>();

    /**
     * 因为Android默认会给Activity启动和关闭的动画，这会与MTranstion的动画冲突，所以需要在必要的时候去掉Android的默认动画
     * 一般情况下，会在FromPage的{@link MTransitionPage#setContainer(View, ITransitPrepareListener)}主动调用一次
     *
     * Because Android defaults to the Activity start and close animation, this will conflict with the MTranstion
     * animation, so you need to remove the Android default Activity animation when necessary
     * In general, it will be invoked once in FromPage's
     * {@link MTransitionPage#setContainer(View, ITransitPrepareListener)}
     *
     * @param activity
     */
    public static void removeActivityCloseAnimation(Activity activity) {
        if (activity != null) {
            saveAnimtaion(activity);
            if (Build.VERSION.SDK_INT >= 26 && !isHuawei()) {
                // android o 并且不是华为
                activity.getWindow().setWindowAnimations(R.style._mtransition_activityCloseNoAnimation_androido);
            } else {
                activity.getWindow().setWindowAnimations(R.style._mtransition_activityCloseNoAnimation);
            }
        }
    }

    /**
     * 因为Android默认会给Activity启动和关闭的动画，这会与MTranstion的动画冲突，所以需要在必要的时候去掉Android的默认动画
     * 一般情况下，会在ToPage的{@link MTransitionPage#setContainer(View, ITransitPrepareListener)}主动调用一次
     *
     * Because Android defaults to the Activity start and close animation, this will conflict with the MTranstion
     * animation, so you need to remove the Android default Activity animation when necessary
     * In general, it will be invoked once in ToPage's
     * {@link MTransitionPage#setContainer(View, ITransitPrepareListener)}
     *
     * @param activity
     */
    public static void removeActivityOpenAnimation(Activity activity) {
        if (activity != null) {
            saveAnimtaion(activity);
            if (Build.VERSION.SDK_INT >= 26 && !isHuawei()) {
                // android o 并且不是华为
                activity.getWindow().setWindowAnimations(R.style._mtransition_activityOpenNoAnimation_androido);
            } else {
                activity.getWindow().setWindowAnimations(R.style._mtransition_activityOpenNoAnimation);
            }
        }
    }

    /**
     * 恢复Activity默认的启动关闭动画
     *
     * Restore Activity default animation
     *
     * @see #removeActivityCloseAnimation(Activity)
     * @see #removeActivityOpenAnimation(Activity)
     *
     * @param activity
     */
    public static void resumeActivityAnimation(Activity activity) {
        if (activity != null) {
            restoreAnimtaion(activity);
        }
    }

    private static void saveAnimtaion(Activity activity) {
        Class clazz = activity.getClass();
        if (!sAnimationMap.containsKey(clazz)) {
            int animtaion = activity.getWindow().getAttributes().windowAnimations;
            sAnimationMap.put(clazz, animtaion);
        }
    }

    private static void restoreAnimtaion(Activity activity) {
        Class clazz = activity.getClass();
        if (sAnimationMap.containsKey(clazz)) {
            int animtaion = sAnimationMap.get(clazz);
            activity.getWindow().setWindowAnimations(animtaion);
            sAnimationMap.remove(clazz);
        }
    }

    private static boolean isHuawei() {
        String brand = Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.toLowerCase().contains("huawei") || brand.toLowerCase().contains("honor")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在启动Activity或者关闭Activity，如果需要做MTransition动画时，需要在{@link Activity#startActivity(Intent)} 或者
     * {@link Activity#finish()}调用之后，调用该方法，来关闭默认的Activity动画
     *
     * @param activity
     */
    public static void removeActivityAnimation(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.no_anim, R.anim.no_anim);
        }
    }
}
