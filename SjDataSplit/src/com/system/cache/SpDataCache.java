package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.system.constant.Constant;
import com.system.model.SpTroneApiModel;
import com.system.model.SpTroneModel;
import com.system.model.TroneModel;

public class SpDataCache
{
	private static Logger logger = Logger.getLogger(SpDataCache.class);
	
	private static List<SpTroneModel> _spTroneList = new ArrayList<SpTroneModel>();
	
	private static List<TroneModel> _troneList = new ArrayList<TroneModel>();
	
	private static List<SpTroneApiModel> _spTroneApiList = new ArrayList<SpTroneApiModel>();
	
	protected static void setSpTroneList(List<SpTroneModel> spTroneList)
	{
		_spTroneList = spTroneList;
	}
	
	protected static void setTroneList(List<TroneModel> list)
	{
		_troneList = list;
	}
	
	protected static void setSpTroneApiList(List<SpTroneApiModel> spTroneApiList)
	{
		_spTroneApiList = spTroneApiList;
	}
	
	/**
	 * 通过判断是否存在业务并且业务是否有对应的API来确定状态
	 * @param spTroneId
	 * @return
	 */
	public static int getSpTroneStatus(int spTroneId)
	{
		for(SpTroneModel model : _spTroneList)
		{
			if(model.getId() == spTroneId)
			{
				if(model.getSpTroneApiId()>0)
				{
					for(SpTroneApiModel apiModel : _spTroneApiList)
					{
						if(apiModel.getId()==model.getSpTroneApiId())
						{
							return model.getStatus() == 0
									? Constant.CP_CP_TRONE_STATUS_SUSPEND : 0;
						}
					}
					break;
				}
			}
		}
		
		return Constant.CP_CP_TRONE_STATUS_SUSPEND;
	}
	
	/**
	 * 通过业务找到SP通道的对像
	 * @param spTroneId
	 * @return
	 */
	public static SpTroneApiModel getSpTroneApibySpTroneId(int spTroneId)
	{
		for(SpTroneModel model : _spTroneList)
		{
			if(model.getId()==spTroneId && model.getStatus()==1)
			{
				return getSpTroneApibyId(model.getSpTroneApiId());
			}
		}
		return null;
	}
	
	/**
	 * 通过SP通道ID找到对像
	 * @param spTroneApiId
	 * @return
	 */
	public static SpTroneApiModel getSpTroneApibyId(int spTroneApiId)
	{
		for(SpTroneApiModel model : _spTroneApiList)
		{
			if(model.getId()==spTroneApiId)
			{
				return model;
			}
		}
		return null;
	}
	
	
	/**
	 * 根据业务获取通道
	 * @param spTroneId
	 * @return
	 */
	public static List<TroneModel> loadTroneBySpTroneId(int spTroneId)
	{
		List<TroneModel> list = new ArrayList<TroneModel>();
		for(TroneModel model : _troneList)
		{
			if(model.getSpTroneId()==spTroneId 
					&& model.getStatus()==1)
			{
				list.add(model);
			}
		}
		return list;
	}
	
	/**
	 * 通过troneId取得SpTroneApiModel
	 * @param troneId
	 * @return
	 */
	public static SpTroneApiModel loadSpTroneApiByTroneId(int troneId)
	{
		for(TroneModel model : _troneList)
		{
			if(model.getId()==troneId && model.getStatus()==1)
			{
				return getSpTroneApibySpTroneId(model.getSpTroneId());
			}
		}
		return null;
	}
	
	/**
	 * 通过通道ID取得业务对象
	 * @param troneId
	 * @return
	 */
	public static SpTroneModel loadSpTroneByTroneId(int troneId)
	{
		for(TroneModel model : _troneList)
		{
			if(model.getId()==troneId)
			{
				return loadSpTroneById(model.getSpTroneId());
			}
		}
		return null;
	}
	
	/**
	 * 通过ID取得业务对象
	 * @param spTroneId
	 * @return
	 */
	public static SpTroneModel loadSpTroneById(int spTroneId)
	{
		for(SpTroneModel model : _spTroneList)
		{
			if(model.getId()==spTroneId)
			{
				return model;
			}
		}
		
		return null;
	}
	
	/**
	 * 根据ID取得通道的数据
	 * @param troneId
	 * @return
	 */
	public static TroneModel getTroneById(int troneId)
	{
		for(TroneModel model : _troneList)
		{
			if(model.getId()==troneId)
			{
				return model;
			}
		}
		return null;
	}
}
