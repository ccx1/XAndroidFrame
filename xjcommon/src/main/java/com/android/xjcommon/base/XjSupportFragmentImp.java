package com.android.xjcommon.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.android.xjcommon.helper.XjFragmentDelegateHelper;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class XjSupportFragmentImp extends Fragment implements XjSupportFragment {

    public  FragmentActivity         _mActivity;
    private XjFragmentDelegateHelper mXJFragmentDelegateHelper = new XjFragmentDelegateHelper(this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _mActivity = getActivity();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = null;
        if (enter) {
            animation = mXJFragmentDelegateHelper.getEnterAnimation();
        } else {
            animation = mXJFragmentDelegateHelper.getExitAnimation();
        }
        return animation;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mXJFragmentDelegateHelper.onAttach(activity);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mXJFragmentDelegateHelper.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        System.out.println("onResume " + getClass().getName());
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    public void pop() {
        mXJFragmentDelegateHelper.pop();
    }

    public void popTo(Class<?> clazz) {
        popTo(clazz, false);
    }

    public void popTo(Class<?> clazz, boolean includeTargetFragment) {
        mXJFragmentDelegateHelper.popTo(clazz, includeTargetFragment);
    }

    public void start(XjSupportFragmentImp fragment) {
        mXJFragmentDelegateHelper.start(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 返回false 则不消费此事件，如果返回true，则消费此事件
     *
     * @return
     */
    @Override
    public boolean onSupportBackPressed() {
        return false;
    }

}
