package com.android.group.sample.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.common.base.SupportFragmentImp;
import com.android.common.bus.RxBus;
import com.android.common.bus.RxBusSubscriptions;
import com.android.common.bus.Event;
import com.android.data.helper.SharedPreferencesHelper;

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
                RxBus.get().post(1234132413);
                RxBus.get().post("asdfsdfad");
                RxBus.get().post(new Event<String>("test1", "afsdg"));
            }
        });

        System.out.println(SharedPreferencesHelper.getInstance().get("abc"));
        return textView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBusSubscriptions.unbind(this);
    }
}
