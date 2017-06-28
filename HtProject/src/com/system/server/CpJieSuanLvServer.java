package com.system.server;

import java.util.Map;

import com.system.dao.CpJieSuanLvDao;

public class CpJieSuanLvServer
{
	public Map<String, Object> loadJieSuanLv(int cpId,int spId,int pageIndex)
	{
		return new CpJieSuanLvDao().loadJieSuanLv(cpId, spId, pageIndex);
	}
	
	public void updateJieSuandLv(int id,float value)
	{
		new CpJieSuanLvDao().updateJieSuandLv(id, value);
	}
}
