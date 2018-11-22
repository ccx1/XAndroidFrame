package com.android.xjhttp.model;

/**
 * @author ccx
 * @date 2018/11/22
 */
public class RequestException extends RuntimeException {
    private int    code;
    private String msg;

    public RequestException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg != null ? msg : "未知错误";
    }

    @Override
    public String toString() {
        return "RequestException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
