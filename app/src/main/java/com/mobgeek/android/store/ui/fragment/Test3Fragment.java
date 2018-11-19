package com.mobgeek.android.store.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.xjcommon.base.XjSupportFragmentImp;
import com.android.xjcommon.bus.XjBus;
import com.android.xjcommon.bus.XjBusSubscriptions;
import com.android.xjcommon.bus.XjEvent;
import com.android.xjdata.helper.SharedPreferencesHelper;

public class Test3Fragment extends XjSupportFragmentImp {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(_mActivity);
        textView.setText("\n\n\n3号界面");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTo(Test1Fragment.class);
                XjBus.get().post(1234132413);
                XjBus.get().post("asdfsdfad");
                XjBus.get().post(new XjEvent<String>("test1", "afsdg"));
            }
        });

        System.out.println(SharedPreferencesHelper.getInstance().get("abc"));
        return textView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        XjBusSubscriptions.unbind(this);
    }
}
