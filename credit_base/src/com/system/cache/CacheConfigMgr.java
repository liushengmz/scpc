package com.system.cache;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.system.dao.BlackDao;
import com.system.dao.CpDataDao;
import com.system.dao.DayMonthLimitDao;
import com.system.dao.LocateDao;
import com.system.dao.SpDataDao;
import com.system.model.CityModel;
import com.system.model.CpTroneModel;
import com.system.model.ProvinceModel;
import com.system.model.SpTroneApiModel;
import com.system.model.SpTroneModel;
import com.system.model.TroneModel;
import com.system.model.TroneOrderModel;

public class CacheConfigMgr
{
	private static Logger logger  = Logger.getLogger(CacheConfigMgr.class); 
	
	static
	{
		refreshAllCache();
	}
	
	public static void refreshAllCache()
	{
		refreshTroneOrderCache();
		refreshSpTroneCache();
		refreshTroneCache();
		refreshSpTroneApiCache();
		refreshPhoneLocateCache();
		refreshProvinceCache();
		refreshCityCache();
		refreshCpSpTroneCache();
		refreshDayMonthLimitCache();
		refreshBlackCache();
	}
	
	public static void refreshAllTroneCache()
	{
		refreshTroneOrderCache();
		refreshSpTroneCache();
		refreshTroneCache();
		refreshSpTroneApiCache();
		refreshCpSpTroneCache();
	}
	
	public static void refreshAllLocateCache()
	{
		refreshPhoneLocateCache();
		refreshProvinceCache();
		refreshCityCache();
		refreshBlackCache();
	}
	
	public static void refreshCpSpTroneCache()
	{
		List<CpTroneModel> list = new CpDataDao().loadCpTrone();
		CpDataCache.setCpTroneList(list);
		logger.info("refreshCpSpTroneCache finish");
	}
	
	public static void refreshTroneOrderCache()
	{
		List<TroneOrderModel> list = new CpDataDao().loadTroneOrderList();
		CpDataCache.setTroneOrder(list);
		logger.info("refreshTroneOrderCache finish");
	}
	
	public static void refreshSpTroneCache()
	{
		List<SpTroneModel> list = new SpDataDao().loadSpTroneList();
		SpDataCache.setSpTroneList(list);
		logger.info("refreshSpTroneCache finish");
	}
	
	public static void refreshTroneCache()
	{
		List<TroneModel> list = new SpDataDao().loadTrone();
		SpDataCache.setTroneList(list);
		logger.info("refreshTroneCache finish");
	}
	
	public static void refreshSpTroneApiCache()
	{
		List<SpTroneApiModel> list = new SpDataDao().loadSpTroneApi();
		SpDataCache.setSpTroneApiList(list);
		logger.info("refreshSpTroneApiCache finish");
	}
	
	public static void refreshPhoneLocateCache()
	{
		 Map<String,Integer> map = new LocateDao().loadPhoneLocateMap();
		 LocateCache.setPhoneLocate(map);
		 logger.info("refreshPhoneLocateCache finish");
	}
	
	public static void refreshProvinceCache()
	{
		List<ProvinceModel> list = new LocateDao().loadProvinceList();
		LocateCache.setProvince(list);
		logger.info("refreshProvinceCache finish");
	}
	
	public static void refreshCityCache()
	{
		List<CityModel> list = new LocateDao().loadCityList();
		LocateCache.setCity(list);
		logger.info("refreshCityCache finish");
	}
	
	public static void refreshDayMonthLimitCache()
	{
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, -2);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = sdf.format(ca.getTime());
		DayMonthLimitDao dao = new DayMonthLimitDao();
		DayMonthLimitCache.setCpSpTroneDayLimit(dao.loadCpTroneDayLimit(startDate));
		DayMonthLimitCache.setCpSpTroneMonthLimit(dao.loadCpTroneMonthLimit(startDate));
		DayMonthLimitCache.setSpTroneMonthLimit(dao.loadSpTroneMonthMap(startDate));
		DayMonthLimitCache.setSpTroneDayLimit(dao.loadSpTroneDayLimit(startDate));
		logger.info("refreshDayMonthLimitCache finish");
	}
	
	public static void refreshBlackCache()
	{
		BlackDao dao = new BlackDao();
		
		List<String> blackPhoneList = dao.loadblackPhoneList();
		List<String> blackImsiList = dao.loadblackImsiList();
		List<String> blackImeiList = dao.loadblackImeiList();
		
		BlackConfigCache.setBlackPhoneList(blackPhoneList);
		BlackConfigCache.setBlackImsiList(blackImsiList);
		BlackConfigCache.setBlackImeiList(blackImeiList);
		
		logger.info("refreshBlackCache finish");
		
	}
	
	public static void init(){}
}
