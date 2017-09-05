package com.pigchen.awesomeseries.bazier;

import android.app.Activity;
import android.os.Bundle;

import com.pigchen.awesomeseries.R;
import com.pigchen.awesomeseries.currenttime.CurrentTv;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/30
 * @ 描述:
 */


public class BazierAct extends Activity {

    private com.pigchen.awesomeseries.currenttime.CurrentTv currentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bazier);
        this.currentTv = (CurrentTv) findViewById(R.id.currentTv);
        currentTv.onStart();
    }
}
