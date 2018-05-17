package com.skinod.tzzo.skinod.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.skinod.tzzo.R;
import com.skinod.tzzo.skinod.activity.PlayMusicActivity;
import com.skinod.tzzo.skinod.utils.AudioRecoderUtils;
import com.skinod.tzzo.skinod.utils.TimeUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class RecorFragment extends BaseFragment {

    private ImageView mImageView;
    private TextView mTextView;
    private Button recorder_begin;
    private ListView lv;
    private String music_path;
    private ArrayList<Map<String, Object>> data;
    private AudioRecoderUtils mAudioRecoderUtils;
    private int bt_flag=1;
    protected boolean bt_click=false;
    private SettingsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecoder();
        getDada();

    }

    public void updateList(){
        getDada();
        adapter.notifyDataSetChanged();
    }

    private void getDada() {
        File sampleDir = Environment.getExternalStorageDirectory();
        music_path = sampleDir.getAbsolutePath() + "/record/";
        File[] files = new File(music_path).listFiles();
        data = new ArrayList<>();
        Log.d("songxf", "getDada");
        if (files != null) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    Log.d("songxf", "f=" + f);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("img", R.mipmap.recorder_play_list);
                    map.put("title", f.getName());
                    data.add(map);
                }
            }
        }
        Collections.reverse(data);
        if (data.size() == 0) {
            Log.d("sxf", "没有录音文件");
        }
    }

    private void initRecoder() {
        mAudioRecoderUtils = new AudioRecoderUtils();
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }
            @Override
            public void onStop(String filePath) {
                //  Toast.makeText(RecorderActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                Log.d("sxf","录音保存在："+filePath);
                mTextView.setText(TimeUtils.long2String(0));
                getDada();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListener.onFragmentInteraction(Uri.parse("content://" + "com.fengge.demo" + "/RecorFragment"));
        View view=inflater.inflate(R.layout.fragment_recorder, container, false);
        mImageView =view.findViewById(R.id.iv_recording_icon);
        mTextView =view.findViewById(R.id.tv_recording_time);
        recorder_begin =view.findViewById(R.id.recorder_begin);
        lv = view.findViewById(R.id.lv);
        initAdapter();
        startListener();
        return view;
    }

    private void initAdapter() {
        adapter = new SettingsAdapter(getActivity().getApplicationContext());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String mpath = music_path + data.get(arg2).get("title").toString();
                try {
                    Intent intent = new Intent(RecorFragment.this.getActivity(), PlayMusicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("music_path", mpath);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 101);
                    //MediaPlayer.getConstant(mpath,RecorderActivity.this).startPlay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void startListener() {
        recorder_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sxf","bt_flag="+bt_flag);
                int a= bt_flag&1;
                if(a==1) {
                    mAudioRecoderUtils.startRecord();
                    bt_click=true;
                }else {
                    mAudioRecoderUtils.stopRecord();
                    bt_click=false;
                }
                bt_flag++;
            }
        });
    }

    public class SettingsAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        private SettingsAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder
        {
            private ImageView img;
            public TextView title;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.adapter_play_list,null);
                holder.img = convertView.findViewById(R.id.img);
                holder.title = convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.img.setImageResource((Integer) data.get(position).get("img"));
            holder.title.setText((String)data.get(position).get("title"));
            return convertView;
        }
    }

    public void actionbutton(){
        Log.d("onKeyDown","bt_flag="+bt_flag);
        int a= bt_flag&1;
        if(a==1) {
            mAudioRecoderUtils.startRecord();
            bt_click=true;
        }else {
            mAudioRecoderUtils.stopRecord();
            bt_click=false;
        }
        bt_flag++;
    }
}
