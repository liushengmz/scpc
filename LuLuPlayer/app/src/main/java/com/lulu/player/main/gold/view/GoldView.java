package com.lulu.player.main.gold.view;

import com.lulu.player.model.Intro;
import com.lulu.player.model.Video;

import java.util.List;

/**
 * @author zxc
 * @time 2016/9/26 0026上午 11:15
 */
public interface GoldView {

    void requestVideoList(Intro<List<Video>> intro);

    void requestFail(String msg);

}
