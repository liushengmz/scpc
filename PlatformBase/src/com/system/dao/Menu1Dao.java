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
import com.system.model.Menu1Model;
import com.system.util.StringUtil;

public class Menu1Dao
{
	@SuppressWarnings("unchecked")
	public List<Menu1Model> loadMenu1List()
	{
		String sql = "select * from tbl_menu_1 order by sort,id asc";
		
		return (List<Menu1Model>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Menu1Model> list = new ArrayList<Menu1Model>();
				
				Menu1Model model = null;
				
				while(rs.next())
				{
					model = new Menu1Model();
					
					model.setId(rs.getInt("id"));
					model.setMenuHeadId(rs.getInt("head_id"));
					model.setName(StringUtil.getString(rs.getString("name"),"MenuName"));
					model.setRemark(StringUtil.getString(rs.getString("remark"),"DefaultRemark"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public Map<String, Object> loadMenu1(int menuHeadId,int pageIndex)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from comsum_config.tbl_menu_1 a left join comsum_config.tbl_menu_head b on a.head_id = b.id ";
		String query = "";
		if(menuHeadId>0)
			query = " where head_id = " + menuHeadId;
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
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		map.put("list", new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " *,b.name head_name ")  + query + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Menu1Model> list = new ArrayList<Menu1Model>();
				 
				while(rs.next())
				{
					Menu1Model model = new Menu1Model();
					model.setId(rs.getInt("id"));
					model.setMenuHeadId(rs.getInt("head_id"));
					model.setMenuHeadName(StringUtil.getString(rs.getString("head_name"),""));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setRemark(StringUtil.getString(rs.getString("remark"),""));
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	public Map<String, Object> loadMenu1(int menuHeadId,int pageIndex,int menu1Id)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from comsum_config.tbl_menu_1 a left join comsum_config.tbl_menu_head b on a.head_id = b.id WHERE 1=1 ";
		String query = "";
		if(menuHeadId>0)
			query = " AND head_id = " + menuHeadId;
		
		if(menu1Id>0)
		{
			query = " AND a.id="+menu1Id;
		}
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
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		map.put("list", new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " *,b.name head_name ")  + query + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Menu1Model> list = new ArrayList<Menu1Model>();
				 
				while(rs.next())
				{
					Menu1Model model = new Menu1Model();
					model.setId(rs.getInt("id"));
					model.setMenuHeadId(rs.getInt("head_id"));
					model.setMenuHeadName(StringUtil.getString(rs.getString("head_name"),""));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setSort(rs.getInt("sort"));
					model.setRemark(StringUtil.getString(rs.getString("remark"),""));
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	public Menu1Model loadMenu1ModelById(int id)
	{
		String sql = "select * from comsum_config.tbl_menu_1 where id = " + id;
		return (Menu1Model)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					Menu1Model model = new Menu1Model();
					model.setId(rs.getInt("id"));
					model.setMenuHeadId(rs.getInt("head_id"));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setRemark(StringUtil.getString(rs.getString("remark"),""));
					return model;
				}
				return null;
			}
		});
	}
	
	public boolean updateMenu1Model(Menu1Model model)
	{
		String sql = "update comsum_config.tbl_menu_1 set head_id = " + model.getMenuHeadId() + ", name = '" + model.getName() + "',remark = '" + model.getRemark() + "' where id =" + model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateMenu1Model(int id,int sort)
	{
		String sql = "update comsum_config.tbl_menu_1 set sort ="+sort+" where id="+id;
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean addMenu1Model(Menu1Model model)
	{
		String sql = "insert into comsum_config.tbl_menu_1 (head_id,name,remark) value(" + model.getMenuHeadId() + ",'" + model.getName() + "','" + model.getRemark() + "')";
		return new JdbcControl().execute(sql);
	}
	
	public List<Menu1Model> loadMenu1NameById(int id)
	{
		String sql = "SELECT a.name,a.id FROM comsum_config.tbl_menu_1 a "
				+ "LEFT JOIN comsum_config.tbl_menu_head b ON a.head_id = b.id WHERE b.`id`="+id;
		
		
		final List<Menu1Model> list = new ArrayList<Menu1Model>();
		
		new JdbcControl().query(sql, 
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						
						Menu1Model model = null;
						while (rs.next()) {
							model = new Menu1Model();
							model.setId(rs.getInt("id"));
							model.setName("name"); 
							list.add(model);
						}
						return list;
					}
				});
		return list;
	}
	
}
