package com.android.mvp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * @author ccx
 * @date 2019/6/12
 */
public abstract class BaseRecyclerViewListAdapter<T extends RecyclerView.ViewHolder, M> extends BaseAdapter<T> {

    public List<M> mData;

    public BaseRecyclerViewListAdapter(List<M> data) {
        this.mData = data;
    }



    @Override
    public void onBindViewHolder(@NonNull T holder, int i) {
        onBindViewHolder(holder, mData.get(i), i);
    }

    /**
     * 绑定数据的方法
     *
     * @param holder   holder
     * @param m        model
     * @param position position
     */
    protected abstract void onBindViewHolder(T holder, M m, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<M> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void updateData(M m) {
        if (mData.contains(m)) {
            int i = mData.indexOf(m);
            notifyItemChanged(i);
        } else {
            addData(m);
        }
    }

    public void removeData(M m) {
        if (mData.contains(m)) {
            int i = mData.indexOf(m);
            mData.remove(m);
            notifyItemRemoved(i);
        }
    }

    public void addData(M m) {
        if (!mData.contains(m)) {
            mData.add(m);
            notifyItemInserted(mData.size() - 1);
        }
    }


}
