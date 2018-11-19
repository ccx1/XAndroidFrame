package com.android.xjhttp.interceptors;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("User-Agent", "android")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Connection", "Keep-Alive");
        return chain.proceed(builder.build());
    }
}