package com.lulu.player.guide.view;

import com.lulu.player.model.UserInfo;

/**
 * 处理业务需要的方法
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:49
 */
public interface GuideView {

    void requestUserInfo(UserInfo user);

    void requestFail(String msg);

    void showProgress();

    void hideProgress();
}
