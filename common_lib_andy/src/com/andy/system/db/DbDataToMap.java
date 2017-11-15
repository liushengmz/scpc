package com.andy.system.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.andy.system.util.StringUtil;

public class DbDataToMap
{
	private static Logger logger = Logger.getLogger(DbDataToMap.class);
	
	public static Map<String, Object> dbDataToMap(ResultSet rs)
	{
		try
		{
			if(rs.next())
				return dbDataToSingleMap(rs);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	private static Map<String, Object> dbDataToSingleMap(ResultSet rs)
	{
		Map<String, Object> map = new HashMap<>();
		
		try
		{
			ResultSetMetaData rsm = rs.getMetaData();
			
			String columnLabel = "";
			String columnTypeName = "";
			
			for(int i=1; i<=rsm.getColumnCount(); i++)
			{
				columnLabel = rsm.getColumnLabel(i);
				columnTypeName = rsm.getColumnTypeName(i);
				if("INT".equalsIgnoreCase(columnTypeName))
				{
					map.put(StringUtil.underlineToCamel(columnLabel,true), rs.getInt(i));
				}
				else if("VARCHAR".equalsIgnoreCase(columnTypeName)
						||"TIMESTAMP".equalsIgnoreCase(columnTypeName)
						||"DATETIME".equalsIgnoreCase(columnTypeName))
				{
					map.put(StringUtil.underlineToCamel(columnLabel,true), StringUtil.getString(rs.getString(i),""));
				}
				else if("DECIMAL".equalsIgnoreCase(columnTypeName))
				{
					map.put(StringUtil.underlineToCamel(columnLabel,true), rs.getDouble(i));
				}
				else if("BIGINT".equalsIgnoreCase(columnTypeName))
				{
					map.put(StringUtil.underlineToCamel(columnLabel,true), rs.getLong(i));
				}
				
				//暂时先不删除,等以后有要测试的时候就需要你的出现了
				//System.out.println("[" + columnLabel + "]->[" + StringUtil.underlineToCamel(columnLabel,true) + "]->[" + columnTypeName + "][" + rs.getObject(i) + "]");
			}
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		return map;
	}
	
	public static List<Map<String, Object>> dbDataToMapList(ResultSet rs)
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		try
		{
			while(rs.next())
			{
				list.add(dbDataToSingleMap(rs));
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		return list;
	}
	
}
