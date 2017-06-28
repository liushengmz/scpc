package com.lulu.player.pay.presenter;

import com.lulu.player.model.Order;
import com.lulu.player.model.RequestOrder;
import com.lulu.player.model.RequestUpdate;
import com.lulu.player.model.Update;
import com.lulu.player.mvp.BasePresenter;
import com.lulu.player.pay.view.PayView;
import com.lulu.player.retrofit.HttpCallback;

/**
 * @author zxc
 * @time 2016/9/28 0028下午 6:22
 */
public class PayPresenter extends BasePresenter<PayView> {

    public PayPresenter(PayView view) {
        attachView(view);
    }

    public void createOrder(RequestOrder order) {
        addSubscription(apiService.createOrder(order), new HttpCallback<Order>() {

            @Override
            public void onSuccess(Order order) {
                mView.createOrder(order);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.createFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });

    }

    public void updateLevel(RequestUpdate update) {
        addSubscription(apiService.updateLevel(update), new HttpCallback<Update>() {

            @Override
            public void onSuccess(Update up) {
                mView.updateLevel(up);
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.updateFail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }


}
