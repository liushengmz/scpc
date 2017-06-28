package com.system.model;

public class SingleCpSpTroneRateModel
{
	private int id;
	private int cpSpTroneId;
	private String title;
	private String startDate;
	private String endDate;
	private float rate;
	private String remark;
	private int cpId;
	private int spTroneId;
	private float defaultRate;
	
	public int getCpId()
	{
		return cpId;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
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
	public float getRate()
	{
		return rate;
	}
	public void setRate(float rate)
	{
		this.rate = rate;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public int getCpSpTroneId()
	{
		return cpSpTroneId;
	}
	public void setCpSpTroneId(int cpSpTroneId)
	{
		this.cpSpTroneId = cpSpTroneId;
	}
	public float getDefaultRate()
	{
		return defaultRate;
	}
	public void setDefaultRate(float defaultRate)
	{
		this.defaultRate = defaultRate;
	}
	
	
}
