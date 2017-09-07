package com.system.cache;

import java.util.List;

import org.apache.log4j.Logger;

import com.system.dao.BaseDataDao;
import com.system.dao.CpTroneDao;
import com.system.dao.LocateDao;
import com.system.dao.SysConfigDao;
import com.system.model.SysCodeModel;
import com.system.sdao.ConfigPropertiesDao;

public class CacheConfigMgr
{
	private static Logger logger  = Logger.getLogger(CacheConfigMgr.class); 
	
	static
	{
		refreshAllCache();
	}
	
	/**
	 * 项目启动及手动刷新数据的时候把内存中的缓存全部刷一遍
	 */
	public static void refreshAllCache()
	{
		refreshUnusualCache();
		refreshFrequenceCache();
		logger.info("refreshAllCache finish");
	}
	
	/**
	 * 更新不常使用的缓存（类似于手机地区号的缓存 如LocateCache,减轻大数据读取对数据库造成的影响）
	 */
	public static void refreshUnusualCache()
	{
		refreshLocateCache();
		refreshSysConfigCache();
		logger.info("refreshUnusualCache finish");
	}
	
	/**
	 * 更新常变更的缓存，定时任务调用会刷新这一块
	 */
	public static void refreshFrequenceCache()
	{
		refreshCpCache();
		refreshConfigPropertiesCache();
		refreshCpTroneCache();
		logger.info("refreshFrequenceCache finish");
	}
	
	/**
	 * CP数据
	 */
	private static void refreshCpCache()
	{
		BaseDataDao dao = new BaseDataDao();
		BaseDataCache.setCpCache(dao.loadCpList());
		logger.info("refreshCpCache finish");
	}
	
	/**
	 * 系统数据配置缓存
	 */
	private static void refreshSysConfigCache()
	{
		SysConfigDao dao = new SysConfigDao();
		List<SysCodeModel> sysCodeList = dao.loadSysCodeList();
		SysConfigCache.setSysCodeList(sysCodeList);
		logger.info("refresh sys code list finish");
	}
	
	/**
	 * 刷新地区、手机区号等缓存
	 */
	private static void refreshLocateCache()
	{
		LocateDao dao = new LocateDao();
		LocateCache.setProvince(dao.loadProvinceList());
		LocateCache.setCity(dao.loadCityList());
		
		LocateCache.setPhoneLocate(dao.loadPhoneLocateMap());
		logger.info("还没有加载手机对应的地区哦");
		logger.info("refreshLocateCache finish");
	}
	
	private static void refreshConfigPropertiesCache()
	{
		ConfigPropertiesDao dao = new ConfigPropertiesDao();
		SysConfigCache.setConfigPropertiesList(dao.loadConfigProperties());
		logger.info("refreshConfigPropertiesCache finish");
	}
	
	private static void refreshCpTroneCache()
	{
		CpTroneDao dao = new CpTroneDao();
		BaseDataCache.setCpTroneCache(dao.loadCpTroneList(1));
		logger.info("refreshCpTroneCache finish");
	}
	
	
	public static void init(){}
}
