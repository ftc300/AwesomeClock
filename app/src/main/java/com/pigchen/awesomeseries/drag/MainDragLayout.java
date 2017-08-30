package com.pigchen.awesomeseries.drag;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.pigchen.awesomeseries.bazier.BazierView;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/28
 * @ 描述:
 */
public class MainDragLayout extends LinearLayout {

    private String TAG = "TAG";
    private FrameLayout frameTop, frameBottom;
    private BazierView bazierView;
    public ViewDragHelper mViewDragHelper;
    private int bottomDeltaY;//底部超出屏幕的部分
    private int topHeight;
    private int bottomHeight;
    private int deltaY;

    public MainDragLayout(Context context) {
        this(context, null);
    }

    public MainDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainDragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallBack());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        topHeight = frameTop.getHeight();
        bottomHeight = frameBottom.getHeight();
        bottomDeltaY = topHeight + bottomHeight - getHeight();
        d("topHeight():"+topHeight+",frameBottom.getHeight():"+frameBottom.getHeight()+",getHeight():"+getHeight()+",bottomDeltaY :"+bottomDeltaY );
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        frameTop = (FrameLayout) getChildAt(0);
        bazierView = (BazierView) getChildAt(1);
        frameBottom = (FrameLayout) getChildAt(2);
    }

    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == frameBottom;
        }


        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            d("top:" + top + ",dy:" + dy + ",topHeight():" + topHeight + ",return:" + Math.max(Math.min(top, topHeight), bottomDeltaY));
            return Math.max(Math.min(top, topHeight),topHeight - bottomDeltaY);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalTop ;
            if(yvel <= 0){//上滑
                if(releasedChild.getTop() < topHeight -  bottomDeltaY / 3){
                    finalTop = topHeight - bottomDeltaY;
                }else{
                    finalTop = topHeight;//上滑回弹
                }
            }else{
                if(releasedChild.getTop() > topHeight - 2 * bottomDeltaY / 3 ){
                    finalTop = topHeight;
                }else{
                    finalTop = topHeight - bottomDeltaY;
                }
            }
//            以松手前的滑动速度为初值，让捕获到的子View自动滚动到指定位置
            mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalTop);
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            deltaY = dy;
//            setTopHeight();
            frameTop.offsetTopAndBottom(dy);
            d("deltaY:"+deltaY);
        }
    }


    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private synchronized  void  setTopHeight(){
        ViewGroup.LayoutParams  params = frameTop.getLayoutParams();
        params.height = topHeight + deltaY;
        frameTop.setLayoutParams(params);
    }
    private void d(String msg){
        Log.d(TAG,msg);
    }
}
