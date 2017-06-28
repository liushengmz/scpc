package com.system.model;

import java.util.List;

public class CpProvinceModel
{
	private int cpId;
	private String cpName;
	private int operatorId;
	private String operatorName;
	private float price;
	private List<Integer> provinces;
	
	public int getCpId()
	{
		return cpId;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public String getCpName()
	{
		return cpName;
	}
	public void setCpName(String cpName)
	{
		this.cpName = cpName;
	}
	public int getOperatorId()
	{
		return operatorId;
	}
	public void setOperatorId(int operatorId)
	{
		this.operatorId = operatorId;
	}
	public String getOperatorName()
	{
		return operatorName;
	}
	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}
	public float getPrice()
	{
		return price;
	}
	public void setPrice(float price)
	{
		this.price = price;
	}
	public List<Integer> getProvinces()
	{
		return provinces;
	}
	public void setProvinces(List<Integer> provinces)
	{
		this.provinces = provinces;
	}
	
	
}
