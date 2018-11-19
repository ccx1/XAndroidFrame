package com.mobgeek.android.store.network;

import com.android.xjhttp.callback.ResponseCallback;
import com.android.xjhttp.manager.HttpServiceManager;
import com.android.xjhttp.manager.OkHttpManager;
import com.android.xjhttp.manager.RetrofitManager;
import com.android.xjhttp.model.ResultModel;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import io.reactivex.Observable;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class HttpHelper {
    private HttpHelper() {
    }

    static {
        RetrofitManager.getInstance().setBaseUrl("http://192.168.2.154:3001")
                .setOkHttpClient(OkHttpManager.getInstance().build());
    }

    public static void get(ResponseCallback<ResultModel<String>> callback) {
        Map<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("abc", "123");
        Observable<ResultModel<String>> observable = RetrofitManager.getInstance().getRetrofit().create(ApiService.class).get(stringStringHashMap);
        HttpServiceManager.getInstance().enqueue(observable, callback);
    }

    public static void get2(ResponseCallback<ResultModel<String>> callback) {
        Map<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("abc", "123");
        JSONObject                      jsonObject = new JSONObject(stringStringHashMap);
        String                          s          = jsonObject.toString();
        MediaType                       type       = MediaType.parse("application/json;charset=utf-8");
        RequestBody                     body       = RequestBody.create(type, s);
        Observable<ResultModel<String>> observable = RetrofitManager.getInstance().getRetrofit().create(ApiService.class).get(body);
        HttpServiceManager.getInstance().enqueue(observable, callback);
    }
}
