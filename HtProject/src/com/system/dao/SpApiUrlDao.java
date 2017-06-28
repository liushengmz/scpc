package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SpApiUrlModel;
import com.system.util.StringUtil;

public class SpApiUrlDao
{
	@SuppressWarnings("unchecked")
	public List<SpApiUrlModel> loadSpApiUrl()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_api_url";
		return (List<SpApiUrlModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpApiUrlModel> list = new ArrayList<SpApiUrlModel>();
				while(rs.next())
				{
					SpApiUrlModel model = new SpApiUrlModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
}
