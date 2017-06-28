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
import com.system.model.PhoneLocateModel;
import com.system.model.ProvinceModel;
import com.system.util.StringUtil;

public class LocateDao
{
	@SuppressWarnings("unchecked")
	public List<ProvinceModel> loadProvinceList()
	{
		String sql = "SELECT * FROM " + com.sytem.dd.DddFord.dataBase + ".tbl_province";
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
		String sql = "select * from " + com.sytem.dd.DddFord.dataBase + ".tbl_city";
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
		String sql = "SELECT * FROM " + com.sytem.dd.DddFord.dataBase + ".`tbl_phone_locate`";
		
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
	
	@SuppressWarnings("unchecked")
	public List<PhoneLocateModel> loadPhoneLaceteList()
	{
		String sql = " SELECT a.id,a.phone,b.id city_id,c.id province_id ";
		sql += " FROM " + com.sytem.dd.DddFord.dataBase + ".tbl_phone_locate a ";
		sql += " LEFT JOIN " + com.sytem.dd.DddFord.dataBase + ".tbl_city b ON a.city_id = b.id";
		sql += " LEFT JOIN " + com.sytem.dd.DddFord.dataBase + ".tbl_province c ON b.province_id = c.id";
		
		return (List<PhoneLocateModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<PhoneLocateModel> list = new ArrayList<PhoneLocateModel>();
				
				while(rs.next())
				{
					PhoneLocateModel model = new PhoneLocateModel();
					model.setId(rs.getInt("id"));
					model.setCityId(rs.getInt("city_id"));
					model.setProvinceId(rs.getInt("province_id"));
					model.setPhonePrefix(StringUtil.getString(rs.getString("phone"), "1380000"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer,List<PhoneLocateModel>> loadPhoneOperatorLaceteMap()
	{
		String sql = " SELECT a.id,a.phone,b.id city_id,c.id province_id,a.operator ";
		sql += " FROM " + com.sytem.dd.DddFord.dataBase + ".tbl_phone_locate a ";
		sql += " LEFT JOIN " + com.sytem.dd.DddFord.dataBase + ".tbl_city b ON a.city_id = b.id";
		sql += " LEFT JOIN " + com.sytem.dd.DddFord.dataBase + ".tbl_province c ON b.province_id = c.id";
		
		return (Map<Integer,List<PhoneLocateModel>>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<PhoneLocateModel> list = null;
				
				Map<Integer,List<PhoneLocateModel>> map = new HashMap<Integer,List<PhoneLocateModel>>();
				
				while(rs.next())
				{
					PhoneLocateModel model = new PhoneLocateModel();
					model.setId(rs.getInt("id"));
					model.setCityId(rs.getInt("city_id"));
					model.setProvinceId(rs.getInt("province_id"));
					model.setPhonePrefix(StringUtil.getString(rs.getString("phone"), "1380000"));
					model.setOperatorId(rs.getInt("operator"));
					
					list = map.get(model.getOperatorId());
					
					if(list==null)
					{
						list = new ArrayList<PhoneLocateModel>();
						map.put(model.getOperatorId(), list);
					}
					
					list.add(model);
				}
				
				return map;
			}
		});
	}
	
}
