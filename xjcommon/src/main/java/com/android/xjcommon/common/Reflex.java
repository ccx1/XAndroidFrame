package com.android.xjcommon.common;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射类
 * 统一初始化方法
 * @author ccx
 * @date 2018/11/16
 */
public class Reflex {

    private static final String INIT = "init";

    static void init(Context context) {
        try {
            initClass(context, "com.android.xjdata.helper.SharedPreferencesHelper");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            initClass(context, "com.android.xjdata.helper.FileHelper");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void initClass(Context context, String s) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz  = Class.forName(s);
        Method   method = clazz.getMethod(INIT, Context.class);
        method.invoke(null, context);
    }
}
