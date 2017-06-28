package com.system.server.ad;

import com.system.dao.ad.AdDao;
import com.system.util.StringUtil;

public class AdServer
{
	public void updateApChannelSummer()
	{
		String startDate = StringUtil.getPreDayOfMonth();
		updateApChannelSummer(startDate,startDate);
		
	}
	
	public void updateApChannelSummer(String startDate,String endDate)
	{
		new AdDao().updateAppSynStatus(startDate, endDate);
		new AdDao().updateChannelSynStatus(startDate, endDate);
	}
}
