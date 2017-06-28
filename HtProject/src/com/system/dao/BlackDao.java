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
import com.system.model.BlackModel;
import com.system.model.CpModel;
import com.system.util.StringUtil;

public class BlackDao {
	@SuppressWarnings("unchecked")
	public List<BlackModel> loadBlack()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_black order by id asc";

		return (List<BlackModel>) new JdbcControl().query(sql, new QueryCallBack()
		{

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<BlackModel> list = new ArrayList<BlackModel>();

				while (rs.next())
				{
					BlackModel model = new BlackModel();

					model.setId(rs.getInt("id"));
					model.setImei(rs.getString("imei"));
					model.setImsi(rs.getString("imsi"));
					model.setPhone(rs.getString("phone"));
					model.setRemark(rs.getString("remark"));
					model.setCreateDate(rs.getString("create_date"));
					list.add(model);
				}

				return list;
			}
		});
	}



	public Map<String, Object> loadBlack(int pageIndex)
	{
//		String sql = "select " + Constant.CONSTANT_REPLACE_STRING
//				+ " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp order by convert(short_name using gbk) asc";
		String sql="select"+Constant.CONSTANT_REPLACE_STRING+" from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_black tb where 1=1";

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
						List<BlackModel> list = new ArrayList<BlackModel>();
						while (rs.next())
						{
							BlackModel model = new BlackModel();
							model.setId(rs.getInt("id"));

							model.setImei(StringUtil
									.getString(rs.getString("imei"), ""));
							model.setImsi(StringUtil
									.getString(rs.getString("imsi"), ""));
							model.setPhone(StringUtil.getString(
									rs.getString("phone"), ""));
							model.setRemark(StringUtil.getString(rs.getString("remark"),
									""));
							model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}

	public Map<String, Object> loadBlack(int pageIndex, String keyWord)
	{
//		String sql = "select " + Constant.CONSTANT_REPLACE_STRING
//				+ " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user b on a.user_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_user c on a.commerce_user_id = c.id  where 1=1";
		String sql="select "+Constant.CONSTANT_REPLACE_STRING+" from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_black tb where 1=1";
		String limit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + ","
				+ Constant.PAGE_SIZE;

		if (!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (tb.imei LIKE '%" + keyWord
					+ "%' or tb.imsi like '%" + keyWord
					+ "%' or tb.phone like '%" + keyWord
					+ "%' or tb.remark like '%" + keyWord
					+ "%' ) ";
		}

		Map<String, Object> map = new HashMap<String, Object>();

		sql += " order by tb.create_date desc ";

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
						"*")
				+ limit, new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<BlackModel> list = new ArrayList<BlackModel>();
						while (rs.next())
						{
							BlackModel model = new BlackModel();

							model.setId(rs.getInt("id"));


							model.setImei(StringUtil
									.getString(rs.getString("imei"), ""));
							model.setImsi(StringUtil
									.getString(rs.getString("imsi"), ""));
							model.setPhone(StringUtil.getString(
									rs.getString("phone"), ""));
							model.setRemark(StringUtil.getString(rs.getString("remark"),
									""));
							model.setCreateDate(StringUtil
									.getString(rs.getString("create_date"), ""));

							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}

	public BlackModel loadBlackById(int id)
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_black tb where tb.id = "
				+ id;
		return (BlackModel) new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if (rs.next())
				{
					BlackModel model = new BlackModel();
					model.setId(rs.getInt("id"));


					model.setImei(StringUtil
							.getString(rs.getString("imei"), ""));
					model.setImsi(StringUtil
							.getString(rs.getString("imsi"), ""));
					model.setPhone(StringUtil.getString(
							rs.getString("phone"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"),
							""));
					model.setCreateDate(StringUtil
							.getString(rs.getString("create_date"), ""));
					return model;
				}

				return null;
			}
		});
	}

	

	public boolean addBlack(BlackModel model)
	{
		String sql="insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_black(imei,imsi,phone,remark) "
				+ "value('"+model.getImei()+"',"
				+"'"+model.getImsi()+"',"
				+"'"+model.getPhone()+"',"
				+"'"+model.getRemark()+"'"
				+")";
				
		return new JdbcControl().execute(sql);
	}
	public void addBlack(BlackModel model,String[] strs,String colName){
		StringBuffer sql=new StringBuffer();
		sql.append("insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_black("+colName+", remark)");
		if(strs==null){
		    sql.append("value('"+model.getPlData()+"','"+model.getRemark()+"')");
		}else{
			sql.append(" values");
			for(int i=0;i<strs.length-1;i++){
				sql.append("('"+strs[i]+"','"+model.getRemark()+"'),");
			}
			sql.append("('"+strs[strs.length-1]+"','"+model.getRemark()+"')");
		}
		new JdbcControl().execute(sql.toString());
	}


	public boolean updateBlack(BlackModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_black set imei = '"
				+ model.getImei() + "',imsi = '"
				+ model.getImsi() + "',phone='"
				+ model.getPhone() + "',remark='" + model.getRemark()
			    +"' where id =" + model.getId();
		return new JdbcControl().execute(sql);
	}
	public boolean delete(int id){
	String sql="delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_black where id="+id;
	return new JdbcControl().execute(sql);
	}
	
}
