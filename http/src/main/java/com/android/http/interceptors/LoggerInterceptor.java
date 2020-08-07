package com.android.http.interceptors;


import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author chicunxiang
 */
public class LoggerInterceptor implements Interceptor {
    private String TAG = "request";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //Chain 里包含了request和response
        Request request   = chain.request();
        long    startTime = System.currentTimeMillis();
        Method  logger    = getLogger();
        i(logger, TAG, "----------开始请求 ");
        i(logger, TAG, "----------请求地址 : " + request.url() + " ");
        i(logger, TAG, "----------请求头 : " + request.headers() + " ");
        i(logger, TAG, "----------请求方式 : " + request.method() + " ");
        i(logger, TAG, "----------请求的body : " +new Gson().toJson(request.body()) + " ");
        Response response = chain.proceed(request);
        long     endTime  = System.currentTimeMillis();
        long     duration = endTime - startTime;
        i(logger, TAG, "----------结束请求 : " + duration + " 毫秒");
        return response;
    }

    private void i(Method logger, String tag, String msg) {
        if (logger == null) {
            return;
        }
        try {
            logger.invoke(null, tag, msg);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method getLogger() {
        Method method = null;
        try {
            Class<?> clazz = Class.forName("com.android.common.common.Logger");
            method = clazz.getMethod("i", String.class, String.class);
            return method;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
