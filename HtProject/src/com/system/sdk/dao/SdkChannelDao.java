package com.system.sdk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.sdk.model.SdkChannelModel;
import com.system.util.StringUtil;

public class SdkChannelDao {
	@SuppressWarnings("unchecked")
	public List<SdkChannelModel>loadSdkChannel(){
	
	String sql="SELECT * FROM daily_config.tbl_sdk_channel order by convert(channel_name using gbk) asc";
	return (List<SdkChannelModel>)new JdbcGameControl().query(sql, new QueryCallBack()
	{
		@Override
		public Object onCallBack(ResultSet rs) throws SQLException
		{
			List<SdkChannelModel> list = new ArrayList<SdkChannelModel>();
			while(rs.next())
			{
				SdkChannelModel model = new SdkChannelModel();
				
				model.setId(rs.getInt("id"));
				model.setSdkChannelId(rs.getInt("sdk_channel_id"));
				model.setAppId(rs.getInt("app_id"));
				model.setChannelId(rs.getInt("channel_id"));
				model.setChannelName(StringUtil.getString(rs.getString("channel_name"), ""));
				model.setType(rs.getInt("type"));
				model.setCreateTime(StringUtil.getString(rs.getString("create_time"), ""));
				list.add(model);
			}
			return list;
		}
	});
	}
	
}
	

