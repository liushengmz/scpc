package com.lulu.player.main.diamond.view;

import com.lulu.player.model.Intro;
import com.lulu.player.model.Video;

import java.util.List;

/**
 * @author zxc
 * @time 2016/9/26 0026上午 11:16
 */
public interface DiamondView {

    void requestVideoList(Intro<List<Video>> intro);

    void requestFail(String msg);

}
