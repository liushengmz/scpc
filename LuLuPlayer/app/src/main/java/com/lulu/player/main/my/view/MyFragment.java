package com.lulu.player.main.my.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lulu.player.R;
import com.lulu.player.common.Constants;
import com.lulu.player.main.my.presenter.MyPresenter;
import com.lulu.player.model.Levels;
import com.lulu.player.model.UserInfo;
import com.lulu.player.mvp.MvpFragment;
import com.lulu.player.pay.view.PayActivity;
import com.lulu.player.utils.ACache;
import com.lulu.player.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author zxc
 * @time 2016/9/23 0023 上午 11:25
 */
public class MyFragment extends MvpFragment<MyPresenter> implements MyView {

    @Bind(R.id.my_id_text)
    TextView user;

    @Bind(R.id.my_password_text)
    TextView password;

    @Bind(R.id.my_xianshi)
    TextView show;

    @Bind(R.id.my_level_text)
    TextView level;

    @Bind(R.id.my_special_text)
    TextView special;

    @Bind(R.id.my_upgrade_text)
    TextView upgrade;

    @Bind(R.id.service)
    TextView phone;

    private String user_name, password_text;

    private List<Levels> mLevels;

    private ACache cache;

    public static MyFragment newInstance(String title, int index) {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TITLE, title);
        bundle.putInt(Constants.INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = ACache.get(getContext());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initDatas() {

        mLevels = new ArrayList<>();

        user_name = "" + cache.getAsString(Constants.USER_NAME);
        user.setText(user_name);

        password_text = "" + cache.getAsString(Constants.PASSWORD);
        password.setText("******");

        setLevels();

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        show.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                show.setVisibility(View.GONE);
                password.setText(password_text);
            }
        });
        upgrade.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cache.getAsString(Constants.LEVEL).equals("2")) {
                    ToastUtils.showShortMessage(getActivity(), "您已是最高级别会员，不用升级！");
                } else {
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    public void onResume() {
        super.onResume();
        setLevels();
    }

    @Override
    protected MyPresenter createPresenter() {
        return new MyPresenter(this);
    }

    @Override
    public void requestUserInfo(UserInfo<List<Levels>> info) {

    }

    @Override
    public void requestFail(String msg) {

    }

    public void setLevels() {
        switch (cache.getAsString(Constants.LEVEL)) {
            case "0":
                level.setText("体验会员");
                special.setText("观看体验区电影");
                phone.setText("客服电话：请您先成为会员才可查看");
                upgrade.setText(getResources().getString(R.string.my_cozuo_c));
                break;
            case "1":
                level.setText("黄金会员");
                special.setText("观看黄金区电影");
                phone.setText(getResources().getString(R.string.my_qq));
                upgrade.setText(getResources().getString(R.string.my_cozuo_c_2));
                break;
            case "2":
                level.setText("钻石会员");
                special.setText("观看所有电影");
                phone.setText(getResources().getString(R.string.my_qq));
                upgrade.setText(getResources().getString(R.string.my_cozuo_cc_3));
                break;
        }
    }
}
