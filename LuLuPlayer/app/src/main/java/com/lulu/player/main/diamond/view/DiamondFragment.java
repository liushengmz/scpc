package com.lulu.player.main.diamond.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.lulu.player.R;
import com.lulu.player.adapter.GridViewAdapter;
import com.lulu.player.common.Constants;
import com.lulu.player.main.diamond.presenter.DiamondPresenter;
import com.lulu.player.model.Intro;
import com.lulu.player.model.RequestVideo;
import com.lulu.player.model.Video;
import com.lulu.player.model.VideoListRsp;
import com.lulu.player.mvp.MvpFragment;
import com.lulu.player.pay.view.PayActivity;
import com.lulu.player.utils.ACache;
import com.lulu.player.utils.JsonDecode;
import com.lulu.player.utils.ToastUtils;
import com.lulu.player.video.VideoActivity;
import com.lulu.player.view.ScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author zxc
 * @time 2016/9/26 0026上午 11:16
 */
public class DiamondFragment extends MvpFragment<DiamondPresenter> implements DiamondView {

    @Bind(R.id.diamond_gridView)
    ScrollGridView diamondGV;

    @Bind(R.id.header_diamond)
    TextView header;

    private ACache cache;

    private GridViewAdapter diamondAdapter;

    private List<Video> mVideos;

    public static DiamondFragment newInstance(String title, int index) {
        DiamondFragment fragment = new DiamondFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TITLE, title);
        bundle.putInt(Constants.INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_diamond;
    }

    @Override
    protected void initDatas() {
        mVideos = new ArrayList<>();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {
        diamondGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cache.getAsString(Constants.LEVEL).equals("2")) {
                    Intent intent = new Intent(getActivity(), VideoActivity.class);
                    intent.putExtra(Constants.VIDEO_URL, mVideos.get(position).getVideoUrl());
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                } else if (cache.getAsString(Constants.LEVEL).equals("1")) {
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortMessage(getActivity(), "请您先成为黄金会员");
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_OK) {

        }

    }

    @Override
    protected void initAdapters() {
        diamondAdapter = new GridViewAdapter(getActivity(), mVideos);
        diamondGV.setAdapter(diamondAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cache = ACache.get(getContext());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (cache.getAsString(Constants.DIAMOND_VIDEO_LIST) == null) {
            RequestVideo requestVideo = new RequestVideo(2);
            presenter.getVideo(requestVideo);
        } else {
            JsonDecode decode = new JsonDecode();
            VideoListRsp videoListRsp = decode.decode(cache.getAsString(Constants.DIAMOND_VIDEO_LIST));
            mVideos.addAll(videoListRsp.getVideoList());
            diamondAdapter.notifyDataSetChanged();
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
    public void requestVideoList(Intro<List<Video>> intro) {

        cache.put(Constants.DIAMOND_VIDEO_LIST, intro.toString(), Constants.CACHE_TIME);
        mVideos.addAll(intro.getVideos());
        diamondAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestFail(String msg) {

    }

    @Override
    protected DiamondPresenter createPresenter() {
        return new DiamondPresenter(this);
    }
}
