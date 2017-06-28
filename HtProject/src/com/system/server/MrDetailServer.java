package com.system.server;

import java.util.List;

import com.system.dao.MrDetailDataDao;
import com.system.model.params.ReportParamsModel;
import com.system.util.StringUtil;
import com.system.vo.DetailDataVo;

public class MrDetailServer
{
	public List<DetailDataVo> loadDetailDataByPhoneMsg(String keyWord,String table,int type)
	{
		return new MrDetailDataDao().loadDetailDataByPhoneMsg(keyWord, table,type);
	}
	
	public List<DetailDataVo> loadDetailDataBySummer(String date,int spId,int cpId,int spTroneId,int troneId,int type,String joinId)
	{
		String table = StringUtil.getMonthFormat(date);
		return new MrDetailDataDao().loadDetailDataBySummer(table,date,spId,cpId,spTroneId,troneId,type,joinId);
	}
	
	public List<DetailDataVo> loadDetailDataBySummer(ReportParamsModel params,String joinId)
	{
		String table = StringUtil.getMonthFormat(params.getStartDate());
		return new MrDetailDataDao().loadDetailDataBySummer(table,params,joinId);
	}
	
	public List<DetailDataVo> loadDetailDataByCondition(String startDate,String endDate,int spId,int cpId,int spTroneId,int synType)
	{
		String table = StringUtil.getMonthFormat(startDate.substring(0,10));
		return new MrDetailDataDao().loadDetailDataByCondition(table, startDate, endDate, spId, cpId, spTroneId, synType);
	}
	
	
}
