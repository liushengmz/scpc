package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;

public class AlarmDao
{
	private JdbcControl control = new JdbcControl();
	
	public Map<Integer, String> loadTroneIdLastTime(String tableName)
	{
		String sql = " SELECT  MAX(b.create_date) create_date,d.id sp_trone_id ";
		
		sql += " FROM  ";
		sql += " (SELECT trone_id,MAX(id) max_id FROM " + Constant.DB_DAILY_LOG + ".tbl_mr_" + tableName + " GROUP BY trone_id) a ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_LOG + ".tbl_mr_" + tableName + " b   ON a.max_id = b.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_trone c ON a.trone_id = c.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp_trone d ON c.sp_trone_id = d.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.sp_id = e.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_user f ON e.commerce_user_id = f.id ";
		sql += " WHERE a.trone_id > 0 ";
		//这里只查询要监控的数据
		sql += " AND d.is_watch_data = 1 AND d.status = 1 AND HOUR(NOW()) >= d.alarm_start_hour AND HOUR(NOW()) <= d.alarm_end_hour";
		sql += " GROUP BY d.id ";
		sql += " ORDER BY e.id,d.id ";
		
		
		final Map<Integer, String> map = new HashMap<Integer, String>();
		
		control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				while(rs.next())
				{
					map.put(rs.getInt("sp_trone_id"), rs.getString("create_date"));
				}
				return null;
			}
		});
		
		return map;
	}
	
	public Map<String, String> loadSpTroneAlarmData(int spTroneId)
	{
		String sql = "SELECT * FROM " + Constant.DB_DAILY_LOG + ".tbl_alarm_log WHERE sp_trone_id = " + spTroneId + " ORDER BY id DESC LIMIT 1";
		
		final Map<String, String> map = new HashMap<String, String>();
		
		control.query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					map.put("createDate", rs.getString("create_date"));
				}
				return null;
			}
		});
		
		return map;
	}
	
	public void  rcordAlarmData(Map<String, String> data)
	{
		String sql = "INSERT INTO " + Constant.DB_DAILY_LOG
				+ ".tbl_alarm_log(sp_trone_id,user_id,phone,msg) VALUES("
				+ data.get("spTroneId") + "," + data.get("userId") + ",'"
				+ data.get("phone") + "','" + data.get("msg") + "')";
		control.execute(sql);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> loadAlarmInfo(int spTroneId)
	{
		String sql = " SELECT c.id user_id,c.phone,c.nick_name user_name,a.name sp_trone_name,b.short_name sp_name ";
		sql += " FROM " + Constant.DB_DAILY_CONFIG + ".tbl_sp_trone a ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp b ON a.sp_id = b.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_user c ON b.commerce_user_id = c.id ";
		sql += " WHERE a.id = " + spTroneId;
		
		final Map<String, String> map = new HashMap<String, String>();
		
		return (Map<String, String>)control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					map.put("userId", String.valueOf(rs.getInt("user_id")));
					
					map.put("userName", rs.getString("user_name"));
					
					map.put("spTroneName", rs.getString("sp_trone_name"));
					
					map.put("spName", rs.getString("sp_name"));
					
					map.put("phone", rs.getString("phone"));
					
					return map;
				}
				return null;
			}
		});
	}
	
}
