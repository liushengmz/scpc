package com.system.model;

public class CpPushUrlModel
{
	private int id;
	private int cpId;
	private String name;
	private int refCount;
	private int holdPercent;
	private float holdAmount;
	private int holdStartCount;
	private int isRealTime;
	private String url;
	private String cpName;
	private String lastDate;
	
	public String getLastDate()
	{
		return lastDate;
	}
	public void setLastDate(String lastDate)
	{
		this.lastDate = lastDate;
	}
	public float getCurAmount()
	{
		return curAmount;
	}
	public void setCurAmount(float curAmount)
	{
		this.curAmount = curAmount;
	}
	private float curAmount;
	
	public int getRefCount()
	{
		return refCount;
	}
	public void setRefCount(int refCount)
	{
		this.refCount = refCount;
	}
	public int getHoldPercent()
	{
		return holdPercent;
	}
	public void setHoldPercent(int holdPercent)
	{
		this.holdPercent = holdPercent;
	}
	public float getHoldAmount()
	{
		return holdAmount;
	}
	public void setHoldAmount(float holdAmount)
	{
		this.holdAmount = holdAmount;
	}
	public int getHoldStartCount()
	{
		return holdStartCount;
	}
	public void setHoldStartCount(int holdStartCount)
	{
		this.holdStartCount = holdStartCount;
	}
	public int getIsRealTime()
	{
		return isRealTime;
	}
	public void setIsRealTime(int isRealTime)
	{
		this.isRealTime = isRealTime;
	}
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
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getCpName()
	{
		return cpName;
	}
	public void setCpName(String cpName)
	{
		this.cpName = cpName;
	}
	
}
