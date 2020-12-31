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
    protected TextView mErrorTextView;
    private View mErrorView;
    private ViewGroup mHolderLayout;
    protected View mProgress;
    private View mEmptyView;
    protected TextView mErrorTextTooltipView;


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
        mProgress = viewGroup.findViewById(R.id.progress);
        mLeftTextView = viewGroup.findViewById(R.id.tv_left);
        mErrorView = viewGroup.findViewById(R.id.error_view);
        mEmptyView = viewGroup.findViewById(R.id.empty_view);
        mErrorTextView = viewGroup.findViewById(R.id.error_text);
        mErrorTextTooltipView = viewGroup.findViewById(R.id.error_tooltip);
        if (layoutContent != 0) {
            View view = inflater.inflate(layoutContent, null, false);
            mHolderLayout.addView(view);
        }
        mLeftImg.setOnClickListener((v) -> leftClick());
        mLeftGroup.setOnClickListener((v) -> leftClick());
        mErrorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!customRetry()) {
                    retry();
                }
            }
        });
        return viewGroup;
    }

    public boolean customRetry() {
        return false;
    }

    public void retry() {
        if (mPresenter != null) {
            mPresenter.retry();
        }
    }


    public void showErrorView() {
        mErrorView.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
        mHolderLayout.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }


    public void showCustomErrorView(String tooltip) {
        mErrorTextTooltipView.setText(tooltip);
        mErrorView.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
        mHolderLayout.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }




    public void showProgress() {
        mErrorView.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
        mHolderLayout.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }


    public void showContentView() {
        mErrorView.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
        mHolderLayout.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    public void showEmptyView() {
        mErrorView.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
        mHolderLayout.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }


    protected void setEmptyTextContent(String text) {
        mErrorTextView.setText(text);
    }

    protected void setEmptyTextContent(@StringRes int id) {
        mErrorTextView.setText(id);
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
