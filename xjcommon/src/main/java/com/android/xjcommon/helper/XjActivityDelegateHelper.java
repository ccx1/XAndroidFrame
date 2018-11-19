package com.android.xjcommon.helper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.xjcommon.R;
import com.android.xjcommon.base.XjSupportActivity;
import com.android.xjcommon.base.XjSupportActivityImp;
import com.android.xjcommon.base.XjSupportFragment;
import com.android.xjcommon.base.XjSupportFragmentImp;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class XjActivityDelegateHelper {

    private final XjSupportActivity xjSupportActivity;
    private final FragmentActivity  mActivity;

    private XjTransactionDelegate mXjTransactionDelegate;
    private Animation             mExitAnim;
    private Animation             mEnterAnim;

    public XjActivityDelegateHelper(XjSupportActivity xjSupportActivity) {
        if (xjSupportActivity == null)
            throw new RuntimeException("Must extends FragmentActivity/AppCompatActivity");
        this.xjSupportActivity = xjSupportActivity;
        this.mActivity = (FragmentActivity) xjSupportActivity;
        getTransactionDelegate();
    }

    public void loadRootFragment(int containerId, XjSupportFragment tofragment) {
        String name = tofragment.getClass().getName();
        loadRootTransaction(getSupportFragmentManager(), containerId, tofragment, name);
    }

    private void loadRootTransaction(FragmentManager supportFragmentManager, int containerId, XjSupportFragment tofragment, String name) {
        mXjTransactionDelegate.loadRootTransaction(supportFragmentManager, containerId, tofragment, name);
    }

    private FragmentManager getSupportFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    public void start(XjSupportFragmentImp fragment) {
        start(mActivity.getSupportFragmentManager(), fragment);
    }

    private void start(FragmentManager fragmentManager, XjSupportFragment tofragment) {
        mXjTransactionDelegate.dispatchStartTransaction(fragmentManager, tofragment);
    }

    public void pop() {
        mXjTransactionDelegate.pop(getSupportFragmentManager());
    }


    public XjTransactionDelegate getTransactionDelegate() {
        if (mXjTransactionDelegate == null) {
            mXjTransactionDelegate = new XjTransactionDelegate(xjSupportActivity);
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
    public void popTo(Class<? extends XjSupportFragment> targetFragment) {
        popTo(targetFragment, false);
    }

    /**
     * 回退到某一个fragment
     *
     * @param targetFragment 回到目标fragment
     */
    public void popTo(Class<? extends XjSupportFragment> targetFragment, boolean includeTargetFragment) {
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
