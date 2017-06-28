package com.system.server.xy;

import org.apache.log4j.Logger;

import com.system.dao.xy.FeeDao;
import com.system.util.StringUtil;

public class FeeServer
{
	Logger log = Logger.getLogger(FeeServer.class);
	
	//分析应用的费用
	public void startAnalyFee()
	{
		String startDate = StringUtil.getPreDayOfMonth();
		analyFeeToSummer(startDate, startDate);
		log.info("finish analy fee to summer");
	}
	
	//将每个渠道每个应用的收入从基础表tbl_xypay_分析进渠道应用汇总表tbl_xypay_summer
	//再将分析出来的结果再再分析进应用汇总表tbl_xy_fee_summer
	public boolean analyFeeToSummer(String startDate,String endDate)
	{
		log.info("start analy fee to summer ["+ startDate +"," + endDate + "]");
		FeeDao dao = new FeeDao();
		dao.deleteFeeFromSummer(startDate, endDate);
		dao.analyFeeToSummer(StringUtil.getMonthFormat(startDate), startDate, endDate);
		//更新游戏CP(CPS合作)扣量后的收入
		dao.updateQdAmount(startDate, endDate);
		//更新渠道(CPS合作)扣量后的收入
		dao.updateChannelShowAmout(startDate, endDate);
		
		return true; //这个返回已经没意义了
	}
	
	public void updateQdFeeSummer(String startDate,String endDate)
	{
		log.info("start update qd fee ["+ startDate +"," + endDate + "]");
		FeeDao dao = new FeeDao();
		//dao.updateQdAmount(startDate, endDate);
		dao.updateQdAmountStatus(startDate, endDate);
		dao.updateChannelShowAmountStatus(startDate, endDate);
	}
	
	public void updateQdFeeSummer()
	{
		String startDate = StringUtil.getPreDayOfMonth();
		updateQdFeeSummer(startDate,startDate);
		log.info("finish update Qd fee data");
	}
	
}
