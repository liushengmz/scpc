package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.database.JdbcGameControl;
import com.system.database.QueryCallBack;
import com.system.model.MrReportModel;
import com.system.model.ThirdPayModel;
import com.system.util.StringUtil;

public class ThirdPayDao
{
	public Map<String, Object> getThirdPayData(String startDate, String endDate,int dataType)
	{
		//判断数值
		String payType=""; //参数为支付类型
		if(dataType == 0){
			payType = "alipay";
		}else if(dataType == 1){
			payType = "unionpay";
		}else if(dataType == 2){
			payType = "wechatpay";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		
//		String query = "";
//		
//		if(dataType>-1)
//			query+= " and a.record_type = " + dataType;
		
		String sql = "select FROM_UNIXTIME(id/1000/1000000, '%Y-%m-%d') AS dt ,price,payChannelOrderId,payChannel,ip,releaseChannel,appKey"
				+ " from `log_third_pay`.`log_success_pays` "
				+ " where 1=1 ";
		if(dataType>-1)
			sql+= " and payChannel = " + "'"+payType+"'";
		
		if(!StringUtil.isNullOrEmpty(startDate))
			sql += " and id>UNIX_TIMESTAMP('"+startDate+"')*1000*1000000 ";
		
		if(!StringUtil.isNullOrEmpty(endDate))
			sql += " and id<=UNIX_TIMESTAMP('"+endDate+"')*1000*1000000 ";
		
		sql+="order by id desc";  //倒叙排序
		
//		String sql = "select a.show_title,aa,bb,cc,dd from (";
//		sql += " select  " + joinId + " join_id," + queryParams + " show_title,sum(a.data_rows) aa,sum(a.amount) bb";
//		sql += " from daily_log.tbl_mr_summer a";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b on a.trone_order_id = b.id ";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on b.trone_id = c.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d on c.sp_id = d.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp e on b.cp_id = e.id ";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_province f on a.province_id = f.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_city g on a.city_id = g.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone h on c.sp_trone_id = h.id";
//		sql += " where a.mr_date >= '" + startDate + "' and a.mr_date <= '" + endDate + "' " + query;
//		sql += " group by join_id order by show_title asc )a";
//		sql += " left join(";
//		sql += " select  " + joinId + " join_id," + queryParams + " show_title,sum(a.data_rows) cc,sum(a.amount) dd";
//		sql += " from daily_log.tbl_cp_mr_summer a ";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b on a.trone_order_id = b.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on b.trone_id = c.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d on c.sp_id = d.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp e on a.cp_id = e.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_province f on a.province_id = f.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_city g on a.city_id = g.id";
//		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone h on c.sp_trone_id = h.id";
//		sql += " where a.mr_date >= '" + startDate + "' and a.mr_date <= '" + endDate + "' " + query;
//		sql += " group by join_id order by show_title asc";
//		sql += " )b on a.join_id = b.join_id;";
		
		JdbcGameControl control = new JdbcGameControl();
		
		final List<Object> datalist = new ArrayList<Object>();
		
		map.put("list", control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ThirdPayModel> list = new ArrayList<ThirdPayModel>();
				int dataRows=0,showDataRows = 0;
				float amount=0,showAmount = 0;
				while(rs.next())
				{
					ThirdPayModel model = new ThirdPayModel();
					
					model.setTimeId(rs.getString("dt"));
					model.setPrice(rs.getInt("price"));
					model.setPayChannelOrderId(rs.getString("payChannelOrderId"));
					model.setPayChannel(rs.getString("payChannel"));
					model.setIp(rs.getString("ip"));
					model.setReleaseChannel(rs.getString("releaseChannel"));
					model.setAppKey(rs.getString("appKey"));
					
					list.add(model);
				}
				
				datalist.add(dataRows);
				datalist.add(showDataRows);
				datalist.add(amount);
				datalist.add(showAmount);
				
				return list;
			}
		}));
		
		map.put("datarows", datalist.get(0));
		map.put("showdatarows", datalist.get(1));
		map.put("amount", datalist.get(2));
		map.put("showamount", datalist.get(3));
		
		return map;
	}
}
