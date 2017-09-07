package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.dao.CpTroneDao;
import com.system.model.CpModel;
import com.system.model.CpRatioModel;
import com.system.model.CpTroneModel;
import com.system.model.TroneModel;

public class BaseDataCache
{
	private static List<CpModel> cpCache = new ArrayList<CpModel>();
	
	private static List<CpRatioModel> cpRatioCache = new ArrayList<CpRatioModel>();
	
	private static List<CpTroneModel> cpTroneCache = new ArrayList<CpTroneModel>();
	
	private static List<CpTroneModel> cpTroneHideCache = new ArrayList<CpTroneModel>();
	
	private static List<TroneModel> troneCache = new ArrayList<TroneModel>();
	
	protected static void setCpCache(List<CpModel> cpList)
	{
		cpCache = cpList;
	}
	
	protected static void setCpRatioCache(List<CpRatioModel> list)
	{
		cpRatioCache = list;
	}
	
	protected static void setCpTroneCache(List<CpTroneModel> list)
	{
		cpTroneCache = list;
	}
	
	protected static void setCpTroneHideCache(List<CpTroneModel> list)
	{
		cpTroneHideCache = list;
	}
	
	protected static void setTroneCache(List<TroneModel> list)
	{
		troneCache = list;
	}
	
	public static CpModel loadCpById(int cpId)
	{
		for(CpModel model : cpCache)
		{
			if(model.getCpId() == cpId)
				return model;
		}
		return null;
	}
	
	public static CpRatioModel getCpRatio(int cpId,int operator,int proId)
	{
		for(CpRatioModel model : cpRatioCache)
		{
			if(model.getCpId()==cpId && model.getOperator()==operator && model.getProId()==proId)
			{
				return model;
			}
		}
		return null;
	}
	
	public static List<CpTroneModel> loadCpTrone(int cpId,int province,int operator)
	{
		List<CpTroneModel> list = new ArrayList<CpTroneModel>();
		for(CpTroneModel model : cpTroneCache)
		{
			if(model.getCpId()==cpId 
					&& model.getProId()==province 
					&& model.getOperator()==operator)
			{
				list.add(model);
			}
		}
		return list;
	}
	
	/**
	 * 检查是否存在相同的通道
	 * @param cpId
	 * @param troneId
	 * @return
	 */
	public static boolean isExistCpTrone(int cpId,int troneId)
	{
		for(CpTroneModel model : cpTroneCache)
		{
			if(model.getCpId()==cpId && model.getTroneId()==troneId)
				return true;
		}
		
		return false;
	}
	
	/**
	 * 把系统匹配的 CP TRONE 增加进缓存
	 * @param cpTroneId
	 * @param cpId
	 * @param troneId
	 */
	public static void addCpTroneHideCache(int cpTroneId,int cpId,int troneId)
	{
		if(isExistCpTrone(cpId, troneId))
			return;
		
		CpTroneDao dao = new CpTroneDao();
		
		CpTroneModel troneModel = dao.getCpTrone(cpTroneId);
		
		if(troneModel!=null)
			cpTroneHideCache.add(troneModel);
		
	}
	
	public static void loadTroneList(int provinceId,int operator,int flowSize,int rang,int timeType)
	{
		
	}
	
}
