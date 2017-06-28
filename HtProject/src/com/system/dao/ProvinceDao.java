package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.ProvinceModel;
import com.system.util.StringUtil;

public class ProvinceDao
{
	@SuppressWarnings("unchecked")
	public List<ProvinceModel> loadProvinceList()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_province order by name asc";
		return (List<ProvinceModel>) new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ProvinceModel> list = new ArrayList<ProvinceModel>();
				
				while(rs.next())
				{
					ProvinceModel model = new ProvinceModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
