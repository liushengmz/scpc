package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.TroneModel;

public class TroneDao
{
	@SuppressWarnings("unchecked")
	public List<TroneModel> loadTroneList()
	{
		String sql = "SELECT a.pro_id,a.ratio ratio,b.send_sms,a.id trone_id,f.id sp_api_id,";
		sql += " b.id sp_trone_id,e.id sp_id,c.num flow_size,c.operator,c.price,d.use_rang rang,d.time_type";
		sql += " FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_trone a";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sp_trone b ON a.sp_trone_id = b.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_basic_price c ON b.price_id = c.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_flow_type d ON b.flow_type_id = d.id";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sp e ON b.sp_id = e.id ";
		sql += " LEFT JOIN " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_sp_api f on b.sp_api_id = f.id";
		sql += " WHERE a.status = 1 AND b.status = 1";
		
		return (List<TroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneModel> list = new ArrayList<TroneModel>();
				
				while(rs.next())
				{
					TroneModel model = new TroneModel();
					model.setId(rs.getInt("trone_id"));
					model.setFlowSize(rs.getInt("flow_size"));
					model.setOperator(rs.getInt("operator"));
					model.setPrice(rs.getInt("price"));
					model.setProId(rs.getInt("pro_id"));
					model.setRang(rs.getInt("rang"));
					model.setSendSms(rs.getInt("send_sms"));
					model.setSpId(rs.getInt("sp_id"));
					model.setRatio(rs.getInt("ratio"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setTimeType(rs.getInt("time_type"));
					model.setSpApiId(rs.getInt("sp_api_id"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
