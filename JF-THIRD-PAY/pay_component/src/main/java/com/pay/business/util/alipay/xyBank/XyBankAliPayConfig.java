package com.pay.business.util.alipay.xyBank;

import com.core.teamwork.base.util.properties.PropertiesUtil;

/**
* @Title: IndustrialBankPayConfig.java 
* @Package com.pay.business.util.alipay.xyBank 
* @Description:兴业银行支付宝扫码支付配置文件
* @author ZHOULIBO   
* @date 2017年7月7日 下午7:37:23 
* @version V1.0
*/
public class XyBankAliPayConfig {
//	public static String appid = "a20170628000004866";
	// 商户ID（由兴业提供）
//	public static String mch_id = "m20170628000004866";
	public static String req_url="https://api.cib.dcorepay.com/pay/gateway";
	public static String notify_url=PropertiesUtil.getProperty("rate", "ali_xyBank_scan_pay_notify_url");;
	public static String method="dcorepay.alipay.native";
	
	/**
	 * 对账
	 */
	// key（由兴业提供）
//	public static String key = "68c9276022648432996a685a71333c25";
	//
//	public static String attach ="`store_appid=s20170706000003611#store_name=金服公众号#op_user=";
}
