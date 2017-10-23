package com.system.server;

import java.util.Map;

import com.system.cache.BaseDataCache;
import com.system.dao.BaseDataDao;
import com.system.dao.CpDataDao;
import com.system.dao.SingleCpOrderDao;
import com.system.model.RedisCpSingleOrderModel;
import com.system.util.StringUtil;

public class SyncFromSpServerV1
{
	public static String handleSyncFromSp(final String key)
	{
		String result = "ERROR";
		
		if(StringUtil.isNullOrEmpty(key))
			return result;
		
		final Map<String, String> map = RedisServer.getSingleCpOrder(key);
		
		if(map==null || map.keySet().size()<=0)
			return result;
		
		int recallStatus =  StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_RECALL_STATUS), 0);
		
		if(recallStatus>0)
			return "OK";
		
		//根据SP那返回的结果处理就行了
		int status = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_STATUS), -1);
		
		final int tempTableId = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_TEMP_TABLE_ID), -1);
		final String tableName = map.get(RedisCpSingleOrderModel.MAP_KEY_MONTH_NAME);
		final int monthTableId = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_MONTH_TABLE_ID), -1);
		
		//更新缓存、数据库回调状态
		map.put(RedisCpSingleOrderModel.MAP_KEY_RECALL_STATUS, "1");
		RedisServer.updateRedisData(1, key, map);
		SingleCpOrderDao dao = new SingleCpOrderDao();
		dao.updateSingleCpOrderReCallStautusInTempTable(tempTableId, 1);
		dao.updateSingleCpOrderReCallStautusInMonthTable(tableName, monthTableId, 1);
		
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
			
			int sendSms = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_SEND_SMS), 0);
			
			//发送短信
			if(sendSms==1)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						String phoneNum = StringUtil.getString(map.get(RedisCpSingleOrderModel.MAP_KEY_MOBILE), "");
						int flowSize = StringUtil.getInteger(map.get(RedisCpSingleOrderModel.MAP_KEY_FLOW_SIZE), 0);
						
						boolean sendSmsSuc = SendSmsServer.aliSendSms(phoneNum, flowSize + "M");
						
						//更新缓存、数据库的短信发送状态
						map.put(RedisCpSingleOrderModel.MAP_KEY_SEND_SMS_STATUS, sendSmsSuc ? "1" : "2");
						RedisServer.updateRedisData(1, key, map);
						SingleCpOrderDao dao = new SingleCpOrderDao();
						dao.updateSingleCpOrderSendSmsStautusInMonthTable(tableName, monthTableId, sendSmsSuc ? 1 : 2);
						dao.updateSingleCpOrderSendSmsStautusInTempTable(tempTableId, sendSmsSuc ? 1 : 2);
					}
				}).start();
			}
		}
				
		//最后通知下游就行了
		SyncToCpServer syncToCp = new SyncToCpServer();
		syncToCp.setKey(key);
		new Thread(syncToCp).start();
		
		return "OK";
	}
}
