package com.system.server.analy;

import java.util.Map;

import com.system.dao.analy.MrSummerRecordDao;
import com.system.model.analy.MrSummerRecordModel;

public class MrSummerRecordServer
{
	public Map<String, Object> loadMrSummerRecordData(String startDate,String endDate,int spId,String spTroneName,int spTroneId,int pageIndex)
	{
		return new MrSummerRecordDao().loadMrSummerRecordData(startDate, endDate, spId, spTroneName, spTroneId, pageIndex);
	}
	
	public boolean existMrSummerRecord(MrSummerRecordModel model)
	{
		return new MrSummerRecordDao().existMrSummerRecord(model);
	}
	
	public void addMrSummerRecordModel(MrSummerRecordModel model)
	{
		new MrSummerRecordDao().addMrSummerRecordModel(model);
	}
	
	public void deleteMrSummerRecordModel(int mrSummerId,int cpMrSummerId)
	{
		new MrSummerRecordDao().deleteMrSummerRecordModel(mrSummerId, cpMrSummerId);
	}
	
	public void updateMrSummerRecordModel(MrSummerRecordModel model)
	{
		new MrSummerRecordDao().updateMrSummerRecordModel(model);
	}
	
	public MrSummerRecordModel loadMrSummerRecordById(int mrSummerId,int cpMrSummerId)
	{
		return new MrSummerRecordDao().loadMrSummerRecordById(mrSummerId, cpMrSummerId);
	}
}
