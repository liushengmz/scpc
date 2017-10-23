package com.system.sdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.smodel.ConfigPropertiesModel;
import com.system.util.StringUtil;

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
				List<ConfigPropertiesModel> list = new ArrayList<ConfigPropertiesModel>();
				while(rs.next())
				{
					ConfigPropertiesModel model = new ConfigPropertiesModel();
					
					model.setId(rs.getInt("id"));
					model.setType(rs.getInt("type"));
					model.setKey(StringUtil.getString(rs.getString("key"), ""));
					model.setValue(StringUtil.getString(rs.getString("value"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
}
