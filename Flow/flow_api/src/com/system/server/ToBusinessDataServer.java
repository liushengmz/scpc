package com.system.server;

import com.system.dao.ToBusinessDataDao;
import com.system.model.ToBusinessOrderModel;
import com.system.util.StringUtil;

public class ToBusinessDataServer
{
	
	private ToBusinessDataDao dao = new ToBusinessDataDao();
	
	/**
	 * 记录下TO_B的请求定单，返回自增长ID
	 * @param model
	 * @return
	 */
	public int recordCpOrder(ToBusinessOrderModel model,String month)
	{
		month = StringUtil.isNullOrEmpty(month) ? StringUtil.getMonthFormat() : month;
		return dao.recordCpOrder(model, month);
	}
	
	
	/**
	 * 根据CPID和定单号查询当前月份中是否存在数据，如果存在则返回ID号，否则返回-1
	 * @param model
	 * @param month
	 * @return
	 */
	public int queryCpOrder(ToBusinessOrderModel model,String month)
	{
		month = StringUtil.isNullOrEmpty(month) ? StringUtil.getMonthFormat() : month;
		return dao.queryCpOrder(model, month);
	}
	
	/**
	 * 将CP定单的数据详情存储进CP定单临时表
	 * @param model
	 * @param month
	 * @return
	 */
	public int saveCpQueryToTempTable(ToBusinessOrderModel model,String month)
	{
		month = StringUtil.isNullOrEmpty(month) ? StringUtil.getMonthFormat() : month;
		return dao.saveCpQueryToTempTable(model, month);
	}
	
	
}
