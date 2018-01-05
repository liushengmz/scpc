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

public class WmSummerDao
{
	/**
	 * 
	 * @param showType 1日期 2SP 3CP 4TRONE_ID 5COMPANY
	 * @return
	 */
	private String[] getShowData(int showType)
	{
		String queryName = "a.mr_date";
		String groupName = "a.mr_date";
		String orderName = "a.mr_date ASC";
		
		switch(showType)
		{
			case 1:
				queryName = "a.mr_date";
				groupName = "a.mr_date";
				orderName = "a.mr_date ASC";
				break;
			case 2:
				queryName = "e.full_name";
				groupName = "e.id";
				orderName = "e.id ASC";
				break;
			case 3:
				queryName = "g.full_name";
				groupName = "g.id";
				orderName = "g.id ASC";
				break;
			case 4:
				queryName = "CONCAT(e.short_name,'-',c.trone_name)";
				groupName = "c.id";
				orderName = "e.short_name ASC,c.trone_name ASC";
				break;
			case 5:
				queryName = "f.full_name";
				groupName = "f.id";
				orderName = "f.id ASC";
				break;
			default:
				break;
		}
		return new String[] {queryName,groupName,orderName};
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> loadWmShowTypeData(String startDate,String endDate,int spId,int troneId,int cpId,int showType)
	{
		String[] names = getShowData(showType);
		
		String queryName = names[0];
		String groupName = names[1];
		String orderName = names[2];
		
		String sql = " SELECT " + queryName + " show_name,SUM(a.data_rows) data_rows,SUM(a.data_amount) data_amount,SUM(a.show_data_rows) show_data_rows,SUM(a.show_data_amount) show_data_amount ";
		sql  += " FROM " + Constant.DB_DAILY_LOG + ".tbl_wm_summer a ";
		sql  += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone_order b ON a.trone_order_id = b.id ";
		sql  += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone c ON b.trone_id = c.id ";
		sql  += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_sp_trone d ON c.sp_trone_id = d.id ";
		sql  += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_sp e ON d.sp_id = e.id ";
		sql  += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_company f ON e.co_id = f.id ";
		sql  += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_cp g ON b.cp_id = g.id ";
		sql  += " WHERE 1=1 ";
		sql  += " AND a.mr_date >= '" + startDate + "' ";
		sql  += " AND a.mr_date <= '" + endDate + "' ";
		
		if(spId>0)
			sql += " AND e.id = " + spId;

		if(cpId>0)
			sql += " AND g.id = " + cpId;
		
		if(troneId>0)
			sql += " AND c.id = " + troneId;
		
		sql  += " GROUP BY " + groupName;
		sql  += " ORDER BY " + orderName;
		sql  += " LIMIT 1000 ";
		
		return (List<Map<String, Object>>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				
				while(rs.next())
				{
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("showName", StringUtil.getString(rs.getString("show_name"), ""));
					map.put("dataRows", rs.getInt("data_rows"));
					map.put("dataAmount", rs.getFloat("data_amount"));
					map.put("showDataRows", rs.getInt("show_data_rows"));
					map.put("showDataAmount", rs.getFloat("show_data_amount"));
					
					list.add(map);
				}
				return list;
			}
		});
	}
	
}
