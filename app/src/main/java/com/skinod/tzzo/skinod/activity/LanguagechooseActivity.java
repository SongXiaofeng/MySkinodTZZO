package com.skinod.tzzo.skinod.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.skinod.tzzo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LanguagechooseActivity extends Activity {

    private GridView gview;
    private int posi=-100;
    private ArrayList<Map<String, Object>> data_list;
    private String[] from = {"image", "text"};
    private int[] to = {R.id.image, R.id.text};

 /*   private int[] icon = {R.mipmap.china,R.mipmap.america,R.mipmap.hongkong,R.mipmap.japan,R.mipmap.korea,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher
    };
*/
    private int[] icon= {R.mipmap.china_small_32,R.mipmap.america_small_32,R.mipmap.hongkong_samll_32,R.mipmap.japan_small_32,R.mipmap.korea_small_32,
            R.mipmap.france_samll_32, R.mipmap.spain_samll_32,R.mipmap.th_samll_32,R.mipmap.ara_samll_32,R.mipmap.ru_samll_32,
            R.mipmap.pt_samll_32, R.mipmap.de_samll_32,R.mipmap.it_samll_32,R.mipmap.el_samll_32,R.mipmap.nl_samll_32,
            R.mipmap.pl_samll_32, R.mipmap.bul_samll_32,R.mipmap.est_samll_32,R.mipmap.dan_samll_32,R.mipmap.fin_samll_32,
            R.mipmap.cs_samll_32, R.mipmap.rom_samll_32,R.mipmap.slo_samll_32,R.mipmap.swe_samll_32,R.mipmap.hu_samll_32,R.mipmap.vie_samll_32
    };

    private String[] iconName = {"中文","英语","粤语", "日语","韩语",
            "法语", "西班牙语","泰语","阿拉伯语","俄语",
            "葡萄牙语","德语","意大利语","希腊语","荷兰语",
            "波兰语","保加利亚语","爱沙尼亚语","丹麦语", "芬兰语",
            "捷克语","罗马尼亚语","斯洛文尼亚语","瑞典语","匈牙利语","越南语"};
    private SimpleAdapter sim_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_languagechoose);
        gview = (GridView) findViewById(R.id.gv_dia);

        data_list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        sim_adapter = new SimpleAdapter(this, data_list, R.layout.gv_dia_item, from, to);
        gview.setAdapter(sim_adapter);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                posi=position;
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK,new Intent().putExtra("choosegriv",posi));
        super.onBackPressed();
    }
}
