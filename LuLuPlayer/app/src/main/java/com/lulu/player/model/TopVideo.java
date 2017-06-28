package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * 广告栏
 *
 * @author zxc
 * @time 2016/9/23 0023下午 4:28
 */
public class TopVideo {

    @SerializedName("id")
    private int id;

    @SerializedName("imgs")
    private String imgUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String videoUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
