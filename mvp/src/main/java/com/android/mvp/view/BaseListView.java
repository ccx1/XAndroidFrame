package com.android.mvp.view;

/**
 * @author ccx
 * @date 2018/11/26
 */
public interface BaseListView extends BaseView {
    void refreshCompleted();

    void loadMoreCompleted();
}
