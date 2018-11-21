package com.mobgeek.android.store.ui.fragment;

import android.Manifest;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.android.xjcommon.bus.XjBus;
import com.android.xjcommon.bus.XjBusSubscriptions;
import com.android.xjcommon.helper.XjPermissionsHelper;
import com.android.xjmvp.view.XjBaseFragment;
import com.mobgeek.android.store.ui.Test2Presenter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class Test2Fragment extends XjBaseFragment<Test2Presenter> {


    private XjPermissionsHelper mPermissionsHelper;

    @Override
    protected void initView(View view) {
        TextView textView = view.findViewById(40097);
        textView.setText("\n2号界面加载成功");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new Test4Fragment());
            }
        });
        Disposable subscribe = XjBus.get().subscribe(Integer.class).map(new Function<Object, Object>() {
            @Override
            public Object apply(Object o) {
                return o;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object s) throws Exception {
                System.out.println("我在2号界面收到了 " + s);
            }
        });

        XjBusSubscriptions.bind(this, subscribe);
        mPermissionsHelper = new XjPermissionsHelper(this);
        mPermissionsHelper
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        System.out.println(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("完成");
                    }
                });
    }

    @Override
    protected View contentLayout() {
        TextView textView = new TextView(_mActivity);
        textView.setId(40097);
        return textView;
    }

    @Override
    protected int contentLayoutId() {
        return 0;
    }

    @Override
    public Test2Presenter initPresenter() {
        return new Test2Presenter();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        XjBusSubscriptions.unbind(this);
        mPresenter.onDestroy();
    }
}
