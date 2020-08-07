package com.android.http.manager;

import com.android.http.callback.FileDownLoadCallback;
import com.android.http.callback.ResponseCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author chicunxiang
 */
public class HttpServiceManager {

    private static HttpServiceManager sHttpServiceManager;

    private HttpServiceManager() {
    }

    public static HttpServiceManager getInstance() {
        if (sHttpServiceManager == null) {
            sHttpServiceManager = new HttpServiceManager();
        }
        return sHttpServiceManager;
    }

    public <T> void enqueue(Observable<T> observable, final ResponseCallback<T> callBack) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T value) {
                        callBack.onResponse(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailure(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public void downloadFile(final String filePath, Observable<ResponseBody> observable, final FileDownLoadCallback<ResponseBody> callBack) {
        observable.subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        try {

                            File file = new File(filePath);
                            if (file.exists()) {
                                file.delete();
                            }
                            InputStream         is    = body.byteStream();
                            FileOutputStream    fos   = new FileOutputStream(file);
                            BufferedInputStream bis   = new BufferedInputStream(is);
                            long                total = body.contentLength();
                            callBack.onPrepare(total);
                            byte[] buffer  = new byte[8 * 1024];
                            int    len;
                            int    process = 0;
                            callBack.onProgress(process);
                            while ((len = bis.read(buffer)) != -1) {
                                process += len;
                                callBack.onProgress((int) (((float) process / total) * 100));
                                fos.write(buffer, 0, len);
                                fos.flush();
                            }
                            fos.close();
                            bis.close();
                            callBack.onDownloadComplete();
                        } catch (IOException e) {
                            e.printStackTrace();
                            callBack.onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
