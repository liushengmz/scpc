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
import com.system.model.SpTroneModel;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;

public class SpTroneDao
{
	public Map<String, Object> loadSpTroneList(int pageIndex,int spId,int userId,String spTroneName)
	{
		String query = " a.*,b.commerce_user_id,b.short_name,c.`name_cn`,d.id trone_api_id,d.name trone_api_name,e.nick_name commerce_name ";
		
		String sql = "SELECT " + Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` c ON a.operator = c.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api d on a.trone_api_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user e on b.commerce_user_id = e.id";
		sql += " where 1=1 ";
		
		if(spId>0)
		{
			sql +=  " and b.id =" + spId;
		}
		
		if(userId>0)
		{
			sql  += " and e.id = " + userId;
		}
		
		if(!StringUtil.isNullOrEmpty(spTroneName))
		{
			sql += " and (a.name like '%" + spTroneName + "%' or e.name like '%" + spTroneName + "%' )";
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		//String orders = " order by  convert(b.short_name using gbk),convert(a.name using gbk) asc ";
		
		String orders = " order by  a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("rows",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + orders + limit, new QueryCallBack()
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
					model.setTroneApiId(rs.getInt("trone_api_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("commerce_name"),""));
					model.setTroneApiName(StringUtil.getString(rs.getString("trone_api_name"), ""));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setJsTypes(rs.getInt("js_type"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnHoldData(rs.getInt("is_unhold_data"));
					model.setIsForceHold(rs.getInt("is_force_hold"));
					model.setIsWatchData(rs.getInt("is_watch_data"));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	public Map<String, Object> loadOriSpTroneList(int pageIndex,String keyWord,int isDaoLiang)
	{
		String query = " a.*,b.short_name,b.commerce_user_id,h.`name_cn`,d.id trone_api_id,d.name trone_api_name,e.nick_name commerce_name,"
				+ "CONCAT(h.`name_cn`,'-',g.`name`,'-',f.`name`) service_name ";
		
		String sql = "SELECT " + Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api d on a.trone_api_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user e on b.commerce_user_id = e.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 f on a.product_id = f.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 g on f.product_1_id = g.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator h on g.operator_id = h.id";
		sql += " where 1=1 ";
		
		
		String[] troneTypes = {"实时","隔天","IVR","第三方支付"};
		
		int queryTroneType = -1;
		
		for(int i=0; i<troneTypes.length; i++)
		{
			if(troneTypes[i].equalsIgnoreCase(keyWord.trim()))
			{
				queryTroneType = i;
			}
		}
		
		if(isDaoLiang>=0)
		{
			sql += " and a.is_unhold_data = " + isDaoLiang;
		}
		else if(queryTroneType>=0)
		{
			 sql += " and a.trone_type = " + queryTroneType;
		}
		else
		{
			if(!StringUtil.isNullOrEmpty(keyWord))
			{
				sql += " and (a.name like '%" + keyWord + "%' or e.nick_name like '%" + keyWord + "%' or b.short_name like '%" 
						+ keyWord + "%' or b.full_name like '%" + keyWord + "%' or h.name_cn like '%" 
						+ keyWord + "%' or h.name_en like '%" + keyWord + "%' or d.name like '%" + keyWord + "%' or e.name like '%" + keyWord + "%'"
								+ " or CONCAT(h.`name_cn`,'-',g.`name`,'-',f.`name`) like '%" + keyWord + "%' )";
			}
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		//String orders = " order by  convert(b.short_name using gbk),convert(a.name using gbk) asc ";
		
		String orders = " order by  a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("rows",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + orders + limit, new QueryCallBack()
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
					model.setTroneApiId(rs.getInt("trone_api_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("commerce_name"),""));
					model.setTroneApiName(StringUtil.getString(rs.getString("trone_api_name"), ""));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setJsTypes(rs.getInt("js_type"));
					model.setServoceCodeName(StringUtil.getString(rs.getString("service_name"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnHoldData(rs.getInt("is_unhold_data"));
					model.setIsForceHold(rs.getInt("is_force_hold"));
					model.setIsWatchData(rs.getInt("is_watch_data"));
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	public Map<String, Object> loadOriSpTroneList(int userId,int pageIndex,String keyWord,int isDaoLiang)
	{
		String query = " a.*,b.short_name,b.commerce_user_id,h.`name_cn`,d.id trone_api_id,d.name trone_api_name,e.nick_name commerce_name,"
				+ "CONCAT(h.`name_cn`,'-',g.`name`,'-',f.`name`) service_name ";
		
		String sql = "SELECT " + Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api d on a.trone_api_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user e on b.commerce_user_id = e.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 f on a.product_id = f.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 g on f.product_1_id = g.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator h on g.operator_id = h.id";
		sql += " where 1=1 and b.commerce_user_id = " + userId;
		
		
		String[] troneTypes = {"实时","隔天","IVR","第三方支付"};
		
		int queryTroneType = -1;
		
		for(int i=0; i<troneTypes.length; i++)
		{
			if(troneTypes[i].equalsIgnoreCase(keyWord.trim()))
			{
				queryTroneType = i;
			}
		}
		
		if(isDaoLiang>=0)
		{
			sql += " and a.is_unhold_data = " + isDaoLiang;
		}
		else if(queryTroneType>=0)
		{
			 sql += " and a.trone_type = " + queryTroneType;
		}
		else
		{
			if(!StringUtil.isNullOrEmpty(keyWord))
			{
				sql += " and (a.name like '%" + keyWord + "%' or e.nick_name like '%" + keyWord + "%' or b.short_name like '%" 
						+ keyWord + "%' or b.full_name like '%" + keyWord + "%' or h.name_cn like '%" 
						+ keyWord + "%' or h.name_en like '%" + keyWord + "%' or d.name like '%" + keyWord + "%' or e.name like '%" + keyWord + "%'"
								+ " or CONCAT(h.`name_cn`,'-',g.`name`,'-',f.`name`) like '%" + keyWord + "%' )";
			}
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		//String orders = " order by  convert(b.short_name using gbk),convert(a.name using gbk) asc ";
		
		String orders = " order by  a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("rows",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + orders + limit, new QueryCallBack()
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
					model.setTroneApiId(rs.getInt("trone_api_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("commerce_name"),""));
					model.setTroneApiName(StringUtil.getString(rs.getString("trone_api_name"), ""));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setJsTypes(rs.getInt("js_type"));
					model.setServoceCodeName(StringUtil.getString(rs.getString("service_name"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnHoldData(rs.getInt("is_unhold_data"));
					model.setIsForceHold(rs.getInt("is_force_hold"));
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<SpTroneModel> loadSpTroneList()
	{
		String sql = "SELECT  a.*,b.commerce_user_id,b.short_name,c.`name_cn`,d.id trone_api_id,d.name trone_api_name   ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` c ON a.operator = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api d on a.trone_api_id = d.id";
		
		
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
					model.setTroneApiId(rs.getInt("trone_api_id"));
					model.setTroneApiName(StringUtil.getString(rs.getString("trone_api_name"), ""));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setJsTypes(rs.getInt("js_type"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnHoldData(rs.getInt("is_unhold_data"));
					model.setIsForceHold(rs.getInt("is_force_hold"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SpTroneModel> loadSpTroneList(int spId)
	{
		String sql = "SELECT  a.*,b.commerce_user_id,b.short_name,c.`name_cn`,d.id trone_api_id,d.name trone_api_name   ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` c ON a.operator = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api d on a.trone_api_id = d.id";
		sql += " where b.id =" + spId;
		
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
					model.setTroneApiId(rs.getInt("trone_api_id"));
					model.setTroneApiName(StringUtil.getString(rs.getString("trone_api_name"), ""));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setJsTypes(rs.getInt("js_type"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnHoldData(rs.getInt("is_unhold_data"));
					model.setIsForceHold(rs.getInt("is_force_hold"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SpTroneModel> loadCpTroneList(int userId)
	{
		String sql = " SELECT c.id,c.`name`,c.`trone_type`,c.operator ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp e ON a.`cp_id` = e.`id`";
		sql += " WHERE e.`user_id` = " + userId;
		sql += " GROUP BY c.id order by convert(c.name using UTF8) asc";
		
		
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
					//model.setSpId(rs.getInt("sp_id"));
					//model.setSpName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setOperator(rs.getInt("operator"));
					//model.setJieSuanLv(rs.getFloat("jiesuanlv"));
					//model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setTroneType(rs.getInt("trone_type"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SpTroneModel> loadTroneListByCpid(int cpId)
	{
		String sql = " SELECT c.id,c.`name`,c.`trone_type`,c.operator,c.provinces ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp e ON a.`cp_id` = e.`id`";
		sql += " WHERE e.`id` = " + cpId + " and c.trone_api_id > 0";
		sql += " GROUP BY c.id;";
		
		
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
					//model.setSpId(rs.getInt("sp_id"));
					//model.setSpName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setOperator(rs.getInt("operator"));
					//model.setJieSuanLv(rs.getFloat("jiesuanlv"));
					//model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setTroneType(rs.getInt("trone_type"));
					model.setProvinces(StringUtil.getString(rs.getString("provinces"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public SpTroneModel loadSpTroneById(int id)
	{
		String sql = "SELECT  a.*,b.commerce_user_id,b.short_name,a.is_watch_data,c.`name_cn`,d.id trone_api_id,d.name trone_api_name,td.name as up_data_type_name  ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` c ON a.operator = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api d on a.trone_api_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_up_data_type td on a.up_data_type=td.id";
		sql += " WHERE a.id = " + id;
		
		return (SpTroneModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SpTroneModel model = new SpTroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setOperator(rs.getInt("operator"));
					model.setJieSuanLv(rs.getFloat("jiesuanlv"));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setProvinces(StringUtil.getString(rs.getString("provinces"), ""));
					model.setTroneType(rs.getInt("trone_type"));
					model.setStatus(rs.getInt("status"));
					model.setTroneApiId(rs.getInt("trone_api_id"));
					model.setTroneApiName(StringUtil.getString(rs.getString("trone_api_name"), ""));
					model.setServiceCodeId(rs.getInt("product_id"));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setJsTypes(rs.getInt("js_type"));
					//增加4个字段
					model.setApiStatus(rs.getInt("is_on_api"));
					model.setShieldStart(StringUtil.getString(rs.getString("shield_start_hour"),"00:00"));
					model.setShieldEnd(StringUtil.getString(rs.getString("shield_end_hour"),"00:00"));
					model.setRemark(StringUtil.getString(rs.getString("ramark"), ""));
					
					model.setLimiteType(rs.getInt("limit_type"));
					model.setUpDataType(rs.getInt("up_data_type"));
					model.setUpDataName(StringUtil.getString(rs.getString("up_data_type_name"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					
					model.setIsUnHoldData(rs.getInt("is_unhold_data"));
					model.setIsForceHold(rs.getInt("is_force_hold"));
					
					model.setIsWatchData(rs.getInt("is_watch_data"));
					model.setAlarmStartHour(rs.getInt("alarm_start_hour"));
					model.setAlarmEndHour(rs.getInt("alarm_end_hour"));
					
					return model;
				}
				
				return null;
			}
		});
	}
	
	public boolean addSpTrone(SpTroneModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone(sp_id,name,operator,jiesuanlv,provinces,create_date,trone_type,trone_api_id,status,"
				+ "day_limit,month_limit,user_day_limit,user_month_limit,product_id,js_type,is_on_api,shield_start_hour,shield_end_hour,"
				+ "ramark,up_data_type,limit_type,is_unhold_data,is_force_hold,is_watch_data,alarm_start_hour,alarm_end_hour) values("
				+ model.getSpId() + ",'" + model.getSpTroneName() + "',"
				+ model.getOperator() + "," + model.getJieSuanLv() + ",'"
				+ model.getProvinces() + "',now()," + model.getTroneType() + ","+ model.getTroneApiId() +","+ model.getStatus() +"," + model.getDayLimit() + "," 
				+ model.getMonthLimit() + "," + model.getUserDayLimit()  + "," +  model.getUserMonthLimit() + "," + model.getServiceCodeId() + "," + model.getJsTypes() 
				+ ","+model.getApiStatus()+",'"+model.getShieldStart()+"','"+model.getShieldEnd()+"','"+SqlUtil.sqlEncode(model.getRemark())+"',"+model.getUpDataType()
				+","+model.getLimiteType()+"," + model.getIsUnHoldData() + ","+ model.getIsForceHold() +"," + model.getIsWatchData() + ","+ model.getAlarmStartHour() +"," + model.getAlarmEndHour() + ")";
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateSpTroneModel(SpTroneModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone set sp_id = "
				+ model.getSpId() + " ,name = '" + model.getSpTroneName()
				+ "',operator = " + model.getOperator() + ", jiesuanlv = "
				+ model.getJieSuanLv() + ",provinces = '" + model.getProvinces()
				+ "',trone_type = " + model.getTroneType() + ",trone_api_id = " 
				+ model.getTroneApiId() + ",status = " + model.getStatus() + ",day_limit=" + model.getDayLimit() + ",month_limit=" + model.getMonthLimit() + ",user_day_limit=" 
				+ model.getUserDayLimit() + ",user_month_limit=" + model.getUserMonthLimit() + ", product_id = " + model.getServiceCodeId() + ",js_type = " + model.getJsTypes() + ",is_on_api="+model.getApiStatus()+""
				+ ",shield_start_hour='"+model.getShieldStart()+"',shield_end_hour='"+model.getShieldEnd()+"',ramark='"+SqlUtil.sqlEncode(model.getRemark())
				+"',up_data_type="+model.getUpDataType()+",limit_type="+model.getLimiteType()+",is_unhold_data = "+ model.getIsUnHoldData() +", is_force_hold =" + model.getIsForceHold() + ",is_watch_data = " + model.getIsWatchData() + ",alarm_start_hour=" + model.getAlarmStartHour() + ",alarm_end_hour=" + model.getAlarmEndHour() + " where id =" + model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateSpTroneRate(int id,float rate)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone set jiesuanlv = " + rate + " where id = " + id;
		
		return new JdbcControl().execute(sql);				
	}
	
	public boolean delSpTrone(int id)
	{
		String sql = "delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone where id =" + id;
		return new JdbcControl().execute(sql);
	}
	public Integer checkAdd(int userId,int commerceId){
		Map<String, Object> map=new HashMap<String, Object>();
		String sql="select count(*) FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_group_user` a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b ON a.`user_id` = b.`id` WHERE a.`group_id` ="+commerceId+" and b.id="+userId ;
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		int count=(Integer) map.get("rows");
		return count;
	}
	public Map<String, Object> loadSpTroneList(int pageIndex,String keyWord,int userId)
	{
		String query = " a.*,b.short_name,b.commerce_user_id,h.`name_cn`,d.id trone_api_id,d.name trone_api_name,e.nick_name commerce_name,"
				+ "CONCAT(h.`name_cn`,'-',g.`name`,'-',f.`name`) service_name ";
		
		String sql = "SELECT " + Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api d on a.trone_api_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user e on b.commerce_user_id = e.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 f on a.product_id = f.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 g on f.product_1_id = g.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator h on g.operator_id = h.id";
		sql += " where 1=1 ";
		
		
		String[] troneTypes = {"实时","隔天","IVR","第三方支付"};
		
		int queryTroneType = -1;
		
		for(int i=0; i<troneTypes.length; i++)
		{
			if(troneTypes[i].equalsIgnoreCase(keyWord.trim()))
			{
				queryTroneType = i;
			}
		}
		
		if(queryTroneType>=0)
		{
			 sql += " and a.trone_type = " + queryTroneType;
		}
		else
		{
			if(!StringUtil.isNullOrEmpty(keyWord))
			{
				sql += " and (a.name like '%" + keyWord + "%' or e.nick_name like '%" + keyWord + "%' or b.short_name like '%" 
						+ keyWord + "%' or b.full_name like '%" + keyWord + "%' or h.name_cn like '%" 
						+ keyWord + "%' or h.name_en like '%" + keyWord + "%' or d.name like '%" + keyWord + "%' or e.name like '%" + keyWord + "%'"
								+ " or CONCAT(h.`name_cn`,'-',g.`name`,'-',f.`name`) like '%" + keyWord + "%' )";
			}else{
				sql+=" and b.commerce_user_id="+userId;
			}
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		//String orders = " order by  convert(b.short_name using gbk),convert(a.name using gbk) asc ";
		
		String orders = " order by  a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("rows",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + orders + limit, new QueryCallBack()
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
					model.setTroneApiId(rs.getInt("trone_api_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("commerce_name"),""));
					model.setTroneApiName(StringUtil.getString(rs.getString("trone_api_name"), ""));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setJsTypes(rs.getInt("js_type"));
					model.setServoceCodeName(StringUtil.getString(rs.getString("service_name"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					
					model.setIsUnHoldData(rs.getInt("is_unhold_data"));
					
					model.setIsForceHold(rs.getInt("is_force_hold"));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	/**
	 * 根据商务的权限分配获取SP业务
	 * @param pageIndex
	 * @param keyWord
	 * @param userRight
	 * @return
	 */
	public Map<String, Object> loadSpTroneList(int pageIndex,String keyWord,String userRight)
	{
		String query = " a.*,b.short_name,b.commerce_user_id,h.`name_cn`,d.id trone_api_id,d.name trone_api_name,e.nick_name commerce_name,"
				+ "CONCAT(h.`name_cn`,'-',g.`name`,'-',f.`name`) service_name ";
		
		String sql = "SELECT " + Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone_api d on a.trone_api_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user e on b.commerce_user_id = e.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 f on a.product_id = f.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 g on f.product_1_id = g.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator h on g.operator_id = h.id";
		sql += " where 1=1 ";
		
		
		String[] troneTypes = {"实时","隔天","IVR","第三方支付"};
		
		int queryTroneType = -1;
		
		for(int i=0; i<troneTypes.length; i++)
		{
			if(troneTypes[i].equalsIgnoreCase(keyWord.trim()))
			{
				queryTroneType = i;
			}
		}
		
		sql+= " and b.commerce_user_id in (" + userRight + ")";
		
		if(queryTroneType>=0)
		{
			 sql += " and a.trone_type = " + queryTroneType;
		}
		else
		{
			if(!StringUtil.isNullOrEmpty(keyWord))
			{
				sql += " and (a.name like '%" + keyWord + "%' or e.nick_name like '%" + keyWord + "%' or b.short_name like '%" 
						+ keyWord + "%' or b.full_name like '%" + keyWord + "%' or h.name_cn like '%" 
						+ keyWord + "%' or h.name_en like '%" + keyWord + "%' or d.name like '%" + keyWord + "%' or e.name like '%" + keyWord + "%'"
								+ " or CONCAT(h.`name_cn`,'-',g.`name`,'-',f.`name`) like '%" + keyWord + "%' )";
			}
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		//String orders = " order by  convert(b.short_name using gbk),convert(a.name using gbk) asc ";
		
		String orders = " order by  a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("rows",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list",new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + orders + limit, new QueryCallBack()
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
					model.setTroneApiId(rs.getInt("trone_api_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("commerce_name"),""));
					model.setTroneApiName(StringUtil.getString(rs.getString("trone_api_name"), ""));
					model.setDayLimit(rs.getFloat("day_limit"));
					model.setMonthLimit(rs.getFloat("month_limit"));
					model.setUserDayLimit(rs.getFloat("user_day_limit"));
					model.setUserMonthLimit(rs.getFloat("user_month_limit"));
					model.setJsTypes(rs.getInt("js_type"));
					model.setServoceCodeName(StringUtil.getString(rs.getString("service_name"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					
					model.setIsUnHoldData(rs.getInt("is_unhold_data"));
					
					model.setIsForceHold(rs.getInt("is_force_hold"));
					model.setProvinces(StringUtil.getString(rs.getString("provinces"), ""));
					model.setIsWatchData(rs.getInt("is_watch_data"));
					
					list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	public void updateSpTroneProvince(int id,String pros)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone SET provinces = '" + pros + "' WHERE id = " + id;
		new JdbcControl().execute(sql);
	}
	
	public void updateSpTroneStatus(int id,int status)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone SET status = '" + status + "' WHERE id = " + id;
		new JdbcControl().execute(sql);
	}
	
	public void updateSpTroneAlarm(int id,int isWatchData)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone SET is_watch_data = '" + isWatchData + "' WHERE id = " + id;
		new JdbcControl().execute(sql);
	}
	
}
