
package com.system.server;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.system.server.ad.AdServer;
import com.system.server.analy.AnalyMrDailyServer;
import com.system.server.xy.FeeServer;
import com.system.server.xy.UserServer;
import com.system.util.ConfigManager;

public class TimerServer
{
	Logger log = Logger.getLogger(TimerServer.class);

	// 一个不同时段的定时任务就起一个TIMMER
	public void startTimer()
	{
		ConfigManager.setConfigFilePath("");
		
		startAnalyDataTimer();
		
		startWatchDataTimer();
		
		//startUpdateDataTimer();
		//startXyQdAnalyDataTimer();
	}
	
	private void startWatchDataTimer()
	{

		log.info("已启动数据监控报警...");
		Timer timer = new Timer();
		timer.schedule(new  AlarmDataTimerTask(), 300000, 300000);
	}
	

	// 更新推送数据在中午12点
	@SuppressWarnings("unused")
	private void startUpdateDataTimer()
	{
		Calendar ca = Calendar.getInstance();

		int curHour = ca.get(Calendar.HOUR_OF_DAY);

		// 今天分析昨天的数据
		if (curHour >= 12)
			ca.add(Calendar.DAY_OF_MONTH, 1);

		ca.set(Calendar.HOUR_OF_DAY, 12);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);

		// ca.set(Calendar.HOUR_OF_DAY, 19);
		// ca.set(Calendar.MINUTE, 1);
		// ca.set(Calendar.SECOND, 0);

		long firstTime = ca.getTimeInMillis();

		// 一天
		long periodTime = 86400000;

		// long periodTime = 120000;

		long delayMils = firstTime - System.currentTimeMillis();

		log.info("更新数据离第一次启动还有:" + delayMils);
		log.info("周期时间是:" + periodTime);
		Timer timer = new Timer();
		timer.schedule(new AnalyDataTimerTask2(), delayMils, periodTime);
		log.info("已经启动了定时任务...");
	}

	// 分析数据在早上一点进行分析
	private void startAnalyDataTimer()
	{
		Calendar ca = Calendar.getInstance();

		int curHour = ca.get(Calendar.HOUR_OF_DAY);

		// 今天分析昨天的数据
		if (curHour >= 1)
			ca.add(Calendar.DAY_OF_MONTH, 1);

		ca.set(Calendar.HOUR_OF_DAY, 1);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);

		// ca.set(Calendar.HOUR_OF_DAY, 23);
		// ca.set(Calendar.MINUTE, 14);
		// ca.set(Calendar.SECOND, 0);

		long firstTime = ca.getTimeInMillis();

		// 一天
		long periodTime = 86400000;

		// long periodTime = 120000;

		long delayMils = firstTime - System.currentTimeMillis();

		log.info("分析数据离第一次启动还有:" + delayMils);
		log.info("周期时间是:" + periodTime);
		Timer timer = new Timer();
		timer.schedule(new AnalyDataTimerTask(), delayMils, periodTime);
		log.info("已经启动了定时任务...");

	}

	// 每隔一小时更新一下翔云渠道数据
	@SuppressWarnings("unused")
	private void startXyQdAnalyDataTimer()
	{
		Calendar ca = Calendar.getInstance();

		int curMins = ca.get(Calendar.MINUTE);

		if (curMins >= 5)
		{
			ca.add(Calendar.HOUR_OF_DAY, 1);
		}

		ca.set(Calendar.MINUTE, 5);

		// ca.set(Calendar.HOUR_OF_DAY, 21);
		// ca.set(Calendar.MINUTE, 15);
		// ca.set(Calendar.SECOND, 0);

		long firstTime = ca.getTimeInMillis();

		// 一小时
		long periodTime = 3600000;

		// long periodTime = 120000;

		long delayMils = firstTime - System.currentTimeMillis();

		log.info("分析翔云渠道数据离第一次启动还有:" + delayMils);
		log.info("周期时间是:" + periodTime);
		Timer timer = new Timer();
		timer.schedule(new AnalyDataTimerTask3(), delayMils, periodTime);
		log.info("已经启动了定时任务...");

	}

	public static void main(String[] args)
	{
		//new TimerServer().startXyQdAnalyDataTimer();
		
		new TimerServer().startTimer();
	}

	//零晨一点分析各种数据到数据汇总表
	private class AnalyDataTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			new UserServer().startAnalyUser();
			new AnalyMrDailyServer().startAnalyDailyMr();
			new FeeServer().startAnalyFee();
		}
	}
	
	//每隔5分钟扫描一次需要监控的数据
	private class AlarmDataTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			AlarmServer.startAnalyAlarmData();
		}
	}

	// 中午12点把翔云渠道的数据更新出去，让渠道能看得到，也就是更新各种表的状态
	private class AnalyDataTimerTask2 extends TimerTask
	{
		@Override
		public void run()
		{
			new UserServer().updateQdUserData();
			new FeeServer().updateQdFeeSummer();
			new AdServer().updateApChannelSummer();
		}
	}

	//每隔一小时更新一下翔云渠道数据
	private class AnalyDataTimerTask3 extends TimerTask
	{
		@Override
		public void run()
		{
			new UserServer().analyQdShowDataWithHour();
		}
	}
}
