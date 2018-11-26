package com.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author ccx
 * @date 2018/11/26
 */
public abstract class BaseCommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context                mContext;
    private LayoutInflater         mInflater;
    private int                    mLayoutId;
    private List<T>                mData;
    private OnItemClickListener<T> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private BaseCommonAdapter(Context context, int layoutId, List<T> data) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, mInflater, mLayoutId, parent, viewType);
        setListener(viewHolder);
        return viewHolder;
    }

    private void setListener(final ViewHolder viewHolder) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int currentPosition = viewHolder.getCurrentPosition();
                    try {
                        mOnItemClickListener.onItemClick(v, currentPosition, mData.get(currentPosition));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int currentPosition = viewHolder.getCurrentPosition();
                    return mOnItemClickListener.onLongClick(v, currentPosition, mData.get(currentPosition));
                }
                return false;
            }
        });
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        convert(viewHolder, mData.get(position));
    }

    /**
     * 适配处理
     * @param holder
     * @param t
     */
    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
