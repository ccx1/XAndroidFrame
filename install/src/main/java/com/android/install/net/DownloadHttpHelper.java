package com.android.install.net;

import com.android.http.callback.FileDownLoadCallback;
import com.android.http.manager.HttpServiceManager;
import com.android.http.manager.RetrofitManager;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Use: downloadHttpHelper
 * Author：cunxiangchi@gamil.com
 * Time: 2020/7/11
 */
public class DownloadHttpHelper {

//
//    static {
//        RetrofitManager.getInstance().setBaseUrl("http://api.ccx1.top")
//                .setOkHttpClient(OkHttpManager.getInstance().build());
//    }


    public static void downloadFile(String path, String apkSavePath, FileDownLoadCallback<ResponseBody> callback){
        Observable<ResponseBody> observable = RetrofitManager.getInstance().getRetrofit().create(DownloadService.class).downloadFile(path);
        HttpServiceManager.getInstance().downloadFile(apkSavePath,observable,callback);
    }
}
