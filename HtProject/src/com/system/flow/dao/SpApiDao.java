package com.system.flow.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.flow.model.SpApiModel;
import com.system.util.StringUtil;

public class SpApiDao
{
	@SuppressWarnings("unchecked")
	public List<SpApiModel> loadSpApiBySpId(int spId)
	{
		String sql = "select * from " + Constant.DB_DAILY_CONFIG + ".tbl_f_sp_api where sp_id = " + spId;
		return (List<SpApiModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpApiModel> list = new ArrayList<SpApiModel>();
				
				while(rs.next())
				{
					SpApiModel model = new SpApiModel();
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setSpId(rs.getInt("sp_id"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
