package com.system.model;

public class PhoneLocateModel
{
	private String phonePre;
	private int provinceId;
	private int cityId;
	private int operator;
	
	public String getPhonePre()
	{
		return phonePre;
	}
	public int getProvinceId()
	{
		return provinceId;
	}
	public int getCityId()
	{
		return cityId;
	}
	public int getOperator()
	{
		return operator;
	}
	public void setPhonePre(String phonePre)
	{
		this.phonePre = phonePre;
	}
	public void setProvinceId(int provinceId)
	{
		this.provinceId = provinceId;
	}
	public void setCityId(int cityId)
	{
		this.cityId = cityId;
	}
	public void setOperator(int operator)
	{
		this.operator = operator;
	}
	
	@Override
	public String toString()
	{
		return "phonePre:" + phonePre + ";provinceId:" + provinceId + ";cityId:" + cityId + ";operator:" + operator;
	}
}
