package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.WmDataModel;
import com.system.util.StringUtil;
import com.system.vmodel.WmDataShowModel;

public class WmDataDao
{
	@SuppressWarnings("unchecked")
	public List<WmDataShowModel> loadWmData(String startDate,String endDate,int spId,int cpId,String keyWord)
	{
		String sql = "SELECT f.id cp_id,e.id sp_id,d.id sp_trone_id,c.id trone_id,b.id trone_order_id,e.full_name sp_name,";
		sql += " d.sp_trone_name,c.trone_name,f.full_name cp_name,c.price sp_price,b.price cp_price,";
		sql += " SUM(a.data_rows) sp_data_rows,SUM(a.data_amount) sp_data_amount,";
		sql += " SUM(a.show_data_rows) cp_data_rows,SUM(a.show_data_amount) cp_data_amount";
		sql += " FROM " + Constant.DB_DAILY_LOG + ".tbl_wm_summer a";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone_order b ON a.trone_order_id = b.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone c ON b.trone_id = c.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_sp_trone d ON c.sp_trone_id = d.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_sp e ON d.sp_id = e.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_cp f ON b.cp_id = f.id";
		sql += " WHERE a.mr_date >= '" + startDate + "' AND a.mr_date <= '" + endDate + "'";
		
		if(spId>0)
		{
			sql += " and e.id = " + spId;
		}
		
		if(cpId>0)
		{
			sql += " and f.id = " + cpId;
		}
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			
		}
		
		sql += " GROUP BY e.id,d.id,c.id,b.id ";
		
		return (List<WmDataShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<WmDataShowModel> list = new ArrayList<WmDataShowModel>();
				
				int spId;
				int spTroneId;
				int troneId;
				int troneOrderId;
				int cpId;
				String spName;
				String spTroneName;
				String troneName;
				float spPrice;
				float cpPrice;
				int spDataRows;
				int cpDataRows;
				float spDataAmount;
				float cpDataAmount;
				String cpName;
				
				while(rs.next())
				{
					
					WmDataShowModel model = null;
					
					spId = rs.getInt("sp_id");
					spName = StringUtil.getString(rs.getString("sp_name"), "");
					spTroneId = rs.getInt("sp_trone_id");
					spTroneName = StringUtil.getString(rs.getString("sp_trone_name"), "");
					troneId = rs.getInt("trone_id");
					troneOrderId = rs.getInt("trone_order_id");
					troneName = StringUtil.getString(rs.getString("trone_name"), "");
					spPrice = rs.getFloat("sp_price");
					spDataRows = rs.getInt("sp_data_rows");
					spDataAmount = rs.getFloat("sp_data_amount");
					cpDataRows = rs.getInt("cp_data_rows");
					cpDataAmount = rs.getFloat("cp_data_amount");
					cpId = rs.getInt("cp_id");
					cpName = StringUtil.getString(rs.getString("cp_name"), "");
					cpPrice = rs.getFloat("cp_price");
					
					//处理SP_ID
					for(WmDataShowModel sModel : list)
					{
						if(sModel.spId==spId)
						{
							model = sModel;
							model.spRowSpand++;
							break;
						}
					}
					
					if(model==null)
					{
						model = new WmDataShowModel();
						model.spId = spId;
						model.spName = spName;
						list.add(model);
					}
					
					//处理 SP_TRONE_ID
					WmDataShowModel.SpTroneModel spTroneModel = null;
					
					for(WmDataShowModel.SpTroneModel  sSpTroneModel : model.spTroneList)
					{
						if(sSpTroneModel.spTroneId==spTroneId)
						{
							spTroneModel = sSpTroneModel;
							spTroneModel.spTroneRowSpand++;
							break;
						}
					}
					
					if(spTroneModel==null)
					{
						spTroneModel = model.new SpTroneModel();
						spTroneModel.spTroneId = spTroneId;
						spTroneModel.spTroneName = spTroneName;
						model.spTroneList.add(spTroneModel);
					}
					
					//处理 TRONE_ID
					WmDataShowModel.SpTroneModel.TroneModel troneModel = null;
					
					for(WmDataShowModel.SpTroneModel.TroneModel sTroneModel : spTroneModel.troneList)
					{
						if(sTroneModel.troneId==troneId)
						{
							troneModel = sTroneModel;
							troneModel.troneRowSpand++;
							break;
						}
					}
					
					if(troneModel==null)
					{
						troneModel = spTroneModel.new TroneModel();
						troneModel.troneId = troneId;
						troneModel.troneName = troneName;
						troneModel.spPrice = spPrice;
						spTroneModel.troneList.add(troneModel);
					}
					
					//处理Trone_Order_Id
					WmDataShowModel.SpTroneModel.TroneModel.TroneOrderModel troneOrderModel = troneModel.new TroneOrderModel();
					
					troneOrderModel.cpId = cpId;
					troneOrderModel.cpName = cpName;
					troneOrderModel.cpPrice = cpPrice;
					troneOrderModel.troneOrderId = troneOrderId;
					troneOrderModel.dataRows = spDataRows;
					troneOrderModel.showDataRows = cpDataRows;
					troneOrderModel.dataAmount = spDataAmount;
					troneOrderModel.showDataAmount = cpDataAmount;
					
					troneModel.troneOrderList.add(troneOrderModel);
					
				}
				
				return list;
			}
		});
		
	}
	
	@SuppressWarnings("unchecked")
	public List<WmDataModel> loadWmCpData(int userId,int troneId,String startDate,String endDate)
	{
		String sql = "SELECT c.id trone_id,c.trone_name,b.price cp_price,SUM(a.show_data_rows) cp_data_rows,SUM(a.show_data_amount) cp_data_amount";
		sql += " FROM " + Constant.DB_DAILY_LOG + ".tbl_wm_summer a";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone_order b    ON a.trone_order_id = b.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone c    ON b.trone_id = c.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_cp d    ON b.cp_id = d.id";
		sql += " WHERE d.user_id = " + userId;
		sql += " AND a.mr_date >= '" + startDate + "'";
		sql += " AND a.mr_date <= '" + endDate + "'";
		
		if(troneId>0)
		{
			sql += " AND c.id = "+ troneId;
		}
		
		sql += " GROUP BY c.id";
		sql += " ORDER BY c.trone_name";
		
		return (List<WmDataModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<WmDataModel> list = new ArrayList<WmDataModel>();
				
				while(rs.next())
				{
					WmDataModel model = new WmDataModel();
					
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setCpPrice(rs.getFloat("cp_price"));
					model.setCpDataRows(rs.getInt("cp_data_rows"));
					model.setCpDataAmount(rs.getFloat("cp_data_amount"));
					model.setTroneId(rs.getInt("trone_id"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<WmDataModel> loadWmCpDataDetail(int userId,int troneId,String startDate,String endDate)
	{
		String sql = "SELECT c.id trone_id,c.trone_name,b.price cp_price,a.show_data_rows cp_data_rows,a.show_data_amount cp_data_amount,a.mr_date ";
		sql += " FROM " + Constant.DB_DAILY_LOG + ".tbl_wm_summer a";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone_order b    ON a.trone_order_id = b.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone c    ON b.trone_id = c.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_cp d    ON b.cp_id = d.id";
		sql += " WHERE d.user_id = " + userId;
		sql += " AND a.mr_date >= '" + startDate + "'";
		sql += " AND a.mr_date <= '" + endDate + "'";
		
		if(troneId>0)
		{
			sql += " AND c.id = "+ troneId;
		}
		
		sql += " ORDER BY c.trone_name,a.mr_date asc";
		
		sql += " limit 1000";
		
		return (List<WmDataModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<WmDataModel> list = new ArrayList<WmDataModel>();
				
				while(rs.next())
				{
					WmDataModel model = new WmDataModel();
					
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setCpPrice(rs.getFloat("cp_price"));
					model.setCpDataRows(rs.getInt("cp_data_rows"));
					model.setCpDataAmount(rs.getFloat("cp_data_amount"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setMrDate(rs.getString("mr_date"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<WmDataModel> loadWmDataDetail(int troneOrderId,String startDate,String endDate)
	{
		String sql = "SELECT f.full_name sp_name,d.full_name cp_name,c.price sp_price,c.id trone_id,c.trone_name,b.price cp_price,a.data_rows sp_data_rows,a.data_amount sp_data_amount,a.show_data_rows cp_data_rows,a.show_data_amount cp_data_amount,a.mr_date ";
		sql += " FROM " + Constant.DB_DAILY_LOG + ".tbl_wm_summer a";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone_order b    ON a.trone_order_id = b.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_trone c    ON b.trone_id = c.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_cp d    ON b.cp_id = d.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_sp_trone e   ON c.sp_trone_id = e.id";
		sql += " LEFT JOIN " + Constant.DB_DAILY_CONFIG + ".tbl_wm_sp f    ON e.sp_id = f.id";
		sql += " WHERE 1=1 ";
		sql += " AND a.trone_order_id = " + troneOrderId;
		sql += " AND a.mr_date >= '" + startDate + "'";
		sql += " AND a.mr_date <= '" + endDate + "'";
		
		sql += " ORDER BY c.trone_name,a.mr_date asc";
		
		sql += " limit 1000";
		
		return (List<WmDataModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<WmDataModel> list = new ArrayList<WmDataModel>();
				
				while(rs.next())
				{
					WmDataModel model = new WmDataModel();
					
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setCpPrice(rs.getFloat("cp_price"));
					model.setCpDataRows(rs.getInt("cp_data_rows"));
					model.setCpDataAmount(rs.getFloat("cp_data_amount"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setMrDate(rs.getString("mr_date"));
					model.setSpPrice(rs.getFloat("sp_price"));
					model.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setSpDataRows(rs.getInt("sp_data_rows"));
					model.setSpDataAmount(rs.getFloat("sp_data_amount"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
}
