package com.android.component.fragmentPager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentPager extends FrameLayout {
    private IFragmentAdapter mAdapter;
    private List<ItemInfo> mItemInfo = new ArrayList<>();
    private int mCurItem;
    private int pageOffset;
    private boolean lazyLoad;

    public FragmentPager(@NonNull Context context) {
        super(context);
    }

    public FragmentPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFragmentsAdapter(IFragmentAdapter adapter) {
        // 先销毁前一个
        if (mAdapter != null) {
            // 检测id是否正常
            mAdapter.startUpdate(this);
            for (int i = 0; i < mAdapter.getCount(); i++) {
                ItemInfo itemInfo = mItemInfo.get(i);
                mAdapter.destroyItem(this, itemInfo.getPosition(), itemInfo.getFragment());
            }
            // 完成更新
            mAdapter.finishUpdate(this);
            mItemInfo.clear();
            mCurItem = 0;
        }
        this.mAdapter = adapter;

        if (mAdapter != null && !lazyLoad) {
            mAdapter.startUpdate(this);
            for (int i = 0; i < mAdapter.getCount(); i++) {
                mItemInfo.add(new ItemInfo(mAdapter.instantiateItem(this, i), i));
            }
            mAdapter.finishUpdate(this);
        }
    }

    /**
     * 设置当前
     *
     * @param i
     */
    public void setCurrentItem(int i) {
        if (mCurItem == i) {
            return;
        }
        mAdapter.startUpdate(this);
        mAdapter.showFragment(this, mCurItem, i);
        mAdapter.finishUpdate(this);
        mCurItem = i;
    }

    public int getCurItem() {
        return mCurItem;
    }

    /**
     * 预加载，处理。
     *
     * @param i
     */
    public void setOffscreenPageLimit(int i) {
        this.pageOffset = i;
    }

    public void setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }


    private static class ItemInfo {
        private Fragment mFragment;
        private int position;


        ItemInfo(Fragment fragment, int position) {
            mFragment = fragment;
            this.position = position;
        }

        public Fragment getFragment() {
            return mFragment;
        }


        public int getPosition() {
            return position;
        }

    }
}
