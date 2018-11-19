package com.android.xjdata.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射类
 * @author ccx
 * @date 2018/11/16
 */
public class Reflex {

    public static boolean isInitialization() {
        try {
            Class<?> aClass           = Class.forName("com.android.xjcommon.common.Common");
            Method   isInitialization = aClass.getMethod("isInitialization");
            return (boolean) isInitialization.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
}
