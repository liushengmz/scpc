
package com.system.util;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUtil
{

	/**
	 * 参数加密
	 * 
	 * @param map
	 * @param keyValue
	 *            商户密钥
	 * @return
	 * @throws Exception
	 */
	public static String getSign(Map<String, Object> map, String keyValue)
			throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("keyValue=" + keyValue + "&");
		buffer.append(getParamStr(map));
		String sNewString = getSign(buffer.toString().toUpperCase(), "MD5");
		return sNewString;

	}

	/**
	 * 参数签名串拼接
	 * 
	 * @param map
	 * @return
	 */
	public static String getParamStr(Map<String, Object> map)
	{
		StringBuffer buffer = new StringBuffer();
		List<String> keys = new ArrayList<String>(map.keySet());
		// 排序
		Collections.sort(keys);
		// 参数值拼接进行加密
		for (int i = 0; i < keys.size(); i++)
		{
			String key = keys.get(i);
			if (!"sign".equals(key) && !"keyValue".equals(key))
			{
				String value = map.get(key) == null ? ""
						: map.get(key).toString();
				if (i == 0)
				{
					buffer.append(key + "=" + value);
				}
				else
				{
					buffer.append("&" + key + "=" + value);
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 获取加密签名
	 * 
	 * @param str
	 *            字符
	 * @param type
	 *            加密类型
	 * @return
	 * @throws Exception
	 */
	public static String getSign(String str, String type) throws Exception
	{
		MessageDigest crypt = MessageDigest.getInstance(type);
		crypt.reset();
		crypt.update(str.getBytes("UTF-8"));
		return str = byteToHex(crypt.digest());
	}

	/**
	 * 
	 * @Title: byteToHex @Description: 字节转换 16 进制 @param @param
	 * hash @param @return 设定文件 @return String 返回类型 @throws
	 */
	private static String byteToHex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 验签
	 * 
	 * @param map
	 * @param keyValue
	 *            商户密钥
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSign(Map<String, Object> map, String keyValue)
			throws Exception
	{
		if (map.get("sign") == null)
		{
			return false;
		}
		// 密文
		String sign = map.get("sign").toString();
		map.remove("sign");
		String sNewString = getSign(map, keyValue);

		return sNewString.equals(sign);
	}

	public static void main(String[] args) throws Exception
	{
		test();
	}

	public static void test() throws Exception
	{
		Map<String, Object> map = new HashMap<String, Object>();

		// 支付签名
		map.put("payMoney", "10.86");
		map.put("bussOrderNum", "15031090042479");
		map.put("notifyUrl",
				"http://qiuguojie.wicp.net/payDetail/tfbCallBack.do");
		map.put("appKey", "69732c1213b42876511c543356bea0dd");
		map.put("orderName", "recharge");
		map.put("channel", 2);
		map.put("returnUrl", "http://172.16.200.94/wx/quzhifu/payReturn.php");
		map.put("notifyUrl", "http://172.16.200.94/wx/quzhifu/payNotice.php");
		map.put("remark", "15031090042479");

		String s = getSign(map, "40pW08gASyxd6IHHBsSAUVW6jnC1ZkQouUrwzZz1");

		String paramStr = getParamStr(map) + "&sign=" + s;

		System.out.println(paramStr);

		String encodeParamStr = URLEncoder.encode(paramStr, "UTF-8");

		System.out.println(
				"https://pay.iquxun.cn/aiJinFuPay/aliScanPay.do?paramStr="
						+ encodeParamStr);
	}

}
