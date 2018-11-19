package com.android.xjcommon.bus;

import com.android.xjcommon.base.XjSupportFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @author ccx
 * @date 2018/11/16
 */
public class XjBus {

    private static XjBus                     sXjBus;
    private final  FlowableProcessor<Object> mProcessor;
    private static List<Object>              subscribeTask = new ArrayList<>();

    private XjBus() {
        mProcessor = PublishProcessor.create().toSerialized();
    }

    public static XjBus get() {
        if (sXjBus == null) {
            synchronized (XjBus.class) {
                if (sXjBus == null) {
                    sXjBus = new XjBus();
                }
            }
        }
        return sXjBus;
    }

    public void post(Object o) {
        mProcessor.onNext(o);
    }

    /**
     * 接受特定类型的post
     *
     * @return
     */
    public <T> Flowable<T> subscribe(Class<T> eventType) {
        return mProcessor.ofType(eventType);
    }

    /**
     * 接受所有的post。不做拦截
     *
     * @return
     */
    public FlowableProcessor<Object> subscribe() {
        return mProcessor;
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return mProcessor.hasSubscribers();
    }


}
