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
		String sql = "delete daily_log.a from daily_log.tbl_cp_mr_201509 a," + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b"
				+ " where a.trone_order_id = b.id and b.cp_id = 2  and a.mr_date = '" + mrDate + "'";
		return new JdbcControl().execute(sql);
	}
	
	public boolean deleteCpMrSummer(String startDate,String endDate)
	{
		String sql = "DELETE FROM daily_log.tbl_cp_mr_summer WHERE mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "'";
		return new JdbcControl().execute(sql);
	}
	
	
	public boolean analyCpMrToCpMrSummer(String tableName,String startDate,String endDate)
	{
		String sql  = "insert into daily_log.tbl_cp_mr_summer(mr_date,cp_id,trone_order_id,"
				+ "mcc,province_id,city_id,data_rows,amount) select a.mr_date,b.cp_id,"
				+ "b.id trone_order_id,a.mcc,province_id,city_id,count(*) data_rows,sum(c.price) amount "
				+ "from daily_log.tbl_cp_mr_" + tableName + " a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b "
				+ "on a.trone_order_id = b.id  left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c "
				+ "on b.trone_id = c.id where a.mr_date >= '" + startDate + "' "
				+ "and a.mr_date <= '" + endDate + "' group by mr_date,cp_id, trone_order_id,"
				+ "mcc,province_id,city_id order by mr_date asc";
		
		return new JdbcControl().execute(sql);
	}
	
	
	/* 这个方法不需要再用到了
	public boolean updateCpMrSummerAmount(String startDate,String endDate)
	{
		String sql = "update daily_log.tbl_cp_mr_summer a "
				+ "left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b on a.trone_order_id = b.id  "
				+ "left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on b.trone_id = c.id set amount = data_rows*c.price "
				+ "where a.mr_date >= '" + startDate + "' and a.mr_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	*/
	
}
