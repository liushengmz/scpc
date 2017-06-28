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
import com.system.model.SpModel;
import com.system.util.StringUtil;

public class SpDao
{
	@SuppressWarnings("unchecked")
	public List<SpModel> loadSp()
	{
		//增加状态字段过滤
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp where status=1 order by convert(short_name using gbk) asc";
		return (List<SpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setId(rs.getInt("id"));
					model.setUserId(rs.getInt("user_id"));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					model.setStatus(rs.getInt("status"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SpModel> loadSpQiBa()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp order by id asc";
		return (List<SpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setId(rs.getInt("id"));
					model.setUserId(rs.getInt("user_id"));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public Map<String, Object> loadSp(int pageIndex)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp order by convert(short_name using gbk) asc";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
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
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setId(rs.getInt("id"));
					
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setQq(StringUtil.getString(rs.getString("qq"), ""));
					model.setPhone(StringUtil.getString(rs.getString("phone"), ""));
					model.setMail(StringUtil.getString(rs.getString("mail"), ""));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public Map<String, Object> loadSp(int pageIndex,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.commerce_user_id = b.id where 1=1 ";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (full_name LIKE '%" + keyWord + "%' or short_name LIKE '%"+ keyWord +"%' or b.nick_name like '%" + keyWord + "%' or a.id = '" + keyWord + "' )";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by a.id desc ";
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
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setId(rs.getInt("id"));
					
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setQq(StringUtil.getString(rs.getString("qq"), ""));
					model.setPhone(StringUtil.getString(rs.getString("phone"), ""));
					model.setMail(StringUtil.getString(rs.getString("mail"), ""));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("nick_name"), ""));
					//添加状态
					model.setStatus(rs.getInt("status"));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public SpModel loadSpById(int id)
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp where id = " + id;
		return (SpModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SpModel model = new SpModel();
					model.setId(rs.getInt("id"));					
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setQq(StringUtil.getString(rs.getString("qq"), ""));
					model.setPhone(StringUtil.getString(rs.getString("phone"), ""));
					model.setMail(StringUtil.getString(rs.getString("mail"), ""));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					//添加状态
					model.setStatus(rs.getInt("status"));
					return model;
				}
				
				return null;
			}
		});
	}
	
	public boolean addSp(SpModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp(full_name,short_name,contract_person,qq,mail,phone,address,contract_start_date,contract_end_date,commerce_user_id,status) "
				+ "value('" + model.getFullName() + "','" + model.getShortName()
				+ "','" + model.getContactPerson() + "','" + model.getQq()
				+ "','" + model.getMail() + "','" + model.getPhone() + "','"
				+ model.getAddress() + "','" + model.getContractStartDate()
				+ "','" + model.getContractEndDate() + "',"+ model.getCommerceUserId() +","+model.getStatus()+")";
		return new JdbcControl().execute(sql);
	}

	public boolean updateSp(SpModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp set full_name = '"
				+ model.getFullName() + "',short_name = '"
				+ model.getShortName() + "',contract_person='"
				+ model.getContactPerson() + "',qq='" + model.getQq()
				+ "',mail='" + model.getMail() + "',phone='" + model.getPhone()
				+ "',address='" + model.getAddress() + "',contract_start_date='"
				+ model.getContractStartDate() + "',contract_end_date='"
				+ model.getContractEndDate() + "',commerce_user_id=" + model.getCommerceUserId() +",status="+model.getStatus()+" where id =" + model.getId();
		return new JdbcControl().execute(sql);
	}
	
	public Map<String, Object> loadSp(int pageIndex,String keyWord,int userId)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.commerce_user_id = b.id where 1=1 ";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (full_name LIKE '%" + keyWord + "%' or short_name LIKE '%"+ keyWord +"%' or b.nick_name like '%" + keyWord + "%' or a.id = '" + keyWord + "' )";
		}else{
			sql +=" AND a.commerce_user_id="+userId;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by a.id desc ";
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
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setId(rs.getInt("id"));
					
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setQq(StringUtil.getString(rs.getString("qq"), ""));
					model.setPhone(StringUtil.getString(rs.getString("phone"), ""));
					model.setMail(StringUtil.getString(rs.getString("mail"), ""));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("nick_name"), ""));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
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
	/**
	 * 增加SP状态查询
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadSp(int pageIndex,int status,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.commerce_user_id = b.id where 1=1 ";
		if(status>=0){
			sql+=" and a.status="+status;
		}
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (full_name LIKE '%" + keyWord + "%' or short_name LIKE '%"+ keyWord +"%' or b.nick_name like '%" + keyWord + "%' or a.id = '" + keyWord + "' )";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by a.id desc ";
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
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setId(rs.getInt("id"));
					
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setQq(StringUtil.getString(rs.getString("qq"), ""));
					model.setPhone(StringUtil.getString(rs.getString("phone"), ""));
					model.setMail(StringUtil.getString(rs.getString("mail"), ""));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("nick_name"), ""));
					//添加状态
					model.setStatus(rs.getInt("status"));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public Map<String, Object> loadSp(int userId,int pageIndex,int status,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.commerce_user_id = b.id where 1=1 and a.commerce_user_id = " + userId;
		if(status>=0){
			sql+=" and a.status="+status;
		}
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (full_name LIKE '%" + keyWord + "%' or short_name LIKE '%"+ keyWord +"%' or b.nick_name like '%" + keyWord + "%' or a.id = '" + keyWord + "' )";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by a.id desc ";
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
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpModel> list = new ArrayList<SpModel>();
				while(rs.next())
				{
					SpModel model = new SpModel();
					
					model.setId(rs.getInt("id"));
					
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setContactPerson(StringUtil.getString(rs.getString("contract_person"), ""));
					model.setQq(StringUtil.getString(rs.getString("qq"), ""));
					model.setPhone(StringUtil.getString(rs.getString("phone"), ""));
					model.setMail(StringUtil.getString(rs.getString("mail"), ""));
					model.setAddress(StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil.getString(rs.getString("contract_end_date"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setCommerceUserName(StringUtil.getString(rs.getString("nick_name"), ""));
					//添加状态
					model.setStatus(rs.getInt("status"));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
}
