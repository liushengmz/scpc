
package com.pay.business.util.swt;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SwtPayUtil
{
	private static String	hexStr		= "0123456789ABCDEF";
	
	/**
	 * 
	 * @param bytes
	 * @return 将二进制转换为十六进制字符输出
	 */
	private static String BinaryToHexString(byte[] bytes)
	{
		
		String result = "";
		
		String hex = "";
		
		for (int i = 0; i < bytes.length; i++)
		{
			// 字节高4位
			hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
			
			// 字节低4位
			hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
			
			result += hex + "";
		}
		return result;
	}
	
	public static String stringToHexString(String oriValue)
	{
		if(oriValue==null || "".equals(oriValue))
			return "";
		
		try
		{
			return BinaryToHexString(oriValue.getBytes());
		}
		catch(Exception ex)
		{
			System.out.println("stringToHexString Error:" + ex.getMessage());
		}
		
		return "";
	}
	
	public static String MD5(String inStr)
	{
		MessageDigest md5 = null;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
		{
			byteArray[i] = (byte) charArray[i]; // 此处有bug，中文是强转，会受损
		}
		byte[] md5Bytes = md5.digest(byteArray);// byte[] md5Bytes =
												// md5.digest(inStr.getBytes()
												// );

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++)
		{
			int val = md5Bytes[i] & 0xFF;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> toMap(String xml)
	{
		Map<String, String> map = new HashMap<String, String>();
		
		try
		{
			Document doc = DocumentHelper.parseText(xml);
			
			Element root = doc.getRootElement();
			
			List<org.dom4j.tree.DefaultAttribute> rootAttrs = root.attributes();
			
			for(org.dom4j.tree.DefaultAttribute attr : rootAttrs)
			{
				map.put(attr.getName(), attr.getValue());
			}
			
			List<Element> elements = root.elements();
			
			for (Element element : elements)
			{
				map.put(element.getName(), String.valueOf(element.getData()));
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return map;
	}
	
	public static void main(String[] args)
	{
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><settle retCode=\"0001\" retMsg=\"交易成功\" attach=\"-1\"><qrURL>weixin://wxpay/bizpayurl?pr=lLvj1uG</qrURL><codeUrl>weixin://wxpay/bizpayurl?pr=lLvj1uG</codeUrl></settle>";
		
		//xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><orderPkg retCode=\"0002\" retMsg=\"验签失败\" attach=\"\"/>";
		
		Map<String, String> map = toMap(xml);
		
		System.out.println(map);
	}
}
