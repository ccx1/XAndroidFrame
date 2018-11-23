package com.android.xjcommon.bus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @author ccx
 * @date 2018/11/16
 */
public class Bus {

    private static Bus                       sXjBus;
    private final  FlowableProcessor<Object> mProcessor;
    private static List<Object>              subscribeTask = new ArrayList<>();

    private Bus() {
        mProcessor = PublishProcessor.create().toSerialized();
    }

    public static Bus get() {
        if (sXjBus == null) {
            synchronized (Bus.class) {
                if (sXjBus == null) {
                    sXjBus = new Bus();
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
