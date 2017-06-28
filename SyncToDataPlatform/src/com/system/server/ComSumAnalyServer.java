package com.system.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.system.dao.ComSumSummerDao;
import com.system.model.ComSumSummerModel;
import com.system.model.FeeDateDataModel;
import com.system.util.StringUtil;

public class ComSumAnalyServer
{
	
	Logger logger = Logger.getLogger(ComSumAnalyServer.class);
	
	/**
	 * 去分析指定公司指定日期的数据
	 * @param coId
	 * @param startDate
	 * @param endDate
	 */
	public void analyComSumData(int coId,String startDate,String endDate)
	{
		ComSumSummerDao dao = new ComSumSummerDao();
		dao.delDailyData(coId, startDate, endDate);
		List<ComSumSummerModel> list = dao.loadComSumSummerData(coId,startDate,endDate);
		
		String head = "INSERT INTO comsum_config.`tbl_data_summer`(co_id,trone_id,province_id,data_rows,amount,mr_date,record_type,cp_id) VALUES ";
		StringBuffer sbData = new StringBuffer(512);
		
		if(list!=null && !list.isEmpty())
		{
			logger.info("分析数据：" + coId +"->" + startDate + "->" + endDate + "->SIZE:" + list.size());
			ComSumSummerModel model = null;
			for(int i=0; i<list.size(); i++)
			{
				model = list.get(i);
				
				sbData.append("(" + coId + "," + model.getTroneId() + "," + model.getProvinceId() + "," + model.getDataRows() + "," + model.getAmount() + ",'" + model.getMrDate() + "'," + model.getRecordType() + "," + model.getCpId() + "),");
				
				if((i+1)%500==0)
				{
					sbData.deleteCharAt(sbData.length()-1);
					sbData.append(";");
					dao.addCpDailyData(head + sbData.toString());
					sbData = new StringBuffer(512);
				}
			}
			if(sbData.length()>0)
			{
				sbData.deleteCharAt(sbData.length()-1);
				sbData.append(";");
				dao.addCpDailyData(head + sbData.toString());
			}
		}
	}
	
	/**
	 * 开始分析各大公司的数据
	 */
	public void startAnalyComSumData()
	{
		Calendar ca = Calendar.getInstance();
		
		ca.add(Calendar.DAY_OF_MONTH, -1);
		
		String endDate = StringUtil.getDateFormat(ca.getTime());
		
		ca.add(Calendar.MONTH, -2);
		
		String startDate = StringUtil.getDateFormat(ca.getTime());
		
		ComSumSummerDao dao = new ComSumSummerDao();
		
		List<String> analyDataList = new ArrayList<String>();
		
		FeeDateDataModel oriModel = null;
		FeeDateDataModel descModel = null;
		
		for(int i=1; i<=4; i++)
		{
			Map<String, FeeDateDataModel> descMap = dao.loadDescSource(i, startDate, endDate);
			Map<String, FeeDateDataModel> oriMap = dao.loadOriSource(i, startDate, endDate);
			
			analyDataList.clear();
			
			for(String oriDate : oriMap.keySet())
			{
				oriModel = oriMap.get(oriDate);
				descModel = descMap.get(oriDate);
				
				//如果目标数据为空或者是两者金额不一致，就需要重新分析了
				if(descModel==null || oriModel.getAmount() != descModel.getAmount())
				{
					analyDataList.add(oriDate);
				}
			}
			
			if(!analyDataList.isEmpty())
			{
				startAnalyComSumData(i,analyDataList);
			}
		}
	}
	
	private void tempAnalyData()
	{
		//控制月份(0-11)
		for(int i=0; i<=2; i++)
		{
			Calendar ca = Calendar.getInstance();
			
			ca.set(Calendar.YEAR,2017);
			ca.set(Calendar.MONTH, i);
			ca.set(Calendar.DAY_OF_MONTH,1);
			
			String startDate = StringUtil.getDateFormat(ca.getTime());
			
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.DAY_OF_MONTH, -1);
			
			String endDate = StringUtil.getDateFormat(ca.getTime());
			
			System.out.println(startDate + "---" + endDate);
			
			tempAnalyData(startDate,endDate);
		}
		
	}
	
	private void tempAnalyData(String startDate,String endDate)
	{
		ComSumSummerDao dao = new ComSumSummerDao();
		
		List<String> analyDataList = new ArrayList<String>();
		
		FeeDateDataModel oriModel = null;
		FeeDateDataModel descModel = null;
		
		//控制公司
		for(int i=4; i<=4; i++)
		{
			Map<String, FeeDateDataModel> descMap = dao.loadDescSource(i, startDate, endDate);
			Map<String, FeeDateDataModel> oriMap = dao.loadOriSource(i, startDate, endDate);
			
			analyDataList.clear();
			
			for(String oriDate : oriMap.keySet())
			{
				oriModel = oriMap.get(oriDate);
				descModel = descMap.get(oriDate);
				
				//如果目标数据为空或者是两者金额不一致，就需要重新分析了
				if(descModel==null || oriModel.getAmount() != descModel.getAmount())
				{
					analyDataList.add(oriDate);
				}
			}
			
			if(!analyDataList.isEmpty())
			{
				startAnalyComSumData(i,analyDataList);
			}
		}
	}
	
	/**
	 * 单独分析每一天的数据
	 * @param coId
	 * @param analyDataList
	 */
	private void startAnalyComSumData(int coId,List<String> analyDataList)
	{
		for(String date : analyDataList)
		{
			analyComSumData(coId, date, date);
		}
	}
	
	public static void main(String[] args)
	{
		ComSumAnalyServer csas = new ComSumAnalyServer();
		//csas.analyComSumData(4, "2015-10-01", "2016-10-01");
		csas.tempAnalyData();
		
		//csas.tempAnalyData("2016-09-01", "2016-09-01");
	}
	
}
