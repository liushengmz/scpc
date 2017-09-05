package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.SysConstant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.ToBusinessChildOrderModel;
import com.system.model.ToBusinessOrderModel;

public class ToBusinessDataDao
{
	/**
	 * 记录下TO_B的请求定单，返回自增长ID
	 * @param model
	 * @return
	 */
	public int recordCpOrder(ToBusinessOrderModel model,String month)
	{
		String sql = "INSERT INTO " + SysConstant.DB_LOG_MAIN + ".tbl_f_cp_order_" + month + "(cp_id,orderid,child_size) VALUE(?,?,?)";
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(1, model.getCpId());
		map.put(2, model.getOrderId());
		map.put(3, model.getOrderList().size());
		return new JdbcControl().insertWithGenKey(sql, map);
	}
	
	
	/**
	 * 根据CPID和定单号查询指定的月份中是否存在数据，如果存在则返回ID号，否则返回-1
	 * @param model
	 * @param month
	 * @return
	 */
	public int queryCpOrder(ToBusinessOrderModel model,String month)
	{
		String sql = "SELECT id FROM daily_log.tbl_f_cp_order_" + month + " WHERE cp_id = " + model.getCpId() + " AND orderid = '" + model.getOrderId() + "'";
		return (Integer)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					return rs.getInt("id");
				}
				return -1;
			}
		});
	}
	
	/**
	 * 将CP定单的数据详情存储进CP定单临时表
	 * @param model
	 * @param month
	 * @return
	 */
	public int saveCpQueryToTempTable(ToBusinessOrderModel model,String month)
	{
		String sql = "INSERT INTO " + SysConstant.DB_LOG_MAIN + ".tbl_f_cp_order(month,month_id,cp_id,orderid,child_size) VALUE(?,?,?,?,?)";
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		map.put(1, month);
		map.put(2, model.getId());
		map.put(3, model.getCpId());
		map.put(4, model.getOrderId());
		map.put(5, model.getOrderList().size());
		return new JdbcControl().insertWithGenKey(sql, map);
	}
	
	public static void main(String[] args)
	{
		ToBusinessOrderModel model = new ToBusinessOrderModel();
		model.setCpId(125);
		model.setOrderId("JSONDSILSIDF");
		List<ToBusinessChildOrderModel> list = new ArrayList<ToBusinessChildOrderModel>();
		for(int i=0;i<3; i++)
		{
			list.add(new ToBusinessChildOrderModel());
		}
		model.setOrderList(list);
		model.setId(3);
		System.out.println(new ToBusinessDataDao().saveCpQueryToTempTable(model, "201709"));
	}
	
	
	
	
	
}
