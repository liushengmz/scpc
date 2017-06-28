
package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import com.system.database.JdbcControl;
import com.system.database.JdbcControlLvLog;
import com.system.database.QueryCallBack;
import com.system.model.LvMrModel;
import com.system.util.StringUtil;

public class LvMrDao
{
	/**
	 * 根据orderId进行选择
	 * 
	 * @param orderId
	 *            为空时，为当前表
	 * @return
	 */
	static String getTableName(String orderId)
	{
		if (StringUtil.isNullOrEmpty(orderId))
			return getTableName();
		int m;
		try
		{
			m = Integer.parseInt(orderId.substring(0, 2));
		}
		catch (Exception ex)
		{
			return getTableName();
		}
		Calendar cal = Calendar.getInstance();
		int cMonth = cal.get(Calendar.MONTH) + 1;
		int cYear = cal.get(Calendar.YEAR);
		if (cMonth < m)
			cYear--;
		return String.format("tbl_mr_%d%02d", cYear, m);
	}

	/**
	 * 当月表
	 * 
	 * @return
	 */
	static String getTableName()
	{
		Calendar cal = Calendar.getInstance();
		int cMonth = cal.get(Calendar.MONTH) + 1;
		int cYear = cal.get(Calendar.YEAR);
		return String.format("tbl_mr_%d%02d", cYear, cMonth);
	}

	public Boolean existed(String orderId)
	{
		String tabName = getTableName(orderId);
		String sql = "select orderId from little_video_log." + tabName
				+ " where orderid='" + StringUtil.sqlEncode(orderId)
				+ "'  limit 1";

		QueryCallBack callBack = new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				return rs.next();
			};
		};
		Object obj = new JdbcControl().query(sql, callBack);
		if (obj == null)
			return true;
		return (Boolean) obj;

		// String curTabName = getTableName();
		//
		// sql = "select orderId from little_video_log." + curTabName
		// + " where orderid='" + StringUtil.sqlEncode(orderId)
		// + "' limit 1";
		//
		// return (Boolean) new JdbcControl().query(sql, callBack);

	}

	public void insert(LvMrModel mr)
	{
		String insSql = "insert into little_video_log."
				+ getTableName(mr.getOrderId())
				+ "( orderid, price, appkey, channel, pay_order_id, status,level_id, pay_type,pay_type_id)"
				+ " values( ?, ?, ?, ?, ?, ?,?,?)";
		HashMap<Integer, Object> map = new HashMap<Integer, Object>();
		int i = 0;
		map.put(++i, mr.getOrderId());
		map.put(++i, mr.getPrice());
		map.put(++i, mr.getAppkey());
		map.put(++i, mr.getChannel());
		map.put(++i, mr.getPayOrderId());
		map.put(++i, mr.getStatus());
		map.put(++i, mr.getLevelId());
		map.put(++i, mr.getPayType());
		map.put(++i, mr.getPayTypeId());

		JdbcControlLvLog jdbc = new JdbcControlLvLog();
		mr.setId(jdbc.insertWithGenKey(insSql, map));
		return;
	}

}
