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
		RedisUtil.init();
		Thread.sleep(1000);
		CacheConfigMgr.init();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cpId", cpId);
		map.put("orderId", System.currentTimeMillis() + "");
		map.put("sign",StringUtil.getMd5String(cpId + map.get("orderId").toString() + key, 32));
		map.put("mobile", "13436104976");
		map.put("rang", 0);
		map.put("flowSize", 500);
		map.put("timeType", 0);
		
		String json = StringUtil.getJsonFormObject(map);
		String encodeData = Base64UTF.encode(json);
		System.out.println(SingleUserOrderServerV1.handleUserOrder(encodeData, "127.0.0.1"));
		
	}
}
