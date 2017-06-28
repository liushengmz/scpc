
package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.LvChannelModel;
import com.system.util.StringUtil;

public class LvChannelDao
{
	@SuppressWarnings("unchecked")
	public List<LvChannelModel> loadData()
	{
		JdbcControl db = new JdbcControl();
		String sql = "select * from tbl_lv_channel";
		QueryCallBack callBack = new QueryCallBack()
		{

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				final String defaultValue = "";
				List<LvChannelModel> lst = new ArrayList<LvChannelModel>();
				while (rs.next())
				{
					LvChannelModel m = new LvChannelModel();
					m.setAppkey(StringUtil.getString(rs.getString("appkey"),
							defaultValue));
					m.setChannel(StringUtil.getString(rs.getString("channel"),
							defaultValue));
					m.setCreateDate(rs.getDate("create_date"));
					m.setHoldPercent(rs.getInt("hold_percent"));
					m.setId(rs.getInt("id"));
					m.setUserId(rs.getInt("user_id"));
					m.setAliPay(rs.getInt("ali_pay"));
					m.setWxPay(rs.getInt("wx_pay"));
					lst.add(m);
				}
				return lst;
			}
		};
		return (List<LvChannelModel>) db.query(sql, callBack);
	}
}
