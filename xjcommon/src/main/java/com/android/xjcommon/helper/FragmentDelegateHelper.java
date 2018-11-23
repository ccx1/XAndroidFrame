package com.android.xjcommon.helper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.xjcommon.R;
import com.android.xjcommon.base.SupportActivity;
import com.android.xjcommon.base.SupportFragment;
import com.android.xjcommon.base.SupportFragmentImp;

import static com.android.xjcommon.helper.TransactionDelegate.FRAGMENTATION_ARG_CONTAINER;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class FragmentDelegateHelper {
    private final SupportFragment     mSupportFragment;
    private final Fragment            mFragment;
    private       TransactionDelegate mTransactionDelegate;
    private       SupportActivity     mSupport;
    private       FragmentActivity    mActivity;
    private       int                 mContainerId;
    private       Animation           mExitAnim;
    private       Animation           mEnterAnim;

    public FragmentDelegateHelper(SupportFragment fragment) {
        this.mSupportFragment = fragment;
        this.mFragment = (Fragment) fragment;
    }

    /**
     * 跳转
     *
     * @param fragment
     */
    public void start(SupportFragmentImp fragment) {
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
            mExitAnim = AnimationUtils.loadAnimation(mActivity.getApplicationContext(), R.anim.anim_fragment_exit);
        }

        return mExitAnim;
    }

    public Animation getEnterAnimation() {
        if (mEnterAnim == null) {
            mEnterAnim = AnimationUtils.loadAnimation(mActivity.getApplicationContext(), R.anim.anim_fragment_enter);
        }
        return mEnterAnim;
    }


    /**
     * 当fragment被添加时。。
     *
     * @param activity
     */
    public void onAttach(Activity activity) {
        if (activity instanceof SupportActivity) {
            this.mSupport = (SupportActivity) activity;
            this.mActivity = (FragmentActivity) activity;
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
