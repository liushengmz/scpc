package com.system.server;

import java.util.List;

import com.system.dao.FinalcialSpCpDataDao;
import com.system.vmodel.FinancialSpCpDataShowModel;

public class FinalcialSpCpDataServer
{
	public List<FinancialSpCpDataShowModel> loadData(int coId,String startDate,String endDate,int spId,int cpId)
	{
		return new FinalcialSpCpDataDao().loadData(coId,startDate, endDate,spId,cpId);
	}
}
