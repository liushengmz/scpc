package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SysCodeModel;
import com.system.util.StringUtil;

public class SysConfigDao
{
	@SuppressWarnings("unchecked")
	public List<SysCodeModel> loadSysCodeList()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sys_code";
		return (List<SysCodeModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SysCodeModel> list = new ArrayList<SysCodeModel>();
				
				while(rs.next())
				{
					SysCodeModel model = new SysCodeModel();
					model.setId(rs.getInt("id"));
					model.setFlag(rs.getInt("flag"));
					model.setCodeName(StringUtil.getString(rs.getString("code_name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
