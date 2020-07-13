package com.android.install.manager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.android.common.common.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Use: 安装管理器,负责apk的安装之类
 * Author：cunxiangchi@gamil.com
 * Time: 2020/7/10
 */
public class InstallManager {

    private static final String TAG = InstallManager.class.getName();
    // 静默
    public static final int FLAG_SILENT = 1;
    // 显示
    public static final int FLAG_EXPLICIT = 2;
    // 没找到权限的错误码
    public static final int NOT_FOUNT_INSTALL_PERMISSION = 1001;

    public static final int INSTALL_PERMISSION_RESULT_CODE = 9007;

    // 卸载，5.0后不可用
    public static void uninstall(String pkgName)
            throws Exception {
        if (TextUtils.isEmpty(pkgName)) {
            throw new IllegalArgumentException("empty packge name");
        }
        Logger.d(TAG, "[" + pkgName + "] uninstall ...");
        String[] args = {"pm", "uninstall", "-k", pkgName};
        exeCmdArgs(args);
    }

    /**
     *
     * @param activity
     * @param apkPath
     * @param flags 是否为静默安装 1 代表静默，2代表显示 -1 unknow , -2 没有安装权限 8.0兼容
     * @param deleteApk
     * @return
     * @throws Exception
     */
    public static int install(Activity activity, String apkPath, int flags, boolean deleteApk) throws Exception {
        Context context = activity.getApplicationContext();
        boolean installed = false;
        int installFlag = -1;
        Exception exception = null;
        Logger.d(TAG, "[" + apkPath + "][" + flags + "][" + deleteApk + "] install ...");
        validateApkParameter(apkPath);
        if (FLAG_SILENT == flags) {
            try {
                // 静默安装
                installSilently(apkPath);
                installed = true;
                installFlag = FLAG_SILENT;
                if (deleteApk) {
                    Logger.i(TAG, "[" + apkPath + "][" + flags + "] install(s) done, delete apk ...");
                    safeDeleteApk(apkPath);
                }
            } catch (Exception e) {
                installed = false;
                exception = e;
                Logger.e(TAG, "[" + apkPath + "] install(s) failed: " + e);
            }
        }
        if ((!installed) && (FLAG_EXPLICIT == flags)) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                    if (!haveInstallPermission) {
                        Uri packageURI = Uri.parse("package:" + context.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                        activity.startActivityForResult(intent, INSTALL_PERMISSION_RESULT_CODE);
                        return NOT_FOUNT_INSTALL_PERMISSION;
                    }
                }
                // 常规安装
                installManually(context, apkPath);
                installed = true;
                installFlag = FLAG_EXPLICIT;
            } catch (Exception e) {
                exception = e;
                Logger.e(TAG, "[" + apkPath + "] install(m) failed: " + e);
            }
        }
        if (!installed) {
            throw (null != exception ? exception : new Exception("install failed"));
        }
        return installFlag;
    }

    private static void validateApkParameter(String apkPath)
            throws Exception {
        if (TextUtils.isEmpty(apkPath)) {
            throw new IllegalArgumentException("empty apk URL");
        }
        if (!apkPath.endsWith(".apk")) {
            throw new IllegalArgumentException("illegal apk URL");
        }
        if (!new File(apkPath).exists()) {
            throw new FileNotFoundException("apk not exist");
        }
    }

    private static void safeDeleteApk(String apkPath) {
        try {
            new File(apkPath).delete();
        } catch (Exception e) {
            Logger.w(TAG, "[" + apkPath + "] delete apk failed: " + e);
        }
    }

    private static void installManually(Context context, String apkPath)
            throws Exception {
        if (TextUtils.isEmpty(apkPath)) {
            throw new IllegalArgumentException("empty local url");
        }
        File file = new File(apkPath);
        if (!file.exists()) {
            throw new IllegalArgumentException("file not exist");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            installByProvider(context, file);
        } else {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.putExtra("name", "");
            Uri data = Uri.fromFile(file);
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    private static void installByProvider(Context context, File file)
            throws Exception {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.putExtra("name", "");

            String packageName = context.getPackageName();
            Uri data = FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
            intent.setDataAndType(data, "application/vnd.android.package-archive");

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable t) {
            throw new Exception("install failed", t);
        }
    }

    /**
     * 静默安装
     * @param apkPath 路径
     * @throws Exception
     */
    private static void installSilently(String apkPath)
            throws Exception {
        if (TextUtils.isEmpty(apkPath)) {
            throw new IllegalArgumentException("empty local url");
        }
        File file = new File(apkPath);
        if (!file.exists()) {
            throw new IllegalArgumentException("file not exist");
        }
        String[] args = {"pm", "install", "-r", apkPath};
        exeCmdArgs(args);
    }

    private static void exeCmdArgs(String[] args)
            throws Exception {
        ByteArrayOutputStream errorBuffer = new ByteArrayOutputStream();
        ByteArrayOutputStream resultBuffer = new ByteArrayOutputStream();
        ProcessBuilder processBuilder = null;
        Process process = null;
        InputStream errorInput = null;
        InputStream resultInput = null;
        int byteOfRead = 0;
        byte[] buffer = new byte[1024];
        try {
            processBuilder = new ProcessBuilder(args);
            process = processBuilder.start();

            errorInput = process.getErrorStream();
            while (-1 != (byteOfRead = errorInput.read(buffer))) {
                errorBuffer.write(buffer, 0, byteOfRead);
            }
            resultInput = process.getInputStream();
            while (-1 != (byteOfRead = resultInput.read(buffer))) {
                resultBuffer.write(buffer, 0, byteOfRead);
            }
            String error = errorBuffer.toString("UTF-8");
            String result = resultBuffer.toString("UTF-8");
            validateResult(error, result);
        } finally {
            close(errorInput, resultInput);
            destroy(process);
        }
    }

    private static void validateResult(String error, String result)
            throws Exception {
        if (error.contains("Failure")) {
            throw new Exception("e=" + error + ", r=" + result);
        }
        if (!result.contains("Success")) {
            throw new Exception("e=" + error + ", r=" + result);
        }
    }

    private static void close(InputStream is1, InputStream is2) {
        try {
            if (null != is1) {
                is1.close();
            }
        } catch (Throwable t) {
            Logger.w(TAG, "close input stream failed: " + t);
        }
        try {
            if (null != is2) {
                is2.close();
            }
        } catch (Throwable t) {
            Logger.w(TAG, "close input stream failed: " + t);
        }
    }

    private static void destroy(Process process) {
        try {
            if (null != process) {
                process.exitValue();
            }
        } catch (IllegalThreadStateException e) {
            try {
                process.destroy();
                process.waitFor();
            } catch (Throwable t) {
                Logger.w(TAG, "close process failed: " + t);
            }
        }
    }

    public static boolean isInstalled(Context context, String pkgName) {
        try {
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            return null != pkgInfo;
        } catch (Exception localException) {
        }
        return false;
    }

    /**
     * 启动apk
     * @param context this
     * @param pkgName package
     * @throws Exception
     */
    public static void open(Context context, String pkgName)
            throws Exception {
        PackageManager pkgMgr = context.getPackageManager();
        if (null == pkgMgr) {
            throw new Exception("can't get package manager");
        }
        Intent intent = pkgMgr.getLaunchIntentForPackage(pkgName);
        if (null == intent) {
            throw new Exception("can't find application launch intent");
        }
        intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static void activate(Context context, String pkgName, String action)
            throws Exception {
        Intent i0 = new Intent();
        i0.setAction(action);
        i0.setPackage(pkgName);
        i0.putExtra("from", context.getPackageName());
        ComponentName cn = context.startService(i0);
        if (null == cn) {
            Intent i1 = new Intent();
            i1.setAction(action);
            i1.putExtra("from", context.getPackageName());
            if (Build.VERSION.SDK_INT >= 12) {
                i1.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            }
            context.sendBroadcast(i1);
        }
    }

}
