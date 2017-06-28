package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SpModel;
import com.system.model.UpDataTypeModel;
import com.system.util.StringUtil;

public class UpDataTypeDao {
	@SuppressWarnings("unchecked")
	public List<UpDataTypeModel> loadUpDataType(){
		String sql = "SELECT * FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_up_data_type order by id asc";
		return (List<UpDataTypeModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UpDataTypeModel> list = new ArrayList<UpDataTypeModel>();
				
				while(rs.next())
				{
					UpDataTypeModel model = new UpDataTypeModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}

}
