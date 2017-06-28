package com.lulu.player.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 获取meta-data属性值
 *
 * @author Administrator
 * @time 2016/9/28 0028下午 6:58
 */
public class MetaDataUtils {

    public static String getValue(Context c, String key) {
        try {
            ApplicationInfo ai = c.getPackageManager().getApplicationInfo(
                    c.getPackageName(), PackageManager.GET_META_DATA);
            Object obj = ai.metaData.get(key);
            if (obj instanceof Integer) {
                long longValue = ((Integer) obj).longValue();
                String value = String.valueOf(longValue);
                return value;
            } else if (obj instanceof String) {
                String value = String.valueOf(obj);
                return value;
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return null;

    }

}
