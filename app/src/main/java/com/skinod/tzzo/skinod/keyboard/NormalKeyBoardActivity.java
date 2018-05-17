package com.skinod.tzzo.skinod.keyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.skinod.tzzo.R;
import com.skinod.tzzo.skinod.application.Myapplication;
import com.skinod.tzzo.skinod.utils.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class NormalKeyBoardActivity extends AppCompatActivity {

    private VirtualKeyboardView virtualKeyboardView;
    private ArrayList<Map<String, String>> valueList;
    private EditText textAmount;
    private Animation enterAnim;
    private Animation exitAnim;
    private Intent intent;
    private int currentkeyboard = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_key_board);
        intent = new Intent();
        initAnim();
        initView();
        valueList = virtualKeyboardView.getValueList();
    }

    private void initAnim() {
        enterAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);
    }

    private void initView() {
        textAmount = findViewById(R.id.textAmount);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(textAmount, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        virtualKeyboardView = findViewById(R.id.virtualKeyboardView);
        GridView gridView = virtualKeyboardView.getGridView();
        gridView.setOnItemClickListener(onItemClickListener);
        textAmount.setOnClickListener((View view) -> onKeyClick());
    }

    private void onKeyClick() {
        virtualKeyboardView.setFocusable(true);
        virtualKeyboardView.setFocusableInTouchMode(true);
        virtualKeyboardView.startAnimation(enterAnim);
        virtualKeyboardView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.d("keydonw_currentkeyboard=" + currentkeyboard + ";event.getKeyCode()=" + event.getKeyCode());
        //|| event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT ) {
            if (currentkeyboard == Myapplication.NUMBER_KEYBOARD) {
                virtualKeyboardView.notifyadapter(Myapplication.CHAR_KEYBOARD);
                currentkeyboard = Myapplication.CHAR_KEYBOARD;
            } else if (currentkeyboard == Myapplication.LATTER_KEYBOARD) {
                virtualKeyboardView.notifyadapter(Myapplication.NUMBER_KEYBOARD);
                currentkeyboard = Myapplication.NUMBER_KEYBOARD;
            } else if (currentkeyboard == Myapplication.CHAR_KEYBOARD) {
                virtualKeyboardView.notifyadapter(Myapplication.LATTER_KEYBOARD);
                currentkeyboard = Myapplication.LATTER_KEYBOARD;
            }


        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT ) {
            if (currentkeyboard == Myapplication.NUMBER_KEYBOARD) {
                virtualKeyboardView.notifyadapter(Myapplication.LATTER_KEYBOARD);
                currentkeyboard = Myapplication.LATTER_KEYBOARD;
            } else if (currentkeyboard == Myapplication.LATTER_KEYBOARD) {
                virtualKeyboardView.notifyadapter(Myapplication.CHAR_KEYBOARD);
                currentkeyboard = Myapplication.CHAR_KEYBOARD;
            } else if (currentkeyboard == Myapplication.CHAR_KEYBOARD) {
                virtualKeyboardView.notifyadapter(Myapplication.NUMBER_KEYBOARD);
                currentkeyboard = Myapplication.NUMBER_KEYBOARD;
            }
        }
        return false;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            LogUtil.d("AdapterView_onItemClick_position=" + position);
            if (currentkeyboard == Myapplication.NUMBER_KEYBOARD) {
                if (position < 11 && position != 9) {    //点击0~9按钮
                    String amount = textAmount.getText().toString().trim();
                    amount = amount + valueList.get(position).get("name");
                    textAmount.setText(amount);
                    Editable ea = textAmount.getText();
                    textAmount.setSelection(ea.length());
                } else {
                    if (position == 9) {
                        virtualKeyboardView.startAnimation(exitAnim);
                        virtualKeyboardView.setVisibility(View.GONE);
                        intent.putExtra("wifipassword", textAmount.getText().toString());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    if (position == 11) {
                        String amount = textAmount.getText().toString().trim();
                        if (amount.length() > 0) {
                            amount = amount.substring(0, amount.length() - 1);
                            textAmount.setText(amount);
                            Editable ea = textAmount.getText();
                            textAmount.setSelection(ea.length());
                        }
                    }
              /*      if (position > 12) {
                        //if (position == 12) currentkeyboard = Myapplication.NUMBER_KEYBOARD;
                        if (position == 13) currentkeyboard = Myapplication.LATTER_KEYBOARD;
                        if (position == 14) currentkeyboard = Myapplication.CHAR_KEYBOARD;
                        virtualKeyboardView.notifyadapter(currentkeyboard);
                    }*/
                }
            } else if (currentkeyboard == Myapplication.LATTER_KEYBOARD) {
                if (position < 26 && position > 0) {    //点击0~9按钮
                    String amount = textAmount.getText().toString().trim();
                    amount = amount + valueList.get(position).get("name");
                    textAmount.setText(amount);
                    Editable ea = textAmount.getText();
                    textAmount.setSelection(ea.length());
                }
         /*       if (position > 25 && position!=27) {
                    if (position == 26) currentkeyboard = Myapplication.NUMBER_KEYBOARD;
                  //  if (position == 27) currentkeyboard = Myapplication.LATTER_KEYBOARD;
                    if (position == 28) currentkeyboard = Myapplication.CHAR_KEYBOARD;
                    virtualKeyboardView.notifyadapter(currentkeyboard);
                }*/
            } else if (currentkeyboard == Myapplication.CHAR_KEYBOARD) {
                if (position >= 0 && position <= 31) {
                    String amount = textAmount.getText().toString().trim();
                    amount = amount + valueList.get(position).get("name");
                    textAmount.setText(amount);
                    Editable ea = textAmount.getText();
                    textAmount.setSelection(ea.length());
                }
             /*   if (position == 32) currentkeyboard = Myapplication.NUMBER_KEYBOARD;
                //  if (position == 27) currentkeyboard = Myapplication.LATTER_KEYBOARD;
                if (position == 34) currentkeyboard = Myapplication.CHAR_KEYBOARD;*/
            }
        }
    };
}
