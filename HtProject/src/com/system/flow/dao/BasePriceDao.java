package com.system.flow.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.flow.model.BasePriceModel;
import com.system.util.StringUtil;

public class BasePriceDao
{
	@SuppressWarnings("unchecked")
	public List<BasePriceModel> loadBasePrice()
	{
		String sql = "SELECT a.*,b.name_cn";
		sql += " FROM " + Constant.DB_DAILY_CONFIG + ".tbl_f_basic_price a";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_operator b ON a.operator = b.flag";
		sql += " ORDER BY name_cn ASC,num ASC";
		
		return (List<BasePriceModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<BasePriceModel> list = new ArrayList<BasePriceModel>();
				while(rs.next())
				{
					BasePriceModel model = new BasePriceModel();
					
					model.setId(rs.getInt("id"));
					model.setName(StringUtil.getString(rs.getString("name"), ""));
					model.setNum(rs.getInt("num"));
					model.setPrice(rs.getInt("price"));
					model.setOperator(rs.getInt("operator"));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
}
