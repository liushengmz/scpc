package com.system.dao.xy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.ConnectionCallBack;
import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.model.xy.XyQdUserModel;
import com.system.model.xy.XyUserModel;
import com.system.util.StringUtil;

public class UserDao
{
	public Map<String, Object> loadUserData(String startDate,String endDate,String keyWord, int pageIndex , int appType)
	{
		String sqlCount = " count(*) ";
		String query = " a.*,b.appname,b.app_type ";
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from game_log.tbl_xy_user_summer a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app b on a.appkey = b.appkey where 1=1 ";
		
		sql += " and active_date >= '" + startDate + "' and active_date <= '" + endDate + "' ";
		
		if(!StringUtil.isNullOrEmpty(keyWord))
			sql += " and ( a.appkey like '%" + keyWord + "%' or b.appname like '%" + keyWord + "%' or a.channelkey like '%" + keyWord + "%')";
		
		if(appType>0)
			sql += " and b.app_type="+appType+" ";
		
		sql += " order by status,active_date,a.channelkey asc";
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		int count = (Integer)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, sqlCount), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		});
		
		result.put("row", count);
		
		count = (Integer)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(data_rows), sum(show_data_rows)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					result.put("data_rows", rs.getInt(1));
					result.put("show_data_rows", rs.getInt(2));
				}
				
				return 0;
			}
		});
		
		result.put("list",new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<XyUserModel> list = new ArrayList<XyUserModel>();
				
				XyUserModel model = null;
				
				while(rs.next())
				{
					model = new XyUserModel();
					
					model.setId(rs.getInt("id"));
					model.setActiveDate(rs.getString("active_date"));
					model.setAppKey(rs.getString("appkey"));
					model.setAppName(StringUtil.getString(rs.getString("appname"),""));
					model.setAppType(rs.getInt("app_type"));
					model.setChannelKey(rs.getString("channelkey"));
					model.setDataRows(rs.getInt("data_rows"));
					model.setShowDataRows(rs.getInt("show_data_rows"));
					model.setStatus(rs.getInt("status"));
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<XyUserModel>  loadUserTodayData(String tableName,String satrtDate,String appKey,String channelKey,int appType)
	{
		String query = " b.appname,b.appkey,b.app_type,a.channelkey,count(*) data_rows  ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from game_log.tbl_xy_user_" + tableName + " a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app b on a.appkey = b.appkey where 1=1 ";
		
		sql += " and addTime >= '" + satrtDate + " 00:00:00' and addTime <= '" + satrtDate + " 23:59:59' ";
		
		if(!StringUtil.isNullOrEmpty(appKey))
			sql += " and a.appkey like '%" + appKey + "%' ";
		
		if(!StringUtil.isNullOrEmpty(channelKey))
			sql += " and a.channelkey like '%" + channelKey + "%' ";
		
		if(appType>0)
			sql += " and b.app_type="+appType+" ";
		
		sql += " group by a.appkey,a.channelkey order by b.appname asc";
		
		return (List<XyUserModel>)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<XyUserModel> list = new ArrayList<XyUserModel>();
				
				XyUserModel model = null;
				
				while(rs.next())
				{
					model = new XyUserModel();
					
					model.setAppKey(rs.getString("appkey"));
					model.setAppName(StringUtil.getString(rs.getString("appname"),""));
					model.setAppType(rs.getInt("app_type"));
					model.setChannelKey(rs.getString("channelkey"));
					model.setDataRows(rs.getInt("data_rows"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public  Map<String, Object> loadQdUserData(String startDate,String endDate,int userId,int pageIndex)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from game_log.tbl_xy_user_summer a "
				+ "left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app b on a.appkey = b.appkey "
				+ "left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel c on a.channelkey = c.channel "
				+ "where 1=1 and status = 1 and c.userid = " + userId + " and a.active_date >= '" + startDate + "' "
				+ "and a.active_date <= '" + endDate + "' order by a.active_date,c.channel asc";
		 
		String sqlCount = " count(*) ";
		String query = " a.*,b.appname ";
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		int count = (Integer)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, sqlCount), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		});
		
		result.put("row", count);
		
		count = (Integer)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(show_data_rows) "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		});
		
		result.put("show_data_rows", count);
		
		result.put("list",new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING,query) + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<XyQdUserModel> list = new ArrayList<XyQdUserModel>();
				
				XyQdUserModel model = null;
				
				while(rs.next())
				{
					model = new XyQdUserModel();
					
					model.setId(rs.getInt("id"));
					model.setActiveDate(rs.getString("active_date"));
					model.setChannelKey(StringUtil.getString(rs.getString("channelkey"), ""));
					model.setAppName(StringUtil.getString(rs.getString("appname"),""));
					model.setShowDataRows(rs.getInt("show_data_rows"));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return result;
	}
	
	//更新渠道CPA展示的数据
	public boolean updateQdData(int id,int showDataRows)
	{
		String sql = "update game_log.tbl_xy_user_summer set show_data_rows = " + showDataRows + " where id = " + id;
		return new JdbcGameControl().execute(sql);
	}
	
	public boolean deleteFromSummer(String startDate,String endDate)
	{
		//当增加了实时同步的渠道后，就不能删除立即同步的数据
		//return new JdbcGameControl().execute("DELETE FROM game_log.`tbl_xy_user_summer` WHERE active_date >= '" + startDate + "' AND active_date <= '" + endDate + "'");
		
		String sql = "DELETE  game_log.a from game_log.`tbl_xy_user_summer` a, " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel b ";
		sql += " WHERE a.active_date >= '" + startDate + "' AND a.active_date <= '" + endDate + "'";
		sql += " and a.channelkey = b.channel and b.syn_type = 1";
		
		return new JdbcGameControl().execute(sql);
	}
	
	public boolean analyUserToSummer(String tableName,String startDate,String endDate)
	{
		String sql  = " INSERT INTO game_log.`tbl_xy_user_summer`(active_date,appkey,channelkey,data_rows) "
				+ " SELECT DATE_FORMAT(addTime,'%Y-%m-%d') active_date,appkey,channelkey,COUNT(*) data_rows "
				+ " FROM game_log.`tbl_xy_user_" + tableName + "` a "
				+ " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel b on a.channelkey = b.channel where b.syn_type = 1 and addTime >= '" + startDate + " 00:00:00' AND addTime <= '" + endDate + " 23:59:59' "
				+ " GROUP BY active_date,appkey,channelkey ORDER BY active_date,appkey,channelkey ";
		
		return new JdbcGameControl().execute(sql);
	}
	
	public boolean updateQdShowData(String startDate,String endDate)
	{
		String sql = " UPDATE game_log.`tbl_xy_user_summer` a," + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_channel` b "
				+ " SET show_data_rows = a.`data_rows`*(100-b.`hold_percent`)/100  "
				+ " WHERE show_data_rows = 0  AND a.`channelkey` = b.`channel`"
				+ " AND active_date >= '" + startDate + "' "
				+ " AND active_date <= '" + endDate + "'"
				+ " AND b.syn_type = 1";
		
		return new JdbcGameControl().execute(sql);
	}
	
	public boolean updateQdShowDataStatus(String startDate,String endDate)
	{
		String sql = "UPDATE game_log.`tbl_xy_user_summer` set status = 1 where active_date >= '" + startDate + "' "
				+ "AND active_date <= '" + endDate + "'";
		
		return new JdbcGameControl().execute(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<XyUserModel> queryQdShowDataWithHour(String tableName,String startDate,int hour)
	{
		String sql = " select appkey,channelkey,count(*) data_rows,count(*)*((100-b.hold_percent)/100) show_data_rows";
		sql += " from game_log.tbl_xy_user_" + tableName + " a";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel b";
		sql += " on a.channelkey = b.channel";
		sql += " where b.syn_type = 2";
		sql += " and a.addTime >= '" + startDate + " " + hour + ":00:00' and a.addTime <= '" + startDate + " " + hour + ":59:59'";
		sql += " group by appkey,channelkey";
		
		return (List<XyUserModel>)new JdbcGameControl().query(sql,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<XyUserModel> list = new ArrayList<XyUserModel>();
						
				while(rs.next())
				{
					XyUserModel model = new XyUserModel();
					model.setAppKey(rs.getString("appkey"));
					model.setChannelKey(rs.getString("channelkey"));
					model.setDataRows(rs.getInt("data_rows"));
					model.setShowDataRows(rs.getInt("show_data_rows"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public void analyUserList(final List<XyUserModel> list,final String startDate)
	{
		new JdbcGameControl().getConnection(new ConnectionCallBack()
		{
			@Override
			public void onConnectionCallBack(Statement stmt,ResultSet rs)
					throws SQLException
			{
				String sql = "select id from game_log.tbl_xy_user_summer where active_date = '%s' and appkey = '%s' and channelkey = '%s'";
				String sqlUpdate = "update game_log.tbl_xy_user_summer set data_rows = (data_rows + %s), show_data_rows = (show_data_rows + %s) where id = %s";
				String sqlInsert = "insert into game_log.tbl_xy_user_summer(active_date,appkey,channelkey,data_rows,show_data_rows,status) value('%s','%s','%s',%s,%s,1)";
				int id = -1;
				for(XyUserModel model : list)
				{
					
					rs = stmt.executeQuery(String.format(sql, startDate,model.getAppKey(),model.getChannelKey()));
					if(rs.next())
						id = rs.getInt(1);
					if(id>0)
					{
						String update = String.format(sqlUpdate, model.getDataRows(),model.getShowDataRows(),id);
						stmt.execute(update);
					}
					else
					{
						String insert = String.format(sqlInsert, startDate,model.getAppKey(),model.getChannelKey(),model.getDataRows(),model.getShowDataRows());
						stmt.execute(insert);
					}
				}
			}
		});
	}
	
	
	
}
