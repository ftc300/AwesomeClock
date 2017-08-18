package com.pigchen.awesomeseries;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/17
 * @ 描述:
 */
public class CustomItem extends LinearLayout {

    private Context mContext;
    private String LabelText;
    private int LabelSize;
    private int LabelColor;
    private String ContentText;
    private int ContentSize;
    private int ContentColor;
    private View rootView ;
    private int CustomDrawable;
    private TextView tvLable;
    private TextView tvContent;
    private ImageView img;

    public CustomItem(Context context) {
        this(context, null);
    }

    public CustomItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setAttribute(context, attrs, defStyle);
        setView();
    }

    public TextView getTvLable() {
        return tvLable;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public ImageView getImg() {
        return img;
    }

    public void setCustomDrawable(int customDrawable) {
        CustomDrawable = customDrawable;
        img.setImageResource(CustomDrawable);
    }

    private void setView() {
        rootView =  inflate(getContext(), R.layout.custom_item, this);
        tvLable = (TextView) rootView.findViewById(R.id.title);
        tvContent = (TextView) rootView.findViewById(R.id.info);
        img = (ImageView) rootView.findViewById(R.id.img);
        tvLable.setText(LabelText);
        tvLable.setTextColor(LabelColor);
        tvLable.setTextSize(TypedValue.COMPLEX_UNIT_PX,LabelSize);
        tvContent.setText(ContentText);
        tvContent.setTextColor(ContentColor);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,ContentSize);
        img.setImageResource(CustomDrawable);
    }

    private void setAttribute(Context context, AttributeSet attrs, int defStyle) {
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomItem, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomItem_labelText:
                    LabelText = a.getString(attr);
                    break;
                case R.styleable.CustomItem_labelSize:
                    LabelSize = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomItem_labelColor:
                    LabelColor = a.getColor(attr, 0);
                    break;
                case R.styleable.CustomItem_contentText:
                    ContentText = a.getString(attr);
                    break;
                case R.styleable.CustomItem_contentSize:
                    ContentSize = a.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.CustomItem_contentColor:
                    ContentColor = a.getColor(attr,0);
                    break;
                case R.styleable.CustomItem_drawable:
                    CustomDrawable = a.getResourceId(attr, 0);
                    break;
            }

        }
        a.recycle();
    }

    public   void setCustomItem(ItemStatus status){
        if(ItemStatus.ENABLE == status){
            LabelColor = Color.parseColor("#000000");
            ContentColor = Color.parseColor("#7F000000");

        }else if(ItemStatus.UNABLE == status){
            LabelColor = Color.parseColor("#4D000000");
            ContentColor = Color.parseColor("#4D000000");
        }
        tvLable.setTextColor(LabelColor);
        tvContent.setTextColor(ContentColor);
    }

}
