package com.pigchen.awesomeseries.example;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by chendong on 2017/8/9.
 */
public class BottomContainerLayout extends LinearLayout {
    private final float DELTA_THRESHOLD = convertDpToPixel(100);
    private final int HIGHEST_POSITION = 0;
    private final int LOWEST_POSITION = 1;
    private final int DOWN_GEST = 0;
    private final int UP_GEST = 1;
    private int mCurrPosition;
    private float mDownY;
    private float mUpY;
    private float mMoveY;
    private float mDeltaY;
    private float mPreDeltaY;
    private int mCurrGest;
    private onGestureListener listener;

    public BottomContainerLayout(Context context) {
        this(context, null);
    }

    public BottomContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCurrPosition = LOWEST_POSITION;//默认最低点 上滑到最高点or回弹
        mCurrGest = -1;
    }

    public void setGestureListener(onGestureListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownY = event.getRawY();
                    break;

                case MotionEvent.ACTION_UP:
                    mUpY = event.getRawY();
                    mDeltaY = mUpY - mDownY;
                    mCurrGest = mDeltaY > 0 ? DOWN_GEST : UP_GEST;//当前手势：向下or向上
                    if (mCurrPosition == HIGHEST_POSITION && mCurrGest == DOWN_GEST && listener != null) {
                        if (Math.abs(mDeltaY) > DELTA_THRESHOLD) {
                            listener.slideDownToBottom();
                            mCurrPosition = LOWEST_POSITION;
                        } else if (Math.abs(mDeltaY) < DELTA_THRESHOLD) {
                            listener.slideDownBack();
                            mCurrPosition = HIGHEST_POSITION;
                        }
                    } else if (mCurrPosition == LOWEST_POSITION && mCurrGest == UP_GEST && listener != null) {
                        if (Math.abs(mDeltaY) > DELTA_THRESHOLD) {
                            listener.wipeUpToTop();
                            mCurrPosition = HIGHEST_POSITION;
                        } else if (Math.abs(mDeltaY) < DELTA_THRESHOLD) {
                            listener.wipeUpBack();
                            mCurrPosition = LOWEST_POSITION;
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    mMoveY = event.getRawY();
                    mDeltaY = mMoveY - mDownY;
//                    if (listener != null && Math.abs(mDeltaY - mPreDeltaY) > convertDpToPixel(10)) {
                    if (listener != null ) {
                        mPreDeltaY = mDeltaY;
                        listener.normalHoldScroll(mDeltaY);
                    }
                    break;
            }
            return true;
        }
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }


}