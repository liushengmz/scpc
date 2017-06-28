package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * 进入应用向服务器请求数据
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:48
 */
public class RequestUserInfo {

    @SerializedName("imsi")
    private String imsi;

    @SerializedName("mac")
    private String mac;

    @SerializedName("androidVersion")
    private String androidVersion;

    @SerializedName("androidLevel")
    private String androidLevel;

    @SerializedName("model")
    private String model;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @SerializedName("imei")

    private String imei;

    @SerializedName("appkey")
    private String appkey;

    @SerializedName("channel")
    private String channel;

    public RequestUserInfo() {
    }

    public RequestUserInfo(String imsi, String imei, String mac, String androidVersion, String androidLevel, String model, String appkey, String channel) {
        this.imsi = imsi;
        this.imei = imei;
        this.mac = mac;
        this.androidVersion = androidVersion;
        this.androidLevel = androidLevel;
        this.model = model;
        this.appkey = appkey;
        this.channel = channel;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getAndroidLevel() {
        return androidLevel;
    }

    public void setAndroidLevel(String androidLevel) {
        this.androidLevel = androidLevel;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
