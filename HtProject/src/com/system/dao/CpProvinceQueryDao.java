package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.util.StringUtil;

public class CpProvinceQueryDao
{
	@SuppressWarnings("unchecked")
	public Map<Integer,Map<Float,List<Integer>>> loadCpProvince(final int cpId)
	{
		String sql = "SELECT f.id operator_id,f.name_cn,b.price,c.provinces";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b ON a.trone_id = b.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c ON b.sp_trone_id = c.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 d ON c.product_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 e ON d.product_1_id = e.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator f ON e.operator_id = f.id";
		sql += " WHERE c.status = 1 AND b.status = 1 AND a.disable = 0 AND f.id >0 AND a.cp_id = " + cpId;
		sql += " GROUP BY f.id,b.price,c.id ORDER BY f.id desc,b.price ASC";
		
		return (Map<Integer,Map<Float,List<Integer>>>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				//  运营商                         价格               省份  
				Map<Integer,Map<Float,List<Integer>>> operatorMap = new LinkedHashMap<Integer, Map<Float,List<Integer>>>(); 
				
				while(rs.next())
				{
					int operId = rs.getInt("operator_id");
					float price = rs.getFloat("price");
					String pros = StringUtil.getString(rs.getString("provinces"), "");
					
					List<Integer> proList = new ArrayList<Integer>();
					for(String pro : pros.split(","))
					{
						int iPro = StringUtil.getInteger(pro, -1);
						if(!proList.contains(iPro))
							proList.add(iPro);
					}
					
					Map<Float,List<Integer>> priceMap = operatorMap.get(operId);
					if(priceMap==null)
					{
						priceMap = new LinkedHashMap<Float, List<Integer>>();
						operatorMap.put(operId, priceMap);
					}
					
					List<Integer> curProList = priceMap.get(price);					
					if(curProList==null)
					{
						curProList = new ArrayList<Integer>();
						priceMap.put(price, curProList);
					}
					
					for(int pro : proList)
					{
						if(!curProList.contains(pro))
							curProList.add(pro);
					}
				}
				
				return operatorMap;
			}
		});
	}
	
	
	
	public static void main(String[] args)
	{
		List<Integer> list = new ArrayList<Integer>();
		
		String s = "1,2,3,4,5,6,1,5";
		
		for(String ss : s.split(","))
		{
			int aa = StringUtil.getInteger(ss, -1);
			
			if(!list.contains(aa))
				list.add(aa);
		}
			
			
		System.out.println(list);
		
	}
}
