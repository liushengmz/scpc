package com.system.server;

import java.util.Map;

import com.system.dao.SingleCpSpTroneRateDao;
import com.system.model.CpSpTroneRateModel;
import com.system.model.SingleCpSpTroneRateModel;

public class SingleCpSpTroneRateServer
{
	public Map<String,Object> loadSingleCpSpTroneRate(int id,int pageIndex)
	{
		return new SingleCpSpTroneRateDao().loadSingleCpSpTroneRate(id, pageIndex);
	}
	
	public SingleCpSpTroneRateModel loadSinleCpSpTroneRateById(int id)
	{
		return new SingleCpSpTroneRateDao().loadSinleCpSpTroneRateById(id);
	}
	
	public void updateSingleRate(SingleCpSpTroneRateModel model)
	{
		SingleCpSpTroneRateDao dao = new SingleCpSpTroneRateDao();
		SingleCpSpTroneRateModel oriModel = dao.loadSinleCpSpTroneRateById(model.getId());
		if(oriModel.getRate()!=model.getRate())
		{
			updateCpMrRate(oriModel.getCpId(),oriModel.getSpTroneId(),model.getRate(),oriModel.getStartDate(),oriModel.getEndDate());
		}
		dao.updateSingleRate(model);
	}
	
	public void delSingleRate(int id)
	{
		SingleCpSpTroneRateDao dao = new SingleCpSpTroneRateDao();
		SingleCpSpTroneRateModel oriModel = dao.loadSinleCpSpTroneRateById(id);
		updateCpMrRate(oriModel.getCpId(),oriModel.getSpTroneId(),oriModel.getDefaultRate(),oriModel.getStartDate(),oriModel.getEndDate());
		dao.delSingleRate(id);
	}
	
	public void addSingleRate(SingleCpSpTroneRateModel model)
	{
		CpSpTroneRateModel oriModel = new CpSpTroneRateServer().loadCpSpTroneRateById(model.getCpSpTroneId());
		updateCpMrRate(oriModel.getCpId(),oriModel.getSpTroneId(),model.getRate(),model.getStartDate(),model.getEndDate());
		new SingleCpSpTroneRateDao().addSingleRate(model);
	}
	
	public boolean isRateDateCross(int cpTroneRateId,String startDate,String endDate)
	{
		return new SingleCpSpTroneRateDao().isRateDateCross(cpTroneRateId, startDate, endDate);
	}
	
	//暂时把更新CP MR表结算率的功能先停掉
	private void updateCpMrRate(int cpId,int spTroneId,float rate,String startDate,String endDate)
	{
		//new MrServer().updateCpMrRate(cpId, spTroneId, rate, startDate, endDate);
	}
	
}
