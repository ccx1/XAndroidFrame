package com.android.common.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chicunxiang
 */
public class ActivityManager {

    private static ActivityManager sActivityManager;
    private List<Activity> mActivitiesTask = new ArrayList<>();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (sActivityManager == null) {
            sActivityManager = new ActivityManager();
        }
        return sActivityManager;
    }


    public void pushOnActivity(Activity activity) {
        // 需要将其放到栈顶
        mActivitiesTask.add(0, activity);
    }

    public void popOneActivity(Activity activity) {
        // 删除当前的.并且关闭
        if (mActivitiesTask.contains(activity)) {
            mActivitiesTask.remove(activity);
            // 如果已经关闭，则只用删除，不用执行关闭
            if (activity.isFinishing()) {
                activity.finish();
            }
        }
        activity = null;
    }

    public Activity getTaskTopActivity() {
        Activity activity = null;
        if (mActivitiesTask.size() != 0) {
            activity = mActivitiesTask.get(0);
        }
        return activity;
    }


    public int getTaskActivitySize() {
        return mActivitiesTask.size();
    }

    public void finishAllActivity() {
        for (Activity activity : mActivitiesTask) {
            popOneActivity(activity);
        }
    }

}
