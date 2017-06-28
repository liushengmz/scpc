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
import com.system.model.CpAppModel;
import com.system.util.StringUtil;

public class CpAppDao {
	public Map<String, Object> loadCpAppByPageindex(int pageIndex)
	{
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ "FROM ad_log.`tbl_app_summer` a"
				+ "  LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid`=b.`id`";
		
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
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.`appname`,b.`appkey` ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<CpAppModel> list = new ArrayList<CpAppModel>();
						CpAppModel model = null;
						while (rs.next()) {
							model = new CpAppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("new_user_rows"));
							model.setAmount(rs.getDouble("amount"));
							model.setShowNewUserRows(rs.getInt("show_new_user_rows"));
							model.setShowAmount(rs.getDouble("show_amount"));
							model.setExtendFee(rs.getDouble("extend_fee"));
							model.setProfit(rs.getDouble("profit"));
							model.setStatus(rs.getInt("status"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public Map<String, Object> loadCpApp(int pageIndex,String appname,String startDate,String endDate)
	{
		
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING
				+ "FROM ad_log.`tbl_app_summer` a"
				+ "  LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid`=b.`id` WHERE 1=1 ";
		
		sql+="  AND fee_date>='"+startDate+"' AND fee_date<='"+endDate+"'  "; //用于拼接查询的计费时间的时间范围
		
		if(!StringUtil.isNullOrEmpty(appname))
		{
			sql += " AND b.appname LIKE '%"+appname+"%' ";
		}
		
		sql += " order by a.fee_date desc ";
		
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
		
		/*new JdbcAdControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(show_new_user_rows) userrows,sum(show_amount) amounts,sum(extend_fee) extendfee,sum(profit) profit "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					result.put("userrows", rs.getInt(1));
					result.put("amount", rs.getDouble(2));
					result.put("extendfee", rs.getDouble(3));
					result.put("profit", rs.getDouble(4));
				}
				
				return 0;
			}
		});*/
		
		control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " SUM(new_user_rows) userrows,SUM(amount) amount,SUM(show_new_user_rows) showuserrows,SUM(show_amount) showamount,SUM(extend_fee) extendfee,SUM(profit) pro  "), 
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						if(rs.next())
						{
							result.put("newUserRows", rs.getInt(1));
							result.put("amountSum", rs.getDouble(2));
							result.put("showUserRows", rs.getInt(3));
							result.put("showAmount", rs.getDouble(4));
							result.put("extendFee", rs.getDouble(5));
							result.put("profit", rs.getDouble(6));
						}
						return 0;
					}
				});
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.`appname`,b.`appkey` ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<CpAppModel> list = new ArrayList<CpAppModel>();
						CpAppModel model = null;
						while (rs.next()) {
							model = new CpAppModel();
							model.setId(rs.getInt("id"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("new_user_rows"));
							model.setAmount(rs.getDouble("amount"));
							model.setShowNewUserRows(rs.getInt("show_new_user_rows"));
							model.setShowAmount(rs.getDouble("show_amount"));
							model.setExtendFee(rs.getDouble("extend_fee"));
							model.setProfit(rs.getDouble("profit"));
							model.setStatus(rs.getInt("status"));
							list.add(model);
						}
						
						return list;
					}
				})); 
		
		return result;
	}
	
	public boolean addCpApp(CpAppModel model)
	{
		String sql = "INSERT INTO ad_log.`tbl_app_summer`(appid,fee_date,new_user_rows,amount,"
				+ "show_new_user_rows,show_amount,extend_fee,profit,status) "
				+ "VALUE("+model.getAppid()+",'"
				+ model.getFeeDate()+"',"
				+ model.getNewUserRows()+","
				+ model.getAmount()+","
				+ model.getShowNewUserRows()+","
				+ model.getShowAmount()+","
				+ model.getExtendFee()+","
				+ model.getProfit()+","+model.getStatus()+")";
		
		return new JdbcAdControl().execute(sql);
	}
	
	public boolean deletCpApp(int id)
	{
		String sql = "DELETE FROM ad_log.`tbl_app_summer` WHERE id="+id;
		
		return new JdbcAdControl().execute(sql);
	}
	
	public CpAppModel loadCpAppById(int id)
	{
		String sql = "SELECT a.*,b.appname,b.appkey,b.id FROM ad_log.`tbl_app_summer` a   "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid`=b.`id` where a.id = "+id;
		
		
		return (CpAppModel)new JdbcAdControl().query(sql,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						
						if(rs.next())
						{
							CpAppModel model = new CpAppModel();
							model.setId(rs.getInt("id"));
							model.setAppid(rs.getInt("appid"));
							model.setAppkey(rs.getString("appkey"));
							model.setAppname(rs.getString("appname"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("new_user_rows"));
							model.setAmount(rs.getDouble("amount"));
							model.setShowNewUserRows(rs.getInt("show_new_user_rows"));
							model.setShowAmount(rs.getDouble("show_amount"));
							model.setExtendFee(rs.getDouble("extend_fee"));
							model.setProfit(rs.getDouble("profit"));
							model.setStatus(rs.getInt("status"));
							return model;
						}
						
						return null;
					}
				});
	}
	
	public boolean updateCpApp(CpAppModel model)
	{
		
		String sql = "UPDATE ad_log.`tbl_app_summer` SET "
				+ "appid="+model.getAppid()+","
				+ " fee_date='"+model.getFeeDate()+"',"
			    + " new_user_rows="+model.getNewUserRows()+","
			    + " amount="+model.getAmount()+","
			    + " show_new_user_rows="+model.getShowNewUserRows()+","
			    + " show_amount="+model.getShowAmount()+","
			    + " extend_fee="+model.getExtendFee()+","
			    + " profit="+model.getProfit()+","
			    + " status="+model.getStatus()+" WHERE id="+model.getId();
		return new JdbcAdControl().execute(sql);
	}
}
