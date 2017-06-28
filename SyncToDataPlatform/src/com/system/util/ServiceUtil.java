
package com.system.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ServiceUtil
{
	public static String sendData(String url, Map<String,String> params,String postData)
	{
		StringBuffer result = new StringBuffer();
		
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		
		OutputStream os = null;
		
		try
		{
			URL realUrl = new URL(url);
			
			URLConnection conn = realUrl.openConnection();
			
			conn.setConnectTimeout(10000);
			
			conn.setReadTimeout(10000);
			
			conn.setRequestProperty("accept", "*/*");
			
			conn.setRequestProperty("connection", "Keep-Alive");
			
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			
			if(params!=null)
			{
				for(String key : params.keySet())
				{
					conn.setRequestProperty(key, params.get(key));
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
				os.write(postData.getBytes("UTF-8"));
				os.flush();
			}
			
			is = conn.getInputStream();
			
			isr = new InputStreamReader(is,"UTF-8");
			
			br = new BufferedReader(isr);
			
			String line = null;
			
			while ((line = br.readLine()) != null)
			{
				result.append(line);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
	
	public static String requestPostData(HttpServletRequest request)
	{
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		StringBuffer result = null;
		
		try
		{
			is = request.getInputStream();
			isr = new InputStreamReader(is);
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
	
	
}




