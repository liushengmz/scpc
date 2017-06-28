package com.lulu.player.mvp;

import android.os.Bundle;
import android.view.View;

import com.lulu.player.base.BaseFragment;

/**
 * presenter bind fragment life
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:44
 */
public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {

    protected P presenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
