package com.system.model;

public class CpSpTroneRateModel
{
	private int id;
	private int cpId;
	private String cpName;
	private int spId;
	private String spName;
	private int spTroneId;
	private String spTroneName;
	private float rate;
	private float dayLimit;
	private float monthLimit;
	private String prosData;
	
	//这个数据增加是为了帐单处理
	private String startDate;
	private String endDate;
	
	//结算类型
	private int jsType;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getCpId()
	{
		return cpId;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public String getCpName()
	{
		return cpName;
	}
	public void setCpName(String cpName)
	{
		this.cpName = cpName;
	}
	public int getSpId()
	{
		return spId;
	}
	public void setSpId(int spId)
	{
		this.spId = spId;
	}
	public String getSpName()
	{
		return spName;
	}
	public void setSpName(String spName)
	{
		this.spName = spName;
	}
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public String getSpTroneName()
	{
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName)
	{
		this.spTroneName = spTroneName;
	}
	public float getRate()
	{
		return rate;
	}
	public void setRate(float rate)
	{
		this.rate = rate;
	}
	public float getDayLimit()
	{
		return dayLimit;
	}
	public void setDayLimit(float dayLimit)
	{
		this.dayLimit = dayLimit;
	}
	public float getMonthLimit()
	{
		return monthLimit;
	}
	public void setMonthLimit(float monthLimit)
	{
		this.monthLimit = monthLimit;
	}
	public String getProsData()
	{
		return prosData;
	}
	public void setProsData(String prosData)
	{
		this.prosData = prosData;
	}
	public int getJsType()
	{
		return jsType;
	}
	public void setJsType(int jsType)
	{
		this.jsType = jsType;
	}
	public String getStartDate()
	{
		return startDate;
	}
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}
	public String getEndDate()
	{
		return endDate;
	}
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	
	
}
