package com.android.data.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ccx
 * @date 2018/11/16
 */
public class FileHelper {

    private static FileHelper sFileHelper;
    private static Context    sContext;

    private FileHelper() {
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static FileHelper getInstance() {
        if (sFileHelper == null) {
            synchronized (FileHelper.class) {
                if (sFileHelper == null) {
                    sFileHelper = new FileHelper();
                }
            }
        }
        return sFileHelper;
    }


    public boolean fileExists(String path) {
        return fileExists(new File(path));
    }

    public boolean fileExists(File file) {
        if (file != null) {
            return file.exists();
        }
        return false;
    }


    public boolean copy(InputStream is, File file) {
        if (!fileExists(file)) {
            return false;
        }
        if (ContextCompat.checkSelfPermission(sContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        BufferedInputStream  bufferedInputStream  = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(is);
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            int    len;
            byte[] buff = new byte[1024];
            while ((len = bufferedInputStream.read(buff)) != -1) {
                bufferedOutputStream.write(buff, 0, len);
                bufferedOutputStream.flush();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            safeClose(bufferedInputStream, bufferedOutputStream, is);
        }
        return false;
    }


    public void delete(File file) {
        if (!fileExists(file)) {
            return;
        }
        file.delete();
    }

    public void safeClose(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    if (closeable == null) {
                        continue;
                    }
                    closeable.close();
                    closeable = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
