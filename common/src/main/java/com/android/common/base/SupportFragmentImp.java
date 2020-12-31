package com.android.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.common.helper.FragmentDelegateHelper;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class SupportFragmentImp extends Fragment implements SupportFragment {

    public FragmentActivity mActivity;
    private FragmentDelegateHelper mFragmentDelegateHelper = new FragmentDelegateHelper(this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = null;
        if (enter) {
            animation = mFragmentDelegateHelper.getEnterAnimation();
        } else {
            animation = mFragmentDelegateHelper.getExitAnimation();
        }
        return animation;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentDelegateHelper.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentDelegateHelper.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        System.out.println("onResume " + getClass().getName());
        super.onResume();
    }

    @Override
    public void onPause() {
        System.out.println("onPause " + getClass().getName());
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    public void pop() {
        mFragmentDelegateHelper.pop();
    }

    public void popSelf() {
        mFragmentDelegateHelper.popSelf();
    }

    public void popTo(Class<?> clazz) {
        popTo(clazz, false);
    }

    public void popTo(Class<?> clazz, boolean includeTargetFragment) {
        mFragmentDelegateHelper.popTo(clazz, includeTargetFragment);
    }

    public void start(SupportFragmentImp fragment) {
        mFragmentDelegateHelper.start(fragment);
    }

    public void start(SupportFragmentImp fragment, boolean closeCurrent) {
        mFragmentDelegateHelper.start(fragment, closeCurrent);
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
