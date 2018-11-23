package com.android.xjmvp.presenter;

import android.content.Context;

import com.android.xjcommon.bus.Bus;
import com.android.xjcommon.bus.BusSubscriptions;
import com.android.xjcommon.utils.ToastUtil;
import com.android.xjmvp.view.BaseView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * @author ccx
 * @date 2018/11/21
 */
public abstract class BasePresenterImp<V extends BaseView> implements BasePresenter<V> {

    protected Context mContext;
    protected V       mView;

    @Override
    public void attachView(V view, Context context) {
        mView = view;
        mContext = context;
        initPresenterData();
    }

    @Override
    public void setData() {
    }

    @Override
    public void retry() {
    }


    @Override
    public <T> Disposable createBusInstance(final Class<T> clazz, Consumer<? super T> action) {
        Disposable subscribe = Bus.get()
                .subscribe(clazz)
                .filter(new Predicate<T>() {
                    @Override
                    public boolean test(T t) throws Exception {
                        return t.getClass() == clazz;
                    }
                })
                .cast(clazz)
                .subscribe(action, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        BusSubscriptions.bind(mView, subscribe);
        return subscribe;
    }

    @Override
    public void showToast(int stringId) {
        ToastUtil.showToast(stringId);
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public String getString(int strId) {
        return mContext.getString(strId);
    }

    @Override
    public void onDestroy() {
        BusSubscriptions.unbind(mView);
    }
}
