package com.android.xjmvp.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.xjcommon.action.AbstractAction1;
import com.android.xjcommon.base.SupportFragmentImp;
import com.android.xjcommon.helper.PermissionsHelper;
import com.android.xjmvp.R;
import com.android.xjmvp.presenter.BasePresenter;
import com.android.xjmvp.widget.StatusLayout;

/**
 * @author ccx
 * @date 2018/11/21
 */
public abstract class BaseFragment<P extends BasePresenter> extends SupportFragmentImp implements BaseView {

    public  P                 mPresenter;
    private StatusLayout      mStatusLayout;
    private PermissionsHelper mPermissionsHelper;

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

    /**
     * 初始化view
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 返回一个view
     * @return
     */
    protected abstract View contentLayout();

    /**
     * 返回一个view的id
     * @return
     */
    @LayoutRes
    protected abstract int contentLayoutId();

    /**
     * 初始化p层
     * @return
     */
    public abstract P initPresenter();

    @Override
    public void requestPermission(String[] permissions, AbstractAction1<Boolean> action1) {
        // 如果之前还没有请求过权限，则需要创建一个权限类
        if (mPermissionsHelper == null) {
            mPermissionsHelper = new PermissionsHelper(this);
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
