
package com.system.api;

/**
 * 订单请求信息
 * 
 * @author Shotgun
 */
public class AppOrderRequestModel extends BaseRequest
{

	String	orderId;
	int		levelId;
	short	payType;
	int		payStatus;
	int		method;

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public int getLevelId()
	{
		return levelId;
	}

	public void setLevelId(int levelId)
	{
		this.levelId = levelId;
	}

	public short getPayType()
	{
		return payType;
	}

	public void setPayType(short payType)
	{
		this.payType = payType;
	}

	public int getMethod()
	{
		return method;
	}

	public void setMethod(int method)
	{
		this.method = method;
	}

	public int getPayStatus()
	{
		return payStatus;
	}

	public void setPayStatus(int payStatus)
	{
		this.payStatus = payStatus;
	}

}
