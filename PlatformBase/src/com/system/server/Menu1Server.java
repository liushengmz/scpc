package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.Menu1Dao;
import com.system.model.Menu1Model;

public class Menu1Server
{
	public List<Menu1Model> loadMenu1List()
	{
		return new Menu1Dao().loadMenu1List();
	}
	
	public Map<String, Object> loadMenu1(int menuHeadId,int pageIndex)
	{
		return new Menu1Dao().loadMenu1(menuHeadId, pageIndex);
	}
	
	public Map<String,Object> loadMenu1(int pageIndex,int menuHeadId,int menu1Id)
	{
		return new Menu1Dao().loadMenu1(menuHeadId, pageIndex, menu1Id);
	}
	
	public Menu1Model loadMenu1ModelById(int id)
	{
		return new Menu1Dao().loadMenu1ModelById(id);
	}
	
	public boolean updateMenu1Model(Menu1Model model)
	{
		return new Menu1Dao().updateMenu1Model(model);
	}
	
	public boolean updateMenu1Model(int id,int sort)
	{
		return new Menu1Dao().updateMenu1Model(id, sort);
	}
	
	public boolean addMenu1Model(Menu1Model model)
	{
		return new Menu1Dao().addMenu1Model(model);
	}
	
	public List<Menu1Model> loadMenu1Name(int id)
	{
		return new Menu1Dao().loadMenu1NameById(id);
	}
}
