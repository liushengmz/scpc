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
import com.system.model.CpJieSuanLvModel;
import com.system.util.StringUtil;

public class CpJieSuanLvDao
{
	public Map<String, Object> loadJieSuanLv(int cpId,int spId,int pageIndex)
	{
		String params = " a.id,c.short_name cp_name,d.short_name sp_name,b.name sp_trone_name,a.jiesuanlv ";
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		
		sql += " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_jiesuan a";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone b on a.sp_trone_id = b.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp c on a.cp_id = c.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d on b.sp_id = d.id where 1=1 ";
		
		String  orders = " order by c.short_name,d.short_name,b.name asc";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String query = "";
		
		if(cpId>0)
			query += " and c.id = " + cpId;
		
		if(spId>0)
			query += " and d.id = " + spId;
		
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
		
		map.put("list", new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, params)  + query + orders + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpJieSuanLvModel> list = new ArrayList<CpJieSuanLvModel>();
				 
				while(rs.next())
				{
					CpJieSuanLvModel model = new CpJieSuanLvModel();
					model.setId(rs.getInt("id"));
					model.setSpName(StringUtil.getString(rs.getString("sp_name"),""));
					model.setCpName(StringUtil.getString(rs.getString("cp_name"),""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"),""));
					model.setJieSuanLv(rs.getFloat("jiesuanlv"));
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	public void updateJieSuandLv(int id,float value)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_jiesuan set jiesuanlv = '" + value + "' where id = " + id;
		
		new JdbcControl().execute(sql);
	}
}
