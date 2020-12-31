package com.android.group.sample.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.common.action.AbstractAction1;
import com.android.component.layoutManager.FocusLayoutManager;
import com.android.group.sample.R;
import com.android.group.sample.adapter.TestAdapter;
import com.android.mvp.view.BaseFragment;
import com.android.group.sample.ui.presenter.Test2Presenter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.functions.Consumer;

/**
 * @author chicunxiang
 */
public class Test2Fragment extends BaseFragment<Test2Presenter> {

    int[] verRes = {R.drawable.v5, R.drawable.v6, R.drawable.v7, R.drawable.v1, R.drawable.v2,
            R.drawable.v3, R.drawable.v4, R.drawable.v5, R.drawable.v6, R.drawable.v7};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"CheckResult", "ResourceType"})
    @Override
    protected void initView(View view) {
        TextView textView = view.findViewById(R.id.next);
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new FocusLayoutManager.Builder()
                .layerPadding(14)
                .normalViewGap(14)
                .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                .isAutoSelect(true)
                .maxLayerCount(3)
                .setOnFocusChangeListener((focusdPosition, lastFocusdPosition) -> {

                })
                .build());
        List<Integer> list1 = Arrays.stream(verRes).boxed().collect(Collectors.toList());;
        recyclerView.setAdapter(new TestAdapter(list1));
    }

    @SuppressLint("ResourceType")
    @Override
    protected View contentLayout() {
        return null;
    }

    @Override
    protected int contentLayoutId() {
        return R.layout.fragment_test2;
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
