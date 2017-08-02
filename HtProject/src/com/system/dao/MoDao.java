package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.ReportMoSummer;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;
import com.system.vo.DetailDataVo;

public class MoDao
{
	@SuppressWarnings("unchecked")
	public List<DetailDataVo> loadMoDetail(String table,String startDate,String endDate,int spId,int spTroneId,String keyWord)
	{
		keyWord = SqlUtil.sqlEncode(keyWord);
		
		String sql = " SELECT a.linkid,a.id,a.imei,a.imsi,a.mobile,h.short_name cp_name,f.name province_name,e.name city_name,d.short_name sp_name,c.name sp_trone_name,b.trone_name,b.price,";
		
		sql += "  b.orders config_order,b.trone_num config_trone,a.ori_order,a.ori_trone,a.create_date";
		sql += "  FROM " + Constant.DB_DAILY_LOG + ".tbl_mo_" + table + " a";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_trone b ON a.trone_id = b.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c ON b.sp_trone_id = c.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.sp_id = d.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_city e ON a.city_id = e.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_province f ON e.province_id = f.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_trone_order g ON a.trone_order_id = g.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_cp h ON g.cp_id = h.id";
		sql += " where a.create_date >= '" + startDate + "' and a.create_date <= '" + endDate + "'";
		
		if(spId>0)
		{
			sql += " and d.id = " + spId;
		}
		
		if(spTroneId>0)
		{
			sql += " and c.id = " + spTroneId;
		}
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (a.ori_order LIKE '%" + keyWord + "%' OR a.ori_trone LIKE '%" + 
					keyWord + "%' or b.orders like '%" + keyWord + "%' or b.trone_num like '%" + 
					keyWord + "%' or a.mobile like '%" + keyWord + "%' or a.linkid like '%" + 
					keyWord + "%' or d.full_name like '%" + keyWord + "%' or c.name like '%" + 
					keyWord + "%' or f.full_name like '%" + keyWord + "%') ";
		}
		
		sql += "  ORDER BY a.id DESC LIMIT 1000";
		
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
					vo.setCreateDate(rs.getString("create_date"));
					vo.setId(rs.getInt("id"));
					vo.setOrder(StringUtil.getString(rs.getString("ori_order"), ""));
					vo.setTroneNum(StringUtil.getString(rs.getString("ori_trone"), ""));
					vo.setConfigOrder(StringUtil.getString(rs.getString("config_order"), ""));
					vo.setConfigTrone(StringUtil.getString(rs.getString("config_trone"), ""));
					vo.setLinkId(StringUtil.getString(rs.getString("linkid"), ""));
					vo.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					list.add(vo);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportMoSummer> loadMoSummer(String table,String startDate,String endDate,String keyWord)
	{
		String sql = "";
		sql += " SELECT h.short_name cp_name,g.short_name sp_name,e.trone_name,f.name sp_trone_name,"
				+ "e.price,a.trone_order_id,mo_data_rows,mr_data_rows,syn_mr_data_rows,e.orders trone_orders,e.trone_num,d.order_num cp_trone_order";
		sql += " FROM";
		sql += " (";
		sql += " SELECT trone_order_id,COUNT(*) mo_data_rows";
		sql += " FROM " + Constant.DB_DAILY_LOG + ".tbl_mo_" + table;
		sql += " WHERE create_date >= '" + startDate + "' AND create_date < '" + endDate + "'";
		sql += " GROUP BY trone_order_id";
		sql += " ) a LEFT JOIN";
		sql += " (";
		sql += " SELECT trone_order_id,COUNT(*) mr_data_rows";
		sql += " FROM " + Constant.DB_DAILY_LOG + ".tbl_mo_" + table;
		sql += " WHERE create_date >= '" + startDate + "' AND create_date < '" + endDate + "'";
		sql += " AND report_flag = 1";
		sql += " GROUP BY trone_order_id";
		sql += " )b ON a.trone_order_id = b.trone_order_id";
		sql += " LEFT JOIN";
		sql += " (";
		sql += " SELECT trone_order_id,COUNT(*) syn_mr_data_rows";
		sql += " FROM " + Constant.DB_DAILY_LOG + ".tbl_mo_" + table;
		sql += " WHERE create_date >= '" + startDate + "' AND create_date < '" + endDate + "'";
		sql += " AND report_flag = 1 AND syn_flag = 1";
		sql += " GROUP BY trone_order_id";
		sql += " )c ON a.trone_order_id = c.trone_order_id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_trone_order d ON a.trone_order_id =d.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_trone e ON d.trone_id = e.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp_trone f ON e.sp_trone_id = f.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp g ON f.sp_id = g.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_cp h ON d.cp_id = h.id";
		sql += " WHERE 1=1 ";
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (d.order_num LIKE '%" + keyWord + "%' OR f.name LIKE '%" + 
					keyWord + "%' or e.orders like '%" + keyWord + "%' or e.trone_num like '%" + 
					keyWord + "%' or e.trone_name like '%" + keyWord + "%' or h.full_name like '%" + 
					keyWord + "%' or h.short_name like '%" + keyWord + "%' or g.short_name like '%" + 
					keyWord + "%' or g.full_name like '%" + keyWord + "%') ";
		}
		
		sql += " ORDER BY cp_name,sp_name,sp_trone_name,price;";
		
		
		return (List<ReportMoSummer>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ReportMoSummer> list = new ArrayList<ReportMoSummer>();
				
				while(rs.next())
				{
					ReportMoSummer mo = new ReportMoSummer();
					mo.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					mo.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					mo.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					mo.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					mo.setPrice(rs.getFloat("price"));
					mo.setMoDataRows(rs.getInt("mo_data_rows"));
					mo.setMrDataRows(rs.getInt("mr_data_rows"));
					mo.setSynMrDataRows(rs.getInt("syn_mr_data_rows"));
					mo.setTroneOrders(StringUtil.getString(rs.getString("trone_orders"), ""));
					mo.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					mo.setCpTroneOrders(StringUtil.getString(rs.getString("cp_trone_order"), ""));
					list.add(mo);
				}
				
				return list;
			}
		});
		

	}
	
	
	
}
