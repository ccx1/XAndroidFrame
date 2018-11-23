package com.android.group.sample.network;

import com.android.http.callback.ResponseCallback;
import com.android.http.manager.HttpServiceManager;
import com.android.http.manager.OkHttpManager;
import com.android.http.manager.RetrofitManager;
import com.android.http.model.ResultModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
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
