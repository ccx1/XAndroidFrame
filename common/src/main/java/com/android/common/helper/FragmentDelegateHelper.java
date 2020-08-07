package com.android.common.helper;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.android.common.R;
import com.android.common.base.SupportActivity;
import com.android.common.base.SupportFragment;
import com.android.common.base.SupportFragmentImp;

import static com.android.common.helper.TransactionDelegate.FRAGMENTATION_ARG_CONTAINER;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class FragmentDelegateHelper {
    private final SupportFragment mSupportFragment;
    private final Fragment mFragment;
    private TransactionDelegate mTransactionDelegate;
    private SupportActivity mSupport;
    private FragmentActivity mActivity;
    private int mContainerId;
    private Animation mExitAnim;
    private Animation mEnterAnim;

    public FragmentDelegateHelper(SupportFragment fragment) {
        this.mSupportFragment = fragment;
        this.mFragment = (Fragment) fragment;
    }

    public void start(SupportFragmentImp fragment, boolean closeCurrent) {
        mTransactionDelegate.dispatchStartTransaction(getFragmentManager(), fragment, closeCurrent);
    }

    public void startChildToFragment(SupportFragmentImp fragment, boolean closeCurrent) {
        Fragment parentFragment = mFragment.getParentFragment();
        if (parentFragment != null) {
            mTransactionDelegate.dispatchStartTransaction(parentFragment.getFragmentManager(), fragment, closeCurrent);
        }
    }

    /**
     * 跳转
     *
     * @param fragment
     */
    public void start(SupportFragmentImp fragment) {
        mTransactionDelegate.dispatchStartTransaction(getFragmentManager(), fragment, false);
    }

    private FragmentManager getFragmentManager() {
        return mFragment.getFragmentManager();
    }

    /**
     * 回去
     */
    public void popSelf() {
        mTransactionDelegate.pop(getFragmentManager(), (SupportFragment) mFragment);
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
            mExitAnim = AnimationUtils.loadAnimation(mActivity.getApplicationContext(), R.anim.anim_right_fragment_exit);
        }

        return mExitAnim;
    }

    public Animation getEnterAnimation() {
        if (mEnterAnim == null) {
            mEnterAnim = AnimationUtils.loadAnimation(mActivity.getApplicationContext(), R.anim.anim_right_fragment_enter);
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
