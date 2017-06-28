package com.system.sdk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.sdk.model.SdkAppModel;
import com.system.util.StringUtil;

public class SdkAppDao {
	@SuppressWarnings("unchecked")
	public List<SdkAppModel> loadSdkApp(){
		String sql="SELECT * FROM daily_config.tbl_sdk_app order by convert(name using gbk) asc";
		return (List<SdkAppModel>)new JdbcGameControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SdkAppModel> list = new ArrayList<SdkAppModel>();
				while(rs.next())
				{
					SdkAppModel model = new SdkAppModel();
					
					model.setId(rs.getInt("id"));
					model.setSdkAppId(rs.getInt("sdk_app_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setAppKey(StringUtil.getString(rs.getString("appkey"),""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCreateTime(StringUtil.getString(rs.getString("create_time"), ""));
					list.add(model);
				}
				return list;
			}
		});

	}

}
