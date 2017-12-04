package com.pay.business.util.tcpay.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil
{
	private static Logger logger = Logger.getLogger(JsonUtil.class);
	
	public static JSONObject getJsonFormObject(Object obj)
	{
		try
		{
			return JSONObject.fromObject(obj);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	public static String getJsonStringFormObject(Object obj)
	{
		try
		{
			return JSONObject.fromObject(obj).toString();
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	public static JSONObject getJsonFromString(String json)
	{
		try
		{
			return JSONObject.fromObject(json);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	public static String getJsonArrayStringFromObject(Object obj)
	{
		try
		{
			return JSONArray.fromObject(obj).toString();
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	public static JSONArray getJsonArrayFromObject(Object obj)
	{
		try
		{
			return JSONArray.fromObject(obj);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	public static String getString(JSONObject json,String key,String defaultValue)
	{
		try
		{
			return json.getString(key);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return defaultValue;
	}
	
	public static int getInteger(JSONObject json,String key,int defaultValue)
	{
		try
		{
			return json.getInt(key);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return defaultValue;
	}
	
	public static void main(String[] args)
	{
		Map<String, Object> map = new HashMap<>();
		map.put("A", "Andy");
		map.put("age", 18);
		JSONObject json = getJsonFormObject(map);
		System.out.println(json);
		//System.out.println(json.getInt("Andy"));
		System.out.println(getInteger(json, "Andy", -1));
	}
}
