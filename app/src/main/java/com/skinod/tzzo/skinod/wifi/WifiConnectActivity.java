package com.skinod.tzzo.skinod.wifi;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.skinod.tzzo.R;
import com.skinod.tzzo.skinod.constant.Contant;
import com.skinod.tzzo.skinod.constant.WifiBean;
import com.skinod.tzzo.skinod.keyboard.NormalKeyBoardActivity;
import com.skinod.tzzo.skinod.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class WifiConnectActivity extends AppCompatActivity implements Handler.Callback, CompoundButton.OnCheckedChangeListener {
    private String currentSSID = "Skinod_1";
    private String currentSSIDPass = "86727876";
    private List<ScanResult> scanResult = new ArrayList<>();
    private WeakHandler mHandler = new WeakHandler(this);
    private WifiUtils myWifiUtil;
    private ProgressBar progressbar;
    private List<WifiBean> wifiList = new ArrayList<>();
    private MyWifiListviewAdapter mAdapter;
    private Switch mSwitchBar;
    private ListView wifilv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect_test);

        wifilv = findViewById(R.id.wifi_lv);
        progressbar = findViewById(R.id.progressbar);

        wifilv.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> listemIitemClick(position));

        mSwitchBar = findViewById(R.id.sh);
        mSwitchBar.setOnCheckedChangeListener(this);

        myWifiUtil = new WifiUtils(this, mHandler);

        handleWifiStateChanged(myWifiUtil.getWifiState());
        //  myWifiUtil.enableWifi();
        mAdapter = new MyWifiListviewAdapter(WifiConnectActivity.this.getApplicationContext(), wifiList);
        wifilv.setAdapter(mAdapter);

        mSwitchBar.setChecked(true);
    }

    private void handleWifiStateChanged(int wifiState) {
        LogUtil.d("handleWifiStateChanged_wifiState=" + wifiState);
        switch (wifiState) {
            case WifiManager.WIFI_STATE_ENABLING:
                mSwitchBar.setEnabled(false);
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                mSwitchBar.setChecked(true);
                mSwitchBar.setEnabled(true);
                break;
            case WifiManager.WIFI_STATE_DISABLING:
                mSwitchBar.setEnabled(false);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                mSwitchBar.setChecked(false);
                mSwitchBar.setEnabled(true);
                break;
            default:
                mSwitchBar.setChecked(false);
                mSwitchBar.setEnabled(true);
        }
    }


    private void listemIitemClick(int position) {
        currentSSID = mAdapter.getItem(position).getWifiName();
        startActivityForResult(new Intent(WifiConnectActivity.this, NormalKeyBoardActivity.class), 200);
    }


    private void connectWithWpa() {
        LogUtil.d("connectWithWpa_ote=" + currentSSID + ";otePass=" + currentSSIDPass);
        if (!currentSSID.isEmpty())
            myWifiUtil.connectWithSSID(currentSSID, currentSSIDPass);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            currentSSIDPass = data.getStringExtra("wifipassword");
            if(!currentSSIDPass.isEmpty())
            connectWithWpa();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        LogUtil.d("Wificonnectactivity_handleMessage.msg.what=" + msg.what);
        WifiBean wifibean;
        switch (msg.what) {
            case Contant.MSG_WIFI_SCAN_SUCESS:
                wifiList.clear();
                scanResult = myWifiUtil.getWifiScanResult();
                currentSSID = myWifiUtil.getConnectWifiSsid();
                LogUtil.d("handleMessage.currentSSID=" + currentSSID);
                for (ScanResult result : scanResult) {
                    // Ignore hidden and ad-hoc networks.
                    if (!(result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]"))) {
                        if(result.SSID.contains("NVRAM WARNING")) continue;
                        wifibean = new WifiBean();
                        wifibean.setWifiName(result.SSID);
                        wifibean.setConnectate(1);
                        wifibean.setLevel(result.level);
                        wifibean.setCapabilities(result.capabilities);
                        if (currentSSID.contains(wifibean.getWifiName())) wifibean.setConnectate(2);
                        wifiList.add(wifibean);
                    }
                }
                mAdapter.notifyDataSetChanged();
                wifilv.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                break;
            case Contant.MSG_WIIF_CONNECT_SUCCESS:
                mHandler.sendEmptyMessageDelayed(Contant.MSG_WIFI_SCAN_SUCESS, 500);
                break;
            case Contant.MSG_WIIF_CONNECT_ERROR:
                Toast.makeText(this,"连接失败",Toast.LENGTH_LONG).show();
                progressbar.setVisibility(View.GONE);
                break;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        wifilv.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        if (isChecked) {
            myWifiUtil.enableWifi();
            progressbar.setVisibility(View.VISIBLE);
            wifilv.setVisibility(View.GONE);
        } else {
            myWifiUtil.disableWifi();
            progressbar.setVisibility(View.GONE);
            wifilv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("wifiConnectActivity_onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("wifiConnectActivity_onDestroy");
        myWifiUtil.unregisterReceiver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.d("wifiConnectActivity_onBackPressed");
        finish();
    }
}
