package com.android.mvp.adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ccx
 * @date 2019/6/12
 */
public abstract class BaseRecyclerViewAdapter<T extends RecyclerView.ViewHolder, M> extends BaseAdapter<T> {

    public M mData;

    public BaseRecyclerViewAdapter(M data) {
        this.mData = data;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int i) {
        onBindViewHolder(holder, mData, i);
    }

    /**
     * 绑定数据的方法
     *
     * @param holder   holder
     * @param m        model
     * @param position position
     */
    protected abstract void onBindViewHolder(T holder, M m, int position);

}
