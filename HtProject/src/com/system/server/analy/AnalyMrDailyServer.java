package com.system.server.analy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import org.apache.log4j.Logger;

import com.system.dao.analy.CpMrDao;
import com.system.dao.analy.HoldConfigDao;
import com.system.dao.analy.MrDao;
import com.system.dao.analy.MrSummerDao;
import com.system.model.analy.HoldConfigModel;
import com.system.model.analy.MrModel;
import com.system.util.StringUtil;

public class AnalyMrDailyServer 
{
	
	Logger log = Logger.getLogger(AnalyMrDailyServer.class);
	
	public void analyMrDailyWithDate(String startDate,String endDate)
	{
		log.info("Start Analy Mr Daily " + startDate + " To " + endDate);
		
		String tableName = StringUtil.getMonthFormat(startDate);
		
		MrSummerDao mrSummerDao = new MrSummerDao();
		
		mrSummerDao.deleteMrSummer(startDate, endDate);
		
		mrSummerDao.analyMrToSummer(tableName, startDate, endDate);
		
		log.info("Analy Mr To Summer Finish");
		
		//以后再确定是否打开，先搞定报表
		/*
		
		if(isAnalyMrToSummerSuc)
		{
			HoldConfigDao holdConfigDao = new HoldConfigDao();
			
			holdConfigDao.deleteHoldConfig(startDate, endDate);
			
			holdConfigDao.insertHoldConfig(startDate, endDate);
			
			log.info("Insert Hold Config Finish");
		}
		else
			log.info("Analy Mr To Summer Fail");
		
		*/
	}
	
	
	public void analyHoldConfigList(String startDate,String endDate)
	{	
		log.info("start analy hold config list");;
		
		HoldConfigDao holdConfigDao = new HoldConfigDao();
		
		ArrayList<HoldConfigModel> holdConfigList =  holdConfigDao.loadHoldConfig(startDate, endDate);
		
		for(HoldConfigModel model : holdConfigList)
		{
			loadCpMrList(StringUtil.getMonthFormat(startDate),model);
		}		
		
		log.info("end analy hold config list");
	}
	
	public void analyCpMrToSummer(String startDate,String endDate)
	{
		log.info("start analy cp mr to summer [" + startDate + ";" + endDate + "]");
		
		CpMrDao cpMrDao = new CpMrDao();
		
		cpMrDao.deleteCpMrSummer(startDate, endDate);
		
		cpMrDao.analyCpMrToCpMrSummer(StringUtil.getMonthFormat(startDate), startDate, endDate);	
		
		log.info("finish analy cp mr to summer");
		
		//cpMrDao.updateCpMrSummerAmount(startDate, endDate);
		
		//log.info("finish update cp mr summer amount");
	}
	
	public void updateHoldConfigActureAmount(String startDate,String endDate)
	{
		log.info("start update hold config acture amount [" + startDate + ";" + endDate + "]");
		
		HoldConfigDao holdConfigDao = new HoldConfigDao();
		
		holdConfigDao.updateHoldConfigActureAmount(startDate, endDate);
		
		log.info("end update hold config acture amount");
	}
	
	
	private void loadCpMrList(String tableName,HoldConfigModel configModel)
	{
		log.info("start load cp mr list (tableName:" + tableName + ";cp_id:" + configModel.getCpId() + ";mr_date:" + configModel.getFeeDate() + ")");
		
		boolean isSynAll = false;
		
		int randomSeed = 0;
		
		if(configModel.getHoldType()==1 && configModel.getHoldPersent() ==0)
			isSynAll = true;
		
		if(configModel.getHoldType()==1 && configModel.getHoldPersent() !=0)
			randomSeed = (100-configModel.getHoldPersent())*10000;
		
		if(configModel.getHoldType()==2 && configModel.getSynAmount() == 0)
			isSynAll = true;
		
		if(configModel.getHoldType()==2 && configModel.getSynAmount() != 0)
		{
			randomSeed = (int)((configModel.getSynAmount()/configModel.getTotalAmount())*1000000);
		}
		
		Random rand  = new Random();
		
		MrDao  mrDao = new MrDao();
		
		ArrayList<Integer> keepList = new ArrayList<Integer>();
		
		ArrayList<MrModel> arrList = mrDao.loadMrData(configModel.getCpId(), tableName, configModel.getFeeDate(), configModel.getFeeDate());
		
		boolean synMr = false;
		
		//先筛选，后插入，最后更新
		for(MrModel model : arrList)
		{
			synMr = false;
			
			if(isSynAll)
				synMr = true;
			else
			{
				if(rand.nextInt(1000000)<=randomSeed)
					synMr = true;
			}
			
			if(synMr)
			{
				model.setSynToCp(true);
			}
			else
			{
				keepList.add(model.getId());
			}
		}
		
		log.info("finish load cp mr list");
		
		CpMrDao cpMrDao = new CpMrDao();
		
		cpMrDao.deleteCpMr(tableName, configModel.getCpId(), configModel.getFeeDate());
		
		cpMrDao.insertCpMr(tableName, arrList);
		
		log.info("finish insert cp mr");
		
		mrDao.updateMrflag(tableName, keepList);
		
		log.info("finish update mr flag and keep (cp_id:" + configModel.getCpId() + ";mr_date:" 
				+ configModel.getFeeDate() + ") not syn datarows size:" + keepList.size());
	}
	
	
	/*
	private String getTableName(String startDate)
	{
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		
		try
		{
			return sdf2.format(sdf1.parse(startDate));
		}
		catch(Exception ex)
		{
			log.error("convert startDate error:" + startDate);
		}
		
		return sdf2.format(new Date());
	}
	*/
	
	public void startAnalyDailyMr()
	{
		log.info("start analy mr daily");
		String startDate = StringUtil.getPreDayOfMonth();
		analyMrDailyWithDate(startDate,startDate);
		log.info("start analy cp mr daily");
		analyCpMrToSummer(startDate,startDate);
		log.info("end analy mr daily");
	}
	
	public static void main(String[] args)
	{
		AnalyMrDailyServer amd = new AnalyMrDailyServer();
		
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i =0; i<28; i++)
		{
			String date = sdf.format(ca.getTime());
			amd.analyMrDailyWithDate(date,date);
			amd.analyCpMrToSummer(date,date);
			ca.add(Calendar.DAY_OF_MONTH, 1);
		}
		
	}
	
	/*
	public static void main(String[] args) 
	{
		AnalyMrDailyServer amd = new AnalyMrDailyServer();
		//分析指定日期的数据到MR SUMMER 表,并更新金额,同量把tbl_mr_summer数据分析到扣量配置表 tbl_hold_config
		amd.analyMrDailyWithDate("2015-09-01","2015-09-30");
		//根据扣量配置表 tbl_hold_config用户配置的数据，分析要同步给CP的详细数据并插入CP MR 的月表，同时更新tbl_mr_yyyyMM的同步标识
		//amd.analyHoldConfigList("2015-09-01","2015-09-20");
		//分析tbl_cp_mr_yyyyMM指日期的数据到tbl_cp_mr_summer
		amd.analyCpMrToSummer("2015-09-01","2015-09-20");
		//把真实同步给CP的数据的金额填充到tbl_hold_config并标识编辑状态为不可编辑，表示已同步，不能再更改
		//amd.updateHoldConfigActureAmount("2015-09-01","2015-09-20");
		System.out.println("finish");
	}
	*/
	
	
}
