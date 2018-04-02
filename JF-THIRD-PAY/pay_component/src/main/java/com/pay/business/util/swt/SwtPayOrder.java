package com.pay.business.util.swt;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pay.business.util.ServiceUtil;

public class SwtPayOrder
{
	/**
	 * 
	 * @param merchantId
	 * @param subChnMerno
	 * @param skey
	 * @param payType 1微信扫码 2支付宝扫码 3QQ扫码
	 * @param orderNo
	 * @param price
	 * @param notifyUrl
	 * @param returnUrl
	 */
	public static Map<String, String> queryOrder(String merchantId, String subChnMerno,
			String skey, int payType, String orderNo, String price,String product,String ip,
			String notifyUrl, String returnUrl,String userCode)
	{
		
		String prodCode = "CP00000085";
		
		if(payType==1)
			prodCode = "CP00000085";
		else if(payType==2)
			prodCode = "CP00000083";
		else if(payType==3)
			prodCode = "CP00000087";
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("version", "2.0");
		map.put("merchantId", merchantId);
		map.put("subChnMerno", subChnMerno);
		map.put("prodCode", prodCode);
		map.put("orderId", orderNo);
		map.put("orderAmount", price);
		map.put("orderDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		map.put("prdOrdType", "0");
		map.put("retUrl", notifyUrl);
		map.put("returnUrl", returnUrl);
		map.put("prdName", SwtPayUtil.stringToHexString(product));
		map.put("signType", "MD5");
		
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
		
		String sign = SwtPayUtil.MD5(sourceKey);
		
		oriStr += "signature=" + sign;

		String result = ServiceUtil.sendGet(SwtUrlConfig.PAY_URL, null, oriStr);
		
		System.out.println(result);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		try
		{
			Map<String, String> dMap = SwtPayUtil.toMap(result);
			
			String reCode = dMap.get("retCode");
			
			if("0001".equals(reCode))
			{
				resultMap.put("qr_code",  dMap.get("codeUrl"));
				resultMap.put("code", "10000");
			}
			else
			{
				resultMap.put("code", "10001");
				resultMap.put("msg", "通道请求失败");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			resultMap.put("code", "10001");
			resultMap.put("msg", ex.toString());
		}
		
		return resultMap;
	}
	
	public static void main(String[] args)
	{
		String merchantId = "00000000019364";
		String subChnMerno = "8db59328878e3a38073f7d9d60beb6a5";
		String skey = "SB0XSDVY9WZO8CEPF09PWIKF";
		int payType = 1;
		String orderNo = "HID" + System.currentTimeMillis();
		String price = "101";
		String product = "测试商品";
		String ip = "189.23.43.11";
		String notifyUrl = "https://pay.iquxun.cn/aiJinFuPay/SwtPayCallBack.do"; 
		String returnUrl = ""; 
		String userCode = "";
		Map<String, String> map = queryOrder(merchantId, subChnMerno, skey, payType, orderNo, price, product, ip, notifyUrl, returnUrl, userCode);
		System.out.println(map);
	}
}
