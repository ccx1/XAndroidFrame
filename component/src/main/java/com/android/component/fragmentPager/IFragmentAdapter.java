package com.android.component.fragmentPager;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface IFragmentAdapter {

    void startUpdate(@NonNull ViewGroup container);

    Fragment instantiateItem(@NonNull ViewGroup container, int position);

    void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object);

    void finishUpdate(@NonNull ViewGroup container);

    int getCount();

    void showFragment(@NonNull ViewGroup container, int current, int target);
}
