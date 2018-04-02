package com.system.server;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.system.dao.AlarmDao;
import com.system.util.StringUtil;

public class AlarmServer
{
	private static boolean isAnalizingData = false;
	
	public static void startAnalyAlarmData()
	{
		if(isAnalizingData)
		{
			System.out.println("正在分析数据，下次再来吧");
			return;
		}
		
		isAnalizingData = true;
		
		long curMils = System.currentTimeMillis();
		
		analyAlarmData();
		
		System.out.println("Analy Data SpendMils:" + (System.currentTimeMillis() - curMils));
		
		isAnalizingData = false;
	} 
	
	public static void analyAlarmData()
	{
		String curMonth = StringUtil.getMonthFormat();
		
		AlarmDao dao = new AlarmDao();
		
		Map<Integer, String> map = dao.loadTroneIdLastTime(curMonth);
		
		long curMils = System.currentTimeMillis();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(int key : map.keySet())
		{
			try
			{
				long lastMils = sdf.parse(map.get(key)).getTime();
				
				long diffMils = curMils - lastMils;
				
				if(diffMils>10000 && diffMils/1000/60 > 30)
				{
					preAlarm(key,map.get(key),(int)diffMils/1000/60);
				}
			}
			catch(Exception ex)
			{
				
			}
		}
	}
	
	
	//如果报警时间超过一小时就发短信吧
	private static void preAlarm(int spTroneId,String lastDataTime,int difMinutes)
	{
		AlarmDao dao = new AlarmDao();
		
		Map<String, String> data = dao.loadSpTroneAlarmData(spTroneId);
		
		if(data==null)
			return;
		
		String createDate = data.get("createDate");
		
		//如果时间为空，直接发报警信息吧
		if(StringUtil.isNullOrEmpty(createDate))
		{
			alarm(spTroneId,lastDataTime,difMinutes);
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		long lastAlarmMils = 0;
				
		long curMils = System.currentTimeMillis();
		
		try
		{
			lastAlarmMils = sdf.parse(createDate).getTime();
		}
		catch(Exception ex)
		{
			
		}
		
		long diffMils = curMils - lastAlarmMils;
		
		if(diffMils>10000 && diffMils/1000/60 > 60)
		{
			alarm(spTroneId,lastDataTime,difMinutes);
		}
		
	}
	
	
	private static void alarm(int spTroneId,String lastDataTime,int difMinutes)
	{
		AlarmDao dao = new AlarmDao();
		
		Map<String, String> info = dao.loadAlarmInfo(spTroneId);
		
		if(info==null)
			return;
		
		String userName = info.get("userName");
		String spName = info.get("spName");
		String spTroneName = info.get("spTroneName");
		String phone = info.get("phone");
		String userId = info.get("userId");
		
		if(StringUtil.isNullOrEmpty(phone))
		{
			phone = "13800138000";
		}
		
		if(StringUtil.isNullOrEmpty(phone))
		{
			System.out.println("Alarm spTroneId[" + spTroneId + "] userName[" + userName + "]'s phone is empty");
			return;
		}
		
		String msg = "Hello," + userName + ",SP公司[" + spName + "]下的[" + spTroneName + "]业务，最后一条数据时间是[" + lastDataTime + "]，[" + difMinutes + "]分钟以前，请沟通数据情况。";
		
		info.put("spTroneId", String.valueOf(spTroneId));
		info.put("userId", userId);
		info.put("phone", phone);
		info.put("msg", msg);
		
		dao.rcordAlarmData(info);
		
	}
	
	public static void main(String[] args)
	{
		//startAnalyAlarmData();
		sendMsg();
	}
	
	private static void sendMsg()
	{
		String postData = "";
		postData += "UserName=809841";
		postData += "&Password=123456";
		postData += "&TimeStamp=";
		postData += "&MobileNumber=15919864976";
		postData += "&MsgContent=Hello,Andy,公司[中集]下的[浦发业务]业务，最后一条数据时间是[2018-03-09 11:12:58.0]，[4362]分钟以前，请沟通数据情况。【浩天投资】";
		postData += "&MsgIdentify=1999920";
		System.out.println(postData);
	}
}
