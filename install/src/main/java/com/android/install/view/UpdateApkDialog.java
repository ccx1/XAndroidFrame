package com.android.install.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
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
import androidx.appcompat.widget.AppCompatImageView;

import com.android.http.callback.FileDownLoadCallback;
import com.android.install.R;
import com.android.install.manager.InstallManager;
import com.android.install.net.DownloadHttpHelper;
import com.android.install.utils.DensityUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private boolean isDownload = false;
    private String baseURL;
    private final SharedPreferences mAppPackageInfo;
    private String code = "";
    private final String VERSION_KEY = "app_package_info_version";
    private final String SHOWING_KEY = "app_package_info_no_show";
    private boolean isForce;


    public UpdateApkDialog(@NonNull Context context) {
        super(context, R.style.UpDateDialog);
        this.mContext = context;
        mAppPackageInfo = mContext.getSharedPreferences("app_package_info", Context.MODE_PRIVATE);
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

        mViewHolder.nextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 记录一下版本号
                mAppPackageInfo.edit().putString(VERSION_KEY, code).apply();
                mAppPackageInfo.edit().putBoolean(SHOWING_KEY, true).apply();
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        // 获取版本号
        String code = mAppPackageInfo.getString(VERSION_KEY, "");
        boolean isShowing = mAppPackageInfo.getBoolean(SHOWING_KEY, false);
        // 如果匹配上, 并且是显示过了. 那就不展示了, 并且还得非强制.
        if (this.code.equals(code) && isShowing && !isForce) {
            return;
        }
        mAppPackageInfo.edit().putBoolean(SHOWING_KEY, false).apply();
        super.show();
    }

    @SuppressLint("CheckResult")
    private void safeDownload() {
//        mPermissionsHelper.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//
//                        }
//                    }
//                });
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(savePath)) {
            Toast.makeText(mContext.getApplicationContext(), "下载出现错误", Toast.LENGTH_SHORT).show();
            return;
        }
        startDownloadApk();
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
        String format = String.format(mContext.getResources().getString(R.string.download_version), code);
        this.code = code;
        mViewHolder.mCode.setText(format);
    }


    /**
     * 描述信息
     *
     * @param detail 描述信息
     */
    public void setDetail(String detail) {
        mViewHolder.mDetail.setText(detail);
    }

    /**
     * 设置下次一定的显示隐藏
     */
    public void setNextTimeVisibility(int visibility) {
        mViewHolder.nextTime.setVisibility(visibility);
    }

    public void setForceUpdate(boolean isForce) {
        this.isForce = isForce;
    }


    static class ViewHolder {


        private TextView mCode;
        private TextView mDetail;
        private TextView mTitle;
        private TextView mDownloadBtn;
        private TextView nextTime;
        private ProgressBar mProgress;
        private AppCompatImageView mProgressView;

        ViewHolder(View view) {
            mCode = view.findViewById(R.id.code);
            mTitle = view.findViewById(R.id.title);
            mDetail = view.findViewById(R.id.detail);
            mDownloadBtn = view.findViewById(R.id.download_btn);
            mProgress = view.findViewById(R.id.progressBar);
            mProgressView = view.findViewById(R.id.progress_view);
            nextTime = view.findViewById(R.id.next_time);
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
        final int dp460 = DensityUtils.dip2px(mContext, 460);
        DownloadHttpHelper.downloadFile(baseURL, path, savePath, new FileDownLoadCallback<ResponseBody>() {
            @Override
            public void onPrepare(long length) {
                mViewHolder.mProgress.setMax((int) length);

                mViewHolder.mDownloadBtn.post(new Runnable() {
                    @Override
                    public void run() {
//                        mViewHolder.mDownloadBtn.setText("0%");
//                        mViewHolder.mDownloadBtn.setTextColor(Color.BLACK);
//                        mViewHolder.mDownloadBtn.setBackgroundResource(0);
                        mViewHolder.mProgress.setVisibility(View.GONE);
                        mViewHolder.mDownloadBtn.setEnabled(false);
                        mViewHolder.mProgressView.setVisibility(View.VISIBLE);
                        mViewHolder.nextTime.setEnabled(false);
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
                        mViewHolder.mDownloadBtn.setText(progress + "%");
                        // 1% * 1325 / 100 + -1325
                        // 1% * 13.25 - 1325
                        float v = progress * (dp460 / 100f) - dp460;
                        System.out.println(v);
//                        ObjectAnimator animator = ObjectAnimator.ofFloat( mViewHolder.mProgressView, "x",dp460, 0);
//                        animator.setDuration(5000);
//                        animator.start();
                        mViewHolder.mProgressView.setPadding((int) v, 0, 0, 0);
                    }
                });
            }

            @Override
            public void onDownloadComplete() {
                mViewHolder.mDownloadBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewHolder.mDownloadBtn.setText(R.string.install_now);
                        mViewHolder.mDownloadBtn.setEnabled(true);
                        mViewHolder.mDownloadBtn.setBackgroundResource(R.drawable.shape_update_dialog_text_btn);
                        mViewHolder.mDownloadBtn.setTextColor(Color.WHITE);
//                        mViewHolder.mProgress.setVisibility(View.GONE);
                        isDownload = true;
                        try {
                            boolean haveInstallPermission ;
                            // 如果没有安装权限. 并且版本大于, 则去获取安装权限.
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                haveInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                                if(!haveInstallPermission){
                                    Toast.makeText(mContext,"请先同意使用安装权限", Toast.LENGTH_SHORT).show();
                                    Uri packageURI = Uri.parse("package:"+mContext.getPackageName());
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
                                    mContext.startActivity(intent);
                                }else {
                                    installApk();
                                }
                            }else {
                                installApk();
                            }
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
//                        mViewHolder.mProgress.setVisibility(View.GONE);
                        mViewHolder.mDownloadBtn.setEnabled(true);
                        mViewHolder.nextTime.setEnabled(true);
                        mViewHolder.mProgressView.setVisibility(View.VISIBLE);
                        mViewHolder.mProgressView.setPadding(-dp460, 0, 0, 0);
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
