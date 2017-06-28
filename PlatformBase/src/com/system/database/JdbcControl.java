package com.system.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class JdbcControl 
{
	Logger logger = Logger.getLogger(JdbcControl.class);
	
	public Object query(String sql,QueryCallBack callBack)
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = ConnConfigMain.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			logger.debug("finish query sql [ " + sql + " ]");
			return callBack.onCallBack(rs);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.error("query sql [" + sql + "] error :" + ex.getMessage());
		}
		finally
		{
			free(rs,stmt,conn);
		}
		return null;
	}
	
	public boolean executeMulData(String sql,List<Map<Integer, Object>> dataParams)
	{
		if(dataParams==null || dataParams.size()<= 0)
		{
			logger.error("executeMulData sql [" + sql + "] not finish as params empty");
			return false;
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try
		{
			conn = ConnConfigMain.getConnection();
			pstmt = conn.prepareStatement(sql);
			for(Map<Integer, Object> params : dataParams)
			{
				for(int key : params.keySet())
				{
					pstmt.setObject(key, params.get(key));
				}
				pstmt.execute();
				pstmt.clearParameters();
			}
			logger.debug("executeMulData sql [" + sql + "] finish size:" + dataParams.size());
			return true;
		}
		catch(Exception ex)
		{
			logger.error("executeMulData sql [" + sql + "] error:" + ex.getMessage());
		}
		finally
		{
			free(null,pstmt,conn);
		}
		return false;
	}
	
	public boolean execute(String sql)
	{
		Connection conn = null;
		Statement stmt = null;
		
		try
		{
			conn = ConnConfigMain.getConnection();
			stmt = conn.createStatement();
			stmt.execute(sql);
			logger.debug("execute sql [" + sql + "] finish");
			return true;
		}
		catch(Exception ex)
		{
			logger.error("execute sql [" + sql + "] error:" + ex.getMessage());
		}
		finally
		{
			free(null,stmt,conn);
		}
		
		return false;
	}
	
	public int insertWithGenKey(String sql,Map<Integer,Object> param)
	{
		logParamsInfo(sql,param);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			conn = ConnConfigMain.getConnection();
			pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for(Integer key : param.keySet())
			{
				pstmt.setObject(key, param.get(key));
			}
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch(Exception ex)
		{
			logger.error("execute sql [" + sql + "] error:" + ex.getMessage());
		}
		finally
		{
			free(rs,pstmt,conn);
		}
		
		return -1;
	}
	
	public boolean execute(String sql,Map<Integer,Object> param)
	{
		logParamsInfo(sql,param);
		Connection conn = null;
		PreparedStatement pstmt = null;
		try
		{
			conn = ConnConfigMain.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			for(int key : param.keySet())
			{
				pstmt.setObject(key, param.get(key));
			}
			pstmt.execute();
			logger.debug("execute sql [" + sql + "] with param finish");
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			logger.error("execute sql [" + sql + "] error:" + ex.getMessage());
		}
		finally
		{
			free(null,pstmt,conn);
		}
		
		return false;
	}
	
	public void logParamsInfo(String sql,Map<Integer, Object> params)
	{
		try
		{
			sql = sql.replaceAll("\\?", "%s");
			
			Object[] objs = new Object[params.size()];
			
			for(int i=0; i<params.size(); i++)
			{
				objs[i] = params.get(i+1);
			}
			
			sql = String.format(sql, objs);
			
			logger.debug(sql);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void getConnection(ConnectionCallBack callBack)
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = ConnConfigMain.getConnection();
			stmt = conn.createStatement();
			if(callBack!=null)
				callBack.onConnectionCallBack(stmt,rs);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			free(rs,stmt,conn);
		}
	}
	
	public static void free(ResultSet rs,Statement stmt,Connection conn)
	{
		try{ if(rs!=null)rs.close(); }catch(Exception ex){}
		try{ if(stmt!=null)stmt.close(); }catch(Exception ex){}
		try{ if(conn!=null)conn.close(); }catch(Exception ex){}
	}
}
