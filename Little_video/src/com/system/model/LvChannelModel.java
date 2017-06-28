
package com.system.model;

import java.util.Date;

public class LvChannelModel
{
	int		id;
	String	appkey;
	String	channel;
	int		holdPercent;
	int		userId;
	Date	createDate;
	int		wxPay;
	int		aliPay;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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

	public int getHoldPercent()
	{
		return holdPercent;
	}

	public void setHoldPercent(int holdPercent)
	{
		this.holdPercent = holdPercent;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public int getWxPay()
	{
		return wxPay;
	}

	public void setWxPay(int wx_pay)
	{
		this.wxPay = wx_pay;
	}

	public int getAliPay()
	{
		return aliPay;
	}

	public void setAliPay(int ali_pay)
	{
		this.aliPay = ali_pay;
	}

}
