package com.system.smodel;

public class ConfigPropertiesModel
{
	private int id;
	private int type;
	private String key;
	private String value;
	private String remark;
	
	public int getId()
	{
		return id;
	}
	public int getType()
	{
		return type;
	}
	public String getKey()
	{
		return key;
	}
	public String getValue()
	{
		return value;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	
}
