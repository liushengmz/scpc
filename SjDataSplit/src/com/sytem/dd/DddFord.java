package com.sytem.dd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.system.cache.CacheConfigMgr;
import com.system.cache.CpDataCache;
import com.system.cache.LocateCache;
import com.system.cache.SpDataCache;
import com.system.dao.SummerDataDao;
import com.system.database.JdbcControl;
import com.system.model.PhoneLocateModel;
import com.system.model.SpTroneModel;
import com.system.model.SummerDataModel;
import com.system.model.TroneModel;
import com.system.model.TroneOrderModel;
import com.system.util.FileUtil;
import com.system.util.StringUtil;

public class DddFord
{
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	private static String troneOrderIds = "678,679,680,681,2151,2152"; 
	public static String dataBase = "yd_bak";
	private static int year = 2016;
	private static int month = 7;
	private static String baseCpSql = "INSERT INTO tbl_cp_mr_%s( mobile, mcc, province_id, city_id, sp_trone_id, trone_id, trone_order_id, ori_trone, ori_order, linkid, ip, syn_flag, mr_date, create_date, IsMatch, sp_api_url_id, sp_id, cp_id, trone_type) VALUES";
	private static String baseSpSql = "insert into yd_bak.temp_tbl_mr_%s(imei,imsi,mobile,user_md10,mcc,province_id,city_id,sp_trone_id,trone_id,trone_order_id,ori_trone,ori_order,linkid,cp_param,service_code,price,ip,STATUS,syn_flag,mo_table,mo_id,mr_date,create_date,IsMatch,sp_api_url_id,sp_id,cp_id,ivr_time,trone_type,api_order_id) VALUES ";
	
	private static int finalDataRows = 0;
	private static JdbcControl control = null;
	
	public static void main(String[] args) throws ParseException
	{
		
		CacheConfigMgr.init();
		
		control = new JdbcControl();
		
		if(!verifyTroneOrderId())
		{
			System.out.println("不好意思，你给的TRONE ORDER ID关系链不完整");
			return;	
		}
		
		System.out.println("start analy data...");
		
		startAnanyData();
		
		//control.execute("source /data/sqlfiles/678_2016-07-01_00001_66114.txt");
		
	}
	
	public static boolean verifyTroneOrderId()
	{
		for(String strTroneOrderId : troneOrderIds.split(","))
		{
			int troneOrderId = StringUtil.getInteger(strTroneOrderId, -1);
			
			TroneOrderModel troneOrderModel = CpDataCache.getTroneOrderModelById(troneOrderId);
			
			if(troneOrderModel==null)
			{
				System.out.println("TroneOrderId " + troneOrderId + " is null");
				return false;
			}
			
			TroneModel troneModel = SpDataCache.getTroneById(troneOrderModel.getTroneId());
			
			if(troneModel==null)
			{
				System.out.println("TroneId " + troneOrderModel.getTroneId() + " is null");
				return false;
			}
			
			SpTroneModel spTroneModel = SpDataCache.loadSpTroneById(troneModel.getSpTroneId());
			
			if(spTroneModel==null)
			{
				System.out.println("SpTroneId " + troneModel.getSpTroneId() + " is null");
				return false;
			}
		}
		return true;
	}
	
	public static void startAnanyData()
	{
		Calendar ca = Calendar.getInstance();
		ca.set(year, month, 1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE,0);
		ca.set(Calendar.SECOND, 0);
		ca.add(Calendar.DAY_OF_MONTH, -1);
		int monthDays = ca.get(Calendar.DAY_OF_MONTH);
		ca.set(year, month-1, 1);
		
		//monthDays = 1;
		
		for(int i=1; i<=monthDays; i++)
		{
			ca.set(Calendar.DAY_OF_MONTH, i);
			
			String mrDate = sdf2.format(ca.getTime());
			
			long curMils = System.currentTimeMillis();
			
			System.out.println("start analy date:" + mrDate);
			
			List<SummerDataModel> dataList = loadSummerData(mrDate);
			
			for(SummerDataModel model : dataList)
			{
				addDataToMonthTable(mrDate,model);
			}
			
			
			System.out.println("cur rows " + finalDataRows + "  analy date " + mrDate + " spend mils:" + (System.currentTimeMillis() - curMils));
		}
	}
	
	
	public static void addDataToMonthTable(String mrDate,SummerDataModel dataModel)
	{
		int unSynDataRows = dataModel.getMrDataRows() - dataModel.getCpMrDataRows();
		
		TroneOrderModel troneOrderModel = CpDataCache.getTroneOrderModelById(dataModel.getTroneOrderId());
		TroneModel troneModel = SpDataCache.getTroneById(troneOrderModel.getTroneId());
		SpTroneModel spTroneModel = SpDataCache.loadSpTroneById(troneModel.getSpTroneId());
		
		int curDataRows = 1;
		
		Map<Integer,List<PhoneLocateModel>> phoneMap = LocateCache.getPhoneMap();
		List<PhoneLocateModel> phoneList = null;
		
		Random rand = new Random();
		PhoneLocateModel phoneModel = null;
		String phone = null;
		Date dMrDate = StringUtil.getDate(mrDate);
		int curUseMils = 0;
		
		//生成基础数据SQL
		while(curDataRows<=dataModel.getMrDataRows())
		{
			StringBuffer sb = new StringBuffer(512);
			sb.append(String.format(baseSpSql, year + String.format("%02d", month)));
			sb.append("\r\n");
			
			Calendar ca = Calendar.getInstance();
			ca.set(2016, 0, 1);
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE,0);
			ca.set(Calendar.SECOND, 0);
			
			int startRows = curDataRows;
			
			for(int i=0; i<100000; i++)
			{
				
				if(curDataRows>dataModel.getMrDataRows())
					break;
				
				if(spTroneModel.getOperator()>0)
				{
					if(phoneMap.get(spTroneModel.getOperator())!=null)
					{
						phoneList = phoneMap.get(spTroneModel.getOperator());
						
						if(phoneList!=null)
							phoneModel = phoneList.get(rand.nextInt(phoneList.size()-1));
					}
				}
				
				if(phoneModel==null)
				{
					phoneModel = new PhoneLocateModel();
					phoneModel.setCityId(416);
					phoneModel.setProvinceId(32);
					phoneModel.setPhonePrefix("1380000");
				}
				
				phone = phoneModel.getPhonePrefix() + String.format("%04d", rand.nextInt(9999));
				
				curUseMils = rand.nextInt(10000);
				
				sb.append("('','','" + phone + "','" + StringUtil.getMd5String(phone, 32) + "',460," + phoneModel.getProvinceId() + "," + phoneModel.getCityId() + "," 
						+ spTroneModel.getId() + "," + troneModel.getId() + "," + troneOrderModel.getId() + ",'" + troneModel.getTroneNum() + "','" + troneModel.getOrders() 
						+ "','" + shuffleForSortingString(dMrDate.getTime() + 8640*i + curUseMils + String.format("%06d", rand.nextInt(1000000))) + "','','',NULL,NULL,'DELIVRD',1,NULL,NULL,'" + mrDate + "','" + sdf1.format(new Date(dMrDate.getTime() + 8640*i + curUseMils)) + "',1," + troneModel.getSpApiUrlId() + "," + spTroneModel.getSpId() + "," + troneOrderModel.getCpId() + ",0," + spTroneModel.getTroneType() + ",NULL)");
				
				if(i==99999 || curDataRows==dataModel.getMrDataRows())
				{
					sb.append(";\r\n");
				}
				else
				{
					sb.append(",\r\n");
				}
				
				curDataRows++;
				finalDataRows++;
			}
			
			String fileName = dataModel.getTroneOrderId() + "_" + mrDate + "_" + String.format("%05d", startRows)+ "_" + (curDataRows-1) + ".txt";
			
			FileUtil.saveDataToFile(sb, "D:SJ_SQL_FILES/" + dataBase + "/", fileName);
			
			long curMils = System.currentTimeMillis();
			control.execute(sb.toString());
			System.out.println("insert data spend mils:" + (System.currentTimeMillis() - curMils));
		}
		
//		if(unSynDataRows>0)
//		{
//			long curUpdateMils = System.currentTimeMillis();
//			new JdbcControl().execute("UPDATE yd_bak.temp_tbl_mr_201607 SET syn_flag = 0 WHERE id IN ( SELECT id FROM ( SELECT id FROM yd_bak.temp_tbl_mr_201607 WHERE trone_order_id = " + dataModel.getTroneOrderId() + "  ORDER BY RAND() LIMIT " + unSynDataRows + ") s);");
//			System.out.println("update data rows " + unSynDataRows + " spend mils:" + (System.currentTimeMillis() - curUpdateMils));
//		}
	}
	
	public static List<SummerDataModel> loadSummerData(String mrDate)
	{
		return new SummerDataDao().loadMrSummerByTroneIds(dataBase, troneOrderIds, mrDate);
	}
	
	public static String shuffleForSortingString(String s)
	{
		char[] c = s.toCharArray();
		List<Character> lst = new ArrayList<Character>();
		for (int i = 0; i < c.length; i++)
		{
			lst.add(c[i]);
		}
		Collections.shuffle(lst);
		String resultStr = "";
		for (int i = 0; i < lst.size(); i++)
		{
			resultStr += lst.get(i);
		}
		return resultStr;
	}
	
}
