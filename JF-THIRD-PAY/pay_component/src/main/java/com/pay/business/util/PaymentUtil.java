package com.pay.business.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.core.teamwork.base.util.http.HttpUtil;
/**
 * 
 * @ClassName: PaymentUtil 
 * @Description:加密工具类
 * @author zhoulibo
 * @date 2017年3月1日 下午5:12:11
 */
public class PaymentUtil {
	
	/**
	 * 参数加密
	 * @param map
	 * @param keyValue  商户密钥
	 * @return
	 * @throws Exception 
	 */
	public static String getSign(Map<String,Object> map,String keyValue) throws Exception{
		//密文
		StringBuffer buffer = new StringBuffer();
		buffer.append("keyValue=" + keyValue);
		
		List<String> keys = new ArrayList<String>(map.keySet());
		//排序
        Collections.sort(keys);
		//参数值拼接进行加密
		for(String key :keys){
			
			if(!"sign".equals(key)&&!"keyValue".equals(key)){
				String value = map.get(key)==null?"":map.get(key).toString();
				buffer.append("&"+key + "=" + value);
			}
		}
		//System.out.println(buffer.toString());
		String sNewString = getSign(buffer.toString().toUpperCase(), "MD5");
		
		return sNewString;
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
	
	public static void main(String[] args) throws Exception {
		Map<String,Object> map = new HashMap<>();
		
		Integer code = HttpUtil.getCode("http://192.168.1.172:8081/jinfuOrder/testCallback.do", map);
		System.out.println(code);
	}
}
