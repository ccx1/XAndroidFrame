package com.android.http.callback;

/**
 * @author chicunxiang
 */
public interface UploadResponseCallback<T>  extends ResponseCallback<T>{

    void onStartUpload();
}
