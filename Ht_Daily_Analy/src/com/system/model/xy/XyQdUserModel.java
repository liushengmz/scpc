package com.system.model.xy;

public class XyQdUserModel
{
	private int id;
	private String activeDate;
	private String appName;
	private String channelKey;
	private int showDataRows;
	
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
	public int getShowDataRows()
	{
		return showDataRows;
	}
	public void setShowDataRows(int showDataRows)
	{
		this.showDataRows = showDataRows;
	}
}
