
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
import com.system.model.SystemUserModel;
import com.system.util.StringUtil;

public class SystemUserDao
{

	public Map<String, Object> loadUser(int pageIndex, int groupId,
			String userName, String nickName, int userId)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM  "
				+ com.system.constant.Constant.DB_DAILY_CONFIG
				+ ".`tbl_group_user` a LEFT JOIN  "
				+ com.system.constant.Constant.DB_DAILY_CONFIG
				+ ".tbl_user b ON a.`user_id` = b.`id` where a.create_user="
				+ userId;

		String query = "";

		String sql2 = "select " + Constant.CONSTANT_REPLACE_STRING + " FROM "
				+ com.system.constant.Constant.DB_DAILY_CONFIG
				+ ".tbl_user a LEFT JOIN "
				+ com.system.constant.Constant.DB_DAILY_CONFIG
				+ ".`tbl_user` b ON b.`id`=a.`create_user` where a.create_user="
				+ userId;

		if (groupId > 0)
			query = " and group_id =" + groupId;

		if (!StringUtil.isNullOrEmpty(userName))
		{
			query += " AND a.name LIKE '%" + userName + "%' ";
		}

		if (!StringUtil.isNullOrEmpty(nickName))
		{
			query += " AND b.nick_name LIKE '%" + nickName + "%' ";
		}

		String limit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + ","
				+ Constant.PAGE_SIZE;

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("rows",
				new JdbcControl().query((groupId > 0 ? sql : sql2).replace(
						Constant.CONSTANT_REPLACE_STRING, " count(*) ") + query,
						new QueryCallBack()
						{
							@Override
							public Object onCallBack(ResultSet rs)
									throws SQLException
							{
								if (rs.next())
									return rs.getInt(1);
								return 0;
							}
						}));

		String order = " order by a.id desc ";

		map.put("list",
				new JdbcControl().query((groupId > 0 ? sql : sql2).replace(
						Constant.CONSTANT_REPLACE_STRING,
						(groupId > 0 ? " a.group_id,b.* "
								: " a.*,b.`nick_name` group_name "))
						+ query + order + limit, new QueryCallBack()
						{
							@Override
							public Object onCallBack(ResultSet rs)
									throws SQLException
							{
								List<SystemUserModel> list = new ArrayList<SystemUserModel>();

								while (rs.next())
								{
									SystemUserModel model = new SystemUserModel();

									model.setId(rs.getInt("id"));
									model.setName(StringUtil.getString(
											rs.getString("name"), ""));
									model.setPwd(StringUtil.getString(
											rs.getString("pwd"), ""));
									model.setNick_name(StringUtil.getString(
											rs.getString("nick_name"), ""));
									model.setMail(StringUtil.getString(
											rs.getString("mail"), ""));
									model.setQq(StringUtil
											.getString(rs.getString("qq"), ""));
									model.setPhone(StringUtil.getString(
											rs.getString("phone"), ""));
									model.setStatus(rs.getInt("status"));
									model.setCreate_user(
											rs.getString("group_name"));

									if (model.getId() > 0)
										list.add(model);
								}

								return list;
							}
						}));

		return map;
	}

	public String loadUserGroup(int userId)
	{
		String sql = "SELECT b.`group_list` FROM `tbl_group_user` a "
				+ " LEFT JOIN `tbl_group_group` b ON b.`group_id`=a.`group_id` "
				+ " WHERE a.`user_id`=" + userId;

		return (String) new JdbcControl().query(sql, new QueryCallBack()
		{
			String str = "";

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if (rs.next())
					str = rs.getString(1);
				return str;
			}
		});
	}

	public boolean adduser(SystemUserModel model)
	{
		String sql = "INSERT INTO `"
				+ com.system.constant.Constant.DB_DAILY_CONFIG
				+ "`.`tbl_user`(`name`,`pwd`,`mail`,`qq`,`phone`,`create_user`,`status`) "
				+ " VALUE('" + model.getName() + "','" + model.getPwd() + "',"
				+ "'" + model.getMail() + "','" + model.getQq() + "'," + "'"
				+ model.getPhone() + "'," + model.getUserid() + ",1)";
		return new JdbcControl().execute(sql);
	}

	public SystemUserModel loadUserById(int id)
	{
		String sql = "SELECT * FROM `tbl_user` WHERE id = " + id;
		final SystemUserModel model = new SystemUserModel();
		;
		new JdbcControl().query(sql, new QueryCallBack()
		{

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{

				if (rs.next())
				{
					model.setId(rs.getInt("id"));
					model.setName(rs.getString("name"));
					model.setPwd(rs.getString("pwd"));
					model.setNick_name(rs.getString("nick_name"));
					model.setMail(rs.getString("mail"));
					model.setQq(rs.getString("qq"));
					model.setPhone(rs.getString("phone"));
					model.setStatus(rs.getInt("status"));
				}
				return model;
			}
		});
		return model;
	}

	public boolean updateUser(SystemUserModel model)
	{
		String sql = "UPDATE `tbl_user` SET " + "name='" + model.getName()
				+ "'," + "nick_name='" + model.getNick_name() + "'," + "pwd='"
				+ model.getPwd() + "'," + "qq='" + model.getQq() + "',"
				+ "phone='" + model.getPhone() + "'," + "mail='"
				+ model.getMail() + "'," + "status=" + model.getStatus()
				+ " WHERE id=" + model.getId();
		return new JdbcControl().execute(sql);
	}

	public boolean deleteUser(int id)
	{
		String sql = "DELETE FROM `"
				+ com.system.constant.Constant.DB_DAILY_CONFIG
				+ "`.`tbl_user` WHERE id=" + id;
		return new JdbcControl().execute(sql);
	}

	public static void main(String[] args)
	{
		SystemUserDao dao = new SystemUserDao();
		// Map<String , Object> map = dao.load(1);
		// System.out.println(map);
		// List<SystemUserModel> list = (List<SystemUserModel>)map.get("list");
		// System.out.println(list);
		// for (SystemUserModel model : list) {
		// System.out.println(model.getName());
		// }
		SystemUserModel model = dao.loadUserById(83);
		System.out.println(model.getName());
	}
}
