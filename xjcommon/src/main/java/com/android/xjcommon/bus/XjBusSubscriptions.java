package com.android.xjcommon.bus;

import com.android.xjcommon.base.XjSupportFragment;
import com.android.xjcommon.base.XjSupportFragmentImp;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ccx
 * @date 2018/11/16
 */

public class XjBusSubscriptions {
    private static Map<Object, List<Disposable>> mTask = new HashMap<>();


    public static void unbind(final Object o) {
        final List<Disposable> disposables = mTask.get(o);
        if (disposables == null) {
            return;
        }
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                if (!disposables.isEmpty()) {
                    for (Disposable disposable : disposables) {
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }
                    mTask.remove(o);
                }
            }
        });

    }

    public static void bind(Object o, Disposable... disposable) {
        bind(o, Arrays.asList(disposable));
    }

    public static void bind(Object o, List<Disposable> disposable) {
        List<Disposable> disposables = mTask.get(o);
        if (disposables == null) {
            mTask.put(o, disposable);
            return;
        }
        disposables.clear();
        disposables.addAll(disposable);
    }

    public static void unBindAll() {
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                Set<Object> objects = mTask.keySet();
                for (Object object : objects) {
                    unbind(object);
                }
            }
        });
    }
}
