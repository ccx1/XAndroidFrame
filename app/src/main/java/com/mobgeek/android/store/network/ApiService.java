package com.mobgeek.android.store.network;

import com.android.xjhttp.model.ResultModel;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("/api/get")
    Observable<ResultModel<String>> get(@QueryMap Map<String, String> params);

    @POST("/api/get")
    Observable<ResultModel<String>> get(@Body RequestBody body);
}
