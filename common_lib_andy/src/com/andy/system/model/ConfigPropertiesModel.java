package com.andy.system.model;

import com.andy.system.annotation.DbColumn;
import com.andy.system.model.BaseDbModel;

public class ConfigPropertiesModel extends BaseDbModel
{
	@DbColumn(columnName="id")
	private int id;
	
	@DbColumn(columnName="type")
	private int type;
	
	@DbColumn(columnName="key")
	private String key;
	
	@DbColumn(columnName="value")
	private String value;
	
	@DbColumn(columnName="remark")
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
