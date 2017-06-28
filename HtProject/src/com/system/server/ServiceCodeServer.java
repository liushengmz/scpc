package com.system.server;

import java.util.ArrayList;
import java.util.List;

import com.system.dao.ServiceCodeDao;
import com.system.model.ServiceCodeModel;

public class ServiceCodeServer
{
	public List<List<ServiceCodeModel>> loadServiceCode()
	{
		List<ServiceCodeModel> list = new ServiceCodeDao().loadServiceCode();
		
		List<List<ServiceCodeModel>> result = new ArrayList<List<ServiceCodeModel>>();
		
		List<ServiceCodeModel> list1 =new ArrayList<ServiceCodeModel>();
		List<ServiceCodeModel> list2 =new ArrayList<ServiceCodeModel>();
		List<ServiceCodeModel> list3 =new ArrayList<ServiceCodeModel>();
		List<ServiceCodeModel> list4 =new ArrayList<ServiceCodeModel>();
		
		result.add(list1);
		result.add(list2);
		result.add(list3);
		result.add(list4);
		
		for(ServiceCodeModel model : list)
		{
			if(model.getOperatorBjFlag()==1)
				list1.add(model);
			else if(model.getOperatorBjFlag()==2)
				list2.add(model);
			else if(model.getOperatorBjFlag()==3)
				list3.add(model);	
			else if(model.getOperatorBjFlag()==5)
				list4.add(model);
		}
		
		return result;
	}
}
