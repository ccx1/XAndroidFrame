package com.android.install.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.common.common.Logger;

/**
 * Use: 版本管理器，负责版本校验。弹窗等等.
 * Author：cunxiangchi@gamil.com
 * Time: 2020/7/10
 */
public class VersionManager {

    private static final String TAG = VersionManager.class.getName();

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static long getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.getLongVersionCode();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 版本号比较
     * 如果两者相同 return 0 否则 如果 前者比后者大，返回1 ，后者比前者大 返回-1
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");

        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;
        // 循环判断每位的大小
        Logger.d(TAG, "verTag" + version1Array[index]);
        while (index < minLen
                && (diff = Long.parseLong(version1Array[index]) - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }


}
