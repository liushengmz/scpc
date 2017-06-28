package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SpModel;
import com.system.util.StringUtil;

public class SpDao
{
	@SuppressWarnings("unchecked")
	public List<SpModel> loadSpData(int coId)
	{
		String sql = "select * from tbl_sp where co_id = " + coId + " order by CONVERT(short_name USING gbk)";
		return (List<SpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setSpId(rs.getInt("sp_id"));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
}
