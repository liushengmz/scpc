package com.system.model.xy;

public class XyUserModel
{
	private int id;
	private String activeDate;
	private String appKey;
	private String appName;
	private int appType;
	private String channelKey;
	private int dataRows;
	private int showDataRows;
	private int status;
	private double showAmount;
	
	public double getShowAmount() {
		return showAmount;
	}
	public void setShowAmount(double showAmount) {
		this.showAmount = showAmount;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getActiveDate()
	{
		return activeDate;
	}
	public void setActiveDate(String activeDate)
	{
		this.activeDate = activeDate;
	}
	public String getAppKey()
	{
		return appKey;
	}
	public void setAppKey(String appKey)
	{
		this.appKey = appKey;
	}
	public String getAppName()
	{
		return appName;
	}
	public void setAppName(String appName)
	{
		this.appName = appName;
	}
	public String getChannelKey()
	{
		return channelKey;
	}
	public void setChannelKey(String channelKey)
	{
		this.channelKey = channelKey;
	}
	public int getDataRows()
	{
		return dataRows;
	}
	public void setDataRows(int dataRows)
	{
		this.dataRows = dataRows;
	}
	public int getShowDataRows()
	{
		return showDataRows;
	}
	public void setShowDataRows(int showDataRows)
	{
		this.showDataRows = showDataRows;
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
