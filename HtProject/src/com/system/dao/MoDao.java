package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;
import com.system.vo.DetailDataVo;

public class MoDao
{
	@SuppressWarnings("unchecked")
	public List<DetailDataVo> loadMoDetail(String table,String startDate,String endDate,int spId,int spTroneId,String keyWord)
	{
		keyWord = SqlUtil.sqlEncode(keyWord);
		
		String sql = " SELECT a.linkid,a.id,a.imei,a.imsi,a.mobile,f.name province_name,e.name city_name,d.short_name sp_name,c.name sp_trone_name,b.trone_name,b.price,";
		
		sql += "  b.orders config_order,b.trone_num config_trone,a.ori_order,a.ori_trone,a.create_date";
		sql += "  FROM " + Constant.DB_DAILY_LOG + ".tbl_mo_" + table + " a";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_trone b ON a.trone_id = b.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c ON b.sp_trone_id = c.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.sp_id = d.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_city e ON a.city_id = e.id";
		sql += "  LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_province f ON e.province_id = f.id";
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
			sql += " AND (a.ori_order LIKE '%" + keyWord + "%' OR a.ori_trone LIKE '%" + keyWord + "%' or b.orders like '%" + keyWord + "%' or b.trone_num like '%" + keyWord + "%') ";
		}
		
		sql += "  ORDER BY a.id DESC LIMIT 100";
		
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
					list.add(vo);
				}
				
				return list;
			}
		});
	}
}
