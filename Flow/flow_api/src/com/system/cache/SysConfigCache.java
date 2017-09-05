package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.model.SysCodeModel;
import com.system.smodel.ConfigPropertiesModel;

public class SysConfigCache
{
	private static List<SysCodeModel> sysCodeCache = new ArrayList<SysCodeModel>();
	
	private static List<ConfigPropertiesModel> configPropertiesList = new ArrayList<ConfigPropertiesModel>();
	
	public static SysCodeModel loadResultCodeByFlag(int resultCode)
	{
		for(SysCodeModel result : sysCodeCache)
		{
			if(result.getFlag() == resultCode)
				return result;
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
