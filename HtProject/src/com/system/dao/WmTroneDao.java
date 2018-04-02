package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.WmTroneModel;
import com.system.util.StringUtil;

public class WmTroneDao
{
	@SuppressWarnings("unchecked")
	public List<WmTroneModel> loadTroneByCpUserId(int userId)
	{
		String sql  = "SELECT b.id,b.trone_name";
		sql += " FROM " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone_order a";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone b    ON a.trone_id = b.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_cp c    ON a.cp_id = c.id";
		sql += " WHERE c.user_id = " + userId;
		sql += " GROUP BY b.id";
		sql += " ORDER BY b.trone_name";
		
		return (List<WmTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<WmTroneModel> list = new ArrayList<WmTroneModel>();
				
				while(rs.next())
				{
					WmTroneModel model = new WmTroneModel();
					
					model.setId(rs.getInt("id"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
		
	}
	
	public static void main(String[] args)
	{
		List<WmTroneModel> list = new WmTroneDao().loadTroneByCpUserId(546);
		for(WmTroneModel model : list)
			System.out.println(model.getId() + "--" + model.getTroneName());
	}
	
}
