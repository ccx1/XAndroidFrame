package com.android.component.banner;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class LoopAdapter extends PagerAdapter {
    private List<View> mViewList = new ArrayList<>();

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int neoPosition = position % getItemCount();
        View view = findViewByPosition(container, neoPosition);
        container.addView(view);
        return view;
    }

    private View findViewByPosition(ViewGroup container, int neoPosition) {
        // 需要存储其他的view，保证渲染正常的的情况。
        // 【-2】 【-1】 【0】 【1】 【2】
        // 渲染保证的是左右加3.所以。当基数大于3的时候，才进行数据复用的处理
        for (View view : mViewList) {
            // 判断其可用性，保证复用的问题, 因为是字符串，所以要使用equals..如果parent == null，说明要开始复用了。
            if (view.getTag().equals("LoopViewTag" + neoPosition) && view.getParent() == null) {
                return view;
            }
        }
        View view = getItemView(container,neoPosition);
        view.setTag("LoopViewTag" + neoPosition);
        mViewList.add(view);
        return view;
    }

    protected abstract View getItemView(ViewGroup container, int position);

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return getItemCount() < 2 ? getItemCount() : (Integer.MAX_VALUE);
    }

    protected abstract int getItemCount();


}
