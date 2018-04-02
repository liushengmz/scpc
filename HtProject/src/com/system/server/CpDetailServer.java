package com.system.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.system.dao.CpDetailDao;
import com.system.util.StringUtil;

public class CpDetailServer
{
	private CpDetailDao dao = new CpDetailDao();
	
	public Map<String, Object> loadDetailDataByMsg(int pageIndex,int userId,String startDate,String endDate,int type,String keyWord)
	{
		String tableName = "";
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		
		Calendar ca = Calendar.getInstance();
		
		ca.add(Calendar.DAY_OF_YEAR, -1);
		
		String yesDate = StringUtil.getDateFormat(ca.getTime()) + " 23:59:59";
		
		try
		{
			Date date = sdf1.parse(startDate);
			
			if(endDate.compareTo(yesDate)>=0)
			{
				endDate = yesDate;
			}
			
			tableName = sdf2.format(date);
		}
		catch (ParseException e)
		{
			tableName = sdf2.format(new Date());
		}
		
		return dao.loadDetailDataByMsg(pageIndex, userId, tableName, startDate, endDate, type, keyWord);
	}
	
}
