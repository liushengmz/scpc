package com.lulu.player.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zxc
 * @time 2016/9/22 0022 下午 5:37
 */
public class SharedPreferencesUtil {

    private static String mFileName = "sp";

    private int mode = Context.MODE_MULTI_PROCESS;

    private static volatile SharedPreferencesUtil INSTANCE;

    private SharedPreferences sp;

    private SharedPreferencesUtil() {

    }

    public SharedPreferencesUtil(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(mFileName, mode);
        }
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SharedPreferencesUtil(context);
                }
            }
        }
        return INSTANCE;
    }

    //保存数组
    public void save(String key, List<Map<String, String>> datas) {
        JSONArray mJsonArray = new JSONArray();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> itemMap = datas.get(i);
            Iterator<Map.Entry<String, String>> iterator = itemMap.entrySet().iterator();
            JSONObject object = new JSONObject();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                try {
                    object.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mJsonArray.put(object);
        }
        Editor editor = sp.edit();
        editor.putString(key, mJsonArray.toString());
        editor.commit();
    }

    public List<Map<String, String>> getInfo(Context context, String key) {
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        String result = sp.getString(key, "");
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObject = array.getJSONObject(i);
                Map<String, String> itemMap = new HashMap<String, String>();
                JSONArray names = itemObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        String value = itemObject.getString(name);
                        itemMap.put(name, value);
                    }
                }
                datas.add(itemMap);
            }
        } catch (JSONException e) {

        }

        return datas;
    }

    public void save(String key, int value) {
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void save(String key, long value) {
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void save(String key, String value) {
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void save(String key, boolean value) {
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getString(String key) {
        String data = sp.getString(key, null);
        return data;
    }

    public int getInt(String key) {
        int data = sp.getInt(key, -1);
        return data;
    }

    public long getLong(String key) {
        long data = sp.getLong(key, -1L);
        return data;
    }

    public boolean getBoolean(String key) {
        boolean data = sp.getBoolean(key, false);
        return data;
    }

    public boolean isFirstUse(String key) {
        boolean data = sp.getBoolean(key, true);
        return data;
    }

    public void remove(String key) {
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getmFileName() {
        return mFileName;
    }

    public static void setmFileName(String mFileName) {
        SharedPreferencesUtil.mFileName = mFileName;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public void setSp(SharedPreferences sp) {
        this.sp = sp;
    }

}
