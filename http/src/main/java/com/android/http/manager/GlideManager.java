package com.android.http.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.security.MessageDigest;


/**
 * @author ccx
 * @date 2018/11/15
 */
public class GlideManager {
    private GlideManager() {
    }

    /**
     * 展示图片--网络加载
     * 空白或者错误占位图片：glide提供了两种占位图片，未加载完成或者加载发生错误的时需要一张图片作为提示。
     * 如果加载发生错误会重复三次请求，三次都失败才会显示erro Place holder
     */
    public static void showImage(Context context, ImageView imageView, String url) {
        if (context != null && imageView != null && !TextUtils.isEmpty(url)) {
            Glide.with(context).load(url).into(imageView);
        }
    }

    /**
     * 展示图片--网络加载
     * 空白或者错误占位图片：glide提供了两种占位图片，未加载完成或者加载发生错误的时需要一张图片作为提示。
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
     * 展示图片: 转换图片以适应布局大小并减少内存占用
     */
    public static void showImage(Context context, ImageView imageView, String url, int width, int height) {
        if (context != null && imageView != null && !TextUtils.isEmpty(url)) {
            RequestOptions options = new RequestOptions()
                    .override(width, height).centerCrop();
            Glide.with(context).asGif().load(url).apply(options).into(imageView);
        }
    }

    /**
     * 展示图片: 圆形图片
     */
    public static void showCropImage(Context context, ImageView imageView, String url) {
        if (context != null && imageView != null && !TextUtils.isEmpty(url)) {
            RequestOptions options = RequestOptions.circleCropTransform();
            Glide.with(context).load(url).apply(options).into(imageView);
        }
    }

    /**
     * 展示图片--圆形图片 -> 资源文件id R.drawable.landing_screen
     */
    public static void showCropImage(Context context, ImageView imageView, int drawable) {
        if (context != null && imageView != null && drawable != -1) {
            RequestOptions options = RequestOptions.circleCropTransform();
            Glide.with(context).load(drawable).apply(options).into(imageView);
        }
    }


    /**
     * 展示图片--圆形带边图片 -> 资源文件id R.drawable.landing_screen
     */
    public static void showCropBorderImage(Context context, ImageView imageView, int drawable) {
        if (context != null && imageView != null && drawable != -1) {
            Glide.with(context)
                    .load(drawable)
                    .circleCrop()
                    .transform(new GlideCircleTransformWithBorder(context, 1, Color.WHITE))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    /**
     * 展示图片--圆形带边图片 -> string url
     */
    public static void showCropBorderImage(Context context, ImageView imageView, String url) {
        if (context != null && imageView != null && !TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .circleCrop()
                    .transform(new GlideCircleTransformWithBorder(context, 1, Color.WHITE))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
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


    public static class GlideCircleTransformWithBorder extends BitmapTransformation {
        private Paint mBorderPaint;
        private float mBorderWidth;

        public GlideCircleTransformWithBorder(Context context) {

        }

        public GlideCircleTransformWithBorder(Context context, int borderWidth, int borderColor) {

            mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;

            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }


        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            if (mBorderPaint != null) {
                float borderRadius = r - mBorderWidth / 2;
                canvas.drawCircle(r, r, borderRadius, mBorderPaint);
            }
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }

    }
}
