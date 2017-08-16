package com.pigchen.awesomeseries;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/15
 * @ 描述: 自定义 LinearLayout 实现类似 CoordinatorLayout 的效果
 */
public class DragLayout extends LinearLayout implements NestedScrollingParent {
    private NestedScrollingParentHelper nsph;
    private float moveDir ;
    private float count;
    //分别表示RecyclerView上面那个View（是个ViewGroup），Recyclerview，收缩之后上面显示的View，展开之后上面显示的View
    private View topView, bottomView, smallView, bigView;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        nsph = new NestedScrollingParentHelper(this);
    }

    //onFinishInflate方法中获得各个子view
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView = getChildAt(0);
        bottomView = getChildAt(1);
        smallView = ((ViewGroup) topView).getChildAt(0);
        bigView = ((ViewGroup) topView).getChildAt(1);
        smallView.setAlpha(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //重新设置Recyclerview的高度，保证Recyclerview的高度和收缩之后上面显示的View的高度之和等于整个ViewGroup的高度
        LayoutParams params = (LayoutParams) bottomView.getLayoutParams();
        int bottomHeight = getHeight() - smallView.getHeight();
        if (bottomHeight != 0) {
            params.height = bottomHeight;
            bottomView.setLayoutParams(params);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.d("TAG", "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        nsph.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //满足两种情况是可以滑动，第一是向上滑并且滑动的高度小于可滑动的最大值
        //第二种是向下滑动，并且滑动的高度大于0
        if ((dy > 0 && (bottomView.getTranslationY() > smallView.getHeight() - topView.getHeight())
                && ((RecyclerView) bottomView).getChildCount() > 0) //如果RecyclerView为空，则不能向上滑动
                || (dy < 0 && bottomView.getTranslationY() < 0
                //判断RecyclerView内部是否可以向下滑动，如果可以，则Recyclerview无法整体向下滑动
                && !ViewCompat.canScrollVertically(target, -1))) {
//            if (count == 0) {
//                moveDir = dy > 0 ? 1 : 2;
//            }
            consumed[1] = dy;
            Log.d("TAG", "dy = " + dy);
            bottomView.setTranslationY(bottomView.getTranslationY() - dy);
            Log.d("TAG", "translationY = " + bottomView.getTranslationY());
            bigView.setAlpha((smallView.getHeight() - topView.getHeight() - 2 * bottomView.getTranslationY())
                    / (smallView.getHeight() - topView.getHeight()));
            smallView.setAlpha((2 * bottomView.getTranslationY()
                    - (smallView.getHeight() - topView.getHeight())) / (smallView.getHeight() - topView.getHeight()));

        }
    }


    private void setHeight(final LinearLayout layout, float currentHeight, float targetHeight){
        ValueAnimator va ;
        va =  ValueAnimator.ofInt((int)currentHeight, (int)targetHeight);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                layout.getLayoutParams().height = (Integer)animation.getAnimatedValue();
                layout.requestLayout();
            }
        });
        va.start();
    }

    @Override
    public void onStopNestedScroll(View child) {
        nsph.onStopNestedScroll(child);
        float stopValue;
        //手指松开时判断当前的位置，大于一半向上移到最高点，小于一半向下移到原始位置
        if (bottomView.getTranslationY() > (smallView.getHeight() - topView.getHeight()) / 2) {
            stopValue = 0;
        } else {
            stopValue = smallView.getHeight() - topView.getHeight();
        }
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(bottomView, "translationY", bottomView.getTranslationY(), stopValue);
        ObjectAnimator smallAnimator = ObjectAnimator.ofFloat(smallView, "alpha",
                smallView.getAlpha(), stopValue / (smallView.getHeight() - topView.getHeight()));
        ObjectAnimator bigAnimator = ObjectAnimator.ofFloat(bigView, "alpha", bigView.getAlpha(),
                (smallView.getHeight() - topView.getHeight() - stopValue) / (smallView.getHeight() - topView.getHeight()));
        AnimatorSet set = new AnimatorSet();
        set.play(bottomAnimator).with(smallAnimator).with(bigAnimator);
        set.start();

    }

}