package com.android.common.helper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.common.R;
import com.android.common.base.SupportActivity;
import com.android.common.base.SupportFragment;
import com.android.common.base.SupportFragmentImp;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class ActivityDelegateHelper {

    private final SupportActivity  mSupportActivity;
    private final FragmentActivity mActivity;

    private TransactionDelegate mXjTransactionDelegate;
    private Animation           mExitAnim;
    private Animation           mEnterAnim;

    public ActivityDelegateHelper(SupportActivity supportActivity) {
        if (supportActivity == null) {
            throw new RuntimeException("Must extends FragmentActivity/AppCompatActivity");
        }
        this.mSupportActivity = supportActivity;
        this.mActivity = (FragmentActivity) supportActivity;
        getTransactionDelegate();
    }

    public void loadRootFragment(int containerId, SupportFragment tofragment) {
        String name = tofragment.getClass().getName();
        loadRootTransaction(getSupportFragmentManager(), containerId, tofragment, name);
    }

    private void loadRootTransaction(FragmentManager supportFragmentManager, int containerId, SupportFragment tofragment, String name) {
        mXjTransactionDelegate.loadRootTransaction(supportFragmentManager, containerId, tofragment, name);
    }

    private FragmentManager getSupportFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    public void start(SupportFragmentImp fragment) {
        start(mActivity.getSupportFragmentManager(), fragment);
    }

    private void start(FragmentManager fragmentManager, SupportFragment tofragment) {
        mXjTransactionDelegate.dispatchStartTransaction(fragmentManager, tofragment);
    }

    public void pop() {
        mXjTransactionDelegate.pop(getSupportFragmentManager());
    }


    public TransactionDelegate getTransactionDelegate() {
        if (mXjTransactionDelegate == null) {
            mXjTransactionDelegate = new TransactionDelegate(mSupportActivity);
        }
        return mXjTransactionDelegate;
    }

    public void onCreate(Bundle savedInstanceState) {

    }


    /**
     * 回退到某一个fragment
     *
     * @param targetFragment 回到目标fragment
     */
    public void popTo(Class<? extends SupportFragment> targetFragment) {
        popTo(targetFragment, false);
    }

    /**
     * 回退到某一个fragment
     *
     * @param targetFragment 回到目标fragment
     */
    public void popTo(Class<? extends SupportFragment> targetFragment, boolean includeTargetFragment) {
        mXjTransactionDelegate.popTo(targetFragment, includeTargetFragment, getSupportFragmentManager());
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

    public void onBackPressed() {
        mXjTransactionDelegate.dispatchBackPressedEvent();
    }


}
