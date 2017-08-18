package com.pigchen.awesomeseries.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.pigchen.awesomeseries.R;

import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by chendong on 2017/3/13.
 */
public class ClockView extends View implements SensorEventListener {
    /* 画布 */
    private Canvas mCanvas;
    /* 圆圈线条宽度 */
    private float mCircleStrokeWidth = 4;
    /* 时钟半径，不包括padding值 */
    private float mRadius;
    /* 刻度线长度 */
    private float mScaleLength;
    /* 刻度线padding*/
    private float mScalePadding;

    /* 加一个默认的padding值，为了防止用camera旋转时钟时造成四周超出view大小 */
    private float mDefaultPadding;
    private float mPaddingLeft;
    private float mPaddingTop;
    private float mPaddingRight;
    private float mPaddingBottom;

    /* 刻度线画笔 */
    private Paint mScaleLinePaint;
    /* 时针画笔 */
    private Paint mHourHandPaint;
    /* 分针画笔 */
    private Paint mMinuteHandPaint;
    /* 秒针画笔 */
    private Paint mSecondHandPaint;
    /* 时针路径 */
    private Path mHourHandPath;
    /* 分针路径 */
    private Path mMinuteHandPath;
    /* 秒针路径 */
    private Path mSecondHandPath;

    /* 时针角度 */
    private float mHourDegree;
    /* 分针角度 */
    private float mMinuteDegree;
    /* 秒针角度 */
    private float mSecondDegree;
    /* 圆圈的外接矩形 */
    private RectF mCircleRectF;

    private SensorManager sManager;
    private Sensor mSensorOrientation;
    private int tempTBValue;
    private int tempLRValue;
    private String mZoneID;//时区

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourHandPaint.setStyle(Paint.Style.FILL);
        mHourHandPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mMinuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinuteHandPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mSecondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondHandPaint.setStyle(Paint.Style.STROKE);
        mSecondHandPaint.setColor(getResources().getColor(R.color.colorPrimary));

        mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);
        mScaleLinePaint.setColor(getResources().getColor(R.color.colorPrimary));
        mScaleLinePaint.setStrokeWidth((float) 2);

        mCircleRectF = new RectF();
        mHourHandPath = new Path();
        mMinuteHandPath = new Path();
        mSecondHandPath = new Path();

        sManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mSensorOrientation = sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sManager.registerListener(this, mSensorOrientation, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureDimension(widthMeasureSpec), measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 800;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //宽和高分别去掉padding值，取min的一半即表盘的半径
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom()) / 2;
        mDefaultPadding = 0.12f * mRadius;//根据比例确定默认padding大小
        mPaddingLeft = mDefaultPadding + w / 2 - mRadius + getPaddingLeft();
        mPaddingTop = mDefaultPadding + h / 2 - mRadius + getPaddingTop();
        mPaddingRight = mPaddingLeft;
        mPaddingBottom = mPaddingTop;
        mScaleLength = 0.12f * mRadius;//根据比例确定刻度线长度
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        getTimeDegree();
        drawScaleLines();
        drawHourHand();
        drawMinuteHand();
        drawSecondHand();
        invalidate();
    }

    /**
     *  根据不断变化的秒针角度旋转画布
     */
    private void drawSecondHand() {
        mCanvas.save();
        mCanvas.rotate(mSecondDegree, getWidth() / 2, getHeight() / 2);
        mSecondHandPath.reset();
        mCircleRectF.set(mPaddingLeft,mPaddingTop,getWidth()-mPaddingRight,getHeight()-mPaddingBottom);
        mSecondHandPath.arcTo(mCircleRectF,-87,354);
        mSecondHandPaint.setStyle(Paint.Style.STROKE);
        mSecondHandPaint.setStrokeWidth(mCircleStrokeWidth);
        mCanvas.drawPath(mSecondHandPath, mSecondHandPaint);
        mCircleRectF.set(getWidth() / 2 - mCircleStrokeWidth,mPaddingTop-mCircleStrokeWidth,getWidth() / 2  + mCircleStrokeWidth ,mPaddingTop + mCircleStrokeWidth);
        mSecondHandPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mSecondHandPaint);
        mCanvas.restore();
    }

    /**
     * 90度一个刻度线
     */
    private void drawScaleLines()
    {
        mScalePadding = 0.03f * mRadius;
        mScaleLength = 0.13f * mRadius;
        mCanvas.drawLine(mPaddingLeft+mCircleStrokeWidth + mScalePadding,getHeight() / 2 , mPaddingLeft + mCircleStrokeWidth + mScalePadding + mScaleLength, getHeight() / 2, mScaleLinePaint);
        mCanvas.drawLine(getWidth() / 2,mPaddingTop + mCircleStrokeWidth + mScalePadding ,getWidth() / 2, mPaddingTop + mCircleStrokeWidth + mScalePadding + mScaleLength, mScaleLinePaint);
        mCanvas.drawLine(getWidth() - mCircleStrokeWidth - mScalePadding -mScaleLength - mPaddingRight , getHeight() / 2 ,getWidth() - mCircleStrokeWidth - mScalePadding - mPaddingRight, getHeight() / 2, mScaleLinePaint);
        mCanvas.drawLine(getWidth() / 2,getHeight()- mCircleStrokeWidth - mScalePadding -mScaleLength -mPaddingBottom ,getWidth() / 2, getHeight() - mCircleStrokeWidth - mScalePadding - mPaddingBottom, mScaleLinePaint);
    }


    /**
     * 画时针，根据不断变化的时针角度旋转画布
     * 针头为圆弧状，使用二阶贝塞尔曲线
     */
    private void drawHourHand() {
        mCanvas.save();
        mCanvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        mHourHandPath.reset();
        float offset = mPaddingTop ;
        mHourHandPath.moveTo(getWidth() / 2 - 0.008f * mRadius, getHeight() / 2 - 0.01f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 - 0.007f * mRadius, offset + 0.4f * mRadius);
        mHourHandPath.quadTo(getWidth() / 2, offset + 0.38f * mRadius, getWidth() / 2 + 0.007f * mRadius, offset + 0.4f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 + 0.008f * mRadius, getHeight() / 2 - 0.02f * mRadius);
        mHourHandPath.close();
        mHourHandPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mHourHandPath, mHourHandPaint);
        mCircleRectF.set(getWidth() / 2 - 0.02f * mRadius, getHeight() / 2 - 0.02f * mRadius, getWidth() / 2 + 0.02f * mRadius, getHeight() / 2 + 0.02f * mRadius);
//        mHourHandPaint.setStyle(Paint.Style.STROKE);
//        mHourHandPaint.setStrokeWidth(0.01f * mRadius);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mHourHandPaint);
        mCanvas.restore();
    }

    /**
     * 画分针，根据不断变化的分针角度旋转画布
     */
    private void drawMinuteHand() {
        mCanvas.save();
        mCanvas.rotate(mMinuteDegree, getWidth() / 2, getHeight() / 2);
        mMinuteHandPath.reset();
        float offset = mPaddingTop ;
        mMinuteHandPath.moveTo(getWidth() / 2 - 0.008f * mRadius, getHeight() / 2 - 0.01f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 - 0.007f * mRadius, offset + 0.28f * mRadius);
        mMinuteHandPath.quadTo(getWidth() / 2, offset + 0.25f * mRadius, getWidth() / 2 + 0.007f * mRadius, offset + 0.28f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 + 0.008f * mRadius, getHeight() / 2 - 0.01f * mRadius);
        mMinuteHandPath.close();
        mMinuteHandPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mMinuteHandPath, mMinuteHandPaint);
        mCircleRectF.set(getWidth() / 2 - 0.02f * mRadius, getHeight() / 2 - 0.02f * mRadius, getWidth() / 2 + 0.02f * mRadius, getHeight() / 2 + 0.02f * mRadius);
//        mMinuteHandPaint.setStyle(Paint.Style.STROKE);
//        mMinuteHandPaint.setStrokeWidth(0.02f * mRadius);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mMinuteHandPaint);
        mCanvas.restore();
    }

    /**
     * 获取当前时分秒所对应的角度
     * 为了不让秒针走得像老式挂钟一样僵硬，需要精确到毫秒
     */
    private void getTimeDegree() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TextUtils.isEmpty(mZoneID)? TimeZone.getDefault(): TimeZone.getTimeZone(mZoneID));
        float milliSecond = calendar.get(Calendar.MILLISECOND);
        float second = calendar.get(Calendar.SECOND) + milliSecond / 1000;
        float minute = calendar.get(Calendar.MINUTE) + second / 60;
        float hour = calendar.get(Calendar.HOUR) + minute / 60;
        mSecondDegree = second / 60 * 360;
        mMinuteDegree = minute / 60 * 360;
        mHourDegree = hour / 12 * 360;
    }

    public void setZoneID(String zoneID) {
        mZoneID = zoneID;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int tbValue = (Math.round(sensorEvent.values[1]));
        int lrValue = (Math.round(sensorEvent.values[2]));
        offsetTopAndBottom(tempTBValue - tbValue);
        offsetLeftAndRight(tempLRValue - lrValue);
//        setShadowLayer();
        tempTBValue = tbValue;
        tempLRValue = lrValue;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void setShadowLayer(float radius, float dx, float dy, String color) {
        mScaleLinePaint.setShadowLayer(radius, dx, dy, Color.parseColor(color));
        mSecondHandPaint.setShadowLayer(radius, dx, dy, Color.parseColor(color));
        mMinuteHandPaint.setShadowLayer(radius, dx, dy, Color.parseColor(color));
        mHourHandPaint.setShadowLayer(radius, dx, dy, Color.parseColor(color));
    }
}
