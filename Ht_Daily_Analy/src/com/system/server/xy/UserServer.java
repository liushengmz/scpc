package com.system.server.xy;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.system.dao.xy.UserDao;
import com.system.model.xy.XyUserModel;
import com.system.util.StringUtil;

public class UserServer
{
	Logger log = Logger.getLogger(UserServer.class);
	
	public boolean updateQdData(int id,int showDataRows)
	{
		return new UserDao().updateQdData(id,showDataRows);
	}
	
	/**
	 * 分析当天数据到SUMMER，并分析
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean analyUserToSummer(String startDate,String endDate)
	{
		log.info("start analy user to summer ["+ startDate +"," + endDate + "]");
		UserDao dao = new UserDao();
		dao.deleteFromSummer(startDate, endDate);
		
		//增加易正龙的数据了
		dao.analyZyLDataToSummer(startDate, endDate);
		
		dao.analyUserToSummer(StringUtil.getMonthFormat(startDate), startDate, endDate);
		
		//更新 渠道的展示数据
		dao.updateQdShowData(startDate, endDate);
		
		return true;
	}
	
	public void startAnalyUser()
	{
		String startDate = StringUtil.getPreDayOfMonth();
		analyUserToSummer(startDate, startDate);
		log.info("finish analy user to summer");
	}
	
	//更新渠道(CPA)扣量后的数据
	public void updateQdUserData()
	{
		String startDate = StringUtil.getPreDayOfMonth();
		updateQdUserSummer(startDate,startDate);
		log.info("finish update Qd user data");
	}
	
	//更新渠道(CPA)扣量后的数据
	public void updateQdUserSummer(String startDate,String endDate)
	{
		log.info("start update qd user ["+ startDate +"," + endDate + "]");
		UserDao dao = new UserDao();
		//dao.updateQdShowData(startDate, endDate);
		dao.updateQdShowDataStatus(startDate, endDate);
	}
	
	//每隔一小时更新渠道(CPA实时)数据
	public void analyQdShowDataWithHour()
	{
		Calendar ca = Calendar.getInstance();
		
		ca.add(Calendar.HOUR, -1);
		
		int hour = ca.get(Calendar.HOUR_OF_DAY);
		
		String startDate = StringUtil.getDateFormat(ca.getTime());
		String tableName = StringUtil.getMonthFormat(ca.getTime());
		
		UserDao dao = new UserDao();
		
		List<XyUserModel> list = dao.queryQdShowDataWithHour(tableName, startDate,hour);
		
		dao.analyUserList(list,startDate);
	}
	
	public static void main(String[] args)
	{
		new UserServer().startAnalyUser();
	}
	
}
