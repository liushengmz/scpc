package com.lulu.player.mvp;


import com.lulu.player.retrofit.ApiService;
import com.lulu.player.retrofit.HttpClient;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author zxc
 * @time 2016/9/22 0022 下午 5:47
 */
public class BasePresenter<V> {

    public V mView;

    protected ApiService apiService;

    private CompositeSubscription mCompositeSubscription;

    public void attachView(V view) {
        this.mView = view;
        apiService = HttpClient.retrofit().create(ApiService.class);
    }

    public void detachView() {
        this.mView = null;
        onUnsubscribe();
    }

    //RXjava unbind avoid oom
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

}
