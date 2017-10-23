package com.system.model;

public class ToBusinessChildOrderModel
{
	private String mobile;
	private String subOrderId;
	private int rang;
	private int flowSize;
	
	public String getMobile()
	{
		return mobile;
	}
	public String getSubOrderId()
	{
		return subOrderId;
	}
	public int getRang()
	{
		return rang;
	}
	public int getFlowSize()
	{
		return flowSize;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public void setSubOrderId(String subOrderId)
	{
		this.subOrderId = subOrderId;
	}
	public void setRang(int rang)
	{
		this.rang = rang;
	}
	public void setFlowSize(int flowSize)
	{
		this.flowSize = flowSize;
	}	
}
