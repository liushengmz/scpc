package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CpModel;
import com.system.model.CpTroneModel;
import com.system.model.TroneOrderModel;
import com.system.util.StringUtil;

public class CpDataDao
{
	@SuppressWarnings("unchecked")
	public List<CpModel> loadCp()
	{
		String sql = "select * from daily_config.tbl_cp order by convert(short_name using gbk) asc";
		
		return (List<CpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpModel> list = new ArrayList<CpModel>();
				
				while(rs.next())
				{
					CpModel model = new CpModel();
					
					model.setId(rs.getInt("id"));
					model.setUserId(rs.getInt("user_id"));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setSynFlag(rs.getInt("syn_flag"));
					model.setDefaultHoldPercent(rs.getInt("default_hold_percent"));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}	
	
	@SuppressWarnings("unchecked")
	public List<CpTroneModel> loadCpTrone()
	{
		String sql = "SELECT * FROM daily_config.`tbl_cp_trone_rate`";
		return (List<CpTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpTroneModel> list = new ArrayList<CpTroneModel>();
				
				while(rs.next())
				{
					CpTroneModel model = new CpTroneModel();
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<TroneOrderModel> loadTroneOrderList()
	{
		String sql  = "select a.*,b.short_name,c.sp_id,c.trone_name,c.price,c.sp_trone_id from daily_config.tbl_trone_order a "
				+ " left join daily_config.tbl_cp b on a.cp_id = b.id "
				+ " left join daily_config.tbl_trone c on a.trone_id = c.id "
				+ " LEFT JOIN daily_config.`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id`"
				+ " WHERE a.`disable` = 0 AND d.`status` = 1 "
				+ " order by b.id,c.sp_id asc";
		
		return (List<TroneOrderModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));//0启用 1关闭
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
				
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
