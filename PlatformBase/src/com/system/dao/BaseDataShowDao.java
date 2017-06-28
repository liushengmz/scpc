package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.BaseDataShowModel;

public class BaseDataShowDao
{
	/***
	 * 获取指定公司的数据，可以单选上下游
	 * @param coId
	 * @param spId
	 * @param cpId
	 * @param showType 1 按日期，2按周，3按月，4按SP，5按CP，6按SP业务线，7按CP业务线，8按SP业务名称，
	 * 9按CP业务名称，10按SP价格通道，11按CP价格通道，12按省份,13按公司
	 */
	@SuppressWarnings("unchecked")
	public List<BaseDataShowModel> loadShowData(String startDate,String endDate,int spId,int cpId,int coId,int showType)
	{
		String[] queryType = loadQueryType(showType);
		
		String sql = "SELECT " + queryType[0] + " title,SUM(a.amount) amount ";
		sql += " FROM comsum_config.`tbl_data_summer`  a";
		sql += " LEFT JOIN (SELECT * FROM comsum_config.`tbl_trone` WHERE co_id = " + coId + ") b ON a.`trone_id` = b.`trone_id`";
		sql += " LEFT JOIN (SELECT * FROM comsum_config.`tbl_sp_trone` WHERE co_id = " + coId + ") c ON b.`sp_trone_id` = c.`sp_trone_id`";
		sql += " LEFT JOIN (SELECT * FROM comsum_config.`tbl_sp` WHERE co_id = " + coId + ") d ON c.`sp_id` = d.`sp_id`";
		sql += " LEFT JOIN (SELECT * FROM comsum_config.`tbl_cp` WHERE co_id = " + coId + ") e ON a.`cp_id` = e.`cp_id`";
		sql += " LEFT JOIN comsum_config.`tbl_product_2` f ON c.product_id = f.`id`";
		sql += " LEFT JOIN comsum_config.`tbl_product_1` g ON f.`product_1_id` = g.`id`";
		sql += " LEFT JOIN comsum_config.`tbl_operator` h ON g.`operator_id` = h.id";
		sql += " LEFT JOIN comsum_config.tbl_company i on a.co_id = i.co_id";
		sql += " LEFT JOIN comsum_config.tbl_province j on a.province_id = j.id";
		sql += " WHERE 1=1 ";
		
		if(coId>0)
		{
			sql += " AND a.co_id = " + coId;
		}
		
		if(spId>0)
		{
			sql += " AND d.sp_id = " + spId;
		}
		
		if(cpId>0)
		{
			sql += " AND e.cp_id = " + cpId;
		}
		
		sql += " AND mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "'";
		sql += " GROUP BY " + queryType[1] + " ORDER BY " + queryType[0] + " ASC";
		sql += " LIMIT 1000";
		
		JdbcControl control = new JdbcControl();
		
		return (List<BaseDataShowModel>)control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<BaseDataShowModel> list = new ArrayList<BaseDataShowModel>();
				
				while(rs.next())
				{
					BaseDataShowModel model = new BaseDataShowModel();
					
					model.setTitle(rs.getString("title"));
					model.setAmount(rs.getFloat("amount"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	
	/***
	 * 获取指定公司的数据，可以单选上下游
	 * @param coId
	 * @param spId
	 * @param cpId
	 * @param showType 1 按日期，2按周，3按月，4按SP，5按CP，6按SP业务线，7按CP业务线，8按SP业务名称，
	 * 9按CP业务名称，10按SP价格通道，11按CP价格通道，12按省份,13按公司
	 */
	@SuppressWarnings("unchecked")
	public List<BaseDataShowModel> loadAllData(String startDate,String endDate,int coId,int showType)
	{
		String[] queryType = loadQueryType(showType);
		
		String sql = "SELECT " + queryType[0] + " title,SUM(a.amount) amount ";
		sql += " FROM comsum_config.`tbl_data_summer`  a";
		sql += " LEFT JOIN comsum_config.tbl_company i on a.co_id = i.co_id";
		sql += " LEFT JOIN comsum_config.tbl_province j on a.province_id = j.id";
		sql += " WHERE 1=1 ";
		
		if(coId>0)
		{
			sql += " AND a.co_id = " + coId;
		}
		
		sql += " AND mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "'";
		sql += " GROUP BY " + queryType[1] + " ORDER BY " + queryType[0] + " ASC";
		sql += " LIMIT 1000";
		
		JdbcControl control = new JdbcControl();
		
		return (List<BaseDataShowModel>)control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<BaseDataShowModel> list = new ArrayList<BaseDataShowModel>();
				
				while(rs.next())
				{
					BaseDataShowModel model = new BaseDataShowModel();
					
					model.setTitle(rs.getString("title"));
					model.setAmount(rs.getFloat("amount"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public String[] loadQueryType(int showType)
	{
		String queryType = "";
		String groupType = "";
		
		switch(showType)
		{
			case 1:
				queryType = "a.mr_date";
				groupType = queryType;
				break;
				
			case 2:
				queryType = "DATE_FORMAT(a.mr_date,'%Y-%u')";
				groupType = queryType;
				break;
				
			case 3:
				queryType = "DATE_FORMAT(a.mr_date,'%Y-%m')";
				groupType = queryType;
				break;
				
			case 4:
				queryType = " d.short_name";
				groupType = " d.id ";
				break;
				
			case 5:
				queryType = " e.short_name ";
				groupType = "a.cp_id";
				break;
				
			case 12:
				queryType = "j.name";
				groupType = "a.province_id";
				break;
				
			case 13:
				queryType = "i.short_name";
				groupType = "i.short_name";
				break;
				
			case 14:
				queryType = "h.name_cn";
				groupType = "h.id";
				break;
				
			default:
				break;
		}
		
		String[] result = new String[2];
		
		result[0] = queryType;
		result[1] = groupType;
		
		return result;
		
	}
}
