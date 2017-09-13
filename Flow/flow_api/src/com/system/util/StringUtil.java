package com.system.util;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;


public class StringUtil 
{
	Logger logger = Logger.getLogger(StringUtil.class);
	
	public static boolean isNullOrEmpty(String source)
	{
		return source==null || "".equals(source);
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
	
	public static long getLong(String str ,long defaultValue)
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
	
	private final static char[]	hexDigits	= { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

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
			if(StringUtil.isNullOrEmpty(input))
				return "";
			
			MessageDigest md = MessageDigest.getInstance(System.getProperty(
					"MD5.algorithm", "MD5"));
			
			if (bit == 32)
				return bytesToHex(md.digest(input.getBytes("utf-8")));
			
			
			if(bit==24)
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
			
		}
		return "";
	}
	
	public static String getDefaultDate()
	{
		return sdf1.format(new Date());
	}
	
	public static String getMonthHeadDate()
	{
		Calendar ca = Calendar.getInstance();
		
		ca.set(Calendar.DAY_OF_MONTH, 1);
		
		return sdf1.format(ca.getTime());
	}
	
	public static String getMonthEndDate()
	{
		Calendar ca = Calendar.getInstance();
		
		ca.set(Calendar.DAY_OF_MONTH, 1);
		
		ca.add(Calendar.MONTH, 1);
		
		ca.add(Calendar.DAY_OF_YEAR, -1);
		
		return sdf1.format(ca.getTime());
	}
	
	public static String getDateFormat(Date date)
	{
		return sdf1.format(date);
	}
	
	public static String getMonthFormat(Date date)
	{
		return sdf2.format(date);
	}
	
	public static String getPreDayOfMonth()
	{
		Calendar ca = Calendar.getInstance();
		
		ca.add(Calendar.DAY_OF_MONTH, -1);
		
		return sdf1.format(ca.getTime());
	}
	
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
	private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//传入 yyyy-MM-dd 的格式 传回 yyyyMM 的格式
	public static String getMonthFormat(String date)
	{
		if(isNullOrEmpty(date))
			return getMonthFormat();
		
		try{ return sdf2.format(sdf1.parse(date));}
		catch(Exception ex){}
		
		return sdf2.format(new Date());
	}
	
	public static  String getMonthFormat()
	{
		return sdf2.format(new Date());
	}
	
	public static String getNowFormat()
	{
		return sdf3.format(new Date());
	}
	
	private static DecimalFormat df1 = new DecimalFormat("###0.00%");
	private static DecimalFormat df2 = new DecimalFormat("###0.00");
	
	public static String getPercent(int data1,int data2)
	{
		data2 = data2==0 ? 1 : data2;
		return df1.format((float)data1/(float)data2);
	}
	
	public static String getDecimalFormat(float data)
	{
		return df2.format(data);
	}
	
	public static String getDecimalFormat(double data)
	{
		return df2.format(data);
	}
	
	
	
	public static String mergerStrings(String[] oris,String splitor)
	{
		if(oris==null)
			return "";
		
		if(oris.length==1)
			return oris[0];
		
		String values = "";
		
		for(String s : oris)
			values += s + splitor;
		
		values = values.substring(0,values.length()-1);
			
		return values;
	}
	
	public static String getJsonFormObject(Object obj)
	{
		try
		{
			return JSONObject.fromObject(obj).toString();
		}
		catch(Exception ex)
		{
			
		}
		return "";
	}
	
}
