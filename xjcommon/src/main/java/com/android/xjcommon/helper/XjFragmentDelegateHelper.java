package com.android.xjcommon.helper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.xjcommon.R;
import com.android.xjcommon.base.XjSupportActivity;
import com.android.xjcommon.base.XjSupportFragmentImp;

import static com.android.xjcommon.helper.XjTransactionDelegate.FRAGMENTATION_ARG_CONTAINER;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class XjFragmentDelegateHelper {
    private final XjSupportFragmentImp  mXjF;
    private final Fragment              mFragment;
    private       XjTransactionDelegate mTransactionDelegate;
    private       XjSupportActivity     mSupport;
    private       FragmentActivity      _mActivity;
    public        int                   mContainerId;
    private       Animation             mExitAnim;
    private       Animation             mEnterAnim;

    public XjFragmentDelegateHelper(XjSupportFragmentImp fragment) {
        this.mXjF = fragment;
        this.mFragment = (Fragment) fragment;
    }

    /**
     * 跳转
     *
     * @param fragment
     */
    public void start(XjSupportFragmentImp fragment) {
        mTransactionDelegate.dispatchStartTransaction(getFragmentManager(), fragment);
    }

    private FragmentManager getFragmentManager() {
        return mFragment.getFragmentManager();
    }

    /**
     * 回去
     */
    public void pop() {
        mTransactionDelegate.pop(getFragmentManager());
    }


    public void popTo(Class<?> clazz) {
        popTo(clazz, false);
    }

    public void popTo(Class<?> clazz, boolean includeTargetFragment) {
        mTransactionDelegate.popTo(clazz, includeTargetFragment, getFragmentManager());
    }

    /**
     * 创建fragment
     *
     * @param savedInstanceState 回传
     */
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = mFragment.getArguments();
        if (arguments != null) {
            mContainerId = arguments.getInt(FRAGMENTATION_ARG_CONTAINER);
        }
    }

    public Animation getExitAnimation() {
        if (mExitAnim == null) {
            mExitAnim = AnimationUtils.loadAnimation(_mActivity.getApplicationContext(), R.anim.anim_fragment_exit);
        }

        return mExitAnim;
    }

    public Animation getEnterAnimation() {
        if (mEnterAnim == null) {
            mEnterAnim = AnimationUtils.loadAnimation(_mActivity.getApplicationContext(), R.anim.anim_fragment_enter);
        }
        return mEnterAnim;
    }


    /**
     * 当fragment被添加时。。
     *
     * @param activity
     */
    public void onAttach(Activity activity) {
        if (activity instanceof XjSupportActivity) {
            this.mSupport = (XjSupportActivity) activity;
            this._mActivity = (FragmentActivity) activity;
            mTransactionDelegate = mSupport.getSupportDelegate().getTransactionDelegate();
        } else {
            throw new RuntimeException(activity.getClass().getSimpleName() + " must impl ISupportActivity!");
        }
    }


    public void onSaveInstanceState(Bundle outState) {
        // 保存数据，防止下次重影
        Bundle arguments = mFragment.getArguments();
        if (arguments != null) {
            mFragment.onSaveInstanceState(arguments);
        }
    }
}
