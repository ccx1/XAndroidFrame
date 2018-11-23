package com.android.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author ccx
 * @date 2018/11/22
 */
public class ToastUtil {

    private static       Toast   toast;
    private static final String  MAIN_THREAD_NAME = Looper.getMainLooper().getThread().getName();
    @SuppressLint("StaticFieldLeak")
    private static       Context sContext;


    public static void init(Context context) {
        sContext = context;
    }

    @SuppressLint("ShowToast")
    public static void showToast(String msg, int length) {
        String  threadName   = Thread.currentThread().getName();
        boolean isMainThread = MAIN_THREAD_NAME.equals(threadName);
        if (!isMainThread) {
            Looper.prepare();
            Toast.makeText(sContext, msg, length).show();
            Looper.loop();
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(sContext, msg, length);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showToast(int resStrId) {
        String string = sContext.getString(resStrId);
        showToast(string);
    }


    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }


}
