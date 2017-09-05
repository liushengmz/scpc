package com.pay.business.util.wxpay;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class WeChatConstant {

	protected static Properties properties = null;
	static{
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("config.properties", Thread.currentThread().getContextClassLoader());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * app支付的应用ID
	 */
	protected static final String APPPAY_APPID_SERVICE = "wxfb28dce18bb0b0f4";//用于扫自定义二维码支付的
	
	protected static final String APPPAY_APPID = "wxf1e29bc9747ce6c1";
	
	/**
	 * 商户号
	 */
	protected static final String APPPAY_MCHID_SERVICE = "1425308602";//用于扫自定义二维码支付的
	
	protected static final String APPPAY_MCHID = "1309462901";
	
	/**
	 * 微信回调地址
	 */
	public static final String WX_NOTIFY_URL = properties.getProperty("wx_notify_url");
	
	/**
	 * APP的支付类型
	 */
	protected static final String TRADE_TYPE_APP = "APP";
	
	/**
	 * 网页或公众号的支付类型
	 */
	protected static final String TRADE_TYPE_JSAPI = "JSAPI";
	
	/**
	 * 本地支付(可能用于扫码的)
	 */
	protected static final String TRADE_TYPE_NATIVE = "NATIVE";
	
	/**
	 * 微信商户平台-->账户设置-->API安全-->密钥设置
	 */
	protected static final String KEY_SERVICE = "nw5IHpj1umiCl0lmLvoitebVBtWKQGtm";//用于扫自定义二维码的
	
	protected static final String KEY = "QyM2XP33ZrpFZsOVhgneSckkbONPQ1Si";
	
	/**
	 * 微信下单地址
	 */
	protected static final String UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/**
	 * 授权地址
	 */
	public static final String OAUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
	
	/**
	 * 获取token地址
	 */
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	/**
	 * 微信公众号的安全密钥
	 */
	public static final String SECRET = "46840e66a29887e69bfe80aa27918276";
	
	/**
	 * 刷卡支付（被扫场景）
	 */
	protected static final String SCAN_PAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";
	
	/**
	 * 退款url
	 */
	protected static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	/**
	 * 退款证书地址
	 */
	protected static final String REFUND_CERT_URL = "D://apiclient_cert.p12";
	
	/**
	 * 查询订单url
	 */
	protected static final String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
}
