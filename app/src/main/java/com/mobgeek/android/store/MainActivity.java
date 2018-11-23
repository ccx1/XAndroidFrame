package com.mobgeek.android.store;

import android.annotation.TargetApi;
import android.os.BatteryManager;
import android.os.Build;
import android.view.View;

import com.android.common.common.Common;
import com.android.mvp.view.BaseActivity;
import com.mobgeek.android.store.ui.fragment.Test1Fragment;

/**
 * @author chicunxiang
 */
public class MainActivity extends BaseActivity<MainPresenter> {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        Common.init(this);
        loadRootFragment(R.id.fl, new Test1Fragment());
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
        return R.layout.activity_main;
    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }


}
