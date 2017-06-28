package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CityModel;
import com.system.util.StringUtil;

public class CityDao
{
	@SuppressWarnings("unchecked")
	public List<CityModel> loadCityList()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_city order by province_id asc";
		
		return (List<CityModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CityModel> list = new ArrayList<CityModel>();
				
				while(rs.next())
				{
					CityModel model = new CityModel();
					
					model.setId(rs.getInt("id"));
					model.setProvinceId(rs.getInt("province_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
