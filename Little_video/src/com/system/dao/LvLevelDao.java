
package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.LvLevelModel;
import com.system.util.StringUtil;

public class LvLevelDao
{
	@SuppressWarnings("unchecked")
	public List<LvLevelModel> loadLvLeve()
	{
		String sql = "select * from daily_config.tbl_lv_level";
		return (List<LvLevelModel>) new JdbcControl().query(sql,
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<LvLevelModel> list = new ArrayList<LvLevelModel>();
						while (rs.next())
						{
							LvLevelModel model = new LvLevelModel();
							model.setId(rs.getInt("id"));
							model.setLevel(rs.getInt("level"));
							model.setPrice(rs.getInt("price"));
							model.setRemark(StringUtil
									.getString(rs.getString("remark"), ""));
							model.setCreateDate(rs.getDate("create_date"));
							model.setAppkey(StringUtil
									.getString(rs.getString("appkey"), ""));
							list.add(model);
						}
						return list;
					}
				});
	}

}
