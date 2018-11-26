package com.android.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author ccx
 * @date 2018/11/26
 */
public class BaseCommonViewPagerAdapter extends PagerAdapter {


    private List<View>   mViews;
    private List<String> mTitle;

    public BaseCommonViewPagerAdapter(List<View> views, List<String> title) {
        this.mViews = views;
        this.mTitle = title;
    }

    public BaseCommonViewPagerAdapter(List<View> views) {
        this(views, null);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitle != null) {
            return mTitle.get(position);
        }
        return super.getPageTitle(position);
    }
}
