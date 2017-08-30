package com.pigchen.awesomeseries.bazier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.pigchen.awesomeseries.R;

/**
 *
 */
public class BazierView extends View {
    private Paint paint;
    private PointF pointF;
    private float dy;
    int width,height;
    private volatile boolean isDrawShadow = true;
    private MyHandler myHandler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    }
    public BazierView(Context context) {
        super(context);
        init();

    }

    public BazierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BazierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        width = getScreenSize(getContext())[0];
        height =getScreenSize(getContext())[1];
        pointF = new PointF(width/2,height/2);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        paint.setTextSize(50);
    }

    private Path drawBezierShadow(float cy) {
        Path path = new Path();
        path.moveTo(0, height/2);
        path.quadTo(width/2, cy, width, height/2);
        return path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (pointF.y > getHeight() / 8) {
//            pointF.y = getHeight() / 8;
//        }
        canvas.drawPath(drawBezierShadow(pointF.y), paint);
    }

    /**
     * 同步方法，如果不用同步会出现闪烁
     */
    private synchronized void release() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isDrawShadow = false;
//                if (pointF.y > getHeight() / 10) {
//                    pointF.y = getHeight() / 10;
//                }
//                for (float i = pointF.y; i > 0; i--) {
//                    pointF.y = i;
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    myHandler.sendEmptyMessage(0);
//                }
                for (float i = pointF.y; i <= height/2; i++) {
                    pointF.y = i;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myHandler.sendEmptyMessage(0);
                }
                isDrawShadow = true;
            }
        }).start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDrawShadow) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    release();
                    break;
                case MotionEvent.ACTION_MOVE:
                    pointF.x = event.getX();
                    float pd = event.getY() - dy;
//                    if (pd > getHeight() / 10) {
//                        pd = getHeight() / 10;
//                    }
                    if(pd > 0) pd = height/2 ;
                    else if(pd < 0 ) pd = height/2 + pd;
                    pointF.y = pd;
                    break;
                case MotionEvent.ACTION_DOWN:
                    dy = event.getY();
                    break;
            }
        }
        invalidate();
//        if ((pointF.y - dy) < 0) {
//            return super.onTouchEvent(event);
//        } else {
//            return true;
//        }
        return true;
    }

    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

}
