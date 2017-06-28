package com.lulu.player.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * @author zxc
 * @time 2016/9/26 0026下午 5:49
 */
public class FullVideoView extends VideoView {
    public FullVideoView(Context context) {
        super(context);
    }

    public FullVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FullVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(WindowManager.LayoutParams.MATCH_PARENT, widthMeasureSpec);
        int height = getDefaultSize(WindowManager.LayoutParams.MATCH_PARENT, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
