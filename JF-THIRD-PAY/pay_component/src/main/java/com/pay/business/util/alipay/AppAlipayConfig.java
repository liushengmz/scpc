package com.pay.business.util.alipay;

import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.properties.PropertiesUtil;

public class AppAlipayConfig {
		// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	
	
		public static String pid = "2088621176118104";
		// pid
		public static String sys_pid = "2088521153480935";
//		// APPID
//		public static String app_id = "2017021005611761";
		// 卖家支付宝名称
		public static String seller_id = "aidianyou@126.com";
		
//		//商户私钥
//		public static String private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAPPM1/sW3dX7nQ+9Z9ICEGmbR+InmOjCVDcXIKWYLlYgTFEd/EDOVYxrqMWPFqfLPCiCyQqwNYqkoTNDTMRuzqebHJajmgcCgM5HlGkidlSLyhI4RNUcizU+5VkzvT8wlrAD589QNqz3Ca01yR5DCwaur+tufZfJ0fhbH6IXXezBAgMBAAECgYBVXR0msqv797zXvQb0Fnrur0stGZZFsX1RSMzKjo1y5J40LN6dRmHX5/5RJjfViqjGunxwPKCSqfAzhdYEVZQPlqHQMVEGrw0XgSGU+0nL4AMjRrslE3YCm4YIbqHm7guhVmOWZo4aYXeF5g1oAk2nxRCuwkD3DTjt10Gu1iq9/QJBAPqTO9uBt629jV4E8x/Fmi0lhn5jnUmexAXpjMB/n8eR+Q29smlra1RdRggHbQW7albh/ZeZ0wzfb8KKmjFrs2sCQQD5FA+WeFHUR6jc3/k93XCkETnJIvxVJfZrxTZNqcC+0GY2lLZRuFBgpC1IeOAhyuKhOqXLM3+APz4VAw07h5eDAkBzWzL0VByWbKxXO1oeJ19aJ2tqZjuz99ZwjluRB3AsdUQ+EjW/mIdZ2HL0IU8Mk4JaK0IO9+8Ufwy5eAuScu2vAkAmiLb8pXKrb8atHS13J7oUd+HDv1jgZ3YfKCyFiVybaKxXh1xJekVdHikvTBwIvlWfce5SaI4yLhaRs54pdom3AkA3i5cxoTAbyLbGtdt80hYNTkhR+/s4VbE/dVKjlEyx273cu37YM35RN60CIX6qgaabwCZVLHwrwFojsFgx/qKH";
//		
//		// 支付宝的公钥，无需修改该值
//		public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

		// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

		// 调试用，创建TXT日志文件夹路径
		public static String log_path = "D:\\";

		// 字符编码格式 目前支持 gbk 或 utf-8
		public static String input_charset = "utf-8";

		// 签名方式 不需修改
		public static String sign_type = "RSA";
		
		/**
		 * 接口名称固定值
		 */
		public static String method = "alipay.trade.app.pay";
		
		/** 支付宝app支付回调地址  */
		public static final String APP_NOTIFY_URL = PropertiesUtil.getProperty("rate", "app_alipay_notify_url");
}
