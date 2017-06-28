package com.system.dao.analy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.analy.MrModel;


public class MrDao
{
	@SuppressWarnings("unchecked")
	public ArrayList<MrModel> loadMrData(int cpId,String tableName,String startDate,String endDate)
	{
		String sql = "select * from daily_log.tbl_mr_" + tableName + " a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b "
					+ "on a.trone_order_id = b.id where b.cp_id = " + cpId + " and mr_date >= '" + startDate + "' and mr_date<= '" + endDate + "'";
		
		return (ArrayList<MrModel>)new JdbcControl().query(sql, new QueryCallBack() 
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException 
			{
				ArrayList<MrModel> mrList = new ArrayList<MrModel>();
				
				MrModel model = null;
				
				while(rs.next())
				{
					model = new MrModel();
					
					model.setMobile(rs.getString("mobile"));
					model.setProvinceId(rs.getInt("province_id"));
					model.setCityId(rs.getInt("city_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					model.setOriOrder(rs.getString("ori_order"));
					model.setOriTrone(rs.getString("ori_trone"));
					model.setLinkId(rs.getString("linkid"));
					model.setCpParams(rs.getString("cp_param"));
					model.setServiceCode(rs.getString("service_code"));
					model.setPrice(rs.getInt("price"));
					model.setMrDate(rs.getString("mr_date"));
					model.setId(rs.getInt("id"));
					
					mrList.add(model);
				}
				
				return mrList;
			}
		});
	}
	
	public boolean updateMrflag(String tableName,ArrayList<Integer> list)
	{
		String sql = "update daily_log.tbl_mr_" + tableName + " set syn_flag = 0 where id = ?";
		
		List<Map<Integer, Object>> dataParams = new ArrayList<Map<Integer, Object>>();
		
		for(int id : list)
		{
			Map<Integer,Object> param = new HashMap<Integer, Object>();
			param.put(1, id);
			dataParams.add(param);
		}
		
		return new JdbcControl().executeMulData(sql, dataParams);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
