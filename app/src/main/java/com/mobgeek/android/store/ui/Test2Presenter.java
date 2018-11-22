package com.mobgeek.android.store.ui;

import com.android.xjhttp.callback.ResponseCallback;
import com.android.xjhttp.model.ResultModel;
import com.android.xjmvp.presenter.XjBasePresenterImp;
import com.mobgeek.android.store.network.HttpHelper;
import com.mobgeek.android.store.ui.fragment.Test2Fragment;

/**
 * @author ccx
 * @date 2018/11/21
 */
public class Test2Presenter extends XjBasePresenterImp<Test2Fragment> {

    @Override
    public void initPresenterData() {
        // 初始化界面展示数据
        setData();
    }


    @Override
    public void setData() {
        HttpHelper.get2(new ResponseCallback<ResultModel<String>>() {
            @Override
            public void onResponse(ResultModel<String> response) {
                System.out.println(response);
                mView.showContent();
            }

            @Override
            public void onFailure(String msg) {
                System.out.println(msg);
                mView.showError();
            }
        });
    }

    @Override
    public void retry() {
        setData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 执行销毁操作
    }

}
