package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.AdAppModel;
import com.system.util.StringUtil;

public class AdAppDao {
	public Map<String, Object> loadAppByPageindex(int pageIndex)
	{
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` ORDER BY id ASC ";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		result.put("rows", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				}));
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AdAppModel> list = new ArrayList<AdAppModel>();
						AdAppModel model = null;
						while (rs.next()) {
							model = new AdAppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setHold_percent(rs.getInt("hold_percent"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Map<String, Object> loadAppByPageindex2()
	{
		//String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` ORDER BY id ASC ";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		result.put("rows", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				}));
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AdAppModel> list = new ArrayList<AdAppModel>();
						AdAppModel model = null;
						while (rs.next()) {
							model = new AdAppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setHold_percent(rs.getInt("hold_percent"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Map<String, Object> loadApp()
	{
		//String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` ORDER BY id ASC ";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		result.put("rows", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				}));
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AdAppModel> list = new ArrayList<AdAppModel>();
						AdAppModel model = null;
						while (rs.next()) {
							model = new AdAppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setHold_percent(rs.getInt("hold_percent"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Map<String, Object> loadAppByPageindex(int pageIndex,String appname,String appkey)
	{
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` a "
				+ " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` b ON a.`user_id` = b.id "
				+ " WHERE 1=1 ";
		
		if(!StringUtil.isNullOrEmpty(appname))
		{
			sql += " AND appname LIKE '%"+appname+"%' ";
		}
		
		if(!StringUtil.isNullOrEmpty(appkey))
		{
			sql += " AND appkey LIKE '%"+appkey+"%' ";
		}
		
		sql +=" ORDER BY a.id ASC ";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		result.put("rows", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				}));
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.nick_name ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AdAppModel> list = new ArrayList<AdAppModel>();
						AdAppModel model = null;
						while (rs.next()) {
							model = new AdAppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setHold_percent(rs.getInt("hold_percent"));
							model.setCreateName(StringUtil.getString(rs.getString("nick_name"),""));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Integer loadIdByName(String appname)
	{
		String sql = "SELECT id FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` where appname='"+appname+"'";
		
		return (Integer)new JdbcControl().query(sql,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				});
	}
	
	public AdAppModel loadAppById(int id)
	{
		String sql = "SELECT * FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` where id = "+id;
		
		
		return (AdAppModel)new JdbcControl().query(sql,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						
						if(rs.next())
						{
							AdAppModel model = new AdAppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setHold_percent(rs.getInt("hold_percent"));
							model.setUser_id(rs.getInt("user_id"));
							return model;
						}
						
						return null;
					}
				});
	}
	
	public boolean updataApp(AdAppModel model)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` SET "
				+ "appkey='"+model.getAppkey()+"',"
				+ " appname='"+model.getAppname()+"',"
			    + " hold_percent="+model.getHold_percent()+" WHERE id="+model.getId();
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateAdAppAccount(int id,int userId)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_ad_app set user_id = " + userId + " where id = " + id;
		return new JdbcControl().execute(sql);
	}
	
	public boolean deletApp(int id)
	{
		String sql = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` WHERE id="+id;
		return new JdbcControl().execute(sql);
	}
	
	public boolean addApp(AdAppModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app`("
				+ "appkey,appname,hold_percent,user_id) value("
				+ "'"+model.getAppkey()+"','"+model.getAppname()+"',"+model.getHold_percent()+","+model.getUser_id()
				+ ")";
		
		return new JdbcControl().execute(sql);
	}
	
	public static void main(String[] args) {
		AdAppDao dao = new AdAppDao();
		
	}
	
}
