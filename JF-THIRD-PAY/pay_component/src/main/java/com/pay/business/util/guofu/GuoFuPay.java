package com.pay.business.util.guofu;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class GuoFuPay {
	private static Logger log = Logger.getLogger(GuoFuPay.class);
	
	/**
	 * 国付QQ被扫接口
	 * @param merchno 商户号
	 * @param amount 交易金额(元)
	 * @param traceno 商户流水号
	 * @param payType 支付方式(1支付宝、2微信、8-QQ)
	 * @param notifyUrl 通知地址(null)
	 * @param settleType 结算方式
	 * @param key 秘钥
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, String> passivePay(String merchno, String amount, String traceno, 
			String payType, String settleType, String key) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("merchno", merchno);
		param.put("amount", amount);
		param.put("traceno", traceno);
		param.put("payType", payType);
		param.put("notifyUrl", GuoFuConfig.NOTIFY_URL);
		//商品名称
		param.put("goodsName", merchno);
		param.put("settleType", settleType);
		try {
			//签名加密
			param.put("signature", signature(param, key, "GBK"));
			
			System.out.println("param：" + param.toString());
			log.info("=====>国付QQ被扫提交参数：\n" + param.toString());
			String url = GuoFuConfig.BASE_URL + GuoFuConfig.PASSIVEPAY;
		
			String doPost = doPost(url, param);
			Map<String, String> maps = (Map)JSON.parse(doPost);
			log.info("=====>国付QQ被扫返回的预支付订单信息：\n" + param.toString());
			if("00".equals(maps.get("respCode"))){
				resultMap.put("qr_code", maps.get("barCode"));
				resultMap.put("code","10000");
				log.info("=====>国付QQ被扫支付调起成功:qr_code : " + param.toString());
			}else {
				resultMap.put("code","10001");
				resultMap.put("msg", maps.get("message"));
				log.error("=====>国付QQ被扫支付失败原因:" + maps.get("message"));
			}
			
		} catch (Exception e) {
			resultMap.put("code","10001");
			resultMap.put("msg", e.getMessage());
			log.error("=====>国付QQ被扫支付失败原因:" + e.getMessage());
			e.printStackTrace();
		}
		
		return resultMap;
	 }
	
	/**
	 * 国付订单查询
	 * @param merchno 
	 * @param traceno 订单号
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> qrcodeQuery(String merchno, String traceno, String key){
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("merchno", merchno);
		param.put("traceno", traceno);
		try {
			//签名加密
			param.put("signature", signature(param, key, "GBK"));
			log.info("=====>国付订单查询参数：\n" + param.toString());
			String url = GuoFuConfig.BASE_URL + GuoFuConfig.QRCODEQUERY;
			String doPost = doPost(url, param);
			log.info("=====>国付订单查询结果：\n" + doPost);
			Map<String, String> maps = (Map)JSON.parse(doPost);
			if("1".equals(maps.get("respCode"))){
				resultMap.put("code","10000");
				resultMap.put("out_trade_no", traceno);
				resultMap.put("total_fee", maps.get("amount"));
				resultMap.put("transaction_id", maps.get("orderno"));
			}else {
				resultMap.put("code", "500");
				resultMap.put("msg", maps.get("message"));
				log.error("=====>国付订单查询失败原因:" + maps.get("message"));
			}
		} catch (Exception e) {
			resultMap.put("code", "500");
			log.error("=====>国付订单查询异常:" + e.getMessage());
		}
		return resultMap;
	}
	
	public static void main(String[] args) throws Exception {
		Map<String, String> passivePay = passivePay("820440348160001", "0.01", Long.toString(new Date().getTime()), "8", "1", "FBCA4448139ADBFC1E61E9556331DBAA");
		System.out.println(passivePay);
		//qrcodeQuery("820440348160001", "1503310296672", "FBCA4448139ADBFC1E61E9556331DBAA");
	}
	
	
	public static String signature(Map<String, String> param, String keyInfo, String encoding) throws Exception {
		Set<String> set = param.keySet();
		List<String> keys = new ArrayList<String>(set);
		Collections.sort(keys);
		boolean start = true;
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			String value = param.get(key);
			if (!key.equals("signature")) {
				if (!start) {
					sb.append("&");
				}
				sb.append(key + "=" + value);
				start = false;
			}
		}
		sb.append("&");
		sb.append(keyInfo);
		String src = sb.toString();
		return getMD5ofStr(src, encoding).toUpperCase();
	}
	
	private static String getMD5ofStr(String str, String encoding) throws Exception {
		MessageDigest alga = MessageDigest.getInstance("MD5");
		byte[] b = str.getBytes(encoding);
		alga.update(b);
		byte[] digesta = alga.digest();
		return byte2hex(digesta);
	}
	
	private static String byte2hex(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	/**
	 * post传递多个参数
	 */
	private static String doPost(String url,Map<String, String> params) throws Exception{
		String result=null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		if (params != null) {
			// 设置2个post参数
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				parameters.add(new BasicNameValuePair(key, (String) params
						.get(key)));
			}
			System.out.println("list：" + parameters);
			// 构造一个form表单式的实体
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "GBK");
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(formEntity);
		}
		try {
			response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "GBK");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
		}
		return result;
	}
}
