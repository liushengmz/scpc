package com.system.model;

import java.util.ArrayList;
import java.util.List;

public class UserRightModel
{
	private UserModel user = null;
	private List<MenuHeadModel> menuHeadList = new ArrayList<MenuHeadModel>();
	private List<Menu1Model> menu1List = new ArrayList<Menu1Model>();
	private List<Menu2Model> menu2List = new ArrayList<Menu2Model>();
	
	public List<MenuHeadModel> getMenuHeadList()
	{
		return menuHeadList;
	}
	public void setMenuHeadList(List<MenuHeadModel> menuHeadList)
	{
		this.menuHeadList = menuHeadList;
	}
	public List<Menu1Model> getMenu1List()
	{
		return menu1List;
	}
	public void setMenu1List(List<Menu1Model> menu1List)
	{
		this.menu1List = menu1List;
	}
	public List<Menu2Model> getMenu2List()
	{
		return menu2List;
	}
	public void setMenu2List(List<Menu2Model> menu2List)
	{
		this.menu2List = menu2List;
	}
	public UserModel getUser()
	{
		return user;
	}
	public void setUser(UserModel user)
	{
		this.user = user;
	}
	
	
	
}
