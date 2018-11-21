package com.android.xjmvp.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.xjcommon.base.XjSupportFragmentImp;
import com.android.xjmvp.R;
import com.android.xjmvp.presenter.XjBasePresenter;
import com.android.xjmvp.widget.StatusLayout;

/**
 * @author ccx
 * @date 2018/11/21
 */
public abstract class XjBaseFragment<P extends XjBasePresenter> extends XjSupportFragmentImp implements XjBaseView {

    public  P            mPresenter;
    private StatusLayout mStatusLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.attachView(this, getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        initCenterView(view);
        return view;
    }

    private void initCenterView(View view) {
        mStatusLayout = view.findViewById(R.id.base_status_layout);
        int layoutId = contentLayoutId();
        if (layoutId != 0) {
            mStatusLayout.setContentView(layoutId);
        } else {
            mStatusLayout.setContentView(contentLayout());
        }
        mStatusLayout.setRetryOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.retry();
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    protected abstract void initView(View view);

    protected abstract View contentLayout();

    @LayoutRes
    protected abstract int contentLayoutId();


    public abstract P initPresenter();

    @Override
    public void showContent() {
        mStatusLayout.showContent();
    }

    @Override
    public void showLoading() {
        mStatusLayout.showLoading();
    }

    @Override
    public void showError() {
        mStatusLayout.showError();
    }

    @Override
    public void showEmpty() {
        mStatusLayout.showEmpty();
    }
}
