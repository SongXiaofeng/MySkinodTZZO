package com.skinod.tzzo.skinod.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.skinod.tzzo.R;

import java.util.ArrayList;


public class KeyboardActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Button> btlist = new ArrayList<>();
    private String[] strnum = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private int[] strid = {R.id.bt0,  R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4,
            R.id.bt5, R.id.bt6, R.id.bt7, R.id.bt8, R.id.bt9, R.id.num, R.id.letter,R.id.empty};
    private String[] strletter = {"abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwx", "yz","*#@"};

    private int[] strclictime = {1,1,1,1,1,1,1,1,1,1};
    private Button bt;
    private Button btnum, btletter;
    private final int BT_NUM=1;
    private final int BT_LETTER=2;
    private  int BtFlag=1;
    private TextView tv;
    private String TAG="sxf";
    private  int clicktime=1;
    private int lasttimebt;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        initViews();
    }

    private void initViews() {
        tv=findViewById(R.id.tv);
        for (int id : strid) {
            bt = findViewById(id);
            bt.setOnClickListener(this);
            btlist.add(bt);
        }
        Log.d("sxf", "btlist=" + btlist.size());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.letter) {
            if (BtFlag != BT_LETTER) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < strletter.length; i++) {
                            btlist.get(i).setText(strletter[i]);
                        }
                    }
                });
                BtFlag = BT_LETTER;
            }
        } else if (v.getId() == R.id.num) {
            if (BtFlag != BT_NUM) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < strnum.length; i++) {
                            btlist.get(i).setText(strnum[i]);
                        }
                    }
                });
                BtFlag = BT_NUM;
            }
        } else if (v.getId() == R.id.empty) {
            if(tv.length()==0){
                tv.setText("");
            }else{
                tv.setText(tv.getText().toString().substring(0,tv.length()-1));
            }
        } else {

            Log.d(TAG, "id=" + v.getId());
            for (int i = 0; i < strnum.length; i++) {
                if (v.getId() == strid[i]) {
                    if (BtFlag == BT_LETTER) {
                        if (strclictime[i] == 1) {
                            tv.append(strletter[i].substring(0, 1));
                            strclictime[i] = strclictime[i] + 1;
                            for(int j=0;j<strletter.length;j++){
                                if(j!=i) strclictime[j]=1;
                            }

                        } else if (strclictime[i] == 2) {
                            //tv.append(strletter[i].substring(1, 2));
                            strclictime[i] = strclictime[i] + 1;
                            str=tv.getText().toString();
                            tv.setText(str.replace(str.charAt(str.length()-1)+"",strletter[i].substring(1, 2)));
                        } else if (strclictime[i] == 3) {
                            //  tv.append(strletter[i].substring(2));
                            str=tv.getText().toString();
                            strclictime[i] = 1;
                            tv.setText(str.replace(str.charAt(str.length()-1)+"",strletter[i].substring(2)));
                        }
                    } else {
                        tv.append(strnum[i]);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed()");
        setResult(Activity.RESULT_OK,new Intent().putExtra("password",tv.getText()));
        super.onBackPressed();
    }
}