package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.MenuHeadDao;
import com.system.model.MenuHeadModel;

public class MenuHeadServer
{
	public List<MenuHeadModel> loadMenuHeadList()
	{
		return new MenuHeadDao().loadMenuHeadList();
	}
	
	public Map<String, Object> loadMenuHead(int pageIndex,String name,int sort)
	{
		return new MenuHeadDao().loadMenuHead(pageIndex,name, sort);
	}
	
	public boolean addMenuHead(MenuHeadModel model)
	{
		return new MenuHeadDao().addMenuHead(model);
	}
	
	public List<String> loadMenuName(int id)
	{
		return new MenuHeadDao().loadMenuName(id);
	}
	
	public MenuHeadModel loadMenuById(int id)
	{
		return new MenuHeadDao().loadMenuById(id);
	}
	
	public boolean deletMenu(int id)
	{
		return new MenuHeadDao().deletMenu(id);
	}
	
	public boolean updataMenu(MenuHeadModel model)
	{
		return new MenuHeadDao().updataMenu(model);
	}
	
	public boolean updateMenu(int id,int sort)
	{
		return new MenuHeadDao().updateMenu(id, sort);
	}
}
