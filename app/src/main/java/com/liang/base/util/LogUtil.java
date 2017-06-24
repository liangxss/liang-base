package com.liang.base.util;

import android.annotation.SuppressLint;
import android.os.Debug;


import com.liang.base.Config;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.WARN;

/**
 * Created by liang on 2017/3/27.
 */
@SuppressLint("LogTagMismatch")
public final class LogUtil {

    public static final String TAG = LogUtil.class.getSimpleName();

    private static boolean isLoggable(int level) {
        return Config.DEBUG; /*|| android.util.Log.isLoggable(TAG, level)*/
    }


    public static void i(Object... objects) {
        if (isLoggable(INFO)) {
            android.util.Log.i(TAG, toString(objects));
        }
    }

    public static void i(String tag, Object... objects) {
        if (isLoggable(INFO)) {
            android.util.Log.i(tag, toString(objects));
        }
    }

    public static void i(Throwable t, Object... objects) {
        if (isLoggable(INFO)) {
            android.util.Log.i(TAG, toString(objects), t);
        }
    }

    public static void d(Object... objects) {
        if (isLoggable(DEBUG)) {
            android.util.Log.d(TAG, toString(objects));
        }
    }

    public static void d(String tag, Object... objects) {
        if (isLoggable(DEBUG)) {
            android.util.Log.d(tag, toString(objects));
        }
    }

    public static void d(Throwable t, Object... objects) {
        if (isLoggable(DEBUG)) {
            android.util.Log.d(TAG, toString(objects), t);
        }
    }

    public static void w(Object... objects) {
        if (isLoggable(WARN)) {
            android.util.Log.w(TAG, toString(objects));
        }
    }

    public static void w(String tag, Object... objects) {
        if (isLoggable(WARN)) {
            android.util.Log.w(tag, toString(objects));
        }
    }

    public static void w(Throwable t, Object... objects) {
        if (isLoggable(WARN)) {
            android.util.Log.w(TAG, toString(objects), t);
        }
    }

    public static void e(Object... objects) {
        if (isLoggable(ERROR)) {
            android.util.Log.e(TAG, toString(objects));
        }
    }

    public static void e(String tag, Object... objects) {
        if (isLoggable(ERROR)) {
            android.util.Log.e(tag, toString(objects));
        }
    }

    public static void e(Throwable t, Object... objects) {
        if (isLoggable(ERROR)) {
            android.util.Log.e(TAG, toString(objects), t);
        }
    }

    /**
     * @throws RuntimeException
     *             always throw after logging the error message.
     */
    public static void tRE(Object... objects) {
        if (isLoggable(ERROR)) {
            android.util.Log.e(TAG, toString(objects));
            throw new RuntimeException("Fatal error : " + toString(objects));
        }
    }

    public static void tRE(String tag, Object... objects) {
        if (isLoggable(ERROR)) {
            android.util.Log.e(tag, toString(objects));
            throw new RuntimeException(tag + " : Fatal error : " + toString(objects));
        }
    }

    /**
     * @throws RuntimeException
     *             always throw after logging the error message.
     */
    public static void tRE(Throwable t, Object... objects) {
        if (isLoggable(ERROR)) {
            android.util.Log.e(TAG, toString(objects), t);
            throw new RuntimeException("Fatal error : " + toString(objects), t);
        }
    }

    private static String toString(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            sb.append(o);
        }
        return sb.toString();
    }

    private static boolean isTraceview = false;

    /**
     * traceview performance test in main thread
     *
     * @param name
     */
    public static void DebugStart(String name) {
        if (isTraceview) {
            Debug.startMethodTracing(name);
        }
    }

    public static void DebugStop() {
        if (isTraceview) {
            Debug.stopMethodTracing();
        }
    }

    public static void ePrint(Exception e) {
        if (isLoggable(ERROR)) {
            e.printStackTrace();
        }
    }
}