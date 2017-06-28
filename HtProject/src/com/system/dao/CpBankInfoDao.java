package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.model.CpBankModel;
import com.system.model.SpModel;
import com.system.util.StringUtil;

public class CpBankInfoDao {
	@SuppressWarnings("unchecked")
	public List<CpBankModel> loadCpBank()
	{
		String sql="SELECT cpb.*,cp.user_id AS com_user_id,cp.full_name,cp.short_name FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_bank_account cpb "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp cp ON cpb.cp_id=cp.id ORDER BY cpb.id DESC";
		return (List<CpBankModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpBankModel> list = new ArrayList<CpBankModel>();
				
				while(rs.next())
				{
					CpBankModel model = new CpBankModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setType(rs.getInt("type"));
					model.setTypeName(model.getType()==1?"对私":"对公");
					model.setBankName(StringUtil.getString(rs.getString("bank_name"), ""));
					model.setUserName(StringUtil.getString(rs.getString("user_name"), ""));
					model.setBankAccount(StringUtil.getString(rs.getString("account"), ""));
					model.setBankBranch(StringUtil.getString(rs.getString("branch"), ""));
					model.setStatus(rs.getInt("status"));
					model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
					model.setComUserId(rs.getInt("com_user_id"));
					model.setCpFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	
	
	
	public Map<String, Object> loadCpBank(int pageIndex,String keyWord,int type,int status)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_bank_account cpb "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp cp ON cpb.cp_id=cp.id where 1=1";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (cpb.bank_name LIKE '%"+keyWord+"%' or cpb.user_name LIKE '%"+keyWord+"%' or cp.short_name LIKE '%"+keyWord+"%' or cp.full_name LIKE '%"+keyWord+"%')";
		}
		if(type>=0){
			sql +=" AND cpb.type="+type;
		}
		if(status>=0){
			sql +=" AND cpb.status="+status;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " ORDER BY cpb.id DESC ";
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
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " cpb.*,cp.user_id AS com_user_id,cp.full_name,cp.short_name ") + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpBankModel> list = new ArrayList<CpBankModel>();
				while(rs.next())
				{
					CpBankModel model = new CpBankModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setType(rs.getInt("type"));
					model.setTypeName(model.getType()==1?"对私":"对公");
					model.setBankName(StringUtil.getString(rs.getString("bank_name"), ""));
					model.setUserName(StringUtil.getString(rs.getString("user_name"), ""));
					model.setBankAccount(StringUtil.getString(rs.getString("account"), ""));
					model.setBankBranch(StringUtil.getString(rs.getString("branch"), ""));
					model.setStatus(rs.getInt("status"));
					model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
					model.setComUserId(rs.getInt("com_user_id"));
					model.setCpFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					
					list.add(model);
				}
				return list;
			}
		}));
		map.put("type", type);
		map.put("status", status);
		return map;
	}
	
	public CpBankModel loadCpBankById(int id)
	{
		String sql = "SELECT cpb.*,cp.user_id AS com_user_id,cp.full_name,cp.short_name FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_bank_account cpb "
				+ "LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp cp ON cpb.cp_id=cp.id where cpb.id="+id;
		return (CpBankModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					CpBankModel model = new CpBankModel();
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setType(rs.getInt("type"));
					model.setTypeName(model.getType()==1?"对私":"对公");
					model.setBankName(StringUtil.getString(rs.getString("bank_name"), ""));
					model.setUserName(StringUtil.getString(rs.getString("user_name"), ""));
					model.setBankAccount(StringUtil.getString(rs.getString("account"), ""));
					model.setBankBranch(StringUtil.getString(rs.getString("branch"), ""));
					model.setStatus(rs.getInt("status"));
					model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
					model.setComUserId(rs.getInt("com_user_id"));
					model.setCpFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					return model;
				}
				
				return null;
			}
		});
	}
	
	public boolean addCpBank(CpBankModel model)
	{
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_bank_account(cp_id,type,bank_name,user_name,account,branch,status) "
				+ "value('" + model.getCpId() + "','" + model.getType()
				+ "','" + model.getBankName() + "','" + model.getUserName()
				+ "','" + model.getBankAccount() + "','" + model.getBankBranch() + "','"
				+ model.getStatus()+"' )";
		return new JdbcControl().execute(sql);
	}

	public boolean updateCpBank(CpBankModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_bank_account set cp_id = '"
				+ model.getCpId() + "',type = '"+ model.getType() 
				+ "',bank_name='"+ model.getBankName() + "',user_name='" + model.getUserName()
				+ "',account='" + model.getBankAccount() + "',branch='" + model.getBankBranch()
				+ "',status='" + model.getStatus() + "' where id =" + model.getId();
		return new JdbcControl().execute(sql);
	}
	public boolean deleteCpBank(int id){
		String sql="delete from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_bank_account where id="+id;
		return new JdbcControl().execute(sql);
	}
	public Map<String, Integer> checkData(int cpId,int type,int status,int id){

		final Map<String,Integer> map=new HashMap<String, Integer>();
		map.put("flag", 0);
		String sql ="select count(*) as sm from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_bank_account where cp_id="+cpId+" and type="+type+" and status="+status+" and id!="+id;
		JdbcControl control=new JdbcControl();
		control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					Integer count=(Integer)rs.getInt("sm");
					if(count>=1){
						map.put("flag", 1);//存在
					}
				}
				return null;
			}
		});
	
		return map;
	
	}
}
