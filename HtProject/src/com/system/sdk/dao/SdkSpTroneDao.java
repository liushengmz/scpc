package com.system.sdk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.sdk.model.SdkSpTroneModel;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;

public class SdkSpTroneDao {
	@SuppressWarnings("unchecked")
	public List<SdkSpTroneModel>loadSdkSpTrone(){
		String sql="SELECT * FROM daily_config.tbl_sdk_sp_trone order by convert(name using gbk) asc";
		return (List<SdkSpTroneModel>)new JdbcGameControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SdkSpTroneModel> list = new ArrayList<SdkSpTroneModel>();
				while(rs.next())
				{
					SdkSpTroneModel model = new SdkSpTroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setOperatorId(rs.getInt("operator_id"));
					model.setCteateDate(StringUtil.getString(rs.getString("create_date"), ""));
					list.add(model);
				}
				return list;
			}
		});
		
		}
	
	public Map<String, Object> loadSdkSpTrone(int pageIndex,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from daily_config.tbl_sdk_sp_trone spt left join daily_config.tbl_sdk_sp sp on spt.sp_id=sp.id where 1=1 ";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (spt.name LIKE '%" + keyWord + "%' or sp.full_name LIKE '%" + keyWord + "%' or sp.short_name LIKE '%"+ keyWord +"%' )";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by spt.id desc ";
		JdbcGameControl control = new JdbcGameControl();
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
		
		map.put("list",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING," spt.id,spt.sp_id,spt.sp_trone_id,spt.name,spt.operator_id,spt.create_date,sp.full_name,sp.short_name ")+limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SdkSpTroneModel> list = new ArrayList<SdkSpTroneModel>();
				while(rs.next())
				{
					SdkSpTroneModel model = new SdkSpTroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setOperatorId(rs.getInt("operator_id"));
					model.setCteateDate(StringUtil.getString(rs.getString("create_date"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	public SdkSpTroneModel loadSdkSpTroneById(int id)
	{
		String sql = "SELECT spt.id,spt.sp_id,spt.sp_trone_id,spt.name,spt.operator_id,spt.create_date,sp.full_name,sp.short_name FROM daily_config.tbl_sdk_sp_trone spt LEFT JOIN daily_config.tbl_sdk_sp sp ON spt.sp_id=sp.id where spt.id="+id;
		return (SdkSpTroneModel)new JdbcGameControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SdkSpTroneModel model = new SdkSpTroneModel();

					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setOperatorId(rs.getInt("operator_id"));
					model.setCteateDate(StringUtil.getString(rs.getString("create_date"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					
					return model;
				}
				
				return null;
			}
		});
	}
	public boolean updateSdkSpTrone(SdkSpTroneModel model){
		String sql="update daily_config.tbl_sdk_sp_trone set sp_id="+model.getSpId()+",sp_trone_id="+model.getSpTroneId()+",name='"+SqlUtil.sqlEncode(model.getName())+"',operator_id="+model.getOperatorId()+" where id="+model.getId();
		return new JdbcGameControl().execute(sql);
	}
	public boolean addSdkSpTrone(SdkSpTroneModel model){
		String sql="insert into daily_config.tbl_sdk_sp_trone(sp_id,sp_trone_id,name,operator_id) value("+model.getSpId()+","+model.getSpTroneId()+",'"+SqlUtil.sqlEncode(model.getName())+"',"+model.getOperatorId()+")";
		return new JdbcGameControl().execute(sql);
	}
	public boolean deleteSdkSpTrone(int id){
		String delSql="delete from daily_config.tbl_sdk_sp_trone where id="+id;
		return new JdbcGameControl().execute(delSql);
	
	} 
}
