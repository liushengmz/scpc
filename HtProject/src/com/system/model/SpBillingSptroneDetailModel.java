package com.system.model;

/**
 * 这个是用于存储中转数据
 * @author Administrator
 *
 */
public class SpBillingSptroneDetailModel
{
	int id;
	private int spBillingId;
	private int spTroneId;
	private String spTroneName;
	private String mrDate;
	private float amount;
	private float reduceAmount; //核减费用
	private int reduceType; //核减类型  已废用
	private float rate;
	private String remark;
	private int status;
	
	//后面再加进来的
	private float reduceDataAmount; //核减的信息费
	private float reduceMoneyAmount; //梳头的结算款
	
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
	public int getSpBillingId()
	{
		return spBillingId;
	}
	public void setSpBillingId(int spBillingId)
	{
		this.spBillingId = spBillingId;
	}
	public float getReduceDataAmount()
	{
		return reduceDataAmount;
	}
	public void setReduceDataAmount(float reduceDataAmount)
	{
		this.reduceDataAmount = reduceDataAmount;
	}
	public float getReduceMoneyAmount()
	{
		return reduceMoneyAmount;
	}
	public void setReduceMoneyAmount(float reduceMoneyAmount)
	{
		this.reduceMoneyAmount = reduceMoneyAmount;
	}
	
}
