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
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.AdChannelModel;
import com.system.model.ChannelModel;
import com.system.util.StringUtil;


public class AdChannelDao {
	
	public Map<String, Object> loadChannel(int pageindex)
	{
		String sqlcount = " count(*) ";
		String sql = "select "+Constant.CONSTANT_REPLACE_STRING+"from "
				+ "" + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid` = b.id"
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
						List<AdChannelModel> list = new ArrayList<AdChannelModel>();
						AdChannelModel model = null;
						while (rs.next()) {
							model = new AdChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppid(rs.getInt("appid"));
							model.setChannel(rs.getString("channel"));
							model.setHold_percent(rs.getString("hold_percent"));
							model.setUserid(rs.getInt("userid"));
							model.setName(rs.getString("name"));
							model.setAppname(rs.getString("appname"));
							model.setAppkey(rs.getString("appkey"));
							model.setScale(rs.getDouble("scale"));
							list.add(model);
						}
						
						
						return list;
					}
				}));
		return result;
		
	}
	
	public Map<String, Object> loadChannel()
	{
		String sqlcount = " count(*) ";
		String sql = "select "+Constant.CONSTANT_REPLACE_STRING+" from "
				+ "" + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid` = b.id ";
		
		
		//String limit = " limit "+Constant.PAGE_SIZE*(pageindex-1) + "," + Constant.PAGE_SIZE;
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.name,a.channel,a.id,a.appid "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AdChannelModel> list = new ArrayList<AdChannelModel>();
						AdChannelModel model = null;
						while (rs.next()) {
							model = new AdChannelModel();
							model.setAppid(rs.getInt("appid"));
							model.setId(rs.getInt("id"));
							model.setChannel(rs.getString("channel"));
							model.setName(rs.getString("name"));
							list.add(model);
						}
						
						
						return list;
					}
				}));
		return result;
		
	}
	
	public AdChannelModel loadAdQdName()
	{
		String sql = "SELECT a.name,a.channel FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid` = b.id";
		
		AdChannelModel model = null;
		
		new JdbcControl().query(sql,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						
						return null;
					}
				});
		
		return model;
	}
	
	public Map<String, Object> loadChannel(int pageIndex,int appid,String appkey,String channel ,String channelname)
	{
		String sqlcount = " count(*) ";
		String sql = "select "+Constant.CONSTANT_REPLACE_STRING+"from "
				+ "" + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid` = b.id "
				+ " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` c ON a.`user_id` = c.id "
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
		
		if(!StringUtil.isNullOrEmpty(channelname))
		{
			sql +=" AND a.name LIKE '%"+channelname+"%' ";
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
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,c.nick_name,b.* ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AdChannelModel> list = new ArrayList<AdChannelModel>();
						AdChannelModel model = null;
						while (rs.next()) {
							model = new AdChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppid(rs.getInt("appid"));
							model.setChannel(rs.getString("channel"));
							model.setHold_percent(rs.getString("hold_percent"));
							model.setName(rs.getString("name"));
							model.setAppname(rs.getString("appname"));
							model.setAppkey(rs.getString("appkey"));
							model.setScale(rs.getDouble("scale"));
							model.setCreateName(rs.getString("nick_name"));
							list.add(model);
						}
						
						
						return list;
					}
				}));
		return result;
		
	}
	
	public AdChannelModel loadQdById(int id)
	{
		String sql = "SELECT * FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid` = b.id WHERE a.`id`="+id;
		
		return (AdChannelModel)new JdbcControl().query(sql, 
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
						{
							AdChannelModel model = new AdChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppname(rs.getString("appname"));
							model.setAppkey(rs.getString("appkey"));
							model.setChannel(rs.getString("channel"));
							model.setName(rs.getString("name"));
							model.setHold_percent(rs.getString("hold_percent"));
							model.setScale(rs.getDouble("scale"));
							return model;
						}
						return null;
					}
				});
	}
	
	public boolean updataChannel(AdChannelModel model)
	{
		int id = new AdAppDao().loadIdByName(model.getAppname());
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` SET "
				+ "appid="+id+","
				+ "channel='"+model.getChannel()+"',"
				+ "hold_percent='"+model.getHold_percent()+"',"
			    + "name='"+model.getName()+"',"
			    + "scale="+model.getScale()+" WHERE id="+model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean addChannel(AdChannelModel model)
	{
		int id = new AdAppDao().loadIdByName(model.getAppname());
		
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel`(appid,channel,hold_percent,name,scale,user_id) "
				+ "value("+id+",'"+model.getChannel()+"',"+model.getHold_percent()+",'"
				+model.getName()+"',"+model.getScale()+","+model.getUserid()+")";
		return new JdbcControl().execute(sql);
	}
	
	public boolean deletChannel(int id)
	{
		String sql = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` WHERE id="+id;
		return new JdbcControl().execute(sql);
	}
	
}
