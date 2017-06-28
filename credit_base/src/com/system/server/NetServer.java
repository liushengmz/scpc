package com.system.server;

import com.system.constant.Constant;
import com.system.model.ApiOrderModel;
import com.system.util.ServiceUtil;
import com.system.util.StringUtil;

public class NetServer
{
	public static String sendBaseApiOrder(ApiOrderModel model)
	{
		return ServiceUtil.sendGet(String.format(Constant.BASE_TRONE_URL, model.getApiOrderId()), null, StringUtil.getJsonFormObject(model));
	}
	
	public static String sendNextApiOrder(ApiOrderModel model)
	{
		return ServiceUtil.sendGet(String.format(Constant.NEXT_TRONE_URL, model.getApiOrderId()), null, StringUtil.getJsonFormObject(model));
	}
}
