package com.lulu.player.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class VideoListRsp {

    private int count;
    private int levelId;
    private int status;
    private List<Video> videoList;
    private List<Video> topVideoList;
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

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public List<Video> getTopVideoList() {
        return topVideoList;
    }

    public void setTopVideoList(List<Video> topVideoList) {
        this.topVideoList = topVideoList;
    }

    @Override
    public String toString() {
        return "{" +
                "count=" + count +
                ", levelId=" + levelId +
                ", status=" + status +
                ", videoList=" + videoList +
                ", topVideoList=" + topVideoList +
                '}';
    }
}
