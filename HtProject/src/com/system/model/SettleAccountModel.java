package com.system.model;

public class SettleAccountModel
{
	private String operatorName;
	private String spTroneName;
	private float amount;
	private float reduceAmount;
	private float jiesuanlv;
	/**
	 * 核减类型，0是按信息费，1是按结算款核减
	 */
	private int reduceType;
	
	public String getSpTroneName()
	{
		return spTroneName;
	}
	public void setSpTroneName(String spTroneName)
	{
		this.spTroneName = spTroneName;
	}
	public float getAmount()
	{
		return amount;
	}
	public void setAmount(float amount)
	{
		this.amount = amount;
	}
	public float getJiesuanlv()
	{
		return jiesuanlv;
	}
	public void setJiesuanlv(float jiesuanlv)
	{
		this.jiesuanlv = jiesuanlv;
	}
	public String getOperatorName()
	{
		return operatorName;
	}
	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}
	public float getReduceAmount()
	{
		return reduceAmount;
	}
	public void setReduceAmount(float reduceAmount)
	{
		this.reduceAmount = reduceAmount;
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
