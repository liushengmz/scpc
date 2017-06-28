package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.ServiceCodeModel;
import com.system.util.StringUtil;

public class ServiceCodeDao
{
	@SuppressWarnings("unchecked")
	public List<ServiceCodeModel> loadServiceCode()
	{
		String sql = "SELECT c.`bj_flag`,a.id,CONCAT(c.name_cn,'-',b.`name`,'-',a.`name`) service_name";
		sql += " FROM  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` b ON a.`product_1_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` c ON b.`operator_id` = c.`flag`";
		sql += " ORDER BY c.`bj_flag` ASC,service_name ASC";
		return (List<ServiceCodeModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ServiceCodeModel> list = new ArrayList<ServiceCodeModel>();
				while(rs.next())
				{
					ServiceCodeModel model = new ServiceCodeModel();
					model.setId(rs.getInt("id"));
					model.setOperatorBjFlag(rs.getInt("bj_flag"));
					model.setServiceName(StringUtil.getString(rs.getString("service_name"), ""));
					list.add(model);
				}
				return list;
			}
		});
		
	}
}
