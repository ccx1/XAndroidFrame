package com.android.base.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

import com.android.base.R;
import com.android.mvp.presenter.BasePresenter;
import com.android.mvp.view.BaseFragment;


public abstract class BaseToolbarFragment<T extends BasePresenter> extends BaseFragment<T> {

    protected Toolbar mToolbar;
    protected TextView mTitle;
    protected ImageView mLeftImg;
    protected ImageView mRightImg;
    protected TextView mRightTextView;
    protected TextView mLeftTextView;
    private TextView mEmptyTextView;
    private View mEmptyView;
    private ViewGroup mHolderLayout;


    @Override
    protected View contentLayout() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        int layoutContent = getLayoutContent();

        View viewGroup = inflater.inflate(R.layout.fragment_list_base, null, false);
        mToolbar = viewGroup.findViewById(R.id.toolbar);
        mHolderLayout = viewGroup.findViewById(R.id.holder_layout);
        mTitle = viewGroup.findViewById(R.id.tv_toolbar_title);
        mLeftImg = viewGroup.findViewById(R.id.iv_toolbar_left);
        View mLeftGroup = viewGroup.findViewById(R.id.left_group);
        mRightImg = viewGroup.findViewById(R.id.iv_toolbar_right);
        mRightTextView = viewGroup.findViewById(R.id.tv_toolbar_right);
        mLeftTextView = viewGroup.findViewById(R.id.tv_left);
        mEmptyView = viewGroup.findViewById(R.id.empty_view);
        mEmptyTextView = viewGroup.findViewById(R.id.empty_text);
        if (layoutContent != 0) {
            View view = inflater.inflate(layoutContent, null, false);
            mHolderLayout.addView(view);
        }
        mLeftImg.setOnClickListener((v) -> leftClick());
        mLeftGroup.setOnClickListener((v) -> leftClick());
        return viewGroup;
    }


    protected void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mHolderLayout.setVisibility(View.GONE);
    }

    protected void showContentView() {
        mEmptyView.setVisibility(View.GONE);
        mHolderLayout.setVisibility(View.VISIBLE);
    }


    protected void setEmptyTextContent(String text) {
        mEmptyTextView.setText(text);
    }

    protected void setEmptyTextContent(@StringRes int id) {
        mEmptyTextView.setText(id);
    }

    public void leftClick() {
        hideInput();
        pop();
    }

    protected abstract int getLayoutContent();

    @Override
    protected int contentLayoutId() {
        return 0;
    }


}
