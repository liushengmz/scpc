package com.system.server;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.system.cache.CacheConfigMgr;
import com.system.cache.SyncCacheMgr;

public class TimerServer
{
	Logger log = Logger.getLogger(TimerServer.class);
	
	public void startRefreshCache()
	{
		startDBdataRefresh();
		startScanUnSyncData();
	}
	
	private void startDBdataRefresh()
	{
		Timer timer = new Timer();
		long periodTime = 5*60*1000;
		timer.schedule(new LoadDataTimerTask(), periodTime, periodTime);
		log.info("已经启动了定时刷新任务...");
	}
	
	private void startScanUnSyncData()
	{
		Timer timer = new Timer();
		long periodTime = 60000;
		timer.schedule(new SyncDataTimerTask(),periodTime,periodTime);
		log.info("已经启动了定时扫描同步的任务...");
	}
	
	private class LoadDataTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			CacheConfigMgr.refreshFrequenceCache();
		}
	}
	
	private class SyncDataTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			SyncCacheMgr.startScanUnSyncData();
		}
	}
}
