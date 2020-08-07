package com.android.component;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.android.base.view.BaseDialog;

public class FileSelectPicker extends BaseDialog {

    private ViewHolder mViewHolder;

    public FileSelectPicker(Context context) {
        super(context);
    }

    @Override
    public int getDialogStyleId() {
        return R.style.dialog_custom;
    }

    @Override
    public View getView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // 父类高度无视
        View view = inflater.inflate(R.layout.dialog_file_picker, null);
        mViewHolder = new ViewHolder(view);

        WindowManager.LayoutParams attributes = mDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
        setCancelable(false);
        return view;
    }

    @Override
    protected boolean needZoomWidth() {
        return true;
    }

    static class ViewHolder {


        ViewHolder(View itemView) {

        }

    }



}
