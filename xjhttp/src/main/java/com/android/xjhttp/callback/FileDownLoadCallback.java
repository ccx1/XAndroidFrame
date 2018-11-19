package com.android.xjhttp.callback;

public interface FileDownLoadCallback<T> {

    void onPrepare(long length);

    void onProgress(int progress);

    void onDownloadComplete();

    void onFailure(String msg);
}
