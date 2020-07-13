package com.android.http.interceptors;


import androidx.annotation.NonNull;

import com.android.http.model.RequestException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author chicunxiang
 */
public class BasicInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("User-Agent", "android")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Connection", "Keep-Alive");
        Request request = builder.build();
        Response response = chain.proceed(request);

        if (response.code() >= 200 && response.code() < 400) {
            return response;
        }

        throw new RequestException(response.code(), response.message());

    }
}