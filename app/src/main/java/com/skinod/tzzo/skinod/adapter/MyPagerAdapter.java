package com.skinod.tzzo.skinod.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;



public class MyPagerAdapter extends PagerAdapter {
    private List<View> pageList;
    public MyPagerAdapter(List<View> pageList){
        this.pageList=pageList;
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // return false;
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //  super.destroyItem(container, position, object);
        container.removeView(pageList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // return super.instantiateItem(container, position);
        container.addView(pageList.get(position));
        Log.d("tag",String.valueOf(position));
        return pageList.get(position);

    }
}
