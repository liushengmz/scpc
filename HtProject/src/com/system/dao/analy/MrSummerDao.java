package com.system.dao.analy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.analy.MrSummerModel;

public class MrSummerDao 
{
	@SuppressWarnings("unchecked")
	public ArrayList<MrSummerModel> loadMrSummer(String tableName,String startDate,String endDate)
	{
		
		String sql = "select mr_date,sp_id,b.id trone_id,cp_id,trone_order_id,mcc,province_id,"
				+ "city_id,count(*) data_rows from daily_log.tbl_mr_"+ tableName +" a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b "
				+ "on a.trone_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order c on c.trone_id = b.id "
				+ "where mr_date >= '" + startDate + "' and mr_date <= '" + endDate + "' "
				+ "group by mr_date,sp_id,b.id,cp_id,trone_order_id,mcc,province_id,city_id";
		
		return (ArrayList<MrSummerModel>)new JdbcControl().query(sql, new QueryCallBack() 
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException 
			{
				ArrayList<MrSummerModel> list = new ArrayList<MrSummerModel>();
				
				while(rs.next())
				{
					MrSummerModel model = new MrSummerModel();
					
					model.setFeeDate(rs.getString("mr_date"));
					model.setSpId(rs.getInt("sp_id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					model.setMcc(rs.getString("mcc"));
					model.setProvinceId(rs.getInt("province_id"));
					model.setCityId(rs.getInt("city_id"));
					model.setDataRows(rs.getInt("data_rows"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	/**
	 * 写入MR SUMMER数据之前确保相同的数据已经删除
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean deleteMrSummer(String startDate,String endDate)
	{
		String sql  = "delete from daily_log.tbl_mr_summer where mr_date >= '" + startDate + "' and mr_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	
	
	/**
	 * 在指定时间把昨天的数据分析到SUMMER表
	 */
	public boolean analyMrToSummer(String tableName,String startDate,String endDate)
	{
		String sql  = " insert into daily_log.tbl_mr_summer(mr_date,sp_id,trone_id,"
				+ " cp_id,trone_order_id,mcc,province_id,city_id,data_rows,amount)"
				+ " select mr_date,a.sp_id,trone_id,cp_id,trone_order_id,mcc,province_id,"
				+ " city_id,count(*) data_rows,sum(b.price) from daily_log.tbl_mr_"+ tableName +" a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b "
				+ " on a.trone_id = b.id "
				+ " where mr_date >= '" + startDate + "' and mr_date <= '" + endDate + "' AND a.trone_id>0 AND trone_order_id>0"
				+ " group by mr_date,a.sp_id,b.id,cp_id,trone_order_id,mcc,province_id,city_id "
				+ " ORDER BY mr_date,a.sp_id,b.id,cp_id,trone_order_id,mcc,province_id,city_id";
		
		return new JdbcControl().execute(sql);
	}
	
}
