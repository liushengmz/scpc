package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SysConfigModel;
import com.system.util.StringUtil;

public class SysConfigDao
{
	@SuppressWarnings("unchecked")
	public List<SysConfigModel> loadSysConfigList()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sys_dic order by type asc";
		return (List<SysConfigModel>) new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SysConfigModel> list = new ArrayList<SysConfigModel>();
				
				while(rs.next())
				{
					SysConfigModel model = new SysConfigModel();
					
					model.setId(rs.getInt("id"));
					model.setType(rs.getInt("type"));
					model.setFlag(StringUtil.getString(rs.getString("flag"), ""));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setName2(StringUtil.getString(rs.getString("name2"),""));
					model.setName3(StringUtil.getString(rs.getString("name3"),""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
