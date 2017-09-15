package com.system.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.system.server.SyncToCpServer;
import com.system.util.StringUtil;

public class SyncCacheMgr
{
	private static Logger logger = Logger.getLogger(SyncCacheMgr.class);
	
	//服务器启动的时候就不再加载进来处理，只能手动同步 long[] 0 syncTimes 1 lastSyncMils 
	private static Map<String, long[]> map = new HashMap<String, long[]>(); 
	
	/**
	 * 检查需要同步的数据，重新同步给下游
	 */
	public static void startScanUnSyncData()
	{
		for(String key : map.keySet())
		{
			long[] data = map.get(key);
			int syncTimes = (int)data[0];
			long lastSyncMils = data[1];
			long curMils = System.currentTimeMillis();
			int nextSyncMils = StringUtil.getInteger(SysConfigCache.getConfigFromSys(1, "NOTIFY_" + syncTimes + "_MILS"),0);
			if(lastSyncMils + nextSyncMils >= curMils)
			{
				SyncToCpServer server = new SyncToCpServer();
				server.setKey(key);
				new Thread(server).start();
			}
		}
		logger.info("Scan Un Sync Data Finish ...");
	}
	
	public static void addSyncData(String key,long[] data)
	{
		map.put(key, data);
	}
	
	public static void removeKey(String key)
	{
		map.remove(key);
	}
	
}	
