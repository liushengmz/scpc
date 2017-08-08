package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.ProfitCalModel;
import com.system.util.StringUtil;

public class ProfitCalDao
{
	@SuppressWarnings("unchecked")
	public List<ProfitCalModel> loadProfit(String startDate,String endDate,int spId,int spTroneId)
	{
		String sql = " SELECT a.*,b.income_money";
		sql += " FROM";
		sql += " (";
		
		sql += " SELECT sp_id,sp_name,sp_trone_id,sp_trone_name,cp_id,cp_name,SUM(outcome_money) outcome_money";
		sql += " FROM";
		sql += " (";
		
		sql += " SELECT  e.id sp_id,e.full_name sp_name,b.id sp_trone_id,b.name sp_trone_name,d.id cp_id,d.full_name cp_name,";
		sql += " CASE reduce_type";
		sql += " WHEN 0 THEN (a.amount - a.reduce_amount)*a.rate";
		sql += " WHEN 1 THEN (a.amount*a.rate - a.reduce_amount)";
		sql += " END AS outcome_money";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_cp_billing_sp_trone a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone b ON a.sp_trone_id = b.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_billing c ON a.cp_billing_id = c.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp d ON c.cp_id = d.id";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON b.sp_id = e.id";
		sql += " WHERE a.start_date >= '" + startDate + "'";
		sql += " AND a.end_date <= '" + endDate + "'	";
		
		if(spId>0)
			sql += " AND e.id = " + spId;
		
		if(spTroneId>0)
			sql += " AND b.id = " + spTroneId;
		
		sql += " )a GROUP BY cp_id,sp_trone_id";
		sql += " )a";
		sql += " LEFT JOIN ";
		sql += " (";
		sql += " SELECT b.id sp_trone_id,";
		sql += " SUM((a.amount - a.reduce_data_amount)*a.rate - a.reduce_money_amount) income_money";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_LOG + ".tbl_sp_billing_sp_trone a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone b ON a.sp_trone_id = b.id";
		sql += " WHERE a.start_date >= '" + startDate + "'";
		sql += " AND a.end_date <= '" + endDate + "'";
		
		if(spId>0)
			sql += " AND b.sp_id = " + spId;
		
		if(spTroneId>0)
			sql += " AND b.id = " + spTroneId;
		
		sql += " GROUP BY b.id";
		sql += " )b ON a.sp_trone_id = b.sp_trone_id";
		sql += " ORDER BY a.sp_name,a.sp_trone_name,cp_name";
		
		return (List<ProfitCalModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<ProfitCalModel> list = new ArrayList<ProfitCalModel>();
				
				
				while(rs.next())
				{
					int spId = rs.getInt("sp_id");
					String spName = StringUtil.getString(rs.getString("sp_name"), "");
					int spTroneId = rs.getInt("sp_trone_id");
					String spTroneName = StringUtil.getString(rs.getString("sp_trone_name"), "");
					int cpId = rs.getInt("cp_id");
					String cpName = StringUtil.getString(rs.getString("cp_name"), "");
					float spTroneMoney = rs.getFloat("income_money");
					float cpTroneMoney = rs.getFloat("outcome_money");
					
					ProfitCalModel spModel = null;
					
					for(ProfitCalModel tmpModel : list)
					{
						if(tmpModel.spId==spId)
						{
							spModel = tmpModel;
							break;
						}
					}
					
					if(spModel==null)
					{
						spModel = new ProfitCalModel();
						spModel.spId = spId;
						spModel.spName = spName;
						list.add(spModel);
					}
					
					spModel.spRowsCount++;
					
					ProfitCalModel.SpTroneModel spTroneModel = null;
					
					for(ProfitCalModel.SpTroneModel tmpModel : spModel.spTroneList)
					{
						if(tmpModel.spTroneId == spTroneId)
						{
							spTroneModel = tmpModel;
							break;
						}
					}
					
					if(spTroneModel==null)
					{
						spTroneModel = spModel.new SpTroneModel();
						spTroneModel.spTroneId = spTroneId;
						spTroneModel.spTroneName = spTroneName;
						spTroneModel.spTroneMoney = spTroneMoney;
						spTroneModel.spTroneProfit = spTroneMoney;
						spModel.spMoney += spTroneMoney;
						spModel.spTroneList.add(spTroneModel);
						spModel.spProfit += spTroneMoney;
					}
					
					spTroneModel.spTroneRowsCount++;
					
					ProfitCalModel.SpTroneModel.CpSpTroneModel cpSpTroneModel = spTroneModel.new CpSpTroneModel();
					cpSpTroneModel.cpId = cpId;
					cpSpTroneModel.cpName = cpName;
					cpSpTroneModel.cpMoney = cpTroneMoney;
					
					spTroneModel.spTroneProfit -= cpTroneMoney;
					spModel.spProfit -= cpTroneMoney;
					
					spTroneModel.cpSpTroneList.add(cpSpTroneModel);
				}
				
				return list;
			}
		});
	}
}
