package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * 会员等级特权
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:48
 */
public class Levels {

    @SerializedName("levelId")
    private int levelId;

    @SerializedName("price")
    private int price;

    @SerializedName("remark")
    private String remark;

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
