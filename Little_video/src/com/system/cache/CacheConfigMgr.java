
package com.system.cache;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.system.dao.LocateDao;
import com.system.dao.LvChannelDao;
import com.system.dao.LvImgDao;
import com.system.dao.LvLevelDao;
import com.system.dao.LvRecommendDao;
import com.system.dao.LvVideoBaseDao;
import com.system.model.CityModel;
import com.system.model.LvChannelModel;
import com.system.model.LvImgModel;
import com.system.model.LvLevelModel;
import com.system.model.LvRecommendModel;
import com.system.model.LvVideoBaseModel;
import com.system.model.ProvinceModel;

public class CacheConfigMgr
{
	private static Logger logger = Logger.getLogger(CacheConfigMgr.class);

	static
	{
		refreshAllCache();
	}

	public static void refreshAllCache()
	{
		refreshProvinceCache();
		refreshCityCache();
		refreshProvinceCache();
		refreshLvImgCache();
		refreshLvLevelCache();
		refreshLvRecommendCache();
		refreshLvVideoBaseCache();
		refreshPhoneLocateMap();
		refreshLvChannelCache();
	}

	private static void refreshLvChannelCache()
	{
		List<LvChannelModel> list = new LvChannelDao().loadData();
		LvChannelCache.setCache(list);
		logger.info(String.format( "refreshLvVideoBaseCache finish,count:%d",list.size()));
	}

	public static void refreshPhoneLocateMap()
	{
		Map<String, Integer> list = new LocateDao().loadPhoneLocateMap();
		LocateCache.setPhoneLocate(list);
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

	public static void refreshLvImgCache()
	{
		List<LvImgModel> list = new LvImgDao().loadLvImg();
		LvImgCache.setCache(list);
		logger.info("refreshLvImgCache finish");
	}

	public static void refreshLvLevelCache()
	{
		List<LvLevelModel> list = new LvLevelDao().loadLvLeve();
		LvLevelCache.setCache(list);
		logger.info("refreshLvLevelCache finish");
	}

	public static void refreshLvRecommendCache()
	{
		List<LvRecommendModel> list = new LvRecommendDao().loadLvRecommend();
		LvRecommendCache.setCache(list);
		logger.info("LvRecommendCache finish");
	}

	public static void refreshLvVideoBaseCache()
	{
		List<LvVideoBaseModel> list = new LvVideoBaseDao().loadLvVideoBase();
		LvVideoBaseCache.setCache(list);
		logger.info("refreshLvVideoBaseCache finish");
	}

	public static void init()
	{
	}
}
