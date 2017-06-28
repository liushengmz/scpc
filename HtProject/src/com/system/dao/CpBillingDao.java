package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.CpBillingModel;
import com.system.model.CpBillingSpTroneModel;
import com.system.model.CpBillingTroneOrderDetailModel;
import com.system.model.SettleAccountModel;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;
import com.system.model.CpBillExportModel;

public class CpBillingDao
{
	/**
	 * 开始CP对帐
	 * 	 
	 * @param cpId
	 * @param jsType
	 * @param startDate
	 * @param endDate
	 */
	public int addCpBilling(int cpId,int jsType,String startDate,String endDate,float preBilling,float amount)
	{
		String sql = "INSERT INTO " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing`(cp_id,js_type,start_date,end_date,pre_billing,amount) VALUES(?,?,?,?,?,?)";
		
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		
		map.put(1, cpId);
		map.put(2, jsType);
		map.put(3, startDate);
		map.put(4, endDate);
		map.put(5, preBilling);
		map.put(6, amount);
		
		return new JdbcControl().insertWithGenKey(sql, map);
	}
	
	/**
	 * 是否重复出帐单了
	 * @param cpId
	 * @param jsType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean isCpBillingCross(int cpId,int jsType,String startDate,String endDate)
	{
		String sql = "SELECT COUNT(*) FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` WHERE cp_id = " + cpId + " AND js_type = " + jsType;
		sql += " AND (('" + startDate + "' >= start_date AND '" + startDate + "' <= end_date) OR('" + endDate + "' >= start_date AND '" 
				+ endDate + "' <= end_date) OR('" + startDate + "' <= start_date AND '" + endDate + "' >= end_date))";
		
		int rows = (Integer)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
					return rs.getInt(1);
				
				return 0;
			}
		});
		
		return rows>0;
	}
	
	/**
	 * 获取CP对应帐单的基础数据
	 */
	/* 这佧数据，暂时把它封存吧，因为不知道它到底什么时候会启用
	@SuppressWarnings("unchecked")
	public List<CpBillingSptroneDetailModel> loadCpBillingSpTroneDetailOri(int cpId,int jsType,String startDate,String endDate)
	{
		String sql = "SELECT b.cp_id,c.`sp_trone_id`,a.`mr_date`,SUM(a.amount) amount,d.`rate`";
		sql += " FROM  ";
		sql += " ( ";
		sql += " SELECT mr_date,trone_order_id,SUM(amount) amount ";
		sql += " FROM daily_log.`tbl_cp_mr_summer` a WHERE a.`mr_date` >= '" + startDate + "' AND a.`mr_date` <= '" + endDate + "' ";
		sql += " GROUP BY mr_date,trone_order_id ";
		sql += " )a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` c ON b.`trone_id` = c.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_trone_rate` d ON b.`cp_id` = d.`cp_id` AND c.`sp_trone_id` = d.`sp_trone_id` ";
		sql += " WHERE 1=1 AND b.`cp_id` = " + cpId + "  AND d.`js_type` = " + jsType;
		sql += " GROUP BY a.`mr_date`,c.`sp_trone_id`";

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
					model.setMrDate(rs.getString("mr_date"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setRate(rs.getFloat("rate"));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	*/
	
	/**
	 * 获取CP对应帐单的基础数据
	 */
	@SuppressWarnings("unchecked")
	public List<CpBillingTroneOrderDetailModel> loadCpBillingTroneOrderDetailOri(int cpId,int jsType,String startDate,String endDate)
	{
		String sql = " SELECT a.`mr_date`,a.trone_order_id,a.`province_id`,c.`sp_trone_id`,SUM(a.amount) amount,d.`rate` ";
		sql += " FROM  ";
		sql += " ( ";
		sql += " SELECT mr_date,trone_order_id,province_id,SUM(amount) amount ";
		sql += " FROM daily_log.`tbl_cp_mr_summer` a WHERE a.`mr_date` >= '" + startDate + "' AND a.`mr_date` <= '" + endDate + "' ";
		sql += " GROUP BY mr_date,trone_order_id,province_id ";
		sql += " )a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` c ON b.`trone_id` = c.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_trone_rate` d ON b.`cp_id` = d.`cp_id` AND c.`sp_trone_id` = d.`sp_trone_id` ";
		sql += " WHERE 1=1 AND b.`cp_id` = " + cpId + "  AND d.`js_type` = " + jsType;
		sql += " GROUP BY a.`mr_date`,a.`trone_order_id`,a.`province_id` ";


		return (List<CpBillingTroneOrderDetailModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpBillingTroneOrderDetailModel> list = new ArrayList<CpBillingTroneOrderDetailModel>();
				
				while(rs.next())
				{
					CpBillingTroneOrderDetailModel model = new CpBillingTroneOrderDetailModel();
					
					model.setAmount(rs.getFloat("amount"));
					model.setMrDate(rs.getString("mr_date"));
					model.setProvinceId(rs.getInt("province_id"));
					model.setRate(rs.getFloat("rate"));
					model.setTroneOrderId(rs.getInt("trone_order_id"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					
					list.add(model);
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<CpBillingSpTroneModel> loadCpBillingSpTroneOri(int cpId,int jsType,final String startDate,final String endDate)
	{
		String sql = " SELECT c.`sp_trone_id`,SUM(a.amount) amount,d.`rate` ";
		sql += " FROM  ";
		sql += " ( ";
		sql += " SELECT mr_date,trone_order_id,SUM(amount) amount ";
		sql += " FROM daily_log.`tbl_cp_mr_summer` a WHERE a.`mr_date` >= '" + startDate + "' AND a.`mr_date` <= '" + endDate + "' ";
		sql += " GROUP BY mr_date,trone_order_id ";
		sql += " )a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` c ON b.`trone_id` = c.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_trone_rate` d ON b.`cp_id` = d.`cp_id` AND c.`sp_trone_id` = d.`sp_trone_id` ";
		sql += " WHERE 1=1 AND b.`cp_id` = " + cpId + "  AND d.`js_type` = " + jsType;
		sql += " GROUP BY c.`sp_trone_id` ";

		return (List<CpBillingSpTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpBillingSpTroneModel> list = new ArrayList<CpBillingSpTroneModel>();
				
				while(rs.next())
				{
					CpBillingSpTroneModel model = new CpBillingSpTroneModel();
					
					model.setAmount(rs.getFloat("amount"));
					model.setStartDate(startDate);
					model.setEndDate(endDate);
					model.setRate(rs.getFloat("rate"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					
					list.add(model);
				}
				return list;
			}
		});
		
	}
	
	/**
	 * 获取指定的帐单号
	 * @param id
	 * @return
	 */
	public CpBillingModel getCpBillingModel(int id)
	{
		String sql = "SELECT a.create_date,a.id,a.amount,a.`cp_id`,b.`short_name` cp_name,a.`js_type`,c.`name` js_name,a.`pre_billing`,a.`remark`,a.start_date,a.end_date,a.tax_rate,a.acture_billing,a.status,a.start_bill_date,a.get_bill_date,a.apply_pay_bill_date, a.pay_time ";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` b ON a.`cp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_js_type` c ON a.`js_type` = c.`type_id`";
		sql += " WHERE 1=1 and a.id = " + id;
		
		return (CpBillingModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					CpBillingModel model = new CpBillingModel();
					
					model.setId(rs.getInt("id"));
					model.setCpId(rs.getInt("cp_id"));
					model.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
					model.setEndDate(StringUtil.getString(rs.getString("end_date"), ""));
					model.setStartDate(StringUtil.getString(rs.getString("start_date"), ""));
					model.setJsType(rs.getInt("js_type"));
					model.setJsName(StringUtil.getString(rs.getString("js_name"), ""));
					model.setTaxRate(rs.getFloat("tax_rate"));
					model.setPreBilling(rs.getFloat("pre_billing"));
					model.setActureBilling(rs.getFloat("acture_billing"));
					model.setStatus(rs.getInt("status"));
					model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
					model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
					model.setAmount(rs.getFloat("amount"));
					//新增三个账单时间
					model.setStartBillDate(StringUtil.getString(rs.getString("start_bill_date"), ""));
					model.setGetBillDate(StringUtil.getString(rs.getString("get_bill_date"), ""));
					model.setApplyPayBillDate(StringUtil.getString(rs.getString("apply_pay_bill_date"), ""));
					
					model.setPayTime(StringUtil.getString(rs.getString("pay_time"), ""));


					
					return model;
				}
				return null;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List<SettleAccountModel> exportExcelData(int cpBillingId)
	{
		String sql = "SELECT b.`name`,CONCAT(e.`name_cn`,'-',d.name) name_cn,a.amount,a.reduce_amount,a.rate,a.reduce_type ";
		
		sql += " FROM daily_log.`tbl_cp_billing_sp_trone` a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` c ON b.`product_id` = c.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` d ON c.`product_1_id` = d.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` e ON d.`operator_id` = e.`id` ";
		
		sql += " WHERE 1=1 AND a.status = 0 AND a.cp_billing_id =" + cpBillingId + " order by name_cn,name";
		
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
					model.setAmount(rs.getFloat("amount"));
					model.setReduceAmount(rs.getFloat("reduce_amount"));
					model.setJiesuanlv(rs.getFloat("rate"));
					model.setReduceType(rs.getInt("reduce_type"));
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public Map<String, Object> loadCpBilling(String startDate, String endDate,
			int cpId,int jsType,int status,int pageIndex)
	{
		startDate = SqlUtil.sqlEncode(startDate);
		endDate = SqlUtil.sqlEncode(endDate);		
		
		String sql = "SELECT " + Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` b ON a.`cp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_js_type` c ON a.`js_type` = c.`type_id`";
		sql += " WHERE 1=1";
		
		if(cpId>0)
		{
			sql += " and b.id = " + cpId;
		}
		
		if(!StringUtil.isNullOrEmpty(startDate))
		{
			sql += " and a.start_date >= '" + startDate + "'";
		}
		
		if(!StringUtil.isNullOrEmpty(endDate))
		{
			sql += " and a.end_date <= '" + endDate + "'";
		}
		
		if(jsType>=0)
		{
			sql += " and a.js_type = " + jsType;
		}
		
		if(status>=0)
		{
			sql += " and a.status = " + status;
		}
		
		sql += " ORDER BY a.id DESC";
		

		String limit = " limit " + Constant.PAGE_SIZE * (pageIndex - 1) + ","
				+ Constant.PAGE_SIZE;

		Map<String, Object> map = new HashMap<String, Object>();

		JdbcControl control = new JdbcControl();
		map.put("rows", control.query(
				sql.replace(Constant.CONSTANT_REPLACE_STRING, "count(*)"),
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						if (rs.next())
							return rs.getInt(1);

						return 0;
					}
				}));

		map.put("list", control.query(
				sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.`short_name` cp_name,c.`name` js_name ") + limit,
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<CpBillingModel> list = new ArrayList<CpBillingModel>();
						while (rs.next())
						{
							CpBillingModel model = new CpBillingModel();

							model.setId(rs.getInt("id"));
							model.setCpId(rs.getInt("cp_id"));
							model.setCpName(StringUtil.getString(rs.getString("cp_name"), ""));
							model.setEndDate(StringUtil.getString(rs.getString("end_date"), ""));
							model.setStartDate(StringUtil.getString(rs.getString("start_date"), ""));
							model.setJsType(rs.getInt("js_type"));
							model.setJsName(StringUtil.getString(rs.getString("js_name"), ""));
							model.setTaxRate(rs.getFloat("tax_rate"));
							model.setPreBilling(rs.getFloat("pre_billing"));
							model.setActureBilling(rs.getFloat("acture_billing"));
							model.setReduceAmount(rs.getFloat("reduce_amount"));
							model.setStatus(rs.getInt("status"));
							model.setRemark(StringUtil.getString(rs.getString("remark"), ""));
							model.setCreateDate(StringUtil.getString(rs.getString("create_date"), ""));
							model.setAmount(rs.getFloat("amount"));
							//增加开票金额
							model.setKaipiaoBilling(rs.getFloat("kaipiao_billing"));
							
							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}
	
	/**
	 * 删除指定对帐单
	 * @param cpBillingId
	 */
	public void delCpBilling(int cpBillingId)
	{
		String sql1 = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` WHERE id = " + cpBillingId;
		String sql2 = "DELETE FROM daily_log.tbl_cp_billing_sp_trone where cp_billing_id = " + cpBillingId;
		String sql3 = "DELETE FROM daily_log.tbl_cp_billing_trone_order_detail where cp_billing_id = " + cpBillingId;
		JdbcControl control = new JdbcControl();
		control.execute(sql1);
		control.execute(sql2);
		control.execute(sql3);
	}
	
	/**
	 * 更新帐单状态
	 * @param cpBillingId
	 * @param status
	 */
	public void updateCpBillingStatus(int cpBillingId,int status)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` SET STATUS = " + status + " WHERE id = " + cpBillingId;
		new JdbcControl().execute(sql);
	}
	
	/**
	 * 调整CP对应的业务数据后，重新进行计算信息费、核减费用、应结费用等
	 * @param cpBillingId
	 */
	public void updateCpBilling(int cpBillingId)
	{
		String sql = "SELECT  SUM(amount) amount, ";
		sql += " SUM(amount*rate) pre_billing, ";
		sql += " SUM(CASE reduce_type WHEN 0 THEN reduce_amount*rate  WHEN 1 THEN reduce_amount END) reduce_amount ";
		sql += " FROM daily_log.`tbl_cp_billing_sp_trone` WHERE cp_billing_id = " + cpBillingId + " AND STATUS = 0; ";

		
		JdbcControl control = new JdbcControl();
		float[] result = (float[])control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					float[] result = {rs.getFloat("amount"),rs.getFloat("reduce_amount"),rs.getFloat("pre_billing")};
					return result;
				}
				return null;
			}
		});
		String sqlUpdate = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` SET amount = ?,reduce_amount = ?,pre_billing = ?  WHERE id = ? ";
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, result[0]);
		params.put(2, result[1]);
		params.put(3, result[2]);
		params.put(4, cpBillingId);
		control.execute(sqlUpdate, params);
	}
	
	
	public void updateCpBillingActurePay(int cpBillingId,float money)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` SET acture_billing = " + money + ",pay_time = NOW(),status = 3 WHERE id = " + cpBillingId;
		new JdbcControl().execute(sql);
	}
	//完成对账是更新对账时间
	public void updateCpBillingActurePay(int cpBillingId,float money,String date)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` SET acture_billing = " + money + ",pay_time ='"+date+"',status = 6 WHERE id = " + cpBillingId;
		new JdbcControl().execute(sql);
	}
	
	/**
	 * 增加CP帐单下面业务汇总数据
	 * @param list
	 * @param cpBillingId
	 */
	public void addCpBillingSpTroneData(List<CpBillingSpTroneModel> list,int cpBillingId)
	{
		String sql = "INSERT INTO daily_log.`tbl_cp_billing_sp_trone` (cp_billing_id,start_date,end_date,sp_trone_id,amount,rate) values ";
		
		for(CpBillingSpTroneModel model : list)
		{
			sql += "(" + cpBillingId + ",'" + model.getStartDate() + "','" + model.getEndDate() + "'," + model.getSpTroneId() + "," + model.getAmount() + "," + model.getRate() + "),";
		}
		
		sql = sql.substring(0,sql.length()-1);
		
		new JdbcControl().execute(sql);
		
	}
	
	/**
	 * 增加CP帐单下面业务的每个通道详细数据
	 * @param list
	 * @param cpBillingId
	 */
	public void addCpBillingTroneOrderDetailData(List<CpBillingTroneOrderDetailModel> list,int cpBillingId)
	{
		String sql = "INSERT INTO daily_log.`tbl_cp_billing_trone_order_detail`(cp_billing_id,province_id,trone_order_id,mr_date,amount) values ";
		
		for(CpBillingTroneOrderDetailModel model : list)
		{
			sql += "(" + cpBillingId + "," + model.getProvinceId() +  "," + model.getTroneOrderId() + ",'" + model.getMrDate() + "'," + model.getAmount() + "),";
		}
		
		sql = sql.substring(0,sql.length()-1);
		
		new JdbcControl().execute(sql);
	}
	public void updateCpBillingModel(int id,int type,int status,String date,float kaipiaoMoney){
		String replaceStr="";
		if(type==1){
			replaceStr="start_bill_date='"+date+"'";
		}
		if(type==2){
			replaceStr="get_bill_date='"+date+"',kaipiao_billing="+kaipiaoMoney;
		}
		if(type==3){
			replaceStr="apply_pay_bill_date='"+date+"'";
		}
		String sql="UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp_billing` SET "+replaceStr+",status ="+status+"  WHERE id ="+id;
		new JdbcControl().execute(sql);

	}
	
	/**
	 * 导出的CP账单数据
	 * @param startDate
	 * @param endDate
	 * @param spId
	 * @param jsTypes
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CpBillExportModel> exportExcelData(String startDate,String endDate,int cpId,String jsTypes,String status)
	{	
		String startDateStr="";
		String endDateStr="";
		if(!StringUtil.isNullOrEmpty(startDate)){
			 startDateStr="AND start_date >= '"+startDate+"'";
		}
		if(!StringUtil.isNullOrEmpty(endDate)){
			endDateStr=" AND end_date<= '"+endDate+"'";
		}
		String sql="SELECT b.id AS bill_id ,a.id detail_id,DATE_FORMAT(b.start_date,'%Y%m') AS bill_month,i.`name`,"+
					" b.`start_date`,b.`end_date`,e.`nick_name`,c.`full_name`,CONCAT(h.name_cn,'-',g.name) product_name,"+
					" d.`name` sp_trone_name,a.`amount`,a.rate,a.amount*a.rate sp_trone_billing_amount,a.reduce_amount,"+
					" a.reduce_type,CASE WHEN a.reduce_type = 0 THEN (a.amount - a.reduce_amount)*rate ELSE a.amount*rate - a.reduce_amount END sp_trone_billing_acture_amount,"+
					" b.pre_billing,b.start_bill_date,b.kaipiao_billing kaipiao_amount,b.get_bill_date,b.apply_pay_bill_date,b.pay_time,"+
					" b.acture_billing,b.status "+
					" FROM (SELECT * FROM daily_log.`tbl_cp_billing_sp_trone` WHERE 1=1 "+startDateStr+endDateStr+") a"+
					" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp_billing b ON a.`cp_billing_id` = b.`id`"+
					" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_cp` c ON b.cp_id = c.id"+
					" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON a.`sp_trone_id` = d.id"+
					" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` e ON c.`commerce_user_id` = e.`id`"+
					" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` f ON d.`product_id` = f.`id`"+
					" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` g ON f.`product_1_id` = g.`id`"+
					" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` h ON g.`operator_id` = h.`id`"+
					" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_js_type` i ON b.`js_type` = i.`type_id` WHERE 1=1";

		if(!StringUtil.isNullOrEmpty(status)){
			sql+=" AND b.status IN ("+status+")";
		}
		if(!StringUtil.isNullOrEmpty(jsTypes)){
			sql+=" AND i.type_id IN ("+jsTypes+")";
		}
		if(cpId>=0){
			sql+=" AND c.id="+cpId;
			
		}
		sql+=" ORDER BY bill_month,i.`name`,start_date,e.`nick_name`,c.full_name,product_name,sp_trone_name";
		return (List<CpBillExportModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<CpBillExportModel> list = new ArrayList<CpBillExportModel>();
				CpBillExportModel model = null;
		        DecimalFormat df2 = new DecimalFormat("0.000");  
				while(rs.next())
				{
		
					model = new CpBillExportModel();
					model.setBillId(rs.getInt("bill_id"));
					model.setDetailId(rs.getInt("detail_id"));
					model.setBillMonth(StringUtil.getString(rs.getString("bill_month"), ""));
					model.setJsName(StringUtil.getString(rs.getString("name"), ""));
					model.setStartDate(StringUtil.getString(rs.getString("start_date"), ""));
					model.setEndDate(StringUtil.getString(rs.getString("end_date"), ""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"), ""));
					model.setCpFullNam(StringUtil.getString(rs.getString("full_name"), ""));
					model.setProductName(StringUtil.getString(rs.getString("product_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setAmount(rs.getFloat("amount"));
					model.setRate(Float.parseFloat(df2.format(rs.getFloat("rate"))));
					model.setSpTroneBillAmount(rs.getFloat("sp_trone_billing_amount"));
					model.setReduceAmount(rs.getFloat("reduce_amount"));
					model.setReduceType(rs.getInt("reduce_type"));
					model.setActureAmount(rs.getFloat("sp_trone_billing_acture_amount"));
					model.setPreBilling(rs.getFloat("pre_billing"));
					model.setBillingDate(StringUtil.getString(rs.getString("start_bill_date"), ""));
					model.setKaipiaoAmount(rs.getFloat("kaipiao_amount"));
					model.setGetbillDate(StringUtil.getString(rs.getString("get_bill_date"), ""));
					model.setApplyPayBillDate(StringUtil.getString(rs.getString("apply_pay_bill_date"), ""));
					model.setPayTime(StringUtil.getString(rs.getString("pay_time"), ""));
					model.setActureBilling(rs.getFloat("acture_billing"));

					model.setStatus(rs.getInt("status"));
					model.setStatusName(getStatusNameByStatu(rs.getInt("status")));
					list.add(model);
				}
				
				return list;
			}
		});
	}
	public String getStatusNameByStatu(int status){
		String msg="";
		switch (status) {
		case 0:
			msg="发起"; break;
		case 1: msg="运营审核"; break;
		case 2: msg="CP审核"; break;
		case 3: msg="发起帐单"; break;
		case 4: msg="收到票据"; break;
		case 5: msg="申请付款"; break;
		case 6: msg="已付款" ; break;
		default:
			break;
		}
		return msg;
	}
}
