package com.android.xjcommon.common;

import android.os.Build;
import android.util.Log;

import com.android.xjcommon.BuildConfig;

public class Logger {


    public static void i(String TAG, String s) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, s);
        }
    }

    public static void i(String s) {
        if (BuildConfig.DEBUG) {
            Log.i(Logger.class.getName(), s);
        }
    }

    public static void w(String TAG, String s) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, s);
        }
    }

    public static void w(String s) {
        if (BuildConfig.DEBUG) {
            Log.w(Logger.class.getName(), s);
        }
    }

    public static void d(String TAG, String s) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, s);
        }
    }

    public static void d(String s) {
        if (BuildConfig.DEBUG) {
            Log.d(Logger.class.getName(), s);
        }
    }

    public static void e(String TAG, String s) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, s);
        }
    }

    public static void e(String s) {
        if (BuildConfig.DEBUG) {
            Log.e(Logger.class.getName(), s);
        }
    }

    public static void v(String TAG, String s) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, s);
        }
    }

    public static void v(String s) {
        if (BuildConfig.DEBUG) {
            Log.v(Logger.class.getName(), s);
        }
    }
}
