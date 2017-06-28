package com.lulu.player.model;

import com.google.gson.annotations.SerializedName;

/**
 * 业务具体处理，包括负责存储、检索、操纵数据等
 * 用户信息
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:57
 */
public class UserInfo<T> {

    @SerializedName("status")
    private int status;

    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;

    @SerializedName("level")
    private int level;

    @SerializedName("levels")
    private T levels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getLevels() {
        return levels;
    }

    public void setLevels(T levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", level=" + level +
                ", levels=" + levels +
                '}';
    }

    public UserInfo() {

    }

    public UserInfo(int status, String name, String password, int level, T levels) {
        this.status = status;
        this.name = name;
        this.password = password;
        this.level = level;
        this.levels = levels;
    }
}
