package com.system.dao.yhxf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;

public class MrDao
{
	/**
	 * 
	 * @param showType 1日期 2月份 3运营商 4下游 5上游+运营商 6上游
	 * @return
	 */
	private String[] getGroupOrder(int showType)
	{
		String groupName = "GROUP BY a.mr_date";
		String showName = "a.mr_date";
		String orderName = "ORDER BY a.mr_date";
		
		switch(showType)
		{
			case 1:
				groupName = "GROUP BY a.mr_date";
				showName = "a.mr_date";
				orderName = "ORDER BY a.mr_date";
				break;
				
			case 2:
				groupName = "GROUP BY DATE_FORMAT(a.mr_date,'%Y-%m')";
				showName = "DATE_FORMAT(a.mr_date,'%Y-%m')";
				orderName = "ORDER BY show_name";
				break;
				
			case 3:
				groupName = "GROUP BY j.id";
				showName = "j.name_cn";
				orderName = "ORDER BY j.name_cn";
				break;
				
			case 4:
				groupName = "GROUP BY f.id";
				showName = "f.short_name";
				orderName = "ORDER BY f.short_name";
				break;
			
			case 5:
				groupName = "GROUP BY e.id,h.id";
				showName = "CONCAT(e.short_name,'-',j.name_cn)";
				orderName = "ORDER BY d.name";
				break;
				
			case 6:
				groupName = "GROUP BY e.id";
				showName = "e.short_name";
				orderName = "ORDER BY e.short_name";
				break;
				
			default:
				break;
		}
		return new String[] {groupName,showName,orderName};
	}
	
	public Map<String, Object> loadShowData(String startDate,String endDate,int spId,int cpId,int operatorId,int showType)
	{
		
		String[] showConditions = getGroupOrder(showType);
		
		String groupName = showConditions[0];
		String showName = showConditions[1];
		String orderName = showConditions[2];
		
		String sql = " SELECT " + Constant.CONSTANT_REPLACE_STRING + ", SUM(a.data_rows) data_rows,SUM(a.amount) data_amount ";
		
		sql += " FROM  " + Constant.DB_DAILY_LOG + ".tbl_mr_summer a ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.trone_order_id = b.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp_trone d ON c.sp_trone_id = d.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.sp_id = e.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_cp f ON b.cp_id = f.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_province g ON a.province_id = g.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_product_2 h ON d.product_id = h.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_product_1 i ON h.product_1_id = i.id ";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_operator j ON i.operator_id = j.id ";
		sql += " WHERE 1=1 ";
		sql += " AND a.mr_date >= '" + startDate + "' ";
		sql += " AND a.mr_date <= '" + endDate + "' ";
		
		if(cpId>0)
			sql += " AND f.id = " + cpId;
		
		if(spId>0)
			sql += " AND e.id = " + spId;
		
		if(operatorId>0)
			sql += " AND j.flag = " + operatorId;
		
		final Map<String, Object> map = new HashMap<String, Object>();
		
		new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, " count(*) "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					map.put("rows", rs.getInt(1));
					map.put("dataRows", rs.getInt("data_rows"));
					map.put("dataAmount", rs.getFloat("data_amount"));
				}
				
				return null;
			}
		});
		
		sql += "  " + groupName;
		sql += "  " + orderName;
		
		
		new JdbcControl().query(sql.replace(Constant.CONSTANT_REPLACE_STRING, showName + " show_name "), new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				
				while(rs.next())
				{
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("showName", rs.getString("show_name"));
					data.put("dataRows", rs.getInt("data_rows"));
					data.put("dataAmount", rs.getFloat("data_amount"));
					list.add(data);
				}
				
				map.put("data", list);
				
				return null;
			}
		});
		
		return map;
	}
	
}
