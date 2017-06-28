package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.AdCpDao;
import com.system.model.AdCpModel;

public class AdCpServer {
	public Map<String, Object> loadCpQd(int pageIndex,int id)
	{
		return new AdCpDao().loadCp(pageIndex, id);
	}
	
	public Map<String, Object> loadCpQd(int pageIndex,int id,String startdate,String enddate)
	{
		return new AdCpDao().loadCp(pageIndex, id, startdate, enddate);
	}
}
