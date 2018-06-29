package com.mjun.mtransition;

/**
 * @author huijun.zhj
 * 日志工具类
 * Log Util
 */
class Log {

    public static final boolean DEBUG = false;

    static void v(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.v(tag, msg);
        }
    }

    static void d(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    static void e(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.e(tag, msg);
        }
    }
}
