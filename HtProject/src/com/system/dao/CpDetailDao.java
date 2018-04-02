package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.util.StringUtil;
import com.system.vo.DetailDataVo;

public class CpDetailDao
{
	@SuppressWarnings("unchecked")
	public Map<String, Object> loadDetailDataByMsg(int pageIndex,int userId,String tableName,String startDate,String endDate,int type,String keyWord)
	{
		List<DetailDataVo> list = null;
		
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
		
		String sql = " SELECT " + com.system.constant.Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM daily_log.`tbl_mr_"+ tableName +"` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` c ON a.`trone_order_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` e ON d.`sp_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` f ON c.`cp_id` = f.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_province` g ON a.`province_id` = g.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_city` h ON a.`city_id` = h.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` i ON f.`user_id` = i.`id`";
		sql += " WHERE 1=1 AND a.syn_flag = 1 AND i.id = " + userId + " AND a.create_date >= '" + startDate + "' AND a.create_date <= '" + endDate + "'";
		
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND " + chkType + " = '" + keyWord + "'";
		}
		
		Map<String, Object> map = new  HashMap<String, Object>();
		
		map.put("rows", new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				return 0;
			}
		}));
		
		String query = "a.linkid,a.ori_order,a.`imei`,a.`imsi`,a.`mobile`,g.`name` province_name,h.`name` city_name,e.`short_name` sp_name,d.`name` sp_trone_name,b.`price`,f.`short_name` cp_name,a.`syn_flag`,a.`create_date`";
		
		String order = " order by  a.`create_date` desc";
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		list = (List<DetailDataVo>)new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + order + limit, new QueryCallBack()
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
					vo.setLinkId(StringUtil.getString(rs.getString("linkid"), ""));
					vo.setOrder(StringUtil.getString(rs.getString("ori_order"), ""));
					
					list.add(vo);
				}
				
				return list;
			}
		});
		
		map.put("list", list);
		
		return map;
	}
}
