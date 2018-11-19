package com.android.xjcommon.bus;

/**
 * @author ccx
 * @date 2018/11/16
 */
public class XjEvent<T> {

    private String tag;
    private T      t;

    public XjEvent(String tag, T t) {
        this.tag = tag;
        this.t = t;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
