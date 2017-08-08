package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CpSpTroneSynModel;
import com.system.model.PayCodeExportChildModel;
import com.system.model.PayCodeExportModel;
import com.system.model.SpTroneModel;
import com.system.model.TroneOrderModel;
import com.system.util.StringUtil;

public class TroneOrderDao
{
	@SuppressWarnings("unchecked")
	public List<TroneOrderModel> loadTroneOrderListQiBa()
	{
		String sql  = "select a.*,b.commerce_user_id,b.short_name,c.sp_id,c.trone_name from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp b on a.cp_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on a.trone_id = c.id order by b.id,c.sp_id asc";
		
		return (List<TroneOrderModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setSpId(rs.getInt("sp_id"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<TroneOrderModel> loadTroneOrderListBySpTroneId(int spTroneId)
	{
		String sql = "select a.*,b.commerce_user_id,b.short_name,c.sp_id,c.trone_name,c.price from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a "
				+ " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp b on a.cp_id = b.id "
				+ " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on a.trone_id = c.id "
				+ " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.id "
				+ " WHERE b.id <> 34 and d.trone_type = 1 and d.id = "
				+ spTroneId + "  order by b.short_name asc";
		
		return (List<TroneOrderModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setSpId(rs.getInt("sp_id"));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setHoldAcount(rs.getInt("hold_start"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<TroneOrderModel> loadTroneOrderListByTroneId(int troneId)
	{
		String sql = "select a.*,b.commerce_user_id,b.short_name,c.sp_id,c.trone_name,c.price from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a "
				+ " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp b on a.cp_id = b.id "
				+ " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on a.trone_id = c.id "
				+ " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.id "
				+ " WHERE b.id <> 34 and c.id = "
				+ troneId + "  order by b.short_name asc";
		
		return (List<TroneOrderModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setSpId(rs.getInt("sp_id"));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setHoldAcount(rs.getInt("hold_start"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<TroneOrderModel> loadTroneOrderList()
	{
		String sql  = "select a.*,b.commerce_user_id,b.short_name,c.sp_id,c.trone_name,c.price from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp b on a.cp_id = b.id left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on a.trone_id = c.id  order by b.short_name asc";
		
		return (List<TroneOrderModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setSpId(rs.getInt("sp_id"));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public Map<String, Object> loadTroneOrder(int spId,int spTroneId,int cpId, int status,int pageIndex,String keyWord)
	{
		String query = " b.sp_trone_id,e.commerce_user_id,c.`name` sp_trone_name,a.*, b.price,d.id sp_id, b.`trone_name`,d.`short_name` sp_name,e.`short_name` cp_name ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` e ON a.`cp_id` = e.`id` WHERE 1=1 and e.id <> 34";
		
		String wheres = "";
		
		if(spId>0)
			wheres += " and d.id = " + spId;
		if(spTroneId>0)
			wheres += " and c.id = " + spTroneId;
		if(cpId>0)
			wheres += " and e.id = " + cpId;
		if(status>=0)
			wheres += " and a.disable = " + status;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			wheres += " and (d.short_name like '%" + keyWord + "%' or d.full_name like '%" + keyWord 
					+ "%' or e.short_name like '%" + keyWord + "%' or e.full_name like '%" + keyWord 
					+ "%' or c.name like '%" + keyWord + "%' or b.orders like '%" + keyWord 
					+ "%' or b.trone_name like '%" + keyWord + "%' or b.trone_num like '%" 
					+ keyWord + "%' OR a.`order_num` LIKE '%"+ keyWord +"%')";
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String orders = " order by a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)") + wheres,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + wheres + orders + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setHoldAmount(rs.getFloat("hold_amount"));
					model.setIsHoldCustom(rs.getInt("hold_is_Custom"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setPrice(rs.getFloat("price"));
					model.setHoldAcount(rs.getInt("hold_start"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	} 
	
	public TroneOrderModel getTroneOrderById(int id)
	{
		String sql = "select  b.sp_trone_id,e.commerce_user_id,c.`name` sp_trone_name,a.*, d.id sp_id, b.`trone_name`,d.`short_name` sp_name,e.`short_name` cp_name  ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` e ON a.`cp_id` = e.`id` WHERE  a.id = " + id;
		
		return (TroneOrderModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setHoldAmount(rs.getFloat("hold_amount"));
					model.setIsHoldCustom(rs.getInt("hold_is_Custom"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setHoldAcount(rs.getInt("hold_start"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					
					return model;
				}
				return null;
			}
		});
	}
	
	public boolean addTroneOrder(TroneOrderModel model)
	{
		
		String sql = "insert into " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order(trone_id,order_num,cp_id,order_trone_name,is_dynamic,push_url_id,disable,is_unknow,hold_percent,hold_amount,hold_is_custom,hold_start,is_unhold_data) values(" + model.getTroneId() + ",'"
				+ model.getOrderNum() + "'," + model.getCpId() + ",'"
				+ model.getOrderTroneName() + "'," + model.getDynamic() + ","
				+ model.getPushUrlId() + "," + model.getDisable() + ","
				+ model.getIsUnKnow() + "," + model.getHoldPercent() + ","
				+ model.getHoldAmount() + "," + model.getIsHoldCustom() + ","
				+ model.getHoldAcount() + "," + model.getIsUnholdData() + ")";

		return new JdbcControl().execute(sql);
		
	}
	
	public boolean updateTroneOrder(TroneOrderModel model)
	{
		String sql = "update " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order set trone_id = " + model.getTroneId() + ", order_num = '"
				+ model.getOrderNum() + "',cp_id=" + model.getCpId()
				+ ",order_trone_name='" + model.getOrderTroneName()
				+ "',is_dynamic=" + model.getDynamic() + ",push_url_id="
				+ model.getPushUrlId() + ",disable=" + model.getDisable()
				+ ",is_unknow=" + model.getIsUnKnow() + ",hold_percent="
				+ model.getHoldPercent() + ",hold_amount=" + model.getHoldAmount() + ",hold_is_custom="
				+ model.getIsHoldCustom() + ",hold_start = " + model.getHoldAcount() + ",is_unhold_data = " + model.getIsUnholdData() + " where id = " + model.getId();
		
		return new JdbcControl().execute(sql);
	}
	
	//不要问我为什么这样处理这个状态，得问邓先生！
	@SuppressWarnings("unchecked")
	public List<TroneOrderModel> loadTroneOrderListByCpSpTroneId(int cpId,int spTroneId,int status)
	{
		String sql = "select a.*,b.commerce_user_id,b.short_name,d.sp_id,c.trone_num,c.orders trone_order,c.trone_name,c.price,d.status,d.name sp_trone_name,d.provinces,d.ramark  from " 
				+ com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a "
				+ " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp b on a.cp_id = b.id "
				+ " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on a.trone_id = c.id "
				+ " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.id "
				+ " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` e ON d.`sp_id` = e.id "
				+ " WHERE b.id = "+ cpId + " and d.trone_api_id > 0 ";
		
				if(status>-1)
				{
					sql += " and c.status = " + status + " and  a.disable = " +  (status==0 ? 1 : 0) ;
				}
		
				if(spTroneId>0)
				{
					sql += " and d.id = " + spTroneId;
				}
				
				sql += " order by a.id desc";
		
		return (List<TroneOrderModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setSpId(rs.getInt("sp_id"));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setSpTroneStatus(rs.getInt("status"));
					model.setHoldAcount(rs.getInt("hold_start"));
					model.setProvince(StringUtil.getString(rs.getString("provinces"), ""));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					
					model.setRemark(StringUtil.getString(rs.getString("ramark"), ""));
					model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					model.setTroneOrder(StringUtil.getString(rs.getString("trone_order"), ""));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	//不要问我为什么这样处理这个状态，得问邓先生！
	@SuppressWarnings("unchecked")
	public List<PayCodeExportModel> loadPayCodeExportModelListByCpSpTroneId(int cpId,int spTroneId,int status)
	{
		String  sql = "SELECT d.id sp_trone_id,d.name sp_trone_name,e.name up_data_type_name,d.provinces,d.sp_id,";
		sql += " a.id pay_code,c.price,c.orders,c.trone_num,d.ramark remark";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp b ON a.cp_id = b.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON a.trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_up_data_type e ON d.up_data_type = e.id";
		sql += " WHERE 1=1 and d.trone_api_id > 0 ";
		
		if(cpId>0)
		{
			sql += " AND b.id = " + cpId;
		}
		
		if(spTroneId>0)
		{
			sql += " AND d.id = " + spTroneId;
		}
		
		if(status >=0 )
		{
			sql += " AND c.status = " + status + " and  a.disable = " +  (status==0 ? 1 : 0) ;
		}
		
		sql += " ORDER BY a.id desc";
		
		
		return (List<PayCodeExportModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<PayCodeExportModel> list = new ArrayList<PayCodeExportModel>();
				
				PayCodeExportModel model = null;
				
				while(rs.next())
				{
					int spTroneId = rs.getInt("sp_trone_id");
					
					model = null;
					
					for(PayCodeExportModel tmpModel : list)
					{
						if(tmpModel.getSpTroneId() == spTroneId)
						{
							model = tmpModel;
							break;
						}
					}
					
					if(model==null)
					{
						model = new PayCodeExportModel();
						model.setSpTroneId(spTroneId);
						model.setPrivinces(StringUtil.getString(rs.getString("provinces"), ""));
						model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
						model.setUpDataTypeName(StringUtil.getString(rs.getString("up_data_type_name"), ""));
						model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
						model.setChildList(new ArrayList<PayCodeExportChildModel>());
						model.setSpId(rs.getInt("sp_id"));
						list.add(model);
					}
						
					PayCodeExportChildModel childModel = new PayCodeExportChildModel();
					
					childModel.setOrders(StringUtil.getString(rs.getString("orders"), ""));
					childModel.setPayCode(rs.getInt("pay_code"));
					childModel.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					childModel.setPrice(rs.getInt("price"));
					
					model.getChildList().add(childModel);
				}
				
				return list;
			}
		});
	}
	
	public CpSpTroneSynModel loadCpSpTroneSynModelById(int id)
	{
		String sql = "SELECT a.`order_num`,b.`trone_num`,c.url,f.`short_name` cp_name,e.`short_name` sp_name,d.`name` sp_trone_name,b.`price`,d.`trone_api_id`";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_push_url` c ON a.`push_url_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON b.`sp_trone_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` e ON d.`sp_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON a.`cp_id` = f.`id`";
		sql += " WHERE a.id = " + id;
		
		return (CpSpTroneSynModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					CpSpTroneSynModel model = new CpSpTroneSynModel();
					model.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setOrder(StringUtil.getString(rs.getString("order_num"), ""));
					model.setTroneNum(StringUtil.getString(rs.getString("trone_num"), ""));
					model.setPrice(rs.getFloat("price"));
					model.setCpUrl(StringUtil.getString(rs.getString("url"),""));
					model.setTroneOrderId(rs.getInt("trone_api_id"));
					return model;
				}
				return null;
			}
		});
		
	}
	
	public void closeTroneOrderByTroneId(int troneId)
	{
		String sql = "udpate " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order set disable = 1 where trone_id = " + troneId;
		
		new JdbcControl().execute(sql);
	}
	public Map<String, Object> loadTroneOrder(int spId,int spTroneId,int cpId, int status,int pageIndex,String keyWord,int userId)
	{
		String query = " b.sp_trone_id,e.commerce_user_id,c.`name` sp_trone_name,a.*, b.price,d.id sp_id, b.`trone_name`,d.`short_name` sp_name,e.`short_name` cp_name ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` e ON a.`cp_id` = e.`id` WHERE 1=1 and e.id <> 34";
		
		String wheres = "";
		
		if(spId>0)
			wheres += " and d.id = " + spId;
		if(spTroneId>0)
			wheres += " and c.id = " + spTroneId;
		if(cpId>0)
			wheres += " and e.id = " + cpId;
		if(status>=0)
			wheres += " and a.disable = " + status;
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			wheres += " and (d.short_name like '%" + keyWord + "%' or d.full_name like '%" + keyWord 
					+ "%' or e.short_name like '%" + keyWord + "%' or e.full_name like '%" + keyWord 
					+ "%' or c.name like '%" + keyWord + "%' or b.orders like '%" + keyWord 
					+ "%' or b.trone_name like '%" + keyWord + "%' or b.trone_num like '%" 
					+ keyWord + "%' OR a.`order_num` LIKE '%"+ keyWord +"%')";
		}
		if(spId<=0&&spTroneId<=0&&cpId<=0&&status<0&&StringUtil.isNullOrEmpty(keyWord)){
			wheres +=" and e.commerce_user_id="+userId;
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String orders = " order by a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)") + wheres,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + wheres + orders + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setHoldAmount(rs.getFloat("hold_amount"));
					model.setIsHoldCustom(rs.getInt("hold_is_Custom"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setPrice(rs.getFloat("price"));
					model.setHoldAcount(rs.getInt("hold_start"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	} 
	/**
	 * CP商务-CP业务管理查询
	 * @param userId
	 * @param spId
	 * @param spTroneId
	 * @param cpId
	 * @param status
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadTroneOrder(int userId,int spId,int spTroneId,int cpId, int status,int pageIndex,String keyWord)
	{
		String query = " b.sp_trone_id,e.commerce_user_id,c.`name` sp_trone_name,a.*, b.price,d.id sp_id, b.`trone_name`,d.`short_name` sp_name,e.`short_name` cp_name ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` e ON a.`cp_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` f ON f.`id` = e.`commerce_user_id`";
				
		sql += " WHERE 1=1 and e.id <> 34";
		
		String wheres = "";
		
		if(spId>0)
			wheres += " and d.id = " + spId;
		if(spTroneId>0)
			wheres += " and c.id = " + spTroneId;
		if(cpId>0)
			wheres += " and e.id = " + cpId;
		if(status>=0)
			wheres += " and a.disable = " + status;
		if(userId>0){
			wheres += " and f.id = " + userId;
		}
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			wheres += " and (d.short_name like '%" + keyWord + "%' or d.full_name like '%" + keyWord 
					+ "%' or e.short_name like '%" + keyWord + "%' or e.full_name like '%" + keyWord 
					+ "%' or c.name like '%" + keyWord + "%' or b.orders like '%" + keyWord 
					+ "%' or b.trone_name like '%" + keyWord + "%' or b.trone_num like '%" 
					+ keyWord + "%' OR a.`order_num` LIKE '%"+ keyWord +"%')";
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String orders = " order by a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)") + wheres,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + wheres + orders + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setHoldAmount(rs.getFloat("hold_amount"));
					model.setIsHoldCustom(rs.getInt("hold_is_Custom"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setPrice(rs.getFloat("price"));
					model.setHoldAcount(rs.getInt("hold_start"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	} 
	
	/**
	 * SP商务-CP业务管理查询
	 * @param userId
	 * @param spId
	 * @param spTroneId
	 * @param cpId
	 * @param status
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */
	public Map<String, Object> loadSpTroneOrder(int userId,int spId,int spTroneId,int cpId, int status,int pageIndex,String keyWord)
	{
		String query = " b.sp_trone_id,e.commerce_user_id,c.`name` sp_trone_name,a.*, b.price,d.id sp_id, b.`trone_name`,d.`short_name` sp_name,e.`short_name` cp_name ";
		
		String sql = "select " + Constant.CONSTANT_REPLACE_STRING ;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` b ON a.`trone_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON b.`sp_trone_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` d ON c.`sp_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` e ON a.`cp_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` f ON f.`id` = d.`commerce_user_id`";
				
		sql += " WHERE 1=1 and e.id <> 34";
		
		String wheres = "";
		
		if(spId>0)
			wheres += " and d.id = " + spId;
		if(spTroneId>0)
			wheres += " and c.id = " + spTroneId;
		if(cpId>0)
			wheres += " and e.id = " + cpId;
		if(status>=0)
			wheres += " and a.disable = " + status;
		if(userId>0){
			wheres += " and f.id = " + userId;
		}
		
		if(!StringUtil.isNullOrEmpty(keyWord))
		{
			wheres += " and (d.short_name like '%" + keyWord + "%' or d.full_name like '%" + keyWord 
					+ "%' or e.short_name like '%" + keyWord + "%' or e.full_name like '%" + keyWord 
					+ "%' or c.name like '%" + keyWord + "%' or b.orders like '%" + keyWord 
					+ "%' or b.trone_name like '%" + keyWord + "%' or b.trone_num like '%" 
					+ keyWord + "%' OR a.`order_num` LIKE '%"+ keyWord +"%')";
		}
		
		String limit = " limit "  + Constant.PAGE_SIZE*(pageIndex-1) + "," + Constant.PAGE_SIZE;
		
		String orders = " order by a.id desc ";
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JdbcControl control = new JdbcControl();
		map.put("rows",control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)") + wheres,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		}));
		
		map.put("list", control.query(sql.replace(Constant.CONSTANT_REPLACE_STRING, query) + wheres + orders + limit, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<TroneOrderModel> list = new ArrayList<TroneOrderModel>();
				while(rs.next())
				{
					TroneOrderModel model = new TroneOrderModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setTroneId(rs.getInt("trone_id"));
					model.setOrderNum(StringUtil.getString(rs.getString("order_num"), ""));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setOrderTroneName(StringUtil.getString(rs.getString("order_trone_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setDynamic(rs.getInt("is_dynamic"));
					model.setPushUrlId(rs.getInt("push_url_id"));
					model.setDisable(rs.getInt("disable"));
					model.setIsUnKnow(rs.getInt("is_unknow"));
					model.setTroneName(StringUtil.getString(rs.getString("trone_name"), ""));
					model.setHoldPercent(rs.getInt("hold_percent"));
					model.setHoldAmount(rs.getFloat("hold_amount"));
					model.setIsHoldCustom(rs.getInt("hold_is_Custom"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setPrice(rs.getFloat("price"));
					model.setHoldAcount(rs.getInt("hold_start"));
					model.setCommerceUserId(rs.getInt("commerce_user_id"));
					model.setIsUnholdData(rs.getInt("is_unhold_data"));
					list.add(model);
				}
				return list;
			}
		}));
		
		return map;
	} 
	
	
	@SuppressWarnings("unchecked")
	public List<com.system.model.SpTroneModel> loadSpTroneListByCpId(int cpId)
	{
		String sql = " SELECT d.id sp_id,d.short_name sp_name,c.id sp_trone_id,c.name sp_trone_name ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b ON a.trone_id = b.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c ON b.sp_trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d ON c.sp_id = d.id ";
		sql += " WHERE a.cp_id = " + cpId;
		sql += " GROUP BY c.id; ";
		
		return (List<com.system.model.SpTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<com.system.model.SpTroneModel> list = new ArrayList<com.system.model.SpTroneModel>();
				
				while(rs.next())
				{
					SpTroneModel model = new SpTroneModel();
					model.setId(rs.getInt("sp_trone_id"));
					model.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setSpId(rs.getInt("sp_id"));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	
}
