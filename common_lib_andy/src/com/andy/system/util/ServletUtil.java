package com.andy.system.util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * 
 * @author Andy.Chen
 *
 */
public class ServletUtil
{
	private static Logger logger = Logger.getLogger(ServletUtil.class);
	
	/**
	 * 获取所有的查询字符串
	 * @param request
	 * @return
	 */
	public static Map<String, String> getQueryParams(HttpServletRequest request)
	{
		if(request==null)
			return null;
		
		Map<String, String> map = new HashMap<String, String>();
		
		String query = request.getQueryString();
		
		if(StringUtil.isNullOrEmpty(query) || "null".equalsIgnoreCase(query))
			return map;
		
		splitQueryDataToMap(query,map);
		
		logger.debug(map);
		
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
	public static Map<String,String> getPostParams(HttpServletRequest request,String chartSet)
	{
		if(request==null)
			return null;
		
		String defaultChartSet = StringUtil.isNullOrEmpty(chartSet) ? "UTF-8" : chartSet;
		
		Map<String, String> map = new HashMap<String, String>();
		
		String query = NetUtil.requestPostData(request,defaultChartSet);
		
		try
		{
			query = URLDecoder.decode(query,defaultChartSet);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		if(StringUtil.isNullOrEmpty(query))
			return map;
		
		splitQueryDataToMap(query,map);
		
		logger.debug(map);
		
		return map;
	}
	
	public static Map<String,String> getRequestParams(HttpServletRequest request,String chartSet)
	{
		if(request==null)
			return null;
		
		Map<String, String> queryParams = getQueryParams(request);
		
		String defaultChartSet = StringUtil.isNullOrEmpty(chartSet) ? "UTF-8" : chartSet;
		
		Map<String, String> postParams = getPostParams(request,defaultChartSet);
	
		if(postParams!=null && !postParams.isEmpty())
			queryParams.putAll(postParams);
		
		logger.debug(queryParams.toString());
		
		return queryParams;
	}
	
}
