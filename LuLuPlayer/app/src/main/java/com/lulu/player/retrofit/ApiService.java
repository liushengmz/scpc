package com.lulu.player.retrofit;


import com.lulu.player.common.HttpUrl;
import com.lulu.player.model.Intro;
import com.lulu.player.model.Levels;
import com.lulu.player.model.Order;
import com.lulu.player.model.RequestOrder;
import com.lulu.player.model.RequestUpdate;
import com.lulu.player.model.RequestVideo;
import com.lulu.player.model.UserInfo;
import com.lulu.player.model.RequestUserInfo;
import com.lulu.player.model.Video;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author zxc
 * @time 2016/9/22 0022 下午 5:40
 */
public interface ApiService {

    //request user info
    @POST(HttpUrl.GET_USER_INFO)
    Observable<UserInfo> getInfo(@Body RequestUserInfo info);

    @POST(HttpUrl.GET_USER_INFO)
    Observable<UserInfo<List<Levels>>> getUser(@Body RequestUserInfo info);

    @POST(HttpUrl.GET_VIDEO_LIST)
    Observable<Intro<List<Video>>> getVideo(@Body RequestVideo video);

    //创建订单
    @POST(HttpUrl.POST_ORDER_INFO)
    Observable<Order> createOrder(@Body RequestOrder order);

    //支付成功请求刷新等级
    @POST(HttpUrl.POST_ORDER_INFO)
    Observable<Order> updateLevel(@Body RequestUpdate update);

}
