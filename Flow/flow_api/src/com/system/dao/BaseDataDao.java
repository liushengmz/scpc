package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CpModel;
import com.system.util.StringUtil;

public class BaseDataDao
{
	@SuppressWarnings("unchecked")
	public List<CpModel> loadCpList()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp WHERE STATUS = 1";
		return (List<CpModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpModel> list = new ArrayList<CpModel>();
				while(rs.next())
				{
					CpModel model = new CpModel();
					model.setCpId(rs.getInt("id"));
					model.setCurrency(rs.getInt("currency"));
					model.setSignKey(StringUtil.getString(rs.getString("sign_key"), ""));
					String ipList = StringUtil.getString(rs.getString("iplist"), "");
					String[] ips = ipList.split(",");
					for(String ip : ips)
					{
						model.getIpList().add(ip);
					}
					list.add(model);
				}
				return list;
			}
		});
	}
}
