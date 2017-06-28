package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.SpTroneApiDao;
import com.system.model.SpTroneApiModel;

public class SpTroneApiServer
{
	public List<SpTroneApiModel> loadSpTroneApi()
	{
		return new SpTroneApiDao().loadSpTroneApi();
	}
	
	public Map<String, Object> loadSpTroneApi(int pageIndex,String keyWord)
	{
		return new SpTroneApiDao().loadSpTroneApi(pageIndex, keyWord);
	}
	
	public SpTroneApiModel getSpTroneApiById(int id)
	{
		return new SpTroneApiDao().getSpTroneApiById(id);
	}
	
	public boolean addSpTroneApiModel(SpTroneApiModel model)
	{
		return new SpTroneApiDao().addSpTroneApiModel(model);
	}
	
	public boolean updateSpTroneApiModel(SpTroneApiModel model)
	{
		return new SpTroneApiDao().updateSpTroneApiModel(model);
	}
}
