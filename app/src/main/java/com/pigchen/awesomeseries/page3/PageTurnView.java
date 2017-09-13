package com.pigchen.awesomeseries.page3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 翻页View
 *
 * @author AigeStudio {@link http://blog.csdn.net/aigestudio}
 * @version 1.0.0
 * @since 2014/12/20
 */
public class PageTurnView extends View {
	private static final float TEXT_SIZE_NORMAL = 1 / 40F, TEXT_SIZE_LARGER = 1 / 20F;// 标准文字尺寸和大号文字尺寸的占比
	private static final float AUTO_AREA_LEFT = 1 / 5F, AUTO_AREA_RIGHT = 4 / 5F;// 控件左右侧自动滑动区域占比
	private static final float MOVE_VALID = 1 / 100F;// 移动事件的有效距离占比

	private TextPaint mTextPaint;// 文本画笔
	private Context mContext;// 上下文环境引用

	private List<Bitmap> mBitmaps;// 位图数据列表

	private int pageIndex;// 当前显示mBitmaps数据的下标
	private int mViewWidth, mViewHeight;// 控件宽高

	private float mTextSizeNormal, mTextSizeLarger;// 标准文字尺寸和大号文字尺寸
	private float mClipX;// 裁剪右端点坐标
	private float mAutoAreaLeft, mAutoAreaRight;// 控件左侧和右侧自动吸附的区域
	private float mCurPointX;// 指尖触碰屏幕时点X的坐标值
	private float mMoveValid;// 移动事件的有效距离

	private boolean isNextPage, isLastPage;// 是否该显示下一页、是否最后一页的标识值

	public PageTurnView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		/*
		 * 实例化文本画笔并设置参数
		 */
		mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 每次触发TouchEvent重置isNextPage为true
		isNextPage = true;

		/*
		 * 判断当前事件类型
		 */
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:// 触摸屏幕时
				// 获取当前事件点x坐标
				mCurPointX = event.getX();

			/*
			 * 如果事件点位于回滚区域
			 */
				if (mCurPointX < mAutoAreaLeft) {
					// 那就不翻下一页了而是上一页
					isNextPage = false;
					pageIndex--;
					mClipX = mCurPointX;
					invalidate();
				}
				break;
			case MotionEvent.ACTION_MOVE:// 滑动时
				float SlideDis = mCurPointX - event.getX();
				if (Math.abs(SlideDis) > mMoveValid) {
					// 获取触摸点的x坐标
					mClipX = event.getX();

					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:// 触点抬起时
				// 判断是否需要自动滑动
				judgeSlideAuto();

			/*
			 * 如果当前页不是最后一页
			 * 如果是需要翻下一页
			 * 并且上一页已被clip掉
			 */
				if (!isLastPage && isNextPage && mClipX <= 0) {
					pageIndex++;
					mClipX = mViewWidth;
					invalidate();
				}
				break;
		}
		return true;
	}

	/**
	 * 判断是否需要自动滑动
	 * 根据参数的当前值判断自动滑动
	 */
	private void judgeSlideAuto() {
		/*
		 * 如果裁剪的右端点坐标在控件左端十分之一的区域内，那么我们直接让其自动滑到控件左端
		 */
		if (mClipX < mAutoAreaLeft) {
			while (mClipX > 0) {
				mClipX--;
				invalidate();
			}
		}
		/*
		 * 如果裁剪的右端点坐标在控件右端十分之一的区域内，那么我们直接让其自动滑到控件右端
		 */
		if (mClipX > mAutoAreaRight) {
			while (mClipX < mViewWidth) {
				mClipX++;
				invalidate();
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// 获取控件宽高
		mViewWidth = w;
		mViewHeight = h;

		// 初始化位图数据
		initBitmaps();

		// 计算文字尺寸
		mTextSizeNormal = TEXT_SIZE_NORMAL * mViewHeight;
		mTextSizeLarger = TEXT_SIZE_LARGER * mViewHeight;

		// 初始化裁剪右端点坐标
		mClipX = mViewWidth;

		// 计算控件左侧和右侧自动吸附的区域
		mAutoAreaLeft = mViewWidth * AUTO_AREA_LEFT;
		mAutoAreaRight = mViewWidth * AUTO_AREA_RIGHT;

		// 计算一度的有效距离
		mMoveValid = mViewWidth * MOVE_VALID;
	}

	/**
	 * 初始化位图数据
	 * 缩放位图尺寸与屏幕匹配
	 */
	private void initBitmaps() {
		List<Bitmap> temp = new ArrayList<Bitmap>();
		for (int i = mBitmaps.size() - 1; i >= 0; i--) {
			Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), mViewWidth, mViewHeight, true);
			temp.add(bitmap);
		}
		mBitmaps = temp;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		/*
		 * 如果数据为空则显示默认提示文本
		 */
		if (null == mBitmaps || mBitmaps.size() == 0) {
			defaultDisplay(canvas);
			return;
		}

		// 绘制位图
		drawBtimaps(canvas);
	}

	/**
	 * 默认显示
	 *
	 * @param canvas
	 *            Canvas对象
	 */
	private void defaultDisplay(Canvas canvas) {
		// 绘制底色
		canvas.drawColor(Color.WHITE);

		// 绘制标题文本
		mTextPaint.setTextSize(mTextSizeLarger);
		mTextPaint.setColor(Color.RED);
		canvas.drawText("FBI WARNING", mViewWidth / 2, mViewHeight / 4, mTextPaint);

		// 绘制提示文本
		mTextPaint.setTextSize(mTextSizeNormal);
		mTextPaint.setColor(Color.BLACK);
		canvas.drawText("Please set data use setBitmaps method", mViewWidth / 2, mViewHeight / 3, mTextPaint);
	}

	/**
	 * 绘制位图
	 *
	 * @param canvas
	 *            Canvas对象
	 */
	private void drawBtimaps(Canvas canvas) {
		// 绘制位图前重置isLastPage为false
		isLastPage = false;

		// 限制pageIndex的值范围
		pageIndex = pageIndex < 0 ? 0 : pageIndex;
		pageIndex = pageIndex > mBitmaps.size() ? mBitmaps.size() : pageIndex;

		// 计算数据起始位置
		int start = mBitmaps.size() - 2 - pageIndex;
		int end = mBitmaps.size() - pageIndex;

		/*
		 * 如果数据起点位置小于0则表示当前已经到了最后一张图片
		 */
		if (start < 0) {
			// 此时设置isLastPage为true
			isLastPage = true;

			// 并显示提示信息
			showToast("This is fucking lastest page");

			// 强制重置起始位置
			start = 0;
			end = 1;
		}

		for (int i = start; i < end; i++) {
			canvas.save();

			/*
			 * 仅裁剪位于最顶层的画布区域
			 * 如果到了末页则不在执行裁剪
			 */
			if (!isLastPage && i == end - 1) {
				canvas.clipRect(0, 0, mClipX, mViewHeight);
			}
			canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);

			canvas.restore();
		}
	}

	/**
	 * 设置位图数据
	 *
	 * @param bitmaps
	 *            位图数据列表
	 */
	public synchronized void setBitmaps(List<Bitmap> bitmaps) {
		/*
		 * 如果数据为空则抛出异常
		 */
		if (null == bitmaps || bitmaps.size() == 0)
			throw new IllegalArgumentException("no bitmap to display");

		/*
		 * 如果数据长度小于2则GG思密达
		 */
		if (bitmaps.size() < 2)
			throw new IllegalArgumentException("fuck you and fuck to use imageview");

		mBitmaps = bitmaps;
		invalidate();
	}

	/**
	 * Toast显示
	 *
	 * @param msg
	 *            Toast显示文本
	 */
	private void showToast(Object msg) {
		Toast.makeText(mContext, msg.toString(), Toast.LENGTH_SHORT).show();
	}
}
