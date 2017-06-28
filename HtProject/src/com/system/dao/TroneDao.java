
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
import com.system.model.TroneModel;
import com.system.server.TroneOrderServer;
import com.system.util.StringUtil;

public class TroneDao
{
	@SuppressWarnings("unchecked")
	public List<TroneModel> loadTrone()
	{
		String sql = "select a.*,b.commerce_user_id,b.short_name from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp b on a.sp_id = b.id order by b.short_name asc";

		return (List<TroneModel>) new JdbcControl().query(sql,
				new QueryCallBack()
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
							model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
							model.setCurrencyId(rs.getInt("currency_id"));
							model.setPrice(rs.getFloat("price"));
							model.setDynamic(rs.getInt("is_dynamic"));
							model.setStatus(rs.getInt("status"));
							model.setMatchPrice(rs.getInt("match_price"));
							model.setCommerceUserId(rs.getInt("commerce_user_id"));
							list.add(model);

						}
						return list;
					}
				});

	}
	
	@SuppressWarnings("unchecked")
	public List<TroneModel> loadSpTrone()
	{
		String sql = "SELECT id,sp_id,name FROM `" + com.system.constant.Constant.DB_DAILY_CONFIG + "`.`tbl_sp_trone` ";
		
		return (List<TroneModel>)new JdbcControl().query(sql, 
				new QueryCallBack() {
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException {
						List<TroneModel> list = new ArrayList<TroneModel>();
						TroneModel model = null;
						while (rs.next()) {
							model = new TroneModel();
							model.setSpTroneId(rs.getInt("id"));
							model.setSpId(rs.getInt("sp_id"));
							model.setSpTroneName(rs.getString("name"));
							list.add(model);
						}
						return list;
					}
				});
	}
	
	@SuppressWarnings("unchecked")
	public List<TroneModel> loadTrone(int spTroneId)
	{
		String sql = "select a.*,b.commerce_user_id,b.short_name from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp b on a.sp_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c on a.sp_trone_id = c.id where a.status=1 and c.id = " + spTroneId + " order by b.short_name asc";

		return (List<TroneModel>) new JdbcControl().query(sql,
				new QueryCallBack()
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
							model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
							model.setCurrencyId(rs.getInt("currency_id"));
							model.setPrice(rs.getFloat("price"));
							model.setDynamic(rs.getInt("is_dynamic"));
							model.setStatus(rs.getInt("status"));
							model.setMatchPrice(rs.getInt("match_price"));
							model.setCommerceUserId(rs.getInt("commerce_user_id"));
							list.add(model);

						}
						return list;
					}
				});

	}
	
	public Map<String, Object> loadTrone(int spId,int pageIndex)
	{
		String query = " a.*,c.commerce_user_id,c.`short_name`,b.`name` sp_trone_name ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp c ON b.`sp_id` = c.`id` where 1=1 ";
		
		String wheres = "";
		
		if(spId>0)
			wheres = " and c.id = " + spId;
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)") + wheres,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + wheres + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneModel> list = new ArrayList<TroneModel>();
				while(rs.next())
				{
					TroneModel model = new TroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpApiUrlId(rs.getInt("sp_api_url_id"));
					model.setSpShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setTroneName(StringUtil
							.getString(rs.getString("trone_name"), ""));
					model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					model.setOrders(StringUtil
							.getString(rs.getString("orders"), ""));
					model.setCurrencyId(rs.getInt("currency_id"));
					model.setPrice(rs.getFloat("price"));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setMatchPrice(rs.getInt("match_price"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public Map<String, Object> loadTrone(int pageIndex,String keyWord)
	{
		String query = " a.*,c.commerce_user_id,c.`short_name`,b.`name` sp_trone_name ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp c ON b.`sp_id` = c.`id` where 1=1 ";
		
		String wheres = "";
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			wheres += " and (c.short_name like '%" + keyWord + "%' or c.full_name like '%" + keyWord 
					+ "%' or b.name like '%" + keyWord + "%' or a.trone_num like '%" + keyWord 
					+ "%' or a.orders like '%" + keyWord + "%' or a.trone_name like '%" + keyWord 
					+ "%')";
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)") + wheres,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + wheres + " order by a.id desc " + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneModel> list = new ArrayList<TroneModel>();
				while(rs.next())
				{
					TroneModel model = new TroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpApiUrlId(rs.getInt("sp_api_url_id"));
					model.setSpShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setTroneName(StringUtil
							.getString(rs.getString("trone_name"), ""));
					model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					model.setOrders(StringUtil
							.getString(rs.getString("orders"), ""));
					model.setCurrencyId(rs.getInt("currency_id"));
					model.setPrice(rs.getFloat("price"));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setMatchPrice(rs.getInt("match_price"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public Map<String, Object> loadTrone(int userId,int pageIndex,String keyWord)
	{
		String query = " a.*,c.commerce_user_id,c.`short_name`,b.`name` sp_trone_name ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp c ON b.`sp_id` = c.`id` where 1=1 and c.commerce_user_id = " + userId;
		
		String wheres = "";
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			wheres += " and (c.short_name like '%" + keyWord + "%' or c.full_name like '%" + keyWord 
					+ "%' or b.name like '%" + keyWord + "%' or a.trone_num like '%" + keyWord 
					+ "%' or a.orders like '%" + keyWord + "%' or a.trone_name like '%" + keyWord 
					+ "%')";
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)") + wheres,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + wheres + " order by a.id desc " + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneModel> list = new ArrayList<TroneModel>();
				while(rs.next())
				{
					TroneModel model = new TroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpApiUrlId(rs.getInt("sp_api_url_id"));
					model.setSpShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setTroneName(StringUtil
							.getString(rs.getString("trone_name"), ""));
					model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					model.setOrders(StringUtil
							.getString(rs.getString("orders"), ""));
					model.setCurrencyId(rs.getInt("currency_id"));
					model.setPrice(rs.getFloat("price"));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setMatchPrice(rs.getInt("match_price"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public TroneModel getTroneById(int id)
	{
		String sql = "select  a.*,b.commerce_user_id,b.`short_name`,c.`name` sp_trone_name ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON a.`sp_trone_id` = c.`id` where 1=1 and a.id = " + id;
		
		return (TroneModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
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
					model.setCurrencyId(rs.getInt("currency_id"));
					model.setPrice(rs.getFloat("price"));
					model.setStatus(rs.getInt("status"));
					model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setMatchPrice(rs.getInt("match_price"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					return model;
				}
				
				return null;
			}
		});
	}
	
	public boolean addTrone(TroneModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone(sp_id,sp_api_url_id,trone_name,orders,trone_num,sp_trone_id,price,is_dynamic,match_price) values(?,?,?,?,?,?,?,?,?)";
		
		Map<Integer,Object> map = new HashMap<Integer, Object>();
		
		map.put(1,model.getSpId());
		map.put(2,model.getSpApiUrlId());
		map.put(3,model.getTroneName());
		map.put(4,model.getOrders());
		map.put(5,model.getTroneNum());
		map.put(6,model.getSpTroneId());
		map.put(7,model.getPrice());
		map.put(8,model.getDynamic());
		map.put(9,model.getMatchPrice());
		
		return new JdbcControl().execute(sql, map);
	}
	
	public int insertTrone(TroneModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone(sp_id,sp_api_url_id,trone_name,orders,trone_num,sp_trone_id,price,is_dynamic,match_price) values(?,?,?,?,?,?,?,?,?)";
		
		Map<Integer,Object> map = new HashMap<Integer, Object>();
		
		map.put(1,model.getSpId());
		map.put(2,model.getSpApiUrlId());
		map.put(3,model.getTroneName());
		map.put(4,model.getOrders());
		map.put(5,model.getTroneNum());
		map.put(6,model.getSpTroneId());
		map.put(7,model.getPrice());
		map.put(8,model.getDynamic());
		map.put(9,model.getMatchPrice());
		
		return new JdbcControl().insertWithGenKey(sql, map);
	}
	
	
	
	public boolean updateTrone(TroneModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone set sp_id = ?,sp_api_url_id = ?,trone_name = ?,orders = ?,trone_num = ?,sp_trone_id = ?,price = ?,is_dynamic = ?,match_price = ?,status = ? where id = ?";
		
		Map<Integer,Object> map = new HashMap<Integer, Object>();
		
		map.put(1,model.getSpId());
		map.put(2,model.getSpApiUrlId());
		map.put(3,model.getTroneName());
		map.put(4,model.getOrders());
		map.put(5,model.getTroneNum());
		map.put(6,model.getSpTroneId());
		map.put(7,model.getPrice());
		map.put(8,model.getDynamic());
		map.put(9,model.getMatchPrice());
		map.put(10, model.getStatus());
		map.put(11, model.getId());
		
		//如果关闭通道，同时关闭通道对应的所有CP业务
		if(model.getStatus()==0)
			new TroneOrderServer().closeTroneOrderByTroneId(model.getId());
		
		return new JdbcControl().execute(sql, map);
	}
	
	public boolean deleteTrone(int delId)
	{
		String sql = "delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone where id = " + delId;
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
	public Map<String, Object> loadTrone(int pageIndex,String keyWord,int userId)
	{
		String query = " a.*,c.commerce_user_id,c.`short_name`,b.`name` sp_trone_name ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp c ON b.`sp_id` = c.`id` where 1=1 ";
		
		String wheres = "";
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			wheres += " and (c.short_name like '%" + keyWord + "%' or c.full_name like '%" + keyWord 
					+ "%' or b.name like '%" + keyWord + "%' or a.trone_num like '%" + keyWord 
					+ "%' or a.orders like '%" + keyWord + "%' or a.trone_name like '%" + keyWord 
					+ "%')";
		}else{
			wheres+=" and c.commerce_user_id="+userId;
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)") + wheres,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + wheres + " order by a.id desc " + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneModel> list = new ArrayList<TroneModel>();
				while(rs.next())
				{
					TroneModel model = new TroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpApiUrlId(rs.getInt("sp_api_url_id"));
					model.setSpShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setTroneName(StringUtil
							.getString(rs.getString("trone_name"), ""));
					model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					model.setOrders(StringUtil
							.getString(rs.getString("orders"), ""));
					model.setCurrencyId(rs.getInt("currency_id"));
					model.setPrice(rs.getFloat("price"));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setMatchPrice(rs.getInt("match_price"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
}
