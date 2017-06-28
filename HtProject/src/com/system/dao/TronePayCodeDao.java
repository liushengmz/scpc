package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.TronePayCodeModel;
import com.system.util.StringUtil;

public class TronePayCodeDao
{
	public boolean addTronePayCode(TronePayCodeModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_paycode(trone_id,paycode,appid,channelid) values(?,?,?,?)";
		
		Map<Integer,Object> map = new HashMap<Integer,Object>();
		
		map.put(1, model.getTroneId());
		map.put(2, model.getPayCode());
		map.put(3, model.getAppId());
		map.put(4, model.getChannelId());
		
		return new JdbcControl().execute(sql,map);
	}
	
	public boolean updateTronePayCode(TronePayCodeModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_paycode set paycode = ?,appid = ?,channelid = ? where trone_id = ?";
		
		Map<Integer,Object> map = new HashMap<Integer,Object>();
		
		map.put(1, model.getPayCode());
		map.put(2, model.getAppId());
		map.put(3, model.getChannelId());
		map.put(4, model.getTroneId());
		
		return new JdbcControl().execute(sql,map);
	}
	
	public TronePayCodeModel getTronePayCode(int tronePayCodeId)
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_paycode where trone_id = " + tronePayCodeId;
		return (TronePayCodeModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					TronePayCodeModel model = new TronePayCodeModel();
					model.setId(rs.getInt("id"));
					model.setChannelId(StringUtil.getString(rs.getString("channelid"), ""));
					model.setAppId(StringUtil.getString(rs.getString("appid"), ""));
					model.setPayCode(StringUtil.getString(rs.getString("paycode"), ""));
					return model;
				}
				return null;
			}
		});
	}
}
