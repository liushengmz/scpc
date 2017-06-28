package com.system.dao.ad;

import com.system.database.JdbcAdControl;

public class AdDao
{
	public void updateAppSynStatus(String startDate,String endDate)
	{
		String sql = "update ad_log.tbl_app_summer set status = 1 where fee_date >= '" + startDate + "' and fee_date <= '" + endDate + "'";
		new JdbcAdControl().execute(sql);
	}
	
	public void updateChannelSynStatus(String startDate,String endDate)
	{
		String sql  = "UPDATE ad_log.`tbl_channel_summer` SET STATUS = 1 WHERE fee_date >= '" + startDate + "' AND fee_date <= '" + endDate + "'";
		new JdbcAdControl().execute(sql);
	}
}
