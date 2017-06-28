package com.lulu.player.main.my.view;

import com.lulu.player.model.Levels;
import com.lulu.player.model.UserInfo;

import java.util.List;

/**
 * @author Administrator
 * @time 2016/9/26 0026上午 11:16
 */
public interface MyView {

    void requestUserInfo(UserInfo<List<Levels>> info);

    void requestFail(String msg);

}
