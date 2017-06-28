package com.lulu.player.main.my.presenter;

import com.lulu.player.main.my.view.MyView;
import com.lulu.player.model.Levels;
import com.lulu.player.model.RequestUserInfo;
import com.lulu.player.model.UserInfo;
import com.lulu.player.mvp.BasePresenter;
import com.lulu.player.retrofit.HttpCallback;

import java.util.List;

/**
 * @author zxc
 * @time 2016/9/26 0026上午 11:16
 */
public class MyPresenter extends BasePresenter<MyView> {

    public MyPresenter(MyView view) {
        attachView(view);
    }

    public void getLevels(RequestUserInfo info) {
        addSubscription(apiService.getUser(info), new HttpCallback<UserInfo<List<Levels>>>() {
            @Override
            public void onSuccess(UserInfo<List<Levels>> levels) {
                mView.requestUserInfo(levels);
            }

            @Override
            public void onFailure(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
