package com.system.flow.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.flow.model.TroneModel;
import com.system.util.StringUtil;

public class TroneDao
{
	/**
	 * 根据SP TRONE ID 获取 FLOW 的 TRONE LIST
	 * @param spTroneId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TroneModel> loadTroneBySpTroneId(int spTroneId)
	{
		String sql = "SELECT a.*,b.name pro_name FROM " + Constant.DB_DAILY_CONFIG + ".tbl_f_trone a LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_province b on a.pro_id = b.id  WHERE a.sp_trone_id = " + spTroneId + " ORDER BY a.pro_id ASC";
		
		return (List<TroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneModel> list = new ArrayList<TroneModel>();
				
				while(rs.next())
				{
					TroneModel model = new TroneModel();
					
					model.setId(rs.getInt("id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setProId(rs.getInt("pro_id"));
					model.setRatio(rs.getInt("ratio"));
					model.setStatus(rs.getInt("status"));
					model.setProName(StringUtil.getString(rs.getString("pro_name"), ""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	public void addTroneList(int spTroneId,List<TroneModel> list)
	{
		String sql = "insert into " + Constant.DB_DAILY_CONFIG + ".tbl_f_trone(sp_trone_id,pro_id,ratio,status) values ";
		for(TroneModel model : list)
		{
			sql  += "(" + spTroneId + "," + model.getProId() + "," + model.getRatio() + "," + model.getStatus() + "),";
		}
		sql = sql.substring(0, sql.length()-1);
		sql += ";";
		new JdbcControl().execute(sql);
	}
	
	public void updateTroneList(List<TroneModel> list)
	{
		String sql = "update " + Constant.DB_DAILY_CONFIG + ".tbl_f_trone set sp_trone_id =  %d , pro_id = %d , ratio = %d , status = %d where id = %d ";
		
		JdbcControl control = new JdbcControl() ;
		
		String lastSql = "";
		
		for(TroneModel model : list)
		{
			lastSql =  String.format(sql, model.getSpTroneId(),model.getProId(),model.getRatio(),model.getStatus(),model.getId());
			control.execute(lastSql);
		}
	}
	
}
