package com.android.group.sample;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import com.android.common.common.Common;
import com.android.component.FileSelectPicker;
import com.android.group.sample.ui.fragment.Test1Fragment;
import com.android.mvp.view.BaseActivity;

/**
 * @author chicunxiang
 */
public class MainActivity extends BaseActivity<MainPresenter> {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        Common.init(this);
        loadRootFragment(R.id.fl, new Test1Fragment());
//        UpdateApkDialog updateApkDialog = new UpdateApkDialog(this);
//        updateApkDialog.popDialog();
//        updateApkDialog.setDownloadApkPathUrl("res/test.apk");
//        updateApkDialog.setDownloadApkSavePath(Environment.getExternalStorageDirectory() + File.separator + "test.apk");
//        TimePickerDialog timePickerDialog = new TimePickerDialog(this);
//        timePickerDialog.show();

        FileSelectPicker fileSelectPicker = new FileSelectPicker(this);
        fileSelectPicker.show();
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
