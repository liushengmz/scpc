package com.system.model;

public class ComSumSummerModel
{
	private int coId;
	private int troneId;
	private int provinceId;
	private int cpId;
	private int dataRows;
	private float amount;
	private String mrDate;
	private int recordType;
	
	public int getCoId()
	{
		return coId;
	}
	public void setCoId(int coId)
	{
		this.coId = coId;
	}
	public int getTroneId()
	{
		return troneId;
	}
	public void setTroneId(int troneId)
	{
		this.troneId = troneId;
	}
	public int getProvinceId()
	{
		return provinceId;
	}
	public void setProvinceId(int provinceId)
	{
		this.provinceId = provinceId;
	}
	public int getCpId()
	{
		return cpId;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
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
	public String getMrDate()
	{
		return mrDate;
	}
	public void setMrDate(String mrDate)
	{
		this.mrDate = mrDate;
	}
	public int getRecordType()
	{
		return recordType;
	}
	public void setRecordType(int recordType)
	{
		this.recordType = recordType;
	}
	
	@Override
	public String toString()
	{
		return troneId + "-" + cpId +  "-" + provinceId + "-" + "-" + dataRows;
	}
	
}
