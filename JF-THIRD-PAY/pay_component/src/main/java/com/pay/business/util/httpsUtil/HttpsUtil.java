package com.pay.business.util.httpsUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.core.teamwork.base.util.http.HttpUtil;

//import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: HttpTest
 * @Description: 自己开始封装的HTTP连接工具,http连接传递的参数封装到一个对象里面，
 *               http中get请求时，是把参数拼接到url后面的，而post请求直接输出即可
 * @author: LUCKY
 * @date:2016年1月6日 下午3:49:28
 */
public class HttpsUtil {
	private static final Logger LOGGER = Logger.getLogger(HttpUtil.class);
	
	public static int doPost(String url,Map<String,Object> map,String charset){  
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{  
            httpClient = new SSLClient();  
            httpPost = new HttpPost(url);  
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
                httpPost.setEntity(entity);  
            }  
            HttpResponse response = httpClient.execute(httpPost);  
            
            int statusCode = response.getStatusLine().getStatusCode();
            LOGGER.info(url+"访问http状态码："+statusCode);
            /*if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  */
            return statusCode;
        }catch(Exception ex){  
            ex.printStackTrace();  
            LOGGER.info(url+"访问http状态码："+500);
            return 500;
        } finally {
			if (null != httpClient) {
				httpClient.getConnectionManager().shutdown();
			}
		} 
    }  
	
	public static String doPostString(String url,Map<String,Object> map,String charset){  
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{  
            httpClient = new SSLClient();  
            httpPost = new HttpPost(url);  
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
                httpPost.setEntity(entity);  
            }  
            HttpResponse response = httpClient.execute(httpPost);  
            
            int statusCode = response.getStatusLine().getStatusCode();
            LOGGER.info(url+"访问http状态码："+statusCode);
            if (statusCode != 200) {
				return "";
			} else {
	            if(response != null){  
	                HttpEntity resEntity = response.getEntity();  
	                if(resEntity != null){  
	                    result = EntityUtils.toString(resEntity,charset);  
	                    return result;
	                }  
	            }
	            return "";
			}
        }catch(Exception ex){  
            ex.printStackTrace();  
            return "";
        }finally {
			if (null != httpClient) {
				httpClient.getConnectionManager().shutdown();
			}
		}  
    } 

}