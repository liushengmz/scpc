package com.system.server;

import java.util.List;

import com.system.dao.FinalcialSpCpDataDao;
import com.system.model.SpcpProfitModel;
import com.system.vmodel.FinancialSpCpDataShowModel;

public class FinalcialSpCpDataServer
{
	public List<FinancialSpCpDataShowModel> loadData(String startDate,String endDate,int spId,int cpId,int dataType)
	{
		return new FinalcialSpCpDataDao().loadData(startDate, endDate,spId,cpId,dataType);
	}
	
	public List<SpcpProfitModel> loadProfitData(String startDate,String endDate,int spId,int spTroneId,int cpId)
	{
		return new FinalcialSpCpDataDao().loadProfitData(startDate, endDate,spId,spTroneId,cpId);
	}
}
