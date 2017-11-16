package com.andy.system.cache;

import java.util.List;

import org.apache.log4j.Logger;

import com.andy.system.dao.ConfigPropertiesDao;
import com.andy.system.dao.SysConfigDao;
import com.andy.system.model.SysCodeModel;


public class BaseCacheConfigMgr
{
	protected static Logger logger  = Logger.getLogger(BaseCacheConfigMgr.class);
	
	public static void refreshAllCache()
	{
		refreshSysConfigCache();
		refreshConfigPropertiesCache();
	}
	
	/**
	 * 系统里各种返回码缓存
	 */
	public static void refreshSysConfigCache()
	{
		SysConfigDao dao = new SysConfigDao();
		List<SysCodeModel> sysCodeList = dao.loadSysCodeList();
		SysConfigMgr.setSysCodeList(sysCodeList);
		logger.info("refresh sys code list finish");
	}
	
	/**
	 * 系统数据配置缓存
	 */
	private static void refreshConfigPropertiesCache()
	{
		ConfigPropertiesDao dao = new ConfigPropertiesDao();
		SysConfigMgr.setConfigPropertiesList(dao.loadConfigProperties());
		logger.info("refreshConfigPropertiesCache finish");
	}
}
