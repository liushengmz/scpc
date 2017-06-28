package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * 支付成功请求刷新等级
 *
 * @author zxc
 * @time 2016/9/28 0028下午 7:22
 */
public class RequestUpdate {

    @SerializedName("orderId")
    private String orderId;

    @SerializedName("method")
    private int method;

    @SerializedName("payStatus")
    private int payStatus;//1 success  2 fail

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public RequestUpdate(String orderId, int method, int payStatus) {
        this.orderId = orderId;
        this.method = method;
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return "RequestUpdate{" +
                "orderId='" + orderId + '\'' +
                ", method=" + method +
                ", payStatus=" + payStatus +
                '}';
    }
}
