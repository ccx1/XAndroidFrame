package com.android.picker;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.base.view.BaseDialog;
import com.android.picker.loopview.LoopBean;
import com.android.picker.loopview.LoopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 带小时，分的时间选择器
 */
public class TimePickerDialog  extends BaseDialog {

    private ViewHolder mViewHolder;

    private List<LoopBean> monthList;
    private List<LoopBean> dayList;
    private List<LoopBean> hoursList;
    private List<LoopBean> minList;

    public TimePickerDialog(Context context) {
        super(context);
    }

    @Override
    public int getDialogStyleId() {
        return R.style.not_background_dialog;
    }

    @Override
    public View getView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_time_picker,null);
        mViewHolder = new ViewHolder(view);
        initDate();
        initEvent();

        WindowManager.LayoutParams attributes = mDialog.getWindow().getAttributes();
        attributes.gravity = Gravity.BOTTOM;

        return view;
    }

    private void initEvent() {
        mViewHolder.cancelButton.setOnClickListener(v -> dismiss());
        mViewHolder.confirmButton.setOnClickListener(v -> {
            if (mOnConfirmClickListener != null) {
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = mViewHolder.monthLoop.getSelectedItem() + 1;
                int day = mViewHolder.dayLoop.getSelectedItem() + 1;
                int hours = mViewHolder.hoursLoop.getSelectedItem();
                int min = mViewHolder.minLoop.getSelectedItem();
                mOnConfirmClickListener.onConfirm(format2LenStr(year, ""), format2LenStr(month, ""),
                        format2LenStr(day, ""), format2LenStr(hours, ""), format2LenStr(min, ""));
            }
        });
    }

    private void initDate() {
        monthList = new ArrayList<>();
        dayList = new ArrayList<>();
        hoursList = new ArrayList<>();
        minList = new ArrayList<>();

        //月
        for (int i = 1; i <= 12; i++) {
            monthList.add(new LoopBean(format2LenStr(i, "月")));
        }
        reDrawDayList(31);

        //小时
        for (int i = 0; i <= 23; i++) {
            hoursList.add(new LoopBean(format2LenStr(i, "时")));
        }
        //分
        for (int i = 0; i <= 59; i++) {
            minList.add(new LoopBean(format2LenStr(i, "分")));
        }
        initItemData();

        mViewHolder.monthLoop.setOnItemSelectChange((position, item) -> {
            Calendar now = Calendar.getInstance();
            int monthOfDay = getMonthOfDay(now.get(Calendar.YEAR), position + 1);
            dayList.clear();
            reDrawDayList(monthOfDay);
            mViewHolder.dayLoop.setItems(dayList);
        });
    }

    @Override
    protected boolean needZoomWidth() {
        return true;
    }

    private void reDrawDayList(int i3) {
        //日
        for (int i = 1; i <= i3; i++) {
            dayList.add(new LoopBean(format2LenStr(i, "日")));
        }
    }

    private void initItemData() {
        mViewHolder.monthLoop.setItems(monthList);
        mViewHolder.dayLoop.setItems(dayList);
        mViewHolder.hoursLoop.setItems(hoursList);
        mViewHolder.minLoop.setItems(minList);
        mViewHolder.monthLoop.setTextSize(18);
        mViewHolder.dayLoop.setTextSize(18);
        mViewHolder.hoursLoop.setTextSize(18);
        mViewHolder.minLoop.setTextSize(18);
    }

    private String format2LenStr(int num, String other) {
        return ((num < 10) ? "0" + num : String.valueOf(num)) + other;
    }


    static class ViewHolder {

        LoopView monthLoop;
        LoopView dayLoop;
        LoopView hoursLoop;
        LoopView minLoop;
        TextView cancelButton;
        TextView confirmButton;

        ViewHolder(View itemView) {
            monthLoop = itemView.findViewById(R.id.month_loop);
            dayLoop = itemView.findViewById(R.id.day_loop);
            hoursLoop = itemView.findViewById(R.id.hours_loop);
            minLoop = itemView.findViewById(R.id.min_loop);
            cancelButton = itemView.findViewById(R.id.cancel_button);
            confirmButton = itemView.findViewById(R.id.confirm_button);
        }

    }


    /**
     * 根据年月获取日
     */
    private int getMonthOfDay(int year, int month) {
        int day = 0;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            day = 29;
        } else {
            day = 28;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;

        }

        return 0;
    }

    private OnConfirmClickListener mOnConfirmClickListener;


    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        mOnConfirmClickListener = onConfirmClickListener;
    }

    public interface OnConfirmClickListener {
        void onConfirm(String year, String month, String day, String hours, String minute);
    }
}
