package com.system.server;

import java.util.ArrayList;
import java.util.List;

import com.system.dao.ProvinceDao;
import com.system.model.ProvinceModel;

public class ProvinceServer
{
	public List<ProvinceModel> loadProvince()
	{
		return new ProvinceDao().loadProvinceList();
	}
	
	public List<String> loadNamesByIds(List<Integer> list)
	{
		String ids = "";
		
		for(int id : list)
		{
			ids += id + ",";
		}
		
		ids = ids.substring(0, ids.length()-1);
		
		List<ProvinceModel> proList = new ProvinceDao().loadProvinceList(ids);
		
		List<String> nameList = new ArrayList<String>();
		
		for(ProvinceModel model : proList)
		{
			nameList.add(model.getName());
		}
		
		return nameList;
	}
}
