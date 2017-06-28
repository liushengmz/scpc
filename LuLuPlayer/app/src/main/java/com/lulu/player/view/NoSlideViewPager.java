package com.lulu.player.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * can't scroll viewPager
 *
 * @author zxc
 * @time 2016/9/22 0022 下午 5:36
 */
public class NoSlideViewPager extends ViewPager {

    private boolean noSlide = true;

    public NoSlideViewPager(Context context) {
        super(context);
    }

    public NoSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNoSlide(boolean noSlide) {
        this.noSlide = noSlide;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noSlide) {
            return false;
        } else {
            return super.onTouchEvent(arg0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noSlide) {
            return false;
        } else {
            return super.onInterceptTouchEvent(arg0);
        }
    }

}
