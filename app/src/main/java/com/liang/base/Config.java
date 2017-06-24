package com.liang.base;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by liang on 2017/3/27.
 */
public class Config {
    public final static boolean DEBUG = isDebug(MyApplication.getApplication());

    private static boolean isDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

}
