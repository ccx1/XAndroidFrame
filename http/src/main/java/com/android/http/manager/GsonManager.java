package com.android.http.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Use: Gson的转换管理器
 * Author：cunxiangchi@gamil.com
 * Time: 2020/6/29
 */
public class GsonManager {

    private static Gson sGson;

    static {
        sGson = new Gson();
    }

    public static String fromModel(Object o) {
        return sGson.toJson(o);
    }

    public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        return sGson.fromJson(gsonString, cls);
    }

    public static <T> List<T> jsonToList(String gsonString, Class<T> cls) {
        return sGson.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
    }

    public static <T> List<Map<String, T>> jsonToListMaps(String gsonString) {
        return sGson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
    }

    public static <T> Map<String, T> jsonToMaps(String gsonString) {
        return sGson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
    }
}
