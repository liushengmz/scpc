package com.system.sdk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.sdk.model.SdkTroneModel;
import com.system.util.StringUtil;

public class SdkTroneDao {
	@SuppressWarnings("unchecked")
	public List<SdkTroneModel>loadSdkTrone(){
		String sql="SELECT * FROM daily_config.tbl_sdk_trone order by convert(name using gbk) asc";
		return (List<SdkTroneModel>)new JdbcGameControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SdkTroneModel> list = new ArrayList<SdkTroneModel>();
				while(rs.next())
				{
					SdkTroneModel model = new SdkTroneModel();
					model.setId(rs.getInt("id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
				
					list.add(model);
				}
				return list;
			}
		});
	}

}
