package com.pigchen.awesomeseries.drag;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;

import com.pigchen.awesomeseries.R;
import com.pigchen.awesomeseries.clock.DeviceStatus;
import com.pigchen.awesomeseries.clock.MainClockView;

import java.util.Calendar;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/28
 * @ 描述:
 */

public class DragAct extends Activity {
    private com.pigchen.awesomeseries.clock.MainClockView clockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_drag);
        this.clockView = (MainClockView) findViewById(R.id.clockView);
        clockView.onStart();
        clockView.setConnectStatus(DeviceStatus.CONNECTING);
        clockView.postDelayed(new Runnable() {
            @Override
            public void run() {
                clockView.onStop();
                Calendar calendar = Calendar.getInstance();
                float milliSecond = calendar.get(Calendar.MILLISECOND);
                final float second = calendar.get(Calendar.SECOND) + milliSecond / 1000;
                final float minute = calendar.get(Calendar.MINUTE) + second / 60;
                final float hour = calendar.get(Calendar.HOUR) + minute / 60;
                PropertyValuesHolder hourHolder = PropertyValuesHolder.ofFloat("hour", 10.17f,hour<=10?hour+12:hour);
                PropertyValuesHolder minuteHolder  = PropertyValuesHolder.ofFloat("min", 10,minute<=10?minute+60:minute);
                PropertyValuesHolder secHolder  = PropertyValuesHolder.ofFloat("sec", 0,second+2);
                ValueAnimator clockAni = ValueAnimator
                        .ofPropertyValuesHolder(hourHolder, minuteHolder,secHolder)
                        .setDuration(2000);
                clockAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        clockView.setTimeDegree((float)valueAnimator.getAnimatedValue("hour"),
                                (float)valueAnimator.getAnimatedValue("min"),
                                (float)valueAnimator.getAnimatedValue("sec")
                        );
                    }
                });
                clockAni.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        clockView.setConnectStatus(DeviceStatus.CONNECTED);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                clockView.setConnectStatus(DeviceStatus.CONNECTINGED);
                clockAni.start();

            }
        },1000);

    }
}
