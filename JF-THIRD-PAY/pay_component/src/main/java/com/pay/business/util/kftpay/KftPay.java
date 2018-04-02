package com.pay.business.util.kftpay;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pay.business.util.tcpay.util.JsonUtil;

public class KftPay
{
	public static Map<String, String> queryOrder(String merchantId, String secMerchantId,
			int payType, String orderNo, String price,String product,String ip, String returnUrl,String userCode)
	{

		Map<String, String> params = new HashMap<String, String>();
		
		params.put("reqNo", orderNo);
		params.put("service", "kpp_zdsm_pay");
		params.put("version", "1.0.0-PRD");
		params.put("charset", "UTF-8");
		params.put("language", "zh_CN");
		
		params.put("callerIp", ip); //客户端IP，可空
		params.put("merchantId", merchantId);//商户身份ID
		params.put("secMerchantId", secMerchantId);//子商户ID，一定得有
		params.put("orderNo", orderNo); //商户交易号
		params.put("terminalIp", ip);
		params.put("notifyUrl", KftUrlConfig.NOTIFY_URL);
		params.put("amount", price);//价格
		params.put("currency", "CNY");
		params.put("tradeName", product);
		params.put("remark", product);
		params.put("tradeTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		
		//0000001	微信		0000002	支付宝		0000003	银联		0000004	QQ钱包
		params.put("bankNo", "0000004"); 
		
		if(payType==1)
		{
			params.put("bankNo", "0000004"); 
		}
		else if(payType==2)
		{
			params.put("bankNo", "0000001"); 
		}
		else if(payType==3)
		{
			params.put("bankNo", "0000002"); 
		}
		
		Object[] keys = params.keySet().toArray();
		
		Arrays.sort(keys);
		
		String oriStr = "";
		
		for(Object key : keys)
		{
			oriStr += key + "=" + params.get(key) + "&";
		}
		
		oriStr = oriStr.substring(0, oriStr.length()-1);
		
		String postData = "";
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		resultMap.put("code", "10001");
		
		resultMap.put("msg", "通道请求失败");
		
		try
		{
			
			KeystoreSignProvider keyStoreSignProvider = new KeystoreSignProvider("PKCS12", KftUrlConfig.KEY_FILE_URL, KftUrlConfig.KEY_FILE_PWD.toCharArray(), null, KftUrlConfig.KEY_FILE_PWD.toCharArray());
			
			String key = keyStoreSignProvider.sign(oriStr.getBytes("UTF-8"), Charset.forName("UTF-8"));
			
			for(Object oKey : keys)
			{
				postData += oKey + "=" + URLEncoder.encode(params.get(oKey),"UTF-8") + "&";
			}
			
			postData += "signatureInfo=" + URLEncoder.encode(key,"UTF-8");
			
			postData += "&signatureAlgorithm=RSA";
			
			Map<String, String> headers = new HashMap<String, String>();
			
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			
			System.out.println("KFT POST : " + postData);
			
			String reJsonStr = NetUtil.requetUrl(KftUrlConfig.PAY_URL, headers, postData, "UTF-8");
			
			if(reJsonStr!=null & !"".equals(reJsonStr))
			{
				net.sf.json.JSONObject jo = JsonUtil.getJsonFromString(reJsonStr);
				
				if(jo!=null)
				{
					System.out.println("KFT RE : " + reJsonStr);
					
					String status = jo.getString("status");
					
					if("1".equals(status) || "7".equals(status) || "3".equals(status))
					{
						resultMap.put("code", "10000");
						
						resultMap.put("qr_code", JsonUtil.getString(jo, "codeUrl", ""));
					}
				}
				else
				{
					System.out.println("KFT_PAY RETURN TO JSON ERROR");
				}
				
			}
			else
			{
				System.out.println("KFT_PAY RETURN EMPTY ERROR");
			}
			
		}
		catch (Exception e)
		{
			System.out.println("KFT_PAY ERROR:" + e.getMessage());
		}
		
		return resultMap;
	}
	
}
