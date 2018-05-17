package com.skinod.tzzo.skinod.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.skinod.tzzo.R;
import com.skinod.tzzo.skinod.application.Myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟键盘
 */
public class VirtualKeyboardView extends RelativeLayout {

    Context context;
    //因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）
    private GridView gridView;    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能
    private ArrayList<Map<String, String>> valueList;    //有人可能有疑问，为何这里不用数组了？
    private KeyBoardAdapter keyBoardAdapter;
    //因为要用Adapter中适配，用数组不能往adapter中填充
    public VirtualKeyboardView(Context context) {
        this(context, null);
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.layout_virtual_keyboard, null);
        valueList = new ArrayList<>();
        gridView = view.findViewById(R.id.gv_keybord);
        initValueList(Myapplication.NUMBER_KEYBOARD);
        setupView();
        addView(view);      //必须要，不然不显示控件
    }

    public ArrayList<Map<String, String>> getValueList() {
        return valueList;
    }

    private void initValueList(int z) {
        // 初始化按钮上应该显示的数字
        valueList.clear();
        Map<String, String> map;
        if (z == Myapplication.NUMBER_KEYBOARD) {
            gridView.setNumColumns(3);
            for (int i = 1; i < 13; i++) {
                map = new HashMap<>();
                if (i < 10) {
                    map.put("name", String.valueOf(i));
                } else if (i == 10) {
                    map.put("name", "确定");
                } else if (i == 11) {
                    map.put("name", String.valueOf(0));
                } else if (i == 12) {
                    map.put("name", "删除");
                }
                valueList.add(map);
            }
        } else if (z == Myapplication.LATTER_KEYBOARD) {
            //gridView.setNumColumns(5);
            for (int i = 1; i < 27; i++) {
                map = new HashMap<>();
                map.put("name", (char) (i + 96) + "");
          /*      if(i==27)map.put("name", "数字");
                if(i==28)map.put("name", "字母");
                if(i==29)map.put("name", "字符");*/
                valueList.add(map);
            }
        } else if (z == Myapplication.CHAR_KEYBOARD) {
            //gridView.setNumColumns(5);
            for (int i = 1; i < 16; i++) {
                map = new HashMap<>();
                map.put("name", (char) (i + 32) + "");
                valueList.add(map);
            }
            for (int i = 1; i < 8; i++) {
                map = new HashMap<>();
                map.put("name", (char) (i + 57) + "");
                valueList.add(map);
            }

            for (int i = 1; i < 7; i++) {
                map = new HashMap<>();
                map.put("name", (char) (i + 90) + "");
                valueList.add(map);
            }
            for (int i = 1; i < 5; i++) {
                map = new HashMap<>();
                map.put("name", (char) (i + 122) + "");
                valueList.add(map);
            }
        }
    }

    public GridView getGridView() {
        return gridView;
    }

    private void setupView() {
        keyBoardAdapter = new KeyBoardAdapter(context, valueList);
        gridView.setAdapter(keyBoardAdapter);
    }

    public void notifyadapter(int i) {
        initValueList(i);
        keyBoardAdapter.notifyDataSetChanged();
    }
}
