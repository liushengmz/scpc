package com.system.sdk.server;
import java.util.Map;
import com.system.sdk.dao.SdkDataSummerDao;
public class SdkDataSummerServer
{
	public Map<String, Object>loadSdkDataSummerModel(int cpId,int channelId,int appId,int troneId,int spTroneId,String startDate,String endDate,int showType,int provinceId){
		return new SdkDataSummerDao().loadSdkDataSummer(cpId, channelId, appId, troneId, spTroneId, startDate, endDate, showType,provinceId);
	}
	
}
