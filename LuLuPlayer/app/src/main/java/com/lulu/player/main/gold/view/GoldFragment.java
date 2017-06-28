package com.lulu.player.main.gold.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.lulu.player.R;
import com.lulu.player.adapter.GridViewAdapter;
import com.lulu.player.common.Constants;
import com.lulu.player.main.gold.presenter.GoldPresenter;
import com.lulu.player.model.Intro;
import com.lulu.player.model.RequestVideo;
import com.lulu.player.model.Video;
import com.lulu.player.model.VideoListRsp;
import com.lulu.player.mvp.MvpFragment;
import com.lulu.player.pay.view.PayActivity;
import com.lulu.player.utils.ACache;
import com.lulu.player.utils.JsonDecode;
import com.lulu.player.video.VideoActivity;
import com.lulu.player.view.ScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author zxc
 * @time 2016/9/26 0026上午 11:15
 */
public class GoldFragment extends MvpFragment<GoldPresenter> implements GoldView {

    @Bind(R.id.gold_gridView)
    ScrollGridView goldGV;

    @Bind(R.id.header_gold)
    TextView header;

    private GridViewAdapter goldAdapter;

    private List<Video> goldVideos;

    private ACache cache;

    public static GoldFragment newInstance(String title, int index) {
        GoldFragment fragment = new GoldFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TITLE, title);
        bundle.putInt(Constants.INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_gold;
    }

    @Override
    protected void initDatas() {
        goldVideos = new ArrayList<>();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        goldGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cache.getAsString(Constants.LEVEL).equals("1") || cache.getAsString(Constants.LEVEL).equals("2")) {
                    Intent intent = new Intent(getActivity(), VideoActivity.class);
                    intent.putExtra(Constants.VIDEO_URL, goldVideos.get(position).getVideoUrl());
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                } else {
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_OK) {
            if (cache.getAsString(Constants.LEVEL).equals("1")) {
                Intent intent = new Intent(getActivity(), PayActivity.class);
                startActivity(intent);
            }
        }

    }

    @Override
    protected void initAdapters() {
        goldAdapter = new GridViewAdapter(getActivity(), goldVideos);
        goldGV.setAdapter(goldAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = ACache.get(getContext());
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (cache.getAsString(Constants.GOLD_VIDEO_LIST) == null) {
            RequestVideo requestVideo = new RequestVideo(1);
            presenter.getVideo(requestVideo);
        } else {
            JsonDecode decode = new JsonDecode();
            VideoListRsp videoListRsp2 = decode.decode(cache.getAsString(Constants.GOLD_VIDEO_LIST));
            goldVideos.addAll(videoListRsp2.getVideoList());
            goldAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        header.setFocusable(true);
        header.setFocusableInTouchMode(true);
        header.requestFocus();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void requestVideoList(Intro<List<Video>> intro) {
        cache.put(Constants.GOLD_VIDEO_LIST, intro.toString(), Constants.CACHE_TIME);
        goldVideos.addAll(intro.getVideos());
        goldAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestFail(String msg) {

    }

    @Override
    protected GoldPresenter createPresenter() {
        return new GoldPresenter(this);
    }
}
