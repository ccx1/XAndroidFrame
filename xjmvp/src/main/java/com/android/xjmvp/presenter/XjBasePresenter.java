package com.android.xjmvp.presenter;

import android.content.Context;
import android.support.annotation.StringRes;

import com.android.xjmvp.view.XjBaseView;

/**
 * @author ccx
 * @date 2018/11/21
 */
public interface XjBasePresenter<V extends XjBaseView> {

    /**
     * 注册
     *
     * @param view    baseview child
     * @param context activity 或者 app
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

    /**
     * 执行回收操作
     */
    void onDestroy();

    // =============== 父类实现方法 ==================
    void showToast(@StringRes int stringId);

    void showToast(String msg);

    String getString(@StringRes int strid);
}
