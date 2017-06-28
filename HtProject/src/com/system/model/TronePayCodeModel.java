package com.system.model;

public class TronePayCodeModel
{
	private int id;
	private int troneId;
	private String payCode;
	private String appId;
	private String channelId;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getTroneId()
	{
		return troneId;
	}
	public void setTroneId(int troneId)
	{
		this.troneId = troneId;
	}
	public String getPayCode()
	{
		return payCode;
	}
	public void setPayCode(String payCode)
	{
		this.payCode = payCode;
	}
	public String getAppId()
	{
		return appId;
	}
	public void setAppId(String appId)
	{
		this.appId = appId;
	}
	public String getChannelId()
	{
		return channelId;
	}
	public void setChannelId(String channelId)
	{
		this.channelId = channelId;
	}
	
	
}
