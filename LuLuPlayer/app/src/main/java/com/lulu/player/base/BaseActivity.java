package com.lulu.player.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.lulu.player.R;
import com.lulu.player.common.Constants;
import com.lulu.player.retrofit.ApiService;
import com.lulu.player.retrofit.HttpClient;
import com.lulu.player.utils.StringUtils;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends FragmentActivity {

    protected FragmentManager fm;

    protected FragmentTransaction fragmentTransaction;

    protected Fragment currentFragment;

    protected TextView titleText;

    protected String title;

    protected Intent intent;

    public ApiService apiStores = HttpClient.retrofit().create(ApiService.class);

    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(getFragmentLayout());
//        setUpComponent();
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
    }

    public void onUnsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();//取消注册，以避免内存泄露
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

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    protected abstract int getFragmentLayout();

    protected abstract void initViews();

    protected abstract void initListeners();

    protected abstract void initAdapters();

    protected abstract void initDatas();

    protected void initReferFragment() {
        fm = getSupportFragmentManager();
    }

    protected void initIntent() {
        intent = getIntent();
    }

    protected void initTitle() {
        title = intent.getStringExtra(Constants.TITLE);
    }

    protected void initActionTitle() {
        titleText = (TextView) findViewById(R.id.title_text);
        titleText.setText(title);
    }

    protected void init() {
        initReferFragment();
        initIntent();
        initDatas();
//        initTitle();
//        initActionTitle();
        initViews();
        initAdapters();
        initListeners();
    }

//    public void setUpComponent() {
//
//    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        int count = fm.getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else if (count == 1) {
            fm.popBackStackImmediate();
        } else {
            setCurrentFragment();
            titleText.setText(currentFragment.getArguments().getString(Constants.TITLE));
        }

    }

    private void setCurrentFragment() {
        // TODO Auto-generated method stub
        for (int i = 0; i < fm.getFragments().size(); i++) {
            currentFragment = fm.getFragments().get(i);
            if (!currentFragment.isHidden()) {
                break;
            }
        }
    }

    public void switchFragment(Fragment fragment, String tag, boolean isAddToBackStack) {
        Fragment targetFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fm.getFragments() != null) {
            setCurrentFragment();
        }

        fragmentTransaction = fm.beginTransaction();

        if (targetFragment == null || targetFragment.isRemoving()) {
            // fragmentTransaction.setCustomAnimations(arg0, arg1, arg2, arg3);
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment);
            }
            fragmentTransaction.add(R.id.content, fragment, tag);
        } else {
            if (targetFragment.isAdded()) {
                fragmentTransaction.hide(currentFragment).show(targetFragment);
            }
        }

        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }

        fragmentTransaction.commit();
        String title = null;
        if (targetFragment != null) {
            title = targetFragment.getArguments().getString(Constants.TITLE);
        } else {
            title = fragment.getArguments().getString(Constants.TITLE);
        }

        if (!StringUtils.isNullOrEmpty(title)) {
            titleText.setText(title == null || "".equals(title) ? "default" : title);
        }

    }

    public void addFragment(Fragment fragment, String tag, boolean isAddToBackStack) {
        fragmentTransaction = fm.beginTransaction();
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.add(R.id.content, fragment, tag);
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, String tag) {
        fm.beginTransaction().replace(R.id.content, fragment, tag).commit();
    }

    public void replaceFragment(Fragment fragment) {
        fm.beginTransaction().replace(R.id.content, fragment).commit();
    }

    public void removeFragment(Fragment fragment) {
        fm.beginTransaction().remove(fragment).commit();
    }

    public void hideFragment(Fragment fragment) {
        fm.beginTransaction().hide(fragment).commit();
    }

    public void showFragment(Fragment fragment) {
        fm.beginTransaction().show(fragment).commit();

    }

}
