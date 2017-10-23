package com.system.server;

import java.util.HashMap;
import java.util.Map;

import com.system.cache.BaseDataCache;
import com.system.cache.LocateCache;
import com.system.cache.SysConfigCache;
import com.system.constant.FlowConstant;
import com.system.constant.ResConstant;
import com.system.dao.SingleCpOrderDao;
import com.system.model.BasePriceModel;
import com.system.model.CpModel;
import com.system.model.CpRatioModel;
import com.system.model.CpTroneModel;
import com.system.model.PhoneLocateModel;
import com.system.model.RedisCpSingleOrderModel;
import com.system.model.SysCodeModel;
import com.system.model.TroneModel;
import com.system.util.Base64UTF;
import com.system.util.ServiceUtil;
import com.system.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 处理Channel充值请求
 * @author Andy.Chen
 *
 */
public class ChannelUserOrderServerV1
{
	public static Map<String, Object> handleUserOrder(String queryData,String ip)
	{
		Map<String, Object> response = new HashMap<String, Object>();
		
		String oriJsonData = Base64UTF.decode(queryData);
		
		return null;		
		
	}
	
	
}
