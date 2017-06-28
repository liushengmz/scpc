package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcAdControl;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.AdChannelModel;
import com.system.model.AdCpModel;
import com.system.util.StringUtil;

public class AdCpDao {
	public Map<String, Object> loadCp(int pageIndex,int id)
	{
		String sqlcount = " count(*) ";
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING+" FROM ad_log.`tbl_app_summer` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid`=b.`id` "
				+ "WHERE b.user_id="+id+" AND a.status=1 ";
		
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcAdControl control = new JdbcAdControl();
		
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
		
		new JdbcAdControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(show_new_user_rows) userrows,sum(show_amount) amounts,sum(extend_fee) extendfee "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					result.put("userrows", rs.getInt(1));
					result.put("amount", rs.getDouble(2));
					result.put("extendfee", rs.getDouble(3));
				}
				
				return 0;
			}
		});
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.id,a.fee_date,b.appname,a.show_new_user_rows,a.show_amount,a.extend_fee ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AdCpModel> list = new ArrayList<AdCpModel>();
						AdCpModel model = null;
						while (rs.next()) {
							model = new AdCpModel();
							model.setId(rs.getInt("id"));
							model.setAppname(rs.getString("appname"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("show_new_user_rows"));
							model.setAmount(rs.getDouble("show_amount"));
							model.setExtendFee(rs.getDouble("extend_fee"));
							list.add(model);
						}
						
						return list;
					}
				}));
		return result;
	}
	
	public Map<String, Object> loadCp(int pageIndex,int id,String startdate,String enddate)
	{
		String sqlcount = " count(*) ";
		String sql = "SELECT "+Constant.CONSTANT_REPLACE_STRING+" FROM ad_log.`tbl_app_summer` a "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_ad_app` b ON a.`appid`=b.`id` "
				+ "WHERE b.user_id="+id+" AND a.status=1 ";
		
		if(!StringUtil.isNullOrEmpty(startdate))
		{
			sql += " AND fee_date>='"+startdate+"' ";
		}
		
		if(!StringUtil.isNullOrEmpty(enddate))
		{
			sql += " AND fee_date<='"+enddate+"' ";
		}
		
		sql +=  " order by a.fee_date asc ";
		
		String limit = " limit "+Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		JdbcAdControl control = new JdbcAdControl();
		
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
		
		new JdbcAdControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " sum(show_new_user_rows) userrows,sum(show_amount) amounts,sum(extend_fee) extendfee,sum(profit) profit "), new QueryCallBack()
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
		});
		
		result.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.id,a.fee_date,b.appname,a.show_new_user_rows,a.show_amount,a.extend_fee ")+limit,
				new QueryCallBack() {
					
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<AdCpModel> list = new ArrayList<AdCpModel>();
						AdCpModel model = null;
						while (rs.next()) {
							model = new AdCpModel();
							model.setId(rs.getInt("id"));
							model.setAppname(rs.getString("appname"));
							model.setFeeDate(rs.getString("fee_date"));
							model.setNewUserRows(rs.getInt("show_new_user_rows"));
							model.setAmount(rs.getDouble("show_amount"));
							model.setExtendFee(rs.getDouble("extend_fee"));
							list.add(model);
						}
						
						return list;
					}
				}));
		return result;
	}
}
