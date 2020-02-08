package com.android.mvp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.common.action.AbstractAction1;
import com.android.common.base.SupportFragmentImp;
import com.android.common.helper.PermissionsHelper;
import com.android.mvp.R;
import com.android.mvp.presenter.BasePresenter;
import com.android.mvp.widget.StatusLayout;

/**
 * @author ccx
 * @date 2018/11/21
 */
public abstract class BaseFragment<P extends BasePresenter> extends SupportFragmentImp implements BaseView {

    public  P                 mPresenter;
    private StatusLayout      mStatusLayout;
    private PermissionsHelper mPermissionsHelper;

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this, getActivity());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        initCenterView(view);
        return view;
    }

    private void initCenterView(View view) {
        mStatusLayout = view.findViewById(R.id.base_status_layout);
        int layoutId = contentLayoutId();
        if (layoutId != 0) {
            mStatusLayout.setContentView(layoutId);
        } else {
            mStatusLayout.setContentView(contentLayout());
        }
        mStatusLayout.setRetryOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.retry();
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    /**
     * 初始化view
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 返回一个view
     * @return view
     */
    protected abstract View contentLayout();

    /**
     * 返回一个view的id
     * @return int
     */
    @LayoutRes
    protected abstract int contentLayoutId();

    /**
     * 初始化p层
     * @return p
     */
    public abstract P initPresenter();

    @Override
    public void requestPermission(String[] permissions, AbstractAction1<Boolean> action1) {
        // 如果之前还没有请求过权限，则需要创建一个权限类
        if (mPermissionsHelper == null) {
            mPermissionsHelper = new PermissionsHelper(this);
        }
        mPermissionsHelper
                .request(permissions)
                .subscribe(action1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 防止其他类型的result
        if (mPermissionsHelper != null) {
            mPermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void showContent() {
        mStatusLayout.showContent();
    }

    @Override
    public void showLoading() {
        mStatusLayout.showLoading();
    }

    @Override
    public void showError() {
        mStatusLayout.showError();
    }

    @Override
    public void showEmpty() {
        mStatusLayout.showEmpty();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
