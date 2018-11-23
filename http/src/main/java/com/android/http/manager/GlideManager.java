package com.android.http.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * @author ccx
 * @date 2018/11/15
 */
public class GlideManager {
    private GlideManager() {
    }

    /**
     * 展示图片--网络加载
     * 空白或者错误占位图片：picasso提供了两种占位图片，未加载完成或者加载发生错误的时需要一张图片作为提示。
     * 如果加载发生错误会重复三次请求，三次都失败才会显示erro Place holder
     */
    public static void showImage(Context context, ImageView imageView, String url) {
        if (context != null && imageView != null && !TextUtils.isEmpty(url)) {
            Glide.with(context).load(url).into(imageView);
        }
    }

    /**
     * 展示图片--网络加载
     * 空白或者错误占位图片：picasso提供了两种占位图片，未加载完成或者加载发生错误的时需要一张图片作为提示。
     * 如果加载发生错误会重复三次请求，三次都失败才会显示erro Place holder
     */
    public static void showImage(Context context, ImageView imageView, String url, int loadingDrawable) {
        if (context != null && imageView != null && !TextUtils.isEmpty(url)) {

            RequestOptions options = new RequestOptions()
                    .error(loadingDrawable)
                    .placeholder(loadingDrawable);

            Glide.with(context).load(url).apply(options).into(imageView);
        }
    }

    /**
     * 展示图片--资源文件id R.drawable.landing_screen
     */
    public static void showImage(Context context, ImageView imageView, int drawable) {
        if (context != null && imageView != null && drawable != -1) {
            Glide.with(context).load(drawable).into(imageView);
        }
    }

    /**
     * 展示图片--文件
     */
    public static void showImage(Context context, ImageView imageView, File file) {
        if (context != null && imageView != null && file != null && file.exists()) {
            Glide.with(context).load(file).into(imageView);
        }
    }

    /**
     * 展示图片: 转换图片以适应布局大小并减少内存占用
     */
    public static void showGifImage(Context context, ImageView imageView, String url, int width, int height) {
        if (context != null && imageView != null && !TextUtils.isEmpty(url)) {
            RequestOptions options = new RequestOptions()
                    .override(width, height).centerCrop();
            Glide.with(context).asGif().load(url).apply(options).into(imageView);
        }
    }

    /**
     * 展示图片: 自定义转换图片
     */
    public static void showImage(Context context, ImageView imageView, String url, RequestOptions options) {
        if (context != null && imageView != null && !TextUtils.isEmpty(url)) {
            Glide.with(context).load(url).apply(options).into(imageView);
        }
    }
}
