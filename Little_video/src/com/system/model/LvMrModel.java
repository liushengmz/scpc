
package com.system.model;

import java.util.Date;

public class LvMrModel
{
	int		id;
	String	orderId;
	int		payType;
	int		price;
	String	appkey;
	String	channel;
	String	payOrderId;
	int		payTypeId;

	/**
	 * 0扣量1不扣量
	 */
	int		status;
	int		levelId;
	Date	createDate;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(String orderId)
	{
		this.orderId = orderId;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public String getAppkey()
	{
		return appkey;
	}

	public void setAppkey(String appkey)
	{
		this.appkey = appkey;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getPayOrderId()
	{
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId)
	{
		this.payOrderId = payOrderId;
	}

	/**
	 * @param status
	 *            0扣量1不扣量
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *            0扣量1不扣量
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getLevelId()
	{
		return levelId;
	}

	public void setLevelId(int levelId)
	{
		this.levelId = levelId;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public int getPayType()
	{
		return payType;
	}

	public void setPayType(int payType)
	{
		this.payType = payType;
	}

	public int getPayTypeId()
	{
		return payTypeId;
	}

	public void setPayTypeId(int payTypeId)
	{
		this.payTypeId = payTypeId;
	}

}
