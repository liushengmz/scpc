package com.system.flow.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.system.flow.dao.SpApiDao;
import com.system.flow.model.SpApiModel;
import com.system.util.StringUtil;

public class SpApiServer
{
	public List<SpApiModel> loadSpApiBySpId(int spId)
	{
		return new SpApiDao().loadSpApiBySpId(spId);
	}
	
	public String loadSpApiJsonBySpId(int spId)
	{
		List<SpApiModel> list = loadSpApiBySpId(spId);
		
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		
		for(SpApiModel model : list)
		{
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", model.getId());
			map.put("name", model.getName());
			listMap.add(map);
		}
		
		return StringUtil.getJsonArrayFromObject(listMap);
	}
	
}
