package com.system.server;

import java.util.List;

import com.system.dao.MoDao;
import com.system.util.StringUtil;
import com.system.vo.DetailDataVo;

public class MoServer
{
	public List<DetailDataVo> loadMoDetail(String startDate,String endDate,int spId,int spTroneId,String keyWord)
	{
		String table = StringUtil.getMonthFormat(startDate.substring(0,10));
		return new MoDao().loadMoDetail(table, startDate, endDate, spId, spTroneId, keyWord);
	}
}
