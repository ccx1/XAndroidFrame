package com.android.common.bus;

/**
 * @author ccx
 * @date 2018/11/16
 */
public class Event<T> {

    private String tag;
    private T      t;

    public Event(String tag, T t) {
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
