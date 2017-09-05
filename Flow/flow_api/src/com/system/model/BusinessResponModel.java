package com.system.model;

/**
 * 
 * @author Andy.Chen
 *
 */
public class BusinessResponModel
{
	/**
	 * 返回码
	 */
	private int resultCode;
	/**
	 * 返回的信息
	 */
	private String resultMsg;
	/**
	 * 合作方订单号，原样返回
	 */
	private String orderId;
	
	public int getResultCode()
	{
		return resultCode;
	}
	public String getResultMsg()
	{
		return resultMsg;
	}
	public String getOrderId()
	{
		return orderId;
	}
	public void setResultCode(int resultCode)
	{
		this.resultCode = resultCode;
	}
	public void setResultMsg(String resultMsg)
	{
		this.resultMsg = resultMsg;
	}
	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}
	
	
	
}
