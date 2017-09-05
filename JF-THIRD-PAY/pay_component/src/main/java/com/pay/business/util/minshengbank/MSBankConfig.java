package com.pay.business.util.minshengbank;

import com.core.teamwork.base.util.properties.PropertiesUtil;
/**
* @Title: MSBankConfig.java 
* @Package com.pay.business.util.minshengbank 
* @Description: 民生银行相关配置 
* @author ZHOULIBO   
* @date 2017年7月11日 下午2:43:11 
* @version V1.0
*/
public class MSBankConfig {
		/**
		 * 请求URL
		 */
		public static final String URL ="http://scp.yufu99.com/scanpay-api/api/unifiedOrder20";
		
		/**
		 * 查询URL
		 */
		public static final String QUERY_URL ="http://scp.yufu99.com/scanpay-api/api/orderQuery20";
		
		/**
		 * 退款URL
		 */
		public static final String REFUND_URL = "http://scp.yufu99.com/scanpay-api/api/refund20";
		/**
		 * 回调URL
		 */
		public static final String NOTIFY_URL = PropertiesUtil.getProperty("rate", "ms_ali_scan_pay_notify_url");
		/**
		 * 对账URL
		 */
		public static final String STATEMENT_URL="https://cpos.cmbc.com.cn:18080/tbm-server/mcht/SearchCupCheckDetailList.json";
		/**
		 * 公众号支付
		 */
		public static final String GZHPAY_URL="http://scp.yufu99.com/scanpay-api/api/wxGZHUnifiedOrder20";
}
