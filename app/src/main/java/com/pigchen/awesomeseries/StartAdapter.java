package com.pigchen.awesomeseries;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/24
 * @ 描述:
 */


public class StartAdapter extends RecyclerView.Adapter<StartAdapter.StartViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;
    private ItemClickListener listener;

    public StartAdapter setListener(ItemClickListener listener) {
        this.listener = listener;
        return this;
    }
    public StartAdapter(Context context) {
        mTitles = new String[]{"时钟","圆弧旋转","圆形进度"};
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public StartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StartAdapter.StartViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(StartViewHolder holder, final int position) {
        holder.tv.setText(mTitles[position]);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(null!=listener){
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    public static class StartViewHolder extends RecyclerView.ViewHolder{
        TextView tv ;
        public StartViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public interface  ItemClickListener{
        void onItemClick(int pos);
    }
}
