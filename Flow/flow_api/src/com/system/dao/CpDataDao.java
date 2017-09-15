package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CpModel;
import com.system.model.CpRatioModel;
import com.system.model.CpTroneModel;
import com.system.util.StringUtil;

public class CpDataDao
{
	@SuppressWarnings("unchecked")
	public List<CpModel> loadCpList()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp WHERE STATUS = 1";
		return (List<CpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpModel> list = new ArrayList<CpModel>();
				while(rs.next())
				{
					CpModel model = new CpModel();
					model.setCpId(rs.getInt("id"));
					model.setCurrency(rs.getInt("currency"));
					model.setSignKey(StringUtil.getString(rs.getString("sign_key"), ""));
					String ipList = StringUtil.getString(rs.getString("iplist"), "");
					String[] ips = ipList.split(",");
					for(String ip : ips)
					{
						model.getIpList().add(ip);
					}
					model.setNotifyUrl(StringUtil.getString(rs.getString("notify_url"), ""));
					list.add(model);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> loadCpCurrency()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp WHERE STATUS = 1";
		return (Map<Integer, Integer>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<Integer, Integer> map = new HashMap<Integer, Integer>();
				while(rs.next())
				{
					map.put(rs.getInt("id"), rs.getInt("currency"));
				}
				return map;
			}
		});
	}
	
	/**
	 * 获取我方给下游的折扣
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CpRatioModel> loadCpRatioList()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp_ratio WHERE STATUS = 1";
		return (List<CpRatioModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpRatioModel> list = new ArrayList<CpRatioModel>(); 
				
				while(rs.next())
				{
					CpRatioModel model = new CpRatioModel();
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setOperator(rs.getInt("operator"));
					model.setProId(rs.getInt("pro_id"));
					model.setRatio(rs.getInt("ratio"));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
		
	}
	
	/**
	 * 
	 * @param type 1是用户建立 2是选择通道自主建立
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CpTroneModel> loadCpTroneList(int addType)
	{
		String sql = "SELECT a.id,a.cp_id,a.ratio cp_ratio,b.pro_id,b.ratio sp_ratio,c.send_sms,b.id trone_id,g.id sp_api_id,";
		sql += " c.id sp_trone_id,d.id sp_id,e.num flow_size,e.operator,e.price,f.use_rang rang,f.time_type";
		sql += " FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp_trone a";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_trone b ON a.trone_id = b.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sp_trone c ON b.sp_trone_id = c.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sp d ON c.sp_id = d.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_basic_price e ON c.price_id = e.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_flow_type f ON c.flow_type_id = f.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sp_api g ON c.sp_api_id = g.id";
		sql += " WHERE a.status = 1";
		sql += " AND b.status = 1 AND c.status  = 1";
		
		if(addType>0)
		{
			sql += " AND a.add_type = " + addType;
		}
		
		return (List<CpTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpTroneModel> list = new ArrayList<CpTroneModel>();
				while(rs.next())
				{
					CpTroneModel model = new CpTroneModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setProId(rs.getInt("pro_id"));
					model.setSpRatio(rs.getInt("sp_ratio"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setCpRatio(rs.getInt("cp_ratio"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setPrice(rs.getInt("price"));
					model.setFlowSize(rs.getInt("flow_size"));
					model.setOperator(rs.getInt("operator"));
					model.setRang(rs.getInt("rang"));
					model.setSpApiId(rs.getInt("sp_api_id"));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	public CpTroneModel getCpTrone(int cpTroneId)
	{
		String sql = "SELECT a.id,a.cp_id,a.ratio cp_ratio,b.pro_id,b.ratio sp_ratio,c.send_sms,b.id trone_id,e.id base_price_id";
		sql += " c.id sp_trone_id,d.id sp_id,e.num flow_size,e.operator,e.price,f.use_rang rang,f.time_type";
		sql += " FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp_trone a";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_trone b ON a.trone_id = b.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sp_trone c ON b.sp_trone_id = c.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sp d ON c.sp_id = d.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_basic_price e ON c.price_id = e.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_flow_type f ON c.flow_type_id = f.id";
		sql += " WHERE a.id = " + cpTroneId;
		
		return (CpTroneModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					CpTroneModel model = new CpTroneModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setProId(rs.getInt("pro_id"));
					model.setSpRatio(rs.getInt("sp_ratio"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setCpRatio(rs.getInt("cp_ratio"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setPrice(rs.getInt("price"));
					model.setFlowSize(rs.getInt("flow_size"));
					model.setOperator(rs.getInt("operator"));
					model.setRang(rs.getInt("rang"));
					model.setBasePriceId(rs.getInt("base_price_id"));
					
					return model;
				}
				return null;
			}
		});
	}
	
	/**
	 * 自动增加CP通道
	 * @param cpId
	 * @param troneId
	 * @param ratio
	 * @return
	 */
	public int addCpTrone(int cpId,int troneId,int ratio)
	{
		String sql = "insert into " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp_trone(cp_id,trone_id,ratio,status,add_type) values(?,?,?,?,?)";
		
		Map<Integer, Object> param = new HashMap<Integer, Object>();
		
		param.put(1, cpId);
		param.put(2, troneId);
		param.put(3, ratio);
		param.put(4, 1);
		param.put(5, 2);
		
		return new JdbcControl().insertWithGenKey(sql, param);
	}
	
	/**
	 * 更新CP的余额
	 * @param cpId
	 * @param fee
	 */
	public void updateCpCurrency(int cpId,int fee)
	{
		String sql = "update " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp set currency = " + fee + " where id = " + cpId;
		new JdbcControl().execute(sql);
	}
	
	
}
