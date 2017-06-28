package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.system.database.JdbcLvControl;
import com.system.database.QueryCallBack;
import com.system.model.LvChannelModel;
import com.system.util.StringUtil;

public class LvChannelDao
{
	/*
	 * 根据用户ID取得对应的渠道信息
	 */ 
	public LvChannelModel loadLvChannelModel(int userId,int symBiosIs)
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_lv_channel where user_id = " + userId + " and symbiosis = " + symBiosIs;
		return (LvChannelModel)new JdbcLvControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				LvChannelModel model = new LvChannelModel();
				
				if(rs.next())
				{
					model.setId(rs.getInt("id"));
					model.setAppKey(StringUtil.getString(rs.getString("appkey"), ""));
					model.setChannel(StringUtil.getString(rs.getString("channel"), ""));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setFenCheng(rs.getInt("fencheng"));
					model.setPrice(rs.getInt("price"));
					model.setUserHold(rs.getInt("user_hold"));
					return model;
				}
				
				return null;
			}
		});
	}
}
