
package com.system.model;

import java.util.Date;

public class LvRequestModel
{
	int		id;
	String	imei;
	int		payType;
	int		price;
	String	orderid;
	Date	createDate;
	String	appkey;
	int		level;
	String	channel;
	/*
	 * sdk id
	 */
	int		payTypeId;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getImei()
	{
		return imei;
	}

	public void setImei(String imei)
	{
		this.imei = imei;
	}

	public int getPayType()
	{
		return payType;
	}

	public void setPayType(int payType)
	{
		this.payType = payType;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}

	public String getOrderid()
	{
		return orderid;
	}

	public void setOrderid(String orderid)
	{
		this.orderid = orderid;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public String getAppkey()
	{
		return appkey;
	}

	public void setAppkey(String appkey)
	{
		this.appkey = appkey;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
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
