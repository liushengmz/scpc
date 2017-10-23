package com.system.test;

import com.system.cache.CacheConfigMgr;
import com.system.server.SyncFromSpServerV1;
import com.system.server.TimerServer;
import com.system.util.RedisUtil;

public class SyncToCpServerTest
{
	public static void main(String[] args) throws InterruptedException
	{
		RedisUtil.init();
		Thread.sleep(1000);
		CacheConfigMgr.init();
		String key = "F9F0B22A896107B7";
		System.out.println(SyncFromSpServerV1.handleSyncFromSp(key));
		new TimerServer().startRefreshCache();
	}
}
