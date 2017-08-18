package com.pigchen.awesomeseries;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.pigchen.awesomeseries.clock.DeviceStatus;
import com.pigchen.awesomeseries.clock.MainClockView;
import com.pigchen.awesomeseries.example.NormalRecyclerViewAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private android.widget.TextView clockDigit;
    private RecyclerView recyclerview;
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
        clockView.postDelayed(new Runnable() {
            @Override
            public void run() {
                clockView.setConnectStatus(DeviceStatus.CONNECTED);
            }
        },1000);
        clockView.onStart();
        this.recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        this.clockDigit = (TextView) findViewById(R.id.clockDigit);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        recyclerview.setAdapter(new NormalRecyclerViewAdapter(this));
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

        setContentView(R.layout.pro_act);
        this.cir = (CircleProgressView) findViewById(R.id.cir);
        cir.setProgress(100);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendMessage(Message.obtain());
            }
        },0,1000);


    }
}
