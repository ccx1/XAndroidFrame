package com.android.xjmvp.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.xjcommon.action.Action1;
import com.android.xjcommon.base.XjSupportFragmentImp;
import com.android.xjcommon.helper.XjPermissionsHelper;
import com.android.xjmvp.R;
import com.android.xjmvp.presenter.XjBasePresenter;
import com.android.xjmvp.widget.StatusLayout;

/**
 * @author ccx
 * @date 2018/11/21
 */
public abstract class XjBaseFragment<P extends XjBasePresenter> extends XjSupportFragmentImp implements XjBaseView {

    public  P                   mPresenter;
    private StatusLayout        mStatusLayout;
    private XjPermissionsHelper mPermissionsHelper;

    @Override
    @SuppressWarnings("unchecked")
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
    public void requestPermission(String[] permissions, Action1<Boolean> action1) {
        // 如果之前还没有请求过权限，则需要创建一个权限类
        if (mPermissionsHelper == null) {
            mPermissionsHelper = new XjPermissionsHelper(this);
        }
        mPermissionsHelper
                .request(permissions)
                .subscribe(action1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 防止其他类型的result
        if (mPermissionsHelper != null) {
            mPermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }
}
