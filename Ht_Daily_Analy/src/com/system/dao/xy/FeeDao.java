package com.system.dao.xy;

import com.system.database.JdbcControl;

public class FeeDao
{
	//将每个渠道每个应用的收入从基础表tbl_xypay_分析进渠道应用汇总表tbl_xypay_summer
	//再将分析出来的结果再再分析进应用汇总表tbl_xy_fee_summer
	public boolean analyFeeToSummer(String tableName,String startDate,String endDate)
	{
		String sql = " INSERT INTO game_log.`tbl_xypay_summer`(fee_date,appkey,channelid,data_rows,amount) ";
		sql += " SELECT DATE_FORMAT(createdate,'%Y-%m-%d') fee_date,appkey,channelid,";
		sql += " COUNT(*) data_rows,sum(amount)/100 amounts ";
		sql += " FROM game_log.`tbl_xypay_" + tableName + "`";
		sql += " where createdate >= '" + startDate + " 00:00:00' AND createdate <= '" + endDate + " 23:59:59' and status = 0 ";
		sql += " GROUP BY fee_date,appkey,channelid ";
		sql += " ORDER BY fee_date,appkey,channelid";
		
		JdbcControl control = new JdbcControl();
		
		control.execute(sql);
		
		String sql2 = "insert into game_log.tbl_xy_fee_summer (fee_date,appkey,amount)";
		sql2 += " select fee_date,appkey,sum(amount) amounts ";
		sql2 += " from game_log.tbl_xypay_summer";
		sql2 += " where fee_date >= '" + startDate + "' and fee_date <= '" + endDate + "' group by appkey";
		
		control.execute(sql2);
		
		return true;
	}
	
	//删除相同时间段内已经分析过的数据
	public boolean deleteFeeFromSummer(String startDate,String endDate)
	{ 
		JdbcControl control = new JdbcControl();
		control.execute("DELETE FROM game_log.`tbl_xypay_summer` WHERE fee_date >= '" + startDate + "' AND fee_date <= '" + endDate + "'");
		control.execute("DELETE FROM game_log.`tbl_xy_fee_summer` WHERE fee_date >= '" + startDate + "' AND fee_date <= '" + endDate + "'");
		return true;
	}
	
	//更新游戏CP(CPS合作)扣量后的收入
	public boolean updateQdAmount(String startDate,String endDate)
	{
		String sql = " UPDATE game_log.`tbl_xy_fee_summer` a,daily_config.`tbl_xy_app` b "
				+ " SET show_amount = a.`amount`*(100-b.`hold_percent`)/100  "
				+ " WHERE show_amount = 0  AND a.`appkey` = b.`appkey`"
				+ " AND fee_date >= '" + startDate + "' "
				+ " AND fee_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	
	//更新渠道(CPS合作)扣量后的收入
	public boolean updateChannelShowAmout(String startDate,String endDate)
	{
		String sql = " UPDATE game_log.`tbl_xypay_summer` a,daily_config.`tbl_xy_channel` b "
				+ " SET show_amount = floor(a.`amount`*(100-b.`hold_percent`)/100)  "
				+ " WHERE show_amount = 0  AND a.`channelid` = b.`channel` AND b.settle_type = 2 "
				+ " AND fee_date >= '" + startDate + "' "
				+ " AND fee_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	
	//更新游戏CP(CPS合作)的展示状态
	public boolean updateQdAmountStatus(String startDate,String endDate)
	{
		String sql = "UPDATE game_log.`tbl_xy_fee_summer` set status = 1 where fee_date >= '" + startDate + "' "
				+ "AND fee_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	
	//更新 渠道(CPS合作) 的展示状态
	public boolean updateChannelShowAmountStatus(String startDate,String endDate)
	{
		String sql = "UPDATE game_log.`tbl_xypay_summer` set status = 1 where fee_date >= '" + startDate + "' "
				+ "AND fee_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	
}
