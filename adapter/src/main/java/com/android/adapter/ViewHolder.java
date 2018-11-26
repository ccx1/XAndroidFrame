package com.android.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ccx
 * @date 2018/11/26
 */
public class ViewHolder extends RecyclerView.ViewHolder {


    private View              mConvertView;
    private Context           mContext;
    private SparseArray<View> mViews;
    private Map<Object, View> mViewMap = new HashMap<>();

    public ViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        this.mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }


    public static ViewHolder get(Context context, View itemView) {
        return new ViewHolder(context, itemView);
    }

    public View getConvertView() {
        return mConvertView;
    }

    public static ViewHolder get(Context context, LayoutInflater inflater, int layoutId, ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(layoutId, parent, false);
        return new ViewHolder(context, inflate);
    }

    public <T extends View> T getView(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mConvertView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }


    public int getCurrentPosition() {
        return this.getAdapterPosition();
    }

    public <T extends View> T getView(Object tag) {
        View view = mViewMap.get(tag);
        if (view == null) {
            view = mConvertView.findViewWithTag(tag);
            mViewMap.put(tag, view);
        }
        return (T) view;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageAlpha(int viewId, int alpha) {
        ImageView view = getView(viewId);
        view.setImageAlpha(alpha);
        return this;
    }

    public ViewHolder setImageLevel(int viewId, int level) {
        ImageView view = getView(viewId);
        view.setImageLevel(level);
        return this;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public ViewHolder setImageIcon(int viewId, Icon icon) {
        ImageView view = getView(viewId);
        view.setImageIcon(icon);
        return this;
    }

    public ViewHolder setImageMatrix(int viewId, Matrix matrix) {
        ImageView view = getView(viewId);
        view.setImageMatrix(matrix);
        return this;
    }

    public ViewHolder setAlpha(int viewId, float value) {
        View view = getView(viewId);
        view.setAlpha(value);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }


    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }


    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder setVisible(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }


    public ViewHolder setEnable(int viewId, boolean enable) {
        View view = getView(viewId);
        try {
            view.setEnabled(enable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public ViewHolder addLinearLayoutView(int viewId, View contentView) {
        LinearLayout view = getView(viewId);
        view.addView(contentView);
        return this;
    }

    /**
     * 关于事件的
     */
    public ViewHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }


    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }


    public ViewHolder clearViews(int viewId) {
        ViewGroup view = getView(viewId);
        view.removeAllViews();
        return this;
    }


}
