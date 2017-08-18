package com.pigchen.awesomeseries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.pigchen.awesomeseries.clock.DeviceStatus;
import com.pigchen.awesomeseries.clock.MainClockView;
import com.pigchen.awesomeseries.example.NormalRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {
    private android.widget.TextView clockDigit;
    private RecyclerView recyclerview;
    private MainClockView clockView;
    private CustomItem ci;
    private android.widget.LinearLayout activitymain;

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

    }
}
