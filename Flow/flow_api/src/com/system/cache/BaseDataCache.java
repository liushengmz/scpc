package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.model.CpModel;

public class BaseDataCache
{
	private static List<CpModel> cpCache = new ArrayList<CpModel>();
	
	protected static void setCpCache(List<CpModel> cpList)
	{
		cpCache = cpList;
	}
	
	public static CpModel loadCpById(int cpId)
	{
		for(CpModel model : cpCache)
		{
			if(model.getCpId() == cpId)
				return model;
		}
		return null;
	}
}
