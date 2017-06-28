package com.lulu.player.main.free.view;

import com.lulu.player.model.Intro;
import com.lulu.player.model.Video;

import java.util.List;

/**
 * @author zxc
 * @time 2016/9/26 0026上午 11:36
 */
public interface FreeView {

    void requestVideoList(Intro<List<Video>> intro);

    void requestFail(String msg);

}
