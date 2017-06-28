package com.system.dao.analy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.ConnectionCallBack;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.analy.MrSummerRecordModel;
import com.system.util.StringUtil;

public class MrSummerRecordDao
{
	public Map<String, Object> loadMrSummerRecordData(String startDate,String endDate,int spId,String spTroneName,int spTroneId,int pageIndex)
	{
		String params = "a.mr_summer_id,b.cp_mr_summer_id,a.price,a.trone_order_id,a.mr_date,a.trone_name,a.trone_id,a.sp_id,a.sp_name,a.sp_trone_id,a.sp_trone_name,a.cp_id,a.cp_name,a.data_rows,a.amount,b.show_data_rows,b.show_amount ";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String query = "";
		
		if(spId>0)
		{
			query += " AND d.id = " + spId;
		}
		
		if(!StringUtil.isNullOrEmpty(spTroneName))
		{
			query += " AND c.name like '%" + spTroneName + "%' ";
		}
		
		if(spTroneId>0)
		{
			query += " AND c.id = " + spTroneId;
		}
		
		String sql = " select " + Constant.CONSTANT_REPLACE_STRING + " from (";
		sql += " SELECT a.id mr_summer_id,b.price,e.id trone_order_id,a.`mr_date` mr_date,b.`trone_name`,b.id trone_id,d.id sp_id,d.`short_name` sp_name,c.id sp_trone_id,c.`name` sp_trone_name,";
		sql += " f.id cp_id,f.`short_name` cp_name,a.data_rows data_rows,a.amount amount";
		sql += " FROM daily_log.tbl_mr_summer a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` e ON a.`trone_order_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON e.`cp_id` = f.id";
		sql += " WHERE a.`mr_date` >= '" + startDate + "' AND a.`mr_date` <= '" + endDate + "'";
		sql += query;
		sql += " AND a.`record_type` = 1";
		sql += " ORDER BY a.`mr_date`,CONVERT(d.`short_name` USING gbk),CONVERT(c.`name` USING gbk),CONVERT(f.`short_name` USING gbk) ASC";
		sql += " ) a";
		sql += " LEFT JOIN (";
		sql += " SELECT a.id cp_mr_summer_id,b.price,e.id trone_order_id,a.`mr_date` mr_date,b.`trone_name`,b.`id` trone_id,d.id sp_id,d.`short_name` sp_name,c.id sp_trone_id,c.`name` sp_trone_name,";
		sql += " f.id cp_id,f.`short_name` cp_name,a.data_rows show_data_rows,a.amount show_amount";
		sql += " FROM daily_log.`tbl_cp_mr_summer` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` e ON a.`trone_order_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b ON e.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON e.`cp_id` = f.`id`";
		sql += " WHERE a.`mr_date` >= '" + startDate + "' AND a.`mr_date` <= '"+ endDate +"'";
		sql += query;
		sql += " AND a.`record_type` = 1";
		sql += " ORDER BY a.`mr_date`,CONVERT(d.`short_name` USING gbk),CONVERT(c.`name` USING gbk),CONVERT(f.`short_name` USING gbk) ASC";
		sql += " ) b";
		sql += " ON a.mr_date = b.mr_date AND a.trone_order_id = b.trone_order_id where a.mr_date is not null";
		sql += " ORDER BY a.mr_date,CONVERT(a.sp_name USING gbk),CONVERT(a.sp_trone_name USING gbk),CONVERT(a.cp_name USING gbk) ASC";
		
		JdbcControl control = new JdbcControl();
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("count", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("data", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, params) + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<MrSummerRecordModel> list = new ArrayList<MrSummerRecordModel>();
				
				while(rs.next())
				{
					
					MrSummerRecordModel model = new MrSummerRecordModel();
					model.setMrSummerId(rs.getInt("mr_summer_id"));
					model.setCpMrSummerId(rs.getInt("cp_mr_summer_id"));
					model.setFeeDate(StringUtil.getString(rs.getString("mr_date"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setSpShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpId(rs.getInt("sp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setTroneId(rs.getInt("trone_id"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setDataRows(rs.getInt("data_rows"));
					model.setShowDataRows(rs.getInt("show_data_rows"));
					model.setAmount(rs.getFloat("amount"));
					model.setShowAmount(rs.getFloat("show_amount"));
					list.add(model);
				}
				
				return list;
			}
		}));
		return map;
	}
	
	public MrSummerRecordModel loadMrSummerRecordById(int mrSummerId,int cpMrSummerId)
	{
		String params = "a.hold_percent,a.mr_summer_id,b.cp_mr_summer_id,a.price,a.trone_order_id,a.mr_date,a.trone_name,a.trone_id,a.sp_id,a.sp_name,a.sp_trone_id,a.sp_trone_name,a.cp_id,a.cp_name,a.data_rows,a.amount,b.show_data_rows,b.show_amount ";
		
		String sql = " select " + params + " from (";
		sql += " SELECT e.hold_percent,a.id mr_summer_id,b.price,e.id trone_order_id,a.`mr_date` mr_date,b.`trone_name`,b.id trone_id,d.id sp_id,d.`short_name` sp_name,c.id sp_trone_id,c.`name` sp_trone_name,";
		sql += " f.id cp_id,f.`short_name` cp_name,a.data_rows data_rows,a.amount amount";
		sql += " FROM daily_log.tbl_mr_summer a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` e ON a.`trone_order_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON e.`cp_id` = f.id";
		sql += " WHERE  a.id = " + mrSummerId;
		sql += " ) a";
		sql += " LEFT JOIN (";
		sql += " SELECT a.id cp_mr_summer_id,b.price,e.id trone_order_id,a.`mr_date` mr_date,b.`trone_name`,b.`id` trone_id,d.id sp_id,d.`short_name` sp_name,c.id sp_trone_id,c.`name` sp_trone_name,";
		sql += " f.id cp_id,f.`short_name` cp_name,a.data_rows show_data_rows,a.amount show_amount";
		sql += " FROM daily_log.`tbl_cp_mr_summer` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` e ON a.`trone_order_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b ON e.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON e.`cp_id` = f.`id`";
		sql += " WHERE a.id = " + cpMrSummerId;
		sql += " ) b";
		sql += " ON a.mr_date = b.mr_date AND a.trone_order_id = b.trone_order_id  where a.mr_date is not null";
		
		JdbcControl control = new JdbcControl();
		
		return (MrSummerRecordModel)control.query(sql,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					MrSummerRecordModel model = new MrSummerRecordModel();
					model.setMrSummerId(rs.getInt("mr_summer_id"));
					model.setCpMrSummerId(rs.getInt("cp_mr_summer_id"));
					model.setFeeDate(StringUtil.getString(rs.getString("mr_date"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setSpShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpId(rs.getInt("sp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setTroneId(rs.getInt("trone_id"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setDataRows(rs.getInt("data_rows"));
					model.setShowDataRows(rs.getInt("show_data_rows"));
					model.setAmount(rs.getFloat("amount"));
					model.setShowAmount(rs.getFloat("show_amount"));
					model.setHoldPercent(rs.getInt("hold_percent"));
					return model;
				}
				
				return null;
			}
		});
	}
	
	public void addMrSummerRecordModel(MrSummerRecordModel model)
	{
		final String sql1 = "INSERT INTO daily_log.`tbl_mr_summer`(sp_id,cp_id,mcc,province_id,city_id,trone_id,trone_order_id,mr_date,data_rows,amount,record_type) VALUE(" + model.getSpId() + "," + model.getCpId() + ",460,32,416," + model.getTroneId() + "," + model.getTroneOrderId() + ",'" + model.getFeeDate() + "'," + model.getDataRows() + "," + model.getAmount() + ",1);";
		
		final String sql2 = "INSERT INTO daily_log.tbl_cp_mr_summer(cp_id,mcc,province_id,city_id,trone_order_id,mr_date,data_rows,amount,record_type) VALUE(" + model.getCpId() + ",460,32,416," + model.getTroneOrderId() + ",'" + model.getFeeDate() + "'," + model.getShowDataRows() + "," + model.getShowAmount() + ",1)";
		
		JdbcControl control = new JdbcControl();
		control.getConnection(new ConnectionCallBack()
		{
			@Override
			public void onConnectionCallBack(Statement stmt, ResultSet rs)
					throws SQLException
			{
				stmt.execute(sql1);
				stmt.execute(sql2);
			}
		});
	}
	
	public boolean existMrSummerRecord(MrSummerRecordModel model)
	{
		String sql = "SELECT count(*) FROM daily_log.`tbl_mr_summer` a WHERE a.trone_order_id = " + model.getTroneOrderId() + " AND mr_date = '" + model.getFeeDate() + "' AND record_type = 1;";
		return (Boolean)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1)>0;
					
				return false;
			}
		});
	}
	
	public void deleteMrSummerRecordModel(int mrSummerId,int cpMrSummerId)
	{
		JdbcControl control = new JdbcControl();
		final String sql1 = "DELETE FROM daily_log.`tbl_mr_summer` WHERE id = " + mrSummerId;
		final String sql2 = "DELETE FROM daily_log.`tbl_cp_mr_summer` WHERE id = " + cpMrSummerId;
		control.getConnection(new ConnectionCallBack()
		{
			@Override
			public void onConnectionCallBack(Statement stmt, ResultSet rs)
					throws SQLException
			{
				stmt.execute(sql1);
				stmt.execute(sql2);
			}
		});
	}
	
	public void updateMrSummerRecordModel(MrSummerRecordModel model)
	{
		JdbcControl control = new JdbcControl();
		final String sql1 = "UPDATE daily_log.`tbl_mr_summer` SET data_rows = " + model.getDataRows() + ",amount = " + model.getAmount() + " WHERE id = " + model.getMrSummerId();
		final String sql2 = "UPDATE daily_log.`tbl_cp_mr_summer` SET data_rows = " + model.getShowDataRows() + ",amount = " + model.getShowAmount() + " WHERE id = " + model.getCpMrSummerId();
		
		control.getConnection(new ConnectionCallBack()
		{
			@Override
			public void onConnectionCallBack(Statement stmt, ResultSet rs)
					throws SQLException
			{
				stmt.execute(sql1);
				stmt.execute(sql2);
			}
		});
	}
}
