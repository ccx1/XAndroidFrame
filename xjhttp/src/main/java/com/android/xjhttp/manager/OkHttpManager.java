package com.android.xjhttp.manager;

import com.android.xjhttp.interceptors.BasicInterceptor;
import com.android.xjhttp.interceptors.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author chicunxiang
 */
public class OkHttpManager {

    private static OkHttpManager sHttpManager;
    private        long          mTimeOut = 60;
    private        OkHttpClient  mOkHttpClient;

    public static OkHttpManager getInstance() {
        if (sHttpManager == null) {
            sHttpManager = new OkHttpManager();
        }
        return sHttpManager;
    }

    public void setTimeOut(long timeOut) {
        mTimeOut = timeOut;
    }

    public OkHttpClient build() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new LoggerInterceptor())
                    .addInterceptor(new BasicInterceptor())
                    .connectTimeout(mTimeOut, TimeUnit.SECONDS)
                    .readTimeout(mTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(mTimeOut, TimeUnit.SECONDS)
                    .build();
        }
        return mOkHttpClient;
    }

    public void addInterceptor(Interceptor interceptor) {
        mOkHttpClient = build().newBuilder().addInterceptor(interceptor).build();
    }
}
