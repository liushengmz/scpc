package com.system.model;

import java.util.List;

public class ToBusinessOrderModel
{
	private int id;
	private int cpId;
	private String orderId;
	private String sign;
	private List<ToBusinessChildOrderModel> orderList;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
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
	public List<ToBusinessChildOrderModel> getOrderList()
	{
		return orderList;
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
	public void setOrderList(List<ToBusinessChildOrderModel> orderList)
	{
		this.orderList = orderList;
	}
}
