package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.CpSpTroneRateDao;
import com.system.model.CpSpTroneRateModel;

public class CpSpTroneRateServer
{
	private static boolean isRunningRateAnaly = false;
	
	
	public Map<String, Object> loadCpSpTroneRate(String keyWord,int pageIndex)
	{
		return new CpSpTroneRateDao().loadCpSpTroneRate(keyWord, pageIndex);
	}
	
	public CpSpTroneRateModel loadCpSpTroneRateById(int id)
	{
		return new CpSpTroneRateDao().loadCpSpTroneRateById(id);
	}
	
	public void addCpSpTroneRate(CpSpTroneRateModel model)
	{
		new CpSpTroneRateDao().addCpSpTroneRate(model);
	}
	
	public void updateCpSpTroneRate(CpSpTroneRateModel model)
	{
		new CpSpTroneRateDao().updateCpSpTroneRate(model);
	}
	
	public void updateCpSpTroneLimit(CpSpTroneRateModel model)
	{
		new CpSpTroneRateDao().updateCpSpTroneLimit(model);
	}
	
	public void updateCpSpTroneRate(int id,float rate)
	{
		new CpSpTroneRateDao().updateCpSpTroneRate(id, rate);
	}
	
	public void delCpSpTroneRate(int id)
	{
		new CpSpTroneRateDao().delCpSpTroneRate(id);
	}
	
	public void syncUnAddCpSpTroneRate()
	{
		if(isRunningRateAnaly)
			return;
		
		isRunningRateAnaly = true;
		
		//更新tbl_trone_order里对应的 jie suan lv id
		try
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					CpSpTroneRateDao dao = new CpSpTroneRateDao();
					
					dao.syncUnAddCpSpTroneRate();
					
					List<Map<String, Object>> list = dao.loadNullTroneOrderCpTroneRate();
					
					if(list==null || list.isEmpty())
						return;
					
					for(Map<String, Object> map : list)
					{
						int troneOrderId = (Integer)map.get("troneOrderId");
						
						int spTroneId = (Integer)map.get("spTroneId");
						
						int cpId = (Integer)map.get("cpId");
						
						int cpTroneRateId = dao.loadCpTroneRate(spTroneId, cpId);
						
						dao.updateCpTroneRate(troneOrderId, cpTroneRateId);
					}
					
				}
			}).start();
		}
		catch(Exception ex)
		{
			System.out.println("SORRY, UPDATE TBL_TRONE_ORDER JIE SUAN LV ID FAIL:" + ex.getMessage());
		}
		finally
		{
			isRunningRateAnaly = false;
		}
	}
	
	/**
	 * 获取CP对应的结算类型指定时间的特殊结算率
	 * @param cpId
	 * @param jsType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<CpSpTroneRateModel> loadCpSpTroneRateList(int cpId,int jsType,String startDate,String endDate)
	{
		return new CpSpTroneRateDao().loadCpSpTroneRateList(cpId, jsType, startDate, endDate);
	}
	
}
