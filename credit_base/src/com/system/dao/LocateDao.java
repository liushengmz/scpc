package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CityModel;
import com.system.model.ProvinceModel;
import com.system.util.StringUtil;

public class LocateDao
{
	@SuppressWarnings("unchecked")
	public List<ProvinceModel> loadProvinceList()
	{
		String sql = "SELECT * FROM daily_config.tbl_province";
		return (List<ProvinceModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ProvinceModel> list = new ArrayList<ProvinceModel>();
				
				while(rs.next())
				{
					ProvinceModel model = new ProvinceModel();
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<CityModel> loadCityList()
	{
		String sql = "select * from daily_config.tbl_city";
		return (List<CityModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CityModel> list = new ArrayList<CityModel>();
				while(rs.next())
				{
					CityModel  model = new CityModel();
					model.setId(rs.getInt("id"));
					model.setProvinceId(rs.getInt("province_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					list.add(model);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Integer> loadPhoneLocateMap()
	{
		String sql = "SELECT * FROM daily_config.`tbl_phone_locate`";
		
		return (Map<String,Integer>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<String,Integer> map = new HashMap<String, Integer>();
				
				while(rs.next())
				{
					map.put(rs.getString("phone"), rs.getInt("city_id"));
				}
				
				return map;
			}
		});
	}
	
}
