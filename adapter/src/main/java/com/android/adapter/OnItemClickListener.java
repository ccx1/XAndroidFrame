package com.android.adapter;

import android.view.View;

/**
 * @author ccx
 * @date 2018/11/26
 */
public interface OnItemClickListener<T> {
    /**
     * 子条目点击事件
     *
     * @param v
     * @param currentPosition
     * @param t
     */
    void onItemClick(View v, int currentPosition, T t);

    /**
     * 子条目长按点击
     *
     * @param v
     * @param currentPosition
     * @param t
     * @return
     */
    boolean onLongClick(View v, int currentPosition, T t);
}
