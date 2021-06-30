package com.android.http.manager;

import com.android.http.client.SSL;
import com.android.http.interceptors.BasicInterceptor;
import com.android.http.interceptors.ChangeBaseUrlInterceptor;
import com.android.http.interceptors.LoggerInterceptor;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author chicunxiang
 */
public class OkHttpManager {

    private static OkHttpManager sHttpManager;
    private        long          mTimeOut = 10 * 1000;
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
            X509TrustManager trustAllCert = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            };
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new ChangeBaseUrlInterceptor())
                    .addInterceptor(new LoggerInterceptor())
                    .addInterceptor(new BasicInterceptor())
                    .connectTimeout(mTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(mTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(mTimeOut, TimeUnit.MILLISECONDS)
                    .sslSocketFactory(new SSL(trustAllCert), trustAllCert)
//                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
//                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                    .build();
        }
        return mOkHttpClient;
    }

    public void addInterceptor(Interceptor interceptor) {
        for (Interceptor interceptorItem : build().interceptors()) {
            if (interceptorItem.getClass() == interceptor.getClass()) {
                return;
            }
        }
        mOkHttpClient = build().newBuilder().addInterceptor(interceptor).build();
    }
}
