package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.database.HtJdbcControl;
import com.system.database.IJdbcControl;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.database.TlJdbcControl;
import com.system.database.XyJdbcControl;
import com.system.database.YdJdbcControl;
import com.system.model.CpModel;
import com.system.model.CpSpTroneModel;
import com.system.model.SpModel;
import com.system.model.SpTroneModel;
import com.system.model.TroneModel;
import com.system.util.StringUtil;

public class BaseDataDao
{
	@SuppressWarnings("unchecked")
	public Map<Integer, SpModel> loadOriSpData(int coId)
	{
		IJdbcControl control = getCoControl(coId);
		
		String sql = "SELECT id,short_name,full_name,STATUS FROM daily_config.tbl_sp";
		
		if(control!=null)
		{
			return (Map<Integer, SpModel>)control.query(sql, new QueryCallBack()
			{
				@Override
				public Object onCallBack(ResultSet rs) throws SQLException
				{
					Map<Integer, SpModel> map = new HashMap<Integer, SpModel>();
					
					while(rs.next())
					{
						SpModel model = new SpModel();
						model.setSpId(rs.getInt("id"));
						model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
						model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
						model.setStatus(rs.getInt("status"));
						map.put(model.getSpId(), model);
					}
					
					return map;
				}
			});
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, CpModel> loadOriCpData(int coId)
	{
		IJdbcControl control = getCoControl(coId);
		
		String sql = "SELECT id,short_name,full_name,STATUS FROM daily_config.tbl_cp";
		
		if(control!=null)
		{
			return (Map<Integer, CpModel>)control.query(sql, new QueryCallBack()
			{
				@Override
				public Object onCallBack(ResultSet rs) throws SQLException
				{
					Map<Integer, CpModel> map = new HashMap<Integer, CpModel>();
					
					while(rs.next())
					{
						CpModel model = new CpModel();
						model.setCpId(rs.getInt("id"));
						model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
						model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
						model.setStatus(rs.getInt("status"));
						map.put(model.getCpId(), model);
					}
					
					return map;
				}
			});
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, SpTroneModel> loadOriSpTroneData(int coId)
	{
		IJdbcControl control = getCoControl(coId);
		
		String sql = "SELECT id,sp_id,NAME,product_id,trone_type,js_type,jiesuanlv,STATUS FROM daily_config.`tbl_sp_trone`";
		
		if(control!=null)
		{
			return (Map<Integer, SpTroneModel>)control.query(sql, new QueryCallBack()
			{
				@Override
				public Object onCallBack(ResultSet rs) throws SQLException
				{
					Map<Integer, SpTroneModel> map = new HashMap<Integer, SpTroneModel>();
					
					while(rs.next())
					{
						SpTroneModel model = new SpTroneModel();
						
						model.setSpId(rs.getInt("sp_id"));
						model.setProductId(rs.getInt("product_id"));
						model.setSpTroneId(rs.getInt("id"));
						model.setName(StringUtil.getString(rs.getString("name"), ""));
						model.setTroneType(rs.getInt("trone_type"));
						model.setJieSuanLv(rs.getFloat("jiesuanlv"));
						model.setStatus(rs.getInt("status"));
						model.setJsType(rs.getInt("js_type"));
						
						map.put(model.getSpTroneId(), model);
					}
					
					return map;
				}
			});
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, TroneModel> loadOriTroneData(int coId)
	{
		IJdbcControl control = getCoControl(coId);
		
		String sql = "SELECT id,sp_trone_id,trone_name,price,STATUS FROM daily_config.`tbl_trone`";
		
		if(control!=null)
		{
			return (Map<Integer, TroneModel>)control.query(sql, new QueryCallBack()
			{
				@Override
				public Object onCallBack(ResultSet rs) throws SQLException
				{
					Map<Integer, TroneModel> map = new HashMap<Integer, TroneModel>();
					
					while(rs.next())
					{
						TroneModel model = new TroneModel();
						
						model.setSpTroneId(rs.getInt("sp_trone_id"));
						model.setTroneId(rs.getInt("id"));
						model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
						model.setPrice(rs.getInt("price"));
						model.setStatus(rs.getInt("status"));
						
						map.put(model.getTroneId(), model);
					}
					
					return map;
				}
			});
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, CpSpTroneModel> loadOriCpSpTroneData(int coId)
	{
		IJdbcControl control = getCoControl(coId);
		
		String sql = "SELECT id,cp_id,sp_trone_id,rate,js_type FROM daily_config.`tbl_cp_trone_rate`";
		
		if(control!=null)
		{
			return (Map<Integer, CpSpTroneModel>)control.query(sql, new QueryCallBack()
			{
				@Override
				public Object onCallBack(ResultSet rs) throws SQLException
				{
					Map<Integer, CpSpTroneModel> map = new HashMap<Integer, CpSpTroneModel>();
					
					while(rs.next())
					{
						CpSpTroneModel model = new CpSpTroneModel();
						
						model.setCpSpTroneId(rs.getInt("id"));
						model.setCpId(rs.getInt("cp_id"));
						model.setSpTroneId(rs.getInt("sp_trone_id"));
						model.setRate(rs.getFloat("rate"));
						model.setJsType(rs.getInt("js_type"));
						
						map.put(model.getCpSpTroneId(), model);
					}
					
					return map;
				}
			});
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, SpModel> loadDescSpData(int coId)
	{
		String sql = "SELECT sp_id,short_name,full_name,STATUS FROM comsum_config.tbl_sp where co_id = " + coId;
		
		return (Map<Integer, SpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<Integer, SpModel> map = new HashMap<Integer, SpModel>();
				
				while(rs.next())
				{
					SpModel model = new SpModel();
					model.setSpId(rs.getInt("sp_id"));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setStatus(rs.getInt("status"));
					map.put(model.getSpId(), model);
				}
				
				return map;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, CpModel> loadDescCpData(int coId)
	{
		String sql = "SELECT cp_id,short_name,full_name,STATUS FROM comsum_config.tbl_cp where co_id = " + coId;
		
		return (Map<Integer, CpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<Integer, CpModel> map = new HashMap<Integer, CpModel>();
				
				while(rs.next())
				{
					CpModel model = new CpModel();
					model.setCpId(rs.getInt("cp_id"));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("full_name"), ""));
					model.setStatus(rs.getInt("status"));
					map.put(model.getCpId(), model);
				}
				
				return map;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, SpTroneModel> loadDescSpTroneData(int coId)
	{
		String sql = "SELECT sp_trone_id,sp_id,NAME,product_id,trone_type,js_type,jiesuanlv,STATUS FROM comsum_config.`tbl_sp_trone` where co_id = " + coId;
		
		return (Map<Integer, SpTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<Integer, SpTroneModel> map = new HashMap<Integer, SpTroneModel>();
				
				while(rs.next())
				{
					SpTroneModel model = new SpTroneModel();
					
					model.setSpId(rs.getInt("sp_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setProductId(rs.getInt("product_id"));
					model.setTroneType(rs.getInt("trone_type"));
					model.setJsType(rs.getInt("js_type"));
					model.setJieSuanLv(rs.getFloat("jiesuanlv"));
					model.setStatus(rs.getInt("status"));
					
					map.put(model.getSpTroneId(), model);
				}
				
				return map;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, TroneModel> loadDescTroneData(int coId)
	{
		String sql = "SELECT trone_id,sp_trone_id,trone_name,price,STATUS FROM comsum_config.`tbl_trone` WHERE  co_id = " + coId;
		
		return (Map<Integer, TroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<Integer, TroneModel> map = new HashMap<Integer, TroneModel>();
				
				while(rs.next())
				{
					TroneModel model = new TroneModel();
					
					model.setTroneId(rs.getInt("trone_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setStatus(rs.getInt("status"));
					
					map.put(model.getTroneId(), model);
				}
				
				return map;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, CpSpTroneModel> loadDescCpSpTroneData(int coId)
	{
		String sql = "SELECT cp_sp_trone_id,cp_id,sp_trone_id,rate,js_type FROM comsum_config.`tbl_cp_trone_rate` WHERE  co_id = " + coId;
		
		return (Map<Integer, CpSpTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<Integer, CpSpTroneModel> map = new HashMap<Integer, CpSpTroneModel>();
				
				while(rs.next())
				{
					CpSpTroneModel model = new CpSpTroneModel();
					
					model.setCpId(rs.getInt("cp_id"));
					model.setCpSpTroneId(rs.getInt("cp_sp_trone_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setRate(rs.getFloat("rate"));
					model.setJsType(rs.getInt("js_type"));
					
					map.put(model.getCpSpTroneId(), model);
				}
				
				return map;
			}
		});
	}
	
	public void addSpData(int coId,List<SpModel> list)
	{
		if(list==null || list.isEmpty())
			return;
			
		StringBuffer sb = new StringBuffer(256);
		
		sb.append("INSERT INTO comsum_config.tbl_sp(co_id,sp_id,short_name,full_name,STATUS) VALUES ");
		
		for(SpModel model : list)
		{
			sb.append("(" + coId + "," + model.getSpId() + ",'" + model.getShortName() + "','" + model.getFullName() + "'," + model.getStatus() + "),");
		}
		
		new JdbcControl().execute(sb.toString().substring(0,sb.length()-1));
	}
	
	public void addCpData(int coId,List<CpModel> list)
	{
		if(list==null || list.isEmpty())
			return;
			
		StringBuffer sb = new StringBuffer(256);
		
		sb.append("INSERT INTO comsum_config.tbl_cp(co_id,cp_id,short_name,full_name,STATUS) VALUES ");
		
		for(CpModel model : list)
		{
			sb.append("(" + coId + "," + model.getCpId() + ",'" + model.getShortName() + "','" + model.getFullName() + "'," + model.getStatus() + "),");
		}
		
		new JdbcControl().execute(sb.toString().substring(0,sb.length()-1));
	}
	
	public void addSpTroneData(int coId,List<SpTroneModel> list)
	{
		if(list==null || list.isEmpty())
			return;
			
		StringBuffer sb = new StringBuffer(256);
		
		sb.append("INSERT INTO comsum_config.tbl_sp_trone(co_id,sp_id,sp_trone_id,name,product_id,js_type,trone_type,jiesuanlv,STATUS) VALUES ");
		
		for(SpTroneModel model : list)
		{
			sb.append("(" + coId + "," + model.getSpId() + ","
					+ model.getSpTroneId() + ",'" + model.getName() + "',"
					+ model.getProductId() + "," + model.getJsType() + ","
					+ model.getTroneType() + "," + model.getJieSuanLv() + ","
					+ model.getStatus() + "),");
		}
		
		new JdbcControl().execute(sb.toString().substring(0,sb.length()-1));
	}
	
	public void addTroneData(int coId,List<TroneModel> list)
	{
		if(list==null || list.isEmpty())
			return;
			
		StringBuffer sb = new StringBuffer(256);
		
		sb.append("INSERT INTO comsum_config.tbl_trone(co_id,trone_id,sp_trone_id,trone_name,price,STATUS) VALUES ");
		
		for(TroneModel model : list)
		{
			sb.append("(" + coId + "," + model.getTroneId() + ","
					+ model.getSpTroneId() + ",'" + model.getTroneName() + "',"
					+ model.getPrice() + "," 
					+ model.getStatus() + "),");
		}
		
		new JdbcControl().execute(sb.toString().substring(0,sb.length()-1));
	}
	
	public void addCpSpTroneData(int coId,List<CpSpTroneModel> list)
	{
		if(list==null || list.isEmpty())
			return;
			
		StringBuffer sb = new StringBuffer(256);
		
		sb.append("INSERT INTO comsum_config.tbl_cp_trone_rate(co_id,cp_sp_trone_id,cp_id,sp_trone_id,rate,js_type) VALUES ");
		
		for(CpSpTroneModel model : list)
		{
			sb.append("(" + coId + "," + model.getCpSpTroneId() + "," + model.getCpId() + "," + model.getSpTroneId() + "," + model.getRate() +  "," + model.getJsType() + "),");
		}
		
		new JdbcControl().execute(sb.toString().substring(0,sb.length()-1));
	}
	
	public void updateSpData(final int coId,final List<SpModel> list)
	{
		if(list==null || list.isEmpty())
			return;
		
		String sql = "UPDATE comsum_config.`tbl_sp` SET short_name = ?, full_name = ?,STATUS = ? WHERE co_id = " + coId + " AND sp_id = ?";
		
		JdbcControl control = new JdbcControl();
		
		List<Map<Integer,Object>> paramList = new ArrayList<Map<Integer,Object>>();
		
		for(SpModel model : list)
		{
			System.out.println(model);
			Map<Integer,Object> map = new HashMap<Integer, Object>();
			
			map.put(1, model.getShortName());
			map.put(2, model.getFullName());
			map.put(3, model.getStatus());
			map.put(4, model.getSpId());
			
			paramList.add(map);
		}
		
		control.executeWithParam(sql, paramList);
	}
	
	public void updateCpData(final int coId,final List<CpModel> list)
	{
		if(list==null || list.isEmpty())
			return;
		
		String sql = "UPDATE comsum_config.`tbl_cp` SET short_name = ?, full_name = ?,STATUS = ? WHERE co_id = " + coId + " AND cp_id = ? ";
		
		JdbcControl control = new JdbcControl();
		
		List<Map<Integer,Object>> paramList = new ArrayList<Map<Integer,Object>>();
		
		for(CpModel model : list)
		{
			Map<Integer,Object> map = new HashMap<Integer, Object>();
			
			map.put(1, model.getShortName());
			map.put(2, model.getFullName());
			map.put(3, model.getStatus());
			map.put(4, model.getCpId());
			
			paramList.add(map);
		}
		
		control.executeWithParam(sql, paramList);
	}
	
	public void updateSpTroneData(final int coId,final List<SpTroneModel> list)
	{
		if(list==null || list.isEmpty())
			return;
		
		String sql = "UPDATE comsum_config.`tbl_sp_trone` SET sp_id = ? , NAME = ? , product_id = ? , trone_type = ? , js_type = ?, jiesuanlv = ? , STATUS = ? WHERE co_id = " + coId + " AND sp_trone_id = ?";
		
		JdbcControl control = new JdbcControl();
		
		List<Map<Integer,Object>> paramList = new ArrayList<Map<Integer,Object>>();
		
		for(SpTroneModel model : list)
		{
			Map<Integer,Object> map = new HashMap<Integer, Object>();
			
			map.put(1, model.getSpId());
			map.put(2, model.getName());
			map.put(3, model.getProductId());
			map.put(4, model.getTroneType());
			map.put(5, model.getJsType());
			map.put(6, model.getJieSuanLv());
			map.put(7, model.getStatus());
			map.put(8, model.getSpTroneId());
			
			paramList.add(map);
		}
		
		control.executeWithParam(sql, paramList);
	}
	
	public void updateTroneData(final int coId,final List<TroneModel> list)
	{
		if(list==null || list.isEmpty())
			return;
		
		String sql = "UPDATE comsum_config.`tbl_trone` SET sp_trone_id = ? , trone_name = ? , price = ? , status = ?  WHERE co_id = " + coId + " AND trone_id = ?";
		
		JdbcControl control = new JdbcControl();
		
		List<Map<Integer,Object>> paramList = new ArrayList<Map<Integer,Object>>();
		
		for(TroneModel model : list)
		{
			Map<Integer,Object> map = new HashMap<Integer, Object>();
			
			map.put(1, model.getSpTroneId());
			map.put(2, model.getTroneName());
			map.put(3, model.getPrice());
			map.put(4, model.getStatus());
			map.put(5, model.getTroneId());
			
			paramList.add(map);
		}
		
		control.executeWithParam(sql, paramList);
	}
	
	public void updateCpSpTroneData(final int coId,final List<CpSpTroneModel> list)
	{
		if(list==null || list.isEmpty())
			return;
		
		String sql = "UPDATE comsum_config.`tbl_cp_trone_rate` SET sp_trone_id = ? , cp_id = ? , rate = ? , js_type = ?  WHERE co_id = " + coId + " AND cp_sp_trone_id = ? ";
		
		JdbcControl control = new JdbcControl();
		
		List<Map<Integer,Object>> paramList = new ArrayList<Map<Integer,Object>>();
		
		for(CpSpTroneModel model : list)
		{
			Map<Integer,Object> map = new HashMap<Integer, Object>();
			
			map.put(1, model.getSpTroneId());
			map.put(2, model.getCpId());
			map.put(3, model.getRate());
			map.put(4, model.getJsType());
			map.put(5, model.getCpSpTroneId());
			
			paramList.add(map);
		}
		
		control.executeWithParam(sql, paramList);
	}
	
	
	private IJdbcControl getCoControl(int coId)
	{
		IJdbcControl control = null;
		
		switch(coId)
		{
			case 1:
				control = new HtJdbcControl();
				break;
				
			case 2:
				control = new TlJdbcControl();
				break;
				
			case 3:
				control = new YdJdbcControl();
				break;	
				
			case 4:
				control = new XyJdbcControl();
				break;
				
			default:
				break;
		}
		
		return control;
	}
}
