package com.system.model;

//用这个类去比较的话会产生在总额不变的情况下就不会重新分析，后面再进行更新
public class FeeDateDataModel
{
	private String feeDate;
	private int dataRows;
	private float amount;
	
	public String getFeeDate()
	{
		return feeDate;
	}
	public void setFeeDate(String feeDate)
	{
		this.feeDate = feeDate;
	}
	public int getDataRows()
	{
		return dataRows;
	}
	public void setDataRows(int dataRows)
	{
		this.dataRows = dataRows;
	}
	public float getAmount()
	{
		return amount;
	}
	public void setAmount(float amount)
	{
		this.amount = amount;
	}
	
	
}
