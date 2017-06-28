package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SpBillingSptroneDetailModel;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;

public class SpBillingDetailDao
{
	@SuppressWarnings("unchecked")
	public List<SpBillingSptroneDetailModel> loadSpBillingSpTroneDetail(final int spBillingId)
	{
		String sql = "SELECT a.*,b.`name`";
		sql += " FROM daily_log.`tbl_sp_billing_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id`";
		sql += " WHERE a.`sp_billing_id` = " + spBillingId;
		
		return (List<SpBillingSptroneDetailModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpBillingSptroneDetailModel> list = new ArrayList<SpBillingSptroneDetailModel>();
				while(rs.next())
				{
					SpBillingSptroneDetailModel model = new SpBillingSptroneDetailModel();
					
					model.setAmount(rs.getFloat("amount"));
					model.setId(rs.getInt("id"));
					model.setSpBillingId(spBillingId);
					model.setReduceAmount(rs.getFloat("reduce_amount"));
					model.setReduceType(rs.getInt("reduce_type"));
					model.setRate(rs.getFloat("rate"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
					model.setReduceDataAmount(rs.getFloat("reduce_data_amount"));
					model.setReduceMoneyAmount(rs.getFloat("reduce_money_amount"));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	public SpBillingSptroneDetailModel getSingleSpBillingSpTroneDetailModel(int id)
	{
		String sql = "SELECT a.*,b.`name`";
		sql += " FROM daily_log.`tbl_sp_billing_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id`";
		sql += " WHERE a.`id` = " + id;
		
		return (SpBillingSptroneDetailModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SpBillingSptroneDetailModel model = new SpBillingSptroneDetailModel();
					
					model.setAmount(rs.getFloat("amount"));
					model.setId(rs.getInt("id"));
					model.setSpBillingId(rs.getInt("sp_billing_id"));
					model.setReduceAmount(rs.getFloat("reduce_amount"));
					model.setReduceType(rs.getInt("reduce_type"));
					model.setRate(rs.getFloat("rate"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
					model.setReduceDataAmount(rs.getFloat("reduce_data_amount"));
					model.setReduceMoneyAmount(rs.getFloat("reduce_money_amount"));
					
					return model;
				}
				return null;
			}
		});
	}
	
	/**
	 * 更新某个帐单下面指定业务的数据
	 * @param model
	 */
	public void updateSingleSpBillingSpTroneDetail(SpBillingSptroneDetailModel model)
	{
		String sql = "update daily_log.`tbl_sp_billing_sp_trone` set reduce_amount = " 
				+ model.getReduceAmount() + "  , status = " + model.getStatus() + ", remark = '" + SqlUtil.sqlEncode(model.getRemark()) 
				+ "',reduce_type = " + model.getReduceType() + ", reduce_data_amount = "+ model.getReduceDataAmount() +", reduce_money_amount = " + model.getReduceMoneyAmount() + " where id = " + model.getId();
		
		new JdbcControl().execute(sql);		
	}
}
