package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.HtDFMonthModel;
import com.system.model.HtDFMrModel;
import com.system.util.StringUtil;

public class HtDfMrDao 
{
	@SuppressWarnings("unchecked")
	public List<HtDFMrModel> loadHtDFMrData(final String tableName,String date,int startHour)
	{
		int endHour = startHour + 1;
		
		String sql = "SELECT a.id,a.`imei`,a.`imsi`,a.`mobile`,a.trone_id,b.`trone_name`,d.`id` sp_trone_id,d.operator,a.create_date,";
		
		sql += " d.`trone_type` sp_trone_type,d.`name` sp_trone_name,a.`ori_order`,a.`ori_trone`,";
		sql += " a.`cp_param`,c.`id` trone_order_id,g.id province_id,g.`name` province_name,h.`id` city_id,h.`name` city_name,";
		sql += " a.`ivr_time`,a.`syn_flag`,b.`price`,a.`linkid`,a.ivr_time,";
		sql += " f.`short_name` cp_name,e.`short_name` sp_name,f.`id` cp_id,e.id sp_id,i.name_cn";
		sql += " FROM daily_log.tbl_mr_"+ tableName +" a";
		sql += " LEFT JOIN daily_config.`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN daily_config.`tbl_trone_order` c ON a.`trone_order_id` = c.`id`";
		sql += " LEFT JOIN daily_config.`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN daily_config.`tbl_sp` e ON d.`sp_id` = e.`id`";
		sql += " LEFT JOIN daily_config.`tbl_cp` f ON c.`cp_id` = f.`id`";
		sql += " LEFT JOIN daily_config.`tbl_province` g ON a.`province_id` = g.`id`";
		sql += " LEFT JOIN daily_config.`tbl_city` h ON a.`city_id` = h.`id`";
		sql += " LEFT JOIN daily_config.tbl_operator i on d.operator = i.id" ;
		sql += " WHERE a.create_date >= '"+ date +" "+ (startHour<10 ? "0" + startHour : "" + startHour) +":00:00' AND a.create_date < '"+ date +" "+ (endHour<10 ? "0" + endHour : "" + endHour) +":00:00'";
		
		return (List<HtDFMrModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<HtDFMrModel> list = new ArrayList<HtDFMrModel>();
				
				while(rs.next())
				{
					HtDFMrModel model = new HtDFMrModel();
					
					model.setIMEI(StringUtil.getString(rs.getString("imei"), ""));
					model.setIMSI(StringUtil.getString(rs.getString("imsi"), ""));
					model.setPHONE(StringUtil.getString(rs.getString("mobile"), ""));
					model.setSPID(rs.getInt("sp_id"));
					model.setSP_NAME(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSP_TRONE_ID(rs.getInt("sp_trone_id"));
					model.setSP_TORNE_NAME(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSP_TRONE_TYPE(rs.getInt("sp_trone_type"));
					model.setTRONG_ID(rs.getInt("trone_id"));
					model.setTRORN_NAME(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setORDER(StringUtil.getString(rs.getString("ori_order"), ""));
					model.setTRONE(StringUtil.getString(rs.getString("ori_trone"), ""));
					model.setCPID(rs.getInt("cp_id"));
					model.setCP_NAME(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setPRICE((int)(rs.getFloat("price")*100));
					model.setCREATE_DATE(StringUtil.getString(rs.getString("create_date"), ""));
					model.setLINKID("tbl_mr_" + tableName + "_" + rs.getInt("id"));
					model.setCP_PARAM(StringUtil.getString(rs.getString("cp_param"), ""));
					model.setTRONE_ORDER_ID(rs.getInt("trone_order_id"));
					model.setPROVINCE_ID(rs.getInt("province_id"));
					model.setPROVINCE_NAME(StringUtil.getString(rs.getString("province_name"), ""));
					model.setCITY_ID(rs.getInt("city_id"));
					model.setCITY_NAME(StringUtil.getString(rs.getString("city_name"), ""));
					model.setIVR_TIME(rs.getInt("ivr_time"));
					model.setSYN_FLAG(rs.getInt("syn_flag"));
					model.setOPERATOR(rs.getInt("operator"));
					model.setOPERATOR_NAME(StringUtil.getString(rs.getString("name_cn"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<HtDFMonthModel> loadHtDFMonthSpData(String startDate,String endDate)
	{
		String sql = "SELECT DATE_FORMAT(a.`mr_date`,'%Y%m01') date_month,d.id sp_id,";
		sql += " d.`short_name` sp_name,c.`operator`,f.id cp_id,f.`short_name` cp_name,";
		sql += " c.`name` sp_trone_name,b.`price`,SUM(a.`data_rows`) total_data_rows";
		sql += " FROM daily_log.`tbl_mr_summer` a";
		sql += " LEFT JOIN daily_config.`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN daily_config.`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN daily_config.tbl_sp d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN daily_config.`tbl_trone_order` e ON a.`trone_order_id` = e.`id`";
		sql += " LEFT JOIN daily_config.tbl_cp f ON e.`cp_id` = f.`id`";
		sql += " WHERE record_type = 1 and a.mr_date >= '"+ startDate +"' and a.mr_date <= '"+ endDate +"'";
		sql += " GROUP BY date_month,c.id,b.`price`";
		
		return (List<HtDFMonthModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<HtDFMonthModel> list = new ArrayList<HtDFMonthModel>();
				
				while(rs.next())
				{
					HtDFMonthModel model = new HtDFMonthModel();
					
					model.setAmount(rs.getInt("price")*100);
					model.setChannelName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOprator(rs.getInt("operator"));
					model.setProductName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setStaticDate(StringUtil.getString(rs.getString("date_month"), ""));
					model.setUserCount(rs.getInt("total_data_rows"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<HtDFMonthModel> loadHtDFMonthCpData(String startDate,String endDate)
	{
		String sql = " SELECT DATE_FORMAT(a.`mr_date`,'%Y%m01') date_month,e.id sp_id,";
		sql += " e.`short_name` sp_name,f.id cp_id,d.`operator`,f.`short_name` cp_name,";
		sql += " d.`name` sp_trone_name,c.`price`,SUM(a.`data_rows`) total_data_rows";
		sql += " FROM daily_log.tbl_cp_mr_summer a";
		sql += " LEFT JOIN daily_config.`tbl_trone_order` b ON a.`trone_order_id` = b.`id`";
		sql += " LEFT JOIN daily_config.`tbl_trone` c ON b.`trone_id` = c.`id`";
		sql += " LEFT JOIN daily_config.`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN daily_config.tbl_sp e ON d.`sp_id` = e.`id`";
		sql += " LEFT JOIN daily_config.tbl_cp f ON b.`cp_id` = f.`id`";
		sql += " WHERE record_type = 1 and a.mr_date >= '"+ startDate +"' and a.mr_date <= '"+ endDate +"'";
		sql += " GROUP BY date_month,d.id,c.`price`";
		
		return (List<HtDFMonthModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<HtDFMonthModel> list = new ArrayList<HtDFMonthModel>();
				
				while(rs.next())
				{
					HtDFMonthModel model = new HtDFMonthModel();
					
					model.setAmount(rs.getInt("price")*100);
					model.setChannelName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOprator(rs.getInt("operator"));
					model.setProductName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setStaticDate(StringUtil.getString(rs.getString("date_month"), ""));
					model.setUserCount(rs.getInt("total_data_rows"));
					list.add(model);
				}
				return list;
			}
		});
	}
}
