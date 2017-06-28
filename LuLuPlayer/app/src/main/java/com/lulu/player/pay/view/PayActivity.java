package com.lulu.player.pay.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ep.sdk.XTSDK;
import com.epplus.view.PayParams;
import com.lulu.player.R;
import com.lulu.player.common.Constants;
import com.lulu.player.model.Order;
import com.lulu.player.model.RequestOrder;
import com.lulu.player.model.RequestUpdate;
import com.lulu.player.model.Update;
import com.lulu.player.mvp.MvpActivity;
import com.lulu.player.pay.presenter.PayPresenter;
import com.lulu.player.utils.ACache;
import com.lulu.player.utils.MetaDataUtils;
import com.lulu.player.utils.ToastUtils;

import butterknife.Bind;

/**
 * 支付
 *
 * @author zxc
 * @time 2016/9/28 0028下午 5:31
 */
public class PayActivity extends MvpActivity<PayPresenter> implements PayView, View.OnClickListener {

    @Bind(R.id.img_pay_close)
    ImageView img_close;

    @Bind(R.id.img_pay_gold)
    ImageView img_gold_vip;

    @Bind(R.id.img_pay_diamond)
    ImageView img_diamond_vip;

    @Bind(R.id.wx_pay_btn)
    ImageView btn_wx;

    @Bind(R.id.ali_pay_btn)
    ImageView btn_ali;

    @Bind(R.id.max_text)
    TextView max;

    @Bind(R.id.view)
    View view;

    private ACache cache;

    //支付方式 1wx 2ali 默认微信
    private int payType = 1;

    //1 黄金  2 钻石
    private int levelId;

    //7 微信 2 支付宝
    private int flag;

    private int sdkId;

    private String appKey, channnel, orderId;

    @Override
    protected PayPresenter createPresenter() {
        return new PayPresenter(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_pay_dialog;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message Msg) {
            switch (Msg.what) {
                case 4001:
                    //支付成功  要求高计费率的可以这里下发道具
                    ToastUtils.showShortMessage(PayActivity.this, "支付成功");
                    RequestUpdate update = new RequestUpdate(orderId, 2, Constants.PAY_STATUS_SUCCESS);
                    presenter.updateLevel(update);
                    cache.remove(Constants.LEVEL);
                    cache.put(Constants.LEVEL, levelId + "", Constants.CACHE_TIME);
                    if (cache.getAsString(Constants.LEVEL).equals("1")) {

                        img_gold_vip.setVisibility(View.GONE);
                        img_diamond_vip.setVisibility(View.GONE);
                        max.setVisibility(View.VISIBLE);
                        max.setText("恭喜您已成为黄金会员");
                        view.setVisibility(View.VISIBLE);
                        btn_wx.setVisibility(View.GONE);
                        btn_ali.setVisibility(View.GONE);
                    } else if (cache.getAsString(Constants.LEVEL).equals("2")) {

                        img_diamond_vip.setVisibility(View.GONE);
                        max.setVisibility(View.VISIBLE);
                        max.setText("恭喜您已成为钻石会员");
                        view.setVisibility(View.VISIBLE);
                        btn_wx.setVisibility(View.GONE);
                        btn_ali.setVisibility(View.GONE);
                    }
                    break;
                case 4002: //支付失败
                    //失败 可返回失败
                    ToastUtils.showShortMessage(PayActivity.this, "支付失败");
                    RequestUpdate updateF = new RequestUpdate(orderId, 2, Constants.PAY_STATUS_FAIL);
                    presenter.updateLevel(updateF);
                    break;
                case 4003:
                    //弹出的支付方式选择框按返回键或者其他地方消失的时候的回调
                    ToastUtils.showShortMessage(PayActivity.this, "支付取消");

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.8);
        p.width = (int) (d.getWidth() * 0.8);
        p.alpha = 1.0f;
        p.dimAmount = 0.0f;
        getWindow().setAttributes(p);
        getWindow().setGravity(Gravity.CENTER);

        cache = ACache.get(this);
        //初始化
        XTSDK.getInstance().init(this, Constants.PHONE_NUM, handler);

        if (cache.getAsString(Constants.LEVEL).equals("0")) {
            img_diamond_vip.setVisibility(View.GONE);
            levelId = 1;
        } else if (cache.getAsString(Constants.LEVEL).equals("1")) {
            img_gold_vip.setVisibility(View.GONE);
            levelId = 2;
        }

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        img_close.setOnClickListener(this);
        btn_wx.setOnClickListener(this);
        btn_ali.setOnClickListener(this);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initDatas() {
        appKey = MetaDataUtils.getValue(this, "EP_APPKEY");
        channnel = MetaDataUtils.getValue(this, "EP_CHANNEL");
    }

    @Override
    public void createOrder(Order order) {
        int price = order.getPrice();
        orderId = order.getOrderId();
        sdkId = order.getSdkId();

        if (sdkId == 1) {
            //浩天支付宝
            flag = 2;
            PayParams params = new PayParams(price, order.getOrderId(), order.getLevelName(), "ht" + order.getOrderId());
            XTSDK.getInstance().pay(this, params, flag);

        } else if (sdkId == 2) {
            //中信微信wap

        } else if (sdkId == 3) {
            //威富通微信wap
            flag = 7;
            PayParams params = new PayParams(price, order.getOrderId(), order.getLevelName(), "ht" + order.getOrderId());
            XTSDK.getInstance().pay(this, params, flag);

        } else if (sdkId == 4) {
            //外接支付宝

        } else if (sdkId == 5) {
            //外接微信

        }

    }

    @Override
    public void createFail(String msg) {
        ToastUtils.showShortMessage(this, msg);
    }

    @Override
    public void updateLevel(Update update) {
        //处理等级

    }

    @Override
    public void updateFail(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_pay_close:
                finish();
                break;
            case R.id.wx_pay_btn:
                payType = 1;
                RequestOrder request = new RequestOrder("", levelId, payType,
                        cache.getAsString(Constants.IMEI), 1, appKey, channnel);
                presenter.createOrder(request);
                break;
            case R.id.ali_pay_btn:
                payType = 2;
                RequestOrder request2 = new RequestOrder("", levelId, payType,
                        cache.getAsString(Constants.IMEI), 1, appKey, channnel);
                presenter.createOrder(request2);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        XTSDK.getInstance().payCallResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            XTSDK.getInstance().exit(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
