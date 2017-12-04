package com.pay.business.util.tcpay.pay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.pay.business.util.StringUtil;
import com.pay.business.util.tcpay.util.HttpClientUtils;
import com.pay.business.util.tcpay.util.JsonUtil;
import com.pay.business.util.tcpay.util.SignUtil;
import com.google.gson.Gson;

public class TcPay
{
	public static Map<String, String> tpPayOrder(String payType, String orderNo,
			String productNo, int price, String appID, String openKey,
			String returnUrl)
	{
		Map<String, Object> payparam = new HashMap<String, Object>();
		Gson gson = new Gson();
		payparam.put("payType", payType);
		payparam.put("productNo", productNo);
		payparam.put("amount", price);
		payparam.put("orderNo", orderNo);
		payparam.put("userName", "wendy@dataeye.com");
		payparam.put("returnurl", returnUrl);

		Map<String, String> extParam = new HashMap<>();

		String ext = gson.toJson(extParam);
		String json = gson.toJson(payparam);
		String sign = SignUtil.getSign(
				SignUtil.getHmacSHA1(appID + "&" + json + "&" + ext, openKey));

		Map<String, String> param = new HashMap<>();

		param.put("appID", appID);
		param.put("ext", ext);
		param.put("json", json);
		param.put("sign", sign);

		Map<String, String> reMap = new HashMap<String, String>();
		reMap.put("code", "10001");
		reMap.put("msg", "未知错误");

		try
		{
			net.sf.json.JSONObject jo = JsonUtil.getJsonFromString(
					HttpClientUtils.post("https://pay.tongcaipay.com/pay/order",
							param));

			System.out.println("TcPay Re Data:" + jo);

			if (jo == null)
			{
				reMap.put("msg", "网关错误");
				return reMap;
			}
			
			int status = JsonUtil.getInteger(jo, "statusCode", -1);
			
			reMap.put("msg", JsonUtil.getString(jo, "statusInfo", "UN KNOW ERRROR"));
			
			if(status==200)
			{
				reMap.put("code", "10000");
				reMap.put("qr_code", JsonUtil.getString(jo, "codeUrl", ""));
			}

			return reMap;
		}
		catch (Exception e)
		{
			reMap.put("msg", e.getMessage());
		}

		return reMap;
	}
	
	
	public static void main(String[] args)
	{
//		String payType = "QQR"; //QQR 微信扫码
//		String orderNo = "AD" + System.currentTimeMillis();
//		String productNo = "P000411";
//		int price = 1;
//		String appID = "A46307DE31B106784995E9B9096C03776";
//		String openKey  = "02aaa7a41e59a2a14445017029a50e24";
//		String returnUrl = "http://www.baidu.com/";
//		
//		Map<String, String> reMap = tpPayOrder(payType, orderNo, productNo, price, appID, openKey, returnUrl);
//		
//		System.out.println(reMap);
		
		String amount = "0.01";
		float fAmount = StringUtil.getFloat(amount, -1);
		System.out.println(fAmount);
		int iAmount = (int)(fAmount*100);
		System.out.println(iAmount);
		
		String moeny = BigDecimal.valueOf(Long.valueOf(iAmount)).divide(new BigDecimal(100)).toString();
		System.out.println(moeny);
	}
}
