package com.pay.business.util.guofu;

import com.core.teamwork.base.util.properties.PropertiesUtil;

public class GuoFuConfig {
	
	/**
	 * 基础地址
	 */
	protected static final String BASE_URL = "http://api.posp168.com/";
	
	/**
	 * 被扫码地址
	 */
	protected static final String PASSIVEPAY = "passivePay";
	
	/**
	 * 单笔订单查询
	 */
	protected static final String QRCODEQUERY = "qrcodeQuery";
	
	/**
	 * 回调通知
	 */
	protected static final String NOTIFY_URL = PropertiesUtil.getProperty("rate", "guofu_notify_url");
}
