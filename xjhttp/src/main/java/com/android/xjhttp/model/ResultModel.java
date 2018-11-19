package com.android.xjhttp.model;

public class ResultModel<T> extends ResultBaseModel {
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
                ", timestamp=" + getTimestamp() + ",data=" + data.toString() +
                "}";
    }
}
