package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SummerDataModel;

public class SummerDataDao
{
	@SuppressWarnings("unchecked")
	public List<SummerDataModel> loadMrSummerByTroneIds(String db,String troneOrderIds,String mrDate)
	{
		String sql = " SELECT a.trone_order_id,mr_data_rows,cp_mr_data_rows";
		sql += " FROM";
		sql += " (";
		sql += "	SELECT trone_order_id,SUM(data_rows) mr_data_rows FROM " + db + ".tbl_mr_summer_2 ";
		sql += "	WHERE trone_order_id IN (" + troneOrderIds + ") AND mr_date = '" + mrDate + "'";
		sql += "	GROUP BY mr_date,trone_order_id";
		sql += " )a";
		sql += " LEFT JOIN";
		sql += " (";
		sql += "	SELECT trone_order_id,SUM(data_rows) cp_mr_data_rows FROM " + db + ".tbl_cp_mr_summer_2 ";
		sql += "	WHERE trone_order_id IN (" + troneOrderIds + ") AND mr_date = '" + mrDate + "'";
		sql += "	GROUP BY mr_date,trone_order_id";
		sql += " )b";
		sql += " ON a.trone_order_id = b.trone_order_id;";
		
		return (List<SummerDataModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SummerDataModel> list = new ArrayList<SummerDataModel>();
				
				while(rs.next())
				{
					SummerDataModel model = new SummerDataModel();
					model.setCpMrDataRows(rs.getInt("cp_mr_data_rows"));
					model.setMrDataRows(rs.getInt("mr_data_rows"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					list.add(model);
				}
				
				return list;
			}
		});
		
	}
}
