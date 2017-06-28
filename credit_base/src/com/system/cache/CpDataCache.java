package com.system.cache;

import java.util.ArrayList;
import java.util.List;

import com.system.model.CpTroneModel;
import com.system.model.TroneOrderModel;

/**
 * 所有与CP相关的数据都存储在这个缓存
 * @author Andy.Chen
 *
 */
public class CpDataCache
{
	//private static Logger logger = Logger.getLogger(CpDataCache.class);
	
	private static List<TroneOrderModel> _troneOrderList = new ArrayList<TroneOrderModel>();
	
	private static List<CpTroneModel> _cpTroneList = new ArrayList<CpTroneModel>();
	
	protected static void setTroneOrder(List<TroneOrderModel> troneOrderList)
	{
		_troneOrderList = troneOrderList;
	}
	
	protected static void setCpTroneList(List<CpTroneModel> cpTroneList)
	{
		_cpTroneList = cpTroneList;
	}
	
	/**
	 * 检测是否存在传过来的TRONE ORDER ID，如果存在则返回 troneId
	 * @param troneOrderId
	 * @return
	 */
	public static int getTroneIdByTroneOrderId(int troneOrderId)
	{
		if(troneOrderId<=0)
			return -1;
		
		for(TroneOrderModel model : _troneOrderList)
		{
			if(model.getId()==troneOrderId)
			{
				return model.getTroneId();
			}
		}
		return -1;
	}
	
	/**
	 * 根据troneOrderId取得TroneOrderModel
	 * @param troneOrderId
	 * @return
	 */
	public static TroneOrderModel getTroneOrderModelById(int troneOrderId)
	{
		if(troneOrderId<=0)
			return null;
		
		for(TroneOrderModel model : _troneOrderList)
		{
			if(model.getId()==troneOrderId)
			{
				return model;
			}
		}
		return null;
	}
	
	/**
	 * 根据TroneOrderId/PayCode取得CP业务带有日月限的MODEL
	 * @param troneOrderId
	 * @return
	 */
	public static CpTroneModel getCpTroneByTroneOrderId(int troneOrderId)
	{
		TroneOrderModel model = getTroneOrderModelById(troneOrderId);
		
		if(model==null)
			return null;
		
		for(CpTroneModel cpTroneModel : _cpTroneList)
		{
			if (cpTroneModel.getCpId() == model.getCpId()
					&& cpTroneModel.getSpTroneId() == model.getSpTroneId())
				return cpTroneModel;
		}
		
		return null;
	}
	
	public static StringBuffer loadCpSpTroneList()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<table border='1'><tr>");
		
		int i=1;
		
		for(CpTroneModel model : _cpTroneList)
		{
			sb.append("<td>" + (model.getCpId() + "-" + model.getSpTroneId() 
			+ "-" + model.getDayLimit() + "-" + model.getMonthLimit()) + "</td>");
			
			if(i%10==0)
				sb.append("</tr><tr>");
			i++;
		}
		
		sb.append("</tr></table>");
		
		return sb;
	}
	
	public static StringBuffer loadTroneOrderList()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<table border='1'><tr>");
		int i=1;
		
		for(TroneOrderModel model : _troneOrderList)
		{
			sb.append("<td>" + (model.getId() + 100000) + "</td>");
			
			if(i%15==0)
				sb.append("</tr><tr>");
			i++;
		}
		
		sb.append("</tr></table>");
		
		return sb;
	}
}
