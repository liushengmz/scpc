package com.pay.business.util.ympay;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pay.business.util.ServiceUtil;
import com.pay.business.util.StringUtil;
import com.pay.business.util.tcpay.util.JsonUtil;

import net.sf.json.JSONObject;

public class YmPay
{
	public static final String NotifyUrl = "https://pay.iquxun.cn/aiJinFuPay/YmPayCallBack.do";
	
	private static String C_GateWay = "http://paymentapi.emaxcard.com/QRCode/CreateOrder";
	
	/**
	 * 
	 * @param merCode
	 * @param skey
	 * @param payType 1微信扫码 2支付宝扫码 3QQ扫码
	 * @param orderNo
	 * @param price
	 * @param returnUrl
	 */
	public static Map<String, String> queryOrder(String merCode,
			String skey, int payType, String orderNo, int price,String returnUrl)
	{
		
		String prodCode = "5";
		
		//1和2暂定
		if(payType==1)
			prodCode = "6";
		else if(payType==2)
			prodCode = "7";
		else if(payType==3)
			prodCode = "5";
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("orderNo", orderNo);
		map.put("orderAmount", price + "");
		map.put("callbackUrl", NotifyUrl);
		map.put("payType", prodCode);//支付类型
		map.put("productDesc", orderNo);
		map.put("merCode", merCode);//商户编号
		map.put("dateTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		map.put("validityNum", "120");
		
		Object[] keys = map.keySet().toArray();
		
		Arrays.sort(keys);
		
		String sourceKey = "";
		
		String oriStr = "";
		
		String value = "";
			
		for(Object s : keys)
		{
			value = map.get(s);
			
			if(value!=null && !"".equals(value))
				sourceKey += ( s + "=" + value + "&");
			
			oriStr += ( s + "=" + value + "&");
		}
		
		sourceKey = sourceKey.substring(0, sourceKey.length()-1);
		
		sourceKey += skey;
		
		String sign = StringUtil.getMd5String(sourceKey, 32);
		
		oriStr += "sign=" + sign;
		
		System.out.println("YM POST :" + oriStr);

		String result = ServiceUtil.sendGet(C_GateWay, null, oriStr);
		
		System.out.println("YM PAY:" + result);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		resultMap.put("code", "10001");
		resultMap.put("msg", "通道请求失败");
		
		try
		{
			if(StringUtil.isNullOrEmpty(result))
			{
				return resultMap;
			}
			
			JSONObject jo = JsonUtil.getJsonFromString(result);
			
			if(jo==null)
			{
				System.out.println("YM PAY JSON EMPTY");
				return resultMap;
			}
			
			String resultCode = jo.getString("resultCode");
			
			if("000000".equalsIgnoreCase(resultCode))
			{
				resultMap.put("qr_code", jo.getString("qrCodeUrl"));
				resultMap.put("code", "10000");
				return resultMap;
			}
		}
		catch(Exception ex)
		{
			System.out.println("YM PAY ERROR:" + ex.getMessage());
			resultMap.put("code", "10001");
			resultMap.put("msg", ex.toString());
		}
		
		return resultMap;
	}
}
