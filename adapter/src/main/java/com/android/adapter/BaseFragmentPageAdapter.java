package com.android.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author ccx
 * @date 2018/11/26
 */
public class BaseFragmentPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments;
    private final String[]       mTitles;

    public BaseFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    public BaseFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        this(fm, fragments, (String[]) null);
    }

    public BaseFragmentPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        this(fm, fragments, titles.toArray(new String[titles.size()]));
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            return mTitles[position];
        }
        return super.getPageTitle(position);
    }
}
