package com.system.dao.xy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.system.database.ConnectionCallBack;
import com.system.database.JdbcControl;
import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.model.xy.XyUserModel;

public class UserDao
{
	//手动更新渠道(CPA)的同步数据
	public boolean updateQdData(int id,int showDataRows)
	{
		String sql = "update game_log.tbl_xy_user_summer set show_data_rows = " + showDataRows + " where id = " + id;
		return new JdbcControl().execute(sql);
	}
	
	public boolean deleteFromSummer(String startDate,String endDate)
	{
		//当增加了实时同步的渠道后，就不能删除立即同步的数据
		//return new JdbcControl().execute("DELETE FROM daily_log.`tbl_xy_user_summer` WHERE active_date >= '" + startDate + "' AND active_date <= '" + endDate + "'");
		
		String sql = "DELETE  a from game_log.`tbl_xy_user_summer` a, daily_config.tbl_xy_channel b ";
		sql += " WHERE a.active_date >= '" + startDate + "' AND a.active_date <= '" + endDate + "'";
		sql += " and a.channelkey = b.channel and b.syn_type = 1";
		
		return new JdbcControl().execute(sql);
	}
	
	//将渠道的用户的数据分析进激活汇总表tbl_xy_user_summer
	public boolean analyUserToSummer(String tableName,String startDate,String endDate)
	{
		String sql  = " INSERT INTO game_log.`tbl_xy_user_summer`(active_date,appkey,channelkey,data_rows) "
				+ " SELECT DATE_FORMAT(addTime,'%Y-%m-%d') active_date,appkey,channelkey,COUNT(*) data_rows "
				+ " FROM game_log.`tbl_xy_user_" + tableName + "` a "
				+ " left join daily_config.tbl_xy_channel b on a.channelkey = b.channel where b.syn_type = 1 and addTime >= '" + startDate + " 00:00:00' AND addTime <= '" + endDate + " 23:59:59' "
				+ " GROUP BY active_date,appkey,channelkey ORDER BY active_date,appkey,channelkey ";
		
		return new JdbcControl().execute(sql);
	}
	
	//韦工接的一款游戏，硬拼上我们 tbl_xy_user_summer 这个系统
	public boolean analyZyLDataToSummer(String startDate,String endDate)
	{
		String sql = "INSERT INTO game_log.`tbl_xy_user_summer`(active_date,appkey,channelkey,data_rows)"
		+ " SELECT DATE_FORMAT(regist_time,'%Y-%m-%d') active_date,appkey,channel,COUNT(*) data_rows"
		+ " FROM game_log.`tbl_zyl_detail` a"
		+ " WHERE regist_time >= '" + startDate + " 00:00:00' AND regist_time <= '" + endDate + " 23:59:59'"
		+ " GROUP BY appkey,channel,active_date";
		
		return new JdbcControl().execute(sql);
	}
	
	//更新渠道(CPA)扣量后的数据
	public boolean updateQdShowData(String startDate,String endDate)
	{
		String sql = " UPDATE game_log.`tbl_xy_user_summer` a,daily_config.`tbl_xy_channel` b "
				+ " SET show_data_rows = a.`data_rows`*(100-b.`hold_percent`)/100  "
				+ " WHERE show_data_rows = 0  AND a.`channelkey` = b.`channel`"
				+ " AND active_date >= '" + startDate + "' "
				+ " AND active_date <= '" + endDate + "'"
				+ " AND b.syn_type = 1";
		
		return new JdbcControl().execute(sql);
	}
	
	//更新渠道(CPA)的数据展示状态
	public boolean updateQdShowDataStatus(String startDate,String endDate)
	{
		String sql = "UPDATE game_log.`tbl_xy_user_summer` set status = 1 where active_date >= '" + startDate + "' "
				+ "AND active_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	
	//查询渠道(CPA实时)
	@SuppressWarnings("unchecked")
	public List<XyUserModel> queryQdShowDataWithHour(String tableName,String startDate,int hour)
	{
		String sql = " select appkey,channelkey,count(*) data_rows,count(*)*((100-b.hold_percent)/100) show_data_rows";
		sql += " from game_log.tbl_xy_user_" + tableName + " a";
		sql += " left join daily_config.tbl_xy_channel b";
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
	
	//更新渠道(CPA实时)的数据
	public void analyUserList(final List<XyUserModel> list,final String startDate)
	{
		new JdbcControl().getConnection(new ConnectionCallBack()
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
