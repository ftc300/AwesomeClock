package com.pigchen.awesomeseries;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chendong on 2017/8/9.
 */
public class ContainerAct extends Activity implements onGestureListener {

    String TAG = "ContainerAct";
    BottomContainerLayout bottom;
    LinearLayout top,act;
    private  float TOP = convertDpToPixel(200);
    private float BOTTOM;
    private MainClockView clockView;
    private android.widget.TextView clockDigit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        this.clockDigit = (TextView) findViewById(R.id.clockDigit);
        this.clockView = (MainClockView) findViewById(R.id.clockView);
        top = (LinearLayout) findViewById(R.id.top);
        bottom = (BottomContainerLayout) findViewById(R.id.bottom);
        act = (LinearLayout) findViewById(R.id.act);
        bottom.setGestureListener(this);
        top.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        top.post(new Runnable() {
            @Override
            public void run() {
                BOTTOM = top.getMeasuredHeight();
            }
        });
    }

    private void setHeight(final LinearLayout layout, float currentHeight, float targetHeight){
        ValueAnimator va ;
        va =  ValueAnimator.ofInt((int)currentHeight, (int)targetHeight);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                layout.getLayoutParams().height = (Integer)animation.getAnimatedValue();
                layout.requestLayout();
            }
        });
        va.start();
    }

    private void setHeightLight(final LinearLayout layout, float height){
        if(height >= TOP && height <= BOTTOM) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
            params.height = (int)height;
            layout.setLayoutParams(params);
        }
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    @Override
    public void slideDownToBottom() {
        setHeight(top,top.getMeasuredHeight(),BOTTOM);
    }

    @Override
    public void slideDownBack() {
        setHeight(top,top.getMeasuredHeight(),TOP);
    }

    @Override
    public void wipeUpToTop() {
        setHeight(top,top.getMeasuredHeight(),TOP);
    }

    @Override
    public void wipeUpBack() {
        setHeight(top,top.getMeasuredHeight(),BOTTOM);
    }

    @Override
    public void normalHoldScroll(float deltaY) {
        setHeightLight(top,top.getMeasuredHeight()+deltaY/10);
    }
}
