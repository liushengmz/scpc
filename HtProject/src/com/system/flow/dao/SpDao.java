package com.system.flow.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.flow.model.SpModel;
import com.system.util.StringUtil;

public class SpDao
{
	@SuppressWarnings("unchecked")
	public List<SpModel> loadSp(int status)
	{
		String sql = "SELECT * FROM " + Constant.DB_DAILY_CONFIG + ".tbl_f_sp WHERE 1=1";
		
		if(status>=0)
			sql += " and status = " + status;
		
		return (List<SpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setId(rs.getInt("id"));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setStatus(rs.getInt("status"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
