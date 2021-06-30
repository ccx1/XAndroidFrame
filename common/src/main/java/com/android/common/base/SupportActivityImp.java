package com.android.common.base;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.common.helper.ActivityDelegateHelper;

/**
 * @author chicunxiang
 */
public class SupportActivityImp extends AppCompatActivity implements SupportActivity {
    private ActivityDelegateHelper mActivityDelegateHelper = new ActivityDelegateHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDelegateHelper.onCreate(savedInstanceState);
    }


    public void start(SupportFragmentImp fragment) {
        mActivityDelegateHelper.start(fragment);
    }

    public void pop() {
        mActivityDelegateHelper.pop();
    }

    /**
     * 关闭至某一个fragment
     *
     * @param targetFragment 目标
     */
    public void popTo(Class<? extends SupportFragment> targetFragment) {
        mActivityDelegateHelper.popTo(targetFragment);
    }

    @Override
    public void loadRootFragment(@IdRes int containerId, SupportFragment fragment) {
        mActivityDelegateHelper.loadRootFragment(containerId, fragment);
    }

    @Override
    public ActivityDelegateHelper getSupportDelegate() {
        return mActivityDelegateHelper;
    }


    /**
     * 监听返回键事件,此方法不做重写
     */
    @Override
    public void onBackPressed() {
        mActivityDelegateHelper.onBackPressed();
    }

    /**
     * 监听返回键，可重写此方法
     */
    @Override
    public void onSupportBackPressed() {
    }


}
