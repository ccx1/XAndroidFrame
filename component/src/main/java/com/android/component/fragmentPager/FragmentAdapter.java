package com.android.component.fragmentPager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



public abstract class FragmentAdapter implements IFragmentAdapter {
    private static final String TAG = "FragmentPagerAdapter";
    private static final boolean DEBUG = false;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    public FragmentAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }


    public abstract Fragment getItem(int position);

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("FragmentPaper with adapter " + this
                    + " requires a view id");
        }
    }

    @Override
    public void showFragment(@NonNull ViewGroup container, int current, int target) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        long itemId = getItemId(target);
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment == null) {
            // 如果等于null. 先加载进来. 再show, 那么target就等于要加载的index. 所以我需要attach, target的fragment
            // 获取目标的target. 加载缓存
            startUpdate(container);
            instantiateItem(container,target);
            finishUpdate(container);
            showFragment(container,current,target);
            return;
        }
        mCurTransaction.hide(getItem(current)).show(getItem(target));
    }

    @SuppressWarnings("ReferenceEquality")
    @NonNull
    @Override
    public Fragment instantiateItem(@NonNull ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        fragment = attachFragment(container, position, itemId, fragment);

        return fragment;
    }

    private Fragment attachFragment(@NonNull ViewGroup container, int position, long itemId, Fragment fragment) {
        // 主要是这里// 缓存加载. 第一次进来的时候. 先不加载全部. 先加载单个..点击一次加载一次. 点击一次加载一次.
        if (fragment != null) {
            Log.i(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            Log.i(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment,
                    makeFragmentName(container.getId(), itemId)).hide(fragment);
        }
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        Log.i(TAG, "Detaching item #" + getItemId(position) + ": f=" + getItem(position)
                + " v=" + getItem(position).getView());

        mCurTransaction.detach(getItem(position));
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }


    private long getItemId(int position) {
        return position;
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:fragment:paper:switcher:" + viewId + ":" + id;
    }
}
