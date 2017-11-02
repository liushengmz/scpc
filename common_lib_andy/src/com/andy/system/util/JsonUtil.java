package com.andy.system.util;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil
{
	private static Logger logger = Logger.getLogger(JsonUtil.class);
	
	public static String getJsonFormObject(Object obj)
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
		return JSONObject.fromObject(json);
	}
	
	public static String getJsonArrayFromObject(Object obj)
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
}
