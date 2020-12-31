package com.android.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author chicunxiang
 */
public class ResultModel<T> extends ResultBaseModel {

    @SerializedName(value = "res", alternate = {"data", "body"})
    private T data;

    public T getData() {
        return data;
    }


    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "请求结果为{" +
                "code=" + getCode() +
                ", msg='" + getMsg() + '\'' +
                ", timestamp=" + getTimestamp() + ",data=" + (data != null ? data.toString() : null) +
                "}";
    }
}
