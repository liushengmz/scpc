package com.system.server;

import java.util.List;

import com.system.dao.ProfitCalDao;
import com.system.model.ProfitCalModel;

public class ProfitCalServer
{
	public List<ProfitCalModel> loadProfit(String startDate,String endDate,int spId,int spTroneId)
	{
		return new ProfitCalDao().loadProfit(startDate, endDate, spId, spTroneId);
	}
}
