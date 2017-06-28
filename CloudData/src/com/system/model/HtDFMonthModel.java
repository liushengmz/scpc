package com.system.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HtDFMonthModel
{
	@JsonProperty("DepId")
	private int devId;
	@JsonProperty("CompanyName")
	private String companyName;
	//sms
	@JsonProperty("ServiceName")
	private String serviceName;
	@JsonProperty("ProductName")
	private String productName;
	//1移动 2联通 3电信
	@JsonProperty("Oprator")
	private int oprator;
	@JsonProperty("ChannelName")
	private String channelName;
	@JsonProperty("UserCount")
	private int userCount;
	@JsonProperty("Amount")
	private int amount;
	//1不扣量 0扣量
	@JsonProperty("IsDeducted")
	private int isDeducted;
	//(yyyyMMdd)例如：20151201代表2015年12月分的所有数据  dd部分固定传01
	@JsonProperty("StaticDate")
	private String staticDate;
	public int getDevId()
	{
		return devId;
	}
	public void setDevId(int devId)
	{
		this.devId = devId;
	}
	public String getCompanyName()
	{
		return companyName;
	}
	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}
	public String getServiceName()
	{
		return serviceName;
	}
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	public int getOprator()
	{
		return oprator;
	}
	public void setOprator(int oprator)
	{
		this.oprator = oprator;
	}
	public String getChannelName()
	{
		return channelName;
	}
	public void setChannelName(String channelName)
	{
		this.channelName = channelName;
	}
	public int getUserCount()
	{
		return userCount;
	}
	public void setUserCount(int userCount)
	{
		this.userCount = userCount;
	}
	public int getAmount()
	{
		return amount;
	}
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	public int getIsDeducted()
	{
		return isDeducted;
	}
	public void setIsDeducted(int isDeducted)
	{
		this.isDeducted = isDeducted;
	}
	public String getStaticDate()
	{
		return staticDate;
	}
	public void setStaticDate(String staticDate)
	{
		this.staticDate = staticDate;
	}
	
	
}
