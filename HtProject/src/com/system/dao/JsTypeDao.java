package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.JsTypeModel;
import com.system.util.StringUtil;

public class JsTypeDao
{
	@SuppressWarnings("unchecked")
	public List<JsTypeModel> loadJsType()
	{
		String sql = "SELECT * FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_js_type ORDER BY sort_id ASC";
		
		return (List<JsTypeModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<JsTypeModel> list = new ArrayList<JsTypeModel>();
				
				while(rs.next())
				{
					JsTypeModel model = new JsTypeModel();
					model.setId(rs.getInt("id"));
					model.setJsType(rs.getInt("type_id"));
					model.setJsName(StringUtil.getString(rs.getString("name"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
