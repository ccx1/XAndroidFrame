package com.android.xjmvp.view;

/**
 * @author ccx
 * @date 2018/11/21
 */
public interface XjBaseView {
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
}
