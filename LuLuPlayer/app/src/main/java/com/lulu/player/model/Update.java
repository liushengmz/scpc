package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * 请求结果
 *
 * @author zxc
 * @time 2016/9/28 0028下午 7:24
 */
public class Update {

    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
