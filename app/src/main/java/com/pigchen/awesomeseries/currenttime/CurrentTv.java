package com.pigchen.awesomeseries.currenttime;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/9/5
 * @ 描述:
 */
public class CurrentTv extends TextView {
    public CurrentTv(Context context) {
        this(context, null);
    }
    public CurrentTv(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm", Locale.getDefault());

    public CurrentTv(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             setText(getCurrentText());
        }
    };

    public void onStart(){
        handler.removeCallbacks(renderRunnable);
        handler.post(renderRunnable);
    }

    public void onStop(){
        handler.removeCallbacks(renderRunnable);
    }


    Runnable renderRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(renderRunnable,1000);
        }
    };

    private String getCurrentText() {
        Date curDate = new Date();
        return sdf.format(curDate);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setText(getCurrentText());
    }

    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            invalidate();
        }
    }
}
