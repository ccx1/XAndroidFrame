package com.android.common.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.common.helper.ActivityDelegateHelper;

/**
 * @author chicunxiang
 */
public class SupportActivityImp extends AppCompatActivity implements SupportActivity {
    private ActivityDelegateHelper mXjActivityDelegateHelper = new ActivityDelegateHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mXjActivityDelegateHelper.onCreate(savedInstanceState);
    }


    public void start(SupportFragmentImp fragment) {
        mXjActivityDelegateHelper.start(fragment);
    }

    public void pop() {
        mXjActivityDelegateHelper.pop();
    }


    public void popTo(Class<? extends SupportFragment> targetFragment) {
        mXjActivityDelegateHelper.popTo(targetFragment);
    }

    @Override
    public void loadRootFragment(@IdRes int containerId, SupportFragment fragment) {
        mXjActivityDelegateHelper.loadRootFragment(containerId, fragment);
    }

    @Override
    public ActivityDelegateHelper getSupportDelegate() {
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


}
