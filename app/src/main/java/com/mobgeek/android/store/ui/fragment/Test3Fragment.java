package com.mobgeek.android.store.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.xjcommon.base.SupportFragmentImp;
import com.android.xjcommon.bus.EventBus;
import com.android.xjcommon.bus.EventBusSubscriptions;
import com.android.xjcommon.bus.Event;
import com.android.xjdata.helper.SharedPreferencesHelper;

/**
 * @author chicunxiang
 */
public class Test3Fragment extends SupportFragmentImp {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(mActivity);
        textView.setText("\n\n\n3号界面");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTo(Test1Fragment.class);
                EventBus.get().post(1234132413);
                EventBus.get().post("asdfsdfad");
                EventBus.get().post(new Event<String>("test1", "afsdg"));
            }
        });

        System.out.println(SharedPreferencesHelper.getInstance().get("abc"));
        return textView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusSubscriptions.unbind(this);
    }
}
