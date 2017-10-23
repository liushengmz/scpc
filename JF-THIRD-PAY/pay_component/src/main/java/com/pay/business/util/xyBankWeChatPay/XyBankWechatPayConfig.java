package com.pay.business.util.xyBankWeChatPay;

import com.core.teamwork.base.util.properties.PropertiesUtil;

/**
* @Title: IndustrialBankPayConfig.java 
* @Package com.pay.business.util.IndustrialBankPay 
* @Description: 兴业银行微信公众号支付配置
* @author ZHOULIBO   
* @date 2017年7月6日 下午5:29:23 
* @version V1.0
*/
public class XyBankWechatPayConfig {
//	public static String appid = "a20170628000004866";
	// 商户ID（由兴业提供）
//	public static String mch_id = "m20170628000004866";
	public static String req_url="https://api.cib.dcorepay.com/pay/unifiedorder";
	public static String notify_url=PropertiesUtil.getProperty("rate", "wechat_xyBank_pay_notify_url");;
	public static String NATIVE="NATIVE";
	public static String JSAPI="JSAPI";
	// key（由兴业提供）
//	public static String key = "68c9276022648432996a685a71333c25";
	//
	public static String attach ="`store_appid=s20170706000003611#store_name=金服公众号#op_user=";
	//退款URL
	public static String refund_url="https://api.cib.dcorepay.com/pay/refund";
	//订单查询URL
	public static String order_url="https://api.cib.dcorepay.com/pay/orderquery";
	/**
	 * 对账URL
	 */
	public static String STATEMENT_URL ="https://api.cib.dotcore.cn/pay/downloadbill";
}
