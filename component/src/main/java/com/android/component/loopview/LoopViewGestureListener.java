package com.android.component.loopview;

import android.view.MotionEvent;


final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final LoopView loopView;

    LoopViewGestureListener(LoopView loopview) {
        loopView = loopview;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        loopView.scrollBy(velocityY);
        return true;
    }
}
