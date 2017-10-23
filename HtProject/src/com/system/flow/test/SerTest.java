package com.system.flow.test;

import com.system.flow.model.CpModel;
import com.system.util.StringUtil;


public class SerTest
{
	public static void main(String[] args)
	{
		CpModel model = new CpModel();
		model.setId(1);
		model.setFullName("死SB");
		model.setShortName("SB");
		
		System.out.println(StringUtil.getJsonFormObject(model));
	}
	
}
