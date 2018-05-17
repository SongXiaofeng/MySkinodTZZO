package com.skinod.tzzo.skinod.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.skinod.tzzo.skinod.constant.Contant;
import com.skinod.tzzo.skinod.utils.LogUtil;
import com.skinod.tzzo.skinod.wifi.wifiConnect.WifiConnectionCallback;
import com.skinod.tzzo.skinod.wifi.wifiConnect.WifiConnectionReceiver;
import com.skinod.tzzo.skinod.wifi.wifiScan.WifiScanCallback;
import com.skinod.tzzo.skinod.wifi.wifiScan.WifiScanReceiver;
import com.skinod.tzzo.skinod.wifi.wifiState.WifiStateCallback;
import com.skinod.tzzo.skinod.wifi.wifiState.WifiStateReceiver;
import com.thanosfisherman.elvis.Objects;
import java.util.ArrayList;
import java.util.List;



class WifiUtils {
    private final WifiManager mWifiManager;
    private final Context mContext;
    private final WifiStateReceiver mWifiStateReceiver;
    private final WifiConnectionReceiver mWifiConnectionReceiver;
    private final WifiScanReceiver mWifiScanReceiver;
    private static WeakHandler mMainHandler;
    private static List<ScanResult> scanResultList = new ArrayList<>();
    private final WifiStateCallback mWifiStateCallback = new WifiStateCallback() {
        @Override
        public void onWifiEnabled() {
            LogUtil.d("WIFI ENABLED...");
            unregisterReceiver(mContext, mWifiStateReceiver);
            LogUtil.d("START SCANNING....");
            if (mWifiManager.startScan())
                registerReceiver(mContext, mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            else {
                LogUtil.d("ERROR COULDN'T SCAN");
            }
        }
    };

    WifiUtils(Context context, WeakHandler hd) {
        mContext = context;
        mMainHandler = hd;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager == null)
            throw new RuntimeException("WifiManager is not supposed to be null");
        mWifiStateReceiver = new WifiStateReceiver(mWifiStateCallback);
        mWifiScanReceiver = new WifiScanReceiver(new WifiScanCallback() {
            @Override
            public void onScanResultsReady() {
                LogUtil.d("GOT SCAN RESULTS");
                unregisterReceiver(mContext, mWifiScanReceiver);
                scanResultList = mWifiManager.getScanResults();
                mMainHandler.sendEmptyMessage(Contant.MSG_WIFI_SCAN_SUCESS);
            }
        });
        mWifiConnectionReceiver = new WifiConnectionReceiver(new WifiConnectionCallback() {
            @Override
            public void successfulConnect() {
                LogUtil.d("CONNECTED SUCCESSFULLY");
                mMainHandler.sendEmptyMessage(Contant.MSG_WIIF_CONNECT_SUCCESS);
            }

            @Override
            public void errorConnect() {
                LogUtil.d("CONNECTED errorConnect");
                mMainHandler.sendEmptyMessage(Contant.MSG_WIIF_CONNECT_ERROR);
                //reenableAllHotspots(mWifiManager);
            }
        }, mWifiManager);

        IntentFilter wifif = new IntentFilter();
        wifif.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        wifif.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(mContext, mWifiConnectionReceiver, wifif);
    }


    void enableWifi() {
        if (mWifiManager.isWifiEnabled())
            mWifiStateCallback.onWifiEnabled();
        else {
            if (mWifiManager.setWifiEnabled(true))
                registerReceiver(mContext, mWifiStateReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
            else {
                LogUtil.d("enableWifi_errorConnect");
            }
        }
    }


    private void registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter filter) {
        if (receiver != null) {
            try {
                context.registerReceiver(receiver, filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        if (receiver != null) {
            try {
                context.unregisterReceiver(receiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    void connectWithSSID(@NonNull final String ssid, @NonNull final String password) {
        ScanResult mSingleScanResult = matchScanResultSsid(ssid, getWifiScanResult());
        if (mSingleScanResult != null) {
            ConnectorUtils.connectToWifi(mContext, mWifiManager, mSingleScanResult, password);
           /* if (ConnectorUtils.connectToWifi(mContext, mWifiManager, mSingleScanResult, password)) {
               // registerReceiver(mContext, mWifiConnectionReceiver.activateTimeoutHandler(mSingleScanResult), new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
            }*/
        }
    }

    private ScanResult matchScanResultSsid(@NonNull String ssid, @NonNull Iterable<ScanResult> results) {
        for (ScanResult result : results)
            if (Objects.equals(result.SSID, ssid))
                return result;
        return null;
    }

    String getConnectWifiSsid() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    List<ScanResult> getWifiScanResult() {
        return mWifiManager.getScanResults();
    }


 /*   static void reenableAllHotspots(@NonNull WifiManager wifi) {
        final List<WifiConfiguration> configurations = wifi.getConfiguredNetworks();
        if (configurations != null && !configurations.isEmpty())
            for (final WifiConfiguration config : configurations)
                wifi.enableNetwork(config.networkId, false);
    }



    public void cancelAutoConnect() {
        unregisterReceiver(mContext, mWifiStateReceiver);
        unregisterReceiver(mContext, mWifiScanReceiver);
        unregisterReceiver(mContext, mWifiConnectionReceiver);
        reenableAllHotspots(mWifiManager);
    }*/

    void disableWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
        LogUtil.d("WiFi Disabled");
    }

    int getWifiState() {
        return mWifiManager.getWifiState();
    }

   /* boolean setWifiEnabled(boolean isChecked) {
        return mWifiManager.setWifiEnabled(isChecked);
    }*/

    void unregisterReceiver() {
        unregisterReceiver(mContext, mWifiStateReceiver);
        unregisterReceiver(mContext, mWifiScanReceiver);
        unregisterReceiver(mContext, mWifiConnectionReceiver);
    }
}
