package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.RedisCpSingleOrderModel;

public class SingleCpOrderDao
{
	/**
	 * 往月表里插入定单数据
	 * @param model
	 * @return
	 */
	public int addSingleCpOrderToMonthTable(RedisCpSingleOrderModel model)
	{
		String sql = "INSERT INTO " + SysConstant.DB_LOG_MAIN + ".tbl_f_cp_order_list_" + model.getMonthName() 
		+ " (cp_order_id,sp_order_id,mobile,operator,flowsize,use_rang,time_type,";
		sql += " cp_trone_id,cp_id,cp_ratio,status,trone_id,sp_trone_id,sp_id,sp_ratio,base_price_id,sp_api_id,notify_url)";
		sql += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, model.getClientOrderId());
		params.put(2, model.getServerOrderId());
		params.put(3, model.getMobile());
		params.put(4, model.getOperator());
		params.put(5, model.getFlowSize());
		params.put(6, model.getRang());
		params.put(7, model.getTimeType());
		params.put(8, model.getCpTroneId());
		params.put(9, model.getCpId());
		params.put(10, model.getCpRatio());
		params.put(11, model.getStatus());
		params.put(12, model.getTroneId());
		params.put(13, model.getSpTroneId());
		params.put(14, model.getSpId());
		params.put(15, model.getSpRatio());
		params.put(16, model.getBasePriceId());
		params.put(17, model.getSpApiId());
		params.put(18, model.getNotifyUrl());
		
		return new JdbcControl().insertWithGenKey(sql, params);
	}
	
	/**
	 * 往临时表里插入定单数据
	 * @param model
	 * @return
	 */
	public int addSingleCpOrderToTempTable(RedisCpSingleOrderModel model)
	{
		String sql = "INSERT INTO " + SysConstant.DB_LOG_MAIN + ".tbl_f_cp_order_list"
		+ " (cp_order_id,sp_order_id,mobile,operator,flowsize,use_rang,time_type,";
		sql += " cp_trone_id,cp_id,cp_ratio,status,trone_id,sp_trone_id,sp_id,sp_ratio,base_price_id,month_table_id,month,sp_api_id,notify_url)";
		sql += " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, model.getClientOrderId());
		params.put(2, model.getServerOrderId());
		params.put(3, model.getMobile());
		params.put(4, model.getOperator());
		params.put(5, model.getFlowSize());
		params.put(6, model.getRang());
		params.put(7, model.getTimeType());
		params.put(8, model.getCpTroneId());
		params.put(9, model.getCpId());
		params.put(10, model.getCpRatio());
		params.put(11, model.getStatus());
		params.put(12, model.getTroneId());
		params.put(13, model.getSpTroneId());
		params.put(14, model.getSpId());
		params.put(15, model.getSpRatio());
		params.put(16, model.getBasePriceId());
		params.put(17, model.getMonthTableId());
		params.put(18, model.getMonthName());
		params.put(19, model.getSpApiId());
		params.put(20, model.getNotifyUrl());
		
		return new JdbcControl().insertWithGenKey(sql, params);
	}
	
	/**
	 * 判断指定表中CP对应的ORDERID是否存在
	 * @param table
	 * @param orderId
	 * @param cpId
	 * @return
	 */
	public boolean isExistCpOrder(String table,int cpId,String orderId)
	{
		String sql = "SELECT id FROM " + SysConstant.DB_LOG_MAIN + ".tbl_f_cp_order_list_" + table + " WHERE cp_order_id = '" + orderId + "' AND cp_id = " + cpId;
		return (Integer)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt("id");
				return 0;
			}
		}) > 0 ;
	}
	
	/**
	 * 更新临时表的状态
	 * @param tempTableId
	 * @param syncStatus
	 * @param lastSyncMils
	 * @param syncTimes
	 * @return
	 */
	public boolean updateSingleCpOrderSyncStatusInTempTable(int tempTableId,int syncStatus,long lastSyncMils,int syncTimes)
	{
		String sql = "UPDATE daily_log.tbl_f_cp_order_list SET notify_status = " + syncStatus + ", notify_times = " + syncTimes + ", last_notify_mils = " + lastSyncMils + " WHERE id  = " + tempTableId;
		
		return new JdbcControl().execute(sql);
	}
	
	/**
	 * 更新月表的状态
	 * @param tableName
	 * @param monthTableId
	 * @param syncStatus
	 * @param lastSyncMils
	 * @param syncTimes
	 * @return
	 */
	public boolean updateSingleCpOrderSyncStatusInMonthTable(String tableName,int monthTableId,int syncStatus,long lastSyncMils,int syncTimes)
	{
		String sql = "UPDATE daily_log.tbl_f_cp_order_list_" + tableName + " SET notify_status = " + syncStatus + ", notify_times = " + syncTimes + ", last_notify_mils = " + lastSyncMils + " WHERE id  = " + monthTableId;
		return new JdbcControl().execute(sql);
	}
	
}
