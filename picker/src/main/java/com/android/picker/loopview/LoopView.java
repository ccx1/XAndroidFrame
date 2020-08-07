package com.android.picker.loopview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.android.picker.R;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class LoopView extends View {

    private float scaleX = 1.05F;

    private static final int DEFAULT_TEXT_SIZE = (int) (Resources.getSystem().getDisplayMetrics().density * 15);

    private static final float DEFAULT_LINE_SPACE = 1f;

    private static final int DEFAULT_VISIBIE_ITEMS = 9;

    public static final int SCROLL_STATE_IDLE = 0;     // 停止滚动
    public static final int SCROLL_STATE_SETTING = 1;  // 用户设置
    public static final int SCROLL_STATE_DRAGGING = 2; // 用户按住滚轮拖拽
    public static final int SCROLL_STATE_SCROLLING = 3; // 依靠惯性滚动

    int lastScrollState = SCROLL_STATE_IDLE;
    int currentScrollState = SCROLL_STATE_SETTING;

    public enum ACTION {
        CLICK, FLING, DRAG
    }

    private Context context;

    Handler handler;
    private GestureDetector flingGestureDetector;
    OnItemSelectedListener onItemSelectedListener;
    OnItemScrollListener mOnItemScrollListener;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint paintOuterText;
    private Paint paintCenterText;
    private Paint paintIndicator;

    protected List<? extends LoopBean> items;

    protected int textSize;
    protected int itemTextHeight;

    //文本的高度
    protected int textHeight;

    protected int outerTextColor;

    protected int centerTextColor;
    protected int dividerColor;

    float lineSpacingMultiplier;
    boolean isLoop;

    protected int firstLineY;
    protected int secondLineY;

    protected int totalScrollY;
    protected int initPosition;
    protected int preCurrentIndex;
    int change;

    protected int itemsVisibleCount;

    protected SparseArray<LoopBean> drawingStrings;
//    HashMap<String,Integer> drawingStr

    protected int measuredHeight;
    protected int measuredWidth;

    protected int halfCircumference;
    protected int radius;

    private int mOffset = 0;
    private float previousY;
    long startTime = 0;

    private Rect tempRect = new Rect();

    private int paddingLeft, paddingRight;

    private Typeface typeface = Typeface.MONOSPACE;

    /**
     * set text line space, must more than 1 间距
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier > 1.0f) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
        }
    }

    /**
     * set outer text color 外圈的字体颜色
     *
     * @param centerTextColor
     */
    public void setCenterTextColor(int centerTextColor) {
        this.centerTextColor = centerTextColor;
        if (paintCenterText != null) {
            paintCenterText.setColor(centerTextColor);
        }

    }

    /**
     * set center text color 中间字体颜色
     *
     * @param outerTextColor
     */
    public void setOuterTextColor(int outerTextColor) {
        this.outerTextColor = outerTextColor;
        if (paintOuterText != null) {
            paintOuterText.setColor(outerTextColor);
        }
    }

    /**
     * set divider color 线条颜色
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        if (paintIndicator != null) {
            paintIndicator.setColor(dividerColor);
        }
    }

    /**
     * set text typeface 字体
     *
     * @param typeface
     */
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public LoopView(Context context) {
        super(context);
        initLoopView(context, null);
    }

    public LoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context, attributeset);
    }

    public LoopView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initLoopView(context, attributeset);
    }

    @SuppressLint("CustomViewStyleable")
    private void initLoopView(Context context, AttributeSet attributeset) {
        this.context = context;
        handler = new MessageHandler(this);
        flingGestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        flingGestureDetector.setIsLongpressEnabled(false);

        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable.loop_view);
        textSize = typedArray.getInteger(R.styleable.loop_view_ccx_textSize, DEFAULT_TEXT_SIZE);
        textSize = (int) (Resources.getSystem().getDisplayMetrics().density * textSize);
        lineSpacingMultiplier = typedArray.getFloat(R.styleable.loop_view_ccx_lineSpace, DEFAULT_LINE_SPACE);
        centerTextColor = typedArray.getInteger(R.styleable.loop_view_ccx_centerTextColor, 0xff313131);
        outerTextColor = typedArray.getInteger(R.styleable.loop_view_ccx_outerTextColor, 0xffafafaf);
        dividerColor = typedArray.getInteger(R.styleable.loop_view_ccx_dividerTextColor, 0xffc5c5c5);
        itemsVisibleCount =
                typedArray.getInteger(R.styleable.loop_view_ccx_itemsVisibleCount, DEFAULT_VISIBIE_ITEMS);
        if (itemsVisibleCount % 2 == 0) {
            itemsVisibleCount = DEFAULT_VISIBIE_ITEMS;
        }
        isLoop = typedArray.getBoolean(R.styleable.loop_view_ccx_isLoop, true);
        typedArray.recycle();

        drawingStrings = new SparseArray<>();
        totalScrollY = 0;
        initPosition = -1;
    }


    /**
     * visible item count, must be odd number
     *
     * @param visibleNumber
     */
    public void setItemsVisibleCount(int visibleNumber) {
        if (visibleNumber % 2 == 0) {
            return;
        }
        if (visibleNumber != itemsVisibleCount) {
            itemsVisibleCount = visibleNumber;
            drawingStrings = new SparseArray<>();
        }
    }

    private void initPaintsIfPossible() {
        if (paintOuterText == null) {
            paintOuterText = new Paint();
            paintOuterText.setColor(outerTextColor);
            paintOuterText.setAntiAlias(true);
            paintOuterText.setTypeface(typeface);
            paintOuterText.setTextSize(textSize);
        }


        if (paintCenterText == null) {
            paintCenterText = new Paint();
            paintCenterText.setColor(centerTextColor);
            paintCenterText.setAntiAlias(true);
            paintCenterText.setTextScaleX(scaleX);
            paintCenterText.setTypeface(typeface);
            paintCenterText.setTextSize(textSize);
        }

        if (paintIndicator == null) {
            paintIndicator = new Paint();
            paintIndicator.setColor(dividerColor);
            paintIndicator.setAntiAlias(true);
        }
    }

    private void remeasure() {
        if (items == null || items.isEmpty()) {
            return;
        }

        measuredWidth = getMeasuredWidth();

        measuredHeight = getMeasuredHeight();

        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }

        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();

        measuredWidth = measuredWidth - paddingRight;

        paintCenterText.getTextBounds("\u661F\u671F", 0, 2, tempRect); // 星期
        textHeight = tempRect.height();
        halfCircumference = (int) (measuredHeight * Math.PI / 2);

        itemTextHeight = (int) (halfCircumference / (lineSpacingMultiplier * (itemsVisibleCount - 1)));

        radius = measuredHeight / 2;
        firstLineY = (int) ((measuredHeight - lineSpacingMultiplier * itemTextHeight) / 2.0F);
        secondLineY = (int) ((measuredHeight + lineSpacingMultiplier * itemTextHeight) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }

        preCurrentIndex = initPosition;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DRAG) {
            float itemHeight = lineSpacingMultiplier * itemTextHeight;
            mOffset = (int) ((totalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture =
                mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
        changeScrollState(SCROLL_STATE_SCROLLING);
    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        // change this number, can change fling speed
        int velocityFling = 10;
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, velocityFling,
                TimeUnit.MILLISECONDS);
        changeScrollState(SCROLL_STATE_DRAGGING);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
            changeScrollState(SCROLL_STATE_IDLE);
        }
    }

    /**
     * 打印方法调用堆栈链信息 用于调试
     *
     * @param methodName
     */
    @SuppressLint("DefaultLocale")
    private void printMethodStackTrace(String methodName) {
        StackTraceElement[] invokers = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder("printMethodStackTrace ");
        sb.append(methodName);
        sb.append(" ");
        for (int i = invokers.length - 1; i >= 4; i--) {
            StackTraceElement invoker = invokers[i];
            sb.append(String.format("%s(%d).%s", invoker.getFileName(), invoker.getLineNumber(), invoker.getMethodName()));
            if (i > 4) {
                sb.append("-->");
            }
        }
        Log.i("printMethodStackTrace", sb.toString());
    }

    private void changeScrollState(int scrollState) {
        if (scrollState != currentScrollState && !handler.hasMessages(MessageHandler.WHAT_SMOOTH_SCROLL_INERTIA)) {
            lastScrollState = currentScrollState;
            currentScrollState = scrollState;
        }
    }

    /**
     * set not loop
     */
    public void setNotLoop() {
        isLoop = false;
    }

    /**
     * set text size in dp
     *
     * @param size
     */
    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
            if (paintOuterText != null) {
                paintOuterText.setTextSize(textSize);
            }
            if (paintCenterText != null) {
                paintCenterText.setTextSize(textSize);
            }

        }
    }

    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.initPosition = 0;
        } else {
            if (items != null && items.size() > initPosition) {
                this.initPosition = initPosition;
            }
        }
    }

    public final void setListener(OnItemSelectedListener OnItemSelectedListener) {
        onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setOnItemScrollListener(OnItemScrollListener mOnItemScrollListener) {
        this.mOnItemScrollListener = mOnItemScrollListener;
    }


    public void setItems(List<? extends LoopBean> items) {

        this.items = items;
        remeasure();
        invalidate();
    }

//    public void setItems(List<String> items) {
//
//        this.items = convertData(items);
//        remeasure();
//        invalidate();
//    }
//
//    public List<LoopBean> convertData(List<String> items){
//        List<LoopBean> data=new ArrayList<>();
//        for (int i = 0; i < items.size(); i++) {
//            data.add(new LoopBean(items.get(i)));
//        }
//        return data;
//    }


    public final int getSelectedItem() {
        return preCurrentIndex;
    }

    public final LoopBean getSelectedItemBean() {
        return this.items.get(preCurrentIndex);
    }


    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
        }
    }

    /**
     * @param scaleX
     */
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    /**
     * set current item position
     *
     * @param position
     */
    public void setCurrentPosition(int position) {
        if (items == null || items.isEmpty()) {
            return;
        }
        int size = items.size();
        if (position >= 0 && position < size && position != getSelectedItem()) {
            initPosition = position;
            totalScrollY = 0;
            mOffset = 0;
            changeScrollState(SCROLL_STATE_SETTING);
            invalidate();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (items == null || items.isEmpty()) {
            return;
        }

        change = (int) (totalScrollY / (lineSpacingMultiplier * itemTextHeight));
        preCurrentIndex = initPosition + change % items.size();

        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }

        int j2 = (int) (totalScrollY % (lineSpacingMultiplier * itemTextHeight));
        // put value to drawingString
        int k1 = 0;
        while (k1 < itemsVisibleCount) {
            int l1 = preCurrentIndex - (itemsVisibleCount / 2 - k1);
            if (isLoop) {
                while (l1 < 0) {
                    l1 = l1 + items.size();
                }
                while (l1 > items.size() - 1) {
                    l1 = l1 - items.size();
                }
                drawingStrings.put(k1, items.get(l1));
            } else if (l1 < 0) {
//                drawingStrings[k1] = "";
                drawingStrings.put(k1, new LoopBean());
            } else if (l1 > items.size() - 1) {
//                drawingStrings[k1] = "";
                drawingStrings.put(k1, new LoopBean());
            } else {
                // drawingStrings[k1] = items.get(l1);
                drawingStrings.put(k1, items.get(l1));
            }
            k1++;
        }
        canvas.drawLine(paddingLeft, firstLineY, measuredWidth, firstLineY, paintIndicator);
        canvas.drawLine(paddingLeft, secondLineY, measuredWidth, secondLineY, paintIndicator);

        int i = 0;
        while (i < itemsVisibleCount) {
            canvas.save();
            float itemHeight = itemTextHeight * lineSpacingMultiplier;
            double radian = ((itemHeight * i - j2) * Math.PI) / halfCircumference;
            if (radian >= Math.PI || radian <= 0) {
                canvas.restore();
            } else {
                int translateY = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) * itemTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= firstLineY && itemTextHeight + translateY >= firstLineY) {
                    // first divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, firstLineY - translateY);
                    drawOuterText(canvas, i);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, firstLineY - translateY, measuredWidth, (int) (itemHeight));
                    drawCenterText(canvas, i);
                    canvas.restore();
                } else if (translateY <= secondLineY && itemTextHeight + translateY >= secondLineY) {
                    // second divider
                    canvas.save();
                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateY);
                    drawCenterText(canvas, i);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, secondLineY - translateY, measuredWidth, (int) (itemHeight));
                    drawOuterText(canvas, i);
                    canvas.restore();
                } else if (translateY >= firstLineY && itemTextHeight + translateY <= secondLineY) {
                    // center item
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    drawCenterText(canvas, i);
                } else {
                    // other item
                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    drawOuterText(canvas, i);
                }
                canvas.restore();
            }
            i++;
        }

        if (currentScrollState != lastScrollState) {
            int oldScrollState = lastScrollState;
            lastScrollState = currentScrollState;
            if (mOnItemScrollListener != null) {
                mOnItemScrollListener.onItemScrollStateChanged(this, getSelectedItem(), oldScrollState, currentScrollState, totalScrollY);
            }

        }


        if (currentScrollState == SCROLL_STATE_IDLE) {
            if (mOnItemSelectChange != null) {
                mOnItemSelectChange.onItemSelect(getSelectedItem(), getSelectedItemBean());
            }

        }
        if (currentScrollState == SCROLL_STATE_DRAGGING || currentScrollState == SCROLL_STATE_SCROLLING) {
            if (mOnItemScrollListener != null) {
                mOnItemScrollListener.onItemScrolling(this, getSelectedItem(), currentScrollState, totalScrollY);
            }
        }
    }


    private void drawOuterText(Canvas canvas, int position) {
        LoopBean bean = drawingStrings.get(position);
        String loopMessage = bean == null ? "" : bean.getLoopMessage();
        canvas.drawText(loopMessage, getTextX(loopMessage, paintOuterText, tempRect),
                getDrawingY(), paintOuterText);
    }

    private void drawCenterText(Canvas canvas, int position) {
        LoopBean bean = drawingStrings.get(position);
        String loopMessage = bean == null ? "" : bean.getLoopMessage();
        canvas.drawText(loopMessage, getTextX(loopMessage, paintOuterText, tempRect),
                getDrawingY(), paintCenterText);
    }


    private int getDrawingY() {
        if (itemTextHeight > textHeight) {
            return itemTextHeight - ((itemTextHeight - textHeight) / 2);
        } else {
            return itemTextHeight;
        }
    }


    // text start drawing position
    private int getTextX(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        int textWidth = rect.width();
        textWidth *= scaleX;
        return (measuredWidth - paddingLeft - textWidth) / 2 + paddingLeft;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initPaintsIfPossible();
        remeasure();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = flingGestureDetector.onTouchEvent(event);
        float itemHeight = lineSpacingMultiplier * itemTextHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousY = event.getRawY();
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = previousY - event.getRawY();
                previousY = event.getRawY();

                totalScrollY = (int) (totalScrollY + dy);

                if (!isLoop) {
                    float top = -initPosition * itemHeight;
                    float bottom = (items.size() - 1 - initPosition) * itemHeight;

                    if (totalScrollY < top) {
                        totalScrollY = (int) top;
                    } else if (totalScrollY > bottom) {
                        totalScrollY = (int) bottom;
                    }
                }
                changeScrollState(SCROLL_STATE_DRAGGING);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((radius - y) / radius) * radius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - itemsVisibleCount / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {
                        smoothScroll(ACTION.DRAG);
                    } else {
                        smoothScroll(ACTION.CLICK);
                    }
                }
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        invalidate();
        return true;
    }

    private OnItemSelectChange mOnItemSelectChange;

    public void setOnItemSelectChange(OnItemSelectChange onItemSelectChange) {
        mOnItemSelectChange = onItemSelectChange;
    }

    public interface OnItemSelectChange {
        void onItemSelect(int position, LoopBean item);
    }

}
