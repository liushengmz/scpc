package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CpModel;
import com.system.util.StringUtil;

public class CpDao
{
	@SuppressWarnings("unchecked")
	public List<CpModel> loadCpData(int coId)
	{
		String sql = "select * from tbl_cp where co_id = " + coId + " order by CONVERT(short_name USING gbk)";
		return (List<CpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpModel> list = new ArrayList<CpModel>();
				
				while(rs.next())
				{
					CpModel model = new CpModel();
					
					model.setCpId(rs.getInt("cp_id"));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
}	
