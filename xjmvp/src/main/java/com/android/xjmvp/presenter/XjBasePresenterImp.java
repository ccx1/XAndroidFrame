package com.android.xjmvp.presenter;

import android.content.Context;

import com.android.xjmvp.view.XjBaseView;

/**
 * @author ccx
 * @date 2018/11/21
 */
public abstract class XjBasePresenterImp<V extends XjBaseView> implements XjBasePresenter<V> {

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
    public void showToast(int stringId) {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public String getString(int strid) {
        return null;
    }


}
