package com.system.model;

public class MrReportModel
{
	private String title1;
	private String title2;
	private String joinId;
	private int dataRows;
	private double amount;
	private int showDataRows;
	private double showAmount;
	private double spMoney;
	private double cpMoney;
	
	public String getTitle1()
	{
		return title1;
	}
	public void setTitle1(String title1)
	{
		this.title1 = title1;
	}
	public String getTitle2()
	{
		return title2;
	}
	public void setTitle2(String title2)
	{
		this.title2 = title2;
	}
	public int getDataRows()
	{
		return dataRows;
	}
	public void setDataRows(int dataRows)
	{
		this.dataRows = dataRows;
	}
	public double getAmount()
	{
		return amount;
	}
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
	public int getShowDataRows()
	{
		return showDataRows;
	}
	public void setShowDataRows(int showDataRows)
	{
		this.showDataRows = showDataRows;
	}
	public double getShowAmount()
	{
		return showAmount;
	}
	public void setShowAmount(double showAmount)
	{
		this.showAmount = showAmount;
	}
	public String getJoinId()
	{
		return joinId;
	}
	public void setJoinId(String joinId)
	{
		this.joinId = joinId;
	}
	public double getSpMoney()
	{
		return spMoney;
	}
	public void setSpMoney(double spMoney)
	{
		this.spMoney = spMoney;
	}
	public double getCpMoney()
	{
		return cpMoney;
	}
	public void setCpMoney(double cpMoney)
	{
		this.cpMoney = cpMoney;
	}
	
}
