package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcAdControl;
import com.system.database.QueryCallBack;
import com.system.model.CpChannelModel;
import com.system.util.StringUtil;

public class CpChannelDao {
	public Map<String, Object> loadCpChannel(int pageIndex)
	{
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM ad_log.`tbl_channel_summer` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` b ON a.`channelid`=b.`id` "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` c ON b.`appid`=c.`id` "
				+ "WHERE 1=1 ";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcAdControl control = new JdbcAdControl();
		
		result.put("rows", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				}));
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.`name` channel,b.`id` channelid,c.`appname`,c.`appkey` ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<CpChannelModel> list = new ArrayList<CpChannelModel>();
						CpChannelModel model = null;
						while (rs.next()) {
							model = new CpChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setChannel(rs.getString("channel"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("new_user_rows"));
							model.setActiveRows(rs.getInt("active_rows"));
							model.setAmount(rs.getDouble("amount"));
							model.setShowNewUserRows(rs.getInt("show_new_user_rows"));
							model.setShowActiveRows(rs.getInt("show_active_rows"));
							model.setShowAmount(rs.getDouble("show_amount"));
							model.setScale(rs.getDouble("scale"));
							model.setStatus(rs.getInt("status"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Map<String, Object> loadCpChannel(int pageIndex,String startdate,String enddate,int appid,int channelid)
	{
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM ad_log.`tbl_channel_summer` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` b ON a.`channelid`=b.`id` "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` c ON b.`appid`=c.`id` "
				+ "WHERE 1=1 ";
		
		if(!StringUtil.isNullOrEmpty(startdate))
		{
			sql += " AND fee_date >= '" + startdate + "'";
		}
		
		if(!StringUtil.isNullOrEmpty(enddate))
		{
			sql += " AND fee_date <= '" + enddate + "' ";
		}
		
		if(appid>0)
		{
			sql += " AND c.`id`="+appid+" ";
		}
		
		if(channelid>0)
		{
			sql += " AND b.`id`="+channelid+" ";
		}
		
		sql += " order by a.fee_date asc ";
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcAdControl control = new JdbcAdControl();
		
		result.put("rows", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				}));
		
		control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(new_user_rows),sum(active_rows),sum(amount),sum(show_new_user_rows),sum(show_active_rows),sum(show_amount) "), 
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
						{
							result.put("newUserRows", rs.getInt(1));
							result.put("activeRows", rs.getInt(2));
							result.put("amount", rs.getDouble(3));
							result.put("sumUserRows", rs.getInt(4));
							result.put("sumActive", rs.getInt(5));
							result.put("sumAmount", rs.getDouble(6));
						}
						return null;
					}
				});
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.`name` channel,b.`id` channelid,c.`appname`,c.`appkey` ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<CpChannelModel> list = new ArrayList<CpChannelModel>();
						CpChannelModel model = null;
						while (rs.next()) {
							model = new CpChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setChannel(rs.getString("channel"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("new_user_rows"));
							model.setActiveRows(rs.getInt("active_rows"));
							model.setAmount(rs.getDouble("amount"));
							model.setShowNewUserRows(rs.getInt("show_new_user_rows"));
							model.setShowActiveRows(rs.getInt("show_active_rows"));
							model.setShowAmount(rs.getDouble("show_amount"));
							model.setScale(rs.getDouble("scale"));
							model.setStatus(rs.getInt("status"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public boolean addCpChannel(CpChannelModel model)
	{
		String sql = "INSERT INTO ad_log.`tbl_channel_summer`(fee_date,channelid,"
				+ "new_user_rows,active_rows,amount,show_new_user_rows,show_active_rows,"
				+ "show_amount,scale,STATUS) "
				+ "VALUE('"+model.getFeeDate()+"',"
				+model.getChannelid()+","
				+model.getNewUserRows()+","
				+model.getActiveRows()+","
				+model.getAmount()+","
				+model.getShowNewUserRows()+","
				+model.getShowActiveRows()+","
				+model.getShowAmount()+","
				+model.getScale()+","
				+model.getStatus()+")";
		
		return new JdbcAdControl().execute(sql);
	}
	
	public boolean deletCpChannel(int id)
	{
		String sql = "DELETE FROM ad_log.`tbl_channel_summer` WHERE id="+id;
		
		return new JdbcAdControl().execute(sql);
	}
	
	public CpChannelModel loadCpChannelById(int id)
	{
		String sql = "SELECT a.*,b.`name` channel,b.`id` channelid,c.`appname`,c.`appkey`,b.appid "
				+ " FROM ad_log.`tbl_channel_summer` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` b ON a.`channelid`=b.`id` "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` c ON b.`appid`=c.`id` "
				+ "where a.id = "+id;
		
		
		return (CpChannelModel)new JdbcAdControl().query(sql,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						
						if(rs.next())
						{
							CpChannelModel model = new CpChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppid(rs.getInt("appid"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setChannel(rs.getString("channel"));
							model.setChannelid(rs.getInt("channelid"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("new_user_rows"));
							model.setActiveRows(rs.getInt("active_rows"));
							model.setAmount(rs.getDouble("amount"));
							model.setShowNewUserRows(rs.getInt("show_new_user_rows"));
							model.setShowActiveRows(rs.getInt("show_active_rows"));
							model.setShowAmount(rs.getDouble("show_amount"));
							model.setScale(rs.getDouble("scale"));
							model.setStatus(rs.getInt("status"));
							return model;
						}
						
						return null;
					}
				});
	}
	
	public boolean updateCpChannel(CpChannelModel model)
	{
		String sql = "UPDATE ad_log.`tbl_channel_summer` SET "
				+ "id="+model.getId()+","
				+ " fee_date='"+model.getFeeDate()+"',"
			    + " new_user_rows="+model.getNewUserRows()+","
			    + " active_rows="+model.getActiveRows()+","
			    + " amount="+model.getAmount()+","
			    + " show_new_user_rows="+model.getShowNewUserRows()+","
			    + " show_active_rows="+model.getShowActiveRows()+","
			    + " show_amount="+model.getShowAmount()+","
			    + " scale="+model.getScale()+","
			    + " status="+model.getStatus()+" WHERE id="+model.getId();
		return new JdbcAdControl().execute(sql);
	}
	
	public Map<String, Object> loadQdShow(int pageIndex,int userid,String startDate,String endDate,String appname,String channel)
	{
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING+" FROM ad_log.`tbl_channel_summer` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_channel` b ON a.`channelid`=b.`id` "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` c ON b.`appid` = c.id "
				+ "WHERE b.user_id="+userid;
		
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(startDate))
		{
			sql += " AND fee_date >= '" + startDate + "'";
		}
		
		if(!StringUtil.isNullOrEmpty(endDate))
		{
			sql += " AND fee_date <= '" + endDate + "' ";
		}
		
		if(!StringUtil.isNullOrEmpty(appname))
		{
			sql += " AND c.appname LIKE '%"+appname+"%' ";
		}
		
		if(!StringUtil.isNullOrEmpty(channel))
		{
			sql += " AND b.name LIKE '%"+channel+"%' ";
		}
		
		sql +=  " order by a.fee_date asc ";
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcAdControl control = new JdbcAdControl();
		
		result.put("rows", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				}));
		
		control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(show_new_user_rows),sum(show_active_rows),sum(show_amount) "), 
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
						{
							result.put("sumUserRows", rs.getInt(1));
							result.put("sumActive", rs.getInt(2));
							result.put("sumAmount", rs.getDouble(3));
						}
						return null;
					}
				});
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,c.appname,b.name channelname ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<CpChannelModel> list = new ArrayList<CpChannelModel>();
						CpChannelModel model = null;
						while (rs.next()) {
							model = new CpChannelModel();
							model.setId(rs.getInt("id"));
							model.setAppname(rs.getString("appname"));
							model.setChannel(rs.getString("channelname"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("new_user_rows"));
							model.setActiveRows(rs.getInt("active_rows"));
							model.setAmount(rs.getDouble("amount"));
							model.setShowNewUserRows(rs.getInt("show_new_user_rows"));
							model.setShowActiveRows(rs.getInt("show_active_rows"));
							model.setShowAmount(rs.getDouble("show_amount"));
							model.setScale(rs.getDouble("scale"));
							model.setStatus(rs.getInt("status"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
}
