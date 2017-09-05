package com.pay.business.util.minshengbank;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

public class MinShengBankSignUtil {

	
	/**
	 * 参数加密
	 * @param map
	 * @param keyValue  商户密钥
	 * @return
	 * @throws Exception 
	 */
	public static String getSign(Map<String,Object> map,String keyValue) throws Exception{
		StringBuffer buffer = new StringBuffer();
		buffer.append(getParamStr(map));
		String sNewString = getSign(buffer.toString()+keyValue, "MD5");
		
		return sNewString.toUpperCase();
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
			if(!"signIn".equals(key)){
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
	 * @param bankSecretKey  商户密钥
	 * @return
	 * @throws Exception 
	 */
	public static boolean verifySign(Map<String,Object> map,String bankSecretKey) throws Exception{
		if(map.get("signIn")==null){
			return false;
		}
		//密文
		String sign = map.get("signIn").toString();
		map.remove("signIn");
		String sNewString = getSign(map, bankSecretKey);
		
		return sNewString.equalsIgnoreCase(sign);
	}
	
	public static void main(String[] args) throws Exception {
//		Map<String,Object> map = new HashMap<>();
		
		//支付签名
//		map.put("payMoney", "21.0");
//		map.put("bussOrderNum", "29318187382785");
//		map.put("notifyUrl", "http://api.aijinfu.cn/jinfuOrder/payLifeCallBack.do");
//		map.put("appKey", "270461df13a412f373ae6c2771ccd926");//内网appKey
//		//map.put("appKey", "234e74508bfcc3cdf5545906320aeb2b");//外网appKey
//		map.put("orderName", "深圳市燃气集团股份有限公司 煤气费 1282201884");
//		map.put("returnUrl", "");
//		//map.put("orderDescribe", "该测试商品的详细描述");
////		map.put("sign", "387ca231498d271b4281dcb037630767");
//		String s =getSign(map,"be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b");//内网密钥
//		//String s =getSign(map,"5abc744cf47f1a40595331e1d6f616e799bd30aa2344bc08db9e77b25f1d4e04");//外网密钥
////		String str = URLEncoder.encode(getParamStr(map)+"&"+"sign="+s);
//		System.out.println(s);
////		str =URLDecoder.decode(str);
////		System.out.println(str);
	}
}
