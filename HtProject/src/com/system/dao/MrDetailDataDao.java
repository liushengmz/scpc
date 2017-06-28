package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.params.ReportParamsModel;
import com.system.util.StringUtil;
import com.system.vo.DetailDataVo;

public class MrDetailDataDao
{
	@SuppressWarnings("unchecked")
	public List<DetailDataVo> loadDetailDataByPhoneMsg(String keyWord,String table,int type)
	{
		String chkType = "a.mobile";
		
		if(type==1)
		{
			chkType = "a.mobile";
		}
		else if(type==2)
		{
			chkType = "a.imei";
		}
		else if(type==3)
		{
			chkType = "a.imsi";
		}
		
		String sql = " SELECT a.`imei`,a.`imsi`,a.`mobile`,";
		sql += " g.`name` province_name,h.`name` city_name,e.`short_name` sp_name,d.`name` sp_trone_name,";
		sql += " b.`price`,f.`short_name` cp_name,a.`syn_flag`,a.`create_date`";
		sql += " FROM daily_log.`tbl_mr_"+ table +"` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` c ON a.`trone_order_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` e ON d.`sp_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` f ON c.`cp_id` = f.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_province` g ON a.`province_id` = g.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_city` h ON a.`city_id` = h.`id`";
		sql += " WHERE "+ chkType +" in ("+ keyWord +") order by a.create_date desc limit 0,100";
		
		return (List<DetailDataVo>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<DetailDataVo> list = new ArrayList<DetailDataVo>();
				
				while(rs.next())
				{
					DetailDataVo vo = new DetailDataVo();
					vo.setCityName(StringUtil.getString(rs.getString("city_name"), ""));
					vo.setProvinceName(StringUtil.getString(rs.getString("province_name"), ""));
					vo.setImei(StringUtil.getString(rs.getString("imei"), ""));
					vo.setImsi(StringUtil.getString(rs.getString("imsi"), ""));
					vo.setMobile(StringUtil.getString(rs.getString("mobile"), ""));
					vo.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					vo.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					vo.setPrice(rs.getFloat("price"));
					vo.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					vo.setSynFlag(rs.getInt("syn_flag"));
					vo.setCreateDate(rs.getString("create_date"));
					list.add(vo);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<DetailDataVo> loadDetailDataBySummer(String table,String date,int spId,int cpId,int spTroneId,int troneId,int sortType,String joinId)
	{
		
		String sql = " SELECT a.`imei`,a.`imsi`,a.`mobile`,";
		sql += " g.`name` province_name,h.`name` city_name,e.`short_name` sp_name,d.`name` sp_trone_name,";
		sql += " b.`price`,f.`short_name` cp_name,a.`syn_flag`,a.`create_date`";
		sql += " FROM daily_log.`tbl_mr_"+ table +"` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` c ON a.`trone_order_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` e ON d.`sp_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` f ON c.`cp_id` = f.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_province` g ON a.`province_id` = g.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_city` h ON a.`city_id` = h.`id`";
		sql += " WHERE a.create_date >= '" + date + " 00:00:00' and a.create_date <= '" + date + " 23:59:59'";
		
		
		if(spId>0)
		{
			sql += " AND e.id = " + spId;
		}
		if(cpId>0)
		{
			sql += " AND f.id = " + cpId;
		}
		if(spTroneId>0)
		{
			sql += " AND d.id = " + spTroneId;
		}
		if(troneId>0)
		{
			sql += " AND b.id = " + troneId;
		}
		
		//sortType 1:天  2:周  3：月  4：SP 5：CP 6：TRONE 7:TRONE_ORDER 8:PROVINCE 9:CITY 10:SP业务,11 小时 
		switch(sortType)
		{
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				sql += " AND e.id = " + joinId;
				break;
			case 5:
				sql += " AND f.id = " + joinId;
				break;
			case 6:
				sql += " AND b.id = " + joinId;
				break;
			case 7:
				sql += " AND c.id = " + joinId;
				break;
			case 8:
				sql += " AND g.id = " + joinId;
				break;
			case 9:
				sql += " AND h.id = " + joinId;
				break;
			case 10:
				sql += " AND d.id = " + joinId;
				break;
			case 11:
				sql += " AND DATE_FORMAT(a.create_date,'%Y-%u-%d-%H') = '" + joinId + "'"; 
				break;
				
			default:
				break;
		}
				 
		sql += " order by a.create_date desc limit 0,1000";
		
		return (List<DetailDataVo>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<DetailDataVo> list = new ArrayList<DetailDataVo>();
				
				while(rs.next())
				{
					DetailDataVo vo = new DetailDataVo();
					vo.setCityName(StringUtil.getString(rs.getString("city_name"), ""));
					vo.setProvinceName(StringUtil.getString(rs.getString("province_name"), ""));
					vo.setImei(StringUtil.getString(rs.getString("imei"), ""));
					vo.setImsi(StringUtil.getString(rs.getString("imsi"), ""));
					vo.setMobile(StringUtil.getString(rs.getString("mobile"), ""));
					vo.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					vo.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					vo.setPrice(rs.getFloat("price"));
					vo.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					vo.setSynFlag(rs.getInt("syn_flag"));
					vo.setCreateDate(rs.getString("create_date"));
					list.add(vo);
				}
				
				return list;
			}
		});
	}
	
	//更新为带参的PARAMS
	@SuppressWarnings("unchecked")
	public List<DetailDataVo> loadDetailDataBySummer(String table,ReportParamsModel params,String joinId)
	{
		
		String sql = " SELECT a.`imei`,a.`imsi`,a.`mobile`,";
		sql += " g.`name` province_name,h.`name` city_name,e.`short_name` sp_name,d.`name` sp_trone_name,";
		sql += " b.`price`,f.`short_name` cp_name,a.`syn_flag`,a.`create_date`";
		sql += " FROM daily_log.`tbl_mr_"+ table +"` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` c ON a.`trone_order_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` e ON d.`sp_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` f ON c.`cp_id` = f.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_province` g ON a.`province_id` = g.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_city` h ON a.`city_id` = h.`id`";
		
		sql += " WHERE a.create_date >= '" + params.getStartDate() + " 00:00:00' and a.create_date <= '" + params.getStartDate() + " 23:59:59'";
		
		
		if(params.getSpId()>0)
		{
			sql += " AND e.id = " + params.getSpId();
		}
		if(params.getCpId()>0)
		{
			sql += " AND f.id = " + params.getCpId();
		}
		if(params.getSpTroneId()>0)
		{
			sql += " AND d.id = " + params.getSpTroneId();
		}
		if(params.getTroneId()>0)
		{
			sql += " AND b.id = " + params.getTroneId();
		}
		if(!StringUtil.isNullOrEmpty(params.getSpCommerceUserId()))
		{
			sql += " AND e.commerce_user_id = " + params.getSpCommerceUserId();
		}
		if(!StringUtil.isNullOrEmpty(params.getCpCommerceUserId()))
		{
			sql += " AND f.commerce_user_id = " + params.getCpCommerceUserId();
		}
		if(params.isOnlyShowSync())
		{
			sql += "AND a.syn_flag = 1";
		}
		
		
		//sortType 1:天  2:周  3：月  4：SP 5：CP 6：TRONE 7:TRONE_ORDER 8:PROVINCE 9:CITY 10:SP业务,11 小时 
		switch(params.getShowType())
		{
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				sql += " AND e.id = " + joinId;
				break;
			case 5:
				sql += " AND f.id = " + joinId;
				break;
			case 6:
				sql += " AND b.id = " + joinId;
				break;
			case 7:
				sql += " AND c.id = " + joinId;
				break;
			case 8:
				sql += " AND g.id = " + joinId;
				break;
			case 9:
				sql += " AND h.id = " + joinId;
				break;
			case 10:
				sql += " AND d.id = " + joinId;
				break;
			case 11:
				sql += " AND DATE_FORMAT(a.create_date,'%Y-%u-%d-%H') = '" + joinId + "'"; 
				break;
				
			case 18:
				sql += " AND c.cp_jiesuanlv_id = " + joinId;
				break;
				
			default:
				break;
		}
				 
		sql += " order by a.create_date desc limit 0,1000";
		
		return (List<DetailDataVo>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<DetailDataVo> list = new ArrayList<DetailDataVo>();
				
				while(rs.next())
				{
					DetailDataVo vo = new DetailDataVo();
					vo.setCityName(StringUtil.getString(rs.getString("city_name"), ""));
					vo.setProvinceName(StringUtil.getString(rs.getString("province_name"), ""));
					vo.setImei(StringUtil.getString(rs.getString("imei"), ""));
					vo.setImsi(StringUtil.getString(rs.getString("imsi"), ""));
					vo.setMobile(StringUtil.getString(rs.getString("mobile"), ""));
					vo.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					vo.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					vo.setPrice(rs.getFloat("price"));
					vo.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					vo.setSynFlag(rs.getInt("syn_flag"));
					vo.setCreateDate(rs.getString("create_date"));
					list.add(vo);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<DetailDataVo> loadDetailDataByCondition(String table,String startDate,String endDate,int spId,int cpId,int spTroneId,int synType)
	{
		
		String sql = " SELECT a.`imei`,a.`imsi`,a.`mobile`,b.orders,b.trone_num,a.id,";
		sql += " g.`name` province_name,h.`name` city_name,e.`short_name` sp_name,d.`name` sp_trone_name,";
		sql += " b.`price`,f.`short_name` cp_name,a.`syn_flag`,a.`create_date`";
		sql += " FROM daily_log.`tbl_mr_"+ table +"` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` c ON a.`trone_order_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` e ON d.`sp_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` f ON c.`cp_id` = f.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_province` g ON a.`province_id` = g.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_city` h ON a.`city_id` = h.`id`";
		sql += " WHERE a.create_date >= '" + startDate + "' and a.create_date <= '" + endDate + "'";
		
		
		if(spId>0)
		{
			sql += " AND e.id = " + spId;
		}
		if(cpId>0)
		{
			sql += " AND f.id = " + cpId;
		}
		if(spTroneId>0)
		{
			sql += " AND d.id = " + spTroneId;
		}
		if(synType>=0)
		{
			sql += " AND a.syn_flag = " + synType;
		}
		
		sql += " order by a.create_date desc limit 0,1000";
		
		return (List<DetailDataVo>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<DetailDataVo> list = new ArrayList<DetailDataVo>();
				
				while(rs.next())
				{
					DetailDataVo vo = new DetailDataVo();
					vo.setCityName(StringUtil.getString(rs.getString("city_name"), ""));
					vo.setProvinceName(StringUtil.getString(rs.getString("province_name"), ""));
					vo.setImei(StringUtil.getString(rs.getString("imei"), ""));
					vo.setImsi(StringUtil.getString(rs.getString("imsi"), ""));
					vo.setMobile(StringUtil.getString(rs.getString("mobile"), ""));
					vo.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					vo.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					vo.setPrice(rs.getFloat("price"));
					vo.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					vo.setSynFlag(rs.getInt("syn_flag"));
					vo.setCreateDate(rs.getString("create_date"));
					vo.setId(rs.getInt("id"));
					vo.setOrder(StringUtil.getString(rs.getString("orders"), ""));
					vo.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					list.add(vo);
				}
				
				return list;
			}
		});
	}
	
	public static void main(String[] args)
	{
		ReportParamsModel model = new ReportParamsModel();
		System.out.println(model.isOnlyShowSync());
	}
}	
