package com.system.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.system.cache.RightConfigCacheMgr;
import com.system.model.Menu2Model;
import com.system.model.MenuHeadModel;
import com.system.model.UserModel;
import com.system.model.UserRightModel;
import com.system.util.StringUtil;

public class UserRightServer
{
	public static void main(String[] args)
	{
		loadRightServer();
	}
	
	public static void loadRightServer()
	{
		RightConfigCacheMgr.refreshAllCache();
		
		UserModel user = new UserModel();
		user.setId(543);
		user.setName("Andy");
		user.setPassword("708718e82ccc4c78ffb67b33737cd436");
		
		UserRightModel userRightModel = RightServer.loadUserRightModel(user);
		
		if(userRightModel==null)
			return;
		
		List<MenuHeadModel> headList = userRightModel.getMenuHeadList();
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		for(MenuHeadModel model : headList)
		{
			LinkedHashMap<String, List<Menu2Model>> map = RightServer.loadUserLeftMenu(user, model.getId());
			
			LinkedHashMap<String, Object> menu1Map = new LinkedHashMap<String, Object>();
			list.add(menu1Map);
			
			menu1Map.put("id",model.getId());
			menu1Map.put("name",model.getName());
			
			
			List<Map<String, Object>> menu2List = new ArrayList<Map<String,Object>>();
			menu1Map.put("childs", menu2List);
			
			for(String key : map.keySet())
			{
				LinkedHashMap<String, Object> menu2Map = new LinkedHashMap<String, Object>();
				
				menu2List.add(menu2Map);
				
				List<Map<String,Object>> menu3List = new ArrayList<Map<String,Object>>();
				
				List<Menu2Model> listMenu2 = map.get(key);
				
				for(Menu2Model menu3 : listMenu2)
				{
					LinkedHashMap<String, Object> menu3map = new LinkedHashMap<String, Object>();
					menu2Map.put("id", menu3.getMenu1Id());
					menu3map.put("id", menu3.getId());
					menu3map.put("url", menu3.getUrl());
					menu3map.put("name", menu3.getName());
					menu3List.add(menu3map);
				}
				
				menu2Map.put("name", key);
				
				menu2Map.put("childs", menu3List);
			}
		}
		
		System.out.println(StringUtil.getJsonArrayFromObject(list));
	}
}
