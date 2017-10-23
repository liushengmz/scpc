package com.pay.business.util.tfbpay;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * 天付宝支付工具类
 * @author Administrator
 *
 */
public class TfbPay {
	
	/**
	 * 银行卡快捷签约支付申请
	 * @param orderNum（订单号）
	 * @param payMoney（订单金额）
	 * @param orderName（订单名称）
	 * @param orderDescribe（订单描述）
	 * @return
	 * @throws Exception
	 */
	public static String paySign(String orderNum,String payMoney,String orderName,String orderDescribe) throws Exception{
		TreeMap<String,String> map = new TreeMap<>();
		// 商户/平台在国采注册的账号。国采维度唯一，固定长度10位
		map.put("spid", TFBConfig.spid);
		// 用户号，持卡人在商户/平台注册的账号。商户/平台维度唯一，必须为纯数字
		map.put("sp_userid", TFBConfig.sp_userid);
		//订单号
		map.put("spbillno", orderNum);
		//订单金额  单位：分
		map.put("money", payMoney);
		//订单金额的类型。1 – 人民币(单位: 分)
		map.put("cur_type", "1");
		map.put("return_url", "http://qiuguojie.wicp.net/jk.html");
		map.put("notify_url", "http://qiuguojie.wicp.net/payDetail/tfbCallBack.do");
		map.put("memo", orderName);
		map.put("channel", "2");
		map.put("encode_type", TFBConfig.encode_type);
		/*map.put("busi_type", "1");
		map.put("sign_sn", "373b35c4217382e36ad30c998fbc2026");*/
		map.put("busi_type", "2");
		map.put("cardid", "6013822000642844875");
		map.put("truename", "邱国杰");
		map.put("cre_id", "431002199009285810");
		map.put("cre_type", "1");
		map.put("mobile", "18682475275");
		//按照“参数=参数值”的模式用“&”字符拼接成字符串
		String paramSrc = RequestUtils.getParamSrc(map);
		//md5签名
		String sign = MD5Utils.sign(paramSrc);
		/*//RSA签名
		String sign = RSAUtils.sign(paramSrc.getBytes());*/
		System.out.println("RSA签名结果："+sign);
		//RAS加密
		String param = RSAUtils.encrypt(paramSrc+"&sign="+sign);
		//请求支付
		String postStr = RequestUtils.doPost(TFBConfig.qsignPayApplyApi
				, "cipher_data=" + URLEncoder.encode(param, TFBConfig.serverEncodeType));
		
		//如果返回码不为00，返回结果不会加密，因此不往下处理，
	    if (!RequestUtils.getXmlElement(postStr, "retcode").equals("00")) {
	    	System.out.println(postStr);
	        return "";
	    }
		
		//获得服务器返回的加密数据
	    String cipherResponseData = RequestUtils.getXmlElement(postStr, "cipher_data");

	    //对服务器返回的加密数据进行rsa解密
	    postStr = RSAUtils.decrypt(cipherResponseData);
		
		return postStr;
	}
	
	/**
	 * 银行卡快捷签约支付确认
	 * @param orderNum（订单号）
	 * @param payMoney（订单金额）
	 * @param orderName（订单名称）
	 * @param orderDescribe（订单描述）
	 * @return
	 * @throws Exception
	 */
	public static String paySignConfirm(String orderNum,String payMoney,String smsCode,String signSn) throws Exception{
		TreeMap<String,String> map = new TreeMap<>();
		// 商户/平台在国采注册的账号。国采维度唯一，固定长度10位
		map.put("spid", TFBConfig.spid);
		// 用户号，持卡人在商户/平台注册的账号。商户/平台维度唯一，必须为纯数字
		map.put("sp_userid", TFBConfig.sp_userid);
		//订单号
		map.put("spbillno", orderNum);
		//订单金额  单位：分
		map.put("money", payMoney);
		//订单金额的类型。1 – 人民币(单位: 分)
		map.put("cur_type", "1");
		map.put("channel", "2");
		map.put("encode_type", TFBConfig.encode_type);
		map.put("busi_type", "2");
		map.put("sign_sn", signSn);
		map.put("sms_code", smsCode);
		//按照“参数=参数值”的模式用“&”字符拼接成字符串
		String paramSrc = RequestUtils.getParamSrc(map);
		//md5签名
		String sign = MD5Utils.sign(paramSrc);
		/*//RSA签名
		String sign = RSAUtils.sign(paramSrc.getBytes());*/
		System.out.println("RSA签名结果："+sign);
		//RAS加密
		String param = RSAUtils.encrypt(paramSrc+"&sign="+sign);
		//请求支付
		String postStr = RequestUtils.doPost(TFBConfig.qsignPayComfirmApi
				, "cipher_data=" + URLEncoder.encode(param, TFBConfig.serverEncodeType));
		
		//如果返回码不为00，返回结果不会加密，因此不往下处理，
	    if (!RequestUtils.getXmlElement(postStr, "retcode").equals("00")) {
	    	System.out.println(postStr);
	        return "";
	    }
		
		//获得服务器返回的加密数据
	    String cipherResponseData = RequestUtils.getXmlElement(postStr, "cipher_data");

	    //对服务器返回的加密数据进行rsa解密
	    postStr = RSAUtils.decrypt(cipherResponseData);
		
		return postStr;
	}
	
	public static void main(String[] args) throws Exception {
		/*String result = paySign("13246532", "1", "测试支付", "测试支付");
		if("".equals(result)){
			System.out.println("支付失败");
			return;
		}
		Map<String,String> map = RequestUtils.parseCipherResponseData(result);
		String sign_sn = map.get("sign_sn");*/
		
		String result = paySignConfirm("13246532", "1", "500040", "373b35c4217382e36ad30c998fbc2026");
		System.out.println(result);
	}
}
