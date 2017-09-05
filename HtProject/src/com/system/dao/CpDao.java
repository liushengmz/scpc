
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
import com.system.model.CpModel;
import com.system.util.StringUtil;

public class CpDao
{
	@SuppressWarnings("unchecked")
	public List<CpModel> loadCp()
	{
		//增加状态字段过滤
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp where status=1 order by convert(short_name using gbk) asc";

		return (List<CpModel>) new JdbcControl().query(sql, new QueryCallBack()
		{

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpModel> list = new ArrayList<CpModel>();

				while (rs.next())
				{
					CpModel model = new CpModel();

					model.setId(rs.getInt("id"));
					model.setUserId(rs.getInt("user_id"));
					model.setFullName(StringUtil
							.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setContactPerson(StringUtil
							.getString(rs.getString("contract_person"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setSynFlag(rs.getInt("syn_flag"));
					model.setDefaultHoldPercent(
							rs.getInt("default_hold_percent"));
					model.setAddress(
							StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(
							rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil
							.getString(rs.getString("contract_end_date"), ""));
					model.setMail(rs.getString("mail"));
					model.setStatus(rs.getInt("status"));
					list.add(model);
				}

				return list;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<CpModel> loadCpQiBa()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp order by id asc";

		return (List<CpModel>) new JdbcControl().query(sql, new QueryCallBack()
		{

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpModel> list = new ArrayList<CpModel>();

				while (rs.next())
				{
					CpModel model = new CpModel();

					model.setId(rs.getInt("id"));
					model.setUserId(rs.getInt("user_id"));
					model.setFullName(StringUtil
							.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setContactPerson(StringUtil
							.getString(rs.getString("contract_person"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setSynFlag(rs.getInt("syn_flag"));
					model.setDefaultHoldPercent(
							rs.getInt("default_hold_percent"));
					model.setAddress(
							StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(
							rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil
							.getString(rs.getString("contract_end_date"), ""));
					model.setMail(rs.getString("mail"));

					list.add(model);
				}

				return list;
			}
		});
	}

	public Map<String, Object> loadCp(int pageIndex)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING
				+ " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp order by convert(short_name using gbk) asc";

		String limit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + ","
				+ Constant.PAGE_SIZE;

		Map<String, Object> map = new HashMap<String, Object>();

		JdbcControl control = new JdbcControl();
		map.put("rows", control.query(
				sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"),
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						if (rs.next())
							return rs.getInt(1);

						return 0;
					}
				}));

		map.put("list", control.query(
				sql.replace(Constant.CONSTANT_REPLACE_STRING, " * ") + limit,
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<CpModel> list = new ArrayList<CpModel>();
						while (rs.next())
						{
							CpModel model = new CpModel();

							model.setId(rs.getInt("id"));

							model.setShortName(StringUtil
									.getString(rs.getString("short_name"), ""));
							model.setFullName(StringUtil
									.getString(rs.getString("full_name"), ""));
							model.setContactPerson(StringUtil.getString(
									rs.getString("contract_person"), ""));
							model.setQq(StringUtil.getString(rs.getString("qq"),
									""));
							model.setPhone(StringUtil
									.getString(rs.getString("phone"), ""));
							model.setMail(StringUtil
									.getString(rs.getString("mail"), ""));
							model.setAddress(StringUtil
									.getString(rs.getString("address"), ""));
							model.setContractStartDate(StringUtil.getString(
									rs.getString("contract_start_date"), ""));
							model.setContractEndDate(StringUtil.getString(
									rs.getString("contract_end_date"), ""));
							model.setCommerceUserId(rs.getInt("commerce_user_id"));

							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}

	public Map<String, Object> loadCp(int pageIndex, String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING
				+ " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.user_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user c on a.commerce_user_id = c.id  where 1=1";

		String limit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + ","
				+ Constant.PAGE_SIZE;

		if (!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (a.full_name LIKE '%"+keyWord+"%' or a.short_name like '%" 
					+ keyWord + "%' or a.id = '"+ keyWord +"' or c.nick_name like '%" 
					+ keyWord + "%' or b.nick_name like '%" + keyWord + "%' or a.id = '" 
					+ keyWord + "') ";
		}

		Map<String, Object> map = new HashMap<String, Object>();

		sql += " order by convert(a.short_name using gbk) asc ";

		JdbcControl control = new JdbcControl();
		map.put("rows", control.query(
				sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"),
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						if (rs.next())
							return rs.getInt(1);

						return 0;
					}
				}));

		map.put("list",
				control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING,
						" a.*,b.name,b.nick_name,c.nick_name commerce_user_name ")
				+ limit, new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<CpModel> list = new ArrayList<CpModel>();
						while (rs.next())
						{
							CpModel model = new CpModel();

							model.setId(rs.getInt("id"));

							model.setShortName(StringUtil
									.getString(rs.getString("short_name"), ""));
							model.setFullName(StringUtil
									.getString(rs.getString("full_name"), ""));
							model.setContactPerson(StringUtil.getString(
									rs.getString("contract_person"), ""));
							model.setQq(StringUtil.getString(rs.getString("qq"),
									""));
							model.setPhone(StringUtil
									.getString(rs.getString("phone"), ""));
							model.setMail(StringUtil
									.getString(rs.getString("mail"), ""));
							model.setAddress(StringUtil
									.getString(rs.getString("address"), ""));
							model.setContractStartDate(StringUtil.getString(
									rs.getString("contract_start_date"), ""));
							model.setContractEndDate(StringUtil.getString(
									rs.getString("contract_end_date"), ""));
							model.setUserName(StringUtil
									.getString(rs.getString("nick_name"), ""));
							model.setCommerceUserName(StringUtil.getString(
									rs.getString("commerce_user_name"), ""));
							model.setCommerceUserId(rs.getInt("commerce_user_id"));

							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}

	public CpModel loadCpById(int id)
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp a  left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.commerce_user_id = b.id where a.id = "
				+ id;
		return (CpModel) new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if (rs.next())
				{
					CpModel model = new CpModel();
					model.setId(rs.getInt("id"));
					model.setShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil
							.getString(rs.getString("full_name"), ""));
					model.setContactPerson(StringUtil
							.getString(rs.getString("contract_person"), ""));
					model.setQq(StringUtil.getString(rs.getString("qq"), ""));
					model.setPhone(
							StringUtil.getString(rs.getString("phone"), ""));
					model.setMail(
							StringUtil.getString(rs.getString("mail"), ""));
					model.setAddress(
							StringUtil.getString(rs.getString("address"), ""));
					model.setUserId(rs.getInt("user_id"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setContractStartDate(StringUtil.getString(
							rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil
							.getString(rs.getString("contract_end_date"), ""));
					model.setStatus(rs.getInt("status"));
					return model;
				}

				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	public List<CpModel> loadCpByIds(int[] ids)
	{

		if (ids == null || ids.length == 0)
			return null;

		StringBuffer sb = new StringBuffer();
		sb.append(
				"select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp a  left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.commerce_user_id = b.id where a.id in( ");

		for (int id : ids)
		{
			sb.append(Integer.toString(id));
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");

		return (List<CpModel>) new JdbcControl().query(sb.toString(),
				new QueryCallBack()
				{

					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<CpModel> list = new ArrayList<CpModel>();

						while (rs.next())
						{
							CpModel model = new CpModel();

							model.setId(rs.getInt("id"));
							model.setUserId(rs.getInt("user_id"));
							model.setFullName(StringUtil
									.getString(rs.getString("full_name"), ""));
							model.setShortName(StringUtil
									.getString(rs.getString("short_name"), ""));
							model.setContactPerson(StringUtil.getString(
									rs.getString("contract_person"), ""));
							model.setCommerceUserId(
									rs.getInt("commerce_user_id"));
							model.setSynFlag(rs.getInt("syn_flag"));
							model.setDefaultHoldPercent(
									rs.getInt("default_hold_percent"));
							model.setAddress(StringUtil
									.getString(rs.getString("address"), ""));
							model.setContractStartDate(StringUtil.getString(
									rs.getString("contract_start_date"), ""));
							model.setContractEndDate(StringUtil.getString(
									rs.getString("contract_end_date"), ""));
							model.setMail(rs.getString("mail"));
							// System.out.println("mail:" +
							// rs.getString("mail"));
							list.add(model);
						}

						return list;
					}
				});

	}

	public boolean addCp(CpModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp(full_name,short_name,contract_person,qq,mail,phone,address,contract_start_date,contract_end_date,commerce_user_id,status) "
				+ "value('" + model.getFullName() + "','" + model.getShortName()
				+ "','" + model.getContactPerson() + "','" + model.getQq()
				+ "','" + model.getMail() + "','" + model.getPhone() + "','"
				+ model.getAddress() + "','" + model.getContractStartDate()
				+ "','" + model.getContractEndDate() + "',"
				+ model.getCommerceUserId() + ","+model.getStatus()+")";
		return new JdbcControl().execute(sql);
	}

	public boolean updateCp(CpModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp set full_name = '"
				+ model.getFullName() + "',short_name = '"
				+ model.getShortName() + "',contract_person='"
				+ model.getContactPerson() + "',qq='" + model.getQq()
				+ "',mail='" + model.getMail() + "',phone='" + model.getPhone()
				+ "',address='" + model.getAddress() + "',contract_start_date='"
				+ model.getContractStartDate() + "',contract_end_date='"
				+ model.getContractEndDate() + "',commerce_user_id="
				+ model.getCommerceUserId() + ",status="+model.getStatus()+" where id =" + model.getId();
		return new JdbcControl().execute(sql);
	}

	public boolean updateCpAccount(int cpId, int userId)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp set user_id = " + userId
				+ " where id = " + cpId;
		return new JdbcControl().execute(sql);
	}

	@SuppressWarnings("unchecked")
	public List<CpModel> loadCpBySptone(int spTroneId)
	{

		String sql = "select * from tbl_cp where  id in( "
				+ "select cp_id  from tbl_trone_order left join   tbl_trone on tbl_trone_order.trone_id= tbl_trone.id "
				+ "where tbl_trone_order.disable=0 and tbl_trone.sp_trone_id="
				+ Integer.toString(spTroneId) + " and is_unknow=0  ) ";

		return (List<CpModel>) new JdbcControl().query(sql, new QueryCallBack()
		{

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpModel> list = new ArrayList<CpModel>();

				while (rs.next())
				{
					CpModel model = new CpModel();

					model.setId(rs.getInt("id"));
					model.setUserId(rs.getInt("user_id"));
					model.setFullName(StringUtil
							.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil
							.getString(rs.getString("short_name"), ""));
					model.setContactPerson(StringUtil
							.getString(rs.getString("contract_person"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setSynFlag(rs.getInt("syn_flag"));
					model.setDefaultHoldPercent(
							rs.getInt("default_hold_percent"));
					model.setAddress(
							StringUtil.getString(rs.getString("address"), ""));
					model.setContractStartDate(StringUtil.getString(
							rs.getString("contract_start_date"), ""));
					model.setContractEndDate(StringUtil
							.getString(rs.getString("contract_end_date"), ""));
					model.setMail(rs.getString("mail"));
					list.add(model);
				}

				return list;
			}
		});

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
	
	public Map<String, Object> loadCp(int pageIndex, String keyWord,int userId)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING
				+ " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.user_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user c on a.commerce_user_id = c.id  where 1=1";

		String limit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + ","
				+ Constant.PAGE_SIZE;

		if (!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (a.full_name LIKE '%"+keyWord+"%' or a.short_name like '%" 
					+ keyWord + "%' or a.id = '"+ keyWord +"' or c.nick_name like '%" 
					+ keyWord + "%' or b.nick_name like '%" + keyWord + "%' or a.id = '" 
					+ keyWord + "') ";
		}else{
			sql+=" AND a.commerce_user_id="+userId;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		sql += " order by convert(a.short_name using gbk) asc ";

		JdbcControl control = new JdbcControl();
		map.put("rows", control.query(
				sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"),
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						if (rs.next())
							return rs.getInt(1);

						return 0;
					}
				}));

		map.put("list",
				control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING,
						" a.*,b.name,b.nick_name,c.nick_name commerce_user_name ")
				+ limit, new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<CpModel> list = new ArrayList<CpModel>();
						while (rs.next())
						{
							CpModel model = new CpModel();

							model.setId(rs.getInt("id"));

							model.setShortName(StringUtil
									.getString(rs.getString("short_name"), ""));
							model.setFullName(StringUtil
									.getString(rs.getString("full_name"), ""));
							model.setContactPerson(StringUtil.getString(
									rs.getString("contract_person"), ""));
							model.setQq(StringUtil.getString(rs.getString("qq"),
									""));
							model.setPhone(StringUtil
									.getString(rs.getString("phone"), ""));
							model.setMail(StringUtil
									.getString(rs.getString("mail"), ""));
							model.setAddress(StringUtil
									.getString(rs.getString("address"), ""));
							model.setContractStartDate(StringUtil.getString(
									rs.getString("contract_start_date"), ""));
							model.setContractEndDate(StringUtil.getString(
									rs.getString("contract_end_date"), ""));
							model.setUserName(StringUtil
									.getString(rs.getString("nick_name"), ""));
							model.setCommerceUserName(StringUtil.getString(
									rs.getString("commerce_user_name"), ""));
							model.setCommerceUserId(rs.getInt("commerce_user_id"));

							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}
	/**
	 * 增加状态字段
	 * @param pageIndex
	 * @param status
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadCp(int pageIndex,int status,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING
				+ " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.user_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user c on a.commerce_user_id = c.id  where 1=1";
		if(status>=0){
			sql+=" and a.status="+status;
		}
		String limit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + ","
				+ Constant.PAGE_SIZE;

		if (!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (a.full_name LIKE '%"+keyWord+"%' or a.short_name like '%" 
					+ keyWord + "%' or a.id = '"+ keyWord +"' or c.nick_name like '%" 
					+ keyWord + "%' or b.nick_name like '%" + keyWord + "%' or a.id = '" 
					+ keyWord + "') ";
		}

		Map<String, Object> map = new HashMap<String, Object>();

		sql += " order by id desc ";

		JdbcControl control = new JdbcControl();
		map.put("rows", control.query(
				sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"),
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						if (rs.next())
							return rs.getInt(1);

						return 0;
					}
				}));

		map.put("list",
				control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING,
						" a.*,b.name,b.nick_name,c.nick_name commerce_user_name ")
				+ limit, new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<CpModel> list = new ArrayList<CpModel>();
						while (rs.next())
						{
							CpModel model = new CpModel();

							model.setId(rs.getInt("id"));

							model.setShortName(StringUtil
									.getString(rs.getString("short_name"), ""));
							model.setFullName(StringUtil
									.getString(rs.getString("full_name"), ""));
							model.setContactPerson(StringUtil.getString(
									rs.getString("contract_person"), ""));
							model.setQq(StringUtil.getString(rs.getString("qq"),
									""));
							model.setPhone(StringUtil
									.getString(rs.getString("phone"), ""));
							model.setMail(StringUtil
									.getString(rs.getString("mail"), ""));
							model.setAddress(StringUtil
									.getString(rs.getString("address"), ""));
							model.setContractStartDate(StringUtil.getString(
									rs.getString("contract_start_date"), ""));
							model.setContractEndDate(StringUtil.getString(
									rs.getString("contract_end_date"), ""));
							model.setUserName(StringUtil
									.getString(rs.getString("nick_name"), ""));
							model.setCommerceUserName(StringUtil.getString(
									rs.getString("commerce_user_name"), ""));
							model.setCommerceUserId(rs.getInt("commerce_user_id"));
							
							model.setStatus(rs.getInt("status"));
							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}
	
	public Map<String, Object> loadCp(int userId,int pageIndex,int status,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING
				+ " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.user_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user c on a.commerce_user_id = c.id  where 1=1 and a.commerce_user_id = " + userId;
		if(status>=0){
			sql+=" and a.status="+status;
		}
		String limit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + ","
				+ Constant.PAGE_SIZE;

		if (!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (a.full_name LIKE '%"+keyWord+"%' or a.short_name like '%" 
					+ keyWord + "%' or a.id = '"+ keyWord +"' or c.nick_name like '%" 
					+ keyWord + "%' or b.nick_name like '%" + keyWord + "%' or a.id = '" 
					+ keyWord + "') ";
		}

		Map<String, Object> map = new HashMap<String, Object>();

		sql += " order by convert(a.short_name using gbk) asc ";

		JdbcControl control = new JdbcControl();
		map.put("rows", control.query(
				sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"),
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						if (rs.next())
							return rs.getInt(1);

						return 0;
					}
				}));

		map.put("list",
				control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING,
						" a.*,b.name,b.nick_name,c.nick_name commerce_user_name ")
				+ limit, new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<CpModel> list = new ArrayList<CpModel>();
						while (rs.next())
						{
							CpModel model = new CpModel();

							model.setId(rs.getInt("id"));

							model.setShortName(StringUtil
									.getString(rs.getString("short_name"), ""));
							model.setFullName(StringUtil
									.getString(rs.getString("full_name"), ""));
							model.setContactPerson(StringUtil.getString(
									rs.getString("contract_person"), ""));
							model.setQq(StringUtil.getString(rs.getString("qq"),
									""));
							model.setPhone(StringUtil
									.getString(rs.getString("phone"), ""));
							model.setMail(StringUtil
									.getString(rs.getString("mail"), ""));
							model.setAddress(StringUtil
									.getString(rs.getString("address"), ""));
							model.setContractStartDate(StringUtil.getString(
									rs.getString("contract_start_date"), ""));
							model.setContractEndDate(StringUtil.getString(
									rs.getString("contract_end_date"), ""));
							model.setUserName(StringUtil
									.getString(rs.getString("nick_name"), ""));
							model.setCommerceUserName(StringUtil.getString(
									rs.getString("commerce_user_name"), ""));
							model.setCommerceUserId(rs.getInt("commerce_user_id"));
							
							model.setStatus(rs.getInt("status"));
							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}
}
