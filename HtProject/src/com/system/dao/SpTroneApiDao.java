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
import com.system.model.SpTroneApiModel;
import com.system.util.StringUtil;

public class SpTroneApiDao
{
	@SuppressWarnings("unchecked")
	public List<SpTroneApiModel> loadSpTroneApi()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api";
		
		return (List<SpTroneApiModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpTroneApiModel> list = new ArrayList<SpTroneApiModel>();
				
				SpTroneApiModel model = null;
				
				while(rs.next())
				{
					model = new SpTroneApiModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setMatchField(rs.getInt("match_field"));
					model.setMatchKeyword(StringUtil.getString(rs.getString("match_keyword"), ""));
					model.setApiFields(StringUtil.getString(rs.getString("api_fields"), ""));
					model.setLocateMatch(rs.getInt("locate_match"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	
	public Map<String, Object> loadSpTroneApi(int pageIndex,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api where 1=1 ";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND name LIKE '%" + keyWord + "%' ";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by id desc ";
		
		JdbcControl control = new JdbcControl();
		
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpTroneApiModel> list = new ArrayList<SpTroneApiModel>();
				
				while(rs.next())
				{
					SpTroneApiModel model = new SpTroneApiModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setMatchField(rs.getInt("match_field"));
					model.setMatchKeyword(StringUtil.getString(rs.getString("match_keyword"), ""));
					model.setApiFields(StringUtil.getString(rs.getString("api_fields"), ""));
					model.setLocateMatch(rs.getInt("locate_match"));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public SpTroneApiModel getSpTroneApiById(int id)
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api where id = " + id;
		
		return (SpTroneApiModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SpTroneApiModel model = new SpTroneApiModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setMatchField(rs.getInt("match_field"));
					model.setMatchKeyword(StringUtil.getString(rs.getString("match_keyword"), ""));
					model.setApiFields(StringUtil.getString(rs.getString("api_fields"), ""));
					model.setLocateMatch(rs.getInt("locate_match"));
					
					return model;
				}
				return null;
			}
		});
	}
	
	public boolean addSpTroneApiModel(SpTroneApiModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api(name,match_field,match_keyword,api_fields,"
				+ "locate_match) values(?,?,?,?,?)";
		
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		
		map.put(1, model.getName());
		map.put(2, model.getMatchField());
		map.put(3, model.getMatchKeyword());
		map.put(4, model.getApiFields());
		map.put(5, model.getLocateMatch());
		
		return new JdbcControl().execute(sql, map);
	}
	
	public boolean updateSpTroneApiModel(SpTroneApiModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api set name = ?,match_field = ?,match_keyword = ?,api_fields = ?,"
				+ "locate_match = ? where id = ?";
		
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		
		map.put(1, model.getName());
		map.put(2, model.getMatchField());
		map.put(3, model.getMatchKeyword());
		map.put(4, model.getApiFields());
		map.put(5, model.getLocateMatch());
		map.put(6, model.getId());
		
		return new JdbcControl().execute(sql, map);
	}
	
}

