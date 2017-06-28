package com.system.server.xy;

import java.util.Map;

import org.apache.log4j.Logger;

import com.system.dao.xy.FeeDao;
import com.system.util.StringUtil;

public class FeeServer
{
	Logger log = Logger.getLogger(FeeServer.class);
	
	public void startAnalyFee()
	{
		String startDate = StringUtil.getPreDayOfMonth();
		analyFeeToSummer(startDate, startDate);
		log.info("finish analy fee to summer");
	}
	
	public boolean analyFeeToSummer(String startDate,String endDate)
	{
		log.info("start analy fee to summer ["+ startDate +"," + endDate + "]");
		FeeDao dao = new FeeDao();
		dao.deleteFeeFromSummer(startDate, endDate);
		return dao.analyFeeToSummer(StringUtil.getMonthFormat(startDate), startDate, endDate);
	}
	
	public Map<String, Object> loadChannelAppFee(String startDate,String endDate,String keyWord, int pageIndex , int appType)
	{
		return new FeeDao().loadChannelAppFee(startDate, endDate, keyWord, pageIndex, appType);
	}
	
	public Map<String, Object> loadAppFee(String startDate,String endDate,String appKey,int pageIndex,int apptype)
	{
		return new FeeDao().loadAppFee(startDate, endDate, appKey, pageIndex, apptype);
	}
	
	public Map<String, Object> loadQdAppFee(String startDate,String endDate,int userId,int pageIndex)
	{
		return new FeeDao().loadQdAppFee(startDate, endDate,userId,pageIndex);
	}
	
	public Map<String, Object> loadQdUserFee(String startDate,String endDate,int userId,String keyWord,int pageIndex)
	{
		return new FeeDao().loadQdUserFee(startDate, endDate, userId, keyWord,pageIndex);
	}
	
	public void updateQdFeeSummer(String startDate,String endDate)
	{
		log.info("start update qd fee ["+ startDate +"," + endDate + "]");
		FeeDao dao = new FeeDao();
		dao.updateQdAmount(startDate, endDate);
		dao.updateQdAmountStatus(startDate, endDate);
	}
	
	//更新游戏CP(CPS)的展示金额
	public boolean updateQdFee(int id,float showAmount)
	{
		return new FeeDao().updateQdFee(id,showAmount);
	}
	
	//更新游戏渠道(CPS)的展示金额
	public boolean updateChannelFee(int id,float showAmount)
	{
		return new FeeDao().updateChannelFee(id, showAmount);
	}
	
	public void updateQdFeeSummer()
	{
		String startDate = StringUtil.getPreDayOfMonth();
		updateQdFeeSummer(startDate,startDate);
		log.info("finish update Qd fee data");
	}
	
}
