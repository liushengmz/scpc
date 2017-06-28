package com.lulu.player.main.diamond.presenter;

import com.lulu.player.main.diamond.view.DiamondView;
import com.lulu.player.model.Intro;
import com.lulu.player.model.RequestVideo;
import com.lulu.player.model.Video;
import com.lulu.player.mvp.BasePresenter;
import com.lulu.player.retrofit.HttpCallback;

import java.util.List;

/**
 * view和model之间的桥梁，从model层检索数据之后返回给view层
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:53
 */
public class DiamondPresenter extends BasePresenter<DiamondView> {

    public DiamondPresenter(DiamondView view) {
        attachView(view);
    }

    public void getVideo(RequestVideo requestVideo) {
        addSubscription(apiService.getVideo(requestVideo), new HttpCallback<Intro<List<Video>>>() {
            @Override
            public void onSuccess(Intro<List<Video>> intro) {
                mView.requestVideoList(intro);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.requestFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });

    }
}
