package com.android.xjhttp.callback;

public interface ResponseCallback<T> {

    void onResponse(T response);

    void onFailure(String msg);
}
