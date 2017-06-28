package com.system.model;

public class PhoneLocateModel
{
	private int id;
	private String phonePrefix;
	private int cityId;
	private int provinceId;
	private int operatorId;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getPhonePrefix()
	{
		return phonePrefix;
	}
	public void setPhonePrefix(String phonePrefix)
	{
		this.phonePrefix = phonePrefix;
	}
	public int getCityId()
	{
		return cityId;
	}
	public void setCityId(int cityId)
	{
		this.cityId = cityId;
	}
	public int getProvinceId()
	{
		return provinceId;
	}
	public void setProvinceId(int provinceId)
	{
		this.provinceId = provinceId;
	}
	public int getOperatorId()
	{
		return operatorId;
	}
	public void setOperatorId(int operatorId)
	{
		this.operatorId = operatorId;
	}
	
	
}
