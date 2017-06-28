package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class RequestVideo {

    @SerializedName("levelId")
    private int levelId;

    public RequestVideo() {
    }

    public RequestVideo(int levelId) {
        this.levelId = levelId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
}
