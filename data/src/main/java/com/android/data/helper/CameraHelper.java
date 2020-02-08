package com.android.data.helper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.android.data.model.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * @author ccx
 * @date 2018/11/16
 */
public class CameraHelper {


    public static final int SYSTEM_CAMERA_RESULT = 10101;
    public static final int SYSTEM_PHOTO_RESULT = 10102;

    private static CameraHelper sCameraHelper;

    public static CameraHelper getInstance() {
        if (sCameraHelper == null) {
            sCameraHelper = new CameraHelper();
        }
        return sCameraHelper;
    }

    public void startSystemCamera(Activity activity, File file) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 23) {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, SYSTEM_CAMERA_RESULT);
    }

    public void startSystemPhotoSelect(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, SYSTEM_PHOTO_RESULT);
    }

    public List<Photo> getPhotosData(Activity activity) {
        TreeSet<Photo> photos = new TreeSet<>(new Comparator<Photo>() {
            @Override
            public int compare(Photo o1, Photo o2) {
                return (int) (Long.valueOf(o2.createDate) - Long.valueOf(o1.createDate));
            }
        });
        // 获取内容提供者
        Cursor cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        // 如果有下一个
        while (cursor != null && cursor.moveToNext()) {
            //获取图片的路径，但是是byte数组的
            byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //获取图片的详细信息
            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            photos.add(new Photo(new String(data, 0, data.length - 1), desc));
        }
        // 关闭
        if (cursor != null) {
            cursor.close();
        }
        return new ArrayList<>(photos);
    }


    public void onActivityResult(Activity activity, File file, int requestCode, Intent data) {
        switch (requestCode) {
            case SYSTEM_CAMERA_RESULT:
                if (!file.exists()) {
                    // 防止直接返回没有数据回来的问题
                    return;
                }
                // 将照片发送到系统相册
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                if (mOnResultCallback != null) {
                    mOnResultCallback.onCameraResult(file);
                }

                break;
            case SYSTEM_PHOTO_RESULT:
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    if (uri != null && uri.getPath() != null) {
                        File photo = new File(uri.getPath().replace("/raw/", ""));
                        if (mOnResultCallback != null) {
                            mOnResultCallback.onPhotoResult(file);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    private OnResultCallback mOnResultCallback;

    public void setOnResultCallback(OnResultCallback onResultCallback) {
        mOnResultCallback = onResultCallback;
    }

    public interface OnResultCallback {
        void onCameraResult(File file);

        void onPhotoResult(File file);
    }

}
