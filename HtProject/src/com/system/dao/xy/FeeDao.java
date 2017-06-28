package com.system.dao.xy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.model.xy.XyFeeModel;
import com.system.model.xy.XyUserCpsModel;
import com.system.util.StringUtil;


public class FeeDao
{
	public boolean analyFeeToSummer(String tableName,String startDate,String endDate)
	{
		String sql = " INSERT INTO game_log.`tbl_xypay_summer`(fee_date,appkey,channelid,data_rows,amount) ";
		sql += " SELECT DATE_FORMAT(createdate,'%Y-%m-%d') fee_date,appkey,channelid,";
		sql += " COUNT(*) data_rows,sum(amount)/100 amounts ";
		sql += " FROM game_log.`tbl_xypay_" + tableName + "`";
		sql += " where createdate >= '" + startDate + " 00:00:00' AND createdate <= '" + endDate + " 23:59:59' and status = 0 ";
		sql += " GROUP BY fee_date,appkey,channelid ";
		sql += " ORDER BY fee_date,appkey,channelid";
		
		JdbcGameControl control = new JdbcGameControl();
		
		control.execute(sql);
		
		String sql2 = "insert into game_log.tbl_xy_fee_summer (fee_date,appkey,amount)";
		sql2 += " select fee_date,appkey,sum(amount) amounts ";
		sql2 += " from game_log.tbl_xypay_summer";
		sql2 += " where fee_date >= '" + startDate + "' and fee_date <= '" + endDate + "' group by appkey";
		
		control.execute(sql2);
		
		return true;
	}
	
	public boolean deleteFeeFromSummer(String startDate,String endDate)
	{
		return new JdbcGameControl().execute("DELETE FROM game_log.`tbl_xypay_summer` WHERE fee_date >= '" + startDate + "' AND fee_date <= '" + endDate + "'");
	}
	
	public boolean updateQdAmount(String startDate,String endDate)
	{
		String sql = " UPDATE game_log.`tbl_xy_fee_summer` a," + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_xy_app` b "
				+ " SET show_amount = a.`amount`*(100-b.`hold_percent`)/100  "
				+ " WHERE show_amount = 0  AND a.`appkey` = b.`appkey`"
				+ " AND fee_date >= '" + startDate + "' "
				+ " AND fee_date <= '" + endDate + "'";
		
		return new JdbcGameControl().execute(sql);
	}
	
	public boolean updateQdAmountStatus(String startDate,String endDate)
	{
		String sql = "UPDATE game_log.`tbl_xy_fee_summer` set status = 1 where fee_date >= '" + startDate + "' "
				+ "AND fee_date <= '" + endDate + "'";
		
		return new JdbcGameControl().execute(sql);
	}
	
	public Map<String, Object> loadChannelAppFee(String startDate,String endDate,String keyWord, int pageIndex , int appType)
	{
		String query = " a.id,a.fee_date,b.appname,b.appkey,b.app_type,a.channelid channelkey,0 data_rows,a.amount,a.show_amount,a.status ";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from game_log.tbl_xypay_summer a";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app b on a.appkey = b.appkey  ";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel d on a.channelid = d.channel ";
		sql += " where 1=1 ";
		sql += " and d.settle_type = 2 ";
		sql += " and a.fee_date >= '" + startDate + "' ";
		sql += " and a.fee_date <= '" + endDate + "' ";
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " and (b.appname like '%" + keyWord + "%' or b.appkey like '%" + keyWord + "%' or a.channelid like '%" + keyWord + "%') ";
		}
		
		if(appType>0)
			sql += " and b.app_type="+appType+" ";
			
		String orders = " order by a.fee_date asc,appname asc,a.channelid asc ";
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		
		int count = (Integer)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "), new QueryCallBack()
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
		
		new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(0), sum(a.amount),sum(a.show_amount) "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					result.put("data_rows", rs.getInt(1));
					result.put("total_amount", rs.getFloat(2));
					result.put("total_show_amount", rs.getFloat(3));
				}
				
				return 0;
			}
		});
		
		result.put("list",new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + orders + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<XyFeeModel> list = new ArrayList<XyFeeModel>();
				
				XyFeeModel model = null;
				
				while(rs.next())
				{
					model = new XyFeeModel();
					
					model.setId(rs.getInt("id"));
					model.setFeeDate(rs.getString("fee_date"));
					model.setAppKey(rs.getString("appkey"));
					model.setAppName(StringUtil.getString(rs.getString("appname"),""));
					model.setAppType(rs.getInt("app_type"));
					model.setChannelId(rs.getString("channelkey"));
					model.setDataRows(rs.getInt("data_rows"));
					model.setAmount(rs.getFloat("amount"));
					model.setShowAmount(rs.getFloat("show_amount"));
					model.setStatus(rs.getInt("status"));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return result;
	}
	
	public Map<String, Object> loadAppFee(String startDate,String endDate,String appKey,int pageIndex,int appType)
	{
		String sqlCount = " count(*) ";
		String query = " a.*,b.appname,b.app_type ";
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from game_log.tbl_xy_fee_summer a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app b on a.appkey = b.appkey where 1=1 ";
		
		sql += " and fee_date >= '" + startDate + "' and fee_date <= '" + endDate + "' ";
		
		if(!StringUtil.isNullOrEmpty(appKey))
			sql += " and a.appkey like '%" + appKey + "%' ";
		
		if(appType>0)
			sql += " and b.app_type="+appType+" ";
		
		sql += " order by fee_date,b.appname asc";
		
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
		
		new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "  sum(amount) amounts, sum(show_amount) show_amounts "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					result.put("amounts", rs.getFloat(1));
					result.put("show_amounts", rs.getFloat(2));
				}
				
				return 0;
			}
		});
		
		result.put("list",new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<XyFeeModel> list = new ArrayList<XyFeeModel>();
				
				XyFeeModel model = null;
				
				while(rs.next())
				{
					model = new XyFeeModel();
					
					model.setId(rs.getInt("id"));
					model.setFeeDate(rs.getString("fee_date"));
					model.setAppKey(rs.getString("appkey"));
					model.setAppName(StringUtil.getString(rs.getString("appname"),""));
					model.setAppType(rs.getInt("app_type"));
					model.setAmount(rs.getFloat("amount"));
					model.setShowAmount(rs.getFloat("show_amount"));
					model.setStatus(rs.getInt("status"));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return result;
	}
	
	/**
	 * LOAD CP 自己应用的数据（应用 CPS 分成展示页面 ）
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param pageIndex
	 * @return
	 */
	public Map<String, Object> loadQdAppFee(String startDate,String endDate,int userId,int pageIndex)
	{
		String sqlCount = " count(*) ";
		String query = " a.id,a.status,fee_date,b.appname,a.appkey,show_amount,data_rows ";
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "select  " + Constant.CONSTANT_REPLACE_STRING
				+ " from game_log.tbl_xy_fee_summer a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app b on a.appkey = b.appkey ";
		
		//董老板说把原来扣量后的激活数改为真实用户数 2015.10.30 16:21 更改者 	Andy.Chen (show_data_rows,data_rows) 
		sql += " LEFT JOIN ( SELECT active_date,appkey,SUM(data_rows) data_rows FROM game_log.`tbl_xy_user_summer` WHERE 1=1 AND active_date >= '"
				+ startDate + "' AND active_date <= '" + endDate
				+ "'  AND STATUS = 1 GROUP BY active_date,appkey  ) c ON a.fee_date = c.active_date AND a.`appkey` = c.appkey WHERE 1=1  AND fee_date >= '"
				+ startDate + "' AND fee_date <= '" + endDate
				+ "' AND a.status = 1 AND b.`user_id` = " + userId
				+ " ORDER BY fee_date,b.appname ASC ";
		
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
		
		new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(show_amount) show_amounts,sum(data_rows) show_rows "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					result.put("show_amounts", rs.getFloat(1));
					result.put("show_rows", rs.getFloat(2));
				}
				
				return 0;
			}
		});
		
		result.put("list",new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<XyFeeModel> list = new ArrayList<XyFeeModel>();
				
				XyFeeModel model = null;
				
				while(rs.next())
				{
					model = new XyFeeModel();
					
					model.setId(rs.getInt("id"));
					model.setFeeDate(rs.getString("fee_date"));
					model.setAppKey(rs.getString("appkey"));
					model.setAppName(StringUtil.getString(rs.getString("appname"),""));
					model.setShowAmount(rs.getFloat("show_amount"));
					model.setDataRows(rs.getInt("data_rows"));
					model.setStatus(rs.getInt("status"));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return result;
	}
	
	/**
	 * 本方法用于查询该用户ID下，一定时间内的同步费用情况，
	 * 查询结果，包括计费日期、游戏名、激活数、同步费用、总激活
	 * 数和总费用
	 * @param startDate  开始时间
	 * @param endDate    结束时间
	 * @param userId     用户ID
	 * @param pageIndex  用于分页
	 * @return MAP结果集
	 */
	public Map<String, Object> loadQdUserFee(String startDate,String endDate,int userId,String keyWord,int pageIndex){
		String sqlCount = " count(*) ";
		String query = " a.id,a.fee_date,b.appname,b.appkey,c.channelkey,c.data_rows,a.show_amount ";
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING +" FROM game_log.tbl_xypay_summer a ";
		
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_app b ON a.appkey = b.appkey ";
		sql += " LEFT JOIN game_log.tbl_xy_user_summer c ON a.appkey = c.appkey";
		sql += " AND a.channelid = c.channelkey AND a.fee_date = c.active_date ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_xy_channel d ON c.channelkey = d.channel";
		sql += " WHERE 1=1  AND d.settle_type = 2 AND a.`status` = 1 AND d.`userid` = " + userId;
		sql += " AND a.fee_date >= '"+ startDate +"' AND a.fee_date <= '" + endDate + "'";
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND b.appname like '%"+ keyWord +"%' ";
		}
		
		sql += " ORDER BY a.fee_date ASC,appname ASC,channelkey ASC ";
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		int count = (Integer)new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, sqlCount),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
							return rs.getInt(1);
						return 0;
					}
				});
		
		result.put("rows", count);
		
		new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(c.data_rows),sum(a.show_amount) "),
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next()){
							result.put("data_rows", rs.getInt(1));
							result.put("show_amounts", rs.getDouble(2));
						}
						return 0;
					}
				});
		
		result.put("list", new JdbcGameControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query)+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<XyUserCpsModel> list = new ArrayList<XyUserCpsModel>();
						XyUserCpsModel model = null;
						
						while (rs.next()) {
							model = new XyUserCpsModel();
							model.setId(rs.getInt("id"));
						    model.setFee_date(rs.getString("fee_date"));
						    model.setChannelKey(StringUtil.getString(rs.getString("channelkey"), ""));
						    model.setAppName(rs.getString("appname"));
						    model.setDataRows(rs.getInt("data_rows"));
						    model.setShowAmount(rs.getDouble("show_amount"));
						    
						    list.add(model);
						}
						return list;
					}
				}));
		
		return result;
	}
	
	//更新游戏CP(CPS)的展示金额
	public boolean updateQdFee(int id,float showAmount)
	{
		String sql = "update game_log.tbl_xy_fee_summer set show_amount = " + showAmount + " where id = " + id;
		return new JdbcGameControl().execute(sql);
	}
	
	
	//更新游戏渠道(CPS)的展示金额
	public boolean updateChannelFee(int id,float showAmount)
	{
		String sql = "update game_log.tbl_xypay_summer set show_amount = " + showAmount + " where id = " + id;
		return new JdbcGameControl().execute(sql);
	}
	
}
