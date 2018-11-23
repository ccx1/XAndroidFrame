package com.android.group.sample;

import android.annotation.TargetApi;
import android.os.BatteryManager;
import android.os.Build;
import android.view.View;

import com.android.common.common.Common;
import com.android.mvp.view.BaseActivity;
import com.android.group.sample.ui.fragment.Test1Fragment;

/**
 * @author chicunxiang
 */
public class MainActivity extends BaseActivity<MainPresenter> {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        Common.init(this);
        loadRootFragment(com.mobgeek.android.store.R.id.fl, new Test1Fragment());
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int            battery        = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        System.out.println("" + battery);
    }

    @Override
    protected View contentLayout() {
        return null;
    }

    @Override
    protected int contentLayoutId() {
        return com.mobgeek.android.store.R.layout.activity_main;
    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }


}
