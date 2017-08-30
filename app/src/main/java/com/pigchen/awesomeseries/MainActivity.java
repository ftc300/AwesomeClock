package com.pigchen.awesomeseries;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.pigchen.awesomeseries.clock.DeviceStatus;
import com.pigchen.awesomeseries.clock.MainClockView;
import com.pigchen.awesomeseries.example.ListAdapter;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private android.widget.TextView clockDigit;
    private ListView recyclerview;
    private MainClockView clockView;
    private CustomItem ci;
    private android.widget.LinearLayout activitymain;
    private CircleProgressView cir;

    private int per = 100;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            per--;
            if(per<0)
                per = 100;
            else {
                cir.setProgress(per);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.act_main);
        this.clockView = (MainClockView) findViewById(R.id.clockView);
//        clockView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                clockView.setConnectStatus(DeviceStatus.TIMEOUT);
//                clockView.onStop();
//            }
//        },2000);
//        clockView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                clockView.setConnectStatus(DeviceStatus.CONNECTED);
//                clockView.onGradientStart();
//            }
//        },3000);
           clockView.onStart();
//        clockView.setConnectStatus(DeviceStatus.CONNECTING);
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
        this.recyclerview = (ListView) findViewById(R.id.recycler_view);
        this.clockDigit = (TextView) findViewById(R.id.clockDigit);
        recyclerview.setAdapter(new ListAdapter(this));
//        recyclerview.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
//        recyclerview.setAdapter(new NormalRecyclerViewAdapter(this));
//        setContentView(R.layout.activity_main);
//        this.activitymain = (LinearLayout) findViewById(R.id.activity_main);
//        this.ci = (CustomItem) findViewById(R.id.ci);
//        ci.setCustomItem(ItemStatus.UNABLE);
//        ci.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ci.setCustomItem(ItemStatus.ENABLE);
//                ci.setCustomDrawable(R.drawable.list_ic_alarm_normal);
//            }
//        },2000);

//        setContentView(R.layout.pro_act);
//        this.cir = (CircleProgressView) findViewById(R.id.cir);
//        cir.setProgress(100);
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.sendMessage(Message.obtain());
//            }
//        },0,1000);


    }
}
