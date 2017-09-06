package com.pigchen.awesomeseries;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/28
 * @ 描述:
 */
public class MainDragLayout2 extends LinearLayout {

    private FrameLayout frameTop, frameBottom;
    private LinearLayout topChild0;
    private View clock;
    public ViewDragHelper mViewDragHelper;
    private int bottomDeltaY;//底部超出屏幕的部分
    private int topHeight;
    private int bottomHeight;
    private int deltaY;
    private int status = -1;
    private GestureDetectorCompat gestureDetector;


    public MainDragLayout2(Context context) {
        this(context, null);
    }

    public MainDragLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainDragLayout2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        setOrientation(VERTICAL);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallBack());
        gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                // 垂直滑动时dy>dx，才被认定是上下拖动
                return Math.abs(dy) > Math.abs(dx);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        topHeight = frameTop.getHeight();
        bottomHeight = frameBottom.getHeight();
        bottomDeltaY = topHeight + bottomHeight - getHeight();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        frameTop = (FrameLayout) getChildAt(0);
//        topChild0 = (LinearLayout) frameTop.getChildAt(0);
//        clock = topChild0.getChildAt(0);
        frameBottom = (FrameLayout) getChildAt(1);
    }

    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == frameBottom;
        }


        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
//            return Math.max(Math.min(top, topHeight),topHeight - bottomDeltaY);
            return Math.max(Math.min(top, topHeight), topHeight - bottomDeltaY);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalTop;
            if (yvel <= 0) {//上滑
                finalTop = topHeight - bottomDeltaY;
                if (status == 1) {
//                    startAnim(true);
                    status = 0;
                }
            } else {
                finalTop = topHeight;
                if (status == 0) {
//                    startAnim(false);
                    status = 1;
                }
            }
            mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalTop);
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            deltaY = dy;
            frameTop.offsetTopAndBottom(dy);
        }
    }


    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 向上
     *
     * @param b
     */
    private void startAnim(boolean b) {
        AnimationSet set = new AnimationSet(true);
        Animation scaleAnim, alphaAnim;
        if (b) {
            alphaAnim = new AlphaAnimation(1, 0);
            scaleAnim = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, .5f);
        } else {
            scaleAnim = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, .5f);
            alphaAnim = new AlphaAnimation(0, 1);
        }
        set.addAnimation(alphaAnim);
        set.addAnimation(scaleAnim);
        set.setFillAfter(true);
        set.setDuration(2000);
        clock.startAnimation(set);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

//        if (frameView1.getBottom() > 0 && frameView1.getTop() < 0) {
//            // view粘到顶部或底部，正在动画中的时候，不处理touch事件
//            return false;
//        }

        boolean yScroll = gestureDetector.onTouchEvent(ev);
        boolean shouldIntercept = false;
        shouldIntercept = mViewDragHelper.shouldInterceptTouchEvent(ev);
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            // action_down时就让mDragHelper开始工作，否则有时候导致异常 他大爷的
            mViewDragHelper.processTouchEvent(ev);
        }
        return shouldIntercept && yScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


}
