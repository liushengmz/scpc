package com.lulu.player.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;

/**
 * Created by Administrator on 2016/9/26 0026.
 */
public class VideoController extends MediaController{
    public VideoController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public VideoController(Context context) {
        super(context);
    }



}
