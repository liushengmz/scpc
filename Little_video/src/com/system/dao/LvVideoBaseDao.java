
package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.LvVideoBaseModel;
import com.system.model.vrVideoBaseModel;
import com.system.util.StringUtil;

public class LvVideoBaseDao
{
	@SuppressWarnings("unchecked")
	public List<LvVideoBaseModel> loadLvVideoBase()
	{
		String sql = "select v.* ,r.type,r.sort_id from daily_config.tbl_lv_video_base v left join tbl_lv_recommend r on v.id=r.video_id order by type, sort_id";
		return (List<LvVideoBaseModel>) new JdbcControl().query(sql,
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<vrVideoBaseModel> list = new ArrayList<vrVideoBaseModel>();
						while (rs.next())
						{
							vrVideoBaseModel model = new vrVideoBaseModel();
							model.setId(rs.getInt("id"));
							model.setImg_id(rs.getInt("Img_id"));
							model.setLength(rs.getInt("length"));
							model.setLevel(rs.getInt("level"));
							model.setName(StringUtil.getString(rs.getString("name"), ""));
							
							model.setPath(StringUtil.getString(rs.getString("path"), ""));
							model.setRemark(StringUtil.getString(rs.getString("Remark"), ""));
							model.setCreateDate(rs.getDate("create_date"));
							
							model.setType(rs.getInt("type"));
							model.setSortId(rs.getInt("sort_id"));

							list.add(model);
						}
						return list;
					}
				});
	}

}
