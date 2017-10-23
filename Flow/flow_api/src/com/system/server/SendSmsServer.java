package com.system.server;

import org.apache.log4j.Logger;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.system.cache.SysConfigCache;
import com.system.util.StringUtil;

public class SendSmsServer
{
	private static Logger logger = Logger.getLogger(SendSmsServer.class);
	
	public static boolean aliSendSms(String phoneNum,String data)
	{
		String keyId = SysConfigCache.getConfigFromSys(3, "SEND_SMS_ALI_KEY_ID");
		String keySecret = SysConfigCache.getConfigFromSys(3, "SEND_SMS_ALI_KEY_SECRET");
		String tempCode = SysConfigCache.getConfigFromSys(3, "SEND_SMS_ALI_TEMP_CODE");
		String signName = SysConfigCache.getConfigFromSys(3, "SEND_SMS_ALI_SIGN_NAME");
		
		//产品名称:云通信短信API产品,开发者无需替换
		String product = "Dysmsapi";
		//产品域名,开发者无需替换
		String domain = "dysmsapi.aliyuncs.com";
		
		if(StringUtil.isNullOrEmpty(keyId)
				||StringUtil.isNullOrEmpty(keySecret)
				||StringUtil.isNullOrEmpty(tempCode)
				||StringUtil.isNullOrEmpty(signName))
			return false;
		
		try
		{
			//可自助调整超时时间
	        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
	        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

	        //初始化acsClient,暂不支持region化
	        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", keyId, keySecret);
	        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
	        IAcsClient acsClient = new DefaultAcsClient(profile);

	        //组装请求对象-具体描述见控制台-文档部分内容
	        SendSmsRequest request = new SendSmsRequest();
	        //必填:待发送手机号
	        request.setPhoneNumbers(phoneNum);
	        //必填:短信签名-可在短信控制台中找到
	        request.setSignName(signName);
	        //必填:短信模板-可在短信控制台中找到
	        request.setTemplateCode(tempCode);
	        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
	        request.setTemplateParam("{\"size\":\"" + data + "\"}");

	        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
	        //request.setSmsUpExtendCode("90997");

	        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
	       // request.setOutId("119988");

	        //hint 此处可能会抛出异常，注意catch
	        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
	        
	        logger.info("PhoneNum: " + phoneNum + " FlowSise: " + data + " SendSms Finish");
	        
	        if(sendSmsResponse!=null && "OK".equalsIgnoreCase(sendSmsResponse.getCode()))
	        	return true;
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
		}
		
		return false;
	}
	
}
