package com.system.dao;

import com.system.database.JdbcControl;

public class SpProductDao
{
	public void syncUnAddCpSpTroneRate()
	{
		String sql = " INSERT INTO " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_product`(sp_id,product_id)";
		sql += " SELECT a.sp_id,a.`product_id` FROM";
		sql += " (";
		sql += " SELECT sp_id,product_id FROM (";
		sql += " SELECT e.id sp_id,c.id product_id,COUNT(*) ss";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` b ON a.`product_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` c ON b.`product_1_id` = c.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` d ON c.`operator_id` = d.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` e ON a.`sp_id` = e.`id`";
		sql += " GROUP BY e.id,c.`id`";
		sql += " )a WHERE product_id IS NOT NULL";
		sql += " )a LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_product` b ";
		sql += " ON a.sp_id = b.`sp_id` AND a.product_id = b.`product_id`";
		sql += " WHERE b.id IS NULL";
		
		new JdbcControl().execute(sql);
	}
}
