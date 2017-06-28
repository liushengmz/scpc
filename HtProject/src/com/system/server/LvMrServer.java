package com.system.server;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.system.dao.LvMrDao;
import com.system.model.LvChannelModel;
import com.system.model.LvSpecialCpaModel;
import com.system.util.StringUtil;

public class LvMrServer {
	public Map<String, Object> getLvMrTodayData(String date,int payType,int userId)
	{
		String tableName = StringUtil.getMonthFormat(date);
		return new LvMrDao().getLvMrTodayData(tableName, date, payType, userId);
	}
	
	public Map<String, Object> getLvMrDaysData(String startDate,String endDate,int payType,int userId)
	{
		String tableName = StringUtil.getMonthFormat(startDate);
		return new LvMrDao().getLvMrDaysData(tableName, startDate, endDate, payType, userId);
	}

	public List<LvSpecialCpaModel> loadSpecialCpaData(String startDate,String endDate,String appKey,String channel)
	{
		LvMrDao dao = new LvMrDao();
		
		List<LvSpecialCpaModel> list = dao.loadMrActiveData(startDate, endDate, appKey, channel);
		
		Collections.sort(list);
		
		String tableName = StringUtil.getMonthFormat(startDate);
		
		Map<String, LvSpecialCpaModel> map = dao.loadMrData(tableName, startDate, endDate, appKey, channel, 1);
		
		LvSpecialCpaModel mapModel = null;
		
		for(LvSpecialCpaModel model : list)
		{
			
			if(map.keySet().contains(model.getActiveDate()))
			{
				mapModel = map.get(model.getActiveDate());
				
				if(mapModel!=null)
				{
					model.setAmount(mapModel.getAmount());
					model.setPayType(mapModel.getPayType());
				}
				
			}
		}
		
		return list;
	}
	
	public List<LvSpecialCpaModel> loadLvSpecialCpaByUserId(String startDate,String endDate,int userId)
	{
		LvChannelModel model = new LvChannelServer().loadLvChannelModel(userId, 1);
		
		if(model==null)
			return null;
		
		return loadSpecialCpaData(startDate,endDate,model.getAppKey(),model.getChannel());
	}
	
}
