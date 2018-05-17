package com.skinod.tzzo.skinod.wifi.wifiConnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.skinod.tzzo.skinod.utils.LogUtil;


public final class WifiConnectionReceiver extends BroadcastReceiver {

    private final WifiConnectionCallback mWifiConnectionCallback;
    private final WifiManager mWifiManager;

    public WifiConnectionReceiver(@NonNull WifiConnectionCallback callback, @NonNull WifiManager wifiManager) {
        this.mWifiConnectionCallback = callback;
        this.mWifiManager = wifiManager;

    }


//NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
    @Override
    public void onReceive(Context context, @NonNull Intent intent) {
        final String action = intent.getAction();
        LogUtil.d("Connection Broadcast action: " + action);

        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            final NetworkInfo.DetailedState state = info.getDetailedState();
            if (state == NetworkInfo.DetailedState.CONNECTED ) {
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    mWifiConnectionCallback.successfulConnect();
                }
            }
        }

        if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
            final SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if (state == null) {
                mWifiConnectionCallback.errorConnect();
            }
            LogUtil.d("Connection Broadcast action: " + state);
                if(SupplicantState.FOUR_WAY_HANDSHAKE.equals(state)) {
                    LogUtil.d("FOUR WAY HANDSHAKE");
                } else if(SupplicantState.ASSOCIATED.equals(state)) {
                    LogUtil.d("ASSOCIATED");
                } else if(SupplicantState.ASSOCIATING.equals(state)) {
                    LogUtil.d("ASSOCIATING");
                } else if(SupplicantState.COMPLETED.equals(state)) {
                    LogUtil.d("COMPLETED");
                } else if(SupplicantState.DISCONNECTED.equals(state)) {
                    LogUtil.d("DISCONNECTED");
                   // mWifiConnectionCallback.errorConnect();
                } else if(SupplicantState.DORMANT.equals(state)) {
                    LogUtil.d("DORMANT");
                } else if(SupplicantState.GROUP_HANDSHAKE.equals(state)) {
                    LogUtil.d("GROUP HANDSHAKE");
                } else if(SupplicantState.INACTIVE.equals(state)) {
                    LogUtil.d("INACTIVE");
                } else if(SupplicantState.INVALID.equals(state)) {
                    LogUtil.d("INVALID");
                } else if(SupplicantState.SCANNING.equals(state)) {
                    LogUtil.d("SCANNING");
                } else if(SupplicantState.UNINITIALIZED.equals(state)) {
                    LogUtil.d("UNINITIALIZED");
                } else {
                    LogUtil.d("BAD");
                }
            }
    }
}

