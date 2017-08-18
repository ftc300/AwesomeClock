package com.pigchen.awesomeseries;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by chendong on 2017/2/15.
 */
public class CircleProgressView extends View {

    private int mMaxProgress = 100;
    private int mProgress = 0;
    private  int mCircleLineStrokeWidth;
    private  int mShineWidth;
    private  int mShineHourWidth;
    // 画圆所在的距形区域
    private final RectF mRectF;
    private final Paint mEffectPaint;//有
    private final Paint mPaint;
    private final Context mContext;
    private int mDefaultDistance;//
    private int mStatus;
    private static  final int STATUS_SHINE = 0;
    private static  final int STATUS_NORMAL = 1;
    private PathEffect effects ;
    private Canvas mCanvas;
    private float mRadius;


    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mRectF = new RectF();
        mEffectPaint = new Paint();
        mPaint = new Paint();
        effects = new DashPathEffect(new float[]{5,10},1);
        mCircleLineStrokeWidth = DensityUtils.dp2px(context,8);
        mShineWidth = DensityUtils.dp2px(context,16);
        mShineHourWidth = DensityUtils.dp2px(context,24);
        mDefaultDistance = DensityUtils.dp2px(context,24);
        mStatus = -1;

        // 设置画笔相关属性
        mEffectPaint.setAntiAlias(true);
        mEffectPaint.setColor(getResources().getColor(R.color.primaryColor));
        mEffectPaint.setStyle(Paint.Style.STROKE);
        mEffectPaint.setPathEffect(effects);


        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.primaryColor));
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);

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
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom()) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        canvas.drawColor(Color.TRANSPARENT);
        drawBackgroudCir();
//        drawForegroudCir();
        drawShine();
    }

    private void drawShine() {
        mEffectPaint.setAlpha(255);
        mEffectPaint.setStrokeWidth(mShineWidth);
        mCanvas.drawArc(mRectF, -90, -((float) mProgress / mMaxProgress) * 360, false, mEffectPaint);
        for (int i = 0; i < 60; i = i + 5) {
            //2 * mDefaultDistance - mCircleLineStrokeWidth/2 ===== 2 * mDefaultDistance + mCircleLineStrokeWidth/2 - mShineHourWidth
            mCanvas.drawLine(mRadius , 2 * mDefaultDistance + mCircleLineStrokeWidth/2 , mRadius ,  2 * mDefaultDistance + mCircleLineStrokeWidth/2 - mShineHourWidth , mPaint);
            mCanvas.rotate(30,mRadius,mRadius);
        }
    }

    private void drawForegroudCir() {
        mEffectPaint.setAlpha(255);
        mEffectPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mCanvas.drawArc(mRectF, -90, -((float) mProgress / mMaxProgress) * 360, false, mEffectPaint);
    }

    private void drawBackgroudCir() {
        mEffectPaint.setAlpha(51);
        mEffectPaint.setStrokeWidth(mCircleLineStrokeWidth);
        // 位置
        mRectF.left = 2 * mDefaultDistance; // 左上角x
        mRectF.top = 2 * mDefaultDistance; // 左上角y
        mRectF.right = 2*mRadius- 2*mDefaultDistance; // 左下角x
        mRectF.bottom = 2*mRadius  - 2*mDefaultDistance; // 右下角y
        // 绘制圆圈，进度条背景
        mCanvas.drawArc(mRectF, 0,360, false, mEffectPaint);
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(final int progress) {
        mProgress =progress;
        postInvalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    private int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
