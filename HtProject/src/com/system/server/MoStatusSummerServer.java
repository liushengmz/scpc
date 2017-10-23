package com.system.server;

import java.util.List;

import com.system.dao.MoStatusSummerDao;
import com.system.model.MoStatusSummerModel;
import com.system.util.StringUtil;

public class MoStatusSummerServer
{
	public List<MoStatusSummerModel> loadMoStatusSummer(String startDate,String endDate,String keyWord)
	{
		String table = StringUtil.getMonthFormat(startDate.substring(0,10));
		return new MoStatusSummerDao().loadMoStatusSummer(table, startDate, endDate, keyWord);
	}
}
