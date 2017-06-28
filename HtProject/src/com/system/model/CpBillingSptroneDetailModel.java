package com.system.model;

/**
 * 这个是用于存储中转数据
 * @author Administrator
 *
 */
public class CpBillingSptroneDetailModel
{
	int id;
	private int cpBillingId;
	private int spTroneId;
	private String spTroneName;
	private String mrDate;
	private float amount;
	private float reduceAmount;
	private int reduceType;
	private float rate;
	private String remark;
	private int status;
	
	public int getCpBillingId()
	{
		return cpBillingId;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getSpTroneName()
	{
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName)
	{
		this.spTroneName = spTroneName;
	}
	public float getReduceAmount()
	{
		return reduceAmount;
	}
	public void setReduceAmount(float reduceAmount)
	{
		this.reduceAmount = reduceAmount;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public void setCpBillingId(int cpBillingId)
	{
		this.cpBillingId = cpBillingId;
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
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public int getReduceType()
	{
		return reduceType;
	}
	public void setReduceType(int reduceType)
	{
		this.reduceType = reduceType;
	}
	
}
