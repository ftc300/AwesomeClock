package com.pigchen.awesomeseries.page3;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.pigchen.awesomeseries.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用主界面
 *
 * @author AigeStudio
 * @since 2014/12/15
 * @version 1.0.0
 *
 */
public class PageCurlActivity extends Activity {
	private PageTurnView mPageCurlView;// 翻页控件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curl_page);

		mPageCurlView = (PageTurnView) findViewById(R.id.main_pcv);

		Bitmap bitmap = null;
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pageone);
		bitmaps.add(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pagetwo);
		bitmaps.add(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pagethree);
		bitmaps.add(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pagefour);
		bitmaps.add(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pagefive);
		bitmaps.add(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pagesix);
		bitmaps.add(bitmap);
		mPageCurlView.setBitmaps(bitmaps);
	}
}
