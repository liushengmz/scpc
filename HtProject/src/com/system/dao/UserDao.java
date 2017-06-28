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
import com.system.model.UserModel;
import com.system.util.StringUtil;

public class UserDao
{
	public Map<String, Object> loadUser(int pageIndex,int groupId)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_group_user` a LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b ON a.`user_id` = b.`id` where 1=1 ";
		
		String query = "";
		
		String sql2 = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user";
		
		if(groupId>0)
			query = " and group_id =" + groupId;
		
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new  HashMap<String, Object>();
		
		map.put("rows", new JdbcControl().query((groupId>0 ? sql :sql2).replace(Constant.CONSTANT_REPLACE_STRING, " count(*) ") + query, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		}));
		
		String order = " order by id desc ";
		
		map.put("list", new JdbcControl().query((groupId>0 ? sql :sql2).replace(Constant.CONSTANT_REPLACE_STRING, (groupId>0 ? " a.group_id,b.* " : " * ")) + query + order + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UserModel> list = new ArrayList<UserModel>();
				 
				while(rs.next())
				{
					UserModel model = new UserModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setPassword(StringUtil.getString(rs.getString("pwd"),""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"),""));
					model.setMail(StringUtil.getString(rs.getString("mail"),""));
					model.setQq(StringUtil.getString(rs.getString("qq"),""));
					model.setPhone(StringUtil.getString(rs.getString("phone"),""));
					model.setStatus(rs.getInt("status"));
					//model.setCreateUser(rs.getInt("create_user"));
					
					if(model.getId()>0)
						list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	public Map<String, Object> loadUser(int pageIndex,int groupId,String userName,String nickName)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_group_user` a LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b ON a.`user_id` = b.`id` left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user c on b.create_user = c.id where 1=1 ";
		
		String query = "";
		
		String sql2 = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` b ON b.`id`=a.`create_user` where 1=1 ";
		
		if(groupId>0)
			query = " and group_id =" + groupId;
		
		if(!StringUtil.isNullOrEmpty(userName))
		{
			if(groupId>0)
				query += " AND b.name LIKE '%"+userName+"%' ";
			else
				query += " AND a.name LIKE '%"+userName+"%' ";
		}
		
		if(!StringUtil.isNullOrEmpty(nickName))
		{
			if(groupId>0)
				query += " AND b.nick_name LIKE '%"+nickName+"%' ";
			else
				query += " AND a.nick_name LIKE '%"+nickName+"%' ";
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new  HashMap<String, Object>();
		
		map.put("rows", new JdbcControl().query((groupId>0 ? sql :sql2).replace(Constant.CONSTANT_REPLACE_STRING, " count(*) ") + query, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		}));
		
		String order = " order by a.id desc ";
		
		map.put("list", new JdbcControl().query((groupId>0 ? sql :sql2).replace(Constant.CONSTANT_REPLACE_STRING, (groupId>0 ? " a.group_id,b.*,c.`nick_name` create_user_name " : " a.*,b.`nick_name` create_user_name ")) + query + order + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UserModel> list = new ArrayList<UserModel>();
				 
				while(rs.next())
				{
					UserModel model = new UserModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setPassword(StringUtil.getString(rs.getString("pwd"),""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"),""));
					model.setMail(StringUtil.getString(rs.getString("mail"),""));
					model.setQq(StringUtil.getString(rs.getString("qq"),""));
					model.setPhone(StringUtil.getString(rs.getString("phone"),""));
					model.setStatus(rs.getInt("status"));
					model.setCreateUser(StringUtil.getString(rs.getString("create_user_name"),""));
					
					if(model.getId()>0)
						list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	/**
	 * LOAD指定的用户出来
	 * @param pageIndex
	 * @param groupId
	 * @param userName
	 * @param nickName
	 * @param userId
	 * @return
	 */
	public Map<String, Object> loadUser(int pageIndex,int groupId,String userName,String nickName,int userId)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_group_user` a LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b ON a.`user_id` = b.`id` left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user c on b.create_user = c.id where 1=1 ";
		
		String query = "";
		
		String sql2 = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` b ON b.`id`=a.`create_user` where 1=1 ";
		
		if(groupId>0)
			query += " and group_id =" + groupId;
		
		if(userId>0)
		{
			if(groupId>0)
				query += " AND b.create_user = " + userId;
			else
				query += " AND a.create_user = " + userId;
		}
		
		if(!StringUtil.isNullOrEmpty(userName))
		{
			if(groupId>0)
				query += " AND b.name LIKE '%"+userName+"%' ";
			else
				query += " AND a.name LIKE '%"+userName+"%' ";
		}
		
		if(!StringUtil.isNullOrEmpty(nickName))
		{
			if(groupId>0)
				query += " AND b.nick_name LIKE '%"+nickName+"%' ";
			else
				query += " AND a.nick_name LIKE '%"+nickName+"%' ";
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		Map<String, Object> map = new  HashMap<String, Object>();
		
		map.put("rows", new JdbcControl().query((groupId>0 ? sql :sql2).replace(Constant.CONSTANT_REPLACE_STRING, " count(*) ") + query, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		}));
		
		String order = " order by a.id desc ";
		
		map.put("list", new JdbcControl().query((groupId>0 ? sql :sql2).replace(Constant.CONSTANT_REPLACE_STRING, (groupId>0 ? " a.group_id,b.*,c.`nick_name` create_user_name " : " a.*,b.`nick_name` create_user_name ")) + query + order + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UserModel> list = new ArrayList<UserModel>();
				 
				while(rs.next())
				{
					UserModel model = new UserModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setPassword(StringUtil.getString(rs.getString("pwd"),""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"),""));
					model.setMail(StringUtil.getString(rs.getString("mail"),""));
					model.setQq(StringUtil.getString(rs.getString("qq"),""));
					model.setPhone(StringUtil.getString(rs.getString("phone"),""));
					model.setStatus(rs.getInt("status"));
					model.setCreateUser(StringUtil.getString(rs.getString("create_user_name"),""));
					
					if(model.getId()>0)
						list.add(model);
				}
				
				return list;
			}
		}));
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserModel> loadUserByGroupId(int groupId)
	{
		String sql = "SELECT b.* FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_group_user` a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b ON a.`user_id` = b.`id` WHERE a.`group_id` = " + groupId + " order by b.name asc" ;
		
		return (List<UserModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UserModel> list = new ArrayList<UserModel>();
				UserModel model = null;
				while(rs.next())
				{
					model = new UserModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setPassword(StringUtil.getString(rs.getString("pwd"),""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"),""));
					model.setMail(StringUtil.getString(rs.getString("mail"),""));
					model.setQq(StringUtil.getString(rs.getString("qq"),""));
					model.setPhone(StringUtil.getString(rs.getString("phone"),""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<UserModel> loadActityUserList()
	{
		String sql = "select * from tbl_user where status = 1";
		
		return (List<UserModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<UserModel> list = new ArrayList<UserModel>();
				UserModel model = null;
				while(rs.next())
				{
					model = new UserModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setPassword(StringUtil.getString(rs.getString("pwd"),""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"),""));
					model.setMail(StringUtil.getString(rs.getString("mail"),""));
					model.setQq(StringUtil.getString(rs.getString("qq"),""));
					model.setPhone(StringUtil.getString(rs.getString("phone"),""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	//废掉，以后PASSWORD 的 MD5 不再在数据库层处理，直接在SERVER层处理
	/*
	public boolean updateUserWithPwd(UserModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user set name = '" + model.getName() + "', pwd = md5('"
				+ model.getPassword() + "') ,nick_name = '" + model.getNickName()
				+ "',mail='" + model.getMail() + "',qq='" + model.getQq()
				+ "',phone='" + model.getPhone() + "' where id ="
				+ model.getId();
		
		return new JdbcControl().execute(sql);
	}
	*/
	
	public boolean updateUserWithPwd(UserModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user set name = '" + model.getName() + "', pwd = '"
				+ model.getPassword() + "',nick_name = '" + model.getNickName()
				+ "',mail='" + model.getMail() + "',qq='" + model.getQq()
				+ "',phone='" + model.getPhone() + "' where id ="
				+ model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	//废掉，以后PASSWORD 的 MD5 不再在数据库层处理，直接在SERVER层处理
	/*
	public boolean updateUser(UserModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user set name = '" + model.getName() + "'," + (StringUtil.isNullOrEmpty(model.getPassword()) ? "" : " pwd = md5('"
				+ model.getPassword() + "') ,") + "nick_name = '" + model.getNickName()
				+ "',mail='" + model.getMail() + "',qq='" + model.getQq()
				+ "',phone='" + model.getPhone() + "',status=" + model.getStatus() + " where id ="
				+ model.getId();
		
		return new JdbcControl().execute(sql);
	}
	*/
	
	public boolean updateUser(UserModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user set name = '" + model.getName() + "'," + (StringUtil.isNullOrEmpty(model.getPassword()) ? "" : " pwd = '"
				+ model.getPassword() + "',") + "nick_name = '" + model.getNickName()
				+ "',mail='" + model.getMail() + "',qq='" + model.getQq()
				+ "',phone='" + model.getPhone() + "',status=" + model.getStatus() + " where id ="
				+ model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean updateUserWithoutPwd(UserModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user  set name = '" + model.getName() + "', nick_name = '" + model.getNickName()
				+ "',mail='" + model.getMail() + "',qq='" + model.getQq()
				+ "',phone='" + model.getPhone() + "' where id ="
				+ model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	public UserModel getUserModelById(int id)
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user where id=" + id;
		
		return (UserModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				UserModel model = null;
				if(rs.next())
				{
					model = new UserModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"),""));
					model.setPassword(StringUtil.getString(rs.getString("pwd"),""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"),""));
					model.setMail(StringUtil.getString(rs.getString("mail"),""));
					model.setQq(StringUtil.getString(rs.getString("qq"),""));
					model.setPhone(StringUtil.getString(rs.getString("phone"),""));
					model.setStatus(rs.getInt("status"));
					
				}
				return model;
			}
		});
	}
	
	//废掉，以后PASSWORD 的 MD5 不再在数据库层处理，直接在SERVER层处理
	/*
	public void addUser(UserModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user(name,pwd,nick_name,mail,qq,phone,create_user) value(?,?,?,?,?,?,?)";
		
		Map<Integer, Object> param = new HashMap<Integer, Object>();
		
		param.put(1, model.getName());
		param.put(2, StringUtil.getMd5String(model.getPassword(), 32));
		param.put(3, model.getNickName());
		param.put(4, model.getMail());
		param.put(5, model.getQq());
		param.put(6, model.getPhone());
		param.put(7, model.getCreateUserId());
		
		new JdbcControl().execute(sql, param);
	}
	*/
	
	public void addUser(UserModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user(name,pwd,nick_name,mail,qq,phone,create_user) value(?,?,?,?,?,?,?)";
		
		Map<Integer, Object> param = new HashMap<Integer, Object>();
		
		param.put(1, model.getName());
		param.put(2, model.getPassword());
		param.put(3, model.getNickName());
		param.put(4, model.getMail());
		param.put(5, model.getQq());
		param.put(6, model.getPhone());
		param.put(7, model.getCreateUserId());
		
		new JdbcControl().execute(sql, param);
	}
	
	public void delUserInGroup(int userId)
	{
		String sql = "delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_group_user where user_id =" + userId;
		new JdbcControl().execute(sql);
	}
	
	public void delUser(int id)
	{
		String sql = "delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user where id = " + id;
		new JdbcControl().execute(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> loadUserGroup(int id)
	{
		String sql = "select group_id from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_group_user where user_id = " + id;
		return (List<Integer>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Integer> list = new ArrayList<Integer>();
				while(rs.next())
					list.add(rs.getInt(1));
				return list;
			}
		});
	}
	
	public void updateUserGroup(List<Integer> list,int userId)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_group_user(user_id,group_id) values ";
		
		String values = "";
		
		for(int i=0; i <list.size(); i++)
		{
			values += "("+ userId +","+ list.get(i) +"),";
		}
		
		if(!StringUtil.isNullOrEmpty(values))
		{
			values = values.substring(0,values.length()-1);
			values += ";";
			new JdbcControl().execute(sql + values);
		}
		
	}
	
	
}
