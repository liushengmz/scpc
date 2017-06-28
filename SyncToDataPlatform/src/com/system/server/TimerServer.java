
package com.system.server;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.system.util.ConfigManager;

public class TimerServer
{
	Logger log = Logger.getLogger(TimerServer.class);

	public void startTimer()
	{
		startAnalyData();
	}

	//每次启去分析数据，每隔三小时扫描一次数据变化
	private void startAnalyData()
	{
		//3小时
		long periodTime = 3600000*3;
		Timer timer = new Timer();
		timer.schedule(new AnalyDataTimerTask1(), 0, periodTime);
		log.info("每隔三个小时就会分析一次数据 ");
	}
	
	public static void analyData()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				long  curMils = System.currentTimeMillis();
				System.out.println("开始分析数据...");
				new ComSumAnalyServer().startAnalyComSumData();
				new BaseDataAnalyServer().startAnalyBaseData();
				System.out.println("结束分析数据，共耗时：" + (System.currentTimeMillis() - curMils));
			}
		}).start();
	}
	
	public static void main(String[] args)
	{
		ConfigManager.setConfigFilePath("");
		
		new TimerServer().startTimer();
	}

	//每隔三小时同步一次三个公司的基础数据和报表数据到大平台中心
	private class AnalyDataTimerTask1 extends TimerTask
	{
		@Override
		public void run()
		{
			analyData();
		}
	}
	
}
