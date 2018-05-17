package com.skinod.tzzo.skinod.activity;

import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.skinod.tzzo.R;
import com.skinod.tzzo.baiduasrtts.recognization.online.InFileStream;
import com.skinod.tzzo.skinod.application.Myapplication;
import com.skinod.tzzo.skinod.fragment.BaseFragment;
import com.skinod.tzzo.skinod.fragment.BaseFragmentPagerAdapter;
import com.skinod.tzzo.skinod.fragment.ConverFragment;
import com.skinod.tzzo.skinod.fragment.RecorFragment;
import com.skinod.tzzo.skinod.fragment.SettingsFragment;
import com.skinod.tzzo.skinod.service.BaiduBinderService;
import com.skinod.tzzo.skinod.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class TranslatorActivity extends FragmentActivity implements BaseFragment.OnFragmentInteractionListener {

    private BaseFragmentPagerAdapter adapter;
    protected Fragment cf;
    private BaiduBinderService baiduService;//protected View root_ll;
    // private PopupWindowFactory mPop;
    protected boolean shortPress;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        radioGroup = findViewById(R.id.navigation_btn);
        initFragment();
        InFileStream.setContext(this);
        Intent _intent = new Intent(TranslatorActivity.this, BaiduBinderService.class);
        bindService(_intent, conn, Service.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BaiduBinderService.MyBinder binder = (BaiduBinderService.MyBinder) service;
            baiduService = binder.getService();
            LogUtil.d("onServiceConnected:" + baiduService.getRandomNumber());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initFragment() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ConverFragment());
        fragments.add(new RecorFragment());
        fragments.add(new SettingsFragment());
        adapter = new BaseFragmentPagerAdapter(getFragmentManager(), fragments);
        ViewPager vp = findViewById(R.id.myViewPager);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtil.d("PageChange_onPageScrolled_position=" + position);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.d("PageChange_onPageSelected_position=" + position);
                if (position == 0) {
                    ConverFragment currentFragment = (ConverFragment) fragments.get(position);
                    currentFragment.resetFocused();
                    radioGroup.check(R.id.btn1);
                }
                if (position == 1) {
                    RecorFragment currentFragment = (RecorFragment) fragments.get(position);
                    currentFragment.updateList();
                    radioGroup.check(R.id.btn2);
                }
                if (position == 2) {
                    radioGroup.check(R.id.btn3);
                    SettingsFragment currentFragment = (SettingsFragment) fragments.get(position);
                    currentFragment.updatwifistatus();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtil.d("PageChange_onPageScrollStateChanged_state=" + state);
            }
        });
        vp.setAdapter(adapter);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
        }
        return super.dispatchKeyShortcutEvent(event);
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        LogUtil.d("sxf", "onKeyLongPress ---> event.getKeyCode()=" + event.getKeyCode());
        if (adapter.getCurrentFragment() instanceof ConverFragment) {
            if (keyCode == KeyEvent.KEYCODE_F3) {
                shortPress = false;
                Myapplication.AUDIO_BUTTION_FLAG = 100;
                findViewById(R.id.iv_bottom).setVisibility(View.VISIBLE);
                baiduService.startasr();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_F4) {
                shortPress = false;
                Myapplication.AUDIO_BUTTION_FLAG = 200;
                findViewById(R.id.iv_bottom).setVisibility(View.VISIBLE);
                baiduService.startasr();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //  LogUtil.d("sxf", "onKeyDown ---> event.getKeyCode()=" + event.getKeyCode());

        if (keyCode == KeyEvent.KEYCODE_F3 || keyCode == KeyEvent.KEYCODE_F4) {
            if (adapter.getCurrentFragment() instanceof ConverFragment) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    event.startTracking();
                    if (event.getRepeatCount() == 0) {
                        shortPress = true;
                    }
                    return true;
                }
            }
        }

        if (keyCode == KeyEvent.KEYCODE_F1) {
            if (adapter.getCurrentFragment() instanceof RecorFragment) {
                RecorFragment currentFragment = (RecorFragment) adapter.getCurrentFragment();
                currentFragment.actionbutton();
            }
        }
        return super.onKeyDown(keyCode, event);

    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // LogUtil.d("sxf", "onKeyUp ---> event.getKeyCode()=" + event.getKeyCode());
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_F3:
            case KeyEvent.KEYCODE_F4:
                //   if (shortPress) {
                findViewById(R.id.iv_bottom).setVisibility(View.GONE);
                // Toast.makeText(this, "shortPress", Toast.LENGTH_LONG).show();
                // }
                shortPress = false;
                return true;
            case KeyEvent.KEYCODE_F1:

                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        LogUtil.d("uri=" + uri.toString());
        if (uri.toString().contains("ConverFragment")) {
            cf = adapter.getItem(0);
        }
        if (uri.toString().contains("ConverFragment")) {
            cf = adapter.getItem(1);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
