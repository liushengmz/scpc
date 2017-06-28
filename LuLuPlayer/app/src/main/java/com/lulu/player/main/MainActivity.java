package com.lulu.player.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lulu.player.R;
import com.lulu.player.adapter.FragmentsAdapter;
import com.lulu.player.base.BaseActivity;
import com.lulu.player.base.BaseFragment;
import com.lulu.player.common.Constants;
import com.lulu.player.main.diamond.view.DiamondFragment;
import com.lulu.player.main.free.view.FreeFragment;
import com.lulu.player.main.gold.view.GoldFragment;
import com.lulu.player.main.my.view.MyFragment;
import com.lulu.player.pay.view.PayActivity;
import com.lulu.player.utils.ACache;
import com.lulu.player.view.NoSlideViewPager;
import com.lulu.player.view.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * @author zxc
 * @time 2016/9/26 0026下午 2:40
 */
public class MainActivity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener {

    @Bind(R.id.content)
    NoSlideViewPager mViewPager;

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.tab_group)
    RadioGroup mTabGroup;

    private List<BaseFragment> mFragments;

    private FragmentsAdapter mFragmentsAdapter;

    private List<String> titles;

    private List<RadioButton> mTabItems;

    private ACache cache;

    private long mExitTime;

    private int count = 35246;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            count += 1;
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.toast, null);
            ToastHelper.makeText(MainActivity.this, "第" + count + "位会员充值成功", ToastHelper.LENGTH_SHORT).setView(view)
                    .setAnimation(R.style.anim_view).show();
            handler.postDelayed(this, 10000);
        }
    };

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewPager();
        initTabBar();
        cache = ACache.get(this);
        if (Build.VERSION.SDK_INT < 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            ViewGroup mContentParent = (ViewGroup) mContentView.getParent();

            View statusBarView = mContentParent.getChildAt(0);
            if (statusBarView != null && statusBarView.getLayoutParams() != null &&
                    statusBarView.getLayoutParams().height == getStatusBarHeight(this)) {
                //避免重复调用时多次添加 View
                statusBarView.setBackgroundColor(Color.rgb(58, 44, 53));
                return;
            }

            //创建一个假的 View, 并添加到 ContentParent
            statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this));
            statusBarView.setBackgroundColor(Color.rgb(58, 44, 53));
            mContentParent.addView(statusBarView, 0, lp);

            //ChildView 不需要预留系统空间
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
            }
        } else {
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(Color.rgb(58, 44, 53));

            ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }

    }

    private void initViewPager() {
        mFragments = new ArrayList<BaseFragment>();
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mTabGroup.check(mTabItems.get(position).getId());
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });
        mFragmentsAdapter = new FragmentsAdapter(fm, mFragments);
        mViewPager.setAdapter(mFragmentsAdapter);
        FreeFragment freeFragment = FreeFragment.newInstance(getResources().getString(R.string.main_free), 0);
        GoldFragment goldFragment = GoldFragment.newInstance(getResources().getString(R.string.main_gold), 1);
        DiamondFragment diamondFragment = DiamondFragment.newInstance(getResources().getString(R.string.main_diamond),
                2);
        MyFragment myFragment = MyFragment.newInstance(getResources().getString(R.string.main_my), 3);

        mFragments.add(freeFragment);
        mFragments.add(goldFragment);
        mFragments.add(diamondFragment);
        mFragments.add(myFragment);
        mFragmentsAdapter.notifyDataSetChanged();
    }

    public void initTabBar() {
        // TODO Auto-generated method stub
        mTabItems = new ArrayList<RadioButton>();
        mTabGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int currentIndex = 0;
                switch (checkedId) {
                    case R.id.tab_free:
                        currentIndex = 0;
                        break;
                    case R.id.tab_gold:
                        currentIndex = 1;
                        break;
                    case R.id.tab_diamond:
                        currentIndex = 2;
                        break;
                    case R.id.tab_my:
                        currentIndex = 3;
                        break;
                }
                mViewPager.setCurrentItem(currentIndex);
                titleText.setText(titles.get(currentIndex));
            }
        });

        for (int i = 0; i < mTabGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) mTabGroup.getChildAt(i);
            mTabItems.add(radioButton);
        }

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {
        title = titles.get(0);
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
        titles = new ArrayList<String>();
        titles.add("体验区");
        titles.add("黄金区");
        titles.add("钻石区");
        titles.add("我的");
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (cache.getAsString(Constants.LEVEL).equals("2")) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    System.exit(0);
                }
                return true;
            } else {
                Intent intent = new Intent(MainActivity.this, PayActivity.class);
                startActivity(intent);
                return false;
            }


        }
        return super.onKeyDown(keyCode, event);

    }

}
