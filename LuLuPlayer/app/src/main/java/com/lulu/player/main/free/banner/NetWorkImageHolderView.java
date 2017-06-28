package com.lulu.player.main.free.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lulu.player.R;
import com.lulu.player.model.Video;

/**
 * 广告栏加载网络图片
 *
 * @author zxc
 * @time 2016/9/26 0026下午 1:32
 */
public class NetWorkImageHolderView implements Holder<Video> {

    private View view;

    @Override
    public View createView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.item_banner, null, false);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, Video data) {
        ImageView imageView = (ImageView) view.findViewById(R.id.img_banner);
        if (data.getImgUrl() != null || "".equals(data.getImgUrl()))
            Glide.with(context).load(data.getImgUrl())
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
    }

}
