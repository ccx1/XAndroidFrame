package com.android.group.sample.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.android.common.action.AbstractAction1;
import com.android.mvp.view.BaseFragment;
import com.android.group.sample.ui.presenter.Test2Presenter;

import io.reactivex.functions.Consumer;

/**
 * @author chicunxiang
 */
public class Test2Fragment extends BaseFragment<Test2Presenter> {


    @SuppressLint({"CheckResult", "ResourceType"})
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

        mPresenter.createBusInstance(Integer.class, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("我在2号界面收到了 " + integer);
            }
        });

        requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new AbstractAction1<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @SuppressLint("ResourceType")
    @Override
    protected View contentLayout() {
        TextView textView = new TextView(mActivity);
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
    public void onDestroyView() {
        super.onDestroyView();
    }
}
