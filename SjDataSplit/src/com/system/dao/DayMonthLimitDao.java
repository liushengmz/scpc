package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;

/**
 * 日月限数据库交互类
 * @author Andy.Chen
 *
 */
public class DayMonthLimitDao
{
	public Map<String, Float> loadSpTroneMonthMap(String startDate)
	{
		String sql  = "SELECT sp_trone_id,DATE_FORMAT(fee_date,'%Y%m') month_date,SUM(cur_day_amount) month_amount";
		sql += " FROM daily_log.`tbl_day_month_limit` ";
		sql += " WHERE fee_date >= '" + startDate + "'";
		sql += " GROUP BY sp_trone_id,month_date";
		sql += " ORDER BY sp_trone_id,month_date";
		
		final Map<String, Float> limitMap = new HashMap<String, Float>();
		
		JdbcControl control = new JdbcControl();
		
		control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				while(rs.next())
				{
					limitMap.put(rs.getInt("sp_trone_id") + "-" + rs.getString("month_date"), rs.getFloat("month_amount"));
				}
				
				return null;
			}
		});
		
		return limitMap;
	}
	
	public Map<String, Float> loadSpTroneDayLimit(String startDate)
	{
		String sql = "SELECT sp_trone_id,fee_date,SUM(cur_day_amount) day_amount";
		sql += " FROM daily_log.`tbl_day_month_limit`";
		sql += " WHERE fee_date >= '" + startDate + "'";
		sql += " GROUP BY sp_trone_id,fee_date";
		sql += " ORDER BY sp_trone_id,fee_date";
		
		final Map<String, Float> limitMap = new HashMap<String, Float>();
		
		JdbcControl control = new JdbcControl();
		
		control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				while(rs.next())
				{
					limitMap.put(rs.getInt("sp_trone_id") + "-" + rs.getString("fee_date"), rs.getFloat("day_amount"));
				}
				
				return null;
			}
		});
		
		return limitMap;
	}
	
	public Map<String, Float> loadCpTroneMonthLimit(String startDate)
	{
		String sql = "SELECT cp_id,sp_trone_id,DATE_FORMAT(fee_date,'%Y%m') month_date,SUM(cur_day_amount) month_amount";
		sql += " FROM daily_log.`tbl_day_month_limit`";
		sql += " WHERE fee_date >= '" + startDate + "'";
		sql += " GROUP BY cp_id,sp_trone_id,month_date";
		sql += " ORDER BY cp_id,sp_trone_id,month_date";
		
		final Map<String, Float> limitMap = new HashMap<String, Float>();
		
		JdbcControl control = new JdbcControl();
		
		control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				while(rs.next())
				{
					limitMap.put( rs.getInt("cp_id") + "-" + rs.getInt("sp_trone_id") + "-" + rs.getString("month_date"), rs.getFloat("month_amount"));
				}
				
				return null;
			}
		});
		
		return limitMap;
	}
	
	public Map<String, Float> loadCpTroneDayLimit(String startDate)
	{
		String sql = "SELECT cp_id,sp_trone_id,fee_date,cur_day_amount";
		sql += " FROM daily_log.tbl_day_month_limit";
		sql += " WHERE fee_date >= '" + startDate + "'";
		sql += " ORDER BY cp_id,sp_trone_id,fee_date";
		
		final Map<String, Float> limitMap = new HashMap<String, Float>();
		
		JdbcControl control = new JdbcControl();
		
		control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				while(rs.next())
				{
					limitMap.put( rs.getInt("cp_id") + "-" + rs.getInt("sp_trone_id") + "-" + rs.getString("fee_date"), rs.getFloat("cur_day_amount"));
				}
				
				return null;
			}
		});
		
		return limitMap;
	}
	
}
