package com.system.server;

import java.util.Map;

import com.system.dao.CpAppDao;
import com.system.model.CpAppModel;


public class CpAppServer {
	public Map<String, Object> loadCpApp(int pageIndex)
	{
		return new CpAppDao().loadCpAppByPageindex(pageIndex);
	}
	
	public Map<String, Object> loadCpApp(int pageIndex,String appname,String startDate,String endDate)
	{
		return new CpAppDao().loadCpApp(pageIndex, appname, startDate, endDate);
	}
	
	public boolean addCpApp(CpAppModel model)
	{
		return new CpAppDao().addCpApp(model);
	}
	
	public boolean deletCpApp(int id)
	{
		return new CpAppDao().deletCpApp(id);
	}
	
	public CpAppModel loadCpAppById(int id)
	{
		return new CpAppDao().loadCpAppById(id);
	}
	
	public boolean updateCpApp(CpAppModel model)
	{
		return new CpAppDao().updateCpApp(model);
	}
	
}
