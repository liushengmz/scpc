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
import com.system.model.MenuHeadModel;
import com.system.util.StringUtil;

public class MenuHeadDao
{
	@SuppressWarnings("unchecked")
	public List<MenuHeadModel> loadMenuHeadList()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_menu_head` order by sort,id asc";
		
		return (List<MenuHeadModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<MenuHeadModel> list = new ArrayList<MenuHeadModel>();
				
				MenuHeadModel model = null;
				
				while(rs.next())
				{
					model = new MenuHeadModel();
					model.setId(rs.getInt("id"));
					model.setName(rs.getString("name"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public Map<String, Object> loadMenuHead(int pageIndex,String menuName,int sort)
	{
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING+" from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_menu_head` WHERE 1=1 ";
		
		if(!StringUtil.isNullOrEmpty(menuName))
		{
			sql += " AND name LIKE '%"+menuName+"%' ";
		}
		
		if(sort>0){
			sql += " AND sort="+sort+" ";
		}
		
		sql += " order by sort,id asc ";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		int rowcount = (Integer)control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				});
		
		result.put("rows", rowcount);
		
		result.put("list",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<MenuHeadModel> list = new ArrayList<MenuHeadModel>();
						
						MenuHeadModel model = null;
						
						while(rs.next())
						{
							model = new MenuHeadModel();
							model.setId(rs.getInt("id"));
							model.setName(rs.getString("name"));
							model.setRemark(rs.getString("remark"));
							model.setSort(rs.getShort("sort"));
							list.add(model);
						}
						
						return list;
					}
				}) );
		
		return result;
		
	}
	
	public boolean addMenuHead(MenuHeadModel model)
	{
		String sql = "INSERT INTO " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_menu_head`(name,remark,sort) VALUE('"
				+ model.getName()+"','"
				+ model.getRemark()+"',"
				+ model.getSort()+")";
		
		return new JdbcControl().execute(sql);
	}
	
	public List<String> loadMenuName(int id)
	{
		String sql = "SELECT name FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_menu_head` WHERE 1=1";
		
		if(id>0)
		{
			sql += " AND id<>"+id;
		}
		
		final List<String> list =new ArrayList<String>();
		new JdbcControl().query(sql,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						
						while (rs.next()) {
							
							list.add(rs.getString(1));
							
						}
						return list;
					}
				});
		
		return list;
	}
	
	public MenuHeadModel loadMenuById(int id)
	{
		String sql = "SELECT * FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_menu_head` WHERE id="+id;
		
		final MenuHeadModel model = new MenuHeadModel();
		
		new JdbcControl().query(sql, 
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						
						if(rs.next())
						{
							
							model.setId(rs.getInt("id"));
							model.setName(rs.getString("name"));
							model.setRemark(rs.getString("remark"));
							model.setSort(rs.getInt("sort"));
							
						}
						return model;
					}
				});
		return model;
	}
	
	public boolean deletMenu(int id)
	{
		String sql = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_menu_head` WHERE id="+id;
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean updataMenu(MenuHeadModel model)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_menu_head` SET "
				+ "name='"+model.getName()+"',"
				+ "remark='"+model.getRemark()+"',"
				+ "sort="+model.getSort()+" WHERE id="+model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateMenu(int id,int sort)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_menu_head` SET "
				+ "sort="+sort+" WHERE id="+id;
		return new JdbcControl().execute(sql);
	}
	
	public static void main(String[] args) {
		MenuHeadDao dao = new MenuHeadDao();
		
		List<String> list = dao.loadMenuName(4);
		
	}
	
	
	
}
