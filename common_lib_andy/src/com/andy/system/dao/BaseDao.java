package com.andy.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.andy.system.constant.SysConstant;
import com.andy.system.db.DbDataToMap;
import com.andy.system.db.JdbcControl;
import com.andy.system.db.QueryCallBack;
import com.andy.system.util.StringUtil;

public abstract class BaseDao
{
	protected String tableName = null;
	
	protected abstract void setTableName();
	
	/**
	 * 增加或更新数据库的对像
	 * @param dataMap
	 * @param keyMap
	 * @return
	 */
	public int addOrUpdateDdData(Map<String, Object> dataMap,Map<String, Object> keyMap)
	{
		if(StringUtil.isNullOrEmpty(tableName) || dataMap==null)
			return -1;
		
		if(keyMap==null || keyMap.isEmpty())
		{
			return addModel(dataMap);
		}
		else
		{
			return updateModel(dataMap,keyMap);
		}
	}
	
	private int addModel(Map<String, Object> dataMap)
	{
		String sql = "INSERT INTO " + tableName + "(";
		String qStrCount = "";
		Map<Integer, Object> param = new HashMap<Integer, Object>();
		int index = 1;
		for(String key : dataMap.keySet())
		{
			qStrCount += "?,";
			sql += StringUtil.camelToUnderline(key) + ",";
			param.put(index++, dataMap.get(key));
		}
		sql = sql.substring(0, sql.length()-1);
		qStrCount = qStrCount.substring(0, qStrCount.length()-1);
		sql += ") VALUES(" + qStrCount + ")";
		
		return new JdbcControl().insertWithGenKey(sql, param);
	}
	
	private int updateModel(Map<String, Object> dataMap,Map<String, Object> keyMap)
	{
		String querySql = "SELECT * FROM " + tableName  + " WHERE 1=1 ";
		
		Map<Integer, Object> param = new HashMap<Integer, Object>();
		
		int index = 1;
		
		for(String key : keyMap.keySet())
		{
			querySql += "AND " + key + " = ? ";
			param.put(index++, keyMap.get(key));
		}
		
		querySql = querySql.substring(0, querySql.length()-1);
		
		JdbcControl control = new JdbcControl();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> oriDataMap = (Map<String, Object>)control.query(querySql, param, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				return DbDataToMap.dbDataToMap(rs);
			}
		});
		
		if(oriDataMap==null || oriDataMap.isEmpty())
			return -1;
		
		String sql = "UPDATE " + tableName + " SET ";
		
		param.clear();
		
		index = 1;
		
		for(String key : dataMap.keySet())
		{
			if(!dataMap.get(key).equals(oriDataMap.get(key)))
			{
				sql += StringUtil.camelToUnderline(key) + " = ? ,";
				param.put(index++, dataMap.get(key));
			}
		}
		
		if(param.isEmpty())
			return -1;
		
		sql = sql.substring(0, sql.length()-1) + " WHERE ";
		
		for(String key : keyMap.keySet())
		{
			sql += StringUtil.camelToUnderline(key) + " = ? " + "AND ";
			param.put(index++, keyMap.get(key));
		}
		
		sql = sql.substring(0, sql.length() - 4);
		
		return control.execute(sql, param) ? 0 : -1;
	}
	
	public boolean delDbData(Map<String, Object> keyMap)
	{
		String sql = "DELETE FROM " + tableName  + " WHERE 1=1 ";
		
		Map<Integer, Object> param = new HashMap<Integer, Object>();
		
		int index = 1;
		
		for(String key : keyMap.keySet())
		{
			sql += "AND " + key + " = ? ";
			param.put(index++, keyMap.get(key));
		}
		
		return new JdbcControl().execute(sql,param);
	}
	
	protected Map<String, Object> loadCustomData(String sql,String queryParams)
	{
		if(StringUtil.isNullOrEmpty(sql) || StringUtil.isNullOrEmpty(queryParams))
			return null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		
		map.put("rows",control.query(sql.replace(SysConstant.CONSTANT_REPLACE_STRING, "count(*)"), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(SysConstant.CONSTANT_REPLACE_STRING, queryParams), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				return DbDataToMap.dbDataToMapList(rs);
			}
		}));
		
		return map;
	}
}
