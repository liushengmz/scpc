package com.system.model;

public class SpBillingSpTroneModel
{
	private int id;
	private int spBillingId;
	private String startDate;
	private String endDate;
	private int spTroneId;
	private float amount;
	private float rate;
	private float reduceAmount;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getSpBillingId()
	{
		return spBillingId;
	}
	public void setSpBillingId(int spBillingId)
	{
		this.spBillingId = spBillingId;
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
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
	public float getAmount()
	{
		return amount;
	}
	public void setAmount(float amount)
	{
		this.amount = amount;
	}
	public float getRate()
	{
		return rate;
	}
	public void setRate(float rate)
	{
		this.rate = rate;
	}
	public float getReduceAmount()
	{
		return reduceAmount;
	}
	public void setReduceAmount(float reduceAmount)
	{
		this.reduceAmount = reduceAmount;
	}
	
	
}
