package com.skinod.tzzo.skinod.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.skinod.tzzo.R;
import com.skinod.tzzo.baiduasrtts.MainHandlerConstant;
import com.skinod.tzzo.baiduasrtts.recognization.IStatus;
import com.skinod.tzzo.skinod.activity.LanguagechooseActivity;
import com.skinod.tzzo.skinod.adapter.MyRecyclerAdapter;
import com.skinod.tzzo.skinod.application.Myapplication;
import com.skinod.tzzo.skinod.datasave.CdufSplite;
import com.skinod.tzzo.skinod.datasave.ContentBean;
import com.skinod.tzzo.skinod.datasave.DatabaseHelper;
import com.skinod.tzzo.skinod.utils.LogUtil;
import java.util.ArrayList;


public class ConverFragment extends BaseFragment implements MainHandlerConstant, IStatus, View.OnClickListener {

    private int[] icon_samll = {R.mipmap.china_small_32, R.mipmap.america_small_32, R.mipmap.hongkong_samll_32, R.mipmap.japan_small_32, R.mipmap.korea_small_32,
            R.mipmap.france_samll_32, R.mipmap.spain_samll_32, R.mipmap.th_samll_32, R.mipmap.ara_samll_32, R.mipmap.ru_samll_32,
            R.mipmap.pt_samll_32, R.mipmap.de_samll_32, R.mipmap.it_samll_32, R.mipmap.el_samll_32, R.mipmap.nl_samll_32,
            R.mipmap.pl_samll_32, R.mipmap.bul_samll_32, R.mipmap.est_samll_32, R.mipmap.dan_samll_32, R.mipmap.fin_samll_32,
            R.mipmap.cs_samll_32, R.mipmap.rom_samll_32, R.mipmap.slo_samll_32, R.mipmap.swe_samll_32, R.mipmap.hu_samll_32, R.mipmap.vie_samll_32
    };
    private String[] iconName = {"中文", "英语", "粤语", "日语", "韩语",
            "法语", "西班牙语", "泰语", "阿拉伯语", "俄语",
            "葡萄牙语", "德语", "意大利语", "希腊语", "荷兰语",
            "波兰语", "保加利亚语", "爱沙尼亚语", "丹麦语", "芬兰语",
            "捷克语", "罗马尼亚语", "斯洛文尼亚语", "瑞典语", "匈牙利语", "越南语"};

/*    private String[] lan = {"zh", "en", "yue", "jp", "kor",
            "fra", "spa", "th", "ara", "ru",
            "pt", "de", "it", "el", "nl",
            "pl", "bul", "est", "dan", "fin",
            "cs", "rom", "slo", "swe", "hu", "vie"};*/

    protected Button bt_chooseLan_a, bt_chooseLan_b;
    public ArrayList<ContentBean> mDatas;
    public MyRecyclerAdapter adapter;
    private MyBroadcastReceiver broadrec;
    private LinearLayoutManager layoutManager;
    public static  DatabaseHelper dbHelper;
    private CdufSplite cdufSplite;
    private int i;
    private SharedPreferences sp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("converfragment_oncreate");

        dbHelper = new DatabaseHelper(this.getActivity(), Myapplication.CHAT_DATABASE_NAME);
        cdufSplite = new CdufSplite(dbHelper);

        HandlerThread myThread = new HandlerThread("converfragment");
        myThread.start();

        sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        broadrec = new MyBroadcastReceiver();
        IntentFilter ifter = new IntentFilter();
        ifter.addAction("com.skinod.send_translate_res");
        this.getActivity().registerReceiver(broadrec, ifter);

        mDatas = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("converfragment_onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("converfragment_onCreateView");
        mListener.onFragmentInteraction(Uri.parse("content://" + "com.fengge.demo" + "/ConverFragment"));

        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        if (mDatas.size() == 0) {
            mDatas = cdufSplite.findall();
        }
        if (mDatas.size() > 4) moveToPosition(layoutManager, mDatas.size() - 1);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerAdapter(this.getActivity(), mDatas);
        recyclerView.setAdapter(adapter);

        bt_chooseLan_a = view.findViewById(R.id.bt_chooseLan_a);
        bt_chooseLan_b = view.findViewById(R.id.bt_chooseLan_b);

        bt_chooseLan_a.setOnClickListener(this);
        bt_chooseLan_b.setOnClickListener(this);

        bt_chooseLan_b.setFocusable(true);
        //btn.setFocusableInTouchMode(true);
        bt_chooseLan_b.requestFocus();
       // btn.requestFocusFromTouch();

        choosebuttonready();


        return view;
    }

    private void choosebuttonready() {

        int left = sp.getInt("Left", -1);
        int right = sp.getInt("Right", -1);
        if (left == -1) left = 0;
        if (right == -1) right = 1;

        bt_chooseLan_a.setText(iconName[left]);
       Drawable ldrawable = ContextCompat.getDrawable(this.getActivity(), icon_samll[left]);
        ldrawable.setBounds(0, 0, ldrawable.getMinimumWidth(), ldrawable.getMinimumHeight());
        bt_chooseLan_a.setCompoundDrawables(ldrawable, null, null, null);

        bt_chooseLan_b.setText(iconName[right]);
        Drawable rdrawable = ContextCompat.getDrawable(this.getActivity(), icon_samll[right]);
        rdrawable.setBounds(0, 0, rdrawable.getMinimumWidth(), rdrawable.getMinimumHeight());
        bt_chooseLan_b.setCompoundDrawables(rdrawable, null, null, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_chooseLan_a:
                startActivityForResult(new Intent(this.getActivity(), LanguagechooseActivity.class), 200);
                break;
            case R.id.bt_chooseLan_b:
                startActivityForResult(new Intent(this.getActivity(), LanguagechooseActivity.class), 300);
                break;
        }
    }


//   ****************************************************************************************  //

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //int ik = data.getIntExtra("choosegriv", -1);
        if (resultCode == Activity.RESULT_OK) {
            i = data.getIntExtra("choosegriv", -1);
            if (i != -1 && i!=-100) {
                setCurrenStatus(requestCode);
            }
        }
    }

    private void setCurrenStatus(int code) {
        SharedPreferences.Editor edit;
        Drawable drawable = ContextCompat.getDrawable(this.getActivity(), icon_samll[i]);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        if (code == 200) {
            //Myapplication.current_left_languange = lan[i];
            bt_chooseLan_a.setText(iconName[i]);
            edit = sp.edit();
            edit.putInt("Left", i);
            edit.commit();
            bt_chooseLan_a.setCompoundDrawables(drawable, null, null, null);
        }
        if (code == 300) {
            //  Myapplication.current_right_languange = lan[i];
            edit = sp.edit();
            edit.putInt("Right", i);
            edit.commit();
            bt_chooseLan_b.setText(iconName[i]);
            bt_chooseLan_b.setCompoundDrawables(drawable, null, null, null);
        }

    }


    public static void moveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
        this.getActivity().unregisterReceiver(broadrec);
        LogUtil.d("converfragment_onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("converfragment_onStop");
    }

    public void notifyupdate() {

        ContentBean rml;
        ContentBean rmr;
        if (Myapplication.AUDIO_BUTTION_FLAG == 100) {
            rml = new ContentBean(1, Myapplication.baidutranres.split("_")[0]);
            rmr = new ContentBean(2, Myapplication.baidutranres.split("_")[1]);

        } else {
            rml = new ContentBean(2, Myapplication.baidutranres.split("_")[0]);
            rmr = new ContentBean(1, Myapplication.baidutranres.split("_")[1]);
        }

        mDatas.add(rml);
        mDatas.add(rmr);
        adapter.notifyDataSetChanged();
        if (mDatas.size() > 4) moveToPosition(layoutManager, mDatas.size() - 1);
        cdufSplite.add(rml);
        cdufSplite.add(rmr);
    }

    public void resetFocused() {
        bt_chooseLan_b.setFocusable(true);
        bt_chooseLan_b.requestFocus();
        choosebuttonready();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("MyBroadcastReceiver_onReceive_intent.getAction()=" + intent.getAction());
            if (intent.getAction() != null && intent.getAction().equals("com.skinod.send_translate_res")) {
                notifyupdate();
            }
        }
    }
}
