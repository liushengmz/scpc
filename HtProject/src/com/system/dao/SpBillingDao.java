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
import com.system.model.SettleAccountModel;
import com.system.model.SpBillExportModel;
import com.system.model.SpBillingModel;
import com.system.model.SpBillingSpTroneModel;
import com.system.util.SqlUtil;
import com.system.util.StringUtil;

public class SpBillingDao
{
	public boolean isSpBillingCross(int spId,int jsType,String startDate,String endDate)
	{
		String sql = "SELECT COUNT(*) FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` WHERE sp_id = " + spId + " AND js_type = " + jsType;
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
	
	@SuppressWarnings("unchecked")
	public List<SpBillingSpTroneModel> loadSpBillingSpTroneOri(final int spId,final int jsType,final String startDate,final String endDate)
	{
		String sql = " SELECT d.`id` sp_trone_id,SUM(a.amount) amount,d.`jiesuanlv` rate ";
		sql += " FROM daily_log.tbl_mr_summer a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone_order` b ON a.`trone_order_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_trone` c ON b.`trone_id` = c.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` d ON c.`sp_trone_id` = d.`id` ";
		sql += " WHERE 1=1 ";
		sql += " AND a.`mr_date` >= '" + startDate + "' AND a.`mr_date` <= '" + endDate + "' ";
		sql += " AND d.`js_type` = " + jsType;
		sql += " AND d.`sp_id` = " + spId;
		sql += " GROUP BY d.`id`; ";
		
		return (List<SpBillingSpTroneModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpBillingSpTroneModel> list = new ArrayList<SpBillingSpTroneModel>();
				
				while(rs.next())
				{
					SpBillingSpTroneModel model = new SpBillingSpTroneModel();
					
					model.setRate(rs.getFloat("rate"));
					model.setSpTroneId(rs.getInt("sp_trone_id"));
					model.setAmount(rs.getFloat("amount"));
					model.setStartDate(startDate);
					model.setEndDate(endDate);
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	
	public int addSpBilling(int spId,int jsType,String startDate,String endDate,float preBilling,float amount)
	{
		String sql = "INSERT INTO " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing`(sp_id,js_type,start_date,end_date,pre_billing,amount) VALUES(?,?,?,?,?,?)";
		
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		
		map.put(1, spId);
		map.put(2, jsType);
		map.put(3, startDate);
		map.put(4, endDate);
		map.put(5, preBilling);
		map.put(6, amount);
		
		return new JdbcControl().insertWithGenKey(sql, map);
	}
	
	
	public void addSpBillingSpTroneData(List<SpBillingSpTroneModel> list,int spBillingId)
	{
		String sql = "INSERT INTO daily_log.`tbl_sp_billing_sp_trone` (sp_billing_id,start_date,end_date,sp_trone_id,amount,rate) values ";
		
		for(SpBillingSpTroneModel model : list)
		{
			sql += "(" + spBillingId + ",'" + model.getStartDate() + "','" + model.getEndDate() + "'," + model.getSpTroneId() + "," + model.getAmount() + "," + model.getRate() + "),";
		}
		
		sql = sql.substring(0,sql.length()-1);
		
		new JdbcControl().execute(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<SettleAccountModel> exportExcelData(int spBillingId)
	{
		String sql = "SELECT b.`name`,CONCAT(e.`name_cn`,'-',d.name) name_cn,a.amount,a.reduce_amount,a.rate,a.reduce_type ";
		
		sql += " FROM daily_log.`tbl_sp_billing_sp_trone` a ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` b ON a.`sp_trone_id` = b.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` c ON b.`product_id` = c.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` d ON c.`product_1_id` = d.`id` ";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` e ON d.`operator_id` = e.`id` ";
		
		sql += " WHERE 1=1 AND a.status = 0 AND a.sp_billing_id =" + spBillingId + " order by name_cn,name";
		
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
	
	/**
	 * 获取指定的帐单号
	 * @param id
	 * @return
	 */
	public SpBillingModel getSpBillingModel(int id)
	{
		String sql = "SELECT a.*,b.`short_name` sp_name,c.`name` js_name";
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_js_type` c ON a.`js_type` = c.`type_id`";
		sql += " WHERE 1=1 and a.id = " + id;
		
		return (SpBillingModel)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					SpBillingModel model = new SpBillingModel();
					
					model.setId(rs.getInt("id"));
					model.setSpId(rs.getInt("sp_id"));
					model.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
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
					model.setReduceAmount(rs.getFloat("reduce_amount"));
					
					//新增三个对账时间,完成对账时间
					model.setBillingDate(StringUtil.getString(rs.getString("billing_date"),""));
					model.setApplyKaipiaoDate(StringUtil.getString(rs.getString("apply_kaipiao_date"), ""));
					model.setKaipiaoDate(StringUtil.getString(rs.getString("kaipiao_date"), ""));
					model.setPayTime(StringUtil.getString(rs.getString("pay_time"), ""));
					
					return model;
				}
				return null;
			}
		});
	}
	
	public Map<String, Object> loadSpBilling(String startDate, String endDate,
			int spId,int jsType,int status,int pageIndex)
	{
		startDate = SqlUtil.sqlEncode(startDate);
		endDate = SqlUtil.sqlEncode(endDate);		
		
		String sql = "SELECT " + Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_js_type` c ON a.`js_type` = c.`type_id`";
		sql += " WHERE 1=1";
		
		if(spId>0)
		{
			sql += " and b.id = " + spId;
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
				sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.`short_name` sp_name,b.`short_name`,c.`name` js_name") + limit,
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<SpBillingModel> list = new ArrayList<SpBillingModel>();
						while (rs.next())
						{
							SpBillingModel model = new SpBillingModel();

							model.setId(rs.getInt("id"));
							model.setSpId(rs.getInt("sp_id"));
							model.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
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
							model.setReduceAmount(rs.getFloat("reduce_amount"));
							//新增三个对账时间
							model.setBillingDate(StringUtil.getString(rs.getString("billing_date"),""));
							model.setApplyKaipiaoDate(StringUtil.getString(rs.getString("apply_kaipiao_date"), ""));
							model.setKaipiaoDate(StringUtil.getString(rs.getString("kaipiao_date"), ""));
							model.setSpShortName(StringUtil.getString(rs.getString("short_name"), ""));
							
							list.add(model);
						}
						return list;
					}
				}));

		return map;
	}
	
	/**
	 * 删除指定对帐单
	 * @param spBillingId
	 */
	public void delSpBilling(int spBillingId)
	{
		String sql1 = "DELETE FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` WHERE id = " + spBillingId;
		String sql2 = "DELETE FROM daily_log.tbl_sp_billing_sp_trone where sp_billing_id = " + spBillingId;
		JdbcControl control = new JdbcControl();
		control.execute(sql1);
		control.execute(sql2);
	}
	
	/**
	 * 更新帐单状态
	 * @param spBillingId
	 * @param status
	 */
	public void updateSpBillingStatus(int spBillingId,int status)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` SET STATUS = " + status + " WHERE id = " + spBillingId;
		new JdbcControl().execute(sql);
	}
	
	public boolean recallSpBilling(int spBillingId)
	{
		String sql = "select status from " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_billing where id = " + spBillingId;
		
		JdbcControl control = new JdbcControl();
		
		int status = (Integer)control.query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				if(rs.next())
				{
					return rs.getInt("status");
				}
				
				return -1;
			}
		});
		
		if(status==1)
		{
			control.execute("UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` SET STATUS = 0 WHERE id = " + spBillingId);
			return true;
		}
		
		return false;
	}
	
	public void updateSpBillingActurePay(int spBillingId,float money,String payTime)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` SET acture_billing = " + money + ",pay_time = '"+payTime+"',status = 2 WHERE id = " + spBillingId;
		new JdbcControl().execute(sql);
	}
	public void updateSpBillingActurePay(int spBillingId,float money)
	{
		String sql = "UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` SET acture_billing = " + money + ",pay_time =now(),status = 2 WHERE id = " + spBillingId;
		new JdbcControl().execute(sql);
	}
	/**
	 * SP商务只能查看自己以及被授权的的运营账单
	 * @param startDate
	 * @param endDate
	 * @param spId
	 * @param jsType
	 * @param status
	 * @param pageIndex
	 * @return
	 */
	public Map<String, Object> loadSpBilling(String startDate, String endDate,
			int spId,int jsType,int userId,int rightType,int status,int pageIndex)
	{
		startDate = SqlUtil.sqlEncode(startDate);
		endDate = SqlUtil.sqlEncode(endDate);		
		
		String sql = "SELECT " + Constant.CONSTANT_REPLACE_STRING;
		sql += " FROM " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` a";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` b ON a.`sp_id` = b.`id`";
		sql += " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_js_type` c ON a.`js_type` = c.`type_id`";
		sql += " WHERE 1=1";
		if(rightType>0){
			sql += " AND (b.commerce_user_id="+userId+" or b.commerce_user_id in ("+getRigthListByUser(userId)+"))";
		}
		if(spId>0)
		{
			sql += " and b.id = " + spId;
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
				sql.replace(Constant.CONSTANT_REPLACE_STRING, " a.*,b.`short_name` sp_name,b.`short_name` short_name,c.`name` js_name") + limit,
				new QueryCallBack()
				{
					@Override
					public Object onCallBack(ResultSet rs) throws SQLException
					{
						List<SpBillingModel> list = new ArrayList<SpBillingModel>();
						while (rs.next())
						{
							SpBillingModel model = new SpBillingModel();

							model.setId(rs.getInt("id"));
							model.setSpId(rs.getInt("sp_id"));
							model.setSpName(StringUtil.getString(rs.getString("sp_name"), ""));
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
							model.setReduceAmount(rs.getFloat("reduce_amount"));
							
							list.add(model);
						}
						return list;
					}
				}));

		return map;
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
	public void updateSpBillingModel(int id,int type,int status,String date){
		String replaceStr="";
		if(type==1){
			replaceStr="billing_date='"+date+"'";
		}
		if(type==2){
			replaceStr="apply_kaipiao_date='"+date+"'";
		}
		if(type==3){
			replaceStr="kaipiao_date='"+date+"'";
		}
		String sql="UPDATE " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_billing` SET "+replaceStr+",status ="+status+"  WHERE id ="+id;
		new JdbcControl().execute(sql);

	}
	
	@SuppressWarnings("unchecked")
	public List<SpBillExportModel> exportExcelData(String startDate,String endDate,int spId,String jsTypes,String status)
	{	
		String startDateStr="";
		String endDateStr="";
		if(!StringUtil.isNullOrEmpty(startDate)){
			 startDateStr="AND start_date >= '"+startDate+"'";
		}
		if(!StringUtil.isNullOrEmpty(endDate)){
			endDateStr=" AND end_date<= '"+endDate+"'";
		}
		String sql="SELECT b.id AS bill_id,a.id AS detail_id,DATE_FORMAT(b.start_date,'%Y%m') AS bill_month, i.`name`,"+
				   " b.`start_date`,b.`end_date`,e.`nick_name`,d.`full_name`,CONCAT(h.name_cn,'-',g.name) product_name,"+
				   " c.`name` sp_trone_name,a.`amount`,a.rate,a.amount*a.rate sp_trone_billing_amount,a.reduce_amount, "+
				   " a.reduce_type,((a.amount - a.reduce_data_amount)*rate - a.reduce_money_amount) sp_trone_billing_acture_amount,a.reduce_data_amount,reduce_money_amount,"+
				   " b.pre_billing,b.billing_date,b.pre_billing kaipiao_amount,b.apply_kaipiao_date,b.kaipiao_date,b.pay_time,b.acture_billing,b.status"+ 
				   " FROM (SELECT * FROM daily_log.`tbl_sp_billing_sp_trone` WHERE 1=1 "+startDateStr+endDateStr+" ) a "+
				   " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_billing b ON a.`sp_billing_id` = b.`id`"+
				   " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp_trone` c ON a.`sp_trone_id` = c.id"+
				   " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_sp` d ON b.`sp_id` = d.`id`"+
				   " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_user` e ON d.`commerce_user_id` = e.`id`"+
				   " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` f ON c.`product_id` = f.`id`"+
				   " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` g ON f.`product_1_id` = g.`id`"+
				   " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` h ON g.`operator_id` = h.`id`"+
				   " LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_js_type` i ON b.`js_type` = i.`type_id` WHERE 1=1";
		if(!StringUtil.isNullOrEmpty(status)){
			sql+=" AND b.status IN ("+status+")";
		}
		if(!StringUtil.isNullOrEmpty(jsTypes)){
			sql+=" AND i.type_id IN ("+jsTypes+")";
		}
		if(spId>=0){
			sql+=" AND d.id="+spId;
			
		}
		sql+=" ORDER BY bill_month ASC,start_date ASC,full_name ASC";
		return (List<SpBillExportModel>)new JdbcControl().query(sql, new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<SpBillExportModel> list = new ArrayList<SpBillExportModel>();
				SpBillExportModel model = null;
		        DecimalFormat df2 = new DecimalFormat("0.000");  
				while(rs.next())
				{
		
					model = new SpBillExportModel();
					model.setBillId(rs.getInt("bill_id"));
					model.setDetailId(rs.getInt("detail_id"));
					model.setBillMonth(StringUtil.getString(rs.getString("bill_month"), ""));
					model.setJsName(StringUtil.getString(rs.getString("name"), ""));
					model.setStartDate(StringUtil.getString(rs.getString("start_date"), ""));
					model.setEndDate(StringUtil.getString(rs.getString("end_date"), ""));
					model.setNickName(StringUtil.getString(rs.getString("nick_name"), ""));
					model.setSpFullNam(StringUtil.getString(rs.getString("full_name"), ""));
					model.setProductName(StringUtil.getString(rs.getString("product_name"), ""));
					model.setSpTroneName(StringUtil.getString(rs.getString("sp_trone_name"), ""));
					model.setAmount(StringUtil.getDecimalFormat(rs.getFloat("amount")));
					model.setRate(df2.format(rs.getFloat("rate"))+"");
					model.setSpTroneBillAmount(StringUtil.getDecimalFormat(rs.getFloat("sp_trone_billing_amount")));
					model.setReduceAmount(StringUtil.getDecimalFormat(rs.getFloat("reduce_amount")));
					model.setReduceType(rs.getInt("reduce_type"));
					model.setActureAmount(StringUtil.getDecimalFormat(rs.getFloat("sp_trone_billing_acture_amount")));
					model.setPreBilling(StringUtil.getDecimalFormat(rs.getFloat("pre_billing")));
					model.setBillingDate(StringUtil.getString(rs.getString("billing_date"), ""));
					model.setKaipiaoAmount(StringUtil.getDecimalFormat(rs.getFloat("kaipiao_amount")));
					model.setApplyKaipiaoDate(StringUtil.getString(rs.getString("apply_kaipiao_date"), ""));
					model.setKaipiaoDate(StringUtil.getString(rs.getString("kaipiao_date"), ""));
					model.setPayTime(StringUtil.getString(rs.getString("pay_time"), ""));
					model.setActureBilling(StringUtil.getDecimalFormat(rs.getFloat("acture_billing")));
					
					//Add By Andy.Chen 2016.10.27
					model.setReduceDataAmount(StringUtil.getDecimalFormat(rs.getFloat("reduce_data_amount")));
					model.setReduceMoneyAmount(StringUtil.getDecimalFormat(rs.getFloat("reduce_money_amount")));
					
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
			msg="刚发起"; break;
		case 1: msg="运营审核通过"; break;
		case 2: msg="已到帐"; break;
		case 3: msg="上游已开票"; break;
		case 4: msg="结算申请开票完成"; break;
		case 5: msg="财务已完成开票"; break;
		default:
			break;
		}
		return msg;
	}
}
