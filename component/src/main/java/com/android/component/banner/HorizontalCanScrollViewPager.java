package com.android.component.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

class HorizontalCanScrollViewPager extends ViewPager {
    public HorizontalCanScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public HorizontalCanScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v == this || v instanceof HorizontalScrollView || v instanceof HorizontalCanScrollViewPager) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
