package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.CpPushUrlDao;
import com.system.model.CpPushUrlModel;

public class CpPushUrlServer
{
	public List<CpPushUrlModel> loadcpPushUrl()
	{
		return new CpPushUrlDao().loadcpPushUrl();
	}
	
	public Map<String, Object> loadCpPushUrl(int cpId,int pageIndex)
	{
		return new CpPushUrlDao().loadCpPushUrl(cpId, pageIndex);
	}
	
	public CpPushUrlModel loadCpPushUrlModelById(int id)
	{
		return new CpPushUrlDao().loadCpPushUrlModelById(id);
	}
	
	public boolean addCpPushUrl(CpPushUrlModel model)
	{
		return new CpPushUrlDao().addCpPushUrl(model);
	}
	
	public boolean updateCpPushUrl(CpPushUrlModel model)
	{
		return new CpPushUrlDao().updateCpPushUrl(model);
	}
	
}
