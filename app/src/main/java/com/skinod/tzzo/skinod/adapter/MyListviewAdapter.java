package com.skinod.tzzo.skinod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skinod.tzzo.R;
import com.skinod.tzzo.skinod.constant.Bean;

import java.util.List;



public class MyListviewAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final List<Bean> mDatas;
    private Context mContext;

    public MyListviewAdapter(Context context, List<Bean> datas) {
        this.mContext=context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_listview, parent, false); //加载布局
            holder = new ViewHolder();

            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);

            convertView.setTag(holder);
        } else { //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (ViewHolder) convertView.getTag();
        }

        Bean bean = mDatas.get(position);
        holder.tv.setText(bean.getTitle());
        holder.iv.setBackground(mContext.getDrawable(bean.getId()));
        return convertView;
    }


    class ViewHolder {
        TextView tv;
        ImageView iv;
    }
}
