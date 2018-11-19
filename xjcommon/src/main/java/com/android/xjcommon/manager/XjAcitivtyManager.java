package com.android.xjcommon.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class XjAcitivtyManager {

    private static XjAcitivtyManager sXjAcitivtyManager;
    private        List<Activity>    mActivitieTask = new ArrayList<>();

    private XjAcitivtyManager() {
    }

    public static XjAcitivtyManager getInstance() {
        if (sXjAcitivtyManager == null) {
            sXjAcitivtyManager = new XjAcitivtyManager();
        }
        return sXjAcitivtyManager;
    }


    public void pushOnActivity(Activity activity) {
        // 需要将其放到栈顶
        mActivitieTask.add(0, activity);
    }

    public void popOneActivity(Activity activity) {
        // 删除当前的.并且关闭
        if (mActivitieTask.contains(activity)) {
            mActivitieTask.remove(activity);
            // 如果已经关闭，则只用删除，不用执行关闭
            if (activity.isFinishing()) {
                activity.finish();
            }
        }
        activity = null;
    }

    public Activity getTaskTopActivity() {
        Activity activity = null;
        if (mActivitieTask.size() != 0) {
            activity = mActivitieTask.get(0);
        }
        return activity;
    }


    public int getTaskActivitySize() {
        return mActivitieTask.size();
    }

    public void finishAllActivity() {
        for (Activity activity : mActivitieTask) {
            popOneActivity(activity);
        }
    }

}
