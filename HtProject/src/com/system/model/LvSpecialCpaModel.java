package com.system.model;

public class LvSpecialCpaModel  implements Comparable<LvSpecialCpaModel>
{
	private String activeDate;
	private int dataRows;
	private int effectDataRows;
	private int payType;
	private float amount;
	
	public String getActiveDate()
	{
		return activeDate;
	}
	public void setActiveDate(String activeDate)
	{
		this.activeDate = activeDate;
	}
	public int getDataRows()
	{
		return dataRows;
	}
	public void setDataRows(int dataRows)
	{
		this.dataRows = dataRows;
	}
	public int getEffectDataRows()
	{
		return effectDataRows;
	}
	public void setEffectDataRows(int effectDataRows)
	{
		this.effectDataRows = effectDataRows;
	}
	
	@Override
	public int compareTo(LvSpecialCpaModel o)
	{
		if(activeDate==null || o==null)
			return -1;
		
		return activeDate.compareTo(o.getActiveDate());
	}
	public int getPayType()
	{
		return payType;
	}
	public void setPayType(int payType)
	{
		this.payType = payType;
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
