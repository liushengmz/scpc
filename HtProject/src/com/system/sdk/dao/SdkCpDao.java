package com.system.sdk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.sdk.model.SdkCpModel;
import com.system.util.StringUtil;

public class SdkCpDao {
	@SuppressWarnings("unchecked")
	public List<SdkCpModel>loadSdkCp(){

		String sql="SELECT * FROM daily_config.tbl_sdk_cp order by convert(name using gbk) asc";
		return (List<SdkCpModel>)new JdbcGameControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SdkCpModel> list = new ArrayList<SdkCpModel>();
				while(rs.next())
				{
					SdkCpModel model = new SdkCpModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"), ""));
					model.setCompanyName(StringUtil.getString(rs.getString("company_name"), ""));
					model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
					list.add(model);
				}
				return list;
			}
		});
	}

}
