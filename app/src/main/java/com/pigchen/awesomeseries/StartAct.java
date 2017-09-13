package com.pigchen.awesomeseries;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pigchen.awesomeseries.bazier.BazierAct;
import com.pigchen.awesomeseries.cir.OilAct;
import com.pigchen.awesomeseries.drag.DragAct;
import com.pigchen.awesomeseries.page3.PageCurlActivity;
import com.pigchen.awesomeseries.rotate.RotateAct;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/24
 * @ 描述:
 */
public class StartAct extends AppCompatActivity {
    private android.support.v7.widget.RecyclerView recyclerview;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.act_main_happy);
        this.recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(new StartAdapter(context).setListener(new StartAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent i = null;
                switch (pos){
                    case 0:
                         i = new Intent(context, MainActivity.class);
                        break;
                    case 1:
                         i = new Intent(context, OilAct.class);
                        break;
                    case 2:
                         i = new Intent(context, RotateAct.class);
                        break;
                    case 3:
                         i = new Intent(context, DragAct.class);
                        break;
                    case 4:
                         i = new Intent(context, BazierAct.class);
                        break;
                    case 5:
                         i = new Intent(context, PageCurlActivity.class);
                        break;
                }
                startActivity(i);
            }
        }));
    }
}
