package com.pay.business.util.wftpay;

import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.properties.PropertiesUtil;

public class WftWechatConfig {
	 /**
     * 威富通交易密钥
     */
    //public static String KEY = "11f4aca52cf400263fdd8faf7a69e007";
    //威富通正式环境密钥
    //public static String KEY = "715c33ff734ad37c952acf3b513d322d";//11游戏
    public static String KEY = "37b559c8d3e45cb17e2d9dbc3cfcbc93";	//全民金服
    //public static String KEY = "4345dbceda911e0eadd5edd7bd06ba36";
    
    /**
     * 威富通商户号
     */
    //public static String MCH_ID = "7552900037";
    //威富通正式环境商户号
    //public static String MCH_ID = "103590007749";//11游戏
    public static String MCH_ID = "101550068466";	//全民金服
    //public static String MCH_ID = "127590000074";
    
    
    /**
     * 威富通请求url
     */
    public static String REQ_URL = "https://pay.swiftpass.cn/pay/gateway";
    
    /**
     * app支付接口类型
     */
    public static String PAY_SERVICE_APP = "unified.trade.pay";
    
    /**
     * wap支付接口类型
     */
    public static String PAY_SERVICE_WAP = "pay.weixin.wappay";
    
    /**
     * 查询订单接口类型
     */
    public static String QUERY_SERVICE = "unified.trade.query";
    
    /**
     * 退款接口类型
     */
    public static String REFUND_SERVICE = "unified.trade.refund";
    
    /**
     * 查询退款接口类型
     */
    public static String QUERY_REFUND_SERVICE = "unified.trade.refundquery";
    
    /**
     * 应用类型 IOSsdk
     */
    public static String IOS_SDK = "IOS_SDK";
    
    /**
     * 应用类型 IOSwap
     */
    public static String IOS_WAP = "IOS_WAP";
    
    /**
     * 应用类型 安卓wap
     */
    public static String AND_WAP = "AND_WAP";
    /**
     * 应用类型 安卓sdk
     */
    public static String AND_SDK = "AND_SDK";
    
    /**
     * 应用名
     */
    public static String APP_NAME = "全民金服";
    
    /**
     * 网站名
     */
    public static String APP_NAME_WEB = "全民金服官网";
    
    /**
     * 应用标识
     */
    public static String APP_PAGEAGE = "com.jinfu.theRecipe";
    
    
    /**
     * 应用官网
     */
    public static String APP_PAGEAGE_WEB = "https://www.aijinfu.cn/";
    
    /** 通知url  */
	public static final String NOTIFY_URL = PropertiesUtil.getProperty("rate", "wft_wx_notify_url");
	
	/**
     * 下载账单请求url
     */
    public static String DOWNLOAD_URL = "https://download.swiftpass.cn/gateway";
    
    /**
     * 接口类型参数值
     * pay.bill.merchant 下载单个商户的对账单
     * pay.bill.bigMerchant 下载大商户下所有子商户的对账单
     * pay.bill.agent 下载某渠道下所有商户的对账单
     */
    public static String DOWNLOAD_SERVICE="pay.bill.merchant";
    
    /**
     * 账单类型
     * ALL 全部
     * SUCCESS 成功
     * REFUND 退款
     */
    public static String BILL_TYPE="ALL";
}
