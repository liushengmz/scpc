package com.system.flow.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.flow.model.CpRatioModel;
import com.system.util.StringUtil;

public class CpRatioDao
{
	@SuppressWarnings("unchecked")
	public List<CpRatioModel> loadCpRatio(int cpId)
	{
		String sql = " SELECT a.id,b.id cp_id,b.short_name cp_name,a.operator,c.name_cn,a.pro_id,d.name pro_name,a.ratio,a.status";
		sql += " FROM " + Constant.DB_DAILY_CONFIG + ".tbl_f_cp_ratio a";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_f_cp b ON a.cp_id = b.id";
		sql += " left join " + Constant.DB_DAILY_CONFIG + ".tbl_operator c on a.operator = c.flag";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_province d ON a.pro_id = d.id";
		sql += " WHERE a.cp_id = " + cpId;
		sql += " order by c.flag,d.id";
		
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
					model.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOperator(rs.getInt("operator"));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setProId(rs.getInt("pro_id"));
					model.setProName(StringUtil.getString(rs.getString("pro_name"), ""));
					model.setRatio(rs.getInt("ratio"));
					model.setStatus(rs.getInt("status"));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, List<CpRatioModel>> loadCpRatioList(int cpId)
	{
		String sql = " SELECT a.id,b.id cp_id,b.short_name cp_name,a.operator,c.name_cn,a.pro_id,d.name pro_name,a.ratio,a.status";
		sql += " FROM " + Constant.DB_DAILY_CONFIG + ".tbl_f_cp_ratio a";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_f_cp b ON a.cp_id = b.id";
		sql += " left join " + Constant.DB_DAILY_CONFIG + ".tbl_operator c on a.operator = c.flag";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_province d ON a.pro_id = d.id";
		sql += " WHERE a.cp_id = " + cpId;
		sql += " order by c.flag,d.id";
		
		return (Map<Integer, List<CpRatioModel>>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				Map<Integer, List<CpRatioModel>> map = new HashMap<Integer, List<CpRatioModel>>();
				
				List<CpRatioModel> list1 = new ArrayList<CpRatioModel>();
				List<CpRatioModel> list2 = new ArrayList<CpRatioModel>();
				List<CpRatioModel> list3 = new ArrayList<CpRatioModel>();
				
				map.put(1, list1);
				map.put(2, list2);
				map.put(3, list3);
				
				while(rs.next())
				{
					CpRatioModel model = new CpRatioModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOperator(rs.getInt("operator"));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setProId(rs.getInt("pro_id"));
					model.setProName(StringUtil.getString(rs.getString("pro_name"), ""));
					model.setRatio(rs.getInt("ratio"));
					model.setStatus(rs.getInt("status"));
					
					if(model.getOperator()==1)
						list1.add(model);
					else if(model.getOperator()==2)
						list2.add(model);
					else if(model.getOperator()==3)
						list3.add(model);
					
				}
				return map;
			}
		});
	}
	
	public void updateCpRatioList(List<CpRatioModel> list)
	{
		String sql = "update " + Constant.DB_DAILY_CONFIG + ".tbl_f_cp_ratio set ratio = %d , status = %d where id = %d ";
		
		JdbcControl control = new JdbcControl() ;
		
		String lastSql = "";
		
		for(CpRatioModel model : list)
		{
			lastSql =  String.format(sql,model.getRatio(),model.getStatus(),model.getId());
			control.execute(lastSql);
		}
	}
	
}
