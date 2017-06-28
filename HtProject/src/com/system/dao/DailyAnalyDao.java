package com.system.dao;

import com.system.database.JdbcControl;

public class DailyAnalyDao
{
	public boolean analyDailyMr(String date,String dateMonth)
	{
		StringBuffer sb = new StringBuffer(1024);
		
		JdbcControl control = new JdbcControl();
		
		sb.append("DELETE FROM  daily_log.tbl_mr_summer WHERE mr_date >= '" + date + "' AND mr_date <= '" 
		+ date + "'   AND record_type IN (0, 2);");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("DELETE  FROM daily_log.tbl_cp_mr_summer  WHERE mr_date >= '" + date + "' AND mr_date <= '" 
		+ date + "' AND record_type IN (0, 2);");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("DELETE FROM  daily_log.tbl_third_pay_mr_summer WHERE mr_date >= '" + date + "' AND mr_date <= '" 
				+ date + "'   AND record_type = 3;");
				
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("DELETE  FROM daily_log.tbl_third_pay_cp_mr_summer  WHERE mr_date >= '" + date + "' AND mr_date <= '" 
		+ date + "' AND record_type = 3;");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("INSERT INTO daily_log.tbl_mr_summer (mr_date,sp_id,trone_id,cp_id,trone_order_id,mcc,province_id,city_id,data_rows,amount) ");
		sb.append(" SELECT  mr_date, a.sp_id, trone_id, cp_id, trone_order_id, mcc, province_id, city_id, COUNT(*) data_rows, SUM(b.price) amount ");
		sb.append(" FROM daily_log.tbl_mr_" + dateMonth + " a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b ON a.trone_id = b.id");
		sb.append(" WHERE mr_date >= '" + date + "' AND mr_date <= '" + date + "'  AND a.trone_id > 0  AND trone_order_id > 0  AND trone_type = 0 ");
		sb.append(" GROUP BY mr_date,a.sp_id,b.id,cp_id,trone_order_id,mcc,province_id,city_id ");
		sb.append(" ORDER BY mr_date,a.sp_id,b.id,cp_id,trone_order_id,mcc,province_id,city_id;");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("INSERT INTO daily_log.tbl_mr_summer (mr_date,sp_id, trone_id,cp_id,trone_order_id,mcc,province_id,city_id,data_rows,amount, record_type) ");
		sb.append(" SELECT mr_date, a.sp_id, trone_id, cp_id,trone_order_id,mcc,province_id,city_id,COUNT(*) data_rows,SUM(b.price * a.ivr_time) amount,2 record_type ");
		sb.append(" FROM daily_log.tbl_mr_" + dateMonth + " a  LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b  ON a.trone_id = b.id");
		sb.append(" WHERE mr_date >= '" + date + "' AND mr_date <= '" + date + "' AND a.trone_id > 0  AND trone_order_id > 0 AND trone_type = 2 ");
		sb.append(" GROUP BY mr_date,a.sp_id,b.id,cp_id,trone_order_id,mcc,province_id,city_id");
		sb.append(" ORDER BY mr_date,a.sp_id,b.id,cp_id,trone_order_id,mcc,province_id,city_id;");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("INSERT INTO daily_log.tbl_cp_mr_summer (mr_date,cp_id,trone_order_id,mcc,province_id,city_id,data_rows,amount) ");
		sb.append(" SELECT a.mr_date,b.cp_id,b.id trone_order_id,a.mcc,province_id,city_id,COUNT(*) data_rows, SUM(c.price) amount ");
		sb.append(" FROM daily_log.tbl_cp_mr_" + dateMonth + " a   LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b   ON a.trone_order_id = b.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c  ON b.trone_id = c.id ");
		sb.append(" WHERE a.mr_date >= '" + date + "' AND a.mr_date <= '" + date + "' AND trone_type = 0 ");
		sb.append(" GROUP BY mr_date,cp_id,trone_order_id,mcc,province_id,city_id ORDER BY mr_date ASC;");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("INSERT INTO daily_log.tbl_cp_mr_summer (mr_date,cp_id,trone_order_id,mcc,province_id,city_id,data_rows,amount,record_type) ");
		sb.append(" SELECT a.mr_date,b.cp_id,b.id trone_order_id,a.mcc,province_id,city_id,COUNT(*) data_rows,SUM(c.price * a.ivr_time) amount,2 ");
		sb.append(" FROM daily_log.tbl_cp_mr_" + dateMonth + " a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.trone_order_id = b.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c  ON b.trone_id = c.id");
		sb.append(" WHERE a.mr_date >= '" + date + "'  AND a.mr_date <= '" + date + "'  AND trone_type = 2");
		sb.append(" GROUP BY mr_date, cp_id, trone_order_id, mcc,  province_id, city_id ORDER BY mr_date ASC;");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("INSERT INTO daily_log.`tbl_third_pay_mr_summer`(sp_id,cp_id,mcc,province_id,city_id,trone_id,trone_order_id,mr_date,data_rows,amount,record_type)");
		sb.append(" SELECT b.sp_id,b.cp_id,460,32,416,b.trone_id,b.trone_order_id,DATE_FORMAT(a.`createdate`,'%Y-%m-%d') mr_date,COUNT(*) data_rows,SUM(amount)/100 amount,3 FROM game_log.`tbl_xypay_" + dateMonth + "` a");
		sb.append(" LEFT JOIN");
		sb.append(" (");
		sb.append(" SELECT a.id trone_order_id,a.`order_num` appkey,b.`orders` pay_type,c.`sp_id`,a.cp_id,b.id trone_id  FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`");
		sb.append(" WHERE c.`trone_type` = 3");
		sb.append(" )");
		sb.append(" b ON a.`appkey` = b.appkey AND a.`oprator` = b.pay_type");
		sb.append(" WHERE a.oprator >= 4 AND a.`createdate` >= '" + date + " 00:00:00' AND a.`createdate` <= '" + date + " 23:59:59' AND trone_order_id >0 ");
		sb.append(" GROUP BY b.trone_order_id;");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		sb.append("INSERT INTO daily_log.`tbl_third_pay_cp_mr_summer`(cp_id,mcc,province_id,city_id,trone_order_id,mr_date,data_rows,amount,record_type)");
		sb.append(" SELECT b.cp_id,460,32,416,b.trone_order_id,DATE_FORMAT(a.`createdate`,'%Y-%m-%d') mr_date,COUNT(*) data_rows,SUM(amount)/100 amount,3 FROM game_log.`tbl_xypay_" + dateMonth + "` a");
		sb.append(" LEFT JOIN");
		sb.append(" (");
		sb.append(" SELECT a.id trone_order_id,a.`order_num` appkey,b.`orders` pay_type,c.`sp_id`,a.cp_id,b.id trone_id  FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`");
		sb.append(" WHERE c.`trone_type` = 3");
		sb.append(" )");
		sb.append(" b ON a.`appkey` = b.appkey AND a.`oprator` = b.pay_type");
		sb.append(" WHERE a.oprator >= 4 AND a.`createdate` >= '" + date + " 00:00:00' AND a.`createdate` <= '" + date + " 23:59:59' AND trone_order_id >0 ");
		sb.append(" GROUP BY b.trone_order_id;");
		
		control.execute(sb.toString());
		sb.delete(0,sb.length());
		
		return true;
	}
	
	public static void main(String[] args)
	{
		System.out.println(new DailyAnalyDao().analyDailyMr("2016-05-05", "201605"));
	}
	
}
