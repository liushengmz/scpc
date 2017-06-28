package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author zxc
 * @time 2016/9/28 0028下午 6:02
 */
public class RequestOrder {

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("levelId")
    private int levelId;

    @SerializedName("payType")
    private int payType;

    @SerializedName("imei")
    private String imei;

    @SerializedName("method")
    private int method;

    @SerializedName("appkey")
    private String appkey;

    @SerializedName("channel")
    private String channel;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(short payType) {
        this.payType = payType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
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

    public RequestOrder(String orderId, int levelId, int payType, String imei, int method, String appkey, String channel) {
        this.orderId = orderId;
        this.levelId = levelId;
        this.payType = payType;
        this.imei = imei;
        this.method = method;
        this.appkey = appkey;
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "RequestOrder{" +
                "orderId='" + orderId + '\'' +
                ", levelId=" + levelId +
                ", payType=" + payType +
                ", imei='" + imei + '\'' +
                ", method=" + method +
                ", appkey='" + appkey + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
