package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.PublicUrlModel;
import com.system.util.StringUtil;

public class PublicUrlDao
{
	
	@SuppressWarnings("unchecked")
	public List<PublicUrlModel> getPublicUrlList()
	{
		String sql = "select * from tbl_public_url";
		return (List<PublicUrlModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<PublicUrlModel> list = new ArrayList<PublicUrlModel>();
				PublicUrlModel model = null;
				while(rs.next())
				{
					model = new PublicUrlModel();
					
					model.setId(rs.getInt("id"));
					model.setUrl(rs.getString("url"));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<String> loadPublicActionUrl()
	{
		String sql = "select action_url from tbl_menu_2";
		return (List<String>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<String>  list = new ArrayList<String>();
				
				while(rs.next())
				{
					for(String url : StringUtil.getString(rs.getString("action_url"), "").split(","))
					{
						if(!StringUtil.isNullOrEmpty(url))
							list.add(url);
					}
				}
				
				return list;
			}
		});
	}
	
}
