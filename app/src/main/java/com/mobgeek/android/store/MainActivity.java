package com.mobgeek.android.store;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.android.xjcommon.base.XjSupportActivityImp;
import com.android.xjcommon.common.Common;
import com.mobgeek.android.store.ui.fragment.Test1Fragment;

public class MainActivity extends XjSupportActivityImp {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Common.init(this);
        loadRootFragment(R.id.fl, new Test1Fragment());


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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
