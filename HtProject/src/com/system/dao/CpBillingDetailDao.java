package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CpBillingSptroneDetailModel;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;

public class CpBillingDetailDao
{
	@SuppressWarnings("unchecked")
	public List<CpBillingSptroneDetailModel> loadCpBillingSpTroneDetail(final int cpBillingId)
	{
		String sql = "SELECT a.*,b.`name`";
		sql += " FROM daily_log.`tbl_cp_billing_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id`";
		sql += " WHERE a.`cp_billing_id` = " + cpBillingId;
		
		return (List<CpBillingSptroneDetailModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpBillingSptroneDetailModel> list = new ArrayList<CpBillingSptroneDetailModel>();
				while(rs.next())
				{
					CpBillingSptroneDetailModel model = new CpBillingSptroneDetailModel();
					
					model.setAmount(rs.getFloat("amount"));
					model.setId(rs.getInt("id"));
					model.setCpBillingId(cpBillingId);
					model.setReduceAmount(rs.getFloat("reduce_amount"));
					model.setReduceType(rs.getInt("reduce_type"));
					model.setRate(rs.getFloat("rate"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	public CpBillingSptroneDetailModel getSingleCpBillingSpTroneDetailModel(int id)
	{
		String sql = "SELECT a.*,b.`name`";
		sql += " FROM daily_log.`tbl_cp_billing_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id`";
		sql += " WHERE a.`id` = " + id;
		
		return (CpBillingSptroneDetailModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					CpBillingSptroneDetailModel model = new CpBillingSptroneDetailModel();
					
					model.setAmount(rs.getFloat("amount"));
					model.setId(rs.getInt("id"));
					model.setCpBillingId(rs.getInt("cp_billing_id"));
					model.setReduceAmount(rs.getFloat("reduce_amount"));
					model.setReduceType(rs.getInt("reduce_type"));
					model.setRate(rs.getFloat("rate"));
					model.setStatus(rs.getInt("status"));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					
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
	public void updateSingleCpBillingSpTroneDetail(CpBillingSptroneDetailModel model)
	{
		String sql = "update daily_log.`tbl_cp_billing_sp_trone` set reduce_amount = " 
				+ model.getReduceAmount() + "  , status = " + model.getStatus() + ", remark = '" + SqlUtil.sqlEncode(model.getRemark()) 
				+ "',reduce_type = " + model.getReduceType() + " where id = " + model.getId();
		
		new JdbcControl().execute(sql);		
	}
}
