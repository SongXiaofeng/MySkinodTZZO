package com.skinod.tzzo.skinod.constant;

/**
 * Created by Administrator on 2018/3/16.
 */

public class WifiBean {
    private String wifiName;
    private int level;
    private String capabilities;//加密方式
    private int connectate=1;//1：未连接 2：连接 3：正在连接

    public int getConnectate() {
        return connectate;
    }

    public void setConnectate(int connectate) {
        this.connectate = connectate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "WifiBean{" +
                "wifiName='" + wifiName + '\'' +
                ", level=" + level +
                ", capabilities='" + capabilities + '\'' +
                ", connectate=" + connectate +
                '}';
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }


}
