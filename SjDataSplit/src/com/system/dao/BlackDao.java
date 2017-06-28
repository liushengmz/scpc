package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.util.StringUtil;

public class BlackDao
{
	@SuppressWarnings("unchecked")
	public List<String> loadblackPhoneList()
	{
		String sql = "SELECT DISTINCT(phone) FROM daily_config.`tbl_black`";
		
		return (List<String>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<String> list = new ArrayList<String>();
				
				String phoneNum;
				
				while(rs.next())
				{
					phoneNum = StringUtil.getString(rs.getString("phone"), "");
					
					if(!StringUtil.isNullOrEmpty(phoneNum))
						list.add(phoneNum);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<String> loadblackImsiList()
	{
		String sql = "SELECT DISTINCT(imsi) FROM daily_config.`tbl_black`";
		
		return (List<String>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<String> list = new ArrayList<String>();
				
				String phoneNum;
				
				while(rs.next())
				{
					phoneNum = StringUtil.getString(rs.getString("imsi"), "");
					
					if(!StringUtil.isNullOrEmpty(phoneNum))
						list.add(phoneNum);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<String> loadblackImeiList()
	{
		String sql = "SELECT DISTINCT(imei) FROM daily_config.`tbl_black`";
		
		return (List<String>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<String> list = new ArrayList<String>();
				
				String phoneNum;
				
				while(rs.next())
				{
					phoneNum = StringUtil.getString(rs.getString("imei"), "");
					
					if(!StringUtil.isNullOrEmpty(phoneNum))
						list.add(phoneNum);
				}
				return list;
			}
		});
	}
}
