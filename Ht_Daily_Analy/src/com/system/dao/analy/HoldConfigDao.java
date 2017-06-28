package com.system.dao.analy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.analy.HoldConfigModel;

public class HoldConfigDao 
{
	/**
	 * 把配置的数据按指定的日期删除
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean deleteHoldConfig(String startDate,String endDate)
	{
		String sql = "delete from daily_log.tbl_hold_config where mr_date >= '" + startDate + "' and mr_date <= '" + endDate + "'";
		
		return new JdbcControl().execute(sql);
	}
	
	/**
	 * 按指定的日期把数据分析进表tbl_hold_config
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean insertHoldConfig(String startDate,String endDate)
	{
		String sql = "insert into daily_log.tbl_hold_config(mr_date,cp_id,total_amount,create_time) "
				+ "select mr_date,cp_id,sum(amount) total_amount,now()"
				+ "from daily_log.tbl_mr_summer where mr_date >= '" + startDate + "' and mr_date <= '" + endDate + "' "
				+ "group by cp_id,mr_date";		
		
		return new JdbcControl().execute(sql);
	}
	
	/**
	 * 加载指定日期的扣量配置
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HoldConfigModel> loadHoldConfig(String startDate,String endDate)
	{
		String sql = "select * from daily_log.tbl_hold_config where mr_date >= '" + startDate + "' and mr_date <= '" + endDate + "' order by mr_date asc";
		
		return (ArrayList<HoldConfigModel>)new JdbcControl().query(sql, new QueryCallBack() 
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException 
			{
				ArrayList<HoldConfigModel> list = new ArrayList<HoldConfigModel>();
				
				HoldConfigModel model = null;
				
				while(rs.next())
				{
					model = new HoldConfigModel();
					
					model.setCpId(rs.getInt("cp_id"));
					model.setFeeDate(rs.getString("mr_date"));
					model.setTotalAmount(rs.getFloat("total_amount"));
					model.setHoldType(rs.getInt("hold_type"));
					model.setHoldPersent(rs.getInt("hold_persent"));
					model.setSynAmount(rs.getFloat("syn_amount"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public boolean updateHoldConfigActureAmount(String startDate,String endDate)
	{
		String sql = "update daily_log.tbl_hold_config a left join ( select mr_date,cp_id,sum(amount) "
				+ "acture_amount from daily_log.tbl_cp_mr_summer where mr_date >= '" + startDate + "' "
				+ "and mr_date <= '" + endDate + "' group by mr_date,cp_id )b on a.mr_date = b.mr_date "
				+ "and a.cp_id = b.cp_id set acture_syn_amount = b.acture_amount,edit_able=1";
		
		return new JdbcControl().execute(sql);
	}
	
	
}
