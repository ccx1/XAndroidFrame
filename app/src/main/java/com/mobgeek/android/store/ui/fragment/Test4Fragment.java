package com.mobgeek.android.store.ui.fragment;

import android.annotation.SuppressLint;
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

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class Test4Fragment extends XjSupportFragmentImp {
    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(_mActivity);
        textView.setText(" \n\n4号界面");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new Test3Fragment());
            }
        });
        XjBusSubscriptions.bind(this,
                XjBus.get().subscribe(XjEvent.class).map(new Function<XjEvent, XjEvent>() {
                    @Override
                    public XjEvent apply(XjEvent o) throws Exception {
                        return o;
                    }
                }).subscribe(new Consumer<XjEvent>() {
                    @Override
                    public void accept(XjEvent s) throws Exception {
                        System.out.println("我在4号界面收到了 " + s.getTag());
                    }
                }));


        return textView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        XjBusSubscriptions.unbind(this);
    }
}
