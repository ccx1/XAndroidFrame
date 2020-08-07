package com.android.component.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.component.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class Banner extends FrameLayout implements View.OnTouchListener {


    private final Context mContext;
    private ViewPager mViewPager;
    private PagerAdapter mViewPagerAdapter;
    private Disposable mSubscribe;
    private BannerStatus status = BannerStatus.NONE;
    private Disposable mTimer;
    private GestureDetector mGestureDetector;


    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mViewPager = new HorizontalCanScrollViewPager(mContext);
        mViewPager.setId(R.id.viewpager_inner);
        mViewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        mViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mViewPager);
        mViewPager.setOnTouchListener(this);
        //手势处理
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnItemClickListener != null) {
                    if (mViewPagerAdapter instanceof LoopAdapter) {
                        mOnItemClickListener.onItemClick(mViewPager.getCurrentItem() % ((LoopAdapter) mViewPagerAdapter).getItemCount());
                    }
                }
                return super.onSingleTapUp(e);
            }
        });
    }


    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setAdapter(PagerAdapter adapter) {
        this.mViewPagerAdapter = adapter;
        mViewPager.setAdapter(adapter);
        start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    public void start() {
        mViewPager.setCurrentItem(this.mViewPagerAdapter.getCount() / 2);
        openTimer();
    }

    private void openTimer() {
        if (mViewPagerAdapter.getCount() < 2) {
            return;
        }
        endTimerCount();
        status = BannerStatus.START;
        mSubscribe = Observable.interval(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (status == BannerStatus.START && mViewPager != null) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                });
    }

    private void endTimerCount() {
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
            status = BannerStatus.STOP;
            mSubscribe = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        endTimerCount();
        endTouchTimer();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                endTouchTimer();
                status = BannerStatus.PAUSE;
                break;
            case MotionEvent.ACTION_MOVE:
                status = BannerStatus.PAUSE;
                endTouchTimer();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                startTouchTimer();
                break;
        }
        return false;
    }

    private void startTouchTimer() {
        mTimer = Observable.timer(2, TimeUnit.SECONDS).subscribe(aLong -> status = BannerStatus.START);
    }

    private void endTouchTimer() {
        if (mTimer != null && !mTimer.isDisposed()) {
            mTimer.dispose();
            mTimer = null;
        }
    }

    enum BannerStatus {
        NONE,
        START,
        PAUSE,
        STOP

    }
}
