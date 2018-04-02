package com.system.server;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;

import com.system.dao.AlarmDao;
import com.system.util.ConfigManager;
import com.system.util.ServiceUtil;
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
	
	private static void analyAlarmData()
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
				ex.printStackTrace();
			}
		}
	}
	
	
	//如果报警时间超过一小时就发短信吧
	private static void preAlarm(int spTroneId,String lastDataTime,int difMinutes)
	{
		AlarmDao dao = new AlarmDao();
		
		Map<String, String> data = dao.loadSpTroneAlarmData(spTroneId);
		
		//这里为空只能说明着问题很严重
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
		
		long lastDataMils = 0;
				
		long curMils = System.currentTimeMillis();
		
		try
		{
			lastAlarmMils = sdf.parse(createDate).getTime();
			lastDataMils =  sdf.parse(lastDataTime).getTime();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return;
		}
		
		//如果最后一条报警时间大于最后一条数据的时间，也不报警
		if(lastAlarmMils>lastDataMils)
		{
			System.out.println("Alarm spTroneId[" + spTroneId + "] data had alarm");
			return;
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
			System.out.println("Alarm spTroneId[" + spTroneId + "] userName[" + userName + "]'s phone is empty");
			return;
		}
		
		String msg = "Hi," + userName + ",[" + spName + "]下[" + spTroneName + "]最后数据时间是[" + lastDataTime + "]请沟通";
		
		info.put("spTroneId", String.valueOf(spTroneId));
		info.put("userId", userId);
		info.put("phone", phone);
		info.put("msg", msg);
		
		dao.rcordAlarmData(info);
		
		sendMsg(msg,phone);
		
	}
	
	private static void sendMsg(String msg,String phoneNum)
	{
		String postData = "";
		
		String sendMsg = msg + "【浩天投资】";
		
		try	{sendMsg = URLEncoder.encode(sendMsg, "GBK");}catch(Exception ex){}
		
		postData += "UserName=" + ConfigManager.getConfigData("SmsUserName", "809841");
		postData += "&Password="  + ConfigManager.getConfigData("SmsPassword", "123456");
		postData += "&TimeStamp=";
		postData += "&MobileNumber=" + phoneNum;
		postData += "&MsgContent=" + sendMsg;
		postData += "&MsgIdentify=1999920" + (1000000 + new Random().nextInt(1000000));
		
		System.out.println(postData);
		
		System.out.println("result:" + ServiceUtil.sendGet(ConfigManager.getConfigData("SmsUrl", "http://119.145.253.67:8080/edeeserver/sendSMS.do"), null, postData));
	}
	
	public static void main(String[] args)
	{
		sendMsg("Hello,Seliner,[漫天游（魔趣）]下[移动MM-8个锤子]最后数据时间[2018-03-13 18:36:55.0]请沟通", "15919864976");
	}
	
}
