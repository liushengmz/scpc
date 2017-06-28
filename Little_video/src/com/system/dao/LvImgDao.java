
package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.LvImgModel;
import com.system.util.StringUtil;

public class LvImgDao
{

	@SuppressWarnings("unchecked")
	public List<LvImgModel> loadLvImg()
	{
		String sql = "select * from daily_config.tbl_lv_img";
		return (List<LvImgModel>) new JdbcControl().query(sql,
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<LvImgModel> list = new ArrayList<LvImgModel>();
						while (rs.next())
						{
							LvImgModel model = new LvImgModel();
							model.setId(rs.getInt("id"));
							model.setName(StringUtil
									.getString(rs.getString("name"), ""));
							model.setLength(rs.getInt("length"));
							model.setPath(StringUtil
									.getString(rs.getString("path"), ""));
							model.setCreateDate(rs.getDate("create_date"));
							list.add(model);
						}
						return list;
					}
				});
	}

}
