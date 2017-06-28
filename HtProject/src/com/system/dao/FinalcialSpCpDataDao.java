package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SpcpProfitModel;
import com.system.util.StringUtil;
import com.system.vmodel.FinancialSpCpDataShowModel;

public class FinalcialSpCpDataDao
{
	@SuppressWarnings("unchecked")
	public List<FinancialSpCpDataShowModel> loadData(String startDate,String endDate,int spId,int cpId,int dataType)
	{
		String query = "";
		
		if(spId>0)
			query += " and d.id = " + spId;
		
		if(cpId>0)
			query += " and f.id = " + cpId;
		
		if(dataType>-1)
			query+= " and a.record_type = " + dataType;
		
		String sql = "SELECT a.sp_id,a.sp_name,a.sp_full_name,a.sp_trone_id,a.product_line_name,a.sp_trone_name,a.cp_id,";
		sql += " a.cp_name,a.cp_full_name,a.data_rows,a.amount,b.show_data_rows,b.show_amounts,";
		sql += " a.sp_jie_suan_lv,b.cp_jie_suan_lv";
		sql += " FROM(";
		sql += " SELECT d.id sp_id,d.`short_name` sp_name,d.full_name sp_full_name,c.id sp_trone_id,c.`name` sp_trone_name,CONCAT(i.name_cn,'-',h.name,'-',g.name) product_line_name,";
		sql += " f.id cp_id,f.`short_name` cp_name,f.`full_name` cp_full_name,c.`jiesuanlv` sp_jie_suan_lv,SUM(a.data_rows) data_rows,SUM(a.amount) amount";
		sql += " FROM daily_log.tbl_mr_summer a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` e ON a.`trone_order_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON e.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON e.`cp_id` = f.id";
		
		//增加SP业务线
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 g ON c.product_id = g.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 h ON g.product_1_id = h.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator i ON h.operator_id = i.flag ";
		
		sql += " WHERE a.`mr_date` >= '" + startDate + "' AND a.`mr_date` <= '" + endDate + "'" + query;
		sql += " GROUP BY d.id,c.id,f.id ORDER BY CONVERT(d.`short_name` USING gbk),CONVERT(c.`name` USING gbk),CONVERT(f.`short_name` USING gbk) ASC";
		sql += " ) a";
		sql += " LEFT JOIN ";
		sql += " (";
		sql += " SELECT d.id sp_id,d.`short_name` sp_name,d.full_name sp_full_name,c.id sp_trone_id,c.`name` sp_trone_name,";
		sql += " f.id cp_id,f.`short_name` cp_name,f.`full_name` cp_full_name,g.`rate` cp_jie_suan_lv,SUM(a.data_rows) show_data_rows,SUM(a.amount) show_amounts";
		sql += " FROM daily_log.`tbl_cp_mr_summer` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` e ON a.`trone_order_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b ON e.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON e.`cp_id` = f.`id`";
//		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate g ON f.id = g.`cp_id` AND c.`id` = g.`sp_trone_id`";
		
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate g ON e.cp_jiesuanlv_id = g.id";
		
		sql += " WHERE a.`mr_date` >= '" + startDate + "' AND a.`mr_date` <= '" + endDate + "'" + query;
		sql += " GROUP BY d.id,c.id,f.id ORDER BY CONVERT(d.`short_name` USING gbk),CONVERT(c.`name` USING gbk),CONVERT(f.`short_name` USING gbk) ASC";
		sql += " ) b";
		sql += " ON a.sp_id = b.`sp_id` AND a.sp_trone_id = b.sp_trone_id AND a.cp_id = b.cp_id";
		sql += " ORDER BY CONVERT(a.sp_name USING gbk),CONVERT(a.sp_trone_name USING gbk),CONVERT(a.cp_name USING gbk) ASC";
		
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
				String productLineName;
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
					productLineName = StringUtil.getString(rs.getString("product_line_name"), "");
					spJieSuanLv = rs.getDouble("sp_jie_suan_lv");
					dataRows = rs.getInt("data_rows");
					amount = rs.getDouble("amount");
					showDataRows = rs.getInt("show_data_rows");
					showAmount = rs.getInt("show_amounts");
					cpId = rs.getInt("cp_id");
					cpFullName = StringUtil.getString(rs.getString("cp_full_name"), "");
					cpShortName = StringUtil.getString(rs.getString("cp_name"), "");
					cpJieSuanLv = rs.getDouble("cp_jie_suan_lv");
					
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
						spTroneModel.productLineName = productLineName;
						
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
							spTroneModel.productLineName = productLineName;
							
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
	/**
	 * 利润查询
	 * @param startDate
	 * @param endDate
	 * @param spId
	 * @param cpId
	 * @param dataType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpcpProfitModel> loadProfitData(String startDate,String endDate,int spId,int spTroneId,int cpId)
	{
		String query = "";
		
		if(spId>0)
			query += " and a.sp_id = " + spId;
		
		if(cpId>0)
			query += " and b.cp_id = " + cpId;
		
		if(spTroneId>-1)
			query+= " and a.sp_trone_id = " + spTroneId;
		
		String sql="SELECT a.*,b.*,c.`full_name` cp_name,c.`short_name` cp_short_name FROM(";
		sql+=" SELECT c.id sp_id,c.`full_name` sp_name,b.id sp_trone_id,b.`name` sp_trone_name,a.amount FROM"; 
		sql+=" (SELECT sp_trone_id,SUM((amount-reduce_data_amount)*rate - reduce_money_amount) amount FROM daily_log.`tbl_sp_billing_sp_trone` ";
		sql+=" WHERE start_date>= '"+startDate+"' AND start_date<= '"+endDate+"' GROUP BY sp_trone_id) a";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id`";
	    sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` c ON b.`sp_id` = c.`id`) a";
	    sql+=" LEFT JOIN (SELECT a.sp_trone_id,b.cp_id,SUM(CASE reduce_type WHEN 0 THEN (a.amount-a.reduce_amount)*rate  WHEN 1 THEN a.amount*rate - a.reduce_amount END) pay_amount FROM daily_log.`tbl_cp_billing_sp_trone` a"; 
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` b ON a.cp_billing_id = b.id";
		sql+=" WHERE a.start_date>= '"+startDate+"' AND a.start_date<= '"+endDate+"'"; 
		sql+=" GROUP BY sp_trone_id,cp_id )b ON a.sp_trone_id = b.sp_trone_id";
		sql+=" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp c ON b.cp_id = c.id where 1=1 ";
		sql+=query;
		sql+=" order by sp_id";
		
		return (List<SpcpProfitModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpcpProfitModel> list = new ArrayList<SpcpProfitModel>();
				int spId;
				String spFullName;
				int spTroneId;
				String spTroneName;
				double amount;
				int cpId;
				String cpFullName;
				String cpShortName;
				double payAmount;
				SpcpProfitModel model = null;
				SpcpProfitModel.SpTroneModel spTroneModel = null;
				
				while(rs.next())
				{
					spId = rs.getInt("sp_id");
					spFullName = StringUtil.getString(rs.getString("sp_name"), "");
					spTroneId = rs.getInt("sp_trone_id");
					spTroneName = StringUtil.getString(rs.getString("sp_trone_name"), "");
					amount=rs.getDouble("amount");
					cpId = rs.getInt("cp_id");
					cpFullName = StringUtil.getString(rs.getString("cp_name"), "");
					cpShortName = StringUtil.getString(rs.getString("cp_short_name"), "");	
					payAmount=rs.getDouble("pay_amount");
					boolean existSp = false;
					
					for(SpcpProfitModel spcpProfitModel : list)
					{
						if(spcpProfitModel.spId == spId)
						{
							existSp = true;
							break;
						}
					}
					
					if(!existSp)
					{
						model = new SpcpProfitModel();
						
						model.spId = spId;
						model.spFullName = spFullName;
						
						spTroneModel = model.new SpTroneModel();
						spTroneModel.spTroneId = spTroneId;
						spTroneModel.spTroneName = spTroneName;
						spTroneModel.amount=amount;
						model.list.add(spTroneModel);
						
						SpcpProfitModel.SpTroneModel.CpModelData cpModelData = spTroneModel.new CpModelData();
						
						cpModelData.cpId = cpId;
						cpModelData.cpShortName = cpShortName;
						cpModelData.cpFullName = cpFullName;
						cpModelData.payAmount=payAmount;
						
						
						spTroneModel.list.add(cpModelData);
						
						list.add(model);
					}
					else
					{
						boolean existSpTrone = false;
						for(SpcpProfitModel.SpTroneModel spTroneModel1 : model.list)
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
							spTroneModel.spTroneId = spTroneId;
							spTroneModel.spTroneName = spTroneName;
							spTroneModel.amount=amount;
							
							model.list.add(spTroneModel);
							
							SpcpProfitModel.SpTroneModel.CpModelData cpModelData = spTroneModel.new CpModelData();
							
							cpModelData.cpId = cpId;
							cpModelData.cpShortName = cpShortName;
							cpModelData.cpFullName = cpFullName;
							cpModelData.payAmount=payAmount;
							spTroneModel.list.add(cpModelData);
						}
						else
						{
							SpcpProfitModel.SpTroneModel.CpModelData cpModelData = spTroneModel.new CpModelData();
							
							cpModelData.cpId = cpId;
							cpModelData.cpShortName = cpShortName;
							cpModelData.cpFullName = cpFullName;
							cpModelData.payAmount=payAmount;
							
							spTroneModel.list.add(cpModelData);
							
							spTroneModel.spTroneRowSpan++;
						}
						model.spRowSpan++;
					}
					spId = 0;
					spFullName="";
					spTroneId = 0;
					spTroneName = "";
					amount = 0;
					cpId = 0;
					cpFullName="";
					cpShortName = "";
					payAmount=0;
				}
				
				return list;
			}
		});
	}
	public static void main(String[] args)
	{
		List<FinancialSpCpDataShowModel> list = new FinalcialSpCpDataDao().loadData("2015-10-01", "2015-11-30",1,2,0);
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
