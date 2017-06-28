package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.SettleAccountModel;
import com.system.util.StringUtil;
import com.system.vmodel.SpFinanceShowModel;

public class SettleAcountDao
{
	@SuppressWarnings("unchecked")
	public List<SpFinanceShowModel> loadSpSettleAccountData(String startDate,String endDate,int spId,int jsType)
	{
		String sql = "SELECT a.* FROM (";
		
		sql += " SELECT a.*,CONCAT(j.`name_cn`,'-',i.name) name_cn ";
		sql += " FROM ( ";
		sql += " SELECT  f.id sp_id,d.id sp_trone_id,d.`product_id`,f.`short_name` sp_name,d.jiesuanlv, ";
		sql += " d.`name` sp_trone_name,SUM(a.data_rows) data_rows,SUM(a.amount) amounts ";
		sql += " FROM daily_log.tbl_mr_summer a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp f ON d.sp_id = f.id ";
		sql += " WHERE a.mr_date >= '" + startDate + "' ";
		sql += " AND a.mr_date <= '" + endDate + "' ";
		
		sql += " AND d.`js_type` = " + jsType;
		
		if(spId>0)
		{
			sql += " AND f.id = " + spId;
		}
		
		sql += " GROUP BY f.id,d.id ";
		sql += " ORDER BY sp_name,sp_trone_name ";
		sql += " )a  ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` h ON a.product_id = h.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` i ON h.`product_1_id` = i.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` j ON i.`operator_id` = j.`id` ";
		
		
		sql += " ) a ";
		
		sql += "WHERE a.sp_id NOT IN ( SELECT sp_id FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` WHERE js_type = " + jsType;
		
		sql += " AND (('" + startDate + "' >= start_date AND '" + startDate + "' <= end_date) OR('" + endDate + "' >= start_date AND '" 
				+ endDate + "' <= end_date) OR('" + startDate + "' <= start_date AND '" + endDate + "' >= end_date)));";
		
		return (List<SpFinanceShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpFinanceShowModel> list = new ArrayList<SpFinanceShowModel>();
				SpFinanceShowModel model = null;
				
				while(rs.next())
				{
					model = new SpFinanceShowModel();
					
					model.setSpId(rs.getInt("sp_id"));
					model.setShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setAmount(rs.getFloat("amounts"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
		
		
		/*
		
		String sql = "select d.id,d.short_name,e.name_cn,c.name,sum(a.amount) amounts,c.jiesuanlv";
		
			sql += " from daily_log.tbl_mr_summer a";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone b on a.trone_id = b.id";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone c on b.sp_trone_id = c.id";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp d on c.sp_id = d.id";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator e on c.operator = e.id";
			sql += " where a.mr_date >= '" + startDate + "' and a.mr_date <= '" + endDate + "'";
			sql += " group by d.id,c.id";
			sql += " order by d.short_name,c.name";
		
		
		return (List<SpFinanceShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpFinanceShowModel> list = new ArrayList<SpFinanceShowModel>();
				SpFinanceShowModel model = null;
				
				while(rs.next())
				{
					model = new SpFinanceShowModel();
					
					model.setSpId(rs.getInt("id"));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setAmount(rs.getFloat("amounts"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
		
		*/
	}
	
	//2016.07.25 废弃BY ANDY.CHEN 因为一个简单的查询就接近一分钟，优化后，查询不到一秒
	/*
	 *
	@SuppressWarnings("unchecked")
	public List<SpFinanceShowModel> loadCpSettleAccountData(String startDate,String endDate,int cpId,int jsType)
	{
		String sql = " SELECT f.id,f.short_name,h.short_name sp_name, CONCAT(k.`name_cn`,'-',j.name) name_cn,d.name,sum(a.amount) amounts,g.rate jiesuanlv ";
		sql += " FROM daily_log.`tbl_cp_mr_summer` a  ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id`";
		sql += " Left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c on b.trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id`";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f on b.cp_id = f.id ";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate g on f.id = g.cp_id and d.id = g.sp_trone_id ";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp h on d.sp_id = h.id ";		
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 i on d.product_id = i.id ";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 j on i.product_1_id = j.id ";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator k on j.operator_id = k.id ";
		
		sql += " where a.mr_date >= '" + startDate + "' and a.mr_date <= '" + endDate + "' ";
		
		if(cpId>0)
		{
			sql += " and f.id = " + cpId;
		}
		
		if(jsType>=0)
		{
			sql += " and g.js_type = " + jsType;
		}
		
		sql += " group by f.id,d.id order by f.short_name,d.name";
		
		return (List<SpFinanceShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpFinanceShowModel> list = new ArrayList<SpFinanceShowModel>();
				SpFinanceShowModel model = null;
				
				while(rs.next())
				{
					model = new SpFinanceShowModel();
					
					model.setSpId(rs.getInt("id"));
					model.setShortName(StringUtil.getString(rs.getString("short_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_name") + "-" + rs.getString("name"), ""));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setAmount(rs.getFloat("amounts"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	*/
	
	
	@SuppressWarnings("unchecked")
	public List<SpFinanceShowModel> loadCpSettleAccountData(String startDate,String endDate,int cpId,int jsType)
	{
		String sql = "SELECT a.* FROM (";
		
		sql += " SELECT a.*,g.`rate` jiesuanlv,CONCAT(j.`name_cn`,'-',i.name) name_cn ";
		sql += " FROM ( ";
		sql += " SELECT  f.id cp_id,d.id sp_trone_id,d.`product_id`,f.`short_name` cp_name, ";
		sql += " d.`name` sp_trone_name,SUM(a.data_rows) data_rows,SUM(a.amount) amounts ";
		sql += " FROM daily_log.tbl_cp_mr_summer a ";
		sql += " LEFT JOIN    " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON b.cp_id = f.id ";
		sql += " WHERE a.mr_date >= '" + startDate + "' ";
		sql += " AND a.mr_date <= '" + endDate + "' ";
		
		if(cpId>0)
		{
			sql += " AND f.id = " + cpId;
		}
		
		sql += " GROUP BY f.id,d.id ";
		sql += " ORDER BY cp_name,sp_trone_name ";
		sql += " )a  ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate g ON a.cp_id = g.cp_id AND a.sp_trone_id = g.sp_trone_id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` h ON a.product_id = h.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` i ON h.`product_1_id` = i.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` j ON i.`operator_id` = j.`id` ";
		sql += " WHERE g.`js_type` = " + jsType;
		
		sql += " ) a ";
		
		sql += "WHERE a.cp_id NOT IN ( SELECT cp_id FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` WHERE js_type = " + jsType;
		
		sql += " AND (('" + startDate + "' >= start_date AND '" + startDate + "' <= end_date) OR('" + endDate + "' >= start_date AND '" 
				+ endDate + "' <= end_date) OR('" + startDate + "' <= start_date AND '" + endDate + "' >= end_date)));";
		
		return (List<SpFinanceShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpFinanceShowModel> list = new ArrayList<SpFinanceShowModel>();
				SpFinanceShowModel model = null;
				
				while(rs.next())
				{
					model = new SpFinanceShowModel();
					
					model.setSpId(rs.getInt("cp_id"));
					model.setShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setAmount(rs.getFloat("amounts"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SpFinanceShowModel> loadCpSettleAccountDataAll(String startDate,String endDate,int cpId,int jsType)
	{
		String sql = "SELECT a.* FROM (";
		
		sql += " SELECT a.*,g.`rate` jiesuanlv,CONCAT(j.`name_cn`,'-',i.name) name_cn ";
		sql += " FROM ( ";
		sql += " SELECT  f.id cp_id,d.id sp_trone_id,d.`product_id`,f.`short_name` cp_name, ";
		sql += " d.`name` sp_trone_name,SUM(a.data_rows) data_rows,SUM(a.amount) amounts ";
		sql += " FROM daily_log.tbl_cp_mr_summer a ";
		sql += " LEFT JOIN    " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp f ON b.cp_id = f.id ";
		sql += " WHERE a.mr_date >= '" + startDate + "' ";
		sql += " AND a.mr_date <= '" + endDate + "' ";
		
		if(cpId>0)
		{
			sql += " AND f.id = " + cpId;
		}
		
		sql += " GROUP BY f.id,d.id ";
		sql += " ORDER BY cp_name,sp_trone_name ";
		sql += " )a  ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate g ON a.cp_id = g.cp_id AND a.sp_trone_id = g.sp_trone_id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` h ON a.product_id = h.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` i ON h.`product_1_id` = i.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` j ON i.`operator_id` = j.`id` ";
		sql += " WHERE g.`js_type` = " + jsType;
		
		sql += " ) a ";
		
		return (List<SpFinanceShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpFinanceShowModel> list = new ArrayList<SpFinanceShowModel>();
				SpFinanceShowModel model = null;
				
				while(rs.next())
				{
					model = new SpFinanceShowModel();
					
					model.setSpId(rs.getInt("cp_id"));
					model.setShortName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setAmount(rs.getFloat("amounts"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SpFinanceShowModel> loadSpSettleAccountDataAll(String startDate,String endDate,int spId,int jsType)
	{
		String sql = "SELECT a.* FROM (";
		
		sql += " SELECT a.*,CONCAT(j.`name_cn`,'-',i.name) name_cn ";
		sql += " FROM ( ";
		sql += " SELECT  f.id sp_id,d.jiesuanlv,d.js_type,d.id sp_trone_id,d.`product_id`,f.`short_name` sp_name, ";
		sql += " d.`name` sp_trone_name,SUM(a.data_rows) data_rows,SUM(a.amount) amounts ";
		sql += " FROM daily_log.tbl_mr_summer a ";
		sql += " LEFT JOIN    " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp f ON d.sp_id = f.id ";
		sql += " WHERE a.mr_date >= '" + startDate + "' ";
		sql += " AND a.mr_date <= '" + endDate + "' ";
		
		if(spId>0)
		{
			sql += " AND f.id = " + spId;
		}
		
		sql += " GROUP BY f.id,d.id ";
		sql += " ORDER BY sp_name,sp_trone_name ";
		sql += " )a  ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` h ON a.product_id = h.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` i ON h.`product_1_id` = i.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` j ON i.`operator_id` = j.`id` ";
		sql += " WHERE a.`js_type` = " + jsType;
		
		sql += " ) a ";
		
		return (List<SpFinanceShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpFinanceShowModel> list = new ArrayList<SpFinanceShowModel>();
				SpFinanceShowModel model = null;
				
				while(rs.next())
				{
					model = new SpFinanceShowModel();
					
					model.setSpId(rs.getInt("sp_id"));
					model.setShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setAmount(rs.getFloat("amounts"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SettleAccountModel> loadSpSettleAccountData(int spId,String startDate,String endDate,int dateType)
	{
		String sql = "SELECT c.`name`,CONCAT(k.`name_cn`,'-',j.name) name_cn,SUM(a.amount) total_amount,c.jiesuanlv";
		//sql += " FROM daily_log.`tbl_cp_mr_summer` a";
		
		sql += " FROM (SELECT * FROM daily_log.`tbl_mr_summer` WHERE mr_date >= '" + startDate + "' and mr_date <= '" + endDate + "') a";
		
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone e ON b.`trone_id` = e.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON e.`sp_trone_id` = c.`id`";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp g on c.sp_id = g.id";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 i on c.product_id = i.id ";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 j on i.product_1_id = j.id ";
		sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator k on j.operator_id = k.id ";
		
		sql += " where c.`sp_id` =  " + spId;
		sql += " and c.js_type = " + dateType;
		
		//sql += " and a.mr_date >= '" + startDate + "' and a.mr_date <= '" + endDate + "'";
		
		sql += " group by c.id";
		sql += " ORDER BY name_cn,name";
	
	return (List<SettleAccountModel>)new JdbcControl().query(sql, new QueryCallBack()
	{
		@Override
		public Object onCallBack(ResultSet rs) throws SQLException
		{
			List<SettleAccountModel> list = new ArrayList<SettleAccountModel>();
			SettleAccountModel model = null;
			
			while(rs.next())
			{
				model = new SettleAccountModel();
				
				model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
				model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
				model.setAmount(rs.getFloat("total_amount"));
				model.setJiesuanlv(rs.getFloat("jiesuanlv"));
				
				list.add(model);
			}
			
			return list;
		}
	});
	}
	
	@SuppressWarnings("unchecked")
	public List<SettleAccountModel> loadCpSettleAccountData(int cpId,String startDate,String endDate,int dateType)
	{
		String sql = "SELECT c.`name`,CONCAT(k.`name_cn`,'-',j.name) name_cn,SUM(a.amount) total_amount,f.rate jiesuanlv";
			//sql += " FROM daily_log.`tbl_cp_mr_summer` a";
			
			sql += " FROM (SELECT * FROM daily_log.`tbl_cp_mr_summer` WHERE mr_date >= '" + startDate + "' and mr_date <= '" + endDate + "') a";
			
			sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id` ";
			sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone e ON b.`trone_id` = e.`id`";
			sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON e.`sp_trone_id` = c.`id`";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp g on b.cp_id = g.id";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_trone_rate f on g.id = f.cp_id and c.id = f.sp_trone_id";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_2 i on c.product_id = i.id ";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_product_1 j on i.product_1_id = j.id ";
			sql += " left join " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_operator k on j.operator_id = k.id ";
			
			sql += " where a.`cp_id` =  " + cpId;
			sql += " and f.js_type = " + dateType;
			
			//sql += " and a.mr_date >= '" + startDate + "' and a.mr_date <= '" + endDate + "'";
			
			sql += " group by c.id";
			sql += " ORDER BY name_cn,name";
		
		return (List<SettleAccountModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SettleAccountModel> list = new ArrayList<SettleAccountModel>();
				SettleAccountModel model = null;
				
				while(rs.next())
				{
					model = new SettleAccountModel();
					
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("name"), ""));
					model.setAmount(rs.getFloat("total_amount"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	/**
	 * SP用户查看自己的账务基础
	 * @param startDate
	 * @param endDate
	 * @param spId
	 * @param jsType
	 * @param userId
	 * @return
	 */
	 
	@SuppressWarnings("unchecked")
	public List<SpFinanceShowModel> loadSpSettleAccountDataAll(String startDate,String endDate,int spId,int jsType,int userId,int rightType)
	{
		String sql = "SELECT a.* FROM (";
		
		sql += " SELECT a.*,CONCAT(j.`name_cn`,'-',i.name) name_cn ";
		sql += " FROM ( ";
		sql += " SELECT  f.id sp_id,d.jiesuanlv,d.js_type,d.id sp_trone_id,d.`product_id`,f.`short_name` sp_name, ";
		sql += " d.`name` sp_trone_name,SUM(a.data_rows) data_rows,SUM(a.amount) amounts ";
		sql += " FROM daily_log.tbl_mr_summer a ";
		sql += " LEFT JOIN    " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN  " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp f ON d.sp_id = f.id ";
		sql += " WHERE a.mr_date >= '" + startDate + "' ";
		sql += " AND a.mr_date <= '" + endDate + "' ";
		if(rightType>0){
			sql += " AND (f.commerce_user_id="+userId+" or f.commerce_user_id in ("+getRigthListByUser(userId)+"))";
		}
		if(spId>0)
		{
			sql += " AND f.id = " + spId;
		}
		
		sql += " GROUP BY f.id,d.id ";
		sql += " ORDER BY sp_name,sp_trone_name ";
		sql += " )a  ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` h ON a.product_id = h.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` i ON h.`product_1_id` = i.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` j ON i.`operator_id` = j.`id` ";
		sql += " WHERE a.`js_type` = " + jsType;
		
		sql += " ) a ";
		
		return (List<SpFinanceShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpFinanceShowModel> list = new ArrayList<SpFinanceShowModel>();
				SpFinanceShowModel model = null;
				
				while(rs.next())
				{
					model = new SpFinanceShowModel();
					
					model.setSpId(rs.getInt("sp_id"));
					model.setShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setAmount(rs.getFloat("amounts"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	public String getRigthListByUser(int userId){
		String sql="select right_list from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_ds_user_right ur where ur.user_id="+userId+" and ur.type=0";
		return (String)new JdbcControl().query(sql, new QueryCallBack(){

			@Override
			public Object onCallBack(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				String str=null;
				while (rs.next()) {
					str=StringUtil.getString(rs.getString("right_list"), "-1");
				}
				return str;
			}
			
		});
	}
	/**
	 * SP商务只能看到自己的账单基础数据
	 * @param startDate
	 * @param endDate
	 * @param spId
	 * @param jsType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SpFinanceShowModel> loadSpSettleAccountData(String startDate,String endDate,int spId,int jsType,int userId,int rightType)
	{
		String sql = "SELECT a.* FROM (";
		
		sql += " SELECT a.*,CONCAT(j.`name_cn`,'-',i.name) name_cn ";
		sql += " FROM ( ";
		sql += " SELECT  f.id sp_id,d.id sp_trone_id,d.`product_id`,f.`short_name` sp_name,d.jiesuanlv, ";
		sql += " d.`name` sp_trone_name,SUM(a.data_rows) data_rows,SUM(a.amount) amounts ";
		sql += " FROM daily_log.tbl_mr_summer a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.trone_id = c.id ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp f ON d.sp_id = f.id ";
		sql += " WHERE a.mr_date >= '" + startDate + "' ";
		sql += " AND a.mr_date <= '" + endDate + "' ";
		if(rightType>0){
			sql += " AND (f.commerce_user_id="+userId+" or f.commerce_user_id in ("+getRigthListByUser(userId)+"))";
		}
		sql += " AND d.`js_type` = " + jsType;
		
		if(spId>0)
		{
			sql += " AND f.id = " + spId;
		}
		
		sql += " GROUP BY f.id,d.id ";
		sql += " ORDER BY sp_name,sp_trone_name ";
		sql += " )a  ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` h ON a.product_id = h.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` i ON h.`product_1_id` = i.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` j ON i.`operator_id` = j.`id` ";
		
		
		sql += " ) a ";
		
		sql += "WHERE a.sp_id NOT IN ( SELECT sp_id FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` WHERE js_type = " + jsType;
		
		sql += " AND (('" + startDate + "' >= start_date AND '" + startDate + "' <= end_date) OR('" + endDate + "' >= start_date AND '" 
				+ endDate + "' <= end_date) OR('" + startDate + "' <= start_date AND '" + endDate + "' >= end_date)));";
		
		return (List<SpFinanceShowModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpFinanceShowModel> list = new ArrayList<SpFinanceShowModel>();
				SpFinanceShowModel model = null;
				
				while(rs.next())
				{
					model = new SpFinanceShowModel();
					
					model.setSpId(rs.getInt("sp_id"));
					model.setShortName(StringUtil.getString(rs.getString("sp_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setOperatorName(StringUtil.getString(rs.getString("name_cn"), ""));
					model.setAmount(rs.getFloat("amounts"));
					model.setJiesuanlv(rs.getFloat("jiesuanlv"));
					
					list.add(model);
				}
				
				return list;
			}
		});
		
		
	
	}
	
	
	
}
