package com.android.xjcommon.action;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author ccx
 * @date 2018/11/22
 */
public abstract class Action1<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        onError(((Exception) e));
    }

    public abstract void onError(Exception e);

}
