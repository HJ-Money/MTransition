package com.mjun.mtransition.bundle;

import com.mjun.mtransition.MTransition;
/**
 * @author huijun.zhj
 *
 * 传递数据所用 {@link MTransition#getBundle()},其用法和{@link android.os.Bundle}一致
 * Used to pass data {@link MTransition#getBundle()}，Its usage is the same as {@link android.os.Bundle}
 */
public class MTransitionBundle extends Bundle {

    public void putObject(String key, Object value) {
        mMap.put(key, value);
    }

    public Object getObject(String key) {
        Object o = mMap.get(key);
        return o;
    }
}
