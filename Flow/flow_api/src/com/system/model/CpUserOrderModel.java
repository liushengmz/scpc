package com.system.model;

public class CpUserOrderModel
{
	private int cpId; //一样从80000起算
	private String orderId;
	private String sign;
	private String mobile;
	private int rang;
	private int flowSize;
	
	public int getCpId()
	{
		return cpId;
	}
	public String getOrderId()
	{
		return orderId;
	}
	public String getSign()
	{
		return sign;
	}
	public String getMobile()
	{
		return mobile;
	}
	public int getRang()
	{
		return rang;
	}
	public int getFlowSize()
	{
		return flowSize;
	}
	public void setCpId(int cpId)
	{
		this.cpId = cpId;
	}
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	public void setSign(String sign)
	{
		this.sign = sign;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
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
