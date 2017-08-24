package com.pigchen.awesomeseries.rotate;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.pigchen.awesomeseries.R;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/24
 * @ 描述:
 */
public class CircleRotateLayout extends ImageView {
    private int mRadius;//直径
    private static final int FLINGABLE_VALUE = 300;//快速移动阈值
    private static final int NOCLICK_VALUE = 3; //屏蔽点击阈值
    private int mFlingableValue = FLINGABLE_VALUE;//移动角度
    private double mStartAngle = 0;
    private float mTmpAngle;//按下到抬起时旋转的角度
    private float mLastX;
    private float mLastY;
    private Context mContext;
    private GestureDetectorCompat gestureDetectorCompat;

    public CircleRotateLayout(Context context) {
        this(context, null);
    }

    public CircleRotateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRotateLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setImageDrawable(getResources().getDrawable(R.drawable.adjust_step_dial));
        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                float start = getAngle(motionEvent.getX(), motionEvent1.getY());
                float end = getAngle(motionEvent1.getX(), motionEvent1.getY());
                Log.e("TAG", "第几象限：" + getQuadrant(x, y) + ",start = " + start + " , end =" + end + "," + "mTmpAngle:" + mTmpAngle);
                // 如果是一、四象限，则直接end-start，角度值都是正值
                if (getQuadrant(motionEvent.getX(), motionEvent.getY()) == 1 || getQuadrant(motionEvent.getX(), motionEvent.getY()) == 4) {
                    if (end - start > 0)
                        mTmpAngle += end - start;
                } else { // 二、三象限，色角度值是付值
                    if (start - end > 0)
                        mTmpAngle += start - end;
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                float start = getAngle(motionEvent.getX(), motionEvent1.getY());
                float end = getAngle(motionEvent1.getX(), motionEvent1.getY());
                Log.e("TAG", "第几象限：" + getQuadrant(x, y) + ",start = " + start + " , end =" + end + "," + "mTmpAngle:" + mTmpAngle);
                // 如果是一、四象限，则直接end-start，角度值都是正值
                if (getQuadrant(motionEvent.getX(), motionEvent.getY()) == 1 || getQuadrant(motionEvent.getX(), motionEvent.getY()) == 4) {
                    if (end - start > 0)
                        mTmpAngle += end - start;
                } else { // 二、三象限，色角度值是付值
                    if (start - end > 0)
                        mTmpAngle += start - end;
                }
                return false;
            }
        });

    }

    private int measureDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 800;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTmpAngle > 0)
            setRotation(mTmpAngle);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }


    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//
//        // Log.e("TAG", "x = " + x + " , y = " + y);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastX = x;
//                mLastY = y;
////                mDownTime = System.currentTimeMillis();
//                mTmpAngle = 0;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float start = getAngle(mLastX, mLastY);
//                float end = getAngle(x, y);
//                 Log.e("TAG", "第几象限："+getQuadrant(x, y)+",start = " + start + " , end =" + end+","+"mTmpAngle:"+mTmpAngle);
//                // 如果是一、四象限，则直接end-start，角度值都是正值
//                if(getQuadrant(x, y) == getQuadrant(mLastX,mLastY)) {
//                    if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
//                        if (end - start > 0)
//                            mTmpAngle += end - start;
//                    } else { // 二、三象限，色角度值是付值
//                        if (start - end > 0)
//                            mTmpAngle += start - end;
//                    }
//                    mLastX = x;
//                    mLastY = y;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                // 如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
//                if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
//                    return true;
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(event);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return true;
//    }

    /**
     * 根据触摸的位置，计算角度
     *
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }

}
