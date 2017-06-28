package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author zxc
 * @time 2016/9/22 0022 下午 5:49
 */
public class Intro<T>{

    @SerializedName("count")
    private int count;

    @SerializedName("levelId")
    private int levelId;

    @SerializedName("status")
    private int status;

    @SerializedName("topVideos")
    private T topVideos;

    @SerializedName("videos")
    private T videos;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getTopVideos() {
        return topVideos;
    }

    public void setTopVideos(T topVideos) {
        this.topVideos = topVideos;
    }

    public T getVideos() {
        return videos;
    }

    public void setVideos(T videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "{" +
                "count=" + count +
                ", levelId=" + levelId +
                ", status=" + status +
                ", topVideos=" + topVideos +
                ", videos=" + videos +
                '}';
    }
}
