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
import com.system.model.*;

public class ApDAO
{
	@SuppressWarnings("unchecked")
	public List<ApModel> loadSp()
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_ap order by convert(short_name using gbk) asc";
		return (List<ApModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ApModel> list = new ArrayList<ApModel>();
				
				while(rs.next())
				{
					ApModel model = new ApModel();
					
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
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_ap order by convert(short_name using gbk) asc";
		
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
				List<ApModel> list = new ArrayList<ApModel>();
				while(rs.next())
				{
					ApModel model = new ApModel();
					
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
	
	public ApModel loadApById(int id)
	{
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_ap where id = " + id;
		return (ApModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					ApModel model = new ApModel();
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
					return model;
				}
				
				return null;
			}
		});
	}
	
	public boolean addAp(ApModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_ap(full_name,short_name,contract_person,qq,mail,phone,address,contract_start_date,contract_end_date) "
				+ "value('" + model.getFullName() + "','" + model.getShortName()
				+ "','" + model.getContactPerson() + "','" + model.getQq()
				+ "','" + model.getMail() + "','" + model.getPhone() + "','"
				+ model.getAddress() + "','" + model.getContractStartDate()
				+ "','" + model.getContractEndDate() + "')";
		return new JdbcControl().execute(sql);
	}

	public boolean updateAp(ApModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_ap set full_name = '"
				+ model.getFullName() + "',short_name = '"
				+ model.getShortName() + "',contract_person='"
				+ model.getContactPerson() + "',qq='" + model.getQq()
				+ "',mail='" + model.getMail() + "',phone='" + model.getPhone()
				+ "',address='" + model.getAddress() + "',contract_start_date='"
				+ model.getContractStartDate() + "',contract_end_date='"
				+ model.getContractEndDate() + "' where id =" + model.getId();
		return new JdbcControl().execute(sql);
	}
	
	public boolean deletAp(int id){
		String sql ="delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_ap where id="+id;
		return new JdbcControl().execute(sql);
	}
	
	public static void main(String[] args) {
		ApDAO dao = new ApDAO();
		
	}
}
