package com.system.server;

import java.util.Calendar;
import java.util.List;

import com.system.constant.Constant;
import com.system.dao.DailyDataDao;
import com.system.model.PlatFormDataModel;
import com.system.util.ConfigManager;
import com.system.util.StringUtil;

public class DailyDataServer
{
	public void analyDataToSummer(String feeDate,String monthFeeDate)
	{
		DailyDataDao dao = new DailyDataDao();
		
		String[] operatros = {"未知业务线","移动","联通","电信"};
		
		String dfCoName = ConfigManager.getConfigData("DF_CO_NAME", "浩天");
		
		StringBuffer sbHead  = new StringBuffer(512);
		sbHead.append("INSERT INTO daily_log.`tbl_all_summer`(T_telecom_type,T_owner_name,T_cp,T_service_type,");
		sbHead.append("T_product_name,T_contract_name,T_fee_name,T_fee,T_fee_type,T_succ_count,T_fail_count,");
		sbHead.append("T_user_count,T_fail_usercount,T_record_date,T_province,IS_flag) VALUES ");
		
		StringBuffer sbData = new StringBuffer(2048);
		
		PlatFormDataModel model = null;
		
		List<PlatFormDataModel> list = null;
		
		dao.delDailyDataByFeeDate(feeDate,1);
		
		int[] recordTypes = {Constant.MR_SUMMER_QUERY_NORMAL,Constant.MR_SUMMER_QUERY_IVR,Constant.MR_SUMMER_QUERY_OTHER_DAY};
		
		String analyFeeDate = feeDate;
		
		for(int recordType : recordTypes)
		{
			if(recordType==Constant.MR_SUMMER_QUERY_OTHER_DAY)
			{
				analyFeeDate = monthFeeDate;
				dao.delDailyDataByFeeDate(analyFeeDate, 2);
			}
			
			list = dao.loadPlatFormDataByType(analyFeeDate,recordType);
			
			if(list!=null && !list.isEmpty())
			{
				for(int i=0; i<list.size(); i++)
				{
					model = list.get(i);
					
					if(model.getProvinceId()==32)
						model.setT_province("全国");
					
					if(recordType==Constant.MR_SUMMER_QUERY_OTHER_DAY)
					{
						model.setT_fee_type(2);
					}
					
					model.setT_contract_name( dfCoName + "_" +  model.getT_owner_name()+ "_" + operatros[model.getT_telecom_type()] + "_" + model.getT_product_name());
					
					int milsDataCount = model.getT_succ_count() - model.getCp_T_succ_Count();
					int milsUserCount = model.getT_user_count() - model.getCP_T_user_count();
					
					model.setT_succ_count(model.getCp_T_succ_Count());
					model.setT_user_count(model.getCP_T_user_count());
					
					sbData.append("(" + model.getT_telecom_type() + ",'" + model.getT_owner_name() + "','" + model.getT_cp() + "','" + model.getT_service_type() + "','" + model.getT_product_name() + "','" + model.getT_contract_name() + "','" + model.getT_fee_name() + "'," + model.getT_fee() + "," + model.getT_fee_type() + "," + model.getT_succ_count() + "," + model.getT_fail_count() + "," + model.getCP_T_user_count() + "," + model.getT_fail_usercount() + ",'" + model.getT_record_date() + "','" + model.getT_province() + "',1),");
					
					if(milsDataCount>0)
					{
						model.setT_cp("于丽红");
						model.setT_succ_count(milsDataCount);
						model.setT_user_count(milsUserCount);
						sbData.append("(" + model.getT_telecom_type() + ",'" + model.getT_owner_name() + "','" + model.getT_cp() + "','" + model.getT_service_type() + "','" + model.getT_product_name() + "','" + model.getT_contract_name() + "','" + model.getT_fee_name() + "'," + model.getT_fee() + "," + model.getT_fee_type() + "," + model.getT_succ_count() + "," + model.getT_fail_count() + "," + model.getT_user_count() + "," + model.getT_fail_usercount() + ",'" + model.getT_record_date() + "','" + model.getT_province() + "',1),");
					}
					
					if((i+1)%500==0)
					{
						if(sbData.length()>0)
						{
							sbData.deleteCharAt(sbData.length()-1);
							sbData.append(";");
							dao.addDataToAllSummer(sbHead.toString() + sbData.toString());
							sbData = new StringBuffer(2048);
						}
					}
				}
				
				if(sbData.length()>0)
				{
					sbData.deleteCharAt(sbData.length()-1);
					sbData.append(";");
					dao.addDataToAllSummer(sbHead.toString() + sbData.toString());
					sbData = new StringBuffer(2048);
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		DailyDataServer server = new DailyDataServer();
		
		Calendar ca = Calendar.getInstance();
		
		ca.add(Calendar.DAY_OF_MONTH, -4);
		
		String feeDate = StringUtil.getDateFormat(ca.getTime());
		
		//ca.add(Calendar.DAY_OF_MONTH, -3);
		
		String monthFeeDate = StringUtil.getDateFormat(ca.getTime());
		
		server.analyDataToSummer(feeDate,monthFeeDate);
	}
}
