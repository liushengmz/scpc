package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CompanyModel;
import com.system.util.StringUtil;

public class CompanyDao
{
	@SuppressWarnings("unchecked")
	public List<CompanyModel> loadCompany()
	{
		
		String sql = "select * from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_company where status=1 order by convert(co_short_name using gbk) asc";

		return (List<CompanyModel>) new JdbcControl().query(sql, new QueryCallBack()
		{

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CompanyModel> list = new ArrayList<CompanyModel>();

				while (rs.next())
				{
					CompanyModel model = new CompanyModel();

					model.setId(rs.getInt("id"));
					model.setShortName(StringUtil.getString(rs.getString("co_short_name"), ""));
					model.setFullName(StringUtil.getString(rs.getString("co_name"), ""));
					list.add(model);
				}

				return list;
			}
		});
	}
}
