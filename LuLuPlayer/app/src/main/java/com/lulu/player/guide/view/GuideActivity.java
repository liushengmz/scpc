package com.lulu.player.guide.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;

import com.lulu.player.R;
import com.lulu.player.common.Constants;
import com.lulu.player.guide.presenter.GuidePresenter;
import com.lulu.player.main.MainActivity;
import com.lulu.player.model.RequestUserInfo;
import com.lulu.player.model.UserInfo;
import com.lulu.player.mvp.MvpActivity;
import com.lulu.player.utils.ACache;
import com.lulu.player.utils.MetaDataUtils;
import com.lulu.player.utils.NetWorkUtils;
import com.lulu.player.utils.ToastUtils;


/**
 * welcome activity ,request user info
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:50
 */
public class GuideActivity extends MvpActivity<GuidePresenter> implements GuideView {

    private String IMSI, IMEI, mac, androidVersion, androidLevel, model, appKey, channel;

    private ACache cache;

    private ProgressDialog progressDialog;

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initDatas() {

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifiMng = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfor = wifiMng.getConnectionInfo();

        IMSI = "" + manager.getSubscriberId();
        mac = "" + wifiInfor.getMacAddress();
        androidVersion = "" + android.os.Build.VERSION.RELEASE;
        androidLevel = "" + android.os.Build.VERSION.SDK;
        model = "" + android.os.Build.MODEL;
        IMEI = "" + manager.getDeviceId();

        appKey = MetaDataUtils.getValue(this, "EP_APPKEY");
        channel = MetaDataUtils.getValue(this, "EP_CHANNEL");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        cache = ACache.get(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("请确认网络连接");
        if (NetWorkUtils.isNetworkAvailable(this)) {
            if (cache.getAsString(Constants.USER_NAME) == null) {
                cache.put(Constants.IMEI, IMEI);
                RequestUserInfo info = new RequestUserInfo(IMSI, IMEI, mac, androidVersion, androidLevel, model, appKey, channel);
                presenter.getInfo(info);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetWorkUtils.isNetworkAvailable(this)) {
            if (cache.getAsString(Constants.USER_NAME) != null) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            progressDialog.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected GuidePresenter createPresenter() {
        return new GuidePresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void requestUserInfo(UserInfo user) {
        cache.put(Constants.USER_NAME, user.getName(), Constants.CACHE_TIME);
        cache.put(Constants.PASSWORD, user.getPassword(), Constants.CACHE_TIME);
        cache.put(Constants.LEVEL, user.getLevel() + "", Constants.CACHE_TIME);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    public void requestFail(String msg) {
        ToastUtils.showShortMessage(this, msg);
    }

    @Override
    public void showProgress() {
        ToastUtils.showShortMessage(this, "加载中...");
    }

    @Override
    public void hideProgress() {
        ToastUtils.showShortMessage(this, "加载完成");
    }

    @Override
    public void onBackPressed() {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        } else {
            super.onBackPressed();
        }

    }
}
