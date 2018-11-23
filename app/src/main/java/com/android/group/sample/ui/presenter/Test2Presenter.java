package com.android.group.sample.ui.presenter;

import com.android.http.callback.ResponseCallback;
import com.android.http.model.ResultModel;
import com.android.mvp.presenter.BasePresenterImp;
import com.android.group.sample.network.HttpHelper;
import com.android.group.sample.ui.fragment.Test2Fragment;

/**
 * @author ccx
 * @date 2018/11/21
 */
public class Test2Presenter extends BasePresenterImp<Test2Fragment> {

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
