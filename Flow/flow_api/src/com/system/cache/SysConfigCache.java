package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.model.SysCodeModel;
import com.system.smodel.ConfigPropertiesModel;

public class SysConfigCache
{
	private static List<SysCodeModel> sysCodeCache = new ArrayList<SysCodeModel>();
	
	private static List<ConfigPropertiesModel> configPropertiesList = new ArrayList<ConfigPropertiesModel>();
	
	/**
	 * 根据CODE返回中文说明
	 * @param resultCode
	 * @return
	 */
	public static SysCodeModel loadResultCodeByFlag(int resultCode)
	{
		for(SysCodeModel result : sysCodeCache)
		{
			if(result.getFlag() == resultCode)
				return result;
		}
		
		return null;
	}
	
	/**
	 * 获取系统的配置参数
	 * @param type
	 * @param key
	 * @return
	 */
	public static String getConfigFromSys(int type,String key)
	{
		for(ConfigPropertiesModel config : configPropertiesList)
		{
			if(config.getType()==type && config.getKey().equalsIgnoreCase(key))
			{
				return config.getValue();
			}
		}
		
		return null;
	}
	
	protected static void setSysCodeList(List<SysCodeModel> list)
	{
		sysCodeCache = list;
	}
	
	protected static void setConfigPropertiesList(List<ConfigPropertiesModel> list)
	{
		configPropertiesList = list;
	}
	
	
	
}
