package com.android.http.interceptors;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 转换baseurl拦截器
 */
public class ChangeBaseUrlInterceptor implements Interceptor {

    public static final String DOMAIN = "domain";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        HttpUrl url = request.url();
        List<String> headers = request.headers(DOMAIN);
        // 不会等于null 因为 默认创建了一个20容量的arrayList
        if (headers.size() > 0) {
            // 获取header里面的url
            String headValue = headers.get(0);
            HttpUrl newBaseUrl = HttpUrl.parse(headValue);
            if (newBaseUrl != null) {
                // 生成新的HttpUrl
                HttpUrl build = url.newBuilder().scheme(newBaseUrl.scheme()).host(newBaseUrl.host()).port(newBaseUrl.port()).build();
                return chain.proceed(request.newBuilder().removeHeader(DOMAIN).url(build).build());
            }
        }

        return chain.proceed(request);
    }
}
