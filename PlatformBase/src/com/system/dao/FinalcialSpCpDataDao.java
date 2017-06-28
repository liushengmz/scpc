package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.util.StringUtil;
import com.system.vmodel.FinancialSpCpDataShowModel;

public class FinalcialSpCpDataDao
{
	@SuppressWarnings("unchecked")
	public List<FinancialSpCpDataShowModel> loadData(int coId,String startDate,String endDate,int spId,int cpId)
	{
		String sql = "SELECT d.sp_id,d.full_name sp_full_name,d.short_name sp_name,c.sp_trone_id,c.name sp_trone_name,e.cp_id,";
		
		sql += " e.full_name cp_full_name,e.short_name cp_name,SUM(data_rows) data_rows,0 show_data_rows,SUM(amount) amount,0 show_amounts,c.jiesuanlv sp_rate,f.rate cp_rate";
		sql += " FROM (SELECT trone_id,province_id,cp_id,data_rows,amount,mr_date,record_type,STATUS,show_type";
		sql += " FROM tbl_data_summer WHERE co_id = " + coId + " AND mr_date >= '" + startDate + "' AND mr_date <= '" + endDate + "') a";
		sql += " LEFT JOIN (SELECT * FROM tbl_trone WHERE co_id = " + coId + ") b ON a.`trone_id` = b.trone_id";
		sql += " LEFT JOIN (SELECT * FROM `tbl_sp_trone` WHERE co_id = " + coId + ") c ON b.sp_trone_id = c.sp_trone_id";
		sql += " LEFT JOIN (SELECT * FROM tbl_sp WHERE co_id = " + coId + ") d ON c.sp_id = d.sp_id";
		sql += " LEFT JOIN (SELECT * FROM tbl_cp WHERE co_id = " + coId + ") e ON a.`cp_id` = e.cp_id";
		sql += " LEFT JOIN (SELECT * FROM tbl_cp_trone_rate WHERE co_id = " + coId + ") f ON c.sp_trone_id = f.sp_trone_id AND a.cp_id = f.cp_id";
		sql += " WHERE 1=1 ";
		
		if(spId>0)
		{
			sql += " AND d.sp_id = " + spId;
		}
		
		if(cpId>0)
		{
			sql += " AND e.cp_id = " + cpId;
		}
		
		sql += " GROUP BY d.sp_id,c.sp_trone_id,a.`cp_id`";
		sql += " ORDER BY CONVERT(d.full_name USING gbk),CONVERT(c.name USING gbk),CONVERT(e.full_name USING gbk) ASC";
		
		return (List<FinancialSpCpDataShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<FinancialSpCpDataShowModel> list = new ArrayList<FinancialSpCpDataShowModel>();
				int spId;
				String spShortName;
				String spFullName;
				int spTroneId;
				String spTroneName;
				double spJieSuanLv;
				int dataRows;
				double amount;
				int showDataRows;
				double showAmount;
				int cpId;
				String cpShortName;
				String cpFullName;
				double cpJieSuanLv;
				
				FinancialSpCpDataShowModel model = null;
				FinancialSpCpDataShowModel.SpTroneModel spTroneModel = null;
				
				while(rs.next())
				{
					spId = rs.getInt("sp_id");
					spFullName = StringUtil.getString(rs.getString("sp_full_name"), "");
					spShortName = StringUtil.getString(rs.getString("sp_name"), "");
					spTroneId = rs.getInt("sp_trone_id");
					spTroneName = StringUtil.getString(rs.getString("sp_trone_name"), "");
					spJieSuanLv = rs.getDouble("sp_rate");
					dataRows = rs.getInt("data_rows");
					amount = rs.getDouble("amount");
					showDataRows = rs.getInt("show_data_rows");
					showAmount = rs.getInt("show_amounts");
					cpId = rs.getInt("cp_id");
					cpFullName = StringUtil.getString(rs.getString("cp_full_name"), "");
					cpShortName = StringUtil.getString(rs.getString("cp_name"), "");
					cpJieSuanLv = rs.getDouble("cp_rate");
					
					boolean existSp = false;
					
					for(FinancialSpCpDataShowModel spCpDataShowModel : list)
					{
						if(spCpDataShowModel.spId == spId)
						{
							existSp = true;
							break;
						}
					}
					
					if(!existSp)
					{
						model = new FinancialSpCpDataShowModel();
						
						model.spId = spId;
						model.spShortName = spShortName;
						model.spFullName = spFullName;
						
						spTroneModel = model.new SpTroneModel();
						spTroneModel.spJieSuanLv = spJieSuanLv;
						spTroneModel.spTroneId = spTroneId;
						spTroneModel.spTroneName = spTroneName;
						
						model.list.add(spTroneModel);
						
						FinancialSpCpDataShowModel.SpTroneModel.CpModelData cpModelData = spTroneModel.new CpModelData();
						
						cpModelData.cpId = cpId;
						cpModelData.cpShortName = cpShortName;
						cpModelData.cpFullName = cpFullName;
						cpModelData.cpJieSuanLv = cpJieSuanLv;
						cpModelData.dataRows = dataRows;
						cpModelData.showDataRows = showDataRows;
						cpModelData.amount = amount;
						cpModelData.showAmount = showAmount;
						
						spTroneModel.spTroneAmount += cpModelData.amount;
						
						spTroneModel.list.add(cpModelData);
						
						list.add(model);
					}
					else
					{
						boolean existSpTrone = false;
						for(FinancialSpCpDataShowModel.SpTroneModel spTroneModel1 : model.list)
						{
							if(spTroneModel1.spTroneId==spTroneId)
							{
								existSpTrone = true;
								break;
							}
						}
						if(!existSpTrone)
						{
							spTroneModel = model.new SpTroneModel();
							spTroneModel.spJieSuanLv = spJieSuanLv;
							spTroneModel.spTroneId = spTroneId;
							spTroneModel.spTroneName = spTroneName;
							
							model.list.add(spTroneModel);
							
							FinancialSpCpDataShowModel.SpTroneModel.CpModelData cpModelData = spTroneModel.new CpModelData();
							
							cpModelData.cpId = cpId;
							cpModelData.cpShortName = cpShortName;
							cpModelData.cpFullName = cpFullName;
							cpModelData.cpJieSuanLv = cpJieSuanLv;
							cpModelData.dataRows = dataRows;
							cpModelData.showDataRows = showDataRows;
							cpModelData.amount = amount;
							cpModelData.showAmount = showAmount;
							
							spTroneModel.spTroneAmount += cpModelData.amount;
							
							spTroneModel.list.add(cpModelData);
						}
						else
						{
							FinancialSpCpDataShowModel.SpTroneModel.CpModelData cpModelData = spTroneModel.new CpModelData();
							
							cpModelData.cpId = cpId;
							cpModelData.cpShortName = cpShortName;
							cpModelData.cpFullName = cpFullName;
							cpModelData.cpJieSuanLv = cpJieSuanLv;
							cpModelData.dataRows = dataRows;
							cpModelData.showDataRows = showDataRows;
							cpModelData.amount = amount;
							cpModelData.showAmount = showAmount;
							
							spTroneModel.spTroneAmount += cpModelData.amount;
							
							spTroneModel.list.add(cpModelData);
							
							spTroneModel.spTroneRowSpan++;
						}
						model.spRowSpan++;
					}
					
					
					
					spId = 0;
					spShortName = "";
					spTroneId = 0;
					spTroneName = "";
					spJieSuanLv = 0;
					dataRows = 0;
					amount = 0;
					showDataRows = 0;
					showAmount = 0;
					cpId = 0;
					cpShortName = "";
					cpJieSuanLv = 0;
				}
				
				return list;
			}
		});
	}
	
	public static void main(String[] args)
	{
		List<FinancialSpCpDataShowModel> list = new FinalcialSpCpDataDao().loadData(1,"2015-10-01", "2015-11-30",0,0);
		for(FinancialSpCpDataShowModel model : list)
		{
			for(FinancialSpCpDataShowModel.SpTroneModel spTroneModel : model.list)
			{
				for(FinancialSpCpDataShowModel.SpTroneModel.CpModelData cpModelData : spTroneModel.list)
				{
					System.out.println("----CpName:" + cpModelData.cpShortName + ";dataRows:" + cpModelData.dataRows + ";"
							+ "showDataRows:" + cpModelData.showDataRows + ";amount:" + cpModelData.amount + ";showAmount:" 
							+ cpModelData.showAmount + ";CpJieSuanLv:" + cpModelData.cpJieSuanLv);
				}
			}
		}
	}
}
