package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.ProvinceDao;
import com.system.dao.TroneOrderDao;
import com.system.model.CpSpTroneSynModel;
import com.system.model.PayCodeExportModel;
import com.system.model.ProvinceModel;
import com.system.model.SpTroneModel;
import com.system.model.TroneOrderModel;

public class TroneOrderServer
{
	public List<TroneOrderModel> loadTroneOrderListQiBa()
	{
		return new TroneOrderDao().loadTroneOrderListQiBa();
	}
	
	public List<TroneOrderModel> loadTroneOrderList()
	{
		return new TroneOrderDao().loadTroneOrderList();
	}
	
	public Map<String, Object> loadTroneOrder(int spId,int spTroneId,int cpId, int status,int pageIndex,String keyWord)
	{
		return new TroneOrderDao().loadTroneOrder(spId, spTroneId, cpId, status ,pageIndex,keyWord);
	}
	
	public boolean addTroneOrder(TroneOrderModel model)
	{
		return new TroneOrderDao().addTroneOrder(model);
	}
	
	public boolean updateTroneOrder(TroneOrderModel model)
	{
		return new TroneOrderDao().updateTroneOrder(model);
	}
	
	public TroneOrderModel getTroneOrderById(int id)
	{
		return new TroneOrderDao().getTroneOrderById(id);
	}
	
	public List<TroneOrderModel> loadTroneOrderListBySpTroneId(int spTroneId)
	{
		return new TroneOrderDao().loadTroneOrderListBySpTroneId(spTroneId);				
	}
	
	public List<TroneOrderModel> loadTroneOrderListByTroneId(int troneId)
	{
		return new TroneOrderDao().loadTroneOrderListByTroneId(troneId);				
	}
	
	public List<TroneOrderModel> loadTroneOrderListByCpSpTroneId(int cpId,int spTroneId,int status)
	{
		List<TroneOrderModel> list =new TroneOrderDao().loadTroneOrderListByCpSpTroneId(cpId, spTroneId, status);
		
		List<ProvinceModel> proList = new ProvinceDao().loadProvinceList();
		
		for(TroneOrderModel model : list)
		{
			String proStrList = "";
			
			String[] pros = model.getProvince() .split(",");
			
			if(pros==null || pros.length<0)
				continue;
			
			for(String proId : pros)
			{
				for(ProvinceModel province : proList)
				{
					if(proId.equals(province.getId() + ""))
						proStrList += province.getName() + ",";
				}
			}
			
			if(proStrList.length()>0)
				proStrList = proStrList.substring(0, proStrList.length()-1);
			
			model.setProvinceList(proStrList);
		}
		
		return list;
		
	}
	
	//Load Cp PayCodeExportModel
	public List<PayCodeExportModel> loadPayCodeExportModelListByCpSpTroneId(int cpId,int spTroneId,int status)
	{
		List<PayCodeExportModel> list =new TroneOrderDao().loadPayCodeExportModelListByCpSpTroneId(cpId, spTroneId, status);
		
		List<ProvinceModel> proList = new ProvinceDao().loadProvinceList();
		
		for(PayCodeExportModel model : list)
		{
			String proStrList = "";
			
			String[] pros = model.getPrivinces() .split(",");
			
			if(pros==null || pros.length<0)
				continue;
			
			for(String proId : pros)
			{
				for(ProvinceModel province : proList)
				{
					if(proId.equals(province.getId() + ""))
						proStrList += province.getName() + ",";
				}
			}
			
			if(proStrList.length()>0)
				proStrList = proStrList.substring(0, proStrList.length()-1);
			
			model.setPrivincesName(proStrList);
		}
		
		return list;
		
	}
	
	
	public CpSpTroneSynModel loadCpSpTroneSynModelById(int id)
	{
		return new TroneOrderDao().loadCpSpTroneSynModelById(id);
	}
	
	public void closeTroneOrderByTroneId(int troneId)
	{
		new TroneOrderDao().closeTroneOrderByTroneId(troneId);
	}
	
	public Map<String, Object> loadTroneOrder(int spId,int spTroneId,int cpId, int status,int pageIndex,String keyWord,int userId)
	{
		return new TroneOrderDao().loadTroneOrder(spId, spTroneId, cpId, status ,pageIndex,keyWord,userId);
	}
	/**
	 * 
	 * @param userId
	 * @param spId
	 * @param spTroneId
	 * @param cpId
	 * @param status
	 * @param pageIndex
	 * @param keyWord
	 * @return
	 */
	
	public Map<String, Object> loadTroneOrder(int userId,int spId,int spTroneId,int cpId, int status,int pageIndex,String keyWord)
	{
		return new TroneOrderDao().loadTroneOrder(userId,spId, spTroneId, cpId, status ,pageIndex,keyWord);
	}
	
	public Map<String, Object> loadSpTroneOrder(int userId,int spId,int spTroneId,int cpId, int status,int pageIndex,String keyWord)
	{
		return new TroneOrderDao().loadSpTroneOrder(userId,spId, spTroneId, cpId, status ,pageIndex,keyWord);
	}
	
	public static void main(String[] args)
	{
		new TroneOrderServer().loadPayCodeExportModelListByCpSpTroneId(129, -1, -1);
	}
	
	public List<SpTroneModel> loadSpTroneListByCpId(int cpId)
	{
		return new TroneOrderDao().loadSpTroneListByCpId(cpId);
	}
}



