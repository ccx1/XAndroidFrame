package com.android.mvp.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.mvp.R;


/**
 * @author ccx
 * @date 2018/11/21
 */
public class StatusLayout extends FrameLayout {

    private SparseArray<View> viewTask = new SparseArray<>();
    private int               contentLayoutID,
            loadingViewID              = R.layout.layout_status_loading,
            errorViewID                = R.layout.layout_status_error,
            emptyViewID                = R.layout.layout_status_empty;

    public StatusLayout(Context context) {
        this(context, null);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // 设置内容布局
    public void setContentView(View view) {
        contentLayoutID = view.getId();
        viewTask.put(contentLayoutID, view);
        showContent();
    }

    // 设置内容布局
    public void setContentView(int id) {
        remove(this.contentLayoutID);
        this.contentLayoutID = id;
        inflate(contentLayoutID);
        showContent();
    }

    private View inflate(int layoutID) {
        View view = viewTask.get(layoutID);
        if (view != null) {
            viewTask.remove(layoutID);
            this.removeView(view);
        }
        view = View.inflate(getContext(), layoutID, null);
        if (layoutID != contentLayoutID && viewTask.get(contentLayoutID) != null) {
            view.setLayoutParams(viewTask.get(contentLayoutID).getLayoutParams());
        } else {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layoutParams);
        }
        view.setVisibility(GONE);
        addView(view);
        viewTask.put(layoutID, view);
        return view;
    }

    public void setRetryOnClickListener(OnClickListener listener) {
        View errorLayout = viewTask.get(errorViewID);
        if (errorLayout == null) {
            inflate(errorViewID);
        }
        View view = findViewById(R.id.retry_button);
        if (view == null) {
            throw new Resources.NotFoundException("The retry view's id must be retry_button !");
        }
        view.setOnClickListener(listener);
    }

    private void remove(int layoutID) {
        View view = viewTask.get(layoutID);
        if (view != null) {
            viewTask.remove(layoutID);
            this.removeView(view);
        }
    }

    private void show(int layoutId) {
        for (int i = 0; i < viewTask.size(); i++) {
            viewTask.valueAt(i).setVisibility(GONE);
        }
        isAddedView(layoutId);

    }

    private void isAddedView(int layoutId) {
        View view = findView(layoutId);
        if (view.getParent() == null) {
            this.addView(view);
        }
        view.setVisibility(VISIBLE);
    }

    private View findView(int layoutId) {
        if (viewTask.get(layoutId, null) != null) {
            return viewTask.get(layoutId);
        }
        return inflate(layoutId);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        if (childCount == 1) {
            setContentView(getChildAt(0));
            return;
        }
        throw new RuntimeException("最多允许有一个子控件!");
    }


    //=========== 根据网络切换布局 ==============

    public void showContent() {
        show(contentLayoutID);
    }

    public void showLoading() {
        show(loadingViewID);
    }

    public void showError() {
        show(errorViewID);
    }

    public void showEmpty() {
        show(emptyViewID);
    }
}
