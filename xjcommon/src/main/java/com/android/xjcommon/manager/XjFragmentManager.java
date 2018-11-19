package com.android.xjcommon.manager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.xjcommon.base.XjSupportFragment;

import java.util.ArrayList;
import java.util.List;


public class XjFragmentManager {
    private static List<XjSupportFragment> task = new ArrayList<>();
    private static XjFragmentManager       sFragmentManager;

    private XjFragmentManager() {
    }

    public static XjFragmentManager getInstance() {
        if (sFragmentManager == null) {
            sFragmentManager = new XjFragmentManager();
        }
        return sFragmentManager;
    }

    public void pushOneFragment(XjSupportFragment f) {
        if (f != null && !task.contains(f)) {
            task.add(0, f);
        }
    }

    public void PopOneFragment(XjSupportFragment f) {
        if (task.contains(f)) {
            task.remove(f);
            f = null;
        }
    }

    public int getFragmentTaskSize() {
        return task.size();
    }

    public XjSupportFragment getTopFragment() {
        XjSupportFragment fragment = null;
        if (task.size() != 0) {
            fragment = task.get(0);
        }
        return fragment;
    }

    public List<XjSupportFragment> getWillPopFragments(FragmentManager supportFragmentManager, String tag, boolean includeTargetFragment) {
        Fragment targetFragment = supportFragmentManager.findFragmentByTag(tag);

        List<XjSupportFragment> list = new ArrayList<>();
        if (targetFragment == null) {
            return list;
        }
        int startIndex = -1;
        for (int i = 0; i < task.size(); i++) {
            if (task.get(i) == targetFragment) {
                // 如果等于true。则为包含此
                // 如果为false，则不包含此
                if (includeTargetFragment) {
                    startIndex = i;
                } else {
                    startIndex = i - 1;
                }
                break;
            }
        }
        if (startIndex == -1) {
            return list;
        }
        // startIndex 是目标位置。
        for (int i = 0; i <= startIndex; i++) {
            Fragment fragment = (Fragment) task.get(i);
            //  如果为null，则不需要这样的fragment
            if (fragment != null && fragment.getView() != null) {
                list.add(task.get(i));
            }
        }
        return list;
    }

}
