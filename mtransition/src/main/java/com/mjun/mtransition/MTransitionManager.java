package com.mjun.mtransition;

import java.util.*;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author huijun.zhj
 *
 * 用于创建、销毁、获取{@link MTransition}实例
 * Used to create, destroy or get {@link MTransition} instances
 */
public class MTransitionManager {

    /**
     * 存储所有{@link MTransition}数据
     * Store all {@link MTransition} data
     */
    private static Map<String, MTransition> sTransitionMap = new HashMap<>();

    private static class LAZY_HOLDER {
        static MTransitionManager sInstance = null;
        static {
            sInstance = new MTransitionManager();
        }
    }

    private MTransitionManager() {
    }

    public static MTransitionManager getInstance() {
        return LAZY_HOLDER.sInstance;
    }

    /**
     * 根据name创建{@link MTransition},存储到{@link #sTransitionMap}, key是name，value是MTransition
     * Create {@link MTransition} by name, store to {@link #sTransitionMap},
     * key is name, and value is MTransition
     *
     * @param name 该MTransition的name
     * @return {@link MTransition}实例
     */
    public @NonNull MTransition createTransition(String name) {
        if (sTransitionMap.containsKey(name)) {
            MTransition transition = sTransitionMap.get(name);
            if (transition != null) {
                transition.clear();
                return transition;
            }
        }
        MTransition transition = new MTransition(name);
        sTransitionMap.put(name, transition);
        return transition;
    }

    /**
     * 获取已经创建的{@link MTransition}实例
     * Get the instance of {@link MTransition} that has been created
     *
     * @param name {@link MTransition}实例对应的name
     * @return 如果存在name对应的{@link MTransition}实例,则返回这个实例；否则，返回null；
     */
    public @Nullable MTransition getTransition(String name) {
        return sTransitionMap.get(name);
    }

    /**
     * 清除已经创建的{@link MTransition}实例
     * Clear the already created {@link MTransition} instance
     *
     * @param name {@link MTransition}实例对应的name
     */
    public void destoryTransition(String name) {
        sTransitionMap.remove(name);
    }
}
