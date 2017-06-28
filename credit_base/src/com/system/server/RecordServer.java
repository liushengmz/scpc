package com.system.server;

import com.system.dao.RecordDao;
import com.system.model.ApiOrderModel;

public class RecordServer
{
	public void recordVisitModel(ApiOrderModel model)
	{
		new RecordDao().addRecord(model);
	}
	
	public void udpateVisistModel(ApiOrderModel model)
	{
		new RecordDao().updateRecord(model);
	}
	
	public ApiOrderModel getApiOrderById(String month,String id)
	{
		return new RecordDao().getApiOrderById(month, id);
	}
	
	public void updateVeryCode(ApiOrderModel model,String tableName)
	{
		new RecordDao().updateVeryCode(model,tableName);
	}
	
	public void udpateSecondVisitModel(ApiOrderModel model)
	{
		new RecordDao().updateRecord(model);
	}
}
