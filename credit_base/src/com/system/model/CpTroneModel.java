package com.system.model;

public class CpTroneModel
{
	private int id;
	private int cpId;
	private int spTroneId;
	private float dayLimit;
	private float monthLimit;
	private int status;
	
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
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
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
}
