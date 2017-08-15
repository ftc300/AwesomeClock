package com.pigchen.awesomeseries;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private android.widget.TextView clockDigit;
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.act_main);
        this.recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        this.clockDigit = (TextView) findViewById(R.id.clockDigit);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        recyclerview.setAdapter(new NormalRecyclerViewAdapter(this));
    }
}
