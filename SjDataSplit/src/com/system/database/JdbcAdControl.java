package com.system.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class JdbcAdControl 
{
	Logger logger = Logger.getLogger(JdbcAdControl.class);
	
	public Object query(String sql,QueryCallBack callBack)
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = ConnAdMain.getConnection();
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
			conn = ConnAdMain.getConnection();
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
			conn = ConnAdMain.getConnection();
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
	
	public boolean execute(String sql,Map<Integer,Object> param)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println("why i am here");
		try
		{
			conn = ConnAdMain.getConnection();
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
	
	public void getConnection(ConnectionCallBack callBack)
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			conn = ConnAdMain.getConnection();
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
