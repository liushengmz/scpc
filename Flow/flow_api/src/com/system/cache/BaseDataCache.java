package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.dao.CpDataDao;
import com.system.model.BasePriceModel;
import com.system.model.CpModel;
import com.system.model.CpRatioModel;
import com.system.model.CpTroneModel;
import com.system.model.TroneModel;
import com.system.server.RedisServer;

public class BaseDataCache
{
	private static List<CpModel> cpCache = new ArrayList<CpModel>();
	
	private static List<CpRatioModel> cpRatioCache = new ArrayList<CpRatioModel>();
	
	private static List<CpTroneModel> cpTroneCache = new ArrayList<CpTroneModel>();
	
	private static List<CpTroneModel> cpTroneHideCache = new ArrayList<CpTroneModel>();
	
	private static List<TroneModel> troneCache = new ArrayList<TroneModel>();
	
	private static List<BasePriceModel> basePriceCache = new ArrayList<BasePriceModel>();
	
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
	
	protected static void setBasePriceCache(List<BasePriceModel> list)
	{
		basePriceCache = list;
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
	
	
	public static CpTroneModel loadCpTrone(int provinceId,int operator,int flowSize,int rang,int timeType)
	{
		int index = -1;
		CpTroneModel model = null;
		int tmpRatio = 0;
		int ratio = 0;
		
		for(int i=0; i<cpTroneCache.size(); i++)
		{
			model = cpTroneCache.get(i);
			if (model.getProId() == provinceId
					&& model.getOperator() == operator
					&& model.getFlowSize() == flowSize
					&& model.getRang() == rang
					&& model.getTimeType() == timeType)
			{
				//并找到利润最高的一条通道
				tmpRatio = model.getCpRatio() - model.getSpRatio();
				if(tmpRatio>0 && tmpRatio>ratio)
				{
					ratio = tmpRatio;
					index = i;
				}
			}
		}
		
		//如果初始索引大于0说明着有找到至少一条通道是符合要求的，返回该通道则OK
		if(index>=0)
		{
			return cpTroneCache.get(index);
		}
		
		return null;
	}
	
	/**
	 * 检查是否存在相同的通道
	 * @param cpId
	 * @param troneId
	 * @return
	 */
	public static boolean isExistCpTrone(int cpId,int troneId)
	{
		System.out.println("cpId:" + cpId + ";troneId:" + troneId);
		for(CpTroneModel model : cpTroneCache)
		{
			if(model.getCpId()==cpId && model.getTroneId()==troneId)
				return true;
		}
		
		for(CpTroneModel model : cpTroneHideCache)
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
		
		CpTroneModel cpTroneModel = new CpDataDao().getCpTrone(cpTroneId);
		
		if(cpTroneModel!=null)
			cpTroneHideCache.add(cpTroneModel);
		
	}
	
	/**
	 * 根据条件从隐藏CP TRONE LIST 取通道回来
	 * @param cpId
	 * @param troneId
	 * @return
	 */
	public static CpTroneModel loadCpTroneHideModel(int cpId,int troneId)
	{
		for(CpTroneModel model : cpTroneCache)
		{
			if(model.getCpId() == cpId && model.getTroneId() == troneId)
				return model;
		}
		return null;
	}
	
	/**
	 * 根据CP TRONE ID 取得对象回来
	 * @param cpTroneId
	 * @return
	 */
	public static CpTroneModel getCpTroneModelById(int cpTroneId)
	{
		for(CpTroneModel model : cpTroneHideCache)
		{
			if(model.getId() == cpTroneId)
				return model;
		}
		
		for(CpTroneModel model : cpTroneCache)
		{
			if(model.getId() == cpTroneId)
				return model;
		}
		
		return null;
	}
	
	/**
	 * 根据请求的条件找到符合要求的通道
	 * @param provinceId 省份
	 * @param operator 运营商
	 * @param flowSize 流量包大小
	 * @param rang 0全国 1省份
	 * @param timeType 1到月底 2是20天
	 * @param cpRatio 我方给CP配置的折扣
	 * @return
	 */
	public static TroneModel loadTroneModel(int provinceId,int operator,int flowSize,int rang,int timeType,int cpRatio)
	{
		int index = -1;
		TroneModel model = null;
		int tmpRatio = 0;
		int ratio = 0;
		
		for(int i=0; i<troneCache.size(); i++)
		{
			model = troneCache.get(i);
			
			//找满足条件的通道出来
			if (model.getProId() == provinceId
					&& model.getOperator() == operator
					&& model.getFlowSize() == flowSize
					&& model.getRang() == rang
					&& model.getTimeType() == timeType
					//满足这些条件的同时要在上游有余额
					&& RedisServer.getSpRemainingMoney(model.getSpId()) > 
						(model.getPrice() * model.getRatio() / 1000))
			{
				//并找到利润最高的一条通道
				tmpRatio = cpRatio - model.getRatio();
				if(tmpRatio>0 && tmpRatio > ratio)
				{
					ratio = tmpRatio;
					index = i;
				}
			}
		}
		
		//如果初始索引大于0说明着有找到至少一条通道是符合要求的，返回该通道则OK
		if(index>=0)
		{
			return troneCache.get(index);
		}
		
		return null;
	}
	
	/**
	 * 根据手机号码的运营商和请求的流量包大小把对应的基础数据取出来
	 * @param operator
	 * @param num
	 * @return
	 */
	public static BasePriceModel loadBasePrice(int operator,int num)
	{
		for(BasePriceModel model : basePriceCache)
		{
			if(model.getOperator()==operator && model.getNum()==num)
				return model;
		}
		
		return null;
	}
	
}
