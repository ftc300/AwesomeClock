package com.pigchen.awesomeseries;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by chendong on 2017/8/9.
 */

public class ContainerAct extends Activity {
    String TAG = "ContainerAct";
    FrameLayout top,bottom ;
    private float mDownY;
    private float mMoveY;
    private float mDelta;
    private int mTouchSlop =  50;
    private float mCurrentHeight;
    LinearLayout act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        top = (FrameLayout) findViewById(R.id.top);
        bottom = (FrameLayout) findViewById(R.id.bottom);
        Log.d(TAG,"Y值："+top.getY());
        act = (LinearLayout) findViewById(R.id.act);
        mCurrentHeight = top.getHeight();
        bottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownY = motionEvent.getRawY();
                        Log.d(TAG,"ACTION_DOWN:"+mDownY);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(mDelta > 200) {
                            setHeight(top,(int)convertDpToPixel(300));
                        }
                        if(mDelta < -200) {
                            setHeight(top,(int)convertDpToPixel(100));
                        }
                        if((mDelta <200&&mDelta>0)||(mDelta <0 && mDelta>-200))
                            Log.d(TAG,"mCurrentHeight:"+mCurrentHeight);
                        setHeight(top,(int)mCurrentHeight);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(mDownY - mMoveY) < 5) return false;
                        mMoveY = motionEvent.getRawY();
                        Log.d(TAG,"ACTION_MOVE:"+mMoveY);
                         mDelta = mMoveY - mDownY;
                        if (mDelta <= 0) Log.d(TAG, "向上滑动");
                        else Log.d(TAG,"向下滑动");
                        Log.d(TAG,"height:"+(int)(top.getHeight()+mDelta/10));
                        setHeight(top,(int)(top.getHeight()+mDelta/10));
                        break;

                }
                return true;
            }
        });
    }

    private void setHeight(final FrameLayout layout, int height){
        ValueAnimator va ;
//        if(height >= convertDpToPixel(100) )
        va =  ValueAnimator.ofInt(1000, 100);
        va.setDuration(1000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                layout.getLayoutParams().height = value.intValue();
                layout.requestLayout();
            }
        });
        va.start();
//        if(height >= convertDpToPixel(100) && height <= convertDpToPixel(300)) {
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
//            params.height = height;
//            layout.setLayoutParams(params);
//        }
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }


    private int convertPxToDp(int px){
        return Math.round(px/(Resources.getSystem().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }

}
