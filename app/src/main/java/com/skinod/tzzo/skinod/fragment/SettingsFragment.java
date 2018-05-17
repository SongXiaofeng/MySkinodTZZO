package com.skinod.tzzo.skinod.fragment;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.skinod.tzzo.R;
import com.skinod.tzzo.skinod.datasave.CdufSplite;
import com.skinod.tzzo.skinod.setting.SeekBarPreference;
import com.skinod.tzzo.skinod.setting.VolumeChangeListener;
import com.skinod.tzzo.skinod.utils.AudioUtil;
import com.skinod.tzzo.skinod.utils.LogUtil;
import com.skinod.tzzo.skinod.utils.MediaPlayer;
import com.skinod.tzzo.skinod.widget.CircleProgress;
import com.skinod.tzzo.skinod.widget.CircleProgressListener;

import java.io.File;

import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, VolumeChangeListener, Preference.OnPreferenceClickListener,CircleProgressListener {

    private static final String KEY_SCREEN_TIMEOUT = "screen_timeout";
    private ListPreference mScreenTimeoutPreference;
    private static final int FALLBACK_SCREEN_TIMEOUT_VALUE = 30000;
    private static final String KEY_MEDIA_VOLUME = "media_volume";
    private static final String KEY_DEVICE_MODEL = "device_model";
    private static final String KEY_BUILD_NUMBER = "build_number";
    private static final String KEY_WIFI_CONNECT = "wifi";
    private static final String KEY_WIFI_SPOT = "wifi_spot";
    private static final String KEY_WIRELESS_UPDATE = "wireless_update";
    private static final String KEY_DELETE_DATA = "delete_data";
   // private static final String KEY_DISPLAY_SETTINGS = "dedisplay_settings";

    private SeekBarPreference volumePref;
    private int value;
    private WifiManager wifienable;
    private PreferenceScreen wifiPref;
    private PopupWindow mPopupWindow;
    private View mPopView;
    private int w_screen;
    private int h_screen;
    private CircleProgress circleview;
    private ViewGroup vewGroup;
    //private PreferenceScreen displaysettingspre;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.display_settings);
        initPopWindow();
        DisplayMetrics dm =getResources().getDisplayMetrics();
        w_screen = dm.widthPixels;
        h_screen = dm.heightPixels;
        mScreenTimeoutPreference = (ListPreference) findPreference(KEY_SCREEN_TIMEOUT);

        PreferenceScreen wifispotpre = (PreferenceScreen) findPreference(KEY_WIFI_SPOT);
        PreferenceScreen wirelessupdatepre = (PreferenceScreen) findPreference(KEY_WIRELESS_UPDATE);
        PreferenceScreen deletedatapre = (PreferenceScreen) findPreference(KEY_DELETE_DATA);
        //displaysettingspre= (PreferenceScreen) findPreference(KEY_DISPLAY_SETTINGS);
        wifispotpre.setOnPreferenceClickListener(this);
        wirelessupdatepre.setOnPreferenceClickListener(this);
        deletedatapre.setOnPreferenceClickListener(this);
        wifispotpre.setSummary(getActivity().getString(R.string.noset));
        volumePref = (SeekBarPreference) findPreference(KEY_MEDIA_VOLUME);
        wifienable = (WifiManager) this.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        volumePref.setOnPreferenceChangeListener(this);
        volumePref.setCallback(this);
        mScreenTimeoutPreference.setOnPreferenceChangeListener(this);
        wifiPref = (PreferenceScreen) findPreference(KEY_WIFI_CONNECT);
    }

    private void initPopWindow() {
        mPopView = getActivity().getLayoutInflater().inflate(R.layout.data_delete_popwindow_layout,vewGroup);
        circleview=mPopView.findViewById(R.id.circle);
        circleview.setfinishListener(this);
        circleview.setClickable(false);
        mPopupWindow = new PopupWindow(mPopView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setTouchable(false);
        mPopupWindow.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();

        final long currentTimeout = Settings.System.getLong(getActivity().getContentResolver(),
                SCREEN_OFF_TIMEOUT, FALLBACK_SCREEN_TIMEOUT_VALUE);
        mScreenTimeoutPreference.setValue(String.valueOf(currentTimeout));
        mScreenTimeoutPreference.setOnPreferenceChangeListener(this);
        updateTimeoutPreferenceDescription(currentTimeout);


        int defaultVolume = AudioUtil.getInstance(this.getActivity().getApplicationContext()).getMediaVolume();
        volumePref.setProgress(defaultVolume);
        volumePref.setSummary(defaultVolume + "");

        setStringSummary(KEY_BUILD_NUMBER, Build.DISPLAY);
        setStringSummary(KEY_DEVICE_MODEL, Build.MODEL);
    }


    private void setStringSummary(String preference, String value) {
        try {
            findPreference(preference).setSummary(value);
        } catch (RuntimeException e) {
            findPreference(preference).setSummary(getResources().getString(R.string.device_info_default));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.vewGroup=container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        value = Integer.parseInt(objValue + "");
        if (KEY_SCREEN_TIMEOUT.equals(key)) {
            try {
                Settings.System.putInt(getActivity().getContentResolver(), SCREEN_OFF_TIMEOUT, value);
                updateTimeoutPreferenceDescription(value);
            } catch (NumberFormatException e) {
                LogUtil.e("could not persist screen timeout setting");
            }
        }
        if (KEY_MEDIA_VOLUME.equals(key)) {
            try {
                MediaPlayer.getConstant(this.getActivity().getApplication(), getMediaVolumeUri()).startByUri();
            } catch (NumberFormatException e) {
                LogUtil.e("could not persist volume change setting");
            }
        }

        if (KEY_WIFI_SPOT.equals(key)) {
            if (null != wifienable) wifienable.setWifiEnabled((Boolean) objValue);
        }


        return false;
    }

    private void updateTimeoutPreferenceDescription(long currentTimeout) {
        ListPreference preference = mScreenTimeoutPreference;
        String summary;
        // if (preference.isDisabledByAdmin()) {
        //   summary = getString(R.string.disabled_by_policy_title);
        // } else
        if (currentTimeout < 0) {
            // Unsupported value
            summary = "";
        } else {
            final CharSequence[] entries = preference.getEntries();
            final CharSequence[] values = preference.getEntryValues();
            if (entries == null || entries.length == 0) {
                summary = "";
            } else {
                int best = 0;
                for (int i = 0; i < values.length; i++) {
                    long timeout = Long.parseLong(values[i].toString());
                    if (currentTimeout >= timeout) {
                        best = i;
                    }
                }
                summary = getString(R.string.screen_timeout_summary, entries[best]);
            }
        }
        preference.setSummary(summary);
    }


    private Uri getMediaVolumeUri() {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + getActivity().getApplicationContext().getPackageName()
                + "/" + R.raw.media_volume);
    }

    @Override
    public void setDialogInterfaceButtonStatus(int status) {
        switch (status) {
            case Dialog.BUTTON_POSITIVE:
                volumePref.setSummary(value + "");
                AudioUtil.getInstance(this.getActivity()).setMediaVolume(value);
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
            case Dialog.BUTTON_NEUTRAL:
                //  volumeChangeListener.setDialogInterfaceButtonStatus(Dialog.BUTTON_NEUTRAL);
                break;
        }
    }

    public void updatwifistatus() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info.isConnected()) wifiPref.setSummary(R.string.Conntected);
            else wifiPref.setSummary(R.string.UnConntected);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        final String key = preference.getKey();
        LogUtil.d("onPreferenceClick_key=" + key);
        if (KEY_WIFI_SPOT.equals(key)) {
            startActivityByName("com.android.settings", "com.android.settings.wifi.hotspot.TetherWifiSettings");
        }else  if (KEY_DELETE_DATA.equals(key)) {
            mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            circleview.start();
            try {
                new Thread(() -> {
                    new CdufSplite(ConverFragment.dbHelper).deleteAll();
                    File sampleDir = Environment.getExternalStorageDirectory();
                    String music_path = sampleDir.getAbsolutePath() + "/record/";
                    deleteAllFiles(music_path);
                }).start();
                circleview.finish(2);
            }catch (Exception e){
                circleview.finish(1);
            }
        }
        return false;
    }


    private void startActivityByName(String pn, String cn) {
        Intent intent = new Intent();
        ComponentName con = new ComponentName(pn, cn);
        intent.setComponent(con);
        startActivity(intent);
    }

    private void deleteAllFiles(String music_path) {
        File[] files = new File(music_path).listFiles();
        if (files.length > 0)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f.getAbsolutePath());
                    try {
                        if (f.delete()) LogUtil.d("删除成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        // deleteAllFiles(f.getAbsolutePath());
                        try {
                            if (f.delete()) LogUtil.d("删除成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    }

    @Override
    public void circlefinish() {
        new Handler().postDelayed(()->mPopupWindow.dismiss(),1000);
    }
}
