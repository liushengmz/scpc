package com.system.server;

import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.system.cache.BaseDataCache;
import com.system.cache.SyncCacheMgr;
import com.system.cache.SysConfigCache;
import com.system.constant.FlowConstant;
import com.system.dao.SingleCpOrderDao;
import com.system.model.CpModel;
import com.system.model.RedisCpSingleOrderModel;
import com.system.model.SysCodeModel;
import com.system.util.ServiceUtil;
import com.system.util.StringUtil;

public class SyncToCpServer implements Runnable
{
	
	private Logger logger = Logger.getLogger(SyncToCpServer.class);
	
	private String _key = null;
	
	private Map<String, String> _map = null;

	public void setKey(String key)
	{
		_key = key;
	}
	
	@Override
	public void run()
	{
		if(StringUtil.isNullOrEmpty(_key))
		{
			return;
		}
		
		_map = RedisServer.getSingleCpOrder(_key);
		
		if(_map==null)
			return;
		
		int notifyStatus = StringUtil.getInteger(_map.get(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_STATUS),2);
		
		if(notifyStatus==1)
			return;
		
		long curMils = System.currentTimeMillis();
		
		boolean isSyncSuc  = syncToClient();
		
		int syncTimes = StringUtil.getInteger(_map.get(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_TIMES), 0);
		int cpId = StringUtil.getInteger(_map.get(RedisCpSingleOrderModel.MAP_KEY_CP_ID), 0);
		
		syncTimes++;
		
		_map.put(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_STATUS, isSyncSuc ?  "1" : "2");
		_map.put(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_TIMES,"" + syncTimes);
		_map.put(RedisCpSingleOrderModel.MAP_KEY_LAST_NOTIFY_MILS,"" + curMils);
		
		if(!isSyncSuc)//失败则记录KEY到内存缓存
		{
			long[] data = {syncTimes,curMils};
			SyncCacheMgr.addSyncData(_key, data);
			
			if(syncTimes>=5)
			{
				SyncCacheMgr.removeKey(_key);
			}
		}
		else //成功则把KEY从缓存中剔除
		{
			if(syncTimes>1)
			{
				SyncCacheMgr.removeKey(_key);
			}
		}
		
		SingleCpOrderDao dao = new SingleCpOrderDao();
		
		int tempTableId = StringUtil.getInteger(_map.get(RedisCpSingleOrderModel.MAP_KEY_TEMP_TABLE_ID), -1);
		
		String tableName = _map.get(RedisCpSingleOrderModel.MAP_KEY_MONTH_NAME);
		
		int monthTableId = StringUtil.getInteger(_map.get(RedisCpSingleOrderModel.MAP_KEY_MONTH_TABLE_ID), -1);
		
		dao.updateSingleCpOrderSyncStatusInTempTable(tempTableId, isSyncSuc ? 1 : 2, curMils,syncTimes);
		
		dao.updateSingleCpOrderSyncStatusInMonthTable(tableName, monthTableId, isSyncSuc ? 1 : 2, curMils,syncTimes);
		
		RedisServer.updateRedisData(1, _key, _map);
		
		logger.info("Sync To Cp[" + cpId + "] " + (isSyncSuc ? "SUCCESS" : "FAIL"));
	}
	
	private boolean syncToClient()
	{
		String postData = getPostJson();
		
		System.out.println(postData);
		
		String url = _map.get(RedisCpSingleOrderModel.MAP_KEY_NOTIFY_URL);
		
		if(StringUtil.isNullOrEmpty(url))
			return false;
		
		String result = ServiceUtil.sendGet(url, null, postData);
		
		if("success".equalsIgnoreCase(result))
			return true;
		
		return false;
	}
	
	private String getPostJson()
	{
		try
		{
			JsonObject jo = new JsonObject();
			int cpId = StringUtil.getInteger(_map.get(RedisCpSingleOrderModel.MAP_KEY_CP_ID),0);
			CpModel cpModel = BaseDataCache.loadCpById(cpId);
			String orderId = _map.get(RedisCpSingleOrderModel.MAP_KEY_CLIENT_ORDER_ID);
			String sign = StringUtil.getMd5String((FlowConstant.FLOW_SYS_CP_CODE_BASE_COUNT + cpId) + orderId + cpModel.getSignKey(), 32);
			jo.addProperty("cpId", FlowConstant.FLOW_SYS_CP_CODE_BASE_COUNT + cpId);
			jo.addProperty("orderId", orderId);
			jo.addProperty("sign", sign);
			int status = StringUtil.getInteger(_map.get(RedisCpSingleOrderModel.MAP_KEY_STATUS),0);
			jo.addProperty("resultCode", status==2 ? 12000 : status);
			SysCodeModel codeModel = SysConfigCache.loadResultCodeByFlag(StringUtil.getInteger(_map.get(RedisCpSingleOrderModel.MAP_KEY_STATUS), 0));
			jo.addProperty("resultMsg", status == 2 ? "充值成功" : (codeModel==null ? "充值失败" : codeModel.getCodeName()));
			return jo.toString();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return "";
	}
	
	
	
	
}
