package com.system.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 该类用于封装操作数据库的方法
 * @author Administrator
 *
 */
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.ChannelModel;
import com.system.util.StringUtil;


public class ChannelDao {
	
	public Map<String, Object> loadChannel(int pageindex)
	{
		String sqlcount = " count(*) ";
		String sql = "select "+Constant.CONSTANT_REPLACE_STRING+"from "
				+ "" + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` b ON a.`appid` = b.id"
				+ " ORDER BY a.channel,b.appname ASC ";
		
		
		String limit = " limit "+Constant.PAGE_SIZE*(pageindex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		int count = (Integer)control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, sqlcount),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				});
		
		result.put("rows", count);
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<ChannelModel> list = new ArrayList<ChannelModel>();
						ChannelModel model = null;
						while (rs.next()) {
							model = new ChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppid(rs.getInt("appid"));
							model.setChannel(rs.getString("channel"));
							model.setHold_percent(rs.getString("hold_percent"));
							model.setSyn_type(rs.getShort("syn_type"));
							model.setUserid(rs.getInt("userid"));
							model.setRemark(rs.getString("remark"));
							model.setAppname(rs.getString("appname"));
							model.setAppkey(rs.getString("appkey"));
							list.add(model);
						}
						
						
						return list;
					}
				}));
		return result;
		
	}
	
	public Map<String, Object> loadChannel(int pageIndex,int appid,String appkey,String channel,int type)
	{
		String sqlcount = " count(*) ";
		String sql = "select "+Constant.CONSTANT_REPLACE_STRING+"from "
				+ "" + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` b ON a.`appid` = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user c on a.userid = c.id "
				+ " WHERE 1=1 ";
		
		if(appid>0)
		{
			sql += " AND b.id ="+appid;
		}
		
		if(!StringUtil.isNullOrEmpty(appkey))
		{
			sql += " AND b.appkey LIKE '%"+appkey+"%' ";
		}
		
		if(!StringUtil.isNullOrEmpty(channel))
		{
			sql += " AND a.channel LIKE '%"+channel+"%' ";
		}
		
		if(type>0)
		{
			sql += " AND a.syn_type="+type+" ";
		}
		
		sql += " ORDER BY a.channel,b.appname ASC ";
		
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		int count = (Integer)control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, sqlcount),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				});
		
		result.put("rows", count);
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<ChannelModel> list = new ArrayList<ChannelModel>();
						ChannelModel model = null;
						while (rs.next()) {
							model = new ChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppid(rs.getInt("appid"));
							model.setChannel(rs.getString("channel"));
							model.setSettleType(rs.getInt("settle_type"));
							model.setHold_percent(rs.getString("hold_percent"));
							model.setSyn_type(rs.getShort("syn_type"));
							model.setUserid(rs.getInt("userid"));
							model.setRemark(rs.getString("remark"));
							model.setAppname(rs.getString("appname"));
							model.setAppkey(rs.getString("appkey"));
							model.setUserName(StringUtil.getString(rs.getString("nick_name"),""));
							list.add(model);
						}
						
						
						return list;
					}
				}));
		return result;
		
	}
	
	public ChannelModel loadQdById(int id)
	{
		String sql = "SELECT * FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` b ON a.`appid` = b.id WHERE a.`id`="+id;
		
		return (ChannelModel)new JdbcControl().query(sql, 
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
						{
							ChannelModel model = new ChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppname(rs.getString("appname"));
							model.setAppkey(rs.getString("appkey"));
							model.setChannel(rs.getString("channel"));
							model.setSettleType(rs.getInt("settle_type"));
							model.setHold_percent(rs.getString("hold_percent"));
							model.setRemark(rs.getString("remark"));
							model.setSyn_type(rs.getShort("syn_type"));
							model.setUserid(rs.getInt("userid"));
							return model;
						}
						return null;
					}
				});
	}
	
	public boolean updataChannel(ChannelModel model)
	{
		int id = new AppDao().loadIdByName(model.getAppname());
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel SET "
				+ "appid="+id+","
				+ "channel='"+model.getChannel()+"',"
				+ "settle_type="+model.getSettleType()+","
				+ "hold_percent='"+model.getHold_percent()+"',"
				+ "syn_type="+model.getSyn_type()+","
			    + "remark='"+model.getRemark()+"' WHERE id="+model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean addChannel(ChannelModel model)
	{
		int id = new AppDao().loadIdByName(model.getAppname());
		
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel(appid,channel,hold_percent,syn_type,settle_type,remark) "
				+ "value("+id+",'"+model.getChannel()+"',"+model.getHold_percent()+","
				+ model.getSyn_type()+","+model.getSettleType()+",'"+model.getRemark()+"')";
		return new JdbcControl().execute(sql);
	}
	
	public boolean deletChannel(int id)
	{
		String sql = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel WHERE id="+id;
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateChannelAccount(int channelId,int userId)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel set userid = " + userId + "  where id = " + channelId;
		return new JdbcControl().execute(sql);
	}
	
}
