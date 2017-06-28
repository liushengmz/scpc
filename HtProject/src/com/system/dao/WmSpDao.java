package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.WmSpModel;
import com.system.util.StringUtil;

public class WmSpDao
{
	@SuppressWarnings("unchecked")
	public List<WmSpModel> loadSp()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_wm_sp where status = 1 order by convert(full_name using gbk) asc";
		return (List<WmSpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<WmSpModel> list = new ArrayList<WmSpModel>();
				
				while(rs.next())
				{
					WmSpModel model = new WmSpModel();
					model.setId(rs.getInt("id"));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
