package com.lulu.player.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author zxc
 * @time 2016/9/23 0023 上午 10:27
 */
public class ToastUtils {

    public ToastUtils() {
    }

    public static void showError(final String message, final Context context) {
        getToast(context,message).show();
    }

    public static void showShortMessage(Context context, String message) {
        getToast(context,message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMessage(Context context, String message) {
        getToast(context,message, Toast.LENGTH_LONG).show();
    }

    private static Toast getToast(Context context, String message) {
        return getToast(context,message, Toast.LENGTH_LONG);
    }

    private static Toast getToast(Context context, String message, int length) {
        return Toast.makeText(context, message, length);
    }

}
