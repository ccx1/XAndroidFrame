package com.android.picker.loopview;

public class LoopBean {

    private String loopMessage;

    public LoopBean(String loopMessage) {
        this.loopMessage = loopMessage;
    }

    LoopBean() {
        this("");
    }

    public String getLoopMessage() {
        return loopMessage;
    }

    public void setLoopMessage(String loopMessage) {
        this.loopMessage = loopMessage;
    }
}
