package com.android.xjcommon.common;

import android.util.Log;

import com.android.xjcommon.BuildConfig;

/**
 * @author chicunxiang
 */
public class Logger {


    public static void i(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, s);
        }
    }

    public static void i(String s) {
        if (BuildConfig.DEBUG) {
            Log.i(Logger.class.getName(), s);
        }
    }

    public static void w(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, s);
        }
    }

    public static void w(String s) {
        if (BuildConfig.DEBUG) {
            Log.w(Logger.class.getName(), s);
        }
    }

    public static void d(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, s);
        }
    }

    public static void d(String s) {
        if (BuildConfig.DEBUG) {
            Log.d(Logger.class.getName(), s);
        }
    }

    public static void e(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, s);
        }
    }

    public static void e(String s) {
        if (BuildConfig.DEBUG) {
            Log.e(Logger.class.getName(), s);
        }
    }

    public static void v(String tag, String s) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, s);
        }
    }

    public static void v(String s) {
        if (BuildConfig.DEBUG) {
            Log.v(Logger.class.getName(), s);
        }
    }
}
