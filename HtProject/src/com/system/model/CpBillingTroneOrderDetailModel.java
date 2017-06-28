package com.system.model;

public class CpBillingTroneOrderDetailModel
{
	private int cpBillingId;
	private int provinceId;
	private int spTroneId;
	private int troneOrderId;
	private String mrDate;
	private float amount;
	private float rate;
	
	public int getCpBillingId()
	{
		return cpBillingId;
	}
	public void setCpBillingId(int cpBillingId)
	{
		this.cpBillingId = cpBillingId;
	}
	public int getProvinceId()
	{
		return provinceId;
	}
	public void setProvinceId(int provinceId)
	{
		this.provinceId = provinceId;
	}
	public int getTroneOrderId()
	{
		return troneOrderId;
	}
	public void setTroneOrderId(int troneOrderId)
	{
		this.troneOrderId = troneOrderId;
	}
	public String getMrDate()
	{
		return mrDate;
	}
	public void setMrDate(String mrDate)
	{
		this.mrDate = mrDate;
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
	public int getSpTroneId()
	{
		return spTroneId;
	}
	public void setSpTroneId(int spTroneId)
	{
		this.spTroneId = spTroneId;
	}
}
