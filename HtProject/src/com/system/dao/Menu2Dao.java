package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.Menu2Model;
import com.system.model.UserMenuModel;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;

public class Menu2Dao
{
	@SuppressWarnings("unchecked")
	public List<Menu2Model> loadMenu2List()
	{
		String sql = "select a.id menu2Id,a.name menu2Name,b.id menu1Id,b.name menu1Name,"
				+ "c.id menuHeadId,c.name menuHeadName,a.url,a.action_url,a.remark from tbl_menu_2 a left join tbl_menu_1 b "
				+ "on a.menu_1_id = b.id left join tbl_menu_head c on b.head_id = c.id "
				+ "order by c.sort,b.sort,a.sort asc";
		
		return (List<Menu2Model>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Menu2Model> list = new ArrayList<Menu2Model>();
				
				Menu2Model model = null;
				
				while(rs.next())
				{
					model = new Menu2Model();
					
					model.setId(rs.getInt("menu2Id"));
					model.setMenu1Id(rs.getInt("menu1Id"));
					model.setName(StringUtil.getString(rs.getString("menu2Name"), "Menu2Name"));
					model.setUrl(StringUtil.getString(rs.getString("url"), "url"));
					
					model.setMenuHeadId(rs.getInt("menuHeadId"));
					model.setMenu1Name(StringUtil.getString(rs.getString("menu1Name"), "Menu1Name"));
					model.setMenuHeadName(StringUtil.getString(rs.getString("menuHeadName"), "MenuHeadName"));
					
					model.setActionUrl(StringUtil.getString(rs.getString("action_url"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public Map<String, Object> loadMenu2(int menuHeadId,int menu1Id,int pageIndex)
	{
		String params = " a.id menu2Id,a.name menu2Name,b.id menu1Id,b.name menu1Name,c.id menuHeadId,c.name menuHeadName,a.url,a.action_url,a.remark,a.sort  ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_2 a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_1 b "
				+ "on a.menu_1_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_head c on b.head_id = c.id where 1=1 ";
		
		String sort = " order by c.sort,b.sort,a.sort asc ";
		
		String query = "";
		
		if(menuHeadId>0)
			query = " and b.head_id = " + menuHeadId;
		
		if(menu1Id>0)
			query += " and a.menu_1_id = " + menu1Id;
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new  HashMap<String, Object>();
		
		map.put("rows", new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) ") + query, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		}));
		
		
		map.put("list", new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, params)  + query + sort + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Menu2Model> list = new ArrayList<Menu2Model>();
				 
				while(rs.next())
				{
					Menu2Model model = new Menu2Model();
					
					model.setId(rs.getInt("menu2Id"));
					model.setMenu1Id(rs.getInt("menu1Id"));
					model.setName(StringUtil.getString(rs.getString("menu2Name"), "Menu2Name"));
					model.setUrl(StringUtil.getString(rs.getString("url"), "url"));
					model.setSort(rs.getInt("sort"));
					model.setMenuHeadId(rs.getInt("menuHeadId"));
					model.setMenu1Name(StringUtil.getString(rs.getString("menu1Name"), "Menu1Name"));
					model.setMenuHeadName(StringUtil.getString(rs.getString("menuHeadName"), "MenuHeadName"));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					model.setActionUrl(StringUtil.getString(rs.getString("action_url"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	
	public Menu2Model loadMenu2ById(int id)
	{
		String sql = "select a.id menu2Id,a.name menu2Name,b.id menu1Id,b.name menu1Name,"
				+ "c.id menuHeadId,c.name menuHeadName,a.url,a.action_url,a.remark from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_2 a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_1 b "
				+ "on a.menu_1_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_head c on b.head_id = c.id where a.id = " + id;
		
		return (Menu2Model)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					Menu2Model model = new Menu2Model();
					
					model.setId(rs.getInt("menu2Id"));
					model.setMenu1Id(rs.getInt("menu1Id"));
					model.setName(StringUtil.getString(rs.getString("menu2Name"), "Menu2Name"));
					model.setUrl(StringUtil.getString(rs.getString("url"), "url"));
					
					model.setMenuHeadId(rs.getInt("menuHeadId"));
					model.setMenu1Name(StringUtil.getString(rs.getString("menu1Name"), "Menu1Name"));
					model.setMenuHeadName(StringUtil.getString(rs.getString("menuHeadName"), "MenuHeadName"));
					
					model.setActionUrl(StringUtil.getString(rs.getString("action_url"), ""));
					//增加备注字段
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
					return model;
				}
				return null;
			}
		});
	}
	
	public void addMenu2(Menu2Model model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_2(menu_1_id,name,url,action_url,remark)value("
				+ model.getMenu1Id() + ",'" + model.getName() + "','"
				+ model.getUrl() + "','" + model.getActionUrl() + "','"+SqlUtil.sqlEncode(model.getRemark())+"')";
		new JdbcControl().execute(sql);
	}

	public void updateMenu2(Menu2Model model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_2 set menu_1_id = "
				+ model.getMenu1Id() + " ,name = '" + model.getName()
				+ "',url = '" + model.getUrl() + "',action_url = '"
				+ model.getActionUrl() + "',remark='"+SqlUtil.sqlEncode(model.getRemark())+"' where id =" + model.getId();
		new JdbcControl().execute(sql);
	}
	
	public boolean updateMenu2(int id,int sort)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_2 set sort="+sort+" where id="+id;
		
		return new JdbcControl().execute(sql);
	}
	/**
	 * 菜单管理增加角色查询字段
	 * @param menuHeadId
	 * @param menu1Id
	 * @param groupId
	 * @param pageIndex
	 * @return
	 */
	public Map<String, Object> loadMenu2(int menuHeadId,int menu1Id,int groupId,int pageIndex)
	{
		String params = " a.id menu2Id,a.name menu2Name,b.id menu1Id,b.name menu1Name,c.id menuHeadId,c.name menuHeadName,a.url,a.action_url,a.remark,a.sort  ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_2 a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_1 b "
				+ "on a.menu_1_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_head c on b.head_id = c.id "
				+ " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_group_right d on d.menu_2_id=a.id where 1=1 ";
		
		String sort = " order by c.sort,b.sort,a.sort asc ";
		
		String query = "";
		
		if(menuHeadId>0)
			query = " and b.head_id = " + menuHeadId;
		
		if(menu1Id>0)
			query += " and a.menu_1_id = " + menu1Id;
		
		if(groupId>0)
			query += " and d.group_id = " + groupId;
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new  HashMap<String, Object>();
		
		map.put("rows", new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) ") + query, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		}));
		
		
		map.put("list", new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, params)  + query + sort + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Menu2Model> list = new ArrayList<Menu2Model>();
				 
				while(rs.next())
				{
					Menu2Model model = new Menu2Model();
					
					model.setId(rs.getInt("menu2Id"));
					model.setMenu1Id(rs.getInt("menu1Id"));
					model.setName(StringUtil.getString(rs.getString("menu2Name"), "Menu2Name"));
					model.setUrl(StringUtil.getString(rs.getString("url"), "url"));
					model.setSort(rs.getInt("sort"));
					model.setMenuHeadId(rs.getInt("menuHeadId"));
					model.setMenu1Name(StringUtil.getString(rs.getString("menu1Name"), "Menu1Name"));
					model.setMenuHeadName(StringUtil.getString(rs.getString("menuHeadName"), "MenuHeadName"));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					model.setActionUrl(StringUtil.getString(rs.getString("action_url"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	/**
	 * 根据用户的菜单
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserMenuModel>loadUserMenuByUserId(int userId)
	{
		String sql="SELECT a.id AS menu_2_id, a.name AS menu_2_name,a.menu_1_id,b.name AS menu_1_name,a.remark FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_2  a "
				+" LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_1  b ON a.menu_1_id=b.id" 
				+" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_group_right c ON c.menu_2_id= a.id"  
				+" WHERE c.group_id IN (SELECT group_id FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_group_user d where d.user_id="+userId+")";
		
		return (List<UserMenuModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UserMenuModel> list = new ArrayList<UserMenuModel>();
				
				UserMenuModel model = null;
				
				while(rs.next())
				{
					model = new UserMenuModel();
					
					model.setMenu_2_id(rs.getInt("menu_2_id"));
					model.setMenu_2_name(StringUtil.getString(rs.getString("menu_2_name"), ""));
					model.setMenu_1_id(rs.getInt("menu_1_id"));
					model.setMenu_1_name(StringUtil.getString(rs.getString("menu_1_name"),""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
//	/**
//	 * 根据用户的菜单
//	 * @param userId
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<UserMenuModel>loadUserMenuByUserId(int userId){
//
//		String sql="SELECT a.id AS menu_2_id, a.name AS menu_2_name,a.menu_1_id,b.name AS menu_1_name,a.remark FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_2  a "
//					+" LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_menu_1  b ON a.menu_1_id=b.id" 
//					+" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_group_right c ON c.menu_2_id= a.id"  
//					+" WHERE c.group_id IN (SELECT group_id FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_group_user d where d.user_id="+userId+")";
//		
//		return (List<UserMenuModel>)new JdbcControl().query(sql, new QueryCallBack()
//		{
//			@Override
//			public Object onCallBack(ResultSet rs) throws SQLException
//			{			
//				List<UserMenuModel> list=new ArrayList<UserMenuModel>();
//				if(rs.next())
//				{
//					UserMenuModel model = new UserMenuModel();
//					model.setMenu_2_id(rs.getInt("menu_2_id"));
//					model.setMenu_2_name(StringUtil.getString(rs.getString("menu_2_name"), ""));
//					model.setMenu_1_id(rs.getInt("menu_1_id"));
//					model.setMenu_1_name(StringUtil.getString(rs.getString("menu_1_name"),""));
//					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
//					list.add(model);
//					
//				}
//				return list;
//			}
//		});
//	
//	}
}	
