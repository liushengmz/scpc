
package com.andy.system.util;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static boolean getBoolean(String str,boolean defaultValue)
	{
		if(isNullOrEmpty(str))
			return defaultValue;
		
		if("true".equalsIgnoreCase(str.trim()))
			return true;
		
		if("false".equalsIgnoreCase(str.trim()))
			return false;
		
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
	
	/**
	 * 下划线字符串转驼峰标识字符串
	 * @param line
	 * @param smallCamel 是否小驼峰
	 * @return
	 */
	public static String underlineToCamel(String line, boolean smallCamel)
	{
		if(isNullOrEmpty(line))
			return "";

		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find())
		{
			String word = matcher.group();
			sb.append(smallCamel && matcher.start() == 0
					? Character.toLowerCase(word.charAt(0))
					: Character.toUpperCase(word.charAt(0)));
			int index = word.lastIndexOf('_');
			if (index > 0)
			{
				sb.append(word.substring(1, index).toLowerCase());
			}
			else
			{
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}
	
	private static final char UNDERLINE = '_';

	public static String camelToUnderline(String param)
	{
		if (isNullOrEmpty(param))
		{
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
		{
			char c = param.charAt(i);
			if (Character.isUpperCase(c))
			{
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			}
			else
			{
				sb.append(c);
			}
		}
		return sb.toString();
	} 
	
	public static String concatDbQuerySql(String[] columns,String keyWord)
	{
		String sql = "AND (";
		
		if(columns==null || columns.length==0 || isNullOrEmpty(keyWord))
			return "";
		
		keyWord = SqlUtil.sqlEncode(keyWord);
		
		for(String column : columns)
		{
			sql += column + " LIKE '%" + keyWord + "%'" + " OR ";
		}
		
		sql = sql.substring(0, sql.length() - 4) + ")";
		
		return sql;
	}
	
	public static void main(String[] args)
	{
		String[] columns = {"nick_name","qq","mail"};
		String keyWord = "Andy";
		System.out.println(concatDbQuerySql(columns, keyWord));
	}

}
