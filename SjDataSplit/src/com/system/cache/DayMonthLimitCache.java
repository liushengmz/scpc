package com.system.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 日月限缓存数据
 * @author Administrator
 *
 */
public class DayMonthLimitCache
{
	private static Map<String, Float> spTroneDayLimit = new HashMap<String, Float>();
	private static Map<String, Float> spTroneMonthLimit = new HashMap<String, Float>();
	private static Map<String, Float> cpSpTroneDayLimit = new HashMap<String, Float>();
	private static Map<String, Float> cpSpTroneMonthLimit = new HashMap<String, Float>();
	
	public static void setSpTroneDayLimit(Map<String, Float> limit)
	{
		spTroneDayLimit = limit;
	}
	
	public static void setSpTroneMonthLimit(Map<String, Float> limit)
	{
		spTroneMonthLimit = limit;
	}
	
	public static void setCpSpTroneDayLimit(Map<String, Float> limit)
	{
		cpSpTroneDayLimit = limit;
	}
	
	public static void setCpSpTroneMonthLimit(Map<String, Float> limit)
	{
		cpSpTroneMonthLimit = limit;
	}
	
	public static void addToLimit(int spTroneId,int cpId,Float money,String curDate,String curMonth)
	{
		String spTroneMonthKey = spTroneId + "-" + curMonth;
		String spTroneDayKey = spTroneId + "-" + curDate;
		String cpSpTroneMonthKey = cpId + "-" + spTroneId + "-" + curMonth;
		String cpSpTroneDayKey = cpId + "-" + spTroneId + "-" + curDate;
		
		Float spTroneMonthMoney = spTroneMonthLimit.get(spTroneMonthKey);
		Float spTroneDayMoney = spTroneDayLimit.get(spTroneDayKey);
		Float cpSpTroneMonthMoney = cpSpTroneMonthLimit.get(cpSpTroneMonthKey);
		Float cpSpTroneDayMoney = cpSpTroneDayLimit.get(cpSpTroneDayKey);
		
		spTroneMonthMoney = spTroneMonthMoney==null ? money : spTroneMonthMoney + money;
		spTroneDayMoney = spTroneDayMoney==null ? money : spTroneDayMoney + money;
		cpSpTroneMonthMoney = cpSpTroneMonthMoney==null ? money : cpSpTroneMonthMoney + money;
		cpSpTroneDayMoney = cpSpTroneDayMoney==null ? money : cpSpTroneDayMoney + money;
		
		spTroneMonthLimit.put(spTroneMonthKey, spTroneMonthMoney);
		spTroneDayLimit.put(spTroneDayKey, spTroneDayMoney);
		cpSpTroneMonthLimit.put(cpSpTroneMonthKey,cpSpTroneMonthMoney);
		cpSpTroneDayLimit.put(cpSpTroneDayKey,cpSpTroneDayMoney);
	}
	
	public static Float getCurSpTroneDayLimit(int spTroneId,String date)
	{
		for(String key : spTroneDayLimit.keySet())
		{
			if((spTroneId + "-" + date).equalsIgnoreCase(key))
			{
				return spTroneDayLimit.get(key);
			}
		}
		return 0.0F;
	}
	
	public static Float getCurSpTroneMonthLimit(int spTroneId,String month)
	{
		for(String key : spTroneMonthLimit.keySet())
		{
			if((spTroneId + "-" + month).equalsIgnoreCase(key))
			{
				return spTroneMonthLimit.get(key);
			}
		}
		return 0.0F;
	}
	
	public static Float getCurCpSpTroneDayLimit(int cpId,int spTroneId,String date)
	{
		for(String key : cpSpTroneDayLimit.keySet())
		{
			if((cpId + "-" + spTroneId + "-" + date).equalsIgnoreCase(key))
			{
				return cpSpTroneDayLimit.get(key);
			}
		}
		return 0.0F;
	}
	
	public static Float getCurCpSpTroneMonthLimit(int cpId,int spTroneId,String month)
	{
		for(String key : cpSpTroneMonthLimit.keySet())
		{
			if((cpId + "-" + spTroneId + "-" + month).equalsIgnoreCase(key))
			{
				return cpSpTroneMonthLimit.get(key);
			}
		}
		return 0.0F;
	}
	
	public static StringBuffer loadDayMonthLimit()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<table border='1' style='text-align:center;'>");
		
		sb.append("<tr><td colspan='5'>SP业务当前月限</td></tr><tr>");
		
		int i= 1;
		
		float totalMoney = 0F;
		float money = 0F;
		
		for(String key : spTroneMonthLimit.keySet())
		{
			money = spTroneMonthLimit.get(key);
			
			totalMoney += money;
			
			sb.append("<td>" +  key + ":" + money + "</td>");
			
			if(i%5==0)
				sb.append("</tr><tr>");
			
			i++;
		}
		
		sb.append("<td>Total:" + totalMoney + "</td></tr><tr><td colspan='5'>SP业务当前日限</td></tr><tr>");
		
		i= 1;
		
		for(String key : spTroneDayLimit.keySet())
		{
			sb.append("<td>" + key + ":" + spTroneDayLimit.get(key) + "</td>");
			
			if(i%5==0)
				sb.append("</tr><tr>");
			
			i++;
		}
		
		sb.append("</tr><tr><td colspan='5'>CP业务当前月限</td></tr><tr>");
		
		i= 1;
		
		for(String key : cpSpTroneMonthLimit.keySet())
		{
			sb.append("<td>" + key + ":" + cpSpTroneMonthLimit.get(key) + "</td>");
			
			if(i%5==0)
				sb.append("</tr><tr>");
			
			i++;
		}
		
		sb.append("</tr><tr><td colspan='5'>CP业务当前日限</td></tr><tr>");
		
		i= 1;
		
		for(String key : cpSpTroneDayLimit.keySet())
		{
			sb.append("<td>" + key + ":" + cpSpTroneDayLimit.get(key) + "</td>");
			
			if(i%5==0)
				sb.append("</tr><tr>");
			
			i++;
		}
		
		sb.append("</tr></table>");
		
		return sb;
	}
}
