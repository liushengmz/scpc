package com.pay.business.util.wftpay.weChatSubscrPay.swiftpassConfig;

import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.properties.PropertiesUtil;



/**
* @Title: SwiftpassConfig.java 
* @Package com.pay.business.util.weChatSubscrPay.swiftpassConfig 
* @Description: 威富通商户信息配置类
* @author 周立波     
* @date 2017年6月27日 下午5:33:30 
* @version V1.0
*/
public class SwiftpassConfig {
//	#测试商户号，商户需改为自己的
//	mch_id=7551000001
//	#测试密钥，商户需改为自己的
//	key=9d101c97133837e13dde2d32a5054abb
//	#接口请求地址，固定不变，无需修改
//	req_url=https://pay.swiftpass.cn/pay/gateway
//	#通知回调地址，目前默认是空格，商户在测试支付和上线时必须改为自己的，且保证外网能访问到
//	notify_url=http://qiuguojie.wicp.net/payDetail/wftWechatPayCallBack.do
    /**
     * 威富通交易密钥
     */
//    public static String key ="9d101c97133837e13dde2d32a5054abb";
    
    /**
     * 威富通商户号
     */
//    public static String mch_id="7551000001";
    
    /**
     * 威富通请求url
     */
    public static final String REQ_URL="https://pay.swiftpass.cn/pay/gateway";
    
    /**
     * 通知url
     */
    public static final String NOTIFY_URL=PropertiesUtil.getProperty("rate", "wachat_wft_pay_notify_url");
    /**
     * 交易完成后跳转的URL，需给绝对路径
     */
    public static final String CALLBACK_URL=PropertiesUtil.getProperty("rate", "wachat_wft_pay_callback_url");
    /**
     * 非原生JS请求支付URL目前是测试URL
     */
    public static final String WFT_PAY_URL="https://pay.swiftpass.cn/pay/jspay?showwxtitle=1&token_id=";
}
