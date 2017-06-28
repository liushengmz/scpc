package com.lulu.player.mvp;

import android.os.Bundle;

import com.lulu.player.base.BaseActivity;

/**
 * presenter bind activity life
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:45
 */
public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {

    protected P presenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
