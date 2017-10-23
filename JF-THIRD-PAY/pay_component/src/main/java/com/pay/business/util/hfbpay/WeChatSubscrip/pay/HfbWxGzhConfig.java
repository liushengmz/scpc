package com.pay.business.util.hfbpay.WeChatSubscrip.pay;

import com.core.teamwork.base.util.properties.PropertiesUtil;

/**
 * @Title: HtbWxGzhConfig.java
 * @Package com.pay.business.util.hfbpay.WeChatSubscrip.pay
 * @Description: 汇付宝：-微信公众号支付-配置文件
 * @author ZHOULIBO
 * @date 2017年7月11日 下午3:08:34
 * @version V1.0
 */
public class HfbWxGzhConfig {
	// 当前接口版本号 1
	public static String version = "1";
	// 商户key
	// public static String key="4CAE7B1B5ACD4FC8BDCB7D61";
	// 1（默认值）=使用微信公众号支付，0=使用wap微信支付
	public static String is_frame = "1";
	// 微信公众号支付默认为1
	public static String is_phone = "1";
	// 30默认
	public static String pay_type = "30";
	// 商户号
	// public static String agent_id="2105434";
	// 回调URL 异步通知地址
	public static String notify_url = PropertiesUtil.getProperty("rate", "wachat_gzh_pay_notify_url");
	//前端结果回调
	public static final String RETURN_URL=PropertiesUtil.getProperty("rate","hfb_return_url");
	// 订单查询地址
	public static String select_order = "https://query.heepay.com/Payment/Query.aspx";
	//退款URL
	public static String refund_order = "https://pay.heepay.com/API/Payment/PaymentRefund.aspx";
	//对账URL
	public static String statement_url="https://www.heepay.com/API/Check/PaymentCheck.aspx";
}
