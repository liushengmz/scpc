package com.system.model;

public class Menu2Model
{
	private int id;
	private int menu1Id;
	private String name;
	private String url;
	private String remark;
	private int sort;
	private int menuHeadId;
	private String menuHeadName;
	private String menu1Name;
	private String actionUrl;
	
	public int getMenuHeadId()
	{
		return menuHeadId;
	}
	public void setMenuHeadId(int menuHeadId)
	{
		this.menuHeadId = menuHeadId;
	}
	public String getMenuHeadName()
	{
		return menuHeadName;
	}
	public void setMenuHeadName(String menuHeadName)
	{
		this.menuHeadName = menuHeadName;
	}
	public String getMenu1Name()
	{
		return menu1Name;
	}
	public void setMenu1Name(String menu1Name)
	{
		this.menu1Name = menu1Name;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getMenu1Id()
	{
		return menu1Id;
	}
	public void setMenu1Id(int menu1Id)
	{
		this.menu1Id = menu1Id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getActionUrl()
	{
		return actionUrl;
	}
	public void setActionUrl(String actionUrl)
	{
		this.actionUrl = actionUrl;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
