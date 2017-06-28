
package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.LvRecommendModel;

public class LvRecommendDao
{
	@SuppressWarnings("unchecked")
	public List<LvRecommendModel> loadLvRecommend()
	{
		String sql = "select * from daily_config.tbl_lv_recommend order by sort_id";
		return (List<LvRecommendModel>) new JdbcControl().query(sql,
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<LvRecommendModel> list = new ArrayList<LvRecommendModel>();
						while (rs.next())
						{
							LvRecommendModel model = new LvRecommendModel();
							model.setId(rs.getInt("id"));
							model.setSortId(rs.getInt("Sort_id"));
							model.setType(rs.getInt("type"));
							model.setVideoId(rs.getInt("video_id"));
							model.setCreateDate(rs.getDate("create_date"));
							list.add(model);
						}
						return list;
					}
				});
	}

}
