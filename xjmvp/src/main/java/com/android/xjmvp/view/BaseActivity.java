package com.android.xjmvp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.xjcommon.action.AbstractAction1;
import com.android.xjcommon.base.SupportActivityImp;
import com.android.xjcommon.helper.PermissionsHelper;
import com.android.xjmvp.R;
import com.android.xjmvp.presenter.BasePresenter;
import com.android.xjmvp.widget.StatusLayout;

/**
 * @author ccx
 * @date 2018/11/22
 */
public abstract class BaseActivity<P extends BasePresenter> extends SupportActivityImp implements BaseView {

    private              P                 mPresenter;
    private              PermissionsHelper mPermissionsHelper;
    private              StatusLayout      mStatusLayout;
    private static final int               DEFAULT_BASE_CONTENT_ID = 100092;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.attachView(this, this);
        super.setContentView(R.layout.activity_base);
        initCenterView();
        initView();
    }

    /**
     * 初始化
     */
    protected abstract void initView();

    private void initCenterView() {
        mStatusLayout = findViewById(R.id.base_status_layout);
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

    /**
     * 返回一个view的容器
     * @return
     */
    protected abstract View contentLayout();

    /**
     * 返回一个view的id
     * @return
     */
    protected abstract int contentLayoutId();

    @Override
    public void setContentView(int layoutResID) {
        mStatusLayout.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        if (view.getId() == -1) {
            view.setId(DEFAULT_BASE_CONTENT_ID);
        }
        mStatusLayout.setContentView(view);
    }

    /**
     * 初始化P层
     * @return
     */
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void requestPermission(String[] permissions, AbstractAction1<Boolean> action1) {
        // 避免造成不必要的浪费
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
        // 防止没有请求权限直接setResult，导致程序崩溃
        if (mPermissionsHelper != null) {
            mPermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
