package com.android.xjmvp.presenter;

import android.content.Context;
import android.support.annotation.StringRes;

import com.android.xjmvp.view.XjBaseView;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author ccx
 * @date 2018/11/21
 */
public interface XjBasePresenter<V extends XjBaseView> {

    /**
     * 注册
     *
     * @param view    baseview child
     * @param context activity
     */
    void attachView(V view, Context context);

    /**
     * 需要初始化p层数据
     */
    void initPresenterData();

    /**
     * 设置数据，或者请求数据
     * 开始请求
     */
    void setData();

    /**
     * 重试，尝试重新连接，请求数据等等
     */
    void retry();

    <T> Disposable createBusInstance(Class<T> clazz, Consumer<? super T> action);

    /**
     * 执行回收操作
     */
    void onDestroy();

    // =============== 父类实现方法 ==================
    void showToast(@StringRes int stringId);

    void showToast(String msg);

    String getString(@StringRes int strid);
}
