package com.system.dao.analy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.model.analy.MrModel;

public class CpMrDao 
{
	public boolean insertCpMr(String tableName,List<MrModel> list)
	{
		String sql = "insert into daily_log.tbl_cp_mr_" + tableName + " (mobile,mcc,"
				+ "province_id,city_id,trone_order_id,ori_trone,ori_order,linkid,"
				+ "cp_param,service_code,price,ip,mr_date,mr_table,mr_id) "
				+ "value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		List<Map<Integer,Object>> dataParams = new ArrayList<Map<Integer,Object>>();
		
		for(MrModel model : list)
		{
			if(model.isSynToCp())
			{
				Map<Integer,Object> param = new HashMap<Integer, Object>();
				param.put(1, model.getMobile());
				param.put(2, "460");
				param.put(3, model.getProvinceId());
				param.put(4, model.getCityId());
				param.put(5, model.getTroneId());
				param.put(6, model.getOriTrone());
				param.put(7, model.getOriOrder());
				param.put(8, model.getLinkId());
				param.put(9, model.getCpParams());
				param.put(10, model.getServiceCode());
				param.put(11, model.getPrice());
				param.put(12, model.getIp());
				param.put(13, model.getMrDate());
				param.put(14, tableName);
				param.put(15, model.getId());
				
				dataParams.add(param);
			}
		}
		
		return new JdbcControl().executeMulData(sql, dataParams);
	}
	
	public boolean deleteCpMr(String tableName,int cpId,String mrDate)
	{
		String sql = "delete daily_log.a from daily_log.tbl_cp_mr_201509 a,daily_config.tbl_trone_order b"
				+ " where a.trone_order_id = b.id and b.cp_id = 2  and a.mr_date = '" + mrDate + "'";
		return new JdbcControl().execute(sql);
	}
	
	public boolean deleteCpMrSummer(String startDate,String endDate)
	{
		String sql = "DELETE FROM daily_log.tbl_cp_mr_summer WHERE mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "' and record_type in( 0,2,3)";
		return new JdbcControl().execute(sql);
	}
	
	
	public boolean analyCpMrToCpMrSummer(String tableName,String startDate,String endDate)
	{
		String sql  = "insert into daily_log.tbl_cp_mr_summer(mr_date,cp_id,trone_order_id,"
				+ "mcc,province_id,city_id,data_rows,amount) select a.mr_date,b.cp_id,"
				+ "b.id trone_order_id,a.mcc,province_id,city_id,count(*) data_rows,sum(c.price) amount "
				+ "from daily_log.tbl_cp_mr_" + tableName + " a left join daily_config.tbl_trone_order b "
				+ "on a.trone_order_id = b.id  left join daily_config.tbl_trone c "
				+ "on b.trone_id = c.id where a.mr_date >= '" + startDate + "' "
				+ "and a.mr_date <= '" + endDate + "' AND trone_type = 0 group by mr_date,cp_id, trone_order_id,"
				+ "mcc,province_id,city_id order by mr_date asc";
		
		return new JdbcControl().execute(sql);
	}
	
	public boolean analyIvrCpMrToCpMrSummer(String tableName,String startDate,String endDate)
	{
		String sql  = "insert into daily_log.tbl_cp_mr_summer(mr_date,cp_id,trone_order_id,"
				+ "mcc,province_id,city_id,data_rows,amount,record_type) select a.mr_date,b.cp_id,"
				+ "b.id trone_order_id,a.mcc,province_id,city_id,count(*) data_rows,sum(c.price*a.ivr_time) amount,2 "
				+ "from daily_log.tbl_cp_mr_" + tableName + " a left join daily_config.tbl_trone_order b "
				+ "on a.trone_order_id = b.id  left join daily_config.tbl_trone c "
				+ "on b.trone_id = c.id where a.mr_date >= '" + startDate + "' "
				+ "and a.mr_date <= '" + endDate + "' AND trone_type = 2 group by mr_date,cp_id, trone_order_id,"
				+ "mcc,province_id,city_id order by mr_date asc";
		
		return new JdbcControl().execute(sql);
	}
	
	//here 
	public boolean analyThirdPayToCpMrSummer(String tableName,String startDate,String endDate)
	{
		String sql = "INSERT INTO daily_log.`tbl_third_pay_cp_mr_summer`(cp_id,mcc,province_id,city_id,trone_order_id,mr_date,data_rows,amount,record_type)";
		
		sql += " SELECT * ";
		sql += " FROM ";
		sql += " ( ";
		sql += " SELECT b.cp_id,460,32,416,b.trone_order_id,mr_date,data_rows,amount/100 amount,3  ";
		sql += " FROM ";
		sql += " ( ";
		sql += " SELECT appkey,oprator pay_type,SUM(amount) amount,DATE_FORMAT(a.`createdate`,'%Y-%m-%d') mr_date,COUNT(*) data_rows  ";
		sql += " FROM game_log.`tbl_xypay_" + tableName + "` a  ";
		sql += " WHERE a.`createdate` >= '" + startDate + " 00:00:00'  ";
		sql += " AND a.`createdate` <= '" + endDate + " 23:59:59' ";
		sql += " GROUP BY appkey,oprator ";
		sql += " ) a LEFT JOIN ";
		sql += " ( ";
		sql += " SELECT a.id trone_order_id,a.`order_num` appkey,b.`orders` pay_type,c.`sp_id`,a.cp_id,b.id trone_id   ";
		sql += " FROM daily_config.tbl_trone_order a  ";
		sql += " LEFT JOIN daily_config.`tbl_trone` b ON a.`trone_id` = b.`id`  ";
		sql += " LEFT JOIN daily_config.`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`  ";
		sql += " WHERE c.`trone_type` = 3  ";
		sql += " )b ";
		sql += " ON a.appkey = b.appkey AND a.pay_type = b.pay_type ";
		sql += " ) a WHERE trone_order_id IS NOT NULL ";
		
		return new JdbcControl().execute(sql);
	}
	
	
	/* 这个方法不需要再用到了
	public boolean updateCpMrSummerAmount(String startDate,String endDate)
	{
		String sql = "update daily_log.tbl_cp_mr_summer a "
				+ "left join daily_config.tbl_trone_order b on a.trone_order_id = b.id  "
				+ "left join daily_config.tbl_trone c on b.trone_id = c.id set amount = data_rows*c.price "
				+ "where a.mr_date >= '" + startDate + "' and a.mr_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	*/
	
}
