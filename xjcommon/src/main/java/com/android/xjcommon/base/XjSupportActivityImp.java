package com.android.xjcommon.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.xjcommon.helper.XjActivityDelegateHelper;

public class XjSupportActivityImp extends AppCompatActivity implements XjSupportActivity {
    private XjActivityDelegateHelper mXjActivityDelegateHelper = new XjActivityDelegateHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mXjActivityDelegateHelper.onCreate(savedInstanceState);
    }


    public void start(XjSupportFragmentImp fragment) {
        mXjActivityDelegateHelper.start(fragment);
    }

    public void pop() {
        mXjActivityDelegateHelper.pop();
    }


    public void popTo(Class<? extends XjSupportFragment> targetFragment) {
        mXjActivityDelegateHelper.popTo(targetFragment);
    }

    @Override
    public void loadRootFragment(@IdRes int containerId, XjSupportFragment fragment) {
        mXjActivityDelegateHelper.loadRootFragment(containerId, fragment);
    }

    @Override
    public XjActivityDelegateHelper getSupportDelegate() {
        return mXjActivityDelegateHelper;
    }


    /**
     * 监听返回键事件
     */
    @Override
    public void onBackPressed() {
        mXjActivityDelegateHelper.onBackPressed();
    }

    @Override
    public void onSupportBackPressed() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }
}
