package com.system.server;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.system.cache.CacheConfigMgr;

public class TimerServer
{
	Logger log = Logger.getLogger(TimerServer.class);
	
	public void startRefreshCache()
	{
		Timer timer = new Timer();
		long periodTime = 5*60*1000;
		timer.schedule(new LoadDataTimerTask(), periodTime, periodTime);
		log.info("已经启动了定时刷新任务...");
	}
	
	private class LoadDataTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			CacheConfigMgr.refreshAllCache();
		}
	}
}
