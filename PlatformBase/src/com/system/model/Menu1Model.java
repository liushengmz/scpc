package com.system.model;

public class Menu1Model
{
	private int id;
	private String menuHeadName;
	private int menuHeadId;
	private String name;
	private String remark;
	private int sort;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getMenuHeadId()
	{
		return menuHeadId;
	}
	public void setMenuHeadId(int menuHeadId)
	{
		this.menuHeadId = menuHeadId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getMenuHeadName()
	{
		return menuHeadName;
	}
	public void setMenuHeadName(String menuHeadName)
	{
		this.menuHeadName = menuHeadName;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
