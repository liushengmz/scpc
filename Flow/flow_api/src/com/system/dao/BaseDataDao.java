package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.BasePriceModel;
import com.system.model.CityModel;
import com.system.model.PhoneLocateModel;
import com.system.model.ProvinceModel;
import com.system.util.StringUtil;

public class BaseDataDao
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
	public Map<String,PhoneLocateModel> loadPhoneLocateMap()
	{
		String sql = "SELECT a.phone,c.id province_id,b.id city_id,a.operator ";
		sql += " FROM daily_config.`tbl_phone_locate` a";
		sql += " LEFT JOIN daily_config.tbl_city b ON a.city_id = b.id";
		sql += " LEFT JOIN daily_config.tbl_province c ON b.province_id = c.id";
		
		return (Map<String,PhoneLocateModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<String,PhoneLocateModel> map = new HashMap<String, PhoneLocateModel>();
				
				while(rs.next())
				{
					PhoneLocateModel model = new PhoneLocateModel();
					
					model.setProvinceId(rs.getInt("province_id"));
					model.setOperator(rs.getInt("operator"));
					model.setCityId(rs.getInt("city_id"));
					model.setPhonePre(StringUtil.getString(rs.getString("phone"), ""));
					
					map.put(model.getPhonePre(), model);
				}
				
				return map;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<BasePriceModel> loadBasePriceData()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_basic_price;";
		return (List<BasePriceModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<BasePriceModel> list = new ArrayList<BasePriceModel>();
				while(rs.next())
				{
					BasePriceModel model = new BasePriceModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setNum(rs.getInt("num"));
					model.setOperator(rs.getInt("operator"));
					model.setPrice(rs.getInt("price"));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	
}
