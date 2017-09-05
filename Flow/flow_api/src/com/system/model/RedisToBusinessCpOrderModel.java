package com.system.model;

/**
 * CP定单在REDIS中存储的数据结构
 * @author Andy.Chen
 *
 */
public class RedisToBusinessCpOrderModel
{
	public static final String MAP_KEY_TEMP_TABLE_ID = "TEMP_TABLE_ID";
	public static final String MAP_KEY_MONTH_NAME = "MONTH_NAME";
	public static final String MAP_KEY_MONTH_TABLE_ID = "MONTH_TABLE_ID";
	public static final String MAP_KEY_CP_ORDER = "CP_ORDER";
	public static final String MAP_KEY_CP_ID = "CP_ID";
	public static final String MAP_KEY_ORDER_LIST_SIZE = "ORDER_LIST_SIZE";
	//STATUS这个KEY暂定，状态说明（暂定） 1、待处理  2、处理中  3、处理完毕
	public static final String MAP_KEY_STATUS = "STATUS";
	
	private String orderId;
	private int cpId;
	private int listSize;
	private int tempTableId;
	private String monthName;
	private int monthTableId;
	private int status;
	
	public String getOrderId()
	{
		return orderId;
	}
	public int getCpId()
	{
		return cpId;
	}
	public int getListSize()
	{
		return listSize;
	}
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public void setListSize(int listSize)
	{
		this.listSize = listSize;
	}
	
	public int getTempTableId()
	{
		return tempTableId;
	}
	public String getMonthName()
	{
		return monthName;
	}
	public int getMonthTableId()
	{
		return monthTableId;
	}
	public int getStatus()
	{
		return status;
	}
	public void setTempTableId(int tempTableId)
	{
		this.tempTableId = tempTableId;
	}
	public void setMonthName(String monthName)
	{
		this.monthName = monthName;
	}
	public void setMonthTableId(int monthTableId)
	{
		this.monthTableId = monthTableId;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	@Override
	public String toString()
	{
		return cpId  + "--" + orderId + "--" + listSize;
	}
	
}
