package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.MrModel;

public class MrDao
{
	@SuppressWarnings("unchecked")
	public List<MrModel> loadUserDayMonthLimit(String userMd5,String month,int spTroneId)
	{
		String sql = "SELECT trone_id,cp_id,mr_date FROM daily_log.tbl_mr_" + month + " WHERE user_md10 = '" + userMd5 + "' and sp_trone_id = " + spTroneId + " ORDER BY mr_date DESC";
		
		return (List<MrModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<MrModel> list = new ArrayList<MrModel>();
				
				while(rs.next())
				{
					MrModel model = new MrModel();
					
					model.setCpId(rs.getInt("cp_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setMrDate(rs.getString("mr_date"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
