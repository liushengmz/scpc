package com.system.test;

import com.system.cache.CacheConfigMgr;
import com.system.model.ApiOrderModel;
import com.system.server.RequestServerV1;

public class BaseTest 
{
	public static void main(String[] args)
	{
		ApiOrderModel model = new ApiOrderModel();
		model.setApiExdata("");
		model.setTroneOrderId(1616);
		model.setCid(1);
		model.setClientIp("201.161.24.82");
		model.setCpVerifyCode("");
		model.setExtrData("123456789");
		model.setId(1);
		model.setImei("865372028198676");
		model.setImsi("460029198599437");
		model.setIp("192.168.1.2");
		//model.setIsHidden(1);
		model.setLac(123);
		model.setMobile("15919864967");
		model.setMsg("");
		model.setNetType("");
		model.setPackageName("com.system.test");
		model.setPort("");
		model.setSdkVersion("19");
		model.setSpExField("");
		model.setSpLinkId("");
		model.setStatus(1);
		model.setExtraParams("NI SHOU NI");
		
		
		CacheConfigMgr.init();
		
		System.out.println(new RequestServerV1().handlCpQuery(model));
		
	}
}
