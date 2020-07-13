package com.android.http.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author chicunxiang
 */
public class RetrofitManager {

    private static RetrofitManager sRetrofitManager;
    private String mBaseUrl;
    private Retrofit retrofit;
    private OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .build();

    private RetrofitManager() {
    }

    public static RetrofitManager getInstance() {
        if (sRetrofitManager == null) {
            sRetrofitManager = new RetrofitManager();
        }
        return sRetrofitManager;
    }

    public RetrofitManager setBaseUrl(String url) {
        this.mBaseUrl = url;
        return this;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.mOkHttpClient = okHttpClient;
        build();
    }

    public void build() {
        if (mBaseUrl == null) {
            throw new RuntimeException("need setBaseUrl");
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .create();
        retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
