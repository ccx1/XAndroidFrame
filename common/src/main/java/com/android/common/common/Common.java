package com.android.common.common;

import android.content.Context;

import com.android.common.utils.ToastUtil;

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
        ToastUtil.init(context);
        Reflex.init(context);
        Initial = true;
    }

    public static boolean isInitialization() {
        return Initial;
    }

}
