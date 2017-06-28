
package com.system.model;

import java.util.Date;

public class LvLevelModel
{
	int		id;
	int		level;
	int		price;
	String	remark;
	Date	createDate;
	String appkey;
	
	public void setId(int id)
	{
		this.id = id;
	}
	public void setLevel(int level)
	{
		this.level = level;
	}
	public void setPrice(int price)
	{
		this.price = price;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public void setCreateDate(Date date)
	{
		this.createDate = date;
	}
	public int getId()
	{
		return id;
	}
	public int getLevel()
	{
		return level;
	}
	public int getPrice()
	{
		return price;
	}
	public String getRemark()
	{
		return remark;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public String getAppkey()
	{
		return appkey;
	}
	public void setAppkey(String appkey)
	{
		this.appkey = appkey;
	}

}
