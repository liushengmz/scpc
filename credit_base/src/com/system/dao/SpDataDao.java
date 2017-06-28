package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SpTroneApiModel;
import com.system.model.SpTroneModel;
import com.system.model.TroneModel;
import com.system.util.StringUtil;

public class SpDataDao
{
	@SuppressWarnings("unchecked")
	public List<SpTroneModel> loadSpTroneList()
	{
		String sql = "SELECT  a.*,b.short_name,c.`name_cn`  ";
		sql += " FROM daily_config.`tbl_sp_trone` a";
		sql += " LEFT JOIN daily_config.`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN daily_config.`tbl_operator` c ON a.operator = c.`id`";
		
		
		return (List<SpTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpTroneModel> list = new ArrayList<SpTroneModel>();
				
				while(rs.next())
				{
					SpTroneModel model = new SpTroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setOperator(rs.getInt("operator"));
					model.setJieSuanLv(rs.getFloat("jiesuanlv"));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setTroneType(rs.getInt("trone_type"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneApiId(rs.getInt("trone_api_id"));
					model.setProvinces(StringUtil.getString(rs.getString("provinces"), ""));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setForceHold(rs.getInt("is_force_hold")==0 ? false:true);
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<TroneModel> loadTrone()
	{
		String sql = "select a.*,b.short_name from daily_config.tbl_trone a left join daily_config.tbl_sp b on a.sp_id = b.id order by b.short_name asc";

		return (List<TroneModel>) new JdbcControl().query(sql,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneModel> list = new ArrayList<TroneModel>();
				while (rs.next())
				{
					TroneModel model = new TroneModel();

					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpApiUrlId(rs.getInt("sp_api_url_id"));
					model.setSpShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setTroneName(StringUtil
							.getString(rs.getString("trone_name"), ""));
					model.setOrders(StringUtil
							.getString(rs.getString("orders"), ""));
					model.setTroneNum(StringUtil
							.getString(rs.getString("trone_num"), ""));
					model.setCurrencyId(rs.getInt("currency_id"));
					model.setPrice(rs.getFloat("price"));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setStatus(rs.getInt("status"));
					model.setMatchPrice(rs.getInt("match_price"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));

					list.add(model);

				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SpTroneApiModel> loadSpTroneApi()
	{
		String sql = "select * from tbl_sp_trone_api";
		return (List<SpTroneApiModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpTroneApiModel> list = new ArrayList<SpTroneApiModel>();
				while(rs.next())
				{
					SpTroneApiModel model = new SpTroneApiModel();
					
					model.setId(rs.getInt("id"));
					model.setMatchField(rs.getInt("match_field"));
					model.setMatchKeyWord(StringUtil.getString(rs.getString("match_keyword"), ""));
					model.setApiFiles(StringUtil.getString(rs.getString("api_fields"), ""));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setLocateMatch(rs.getInt("locate_match"));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	public static void main(String[] args)
	{
		Map<String,Float> map = new HashMap<String, Float>();
		
		Float aa =  map.get("AAA");
		
		System.out.println(aa + 1.14);
	}
}
