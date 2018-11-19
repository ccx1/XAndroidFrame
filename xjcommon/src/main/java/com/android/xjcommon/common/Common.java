package com.android.xjcommon.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class Common {

    private static boolean Initial = false;

    public static void init(Context context) {
        initBasic(context.getApplicationContext());
    }

    private static void initBasic(Context context) {
        Reflex.init(context);
        Initial = true;
    }

    public static boolean isInitialization() {
        return Initial;
    }

}
