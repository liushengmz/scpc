package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.util.StringUtil;

public class DataHandleCache
{
	private static List<String> verifyCodeCacheList = new ArrayList<String>();			
	
	public static boolean isExistVerifyCode(String key)
	{
		if(StringUtil.isNullOrEmpty(key))
			return false;
		
		return verifyCodeCacheList.contains(key);
	}
	
	public static void removeVerifyCode(String key)
	{
		verifyCodeCacheList.remove(key);
	}
	
	public static void addVerifyCode(String key)
	{
		verifyCodeCacheList.add(key);
	}
	
}
