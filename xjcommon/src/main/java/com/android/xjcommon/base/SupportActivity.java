package com.android.xjcommon.base;

import android.support.annotation.IdRes;

import com.android.xjcommon.helper.ActivityDelegateHelper;

/**
 * @author ccx
 * @date 2018/11/16
 */
public interface SupportActivity {
    /**
     * 返回键触发
     */
    void onSupportBackPressed();

    /**
     * 加载根部fragment
     * @param containerId
     * @param fragment
     */
    void loadRootFragment(@IdRes int containerId, SupportFragment fragment);

    /**
     * getSupportDelegate
     * @return
     */
    ActivityDelegateHelper getSupportDelegate();
}
