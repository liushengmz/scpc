package com.system.server;

import java.util.List;

import com.system.dao.WmDataDao;
import com.system.model.WmDataModel;
import com.system.vmodel.WmDataShowModel;

public class WmDataServer
{
	public List<WmDataShowModel> loadWmData(String startDate,String endDate,int spId,int cpId,String keyWord)
	{
		return new WmDataDao().loadWmData(startDate, endDate, spId, cpId, keyWord);
	}
	
	public List<WmDataModel> loadWmCpData(int userId,int troneId,String startDate,String endDate)
	{
		return new WmDataDao().loadWmCpData(userId, troneId, startDate, endDate);
	}
	
	public List<WmDataModel> loadWmCpDataDetail(int userId,int troneId,String startDate,String endDate)
	{
		return new WmDataDao().loadWmCpDataDetail(userId, troneId, startDate, endDate);
	}
	
	public List<WmDataModel> loadWmDataDetail(int troneOrderId,String startDate,String endDate)
	{
		return new WmDataDao().loadWmDataDetail(troneOrderId, startDate, endDate);
	}
	
	public static void main(String[] args)
	{
		List<WmDataShowModel> list = new WmDataServer().loadWmData("2016-12-01", "2017-05-01", -1, -1, "");
		for(WmDataShowModel model : list)
		{
			System.out.println(model.spId + "--" + model.spName + "--" + model.spRowSpand);
			
			for(WmDataShowModel.SpTroneModel spTroneModel : model.spTroneList)
			{
				System.out.println("\t" + spTroneModel.spTroneId + "--" + spTroneModel.spTroneName + "--" + spTroneModel.spTroneRowSpand);
				
				for(WmDataShowModel.SpTroneModel.TroneModel troneModel : spTroneModel.troneList)
				{
					System.out.println("\t\t" + troneModel.troneId + "--" + troneModel.troneName + "--" + troneModel.spPrice + "--" + troneModel.troneRowSpand);
					
					for(WmDataShowModel.SpTroneModel.TroneModel.TroneOrderModel troneOrderModel : troneModel.troneOrderList)
					{
						System.out.println("\t\t\t" + troneOrderModel.cpId + "--" + troneOrderModel.cpName + "--" + troneOrderModel.cpPrice + 
								"--" + troneOrderModel.dataRows + "--" + troneOrderModel.dataAmount + "--" + troneOrderModel.showDataRows + "--" + troneOrderModel.showDataAmount);
					}
				}
				
			}
		}
	}
}
