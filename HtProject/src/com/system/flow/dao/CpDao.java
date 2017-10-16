package com.system.flow.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.flow.model.CpModel;
import com.system.util.StringUtil;

public class CpDao
{
	@SuppressWarnings("unchecked")
	public List<CpModel> loadCp(int status)
	{
		String sql = "select * from " + Constant.DB_DAILY_CONFIG + ".tbl_f_cp where 1=1 " ;
		if(status>=0)
		{
			sql += " and status = " + status;
		}
		return (List<CpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpModel> list = new ArrayList<CpModel>();
				
				while(rs.next())
				{
					CpModel model = new CpModel();
				
					model.setId(rs.getInt("id"));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public CpModel getCpById(int cpId)
	{
		String sql =  "select * from " + Constant.DB_DAILY_CONFIG + ".tbl_f_cp where id = " + cpId ;
		
		return (CpModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				
				if(rs.next())
				{
					CpModel model = new CpModel();
				
					model.setId(rs.getInt("id"));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					
					return model;
				}
				
				return null;
			}
		});
		
	}
}
