package com.pay.business.util.xyShenzhen;

import com.core.teamwork.base.util.properties.PropertiesUtil;

public class XYBankConfig {
	public static String req_url="https://api.cib.dcorepay.com/pay/gateway";
	public static String notify_url = PropertiesUtil.getProperty("rate", "xysz_ali_scan");
	public static String method="dcorepay.alipay.native";
	
	
	public static String WFTmethod_ZFB = "pay.alipay.native";
	public static String WFTmethod_WX = "pay.weixin.native";
	public static String WFT_url = "https://pay.swiftpass.cn/pay/gateway";
	
	
	public static String WFT_GZH = "pay.weixin.jspay";
	
	/**
     * 接口类型参数值
     * pay.bill.merchant 下载单个商户的对账单
     * pay.bill.bigMerchant 下载大商户下所有子商户的对账单
     * pay.bill.agent 下载某渠道下所有商户的对账单
     */
    public static String DOWNLOAD_SERVICE="pay.bill.merchant";
    
    /**
     * 下载账单请求url
     */
    public static String DOWNLOAD_URL = "https://download.swiftpass.cn/gateway";
    
    /**
     * 账单类型
     * ALL 全部
     * SUCCESS 成功
     * REFUND 退款
     */
    public static String BILL_TYPE="ALL";
    
    /**
     * 退款接口类型
     */
    public static String REFUND_SERVICE = "unified.trade.refund";
    
    /**
     * 查询订单接口类型
     */
    public static String QUERY_SERVICE = "unified.trade.query";
}
