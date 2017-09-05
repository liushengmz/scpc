package com.system.test;

import java.util.ArrayList;
import java.util.List;

import com.system.cache.CacheConfigMgr;
import com.system.model.ToBusinessChildOrderModel;
import com.system.model.ToBusinessOrderModel;
import com.system.server.ToBusinessRequestServerV1;
import com.system.util.Base64UTF;
import com.system.util.RedisUtil;
import com.system.util.StringUtil;

public class ToBusinessRequestTest
{
	private static int cpId = 80001;
	private static String signKey = "*B92E689A034E998DBE0D7367CB62A9496EDC7237";
	private static String orderId = "7367CB62A9496EDC7237";
	
	public static String getBusinessQueryString()
	{
		ToBusinessOrderModel model = new ToBusinessOrderModel();
		model.setCpId(cpId);
		model.setOrderId(orderId);
		model.setSign(StringUtil.getMd5String(model.getCpId() + model.getOrderId() + signKey,32));
		
		List<ToBusinessChildOrderModel> childList = new ArrayList<ToBusinessChildOrderModel>();
		
		for(int i=0; i<4; i++)
		{
			ToBusinessChildOrderModel child = new ToBusinessChildOrderModel();
			child.setFlowSize(100*(i+1));
			child.setMobile("1380013800" + i);
			child.setRang(i);
			child.setSubOrderId(System.currentTimeMillis() + i + "");
			childList.add(child);
		}
		
		model.setOrderList(childList);
		String queryData = StringUtil.getJsonFormObject(model);
		return Base64UTF.encode(queryData);
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		RedisUtil.init();
		CacheConfigMgr.init();
		String queryData =  getBusinessQueryString();
		System.out.println(StringUtil.getJsonFormObject(ToBusinessRequestServerV1
						.handleBusinessRequest(queryData, "192.168.1.11")));
	}
}
