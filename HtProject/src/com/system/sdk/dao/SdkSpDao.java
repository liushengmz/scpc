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
import com.system.sdk.model.SdkSpModel;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;

public class SdkSpDao {
	@SuppressWarnings("unchecked")
	public List<SdkSpModel>loadSdkSp(){
		String sql="SELECT * FROM daily_config.tbl_sdk_sp sp ORDER BY sp.id DESC";
		return (List<SdkSpModel>)new JdbcGameControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SdkSpModel> list = new ArrayList<SdkSpModel>();
				while(rs.next())
				{
					SdkSpModel model = new SdkSpModel();
					
					model.setId(rs.getInt("id"));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
					list.add(model);
				}
				return list;
			}
		});

	
		
	}
	
	public Map<String, Object> loadSdkSp(int pageIndex,String keyWord)
	{
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING + " from daily_config.tbl_sdk_sp  where 1=1 ";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (full_name LIKE '%" + keyWord + "%' or short_name LIKE '%"+ keyWord +"%' )";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		sql += " order by id desc ";
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
		
		map.put("list",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING," * ")+limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SdkSpModel> list = new ArrayList<SdkSpModel>();
				while(rs.next())
				{
					SdkSpModel model = new SdkSpModel();
					
					model.setId(rs.getInt("id"));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	}
	
	public boolean addSdkSp(SdkSpModel model)
	{
		String sql="insert into daily_config.tbl_sdk_sp(full_name,short_name,remark) value('"+SqlUtil.sqlEncode(model.getFullName())+"','"+SqlUtil.sqlEncode(model.getShortName())+"','"+SqlUtil.sqlEncode(model.getRemark())+"')";
		return new JdbcGameControl().execute(sql);
	}

	public boolean updateSdkSp(SdkSpModel model)
	{
		String sql="update daily_config.tbl_sdk_sp set full_name='"+SqlUtil.sqlEncode(model.getFullName())+"',short_name='"+SqlUtil.sqlEncode(model.getShortName())+"',remark='"+SqlUtil.sqlEncode(model.getRemark())+"' where id="+model.getId();
		return new JdbcGameControl().execute(sql);
	}
	
	public SdkSpModel loadSdkSpById(int id)
	{
		String sql = "select * from daily_config.tbl_sdk_sp where id = " + id;
		return (SdkSpModel)new JdbcGameControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SdkSpModel model = new SdkSpModel();
					model.setId(rs.getInt("id"));					
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					return model;
				}
				
				return null;
			}
		});
	}
	
	public boolean deleteSdkSp(int id){
		String delSql="delete from daily_config.tbl_sdk_sp where id="+id;
		return new JdbcGameControl().execute(delSql);
	
	}
	public Map<String,Integer> checkData(String fullName,String shortName,int id){
		final Map<String,Integer> map=new HashMap<String, Integer>();
		map.put("flag", 0);
		String fullSql ="select count(*) as sm from daily_config.tbl_sdk_sp where full_name='"+fullName+"' and id!="+id;
		String shortSql ="select count(*) as sm from daily_config.tbl_sdk_sp where short_name='"+shortName+"' and id!="+id;
		JdbcGameControl control=new JdbcGameControl();
		control.query(fullSql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					Integer count=(Integer)rs.getInt("sm");
					if(count>=1){
						map.put("flag", 1);//fullName存在
					}
				}
				return null;
			}
		});
		control.query(shortSql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					Integer count=(Integer)rs.getInt("sm");
					if(count>=1){
						Integer c=(Integer)map.get("flag");
						if(c==1){
							map.put("flag", 3);//都存在	
						}else{
							map.put("flag", 2);//shortName存在
						}
					}
				}
				return null;
			}
		});
		return map;
	}

}
