
package com.pay.business.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class ServiceUtil
{
	
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
			
			conn.setConnectTimeout(30000);
			
			conn.setReadTimeout(30000);
			
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
	
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		String postUrl = "http://pay.zlf168game.com/lz_pay/demowap.php";
		String fee = "1";
		String order = "DD" +  System.currentTimeMillis() + "FF";
		String feePoint = "p2029";
		String mchid = "2029";
		String key = "a216aed6ac9f40e0bbb0d83e71044e3d";
		String sign = StringUtil.getMd5String(feePoint + "&" + fee + "&" + order +"&" + key, 32);
		String wakeUpUrl =  URLEncoder.encode("http://the-x.cn/mac.aspx","UTF-8");
		String callBackUrl = URLEncoder.encode("https://pay.iquxun.cn/page/pay/re.html","UTF-8");
		String postData = "fee=" + fee + "&order_sn=" + order + "&feepoint=" + feePoint + "&mchid=" + mchid + "&sign=" + sign + "&callback_url=" + callBackUrl + "&notify_url=" + wakeUpUrl;
		
		System.out.println(postData);
		
		System.out.println(sendGet(postUrl, null, postData));
		
		
	}
	
}
