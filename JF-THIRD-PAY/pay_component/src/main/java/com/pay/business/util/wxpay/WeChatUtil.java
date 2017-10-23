package com.pay.business.util.wxpay;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.core.teamwork.base.util.http.HttpUtil;

/**
 * 微信额外用到的方法集成
 * @ClassName: WeChatUtil 
 * @Description: 
 * @author yangyu
 * @date 2016年11月10日 下午3:37:48
 */
public class WeChatUtil {

	private static Logger logger = Logger.getLogger(WeChatUtil.class);
	
	/**
	 * 微信请求xml请求方法
	 * @param url
	 * @param content 默认UTF-8
	 * @return
	 */
	public static InputStream post(String url,String content){
    	return post(url, content, "UTF-8");
    }
	
	protected static InputStream post(String url,String content,String charset){
		URL uri = null;
		try {
			uri = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream out = conn.getOutputStream();
			OutputStreamWriter ow = new OutputStreamWriter(out,charset);
			ow.write(content);
			ow.flush();
			ow.close();
			out.flush();
			out.close();
			int len = conn.getContentLength();
			if(len>0){
				return conn.getInputStream();
			}
		} catch (Exception e) {
			logger.error("=====请求微信HTTP异常:", e);
		}
		return null;
	}
	
	/**
	 * 创建微信的签名
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String createSign(Map<String, String> map) throws Exception {
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String k = it.next();
            String v = map.get(k);
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WeChatConstant.KEY);
        String sign = SecurityUtil.md5(sb.toString()).toUpperCase();
        return sign;
    }
	/*public static String createSign(Map<String, String> map) throws Exception {
        List<String> keys = new ArrayList<>( map.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String k = it.next();
            String v = map.get(k);
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" +v+ "&");
            }
        }
        sb.append("key=" + WeChatConstant.KEY);
        String sign = SecurityUtil.md5(sb.toString()).toUpperCase();
        return sign;
    }*/
	
	public static String createSignService(Map<String, String> map) throws Exception {
       return createSignService(map,"");
    }
	
	/**
	 * 签名 
	 * @param map
	 * @param key 默认用 KEY_SERVICE
	 * @return
	 * @throws Exception
	 */
	public static String createSignService(Map<String, String> map,String key) throws Exception {
        StringBuffer sb = new StringBuffer();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String k = it.next();
            String v = map.get(k);
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        if(StringUtils.isBlank(key)){
        	//为空的话 默认用 keyservice
        	key = WeChatConstant.KEY_SERVICE;
        }
        sb.append("key=" + key);
        String sign = SecurityUtil.md5(sb.toString()).toUpperCase();
        return sign;
    }
	
	/**
     * 随机算法
     * @return
     */
    protected static String create_nonce_str() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    public static String getOauth2(String redirect_uri){
    	return getOauth2(redirect_uri, null);
    }
    
    /**
     * 获取授权地址(该地址只能获取微信用户的openid)
     * @param redirect_uri 微信重定向请求的地址
     * @param state 微信回调后 携带的参数(可以不传)
     * @return
     */
    public static String getOauth2(String redirect_uri,String state){
    	StringBuilder sb = new StringBuilder();
    	sb.append(WeChatConstant.OAUTH2_URL);
    	sb.append("?appid=").append(WeChatConstant.APPPAY_APPID_SERVICE);
    	sb.append("&redirect_uri=").append(redirect_uri);
    	sb.append("&response_type=code");
    	sb.append("&scope=snsapi_base");
    	if(StringUtils.isNoneBlank(state)){
    		sb.append("&state=").append(state);
    	}else{
    		sb.append("&state=1");
    	}
    	sb.append("#wechat_redirect");
    	return sb.toString();
    }
    
    /**
     * 获取openid
     * @param code 微信重定向回来时候会携带
     * @return 失败或者没有获取到都返回的空,只有正确返回的不是空
     */
    public static String getOpenId(String code){
    	Map<String, Object> header = new HashMap<String, Object>();
    	header.put("appid", WeChatConstant.APPPAY_APPID_SERVICE);
    	header.put("secret", WeChatConstant.SECRET);
    	header.put("code", code);
    	header.put("grant_type","authorization_code");
    	try {
    		String result = HttpUtil.httpPost(WeChatConstant.ACCESS_TOKEN_URL, header);
    		if(StringUtils.isNotBlank(result)){
        		JSONObject jsons = JSON.parseObject(result);
        		if(jsons.containsKey("errcode")){
        			logger.error("获取微信openid失败,微信返回的结果:"+result);
        		}else{
        			return jsons.getString("openid");
        		}
        	}
		} catch (Exception e) {
			logger.error("获取微信openid异常:"+code,e);
		}
    	return "";
    }
    
    
    /**
     *	微信退款证书认证
     */
    public static String certPost(String url,String data,String certUrl) throws Exception{
    	KeyStore keyStore  = KeyStore.getInstance("PKCS12");
    	FileInputStream instream = new FileInputStream(new File(certUrl));
    	keyStore.load(instream, WeChatConstant.APPPAY_MCHID.toCharArray());
    	instream.close();
    	SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WeChatConstant.APPPAY_MCHID.toCharArray()).build();
    	SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,
    	SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    	CloseableHttpClient httpclient = HttpClients.custom() .setSSLSocketFactory(sslsf) .build();
        HttpPost httpost = new HttpPost(url); // 
        httpost.addHeader("Connection", "keep-alive");
        httpost.addHeader("Accept", "*/*");
        httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpost.addHeader("Host", "api.mch.weixin.qq.com");
        httpost.addHeader("X-Requested-With", "XMLHttpRequest");
        httpost.addHeader("Cache-Control", "max-age=0");
        httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        httpost.setEntity(new StringEntity(data, "UTF-8"));
        CloseableHttpResponse response = httpclient.execute(httpost);
        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils .toString(response.getEntity(), "UTF-8");
        EntityUtils.consume(entity);
        return jsonStr;
    }
}
