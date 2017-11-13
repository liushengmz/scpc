package com.andy.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.andy.system.constant.SysConstant;
import com.andy.system.db.AnoDbDataToModel;
import com.andy.system.db.JdbcControl;
import com.andy.system.db.QueryCallBack;
import com.andy.system.model.SysCodeModel;

public class SysConfigDao
{
	@SuppressWarnings("unchecked")
	public List<SysCodeModel> loadSysCodeList()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_sys_code";
		return (List<SysCodeModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				return new AnoDbDataToModel().fillChildDbModelList(SysCodeModel.class, rs);
			}
		});
	}
}
