package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * 订单信息
 *
 * @author zxc
 * @time 2016/9/28 0028下午 5:50
 */
public class Order {

    @SerializedName("createDate")
    private long createDate;

    @SerializedName("levelId")
    private int levelId;

    @SerializedName("levelName")
    private String levelName;

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("payDate")
    private long payDate;

    @SerializedName("price")
    private int price;

    @SerializedName("sdkId")
    private int sdkId;

    @SerializedName("status")
    private int status;

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getPayDate() {
        return payDate;
    }

    public void setPayDate(long payDate) {
        this.payDate = payDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSdkId() {
        return sdkId;
    }

    public void setSdkId(int sdkId) {
        this.sdkId = sdkId;
    }

    @Override
    public String toString() {
        return "{" +
                "createDate=" + createDate +
                ", levelId=" + levelId +
                ", levelName='" + levelName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", payDate=" + payDate +
                ", price=" + price +
                ", sdkId=" + sdkId +
                ", status=" + status +
                '}';
    }
}
