package com.skinod.tzzo.skinod.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public   class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;

    private List<Fragment> mFragments;

    //保存每个Fragment的Tag，刷新页面的依据
    private SparseArray<String> tags = new SparseArray<>();
    private Fragment mCurrentFragment;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragmentManager = fm;
        mFragments=fragments;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //得到缓存的fragment
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        String tag = fragment.getTag();
        //保存每个Fragment的Tag
        tags.put(position, tag);
        return fragment;
    }

    //拿到指定位置的Fragment
    public Fragment getFragmentByPosition(int position) {
        return mFragmentManager.findFragmentByTag(tags.get(position));
    }

    public List<Fragment> getFragments(){
        return mFragments;
    }

    //刷新指定位置的Fragment
    public void notifyFragmentByPosition(int position) {
        tags.removeAt(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        Fragment fragment = (Fragment) object;
        //如果Item对应的Tag存在，则不进行刷新
        if (tags.indexOfValue(fragment.getTag()) > -1) {
            return super.getItemPosition(object);
        }
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
