
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
	private static final int REQUEST_EXPIRE_MILS = StringUtil.getInteger(ConfigManager.getConfigData("REQUEST_EXPIRE_MILS"), 30000);
	
	public static String sendGet(String url, Map<String,String> params,String postData)
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
			
			conn.setConnectTimeout(REQUEST_EXPIRE_MILS);
			
			conn.setReadTimeout(REQUEST_EXPIRE_MILS);
			
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
	
	public static void main(String[] args)
	{
		//String data = "amount=10000&open_id=dae6fb7c8af527d98e878ecebe12f210&ord_no=91504195142289485887&out_no=DD2017083123590222475746&pay_time=20170831235935&rand_str=vmRLiBIbSxLvFN8hvNQUnmtUpAmZsvwYRoJaZrlRY6nEUvVpjLjH8MBxnYwQt3Olryvq0RiZYFDSbyhvkAcsnNZLMwBfApA1Y6sYXKXVqANC847sFjV37VOTrq91QJ3O&status=1&timestamp=1504195176&sign=6d152bc27bfc0ea9733420ff05b28f61";
		
		String ss = "https://pay.iquxun.cn/aiJinFuPay/aliScanPay.do";
		
		//System.out.println("send get data:" + sendGet("https://pay.iquxun.cn/aiJinFuPay/PABankScanPayCallBack.do", null, data));
		
		System.out.println("send get data:" + sendGet(ss, null, null));
	}
	
}
