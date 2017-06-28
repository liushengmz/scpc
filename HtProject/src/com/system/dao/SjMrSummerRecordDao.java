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
import com.system.model.SjMrSummerRecordModel;
import com.system.util.StringUtil;

public class SjMrSummerRecordDao
{
	public Map<String, Object> loadSjMrSummerRecordData(String startMonth,String endMonth,SjMrSummerRecordModel params,int pageIndex)
	{
		String sql = " SELECT " + Constant.CONSTANT_REPLACE_STRING;
		
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_sj_ori_data a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.trone_order_id = b.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone d ON c.sp_trone_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.sp_id = e.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON b.cp_id = f.id";
		sql += " WHERE CONCAT(YEAR,'-',LPAD(MONTH, 2 ,0)) >= '" + startMonth + "' AND CONCAT(YEAR,'-',LPAD(MONTH, 2 ,0)) <= '" + endMonth + "'";
		
		if(params.getCpId()>0)
		{
			sql += " and f.id =" + params.getCpId();
		}
		if(params.getSpId()>0)
		{
			sql += " and e.id =" + params.getSpId();
		}
		if(params.getSpTroneId()>0)
		{
			sql += " and d.id =" + params.getSpTroneId();
		}
		if(params.getSaveLocate()>0)
		{
			sql += " and a.save_locate =" + params.getSaveLocate();
		}
		
		String  sqlLimit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + "," + Constant.PAGE_SIZE;
		
		String queryReplaceString = "a.id,e.id sp_id,b.id trone_order_id,c.id trone_id,e.full_name sp_name,f.id cp_id,f.full_name cp_name,d.id sp_trone_id,d.name sp_trone_name,c.price,a.year,a.month,a.sp_data_rows,a.sp_amount,a.cp_data_rows,a.cp_amount,a.save_locate";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		map.put("rows", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, queryReplaceString) + sqlLimit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SjMrSummerRecordModel> list = new ArrayList<SjMrSummerRecordModel>();
				
				while(rs.next())
				{
					SjMrSummerRecordModel model = new SjMrSummerRecordModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setCpName(StringUtil.getString(rs.getString("cp_name"),""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setYear(rs.getInt("year"));
					model.setMonth(rs.getInt("month"));
					model.setSpDataRows(rs.getInt("sp_data_rows"));
					model.setSpAmount(rs.getFloat("sp_amount"));
					model.setCpDataRows(rs.getInt("cp_data_rows"));
					model.setCpAmount(rs.getFloat("cp_amount"));
					model.setSaveLocate(rs.getInt("save_locate"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					model.setTroneId(rs.getInt("trone_id"));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	
	public boolean addSjMrSummerRecord(SjMrSummerRecordModel model)
	{
		String sql = "INSERT INTO " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_sj_ori_data(trone_order_id,YEAR,MONTH,sp_data_rows,sp_amount,cp_data_rows,cp_amount,save_locate)VALUES (" 
				+ model.getTroneOrderId() + "," + model.getYear() + "," + model.getMonth() + "," + model.getSpDataRows() + "," 
				+ model.getSpAmount() + "," + model.getCpDataRows() + "," + model.getCpAmount() + "," + model.getSaveLocate() + ")";
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean delSjMrSummerRecord(int id)
	{
		String sql = "delete from " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_sj_ori_data where id = " + id;
		
		return new JdbcControl().execute(sql);
	}
	
	public SjMrSummerRecordModel getSjMrSummerRecord(int id)
	{
		String sql = " SELECT a.id,e.id sp_id,b.id trone_order_id,c.id trone_id,e.full_name sp_name,f.id cp_id,f.full_name cp_name,d.id sp_trone_id,d.name sp_trone_name,c.price,a.year,a.month,a.sp_data_rows,a.sp_amount,a.cp_data_rows,a.cp_amount,a.save_locate ";
		
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_sj_ori_data a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.trone_order_id = b.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone d ON c.sp_trone_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.sp_id = e.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON b.cp_id = f.id";
		sql += " WHERE a.id = " + id;
		
		return (SjMrSummerRecordModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SjMrSummerRecordModel model = new SjMrSummerRecordModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setCpName(StringUtil.getString(rs.getString("cp_name"),""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setYear(rs.getInt("year"));
					model.setMonth(rs.getInt("month"));
					model.setSpDataRows(rs.getInt("sp_data_rows"));
					model.setSpAmount(rs.getFloat("sp_amount"));
					model.setCpDataRows(rs.getInt("cp_data_rows"));
					model.setCpAmount(rs.getFloat("cp_amount"));
					model.setSaveLocate(rs.getInt("save_locate"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					model.setTroneId(rs.getInt("trone_id"));
					
					return model;
				}
				
				return null;
			}
		});
	}
	
	/**
	 * 是否有记录存在
	 * @param year
	 * @param month
	 * @param troneOrderId
	 * @return
	 */
	public boolean isExistDataInRecord(int year,int month,int troneOrderId)
	{
		String sql = "SELECT COUNT(*) FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_sj_ori_data WHERE trone_order_id = " + troneOrderId + " AND YEAR = " + year + " AND MONTH = " + month;
		return (Boolean)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					if(rs.getInt(1)>0)
						return true;
				
				return false;
			}
		});
	}
	
	/**
	 * 在SUMMER表中是否存在着数据
	 * @param year
	 * @param month
	 * @param troneOrderId
	 * @return
	 */
	public boolean isExistDataInSummer(int year,int month,int troneOrderId)
	{
		String sql1 = "SELECT COUNT(*) FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_mr_summer WHERE trone_order_id = " + troneOrderId + " AND mr_date LIKE '" + year + "-" + String.format("%02d", month) + "%';";
		String sql2 = "SELECT COUNT(*) FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_mr_summer_2 WHERE trone_order_id = " + troneOrderId + " AND mr_date LIKE '" + year + "-" + String.format("%02d", month) + "%';";
		boolean isExistInSummer = (Boolean)new JdbcControl().query(sql1, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					if(rs.getInt(1)>0)
						return true;
				
				return false;
			}
		});
		
		if(isExistInSummer)
			return true;
		
		boolean isExistInSummer2 = (Boolean)new JdbcControl().query(sql2, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					if(rs.getInt(1)>0)
						return true;
				
				return false;
			}
		});
		
		if(isExistInSummer2)
			return true;
		
		return false;
	}
	
	public boolean delMrSummerData(String startDate,String endDate,int troneOrderId)
	{
		String sql1 = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_mr_summer WHERE trone_order_id = " + troneOrderId + " AND mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "'";
		String sql2 = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_cp_mr_summer WHERE trone_order_id = " + troneOrderId + " AND mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "'";
		
		JdbcControl control = new JdbcControl();
		control.execute(sql1);
		control.execute(sql2);
		
		return true;
	}
	
	public boolean delSjMrSummerData(String startDate,String endDate,int troneOrderId)
	{
		String sql1 = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_mr_summer_2 WHERE trone_order_id = " + troneOrderId + " AND mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "'";
		String sql2 = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_cp_mr_summer_2 WHERE trone_order_id = " + troneOrderId + " AND mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "'";
		
		JdbcControl control = new JdbcControl();
		control.execute(sql1);
		control.execute(sql2);
		
		return true;
	}
	
}
