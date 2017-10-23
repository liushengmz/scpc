package com.system.test;

import java.util.HashMap;
import java.util.Map;

import com.system.cache.CacheConfigMgr;
import com.system.constant.FlowConstant;
import com.system.server.SingleUserOrderServerV1;
import com.system.util.Base64UTF;
import com.system.util.RedisUtil;
import com.system.util.StringUtil;

public class SingleUserOrderTest
{
	private static int cpId = FlowConstant.FLOW_SYS_CP_CODE_BASE_COUNT + 1;
	private static String key = "*B92E689A034E998DBE0D7367CB62A9496EDC7237";
	
	
	public static void main(String[] args) throws InterruptedException
	{
		//ss();
		String userOrderData = genUserOrderData();
		
		System.out.println("POST DATA:" + userOrderData);
		
		String returnData = testHandleUserOrder(userOrderData, "127.0.0.1");
		
		System.out.println("RETURN DATA:" + returnData);
		
		System.out.println(Base64UTF.decode(returnData));
	}
	
	public static String genUserOrderData()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cpId", cpId);
		map.put("orderId", System.currentTimeMillis() + "");
		map.put("sign",StringUtil.getMd5String(cpId + map.get("orderId").toString() + key, 32));
		map.put("mobile", "17788773835");
		map.put("rang", 0);
		map.put("flowSize", 10);
		map.put("timeType", 0);
		map.put("notifyUrl", "http://tp-core.n8wan.com/sc.html");
		String json = StringUtil.getJsonFormObject(map);
		System.out.println(json);
		return Base64UTF.encode(json);
	}
	
	public static String testHandleUserOrder(String postData,String ip)
	{
		RedisUtil.init();
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		CacheConfigMgr.init();
		
		return SingleUserOrderServerV1.handleUserOrder(postData, "127.0.0.1").toString();
	}
	
	public static void ss()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cpId", 80001);
		map.put("resultMsg", "充值成功");
		map.put("resultCode", 12000);
		map.put("orderId", "1505895085525");
		map.put("sign", "8834586dfbb1e23b3487cc8ecda73747");
		String json = StringUtil.getJsonFormObject(map);
		System.out.println(json);
		System.out.println(Base64UTF.encode(json));
	}
}
