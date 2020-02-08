package com.android.mvp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ccx
 * @date 2019/6/12
 */
public abstract class BaseRecyclerViewMapAdapter<T extends RecyclerView.ViewHolder, K, V> extends BaseAdapter<T> {

    public Map<K, V> mData;
    private final List<K> mKeys;

    public BaseRecyclerViewMapAdapter(Map<K, V> data) {
        this.mData = data;
        this.mKeys = new ArrayList<>(mData.keySet());
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int i) {
        onBindViewHolder(holder, mKeys.get(i), mData.get(mKeys.get(i)), i);
    }

    /**
     * 绑定数据的方法
     *
     * @param holder   holder
     * @param k        key
     * @param v        value
     * @param position position
     */
    protected abstract void onBindViewHolder(T holder, K k, V v, int position);

    @Override
    public int getItemCount() {
        return mKeys.size();
    }

    public void setData(Map<K, V> data) {
        if (data != null) {
            mData.clear();
            mData.putAll(data);
            mKeys.clear();
            mKeys.addAll(data.keySet());
        }
    }

    public void updateData(K k, V v) {
        if (mData.containsValue(k)) {
            mData.put(k, v);
            int i = mKeys.indexOf(k);
            notifyItemChanged(i);
        } else {
            addData(k, v);
        }
    }

    public void removeData(K k) {
        if (mData.containsKey(k)) {
            mData.remove(k);
            int i = mKeys.indexOf(k);
            mKeys.remove(k);
            notifyItemRemoved(i);
        }
    }

    public void addData(K k, V v) {
        if (!mData.containsKey(k)) {
            mData.put(k, v);
            mKeys.add(k);
            notifyItemInserted(mData.size() - 1);
        }
    }

}
