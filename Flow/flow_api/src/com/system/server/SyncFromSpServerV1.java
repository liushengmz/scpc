package com.system.server;

import java.util.Map;

import com.system.cache.BaseDataCache;
import com.system.dao.BaseDataDao;
import com.system.dao.CpDataDao;
import com.system.model.RedisCpSingleOrderModel;
import com.system.util.StringUtil;

public class SyncFromSpServerV1
{
	public static String handleSyncFromSp(String key)
	{
		String result = "ERROR";
		
		if(StringUtil.isNullOrEmpty(key))
			return result;
		
		Map<String, String> map = RedisServer.getSingleCpOrder(key);
		
		if(map==null || map.keySet().size()<=0)
			return result;
		
		//根据SP那返回的结果处理就行了
		int status = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_STATUS), -1);
		
		//充值成功，SP和CP的余额都得相应减去
		if(status==2)
		{
			int spId = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_SP_ID), -1);
			int cpId = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_CP_ID), -1);
			int spRatio = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_SP_RATIO), 0);
			int cpRatio = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_CP_RATIO), 0);
			int price = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_PRICE), 0);
			int spCurrency = BaseDataCache.getSpCurrency(spId);
			int cpCurrency = BaseDataCache.getSpCurrency(cpId);
			int spPrice = price*spRatio/1000;
			int cpPrice = price*cpRatio/1000;
			new CpDataDao().updateCpCurrency(cpId, cpCurrency-cpPrice);
			new BaseDataDao().updateSpCurrency(spId, spCurrency-spPrice);
			BaseDataCache.updateCpCurrency(cpId, cpPrice, true);
			BaseDataCache.updateSpCurrency(spId, spPrice, true);
		}
		//充值失败(好像没什么好处理的)
		else if(status==-1)
		{
			return result;
		}
		
		//最后通知下游就行了
		SyncToCpServer syncToCp = new SyncToCpServer();
		syncToCp.setKey(key);
		new Thread(syncToCp).start();
		
		return "OK";
	}
}
