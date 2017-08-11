package com.pigchen.awesomeseries;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by chendong on 2017/8/11.
 */

public class MainTextView extends TextView {
    public MainTextView(Context context) {
        super(context);
    }

    public MainTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
