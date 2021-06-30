package com.android.base.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.base.R;


public abstract class BaseDialog {
    protected Context mContext;
    protected Dialog mDialog;

    /* access modifiers changed from: protected */
    public abstract int getDialogStyleId();

    /* access modifiers changed from: protected */
    public abstract View getView();

    public BaseDialog(Context context) {
        this.mContext = context;
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (getDialogStyleId() == 0) {
            this.mDialog = new Dialog(context, R.style.dialog_custom);
        } else {
            this.mDialog = new Dialog(context, getDialogStyleId());
        }
        View view = getView();
        if (!needZoomWidth()) {
            view.setLayoutParams(new FrameLayout.LayoutParams((int) (((double) display.getWidth()) * 0.8d), -2));
        } else {
            view.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), -2));
        }
        this.mDialog.setContentView(view);
        Window window = this.mDialog.getWindow();
        if (window != null)
            window.setSoftInputMode(3);
        if (needZoomWidth()) {
            WindowManager.LayoutParams attributes = this.mDialog.getWindow().getAttributes();
            attributes.width = display.getWidth();
        }
    }

    protected boolean needZoomWidth() {
        return false;
    }

    public void setCancelable(boolean cancel) {
        this.mDialog.setCancelable(cancel);
    }

    public void show() {
        this.mDialog.show();
    }

    public void dismiss() {
        this.mDialog.dismiss();
    }

    public boolean isShowing() {
        return this.mDialog.isShowing();
    }

    public BaseDialog setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.mDialog.setOnDismissListener(dismissListener);
        return this;
    }
}