package com.android.http.callback;

/**
 * @author chicunxiang
 */
public interface FileDownLoadCallback<T> {

    /**
     * 准备工作
     * @param length total总长度
     */
    void onPrepare(long length);

    /**
     * 进度开始
     * @param progress 进度
     */
    void onProgress(int progress);

    /**
     * 下载完成
     */
    void onDownloadComplete();

    /**
     * 出错
     * @param msg
     */
    void onFailure(String msg);
}
