
package com.andy.system.util;

import java.util.HashMap;
import java.util.Map;

public class SqlUtil
{

	public static String sqlEncode(String txt)
	{
		if (StringUtil.isNullOrEmpty(txt))
			return "";
		return txt.replace("\\", "\\\\").replace("\'", "\\\'");
	}

	public static Map<Integer, Object> getInsertSqlMap(String tableName,Map<String, Object> dataMap)
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
		param.put(index, sql);
		return param;
	}
	
	public static void main(String[] args)
	{
		String tableName = "tp_config.tbl_user";
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("name", "fff");
		dataMap.put("pwd", "ddddd");
		dataMap.put("nickName", "dsfew");
		dataMap.put("mail", "wwwww@163.com");
		dataMap.put("qq", "cdcdc");
		dataMap.put("phone", "sdfe");
		dataMap.put("groupList", "1,2,78,34,22");
		dataMap.put("createUser", 110);
		dataMap.put("status", 1);
		
		Map<Integer, Object> params  = SqlUtil.getInsertSqlMap(tableName, dataMap);
		
		System.out.println(params);
		
		String sql = params.get(params.size()).toString();
		
		params.remove(params.size());
		
		System.out.println(sql);
		
		System.out.println(params);
	}
	
}
