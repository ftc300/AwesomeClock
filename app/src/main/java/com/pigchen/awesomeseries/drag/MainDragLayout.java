package com.pigchen.awesomeseries.drag;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.pigchen.awesomeseries.R;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/28
 * @ 描述:
 * @see android.view.View
 * @version 1
 * @author chendong
 * @since 2017
 */
public class MainDragLayout extends LinearLayout {

    private String TAG = "TAG";
    private FrameLayout frameTop, frameBottom ;
    private LinearLayout topChild0;
    private View clock,city;
//    private BazierView bazierView;
    public ViewDragHelper mViewDragHelper;
    private int bottomDeltaY;//底部超出屏幕的部分
    private int topHeight;
    private int bottomHeight;
    private int deltaY;
    private int mTop;
    private Paint paint;
    private int status = -1;

    public MainDragLayout(Context context) {
        this(context, null);
    }

    public MainDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainDragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        setOrientation(VERTICAL);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallBack());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        paint.setTextSize(50);
        status = 1;
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
        topChild0 = (LinearLayout) frameTop.getChildAt(0);
        clock = topChild0.getChildAt(0);
        city = topChild0.getChildAt(1);
//        bazierView = (BazierView) getChildAt(1);
        frameBottom = (FrameLayout) getChildAt(1);
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
            if(yvel <= 0 ){//上滑
                finalTop = topHeight - bottomDeltaY;
//                if(releasedChild.getTop() < topHeight -  bottomDeltaY / 3){
//                    finalTop = topHeight - bottomDeltaY;
//                }else{
//                    finalTop = topHeight;//上滑回弹
                if(status == 1) {
                    startAnim(true);
                    status = 0;
                }
//                }
            }else {
                finalTop = topHeight;
                if(status == 0) {
                    startAnim(false);
                    status = 1;
                }
//                if(releasedChild.getTop() > topHeight - 2 * bottomDeltaY / 3 ){
//                    finalTop = topHeight;
//                }else{
//                    finalTop = topHeight - bottomDeltaY;
//                }
            }
//            以松手前的滑动速度为初值，让捕获到的子View自动滚动到指定位置
            mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalTop);
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            deltaY = dy;
            mTop = top;
            frameTop.offsetTopAndBottom(dy);
//            city.offsetTopAndBottom((int)(1.2*dy));
        }
    }

    private Path drawBezierShadow() {
        Path path = new Path();
        path.moveTo(0,mTop);
        path.quadTo(getWidth()/2, deltaY, getWidth(), mTop);
        return path;
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawPath(drawBezierShadow(), paint);
//    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    /**
     * 向上
     * @param b
     */
    private void startAnim(boolean b){
        AnimationSet set = new AnimationSet(true);
        Animation scaleAnim,alphaAnim;
        if(b) {
            alphaAnim = new AlphaAnimation(1,0);
            scaleAnim = new ScaleAnimation(1f, 0f, 1f, 0f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, .5f);
        }else{
            scaleAnim = new ScaleAnimation(0f, 1f, 0f, 1f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, .5f);
            alphaAnim = new AlphaAnimation(0,1);
        }
        set.addAnimation(alphaAnim);
        set.addAnimation(scaleAnim);
        set.setFillAfter(true);
        set.setDuration(2000);
        clock.startAnimation(set);
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
        ViewGroup.LayoutParams  params = clock.getLayoutParams();
        params.height = topHeight -Math.abs(deltaY) ;
        clock.setLayoutParams(params);
    }

    private void d(String msg){
        Log.d(TAG,msg);
    }
}
