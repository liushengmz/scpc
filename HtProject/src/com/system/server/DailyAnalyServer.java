package com.system.server;

import com.system.dao.DailyAnalyDao;
import com.system.util.StringUtil;

public class DailyAnalyServer
{
	private static boolean isAnalysing = false;
	
	public boolean analyDailyMr(String date)
	{
		if(isAnalysing)
			return false;

		isAnalysing = true;
		
		String dateMonth = StringUtil.getMonthFormat(date);
		
		new DailyAnalyDao().analyDailyMr(date, dateMonth);
		
		isAnalysing = false;
		
		return true;
	}
}
