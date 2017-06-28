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
import com.system.model.CpPushUrlModel;
import com.system.util.StringUtil;

public class CpPushUrlDao
{
	@SuppressWarnings("unchecked")
	public List<CpPushUrlModel> loadcpPushUrl()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_push_url";
		return (List<CpPushUrlModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpPushUrlModel> list = new ArrayList<CpPushUrlModel>();
				while(rs.next())
				{
					CpPushUrlModel model = new CpPushUrlModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setUrl(StringUtil.getString(rs.getString("url"), ""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	public Map<String, Object> loadCpPushUrl(int cpId,int pageIndex)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_push_url a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp b on a.cp_id = b.id where 1=1 ";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(cpId>0)
		{
			sql += " and a.cp_id = " + cpId;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by convert(b.short_name using gbk) asc ";
		
		JdbcControl control = new JdbcControl();
		
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.short_name ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpPushUrlModel> list = new ArrayList<CpPushUrlModel>();
				while(rs.next())
				{
					CpPushUrlModel model = new CpPushUrlModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setRefCount(rs.getInt("ref_count"));
					model.setUrl(StringUtil.getString(rs.getString("url"), ""));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setHoldAmount(rs.getFloat("hold_amount"));
					model.setHoldStartCount(rs.getInt("hold_start"));
					model.setIsRealTime(rs.getInt("is_realtime"));
					model.setCpName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setCurAmount(rs.getFloat("amount"));
					model.setLastDate(StringUtil.getString(rs.getString("lastDate"), ""));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public CpPushUrlModel loadCpPushUrlModelById(int id)
	{
		String sql = "select a.*,b.short_name from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_push_url a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp b on a.cp_id = b.id where a.id = " + id;
		
		return (CpPushUrlModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				CpPushUrlModel model = new CpPushUrlModel();
				
				if(rs.next())
				{
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setRefCount(rs.getInt("ref_count"));
					model.setUrl(StringUtil.getString(rs.getString("url"), ""));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setHoldAmount(rs.getFloat("hold_amount"));
					model.setHoldStartCount(rs.getInt("hold_start"));
					model.setIsRealTime(rs.getInt("is_realtime"));
					model.setCpName(StringUtil.getString(rs.getString("short_name"), ""));
				}
				
				return model;
			}
		});
		
	}
	
	public boolean addCpPushUrl(CpPushUrlModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_push_url(name,ref_count,url,cp_id,hold_percent,hold_amount,hold_start) values('"+ model.getName() 
		+"',"+ model.getRefCount() +",'"+ model.getUrl() +"',"+ model.getCpId() +","+ model.getHoldPercent() +","+ model.getHoldAmount() +","+ model.getHoldStartCount() +")";
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateCpPushUrl(CpPushUrlModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_push_url set name = '"+ model.getName() +"',ref_count=" + model.getRefCount() + ",url='" + model.getUrl() + "',cp_id=" + model.getCpId() + ",hold_percent=" + model.getHoldPercent() + ",hold_amount=" + model.getHoldAmount() + ",hold_start=" + model.getHoldStartCount() + " where id=" + model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
}
