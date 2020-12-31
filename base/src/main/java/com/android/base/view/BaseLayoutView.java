package com.android.base.view;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.android.common.action.AbstractAction1;
import com.android.mvp.presenter.BasePresenter;
import com.android.mvp.view.BaseView;

/**
 * Use: 自定义界面的v层base类
 * Author：cunxiangchi@gamil.com
 * Time: 2020/6/17
 */
public abstract class BaseLayoutView<T extends BasePresenter> extends FrameLayout implements BaseView {
    protected final T mPresenter;

    @SuppressWarnings("unchecked")
    public BaseLayoutView(@NonNull Context context) {
        super(context);
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this, context);
        }
    }

    @Override
    public void showContent() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showEmpty() {

    }

    /**
     * 初始化p层
     * @return p
     */
    public abstract T initPresenter();


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void requestPermission(String[] permissions, AbstractAction1<Boolean> action1) {

    }

}
