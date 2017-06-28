package com.system.server;

import java.util.Map;

import com.system.dao.ApDAO;
import com.system.model.ApModel;

public class TestApServer {
	public Map<String, Object> loadAp(int pageIndex)
	{
		return new ApDAO().loadSp(pageIndex);
	}
	
	public ApModel loadApById(int id)
	{
		return new ApDAO().loadApById(id);
	}
	
	public boolean deletById(int id)
	{
		return new ApDAO().deletAp(id);
	}
	
	public boolean updateAp(ApModel model)
	{
		return new ApDAO().updateAp(model);
	}
	
	public boolean addAp(ApModel model)
	{
		return new ApDAO().addAp(model);
	}
}
