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
import com.system.model.SingleCpSpTroneRateModel;
import com.system.util.StringUtil;

public class SingleCpSpTroneRateDao
{
	public Map<String,Object> loadSingleCpSpTroneRate(int id,int pageIndex)
	{
		Map<String,Object> map = new HashMap<String, Object>();
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_trone_rate_list` a";
		
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_trone_rate` b ON a.`cp_trone_rate_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` c ON b.`cp_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.`sp_id` = e.`id`";
		sql += " where b.id = " + id;
		sql += " order by a.start_date desc";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		JdbcControl control = new JdbcControl();
		
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,c.`short_name` cp_name ,e.`short_name` sp_name,d.`name` sp_trone_name ") + limit, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SingleCpSpTroneRateModel> list = new ArrayList<SingleCpSpTroneRateModel>();
				
				while(rs.next())
				{
					SingleCpSpTroneRateModel model = new SingleCpSpTroneRateModel();
					
					model.setId(rs.getInt("id"));
					
					model.setStartDate(StringUtil.getString(rs.getString("start_date"), ""));
					model.setEndDate(StringUtil.getString(rs.getString("end_date"), ""));
					model.setRate(rs.getFloat("rate"));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					model.setCpSpTroneId(rs.getInt("cp_trone_rate_id"));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public SingleCpSpTroneRateModel loadSinleCpSpTroneRateById(int id)
	{
		String sql = "select a.*,c.`short_name` cp_name ,e.`short_name` sp_name,d.`name` sp_trone_name,c.id cp_id,d.id sp_trone_id,b.rate default_rate FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_trone_rate_list` a";
		
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_trone_rate` b ON a.`cp_trone_rate_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` c ON b.`cp_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.`sp_id` = e.`id`";
		sql += " where a.id = " + id;
		
		return (SingleCpSpTroneRateModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SingleCpSpTroneRateModel model = new SingleCpSpTroneRateModel();
					
					model.setTitle(StringUtil.getString(rs.getString("cp_name"), "") + "-" + StringUtil.getString(rs.getString("sp_name"), "") + "-" + StringUtil.getString(rs.getString("sp_trone_name"), ""));
					
					model.setStartDate(StringUtil.getString(rs.getString("start_date"), ""));
					model.setEndDate(StringUtil.getString(rs.getString("end_date"), ""));
					model.setRate(rs.getFloat("rate"));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setId(rs.getInt("id"));
					model.setDefaultRate(rs.getFloat("default_rate"));
					
					return model;
				}
				return null;
			}
		});
		
	}
	
	public void updateSingleRate(SingleCpSpTroneRateModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate_list set rate = ? , remark = ? where id = ?";
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		
		params.put(1, model.getRate());
		params.put(2, model.getRemark());
		params.put(3, model.getId());
		
		new JdbcControl().execute(sql, params);
	}
	
	public void delSingleRate(int id)
	{
		String sql = "delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate_list where id = " + id;
		new JdbcControl().execute(sql);
	}
	
	public void addSingleRate(SingleCpSpTroneRateModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate_list(cp_trone_rate_id,start_date,end_date,rate,remark) values(?,?,?,?,?)";
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		
		params.put(1, model.getCpSpTroneId());
		params.put(2, model.getStartDate());
		params.put(3, model.getEndDate());
		params.put(4, model.getRate());
		params.put(5, model.getRemark());
		
		new JdbcControl().execute(sql, params);
	}
	
	public boolean isRateDateCross(int cpTroneRateId,String startDate,String endDate)
	{
		String sql = "SELECT count(*) FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate_list	WHERE cp_trone_rate_id = "
				+ cpTroneRateId + " AND ( ('" + startDate + "' >= start_date AND '"
				+ startDate + "' <= end_date) OR('" + endDate
				+ "' >= start_date AND '" + endDate + "' <= end_date) OR('"
				+ startDate + "' <= start_date AND '" + endDate
				+ "' >= end_date) );";
		
		int rows = (Integer)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		});
		
		return rows>0;
	}
	
	public static void main(String[] args)
	{
		int cpTroneRateId = 0;
		String startDate = "startDate";
		String endDate = "endDate";
		
		String sql = "SELECT count(*) FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate_list	WHERE cp_trone_rate_id = "
				+ cpTroneRateId + " AND ( ('" + startDate + "' >= start_date AND '"
				+ startDate + "' <= end_date) OR('" + endDate
				+ "' >= start_date AND '" + endDate + "' <= end_date) OR('"
				+ startDate + "' <= start_date AND '" + endDate
				+ "' >= end_date) );";
		
		System.out.println(sql);
		
	}
	
}
