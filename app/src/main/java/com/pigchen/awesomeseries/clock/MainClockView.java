package com.pigchen.awesomeseries.clock;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.pigchen.awesomeseries.R;

import java.util.Calendar;


public class MainClockView extends View implements ValueAnimator.AnimatorUpdateListener {
    /* 画布 */
    private Canvas mCanvas;
    /* 小时文本画笔 */
    private Paint mTextPaint;
    /* 测量小时文本宽高的矩形 */
    private Rect mTextRect;
    /* 小时圆圈画笔 */
    private Paint mCirclePaint;
    /* 小时圆圈线条宽度 */
    private float mCircleStrokeWidth = 2;
    /* 小时圆圈的外接矩形 */
    private RectF mCircleRectF;
    /* 刻度圆弧画笔 */
    private Paint mScaleArcPaint;
    /* 刻度圆弧的外接矩形 */
    private RectF mScaleArcRectF;
    /* 刻度线画笔 */
    private Paint mScaleLinePaint;
    /* 时针画笔 */
    private Paint mHourHandPaint;
    /* 内部时钟刻度画笔 */
    private Paint mInnerScaleLinePaint;
    /*最里面的实心白圆*/
    private Paint mInnerWhiteCir;
    /* 分针画笔 */
    private Paint mMinuteHandPaint;
    /* 秒针画笔 */
    private Paint mSecondHandPaint;
    /* 秒针画笔 */
    private Paint mInnerCir;
    /* 时针路径 */
    private Path mHourHandPath;
    /* 分针路径 */
    private Path mMinuteHandPath;
    /* 秒针路径 */
    private Path mSecondHandPath;

    /* 亮色，用于分针、秒针、渐变终止色 */
    private int mLightColor;
    /* 暗色，圆弧、刻度线、时针、渐变起始色 */
    private int mDarkColor;

    private int mCircleColor;
    /* 背景色 */
    private int mBackgroundColor;
    /* 小时文本字体大小 */
    private float mTextSize;
    /* 时钟半径，不包括padding值 */
    private float mRadius;
    /* 刻度线长度 */
    private float mScaleLength;
    /*闪动的长度*/
    private float mShineScaleLength;

    /* 时针角度 */
    private float mHourDegree;
    /* 分针角度 */
    private float mMinuteDegree;
    /* 秒针角度 */
    private float mSecondDegree;

    /* 加一个默认的padding值，为了防止用camera旋转时钟时造成四周超出view大小 */
    private float mDefaultPadding;
    private float mPaddingLeft;
    private float mPaddingTop;
    private float mPaddingRight;
    private float mPaddingBottom;
    private DeviceStatus mStatus;

    /* 梯度扫描渐变 */
    private SweepGradient mSweepGradient;
    /* 渐变矩阵，作用在SweepGradient */
    private Matrix mGradientMatrix;
    private Context context;
    private Handler handler;
    private float mFraction;//变化因子
    private ValueAnimator mAnimator;
    private ValueAnimator mGradientAnimatorMinute;
    private ValueAnimator mGradientAnimatorHour;
    private  int SHINE_INTERVAL = 2000;


    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            connect();
            handler.postDelayed(this, SHINE_INTERVAL);
        }
    };

    public void connect() {
        mAnimator.start();
    }

    public void onStart() {
        handler.removeCallbacks(heartBeatRunnable);
        handler.post(heartBeatRunnable);
    }


    public void onStop() {
        handler.removeCallbacks(heartBeatRunnable);
    }

    public MainClockView(Context context) {
        this(context, null);
    }

    public MainClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MiClockView, defStyleAttr, 0);
        mBackgroundColor = ta.getColor(R.styleable.MiClockView_backgroundColor, getResources().getColor(R.color.primaryColor));
        setBackgroundColor(mBackgroundColor);
        mLightColor = ta.getColor(R.styleable.MiClockView_lightColor, Color.parseColor("#ffffff"));
        mDarkColor = ta.getColor(R.styleable.MiClockView_darkColor, Color.parseColor("#30ffffff"));
        mCircleColor = ta.getColor(R.styleable.MiClockView_darkColor, Color.parseColor("#80ffffff"));
        mTextSize = ta.getDimension(R.styleable.MiClockView_textSize, sp2px(context, 14));
        ta.recycle();

        mHourHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourHandPaint.setStyle(Paint.Style.FILL);
        mHourHandPaint.setColor(Color.parseColor("#000000"));

        mInnerScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerScaleLinePaint.setStyle(Paint.Style.FILL);
        mInnerScaleLinePaint.setColor(Color.parseColor("#000000"));

        mInnerWhiteCir = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerWhiteCir.setStyle(Paint.Style.FILL);
        mInnerWhiteCir.setColor(Color.parseColor("#ffffff"));

        mMinuteHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMinuteHandPaint.setColor(Color.parseColor("#000000"));

        mSecondHandPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondHandPaint.setStyle(Paint.Style.FILL);
        mSecondHandPaint.setColor(Color.parseColor("#000000"));

        mInnerCir = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCir.setStyle(Paint.Style.FILL);
        mInnerCir.setColor(mLightColor);

        mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);
        mScaleLinePaint.setColor(mBackgroundColor);

        mScaleArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleArcPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mCircleColor);
        mTextPaint.setTextSize(mTextSize);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
        mCirclePaint.setColor(mCircleColor);

        mTextRect = new Rect();
        mCircleRectF = new RectF();
        mScaleArcRectF = new RectF();
        mHourHandPath = new Path();
        mMinuteHandPath = new Path();
        mSecondHandPath = new Path();

        mGradientMatrix = new Matrix();
        mStatus = DeviceStatus.CONNECTING;
        mAnimator = ValueAnimator.ofFloat(0.3f, 1f,0.3f).setDuration(SHINE_INTERVAL);
        mAnimator.addUpdateListener(this);

        if (null == handler) {
            handler = new android.os.Handler();
        }
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

    public void setConnectStatus(DeviceStatus status) {
        mStatus =  status;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //宽和高分别去掉padding值，取min的一半即表盘的半径
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingTop() - getPaddingBottom()) / 2;
        mDefaultPadding = 0 ;//根据比例确定默认padding大小
        mPaddingLeft = mDefaultPadding + w / 2 - mRadius + getPaddingLeft();
        mPaddingTop = mDefaultPadding + h / 2 - mRadius + getPaddingTop();
        mPaddingRight = mPaddingLeft;
        mPaddingBottom = mPaddingTop;
        mScaleLength = 0.075f * mRadius;//根据比例确定刻度线长度
        mShineScaleLength = 0.082f * mRadius;//闪动的刻度线长度
        mScaleArcPaint.setStrokeWidth(mScaleLength);
        mScaleLinePaint.setStrokeWidth(0.015f * mRadius);
        //梯度扫描渐变，以(w/2,h/2)为中心点，两种起止颜色梯度渐变
        //float数组表示，[a0,a0.75)为起始颜色所占比例，[a0.75,1}为起止颜色渐变所占比例
        mSweepGradient = new SweepGradient(w / 2, h / 2, new int[]{mDarkColor, mLightColor}, new float[]{0f, 1f});
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        if (mStatus == DeviceStatus.CONNECTED) {
            getTimeDegree();
            drawInnerCirAndScaleMark();
            drawScaleLine();
            drawSecondHand();
            drawHourHand(mStatus);
            drawMinuteHand(mStatus);
        } else if (mStatus == DeviceStatus.CONNECTING || mStatus == DeviceStatus.TIMEOUT) {
            mSecondDegree = 0;
            mMinuteDegree = 60;
            mHourDegree = 305;
            drawShineScaleLine();
            drawInnerCirAndScaleMark();
            drawHourHand(mStatus);
            drawMinuteHand(mStatus);
        } else if (mStatus==DeviceStatus.CONNECTINGED){
            drawInnerCirAndScaleMark();
            drawScaleLine();
            drawSecondHand();
            drawHourHand(mStatus);
            drawMinuteHand(mStatus);
        }
        invalidate();
    }


    /**
     * 获取当前时分秒所对应的角度
     * 为了不让秒针走得像老式挂钟一样僵硬，需要精确到毫秒
     */
    private void getTimeDegree() {
        Calendar calendar = Calendar.getInstance();
        float milliSecond = calendar.get(Calendar.MILLISECOND);
        float second = calendar.get(Calendar.SECOND) + milliSecond / 1000;
        float minute = calendar.get(Calendar.MINUTE) + second / 60;
        float hour = calendar.get(Calendar.HOUR) + minute / 60;
        mSecondDegree = second / 60 * 360;
        mMinuteDegree = minute / 60 * 360;
        mHourDegree = hour / 12 * 360;
    }

    public void setTimeDegree(float hour ,float minute ,float second ) {
        mSecondDegree = second / 60 * 360;
        mMinuteDegree = minute / 60 * 360;
        mHourDegree = hour / 12 * 360;
    }


    /**
     * 画一圈梯度渲染的亮暗色渐变圆弧，重绘时不断旋转，上面盖一圈背景色的刻度线
     */
    private void drawScaleLine() {
        mCanvas.save();
        mScaleArcRectF.set(mPaddingLeft + 1.5f * mScaleLength + mTextRect.height() / 2,
                mPaddingTop + 1.5f * mScaleLength + mTextRect.height() / 2,
                getWidth() - mPaddingRight - mTextRect.height() / 2 - 1.5f * mScaleLength,
                getHeight() - mPaddingBottom - mTextRect.height() / 2 - 1.5f * mScaleLength);
        //matrix默认会在三点钟方向开始颜色的渐变，为了吻合钟表十二点钟顺时针旋转的方向，把秒针旋转的角度减去90度
        mGradientMatrix.setRotate(mSecondDegree - 90, getWidth() / 2, getHeight() / 2);
        mSweepGradient.setLocalMatrix(mGradientMatrix);
        mScaleArcPaint.setShader(mSweepGradient);
        mCanvas.drawArc(mScaleArcRectF, 0, 360, false, mScaleArcPaint);
        //画背景色刻度线
        for (int i = 0; i < 180; i++) {
            mCanvas.drawLine(getWidth() / 2, mPaddingTop + mScaleLength + mTextRect.height() / 2 ,
                    getWidth() / 2, mPaddingTop + 2 * mScaleLength + mTextRect.height() / 2, mScaleLinePaint);
            mCanvas.rotate(2f, getWidth() / 2, getHeight() / 2);
        }
        mCanvas.restore();
    }

    /**
     * 连接中状态闪动效果
     */
    private void drawShineScaleLine() {
        int color = context.getResources().getColor(R.color.white);
        mCanvas.save();
        mScaleArcRectF.set(mPaddingLeft + 1.5f * mScaleLength + mTextRect.height() / 2 - 0.01f * mRadius * mFraction,
                mPaddingTop + 1.5f * mScaleLength + mTextRect.height() / 2 - 0.01f * mRadius * mFraction,
                getWidth() - mPaddingRight - mTextRect.height() / 2 - 1.5f * mScaleLength + 0.01f * mRadius * mFraction,
                getHeight() - mPaddingBottom - mTextRect.height() / 2 - 1.5f * mScaleLength + 0.01f * mRadius * mFraction);
        mScaleArcPaint.setColor(color);
        mScaleArcPaint.setAlpha((int)(255 * mFraction));//根据因子设置透明度
        mCanvas.drawArc(mScaleArcRectF, 0, 360, false, mScaleArcPaint);
        //画背景色刻度线
        for (int i = 0; i < 180; i++) {
            mCanvas.drawLine(getWidth() / 2, mPaddingTop + mScaleLength + mTextRect.height() / 2 - 0.015f * mRadius * mFraction,
                    getWidth() / 2, mPaddingTop + 2 * mScaleLength + mTextRect.height() / 2, mScaleLinePaint);
            mCanvas.rotate(2f, getWidth() / 2, getHeight() / 2);
        }
        mCanvas.restore();
    }

    /**
     * 画秒针，根据不断变化的秒针角度旋转画布
     */
    private void drawSecondHand() {
        mCanvas.save();
        mCanvas.rotate(mSecondDegree, getWidth() / 2, getHeight() / 2);
        mSecondHandPath.reset();
        float offset = mPaddingTop + mScaleLength;
        mSecondHandPath.moveTo(getWidth() / 2, offset);
        mSecondHandPath.lineTo(getWidth() / 2 - 0.03f * mRadius, offset - 0.06f * mRadius);
        mSecondHandPath.lineTo(getWidth() / 2 + 0.03f * mRadius, offset - 0.06f * mRadius);
        mSecondHandPath.close();
        mSecondHandPaint.setColor(mLightColor);
        mCanvas.drawPath(mSecondHandPath, mSecondHandPaint);
        mCanvas.restore();
    }


    /**
     * 画时针，根据不断变化的时针角度旋转画布
     * 针头为圆弧状，使用二阶贝塞尔曲线
     */
    private void drawHourHand(DeviceStatus status) {
        mCanvas.save();
        mCanvas.rotate(mHourDegree, getWidth() / 2, getHeight() / 2);
        mHourHandPath.reset();
        float offset = mPaddingTop ;
        mHourHandPath.moveTo(getWidth() / 2 - 0.01f * mRadius, getHeight() / 2 - 0.01f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 - 0.007f * mRadius, offset + 0.5f * mRadius);
        mHourHandPath.quadTo(getWidth() / 2, offset + 0.48f * mRadius, getWidth() / 2 + 0.007f * mRadius, offset + 0.5f * mRadius);
        mHourHandPath.lineTo(getWidth() / 2 + 0.01f * mRadius, getHeight() / 2 - 0.02f * mRadius);
        mHourHandPath.close();
        mHourHandPaint.setStyle(Paint.Style.FILL);
        mHourHandPaint.setColor(context.getResources().getColor(status == DeviceStatus.CONNECTED?R.color.hourHand_normal:R.color.hourHand_unable));
        mCanvas.drawPath(mHourHandPath, mHourHandPaint);
        mCircleRectF.set(getWidth() / 2 - 0.04f * mRadius, getHeight() / 2 - 0.04f * mRadius, getWidth() / 2 + 0.04f * mRadius, getHeight() / 2 + 0.04f * mRadius);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mHourHandPaint);
        mCanvas.restore();
    }

    /**
     * 画分针，根据不断变化的分针角度旋转画布
     */
    private void drawMinuteHand(DeviceStatus status) {
        mCanvas.save();
        mCanvas.rotate(mMinuteDegree, getWidth() / 2, getHeight() / 2);
        mMinuteHandPath.reset();
        float offset = mPaddingTop ;
        mMinuteHandPath.moveTo(getWidth() / 2 - 0.01f * mRadius, getHeight() / 2 - 0.01f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 - 0.007f * mRadius, offset + 0.4f * mRadius);
        mMinuteHandPath.quadTo(getWidth() / 2, offset + 0.38f * mRadius, getWidth() / 2 + 0.007f * mRadius, offset + 0.4f * mRadius);
        mMinuteHandPath.lineTo(getWidth() / 2 + 0.01f * mRadius, getHeight() / 2 - 0.01f * mRadius);
        mMinuteHandPath.close();
        mMinuteHandPaint.setStyle(Paint.Style.FILL);
        mMinuteHandPaint.setColor(context.getResources().getColor(status == DeviceStatus.CONNECTED?R.color.minuteHand_normal:R.color.minuteHand_unable));
        mCanvas.drawPath(mMinuteHandPath, mMinuteHandPaint);
        mCircleRectF.set(getWidth() / 2 - 0.03f * mRadius, getHeight() / 2 - 0.03f * mRadius, getWidth() / 2 + 0.03f * mRadius, getHeight() / 2 + 0.03f * mRadius);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mMinuteHandPaint);
        mCircleRectF.set(getWidth() / 2 - 0.018f * mRadius, getHeight() / 2 - 0.018f * mRadius, getWidth() / 2 + 0.018f * mRadius, getHeight() / 2 + 0.018f * mRadius);
        mCanvas.drawArc(mCircleRectF, 0, 360, false, mInnerWhiteCir);
        mCanvas.restore();
    }

    private void drawInnerCirAndScaleMark() {
        mCanvas.drawCircle(getWidth() / 2, getHeight() / 2, 0.8f * mRadius, mInnerCir);
        for (int i = 0; i < 60; i = i + 5) {
            mCanvas.drawLine(getWidth() / 2, getHeight() / 2 - 0.8f * mRadius,
                    getWidth() / 2, getHeight() / 2 - 0.68f * mRadius, mInnerScaleLinePaint);
            mCanvas.rotate(30, getWidth() / 2, getHeight() / 2);
        }
    }

    private int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mFraction = (float) valueAnimator.getAnimatedValue();
    }
}
