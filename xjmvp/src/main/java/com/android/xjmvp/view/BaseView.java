package com.android.xjmvp.view;

import com.android.xjcommon.action.AbstractAction1;

/**
 * @author ccx
 * @date 2018/11/21
 */
public interface BaseView {
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
}
