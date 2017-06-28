package com.system.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public interface IJdbcControl
{
	public Object query(String sql,QueryCallBack callBack);
	
	public boolean executeMulData(String sql,List<Map<Integer, Object>> dataParams);
	
	public boolean execute(String sql);
	
	public boolean executeWithParam(String sql,Map<Integer,Object> param);
	
	public void getConnection(ConnectionCallBack callBack);
	
	public void free(ResultSet rs,Statement stmt,Connection conn);
	
}
