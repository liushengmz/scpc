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
import com.system.model.AppModel;
import com.system.util.StringUtil;

public class AppDao {
	public Map<String, Object> loadAppByPageindex(int pageIndex)
	{
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` ORDER BY id ASC ";
		
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
						List<AppModel> list = new ArrayList<AppModel>();
						AppModel model = null;
						while (rs.next()) {
							model = new AppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setHold_percent(rs.getInt("hold_percent"));
							model.setRemark(rs.getString("remark"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Map<String, Object> loadAppByPageindex()
	{
		//String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` ORDER BY id ASC ";
		
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
						List<AppModel> list = new ArrayList<AppModel>();
						AppModel model = null;
						while (rs.next()) {
							model = new AppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setHold_percent(rs.getInt("hold_percent"));
							model.setRemark(rs.getString("remark"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Map<String, Object> loadAppByPageindex(int pageIndex,String appname,String appkey,int appType)
	{
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.user_id = b.id WHERE 1=1 ";
		
		if(!StringUtil.isNullOrEmpty(appname))
		{
			sql += " AND appname LIKE '%"+appname+"%' ";
		}
		
		if(!StringUtil.isNullOrEmpty(appkey))
		{
			sql += " AND appkey LIKE '%"+appkey+"%' ";
		}
		
		if(appType>0)
		{
			sql += " And app_type = " + appType;
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
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AppModel> list = new ArrayList<AppModel>();
						AppModel model = null;
						while (rs.next()) {
							model = new AppModel();
							model.setId(rs.getInt("a.id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setHold_percent(rs.getInt("hold_percent"));
							model.setRemark(rs.getString("remark"));
							model.setUserName(StringUtil.getString(rs.getString("nick_name"), ""));
							model.setAppType(rs.getInt("app_type"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Integer loadIdByName(String appname)
	{
		String sql = "SELECT id FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` where appname='"+appname+"'";
		
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
	
	public AppModel loadAppById(int id)
	{
		String sql = "SELECT * FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` where id = "+id;
		
		
		return (AppModel)new JdbcControl().query(sql,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						
						if(rs.next())
						{
							AppModel model = new AppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setAppType(rs.getInt("app_type"));
							model.setHold_percent(rs.getInt("hold_percent"));
							model.setRemark(rs.getString("remark"));
							model.setUser_id(rs.getInt("user_id"));
							return model;
						}
						
						return null;
					}
				});
	}
	
	public boolean updataApp(AppModel model)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` SET "
				+ "appkey='"+model.getAppkey()+"',"
				+ " appname='"+model.getAppname()+"',"
			    + " hold_percent="+model.getHold_percent()+","
			    + " app_type="+model.getAppType()+","
			    + " remark='"+model.getRemark()+"' WHERE id="+model.getId();
		return new JdbcControl().execute(sql);
	}
	
	public boolean deletApp(int id)
	{
		String sql = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` WHERE id="+id;
		return new JdbcControl().execute(sql);
	}
	
	public boolean addApp(AppModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app`("
				+ "appkey,appname,hold_percent,remark,app_type) value("
				+ "'"+model.getAppkey()+"','"+model.getAppname()+"',"+model.getHold_percent()
				+ ",'"+model.getRemark()+"',"+model.getAppType()+")";
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateAppLoginAccount(int appId,int userId)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app set user_id = " + userId + " where id = " + appId;
		
		return new JdbcControl().execute(sql);
	}
	
}
