package com.lulu.player.main.gold.presenter;

import com.lulu.player.main.gold.view.GoldView;
import com.lulu.player.model.Intro;
import com.lulu.player.model.RequestVideo;
import com.lulu.player.model.Video;
import com.lulu.player.mvp.BasePresenter;
import com.lulu.player.retrofit.HttpCallback;

import java.util.List;

/**
 * @author zxc
 * @time 2016/9/26 0026上午 11:15
 */
public class GoldPresenter extends BasePresenter<GoldView> {

    public GoldPresenter(GoldView view) {
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
