package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.MoStatusSummerModel;
import com.system.util.StringUtil;

public class MoStatusSummerDao
{
	@SuppressWarnings("unchecked")
	public List<MoStatusSummerModel> loadMoStatusSummer(String table,String startDate,String endDate,String keyWord)
	{
		String sql = "";
		sql += " SELECT e.id sp_id,d.id sp_trone_id,c.id trone_id,b.id trone_order_id,f.id cp_id,c.price,";
		sql += " e.short_name sp_name,d.name sp_trone_name,c.trone_name,c.orders,";
		sql += " c.trone_num,b.order_num,f.short_name cp_name,a.status,";
		sql += " COUNT(a.trone_id) trone_id_rows,COUNT(a.trone_order_id) trone_order_id_rows,COUNT(a.status) status_rows";
		sql += " FROM daily_log.tbl_mo_" + table + " a";
		sql += " LEFT JOIN daily_config.tbl_trone_order b ON a.trone_order_id = b.id";
		sql += " LEFT JOIN daily_config.tbl_trone c ON a.trone_id = c.id";
		sql += " LEFT JOIN daily_config.tbl_sp_trone d ON c.sp_trone_id = d.id";
		sql += " LEFT JOIN daily_config.tbl_sp e ON d.sp_id = e.id";
		sql += " LEFT JOIN daily_config.tbl_cp f ON b.cp_id = f.id";
		sql += " WHERE a.create_date >= '" + startDate + "'";
		sql += " AND a.create_date < '" + endDate + "'";
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			sql += " AND (e.full_name LIKE '%" + keyWord + "%' OR d.name LIKE '%" + keyWord 
					+ "%' OR c.trone_name LIKE '%" + keyWord + "%' OR f.full_name LIKE '%" + keyWord + "%')";
		}
		sql += " GROUP BY a.trone_id,a.trone_order_id,a.status;";

		return (List<MoStatusSummerModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<MoStatusSummerModel> list = new ArrayList<MoStatusSummerModel>();
				
				int spId = 0;
				int spTroneId = 0;
				int troneId = 0;
				float price;
				String spName = "";
				String spTroneName = "";
				String troneName = "";
				String troneConfigOrders = "";
				String troneNum = "";
				int troneIdRows = 0;
				int troneOrderId = 0;
				int cpId = 0;
				String troneOrders = "";
				String cpName = "";
				int troneOrderIdRows = 0;
				String status = "";
				int statusRows = 0;
				
				MoStatusSummerModel troneModel = null;
				MoStatusSummerModel.TroneOrderDataModel troneOrderModel = null;
				MoStatusSummerModel.TroneOrderDataModel.TroneOrderStatusDataModel troneOrderStatusModel = null;
				
				while(rs.next())
				{
					
					troneModel = null;
					troneOrderModel = null;
					troneOrderStatusModel = null;
					
					spId = rs.getInt("sp_id");
					spTroneId = rs.getInt("sp_trone_id");
					troneId = rs.getInt("trone_id");
					price = rs.getFloat("price");
					spName = StringUtil.getString(rs.getString("sp_name"), "");
					spTroneName = StringUtil.getString(rs.getString("sp_trone_name"), "");
					troneName = StringUtil.getString(rs.getString("trone_name"), "");
					troneConfigOrders = StringUtil.getString(rs.getString("orders"), "");
					troneNum = StringUtil.getString(rs.getString("trone_num"), "");
					troneOrders = StringUtil.getString(rs.getString("order_num"), "");
					troneOrderId = rs.getInt("trone_order_id");
					troneIdRows = rs.getInt("trone_id_rows");
					troneOrderIdRows = rs.getInt("trone_order_id_rows");
					cpName = StringUtil.getString(rs.getString("cp_name"), "");
					cpId = rs.getInt("cp_id");
					status = StringUtil.getString(rs.getString("status"), "NULL");
					statusRows = rs.getInt("status_rows");
					
					troneModel = null;
					
					for(MoStatusSummerModel model : list)
					{
						if(model.troneId == troneId)
						{
							troneModel = model;
							break;
						}
					}
					
					if(troneModel==null)
					{
						troneModel = new MoStatusSummerModel();
						troneModel.spId = spId;
						troneModel.spTroneId = spTroneId;
						troneModel.troneId = troneId;
						troneModel.price = price;
						troneModel.spName = spName;
						troneModel.spTroneName = spTroneName;
						troneModel.troneName = troneName;
						troneModel.troneConfigOrders = troneConfigOrders;
						troneModel.troneNum = troneNum;
						troneModel.troneOrderList = new ArrayList<MoStatusSummerModel.TroneOrderDataModel>();
						list.add(troneModel);
					}
					
					troneModel.troneIdRows += troneIdRows;
					troneModel.troneRowSpand++;
					
					for(MoStatusSummerModel.TroneOrderDataModel model : troneModel.troneOrderList)
					{
						if(model.troneOrderId == troneOrderId)
						{
							troneOrderModel = model;
							break;
						}
					}
					
					if(troneOrderModel==null)
					{
						troneOrderModel = troneModel.new TroneOrderDataModel();
						troneOrderModel.troneOrderId = troneOrderId;
						troneOrderModel.cpId = cpId;
						troneOrderModel.troneOrders = troneOrders;
						troneOrderModel.cpName = cpName;
						troneOrderModel.troneOrderStatusList = new ArrayList<MoStatusSummerModel.TroneOrderDataModel.TroneOrderStatusDataModel>();
						troneModel.troneOrderList.add(troneOrderModel);
					}
					
					troneOrderModel.troneOrderIdRows += troneOrderIdRows;
					troneOrderModel.troneOrderRowSpand++;
					
					troneOrderStatusModel = troneOrderModel.new TroneOrderStatusDataModel();
					troneOrderStatusModel.status = status;
					troneOrderStatusModel.statusRows = statusRows;
					
					troneOrderModel.troneOrderStatusList.add(troneOrderStatusModel);
				}
				
				return list;
			}
		});
		
	}
}
