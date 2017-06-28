package com.lulu.player.pay.view;

import com.lulu.player.model.Order;
import com.lulu.player.model.Update;

/**
 * @author zxc
 * @time 2016/9/28 0028下午 5:49
 */
public interface PayView {

    void createOrder(Order order);

    void createFail(String msg);

    void updateLevel(Update update);

    void updateFail(String msg);

}
