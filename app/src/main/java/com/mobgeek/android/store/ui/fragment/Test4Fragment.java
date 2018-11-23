package com.mobgeek.android.store.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.common.base.SupportFragmentImp;
import com.android.common.bus.EventBus;
import com.android.common.bus.EventBusSubscriptions;
import com.android.common.bus.Event;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author chicunxiang
 */
public class Test4Fragment extends SupportFragmentImp {
    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(mActivity);
        textView.setText(" \n\n4号界面");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new Test3Fragment());
            }
        });
        EventBusSubscriptions.bindAll(this,
                EventBus.get().subscribe(Event.class).map(new Function<Event, Event>() {
                    @Override
                    public Event apply(Event o) throws Exception {
                        return o;
                    }
                }).subscribe(new Consumer<Event>() {
                    @Override
                    public void accept(Event s) throws Exception {
                        System.out.println("我在4号界面收到了 " + s.getTag());
                    }
                }));


        return textView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusSubscriptions.unbind(this);
    }
}
