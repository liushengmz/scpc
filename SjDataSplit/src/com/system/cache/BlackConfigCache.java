package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.system.util.StringUtil;

public class BlackConfigCache
{
	private static Logger logger = Logger.getLogger(BlackConfigCache.class); 
	
	private static List<String> blackPhoneList = new ArrayList<String>();
	
	private static List<String> blackImsiList = new ArrayList<String>();
	
	private static List<String> blackImeiList = new ArrayList<String>();
	
	
	public static boolean isInBlack(String phone,String imsi,String imei)
	{
		if(StringUtil.isNullOrEmpty(phone) || StringUtil.isNullOrEmpty(imsi) || StringUtil.isNullOrEmpty(imei))
		{
			return false;
		}
		
		if(!StringUtil.isNullOrEmpty(phone))
		{
			for(String strPhone : blackPhoneList)
			{
				if(phone.equalsIgnoreCase(strPhone))
				{
					logger.info("black phone num:" + strPhone);
					
					return true;
				}
			}
		}
		
		if(!StringUtil.isNullOrEmpty(imsi))
		{
			for(String strImsi : blackImsiList)
			{
				if(imsi.equalsIgnoreCase(strImsi))
				{
					logger.info("black imsi:" + imsi);
					
					return true;
				}
			}
		}
		
		if(!StringUtil.isNullOrEmpty(imei))
		{
			for(String strImei : blackImeiList)
			{
				if(imsi.equalsIgnoreCase(strImei))
				{
					logger.info("black imei:" + imei);
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void setBlackPhoneList(List<String> list)
	{
		blackPhoneList = list; 
	}
	
	public static void setBlackImsiList(List<String> list)
	{
		blackImsiList = list;
	}
	
	public static void setBlackImeiList(List<String> list)
	{
		blackImeiList = list;
	}
	
}
