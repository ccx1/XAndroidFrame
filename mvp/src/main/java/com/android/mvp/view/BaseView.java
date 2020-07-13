package com.android.mvp.view;

import com.android.common.action.AbstractAction1;

/**
 * @author ccx
 * @date 2018/11/21
 */
public interface BaseView<P> {
    /**
     * 显示内容视图
     */
    void showContent();

    /**
     * 显示loading视图
     */
    void showLoading();

    /**
     * 显示错误视图
     */
    void showError();

    /**
     * 显示空数据视图
     */
    void showEmpty();

    /**
     * 统一请求权限方法
     * @param permissions
     * @param action1
     */
    void requestPermission(String[] permissions, AbstractAction1<Boolean> action1);

    /**
     * 初始化P层
     *
     * @return
     */
    P initPresenter();
}
