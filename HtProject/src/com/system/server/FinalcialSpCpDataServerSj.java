package com.system.server;

import java.util.List;

import com.system.dao.FinalcialSpCpDataDaoSj;
import com.system.vmodel.FinancialSpCpDataShowModel;

public class FinalcialSpCpDataServerSj
{
	public List<FinancialSpCpDataShowModel> loadData(String startDate,String endDate,int spId,int cpId,int dataType)
	{
		return new FinalcialSpCpDataDaoSj().loadData(startDate, endDate,spId,cpId,dataType);
	}
}
