package com.system.server;

import java.util.List;

import com.system.cache.SpDataCache;
import com.system.dao.MrDao;
import com.system.model.MrModel;
import com.system.model.TroneModel;

public class MrServer
{
	public float[] loadUserDayMonthLimit(String userMd5,String month,String curDate,int spTroneId)
	{
		List<MrModel> list = new MrDao().loadUserDayMonthLimit(userMd5,month,spTroneId);
		
		if(list==null || list.isEmpty())
			return null;
		
		float spTroneDaylimit = 0;
		float spTroneMonthLimit = 0;
		
		for(MrModel model : list)
		{
			TroneModel troneModel = SpDataCache.getTroneById(model.getTroneId());
			if(troneModel!=null)
			{
				if(curDate.equalsIgnoreCase(model.getMrDate()))
				{
					spTroneDaylimit += troneModel.getPrice();
				}
				spTroneMonthLimit += troneModel.getPrice();
			}
		}
		
		float[] limit = {spTroneDaylimit , spTroneMonthLimit};
		return limit;
	}
}
