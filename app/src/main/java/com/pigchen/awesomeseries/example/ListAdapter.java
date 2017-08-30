package com.pigchen.awesomeseries.example;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/15
 * @ 描述:
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.pigchen.awesomeseries.R;


public class ListAdapter extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;

    public ListAdapter(Context context) {
        mTitles = new String[]{"sssss1","23234","23234","23234"};
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return  mTitles == null ? 0 : mTitles.length;
    }

    @Override
    public String getItem(int i) {
        return mTitles[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_text, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) view.findViewById(R.id.tv);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTextView.setText(getItem(position));
        return view;
    }
    static class ViewHolder {
        TextView mTextView;
    }
}