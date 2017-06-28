package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.cache.RightConfigCacheMgr;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.Menu1Model;
import com.system.model.Menu2Model;
import com.system.model.MenuHeadModel;
import com.system.model.UserModel;
import com.system.model.UserRightModel;

public class RightAccessDao
{
	@SuppressWarnings("unchecked")
	public List<UserRightModel> loadUserRight()
	{
		String sql = "SELECT a.id userId,f.`id` headId,e.id menu1Id,d.id menu2Id FROM tbl_user a "
				+ "LEFT JOIN tbl_group_user b ON a.id = b.`user_id` "
				+ "LEFT JOIN tbl_group_right c ON c.`group_id` = b.`group_id` "
				+ "LEFT JOIN tbl_menu_2 d ON d.`id` = c.`menu_2_id` "
				+ "LEFT JOIN tbl_menu_1 e ON d.`menu_1_id` = e.`id` "
				+ "LEFT JOIN tbl_menu_head f ON e.`head_id` = f.`id` "
				+ "where a.status = 1 ORDER BY f.sort,e.sort,d.sort ASC";
		
		return (List<UserRightModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UserRightModel> userRightList = new ArrayList<UserRightModel>();
				int userId,menuHeadId,menu1Id,menu2Id = 0;
				while(rs.next())
				{
					userId = rs.getInt("userId");
					menuHeadId = rs.getInt("headId");
					menu1Id = rs.getInt("menu1Id");
					menu2Id = rs.getInt("menu2Id");
					
					if(0==menu2Id)
						continue;
					
					analyRight(userRightList,userId,menuHeadId,menu1Id,menu2Id);
				}
				
				return userRightList;
			}
		});
		
	}
	
	public void analyRight(List<UserRightModel> list,int userId,int menuHeadId,int menu1Id,int menu2Id)
	{
		boolean existUser = false;
		UserRightModel userRightModel = null;
		for(UserRightModel model : list)
		{
			if(model.getUser()!=null && model.getUser().getId()==userId)
			{
				userRightModel = model;
				existUser = true;
				break;
			}
		}
		
		if(!existUser)
		{
			userRightModel = new UserRightModel();
			for(UserModel model : RightConfigCacheMgr.userListCache)
			{
				if(model.getId()==userId)
					userRightModel.setUser(model);
			}
			list.add(userRightModel);
		}
		
		List<MenuHeadModel> menuHeadList = userRightModel.getMenuHeadList();
		List<Menu1Model> menu1List = userRightModel.getMenu1List();
		List<Menu2Model> menu2List = userRightModel.getMenu2List();
		
		if(menuHeadList==null)
		{
			menuHeadList = new ArrayList<MenuHeadModel>();
			userRightModel.setMenuHeadList(menuHeadList);
		}
		
		if(menu1List==null)
		{
			menu1List = new ArrayList<Menu1Model>();
			userRightModel.setMenu1List(menu1List);
		}
		
		if(menu2List==null)
		{
			menu2List = new ArrayList<Menu2Model>();
			userRightModel.setMenu2List(menu2List);
		}
		
		boolean existMenuRight = false;
		
		for(MenuHeadModel model : menuHeadList)
		{
			if(model.getId()==menuHeadId)
			{
				existMenuRight = true;
				break;
			}
		}
		
		if(!existMenuRight)
		{
			for(MenuHeadModel model : RightConfigCacheMgr.menuHeadListCache)
			{
				if(model.getId() == menuHeadId)
				{
					menuHeadList.add(model);
					break;
				}
			}
		}
		
		existMenuRight = false;
		
		for(Menu1Model model : menu1List)
		{
			if(model.getId()==menu1Id)
			{
				existMenuRight = true;
				break;
			}
		}
		
		if(!existMenuRight)
		{
			for(Menu1Model model : RightConfigCacheMgr.menu1ListCache)
			{
				if(model.getId() == menu1Id)
				{
					menu1List.add(model);
					break;
				}
			}
		}
		
		existMenuRight = false;
		
		for(Menu2Model model : menu2List)
		{
			if(model.getId()==menu2Id)
			{
				existMenuRight = true;
				break;
			}
		}
		
		if(!existMenuRight)
		{
			for(Menu2Model model : RightConfigCacheMgr.menu2ListCache)
			{
				if(model.getId() == menu2Id)
				{
					menu2List.add(model);
					break;
				}
			}
		}
	}	
	
}
