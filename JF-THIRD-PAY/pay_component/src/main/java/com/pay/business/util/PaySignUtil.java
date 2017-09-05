package com.pay.business.util;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.core.teamwork.base.util.http.HttpUtil;
import com.pay.business.util.httpsUtil.HttpsUtil;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;

public class PaySignUtil {
	private static final Log logger = LogFactory.getLog(PaySignUtil.class);
	
	/**
	 * 参数加密
	 * @param map
	 * @param keyValue  商户密钥
	 * @return
	 * @throws Exception 
	 */
	public static String getSign(Map<String,Object> map,String keyValue) throws Exception{
		StringBuffer buffer = new StringBuffer();
		buffer.append("keyValue=" + keyValue+"&");
		buffer.append(getParamStr(map));
		//System.out.println(buffer.toString());
		String sNewString = getSign(buffer.toString().toUpperCase(), "MD5");
		
		return sNewString;
	}
	
	/**
	 * 参数签名串拼接
	 * @param map
	 * @return
	 */
	public static String getParamStr(Map<String,Object> map){
		StringBuffer buffer = new StringBuffer();
		List<String> keys = new ArrayList<String>(map.keySet());
		//排序
        Collections.sort(keys);
		//参数值拼接进行加密
        for (int i = 0; i < keys.size(); i++) {
        	String key = keys.get(i);
			if(!"sign".equals(key)&&!"keyValue".equals(key)){
				String value = map.get(key)==null?"":map.get(key).toString();
				if(i==0){
					buffer.append(key + "=" + value);
				}else{
					buffer.append("&"+key + "=" + value);
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 获取加密签名
	 * @param str 字符
	 * @param type 加密类型
	 * @return 
	 * @throws Exception
	 */
	public static String getSign(String str, String type) throws Exception {
		MessageDigest crypt = MessageDigest.getInstance(type);
		crypt.reset();
		crypt.update(str.getBytes("UTF-8"));
		return str = byteToHex(crypt.digest());
	}
	
	/**
	 * 
	* @Title: byteToHex 
	* @Description: 字节转换 16 进制
	* @param @param hash
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	/**
	 * 验签
	 * @param map
	 * @param keyValue  商户密钥
	 * @return
	 * @throws Exception 
	 */
	public static boolean checkSign(Map<String,Object> map,String keyValue) throws Exception{
		if(map.get("sign")==null){
			return false;
		}
		//密文
		String sign = map.get("sign").toString();
		map.remove("sign");
		String sNewString = getSign(map, keyValue);
		
		return sNewString.equals(sign);
	}
	
	public static void main(String[] args) throws Exception 
	{
		//微信公众号内支付
		//wxGzhPay();
		//平安特殊微信公众号支付
		paSpecialPay();
		//支付宝微信扫码支付
		//alipyWxScan(2);
	}
	
	//微信公众号内支付
	private static void wxGzhPay()
	{
		Map<String,Object> map = new HashMap<>();
		
		//支付签名
		map.put("payMoney", "0.06");
		map.put("bussOrderNum", System.currentTimeMillis());
		map.put("notifyUrl", "http://qiuguojie.wicp.net/payDetail/tfbCallBack.do");
		map.put("appKey", "8490d1263f4e30c3502eb138705bc272");
		map.put("orderName", "别克2部");
		map.put("ip", "192.168.1.172");
		map.put("appType", 1);
		map.put("payPlatform", 2);
		map.put("returnUrl", "http://qiuguojie.wicp.net/payDetail/tfbCallBack.do");
		//map.put("payPlatform", 2);
		String s = "";
		try
		{
			s = getSign(map,"IxRsjdpKLEn9oml1ikixuKJItRygrsar0A1C6tV1");
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		String paramStr = getParamStr(map)+"&sign="+s;
		
		
		String  encodeParamStr = URLEncoder.encode(paramStr);
		
		String url = "https://pay.iquxun.cn/GateWay/wxGzhPay.do";
		
		System.out.println(url + "?paramStr="+encodeParamStr);
		
		//System.out.println(URLEncoder.encode(url + "?paramStr=" + paramStr));
		
		map.clear();
		
		map.put("paramStr", paramStr);
		
		//System.out.println("post return:" + HttpsUtil.doPostString(url, map, "UTF-8"));
		
		
	}
	
	//支付宝微信扫码支付 1ZFB 2WX
	private static void alipyWxScan(int channel)
	{
		Map<String, Object> map = new HashMap<>();

		// 支付签名
		map.put("payMoney", "0.03");
		map.put("bussOrderNum", System.currentTimeMillis());
		map.put("notifyUrl",
				"http://qiuguojie.wicp.net/payDetail/tfbCallBack.do");
		map.put("appKey", "8b1015b360a5936fd76e2447da095a24");
		map.put("orderName", "别克1部");
		map.put("ip", "192.168.1.172");
		map.put("channel", channel);
		// map.put("appType", 1);
		map.put("returnUrl",
				"http://qiuguojie.wicp.net/payDetail/tfbCallBack.do");
		String s = "";
		try
		{
			s = getSign(map, "F776tcz4cJ0aXh0S7a6hjAmCFLpeMx144cpatQih");

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		String paramStr = getParamStr(map) + "&sign=" + s;

		String encodeParamStr = URLEncoder.encode(paramStr);

		String url = "https://pay.iquxun.cn/aiJinFuPay/aliScanPay.do";

		System.out.println(url + "?paramStr=" + encodeParamStr);

		// System.out.println(URLEncoder.encode(url + "?paramStr=" + paramStr));

		map.clear();

		map.put("paramStr", paramStr);

		System.out.println("post return:" + HttpsUtil.doPostString(url,map,"UTF-8"));

	}
	
	//8490d1263f4e30c3502eb138705bc272
	//IxRsjdpKLEn9oml1ikixuKJItRygrsar0A1C6tV1
	
	//平安特殊微信公众号支付
	private static void paSpecialPay()
	{
		Map<String, Object> map = new HashMap<>();

		// 支付签名
		map.put("payMoney", "0.01");
		map.put("bussOrderNum", System.currentTimeMillis());
		map.put("notifyUrl",
				"http://qiuguojie.wicp.net/payDetail/tfbCallBack.do");
		map.put("appKey", "8490d1263f4e30c3502eb138705bc272");
		map.put("orderName", "BIEKEYIBU");
		map.put("ip", "192.168.1.172");
		map.put("channel", 2);
		//map.put("appType", 1);
		map.put("returnUrl", "http://www.sznews.com/news/content/2017-09/05/content_17194075_3.htm");
		String s = "";
		try
		{
			s = getSign(map, "IxRsjdpKLEn9oml1ikixuKJItRygrsar0A1C6tV1");

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		String paramStr = getParamStr(map) + "&sign=" + s;

		String encodeParamStr = URLEncoder.encode(paramStr);

		//String url = "https://pay.iquxun.cn/aiJinFuPay/aliScanPay.do";
		String url = "http://127.0.0.1:8080/pay_website/aiJinFuPay/aliScanPay.do";

		System.out.println(url + "?paramStr=" + encodeParamStr);

		//System.out.println(URLEncoder.encode(url + "?paramStr=" + paramStr));

		map.clear();

		map.put("paramStr", paramStr);

		//System.out.println("post return:" + HttpsUtil.doPostString(url, map,"UTF-8"));

	}
	
	/**
	 * 
	 * HTTP协议POST请求添加参数的封装方�?
	 */
	public static String getParamStr(TreeMap<String, String> paramsMap) {
		StringBuilder param = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> it = paramsMap.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, String> e = it.next();
			param.append("&").append(e.getKey()).append("=")
					.append(e.getValue());
		}
		return param.toString().substring(1);
	}
	
}
