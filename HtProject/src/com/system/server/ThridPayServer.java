package com.system.server;

import java.util.Map;

import com.system.dao.MrDao;
import com.system.dao.ThirdPayDao;

public class ThridPayServer
{
	public Map<String, Object> getThridPayData(String startDate, String endDate,int dataType)
	{
		return new ThirdPayDao().getThirdPayData(startDate, endDate, dataType);
	}
}
