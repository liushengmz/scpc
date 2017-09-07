package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CpRatioModel;
import com.system.util.StringUtil;

public class CpRatioDao
{
	@SuppressWarnings("unchecked")
	public List<CpRatioModel> loadCpRatioList()
	{
		String sql = "SELECT * FROM " + SysConstant.DB_CONFIG_MAIN + ".tbl_f_cp_ratio WHERE STATUS = 1";
		return (List<CpRatioModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpRatioModel> list = new ArrayList<CpRatioModel>(); 
				
				while(rs.next())
				{
					CpRatioModel model = new CpRatioModel();
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setOperator(rs.getInt("operator"));
					model.setProId(rs.getInt("pro_id"));
					model.setRatio(rs.getInt("ratio"));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					list.add(model);
				}
				
				return list;
			}
		});
		
	}
}
