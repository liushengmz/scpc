package com.system.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.model.CpModel;
import com.system.util.StringUtil;

public class JsonTest
{
	public static void main(String[] args)
	{
		List<CpModel> list = new ArrayList<CpModel>();
		
		for(int i=0; i<3; i++)
		{
			CpModel model = new CpModel();
			model.setCpId(123 + i);
			model.setFullName("ShitName" + i);
			model.setSignKey("fdwqewq" + i);
			list.add(model);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("status", 200);
		map.put("msg", "success");
		map.put("data", list);
		
		System.out.println(StringUtil.getJsonFormObject(map));
	}
}
