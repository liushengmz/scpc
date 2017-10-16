
package com.andy.system.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class NetUtil
{
	private static Logger logger = Logger.getLogger(NetUtil.class);
	
	private static final int REQUEST_EXPIRE_MILS = StringUtil.getInteger(PropertyConfigUtil.getConfigData("REQUEST_EXPIRE_MILS"), 30000);
	
	public static String requetUrl(String url, Map<String,String> headers,String postData,String chartSet)
	{
		StringBuffer result = new StringBuffer();
		
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		
		OutputStream os = null;
		
		String defaultChartSet = StringUtil.isNullOrEmpty(chartSet) ? "UTF-8" : chartSet;
		
		try
		{
			URL realUrl = new URL(url);
			
			URLConnection conn = realUrl.openConnection();
			
			conn.setConnectTimeout(REQUEST_EXPIRE_MILS);
			
			conn.setReadTimeout(REQUEST_EXPIRE_MILS);
			
			conn.setRequestProperty("accept", "*/*");
			
			conn.setRequestProperty("connection", "Keep-Alive");
			
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			
			if(headers!=null)
			{
				for(String key : headers.keySet())
				{
					conn.setRequestProperty(key, headers.get(key));
				}
			}
			
			if(!StringUtil.isNullOrEmpty(postData))
			{
				conn.setDoOutput(true);
			}

			conn.connect();
			
			if(!StringUtil.isNullOrEmpty(postData))
			{
				os = conn.getOutputStream();
				os.write(postData.getBytes(defaultChartSet));
				os.flush();
			}
			
			is = conn.getInputStream();
			
			isr = new InputStreamReader(is,defaultChartSet);
			
			br = new BufferedReader(isr);
			
			String line = null;
			
			while ((line = br.readLine()) != null)
			{
				result.append(line);
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try{if(os!=null)os.close();}catch(Exception ex){}
			try{if(br!=null)br.close();}catch(Exception ex){}
			try{if(isr!=null)isr.close();}catch(Exception ex){}
			try{if(is!=null)is.close();}catch(Exception ex){}
		}
		return result.toString().trim();
	}
	
	public static String requestPostData(HttpServletRequest request,String chartSet)
	{
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		StringBuffer result = null;
		
		String defaultChartSet = StringUtil.isNullOrEmpty(chartSet) ? "UTF-8" : chartSet;
		
		try
		{
			is = request.getInputStream();
			isr = new InputStreamReader(is,defaultChartSet);
			br = new BufferedReader(isr);
			result = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null)
			{
				result.append(line);
			}
			return result.toString().trim();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try{if(br!=null)br.close();}catch(Exception ex){}
			try{if(isr!=null)isr.close();}catch(Exception ex){}
			try{if(is!=null)is.close();}catch(Exception ex){}
		}
		return "";
	}
	
	public static void main(String[] args)
	{
		
	}
	
}
