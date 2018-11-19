package com.android.xjcommon.base;

import android.support.annotation.IdRes;

import com.android.xjcommon.helper.XjActivityDelegateHelper;

/**
 * @author ccx
 * @date 2018/11/16
 */
public interface XjSupportActivity {
    void onSupportBackPressed();

    void loadRootFragment(@IdRes int containerId, XjSupportFragment fragment);

    XjActivityDelegateHelper getSupportDelegate();
}
