package com.system.server;

import java.util.List;

import com.system.dao.BaseDataShowDao;
import com.system.model.BaseDataShowModel;

public class BaseDataShowServer
{
	public List<BaseDataShowModel> loadShowData(String startDate,String endDate,int spId,int cpId,int coId,int showType)
	{
		List<BaseDataShowModel> list = new BaseDataShowDao().loadShowData(startDate, endDate, spId, cpId, coId, showType);

		return list;
	}
	
	
	public List<BaseDataShowModel> loadAllData(String startDate,String endDate,int coId,int showType)
	{
		List<BaseDataShowModel> list = new BaseDataShowDao().loadAllData(startDate, endDate, coId, showType);

		return list;
	}
}
