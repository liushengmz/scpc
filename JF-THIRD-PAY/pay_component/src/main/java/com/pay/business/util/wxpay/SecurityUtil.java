package com.pay.business.util.wxpay;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.UUID;

/**
 * 加密工具类
 * @ClassName: SecurityUtil 
 * @Description: 
 * @author yangyu
 * @date 2016年11月8日 下午4:02:51
 */
public class SecurityUtil {

	private static final String uuid = "4c7a37a8-b8d7-442f-b9df-31ec4bcacebc";
	
	/**
	 * 获取 md5 加密字符串
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String md5(String str) throws Exception {
		return getSign(str,"MD5");
	}
	
	/**
	 * 获取 md5 加密字符串
	 * 规则 str+uuid
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String md5x(String str) throws Exception {
		return getSign(str+uuid,"MD5");
	}

	/**
	 * 获取 sha1 加密字符串
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String sha1(String str) throws Exception {
		return getSign(str,"SHA-1");
	}
	
	/**
	 * 获取 sha1 加密字符串
	 * 二次加密
	 * 规则 str+uuid
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String sha1x(String str) throws Exception {
		return getSign(str+uuid,"SHA-1");
	}
	
	/**
	 * 获取 sha1进行加密的字符后再进行 md5  加密字符串
	 * 二次加密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String md5_sha1(String str) throws Exception {
		return  getSign(getSign(str,"MD5"),"SHA-1");
	}
	
	/**
	 * 获取 md5 进行加密的字符后再进行 sha1加密字符串
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String sha1_md5(String str) throws Exception {
		return getSign(getSign(str,"SHA-1"),"MD5");
	}

	/**
	 * 创建随机字符
	 * @return
	 */
	public static String createNonceStr() {
		return UUID.randomUUID().toString();
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
	 * 创建时间戳
	 * @return
	 */
	public static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	/**
	 * 字节转换 16 进制
	 * @param hash
	 * @return
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

}
