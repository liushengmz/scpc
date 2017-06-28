package com.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.constant.Constant;
import com.system.database.JdbcControl;
import com.system.database.QueryCallBack;
import com.system.model.PlatFormDataModel;
import com.system.util.StringUtil;

/**
 * 这个是给北京大平台数据分析的DAO
 * @author Andy.Chen
 *
 */
public class DailyDataDao
{
	
	//处理Mr recordType 是 0和1 的 0是实时数据，1是隔天数据，2是IVR数据
	private StringBuffer loadSqlByType(String feeDate,int recordType)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT a.*,b.CP_T_succ_count,0 T_user_count,0 CP_T_user_count");
		sb.append(" FROM");
		sb.append(" (");
		sb.append(" SELECT e.`full_name` T_owner_name,g.`full_name` T_cp,");
		sb.append(" d.`name` T_product_name,c.`trone_name`  T_fee_name,CONCAT(k.`name_cn`,j.`name`,'-',i.`name`) T_service_type,");
		sb.append(" c.`price`*100 T_fee,h.`name` T_province,");
		sb.append(" k.`bj_flag` T_telecom_type,SUM(data_rows) T_succ_count,mr_date T_record_date,");
		sb.append(" a.`trone_order_id`,h.id province_id");
		sb.append(" FROM daily_log.tbl_mr_summer a");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.trone_order_id = b.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON a.`trone_id` = c.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone d ON c.`sp_trone_id` = d.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.`sp_id` = e.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp g ON b.`cp_id` = g.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_province h ON a.province_id = h.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` i ON d.`product_id` = i.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` j ON i.`product_1_id` = j.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` k ON j.`operator_id` = k.id ");
		sb.append(" WHERE a.mr_date = '" + feeDate + "'");
		sb.append(" AND a.record_type = " + recordType);		
		sb.append(" GROUP BY b.id,d.id,e.id,h.id");
		sb.append(" ORDER BY b.id,d.id,e.id,h.id");
		sb.append(" ) a");
		sb.append(" LEFT JOIN");
		sb.append(" (");
		sb.append(" SELECT SUM(data_rows) CP_T_succ_count,mr_date,a.`trone_order_id`,h.`id` province_id");
		sb.append(" FROM daily_log.tbl_cp_mr_summer a");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.trone_order_id = b.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.`trone_id` = c.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone d ON c.`sp_trone_id` = d.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.`sp_id` = e.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp g ON b.`cp_id` = g.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_province h ON a.province_id = h.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` i ON d.`product_id` = i.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` j ON i.`product_1_id` = j.`id`");
		sb.append(" WHERE a.mr_date = '" + feeDate + "'");
		sb.append(" AND a.record_type = " + recordType);		
		sb.append(" GROUP BY b.id,d.id,e.id,h.id");
		sb.append(" ORDER BY b.id,d.id,e.id,h.id");
		sb.append(" )b");
		sb.append(" ON a.trone_order_id = b.trone_order_id AND a.province_id = b.province_id");
		return sb;
	}
	
	//处理Mr RecordType 是2的数据, 0是实时数据，1是隔天数据，2是IVR数据
	private StringBuffer loadIVRSqlByType(String feeDate)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT a.*,b.CP_T_user_count,b.CP_T_succ_count ");
		sb.append(" FROM");
		sb.append(" (");
		sb.append(" SELECT e.`full_name` T_owner_name,g.`full_name` T_cp,");
		sb.append(" d.`name` T_product_name,c.`trone_name`  T_fee_name,CONCAT(k.`name_cn`,j.`name`,'-',i.`name`) T_service_type,");
		sb.append(" c.`price`*100 T_fee,h.`name` T_province,");
		sb.append(" k.`bj_flag` T_telecom_type,SUM(data_rows) T_user_count, SUM(a.amount/c.price) T_succ_count,mr_date T_record_date,");
		sb.append(" a.`trone_order_id`,h.id province_id");
		sb.append(" FROM daily_log.tbl_mr_summer a");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.trone_order_id = b.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON a.`trone_id` = c.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone d ON c.`sp_trone_id` = d.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.`sp_id` = e.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp g ON b.`cp_id` = g.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_province h ON a.province_id = h.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` i ON d.`product_id` = i.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` j ON i.`product_1_id` = j.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_operator` k ON j.`operator_id` = k.id ");
		sb.append(" WHERE a.mr_date = '" + feeDate + "'");
		sb.append(" AND a.record_type =2");
		sb.append(" GROUP BY b.id,d.id,e.id,h.id");
		sb.append(" ORDER BY b.id,d.id,e.id,h.id");
		sb.append(" ) a");
		sb.append(" LEFT JOIN");
		sb.append(" (");
		sb.append(" SELECT SUM(data_rows) CP_T_user_count,SUM(a.amount/c.price) CP_T_succ_count,mr_date,a.`trone_order_id`,h.`id` province_id");
		sb.append(" FROM daily_log.tbl_cp_mr_summer a");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone_order b ON a.trone_order_id = b.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_trone c ON b.`trone_id` = c.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp_trone d ON c.`sp_trone_id` = d.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_sp e ON d.`sp_id` = e.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_cp g ON b.`cp_id` = g.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".tbl_province h ON a.province_id = h.id");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_2` i ON d.`product_id` = i.`id`");
		sb.append(" LEFT JOIN " + com.system.constant.Constant.DB_DAILY_CONFIG + ".`tbl_product_1` j ON i.`product_1_id` = j.`id`");
		sb.append(" WHERE a.mr_date = '" + feeDate + "'");
		sb.append(" AND a.record_type = 2 ");
		sb.append(" GROUP BY b.id,d.id,e.id,h.id");
		sb.append(" ORDER BY b.id,d.id,e.id,h.id");
		sb.append(" )b");
		sb.append(" ON a.trone_order_id = b.trone_order_id AND a.province_id = b.province_id");
		
		return sb;
	}
	
	/**
	 * 
	 * @param feeDate 计费日期
	 * @param delType 1删除点播和IVR的数据，2删除包月数据（也就是隔天数据）
	 */
	public void delDailyDataByFeeDate(String feeDate,int delType)
	{
		String sql = "DELETE FROM `daily_log`.`tbl_all_summer` WHERE T_record_date = '" + feeDate + "' AND T_fee_type = " + delType;
		new JdbcControl().execute(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<PlatFormDataModel> loadPlatFormDataByType(final String feeDate,int recordType)
	{
		String sql = "";
		
		if(recordType==Constant.MR_SUMMER_QUERY_NORMAL 
				|| recordType == Constant.MR_SUMMER_QUERY_OTHER_DAY)
		{
			sql = loadSqlByType(feeDate,recordType).toString();
		}
		else if(recordType==Constant.MR_SUMMER_QUERY_IVR)
		{
			sql = loadIVRSqlByType(feeDate).toString();
		}
				
		return (List<PlatFormDataModel>)new JdbcControl().query(sql,new QueryCallBack()
		{
			@Override
			public Object onCallBack(ResultSet rs) throws SQLException
			{
				List<PlatFormDataModel> list = new ArrayList<PlatFormDataModel>();
				
				while(rs.next())
				{
					PlatFormDataModel model = new PlatFormDataModel();
					
					model.setT_telecom_type(rs.getInt("T_telecom_type"));
					model.setT_owner_name(StringUtil.getString(rs.getString("T_owner_name"), ""));
					model.setT_cp(StringUtil.getString(rs.getString("T_cp"), ""));
					model.setT_service_type(StringUtil.getString(rs.getString("T_service_type"), ""));
					model.setT_product_name(StringUtil.getString(rs.getString("T_product_name"), ""));
					model.setT_fee_name(StringUtil.getString(rs.getString("T_fee_name"), ""));
					model.setT_fee(rs.getInt("T_fee"));
					model.setT_succ_count(rs.getInt("T_succ_count"));
					model.setCp_T_succ_Count(rs.getInt("CP_T_succ_count"));
					model.setT_record_date(feeDate);
					model.setProvinceId(rs.getInt("province_id"));
					model.setT_province(StringUtil.getString(rs.getString("T_province"), ""));
					model.setT_user_count(rs.getInt("T_user_count"));
					model.setCP_T_user_count(rs.getInt("CP_T_user_count"));
					model.setT_fee_type(1);
					
					list.add(model);
				}
				
				return list;
			}
		});
	}
	
	public void addDataToAllSummer(String data)
	{
		new JdbcControl().execute(data.toString());
	}
	
	public static void main(String[] args)
	{
		
	}
}
