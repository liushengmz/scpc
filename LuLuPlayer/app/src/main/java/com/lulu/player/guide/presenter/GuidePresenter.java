package com.lulu.player.guide.presenter;

import com.lulu.player.guide.view.GuideView;
import com.lulu.player.model.RequestUserInfo;
import com.lulu.player.model.UserInfo;
import com.lulu.player.mvp.BasePresenter;
import com.lulu.player.retrofit.HttpCallback;

/**
 * view和model之间的桥梁，从model层检索数据之后返回给view层
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:53
 */
public class GuidePresenter extends BasePresenter<GuideView> {

    public GuidePresenter(GuideView view) {
        attachView(view);
    }

    public void getInfo(RequestUserInfo info) {
        mView.showProgress();
        addSubscription(apiService.getInfo(info), new HttpCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                mView.requestUserInfo(userInfo);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.requestFail(msg);
            }

            @Override
            public void onFinish() {
                mView.hideProgress();
            }
        });

    }

}
