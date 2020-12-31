package com.android.install.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.common.helper.PermissionsHelper;
import com.android.http.callback.FileDownLoadCallback;
import com.android.install.R;
import com.android.install.manager.InstallManager;
import com.android.install.net.DownloadHttpHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Use: 更新用的dialog
 * Author：cunxiangchi@gamil.com
 * Time: 2020/7/11
 */
public class UpdateApkDialog extends Dialog {
    private Context mContext;
    private ViewHolder mViewHolder;
    private String path;
    private String savePath;
    private final PermissionsHelper mPermissionsHelper;
    private boolean isDownload = false;
    private String baseURL;

    public UpdateApkDialog(@NonNull Context context) {
        super(context, R.style.UpDateDialog);
        this.mContext = context;
        mPermissionsHelper = new PermissionsHelper((FragmentActivity) mContext);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_file, null);
        setContentView(view);
        mViewHolder = new ViewHolder(view);
        mViewHolder.mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDownload) {
                    try {
                        installApk();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    safeDownload();
                }
            }
        });
    }

    @SuppressLint("CheckResult")
    private void safeDownload() {
        mPermissionsHelper.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(savePath)) {
                                Toast.makeText(mContext.getApplicationContext(), "下载出现错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            startDownloadApk();
                        }
                    }
                });
    }

    /**
     * 传除了baseUrl的后半段
     *
     * @param path
     */
    public void setDownloadApkPathUrl(String path) {
        this.path = path;
    }

    /**
     * 传除了baseUrl的后半段
     *
     * @param baseURL
     */
    public void setDownloadApkPathBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }


    /**
     * 传apk下载的地方path
     *
     * @param path
     */
    public void setDownloadApkSavePath(String path) {
        this.savePath = path;
    }

    /**
     * 版本号
     *
     * @param code 版本号
     */
    public void setCode(String code) {
        mViewHolder.mCode.setText(code);
    }


    /**
     * 描述信息
     *
     * @param detail 描述信息
     */
    public void setDetail(String detail) {
        mViewHolder.mDetail.setText(detail);
    }


    static class ViewHolder {


        private TextView mCode;
        private TextView mDetail;
        private TextView mTitle;
        private TextView mDownloadBtn;
        private ProgressBar mProgress;

        ViewHolder(View view) {
            mCode = view.findViewById(R.id.code);
            mTitle = view.findViewById(R.id.title);
            mDetail = view.findViewById(R.id.detail);
            mDownloadBtn = view.findViewById(R.id.download_btn);
            mProgress = view.findViewById(R.id.progressBar);
        }
    }


    /**
     * 弹出dialog，显示
     */
    public void popDialog() {
        setCancelable(false);
        show();
        Window window = getWindow();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.gravity = Gravity.CENTER;
        window.setAttributes(p);
    }

    @SuppressLint("SetTextI18n")
    private void startDownloadApk() {
        DownloadHttpHelper.downloadFile(baseURL, path, savePath, new FileDownLoadCallback<ResponseBody>() {
            @Override
            public void onPrepare(long length) {
                mViewHolder.mProgress.setMax((int) length);
                mViewHolder.mDownloadBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewHolder.mDownloadBtn.setText("0%");
                        mViewHolder.mDownloadBtn.setTextColor(Color.BLACK);
                        mViewHolder.mDownloadBtn.setBackgroundResource(0);
                        mViewHolder.mProgress.setVisibility(View.VISIBLE);
                        mViewHolder.mDownloadBtn.setEnabled(false);
                        isDownload = true;
                    }
                });
            }

            @Override
            public void onProgress(final int progress) {
                mViewHolder.mProgress.setProgress(progress);
                int max = mViewHolder.mProgress.getMax();
                //完成率
                final int complete = (progress * 100 / max);
                mViewHolder.mDownloadBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewHolder.mDownloadBtn.setText(complete + "%");
                    }
                });
            }

            @Override
            public void onDownloadComplete() {
                mViewHolder.mDownloadBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewHolder.mTitle.setText(R.string.tooltip_complete);
                        mViewHolder.mDownloadBtn.setText(R.string.install_now);
                        mViewHolder.mDownloadBtn.setEnabled(true);
                        mViewHolder.mDownloadBtn.setBackgroundResource(R.drawable.shape_update_dialog_text_btn);
                        mViewHolder.mProgress.setVisibility(View.GONE);
                        mViewHolder.mDownloadBtn.setTextColor(Color.WHITE);
                        isDownload = true;
                        try {
                            installApk();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onFailure(final String msg) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
                        mViewHolder.mDownloadBtn.setText(R.string.download_now);
                        mViewHolder.mDownloadBtn.setTextColor(Color.WHITE);
                        mViewHolder.mDownloadBtn.setBackgroundResource(R.drawable.shape_update_dialog_text_btn);
                        mViewHolder.mProgress.setVisibility(View.GONE);
                        mViewHolder.mDownloadBtn.setEnabled(true);
                        isDownload = false;
                    }
                });
            }
        });
    }

    private void installApk() throws Exception {
        InstallManager.install((Activity) mContext, savePath, InstallManager.FLAG_EXPLICIT, false);
    }


}
