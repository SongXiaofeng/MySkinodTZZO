package com.skinod.tzzo.skinod.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skinod.tzzo.R;
import com.skinod.tzzo.skinod.constant.RobotMessage;
import com.skinod.tzzo.skinod.datasave.ContentBean;
import com.skinod.tzzo.skinod.utils.LogUtil;

import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyHolder> {
    private Context mContext;
    private List<ContentBean> mDatas;

    public MyRecyclerAdapter(Context context, List<ContentBean> datas) {
        super();
        Log.d("sxf", "datas=" + datas.size());
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        LogUtil.d("sxf", "onBindViewHolder_position=" + position + ";mDatas.get(position).getName()=" + mDatas.get(position).getType()                + "mDatas.get(position).getContent=" + mDatas.get(position).getText());


        if (mDatas.get(position).getType()==1) {
            holder.rl_send.setVisibility(View.GONE);
            holder.ll_from.setVisibility(View.VISIBLE);
             /*   holder.ll_chat_from.setVisibility(View.VISIBLE);
                holder.chat_from_content.setVisibility(View.VISIBLE);

                holder.chat_send_content.setVisibility(View.GONE);
                holder.ll_chat_send.setVisibility(View.GONE);*/

            holder.chat_from_content.setText(mDatas.get(position).getText());

        } else {
            holder.rl_send.setVisibility(View.VISIBLE);
            holder.ll_from.setVisibility(View.GONE);

           /*     holder.ll_chat_send.setVisibility(View.VISIBLE);
                holder.ll_chat_from.setVisibility(View.GONE);

                holder.chat_send_content.setVisibility(View.VISIBLE);
                holder.chat_from_content.setVisibility(View.GONE);*/

            holder.chat_send_content.setText(mDatas.get(position).getText());
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        Log.d("sxf", "arg1=" + arg1);
        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.main_chat_from_msg, null);

        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_from;
        private RelativeLayout rl_send;
        private TextView chat_from_content, chat_send_content;

        public MyHolder(View view) {
            super(view);

            ll_from = (LinearLayout) view.findViewById(R.id.ll_from);
            rl_send = (RelativeLayout) view.findViewById(R.id.rl_send);
            chat_from_content = (TextView) view.findViewById(R.id.chat_from_content);
            chat_send_content = (TextView) view.findViewById(R.id.chat_send_content);
            chat_from_content = (TextView) view.findViewById(R.id.chat_from_content);
            chat_send_content = (TextView) view.findViewById(R.id.chat_send_content);

        }
    }


}
