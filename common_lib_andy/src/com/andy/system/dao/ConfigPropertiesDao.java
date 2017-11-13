package com.andy.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.andy.system.constant.SysConstant;
import com.andy.system.db.AnoDbDataToModel;
import com.andy.system.db.JdbcControl;
import com.andy.system.db.QueryCallBack;
import com.andy.system.model.ConfigPropertiesModel;

public class ConfigPropertiesDao
{
	@SuppressWarnings("unchecked")
	public List<ConfigPropertiesModel> loadConfigProperties()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_s_config_properties";
		return (List<ConfigPropertiesModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				return new AnoDbDataToModel().fillChildDbModelList(ConfigPropertiesModel.class, rs);
			}
		});
	}
}
