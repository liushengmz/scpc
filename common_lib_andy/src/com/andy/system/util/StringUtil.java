
package com.andy.system.util;

import java.security.MessageDigest;

import org.apache.log4j.Logger;

public class StringUtil
{
	private static Logger logger = Logger.getLogger(StringUtil.class);

	public static boolean isNullOrEmpty(String source)
	{
		return source == null || "".equals(source);
	}

	public static String getString(String str, String defaultValue)
	{
		return isNullOrEmpty(str) ? defaultValue : str;
	}

	public static int getInteger(String str, int defaultValue)
	{
		try
		{
			return Integer.parseInt(str);
		}
		catch (Exception ex)
		{
			
		}
		return defaultValue;
	}

	public static long getLong(String str, long defaultValue)
	{
		try
		{
			return Long.parseLong(str);
		}
		catch (Exception ex)
		{

		}
		return defaultValue;

	}

	public static Float getFloat(String str, float defaultValue)
	{
		try
		{
			return Float.parseFloat(str);
		}
		catch (Exception ex)
		{

		}
		return defaultValue;
	}

	/**
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static Double getDouble(String str, double defaultValue)
	{
		try
		{
			return Double.valueOf(str);
		}
		catch (Exception ex)
		{

		}
		return defaultValue;
	}

	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static String bytesToHex(byte[] bytes)
	{
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++)
		{
			t = bytes[i];
			if (t < 0)
				t += 256;
			sb.append(hexDigits[(t >>> 4)]);
			sb.append(hexDigits[(t % 16)]);
		}
		return sb.toString();
	}

	public static String getMd5String(String input, int bit)
	{
		try
		{
			if (StringUtil.isNullOrEmpty(input))
				return "";

			MessageDigest md = MessageDigest
					.getInstance(System.getProperty("MD5.algorithm", "MD5"));

			if (bit == 32)
				return bytesToHex(md.digest(input.getBytes("utf-8")));

			if (bit == 24)
				return bytesToHex(md.digest(input.getBytes("utf-8")))
						.substring(0, 24);

			if (bit == 16)
				return bytesToHex(md.digest(input.getBytes("utf-8")))
						.substring(8, 24);

			if (bit == 8)
			{
				return bytesToHex(md.digest(input.getBytes("utf-8")))
						.substring(8, 16);
			}

			return bytesToHex(md.digest(input.getBytes("utf-8")));
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		return "";
	}

	public static String mergerStrings(String[] oris, String splitor)
	{
		if (oris == null)
			return "";

		if (oris.length == 1)
			return oris[0];

		String values = "";

		for (String s : oris)
			values += s + splitor;

		values = values.substring(0, values.length() - 1);

		return values;
	}

}
