package com.mobgeek.android.store;

import android.view.View;

import com.android.xjcommon.common.Common;
import com.android.xjmvp.view.BaseActivity;
import com.mobgeek.android.store.ui.fragment.Test1Fragment;

public class MainActivity extends BaseActivity<MainPresenter> {

    @Override
    protected void initView() {
        Common.init(this);
        loadRootFragment(R.id.fl, new Test1Fragment());
    }

    @Override
    protected View contentLayout() {
        return null;
    }

    @Override
    protected int contentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }


}
