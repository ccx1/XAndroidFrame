package com.android.install.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * Use: 下载的地址
 * Author：cunxiangchi@gamil.com
 * Time: 2020/7/11
 */
public interface DownloadService {

    @Streaming
    @GET("{path}")
    Observable<ResponseBody> downloadFile(@Path(value = "path",encoded = true) String downloadPath);
}
