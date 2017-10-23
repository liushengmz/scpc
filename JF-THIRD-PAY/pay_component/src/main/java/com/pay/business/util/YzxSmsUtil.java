package com.pay.business.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pay.business.util.tfbpay.Base64;

public class YzxSmsUtil
{
	public static final int YZX_SEND_SMS_DATA_TYPE_CHANGE_SKEY = 1;
	public static final int YZX_SEND_SMS_DATA_TYPE_FORGET_PWD = 2;
	
	private static String sendMsg(String accountSid, String authToken,
			String appId, String templateId, String to, String params)
	{
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String signature = StringUtil.getMd5String(accountSid + authToken + timeStamp, 32);
		
		String url = "https://api.ucpaas.com/2014-06-30/Accounts/" + accountSid + "/Messages/templateSMS?sig=" + signature.toUpperCase();
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("appId", appId);
		data.put("templateId", templateId);
		data.put("to", to);
		data.put("param", params);
		
		String  body="{\"templateSMS\":"+StringUtil.getJsonFormObject(data)+"}";
		
		String src = accountSid + ":" + timeStamp;
		String auth = Base64.encode(src.getBytes());
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json;charset=utf-8");
		headers.put("Authorization", auth);
		
		return ServiceUtil.sendGet(url, headers, body);
	}
	
	private static String sendSmsData(String toPhone,String templateId,String params)
	{
		String accountSid="af582a20fc719da412ab2f5c492cd5e1";
		String authToken="bc71927fc5ae479b3b91d192ed5bad34";
		String appId="44aaffc75a654c75903a2b7fbc86b4f0";
		
		String result = sendMsg(accountSid, authToken, appId, templateId, toPhone, params);
		
		System.out.println("Send Sms: [" + toPhone + "] [" + templateId + "] [" + params + "] result : " + result);
		
		return result;
	}
	
	public static String sendForgetPwdSmsData(String toPhone,String params,int type)
	{
		String templateId= "162469";
		
		if(type==YZX_SEND_SMS_DATA_TYPE_CHANGE_SKEY)
		{
			templateId = "162468";
		}
		
		return  sendSmsData(toPhone,templateId,params);
	}
	
	public static void main(String[] args)
	{
		
		String templateId="162468";
		String to="15919864976";
		String params="88888";
		
		System.out.println(sendSmsData(to,templateId, params));
	}
}
