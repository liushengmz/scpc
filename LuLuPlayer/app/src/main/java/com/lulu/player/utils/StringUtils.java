package com.lulu.player.utils;

/**
 * @author zxc
 * @time 2016/9/22 0022 下午 5:37
 */
public class StringUtils {

    private StringUtils() {
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str);
    }

}
