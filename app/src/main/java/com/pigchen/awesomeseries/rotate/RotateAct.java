package com.pigchen.awesomeseries.rotate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.pigchen.awesomeseries.R;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/24
 * @ 描述:
 */
public class RotateAct extends AppCompatActivity {
//    CircleMenuLayout menu ;

    RotateView v;
    private TextView textView2;
    private ShimmerFrameLayout shimmerviewcontainer;
    private RotateImage menu;
    private RotateView rotateView;
    private boolean b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rotate2);
        this.rotateView = (RotateView) findViewById(R.id.rotateView);
        this.menu = (RotateImage) findViewById(R.id.menu);
        this.shimmerviewcontainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        this.textView2 = (TextView) findViewById(R.id.textView2);
//        menu = (CircleMenuLayout) findViewById(R.id.menu);
//        menu.setMenuItemIconsAndTexts(new int[]{R.drawable.common_list_item_anchor_normal,R.drawable.common_list_item_anchor_normal,R.drawable.common_list_item_anchor_normal},
//                new String[]{"1","2","3"});
        setImageText();
        v = (RotateView) findViewById(R.id.rotateView);
        v.setRotatDrawableResource(R.drawable.adjust_step_dial);

        ShimmerFrameLayout container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.setDuration(2000);
        container.setIntensity(.5f);
        container.startShimmerAnimation();
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RotateAct.this, "ss", Toast.LENGTH_SHORT).show();
                textView2.setEnabled(b = !b);
            }
        });

    }

    void setImageText(){
        TextView textViewImage = (TextView) findViewById(R.id.textView2);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        ImageSpan imageSpan1 = new ImageSpan(this, R.drawable.list_ic_alarm_normal,ImageSpan.ALIGN_BASELINE);
        SpannableString spannableString1 = new SpannableString("一棵参天大树！");
        spannableString1.setSpan(imageSpan1, spannableString1.length() - 1, spannableString1.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ImageSpan imageSpan = new ImageSpan(this, R.drawable.list_ic_alarm_normal);
        spannableStringBuilder.append(spannableString1);
        spannableStringBuilder.append("=^_^=");
        textViewImage.setText(spannableStringBuilder);
    }
}
