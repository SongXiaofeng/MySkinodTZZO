package com.skinod.tzzo.skinod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skinod.tzzo.R;
import com.skinod.tzzo.skinod.constant.WifiBean;

import java.util.List;


/**
 * Created by Administrator on 2018/3/7.
 */

public class MyWifiListviewAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final List<WifiBean> mDatas;
    private Context mContext;
    private int level;

    public MyWifiListviewAdapter(Context context, List<WifiBean> datas) {
        this.mContext=context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public WifiBean getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.wifi_item_listview, parent, false); //加载布局
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        } else { //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(mDatas.get(position).getWifiName());
        level=mDatas.get(position).getLevel();
        if (level <= 0 && level >= -50) {
            holder.iv.setBackground(mContext.getDrawable(R.mipmap.ic_wifi_signal_4_dark));
        } else if (level < -50 && level >= -70) {
            holder.iv.setBackground(mContext.getDrawable(R.mipmap.ic_wifi_signal_3_dark));
        } else if (level < -70 && level >= -80) {
            holder.iv.setBackground(mContext.getDrawable(R.mipmap.ic_wifi_signal_2_dark));
        } else if (level < -80 && level >= -100) {
            holder.iv.setBackground(mContext.getDrawable(R.mipmap.ic_wifi_signal_1_dark));
        } else {
            holder.iv.setBackground(mContext.getDrawable(R.mipmap.ic_wifi_signal_1_dark));
        }

        if(mDatas.get(position).getConnectate()==2){
            holder.iv.setBackground(mContext.getDrawable(R.mipmap.wifi_connected));
        }
        return convertView;
    }
    class ViewHolder {
        TextView tv;
        ImageView iv;
    }
}
