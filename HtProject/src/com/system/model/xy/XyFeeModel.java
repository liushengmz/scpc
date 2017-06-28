package com.system.model.xy;

public class XyFeeModel
{
	private int id;
	private String appKey;
	private String appName;
	private int appType;
	private String channelId;
	private int dataRows;
	private float amount;
	private float showAmount;
	private String feeDate;
	private int status;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getAppKey()
	{
		return appKey;
	}
	public void setAppKey(String appKey)
	{
		this.appKey = appKey;
	}
	public String getChannelId()
	{
		return channelId;
	}
	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}
	public int getDataRows()
	{
		return dataRows;
	}
	public void setDataRows(int dataRows)
	{
		this.dataRows = dataRows;
	}
	public float getAmount()
	{
		return amount;
	}
	public void setAmount(float amount)
	{
		this.amount = amount;
	}
	public String getFeeDate()
	{
		return feeDate;
	}
	public void setFeeDate(String feeDate)
	{
		this.feeDate = feeDate;
	}
	public String getAppName()
	{
		return appName;
	}
	public void setAppName(String appName)
	{
		this.appName = appName;
	}
	public float getShowAmount()
	{
		return showAmount;
	}
	public void setShowAmount(float showAmount)
	{
		this.showAmount = showAmount;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getAppType() {
		return appType;
	}
	public void setAppType(int appType) {
		this.appType = appType;
	}
	
}
