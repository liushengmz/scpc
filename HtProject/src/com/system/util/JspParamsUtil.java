package com.system.util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Andy.Chen
 *
 */
public class JspParamsUtil
{
	/**
	 * 获取所有的查询字符串
	 * @param request
	 * @return
	 */
	public static Map<String, String> trandGetParams(HttpServletRequest request)
	{
		if(request==null)
			return null;
		
		Map<String, String> map = new HashMap<String, String>();
		
		String query = request.getQueryString();
		
		if(StringUtil.isNullOrEmpty(query) || "null".equalsIgnoreCase(query))
			return map;
		
		splitQueryDataToMap(query,map);
		
		return map;
	}
	
	/**
	 * 将数据字符串转成MAP格式
	 * @param query
	 * @param map
	 */
	private static void splitQueryDataToMap(String query,Map<String, String> map)
	{
		String[] keyValues = query.split("&");
		
		if(keyValues==null)
			return;
		
		for(String keyValue : keyValues)
		{
			String[] kv = keyValue.split("=");
			
			if(kv==null || kv.length !=2)
				continue;
			
			map.put(kv[0], kv[1]);
		}
	}
	
	/**
	 * 获取所有POST过来的参数
	 * 注意：一定要在request获取流之前获取，否则将获取不到内容
	 * @param request
	 * @return
	 */
	public static Map<String,String> trandPostParams(HttpServletRequest request)
	{
		if(request==null)
			return null;
		
		Map<String, String> map = new HashMap<String, String>();
		
		String query = ServiceUtil.requestPostData(request);
		
		try
		{
			query = URLDecoder.decode(query,"UTF-8");
			
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		
		if(StringUtil.isNullOrEmpty(query))
			return map;
		
		splitQueryDataToMap(query,map);
		
		return map;
	}
	
	public static Map<String,String> trandParams(HttpServletRequest request)
	{
		if(request==null)
			return null;
		
		
		Map<String, String> map = new HashMap<String, String>();
		
		return map;
	}
	
	
	
}
