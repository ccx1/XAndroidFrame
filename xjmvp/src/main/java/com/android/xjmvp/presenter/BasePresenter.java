package com.android.xjmvp.presenter;

import android.content.Context;
import android.support.annotation.StringRes;

import com.android.xjmvp.view.BaseView;

import io.reactivex.functions.Consumer;

/**
 * @author ccx
 * @date 2018/11/21
 */
public interface BasePresenter<V extends BaseView> {

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
     * 带参数的请求数据，用于上个接口返回才需要请求下个接口时所用
     * @param o
     */
    void setData(Object o);

    /**
     * 重试，尝试重新连接，请求数据等等
     */
    void retry();

    /**
     * 创建Bus
     *
     * @param clazz
     * @param action
     * @param <T>
     * @return
     */
    <T> void createBusInstance(Class<T> clazz, Consumer<? super T> action);

    /**
     * 执行回收操作
     */
    void onDestroy();

    // =============== 父类实现方法 ==================

    /**
     * 吐司一个资源id的string字符串
     *
     * @param stringId
     */
    void showToast(@StringRes int stringId);

    /**
     * 吐司一个字符串string
     *
     * @param msg
     */
    void showToast(String msg);

    /**
     * 获取资源id的字符串
     *
     * @param strid
     * @return
     */
    String getString(@StringRes int strid);
}
