package com.android.http.callback;

/**
 * @author chicunxiang
 */
public interface ResponseCallback<T> {

    /**
     * 返回结果
     * @param response
     */
    void onResponse(T response);

    /**
     * 错误信息
     * @param msg
     */
    void onFailure(String msg);
}
