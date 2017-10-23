package com.pay.business.util.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.core.teamwork.base.util.IdUtils;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.mail.MailSenderInfo;
import com.core.teamwork.base.util.mail.SimpleMailSender;
import com.core.teamwork.base.util.pay.alipay.config.AlipayConfig;
import com.core.teamwork.base.util.pay.alipay.sign.RSA;
import com.core.teamwork.base.util.pay.alipay.util.AlipayCore;
import com.core.teamwork.base.util.properties.PropertiesUtil;

/**
 * 
 * @ClassName: AliPay 
 * @Description: 支付宝 用于调用支付(app支付)
 * @author yangyu
 * @date 2016年11月11日 下午5:05:34
 */
public class AppAliPay {

	/**
	 * 支付宝签名
	 * @param orderNo 订单号
	 * @param price 金额(以元为单位)
	 * @param subject 商品简介
	 * @param body 商品详情 (可空)
	 * @param notify_url 回调地址 (可空,默认调用常量类的)
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException 
	 */
	public static String sign(String orderNo,String price,String subject
			,String appId,String privateKey) throws UnsupportedEncodingException, AlipayApiException{
		if(StringUtils.isBlank(subject)){
			throw new NullPointerException("支付宝支付不允许商品简介为空");
		}
		
		//参数拼接参数签名
		Map<String, String> params1 = createParam1(orderNo, price, subject,appId);
		//待签名字符串
		String goods1 = AlipayCore.createLinkString(params1);
		//System.out.println("sss:"+goods1);
		String sign = RSA.sign(goods1, privateKey,"UTF-8");
		
		String signs = URLEncoder.encode(sign, "UTF-8");
		
		
		//参数value进行encode编码
		Map<String, String> params = createParam(orderNo, price, subject,appId);
		//最后对请求字符串的所有一级value（biz_content作为一个value）进行encode，编码格式按请求串中的charset为准，没传charset按UTF-8处理，获得最终的请求字符串： 
		String goods = AlipayCore.createLinkString(params);
		return createPayInfo(goods, signs);
	}
	
	private static String createPayInfo(String goods,String sign){
		String payInfo = goods
				+ "&sign="  + sign  ;
		//System.out.println("sss:"+payInfo);
		return payInfo;
	}
	
	/**
	 * 参数value进行encode编码
	 * @param orderNo
	 * @param price
	 * @param body
	 * @param subject
	 * @param notify_url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static Map<String,String> createParam(String orderNo,String price,String subject
			,String appId) throws UnsupportedEncodingException{
		
		Map<String,String> param = new ConcurrentHashMap<String, String>();
		// app_id
		param.put("app_id", URLEncoder.encode(appId, "UTF-8") );
		// 服务接口名称， 固定值
		param.put("method", URLEncoder.encode(AppAlipayConfig.method, "UTF-8") );
		
		// 参数编码， 固定值
		param.put("format", URLEncoder.encode("JSON", "UTF-8") );
		// 参数编码， 固定值
		param.put("charset", URLEncoder.encode("utf-8", "UTF-8") );
		//签名类型
		param.put("sign_type", URLEncoder.encode("RSA", "UTF-8") );
		//发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
		param.put("timestamp", URLEncoder.encode(DateStr(new Date()), "UTF-8") );
		//调用的接口版本，固定为：1.0
		param.put("version", URLEncoder.encode("1.0", "UTF-8") );
		// 服务器异步通知页面路径
		param.put("notify_url",URLEncoder.encode(PropertiesUtil.getProperty("rate", "alipay_notify_url"), "UTF-8") );
		//String biz_content = {"timeout_express":"30m","seller_id":"","product_code":"QUICK_MSECURITY_PAY","total_amount":"0.01","subject":"1","body":"我是测试数据","out_trade_no":"IQJZSRC1YMQB5HU"};
		//业务扩展参数    系统商编号，该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
		String extend_params = "{" +
				"\"sys_service_provider_id\":\""+AppAlipayConfig.sys_pid+"\"" +
		        "}";
		//System.out.println("##########"+AppAlipayConfig.sys_pid);
		String biz_content = "{" +
				 /*"\"timeout_express\":\""+"30m"+"\"," +
				 "\"seller_id\":\""+""+"\"," +
				 "\"product_code\":\""+"QUICK_MSECURITY_PAY"+"\"," +
				 "\"total_amount\":\""+"1.0"+"\"," +
				 "\"subject\":\""+subject+"\"," +
				 "\"body\":\""+"1"+"\"," +
				 "\"out_trade_no\":\""+orderNo+"\"" +*/
	        "\"subject\":\""+subject+"\"," +
	        "\"out_trade_no\":\""+orderNo+"\"," +
	        "\"total_amount\":\""+price+"\"," +
	        "\"seller_id\":\""+AppAlipayConfig.seller_id+"\"," +
	        "\"extend_params\":"+extend_params+"," +
	        "\"product_code\":\"QUICK_MSECURITY_PAY\"" +
	        "}";
		//业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
		param.put("biz_content", URLEncoder.encode(biz_content, "UTF-8") );
		return param;
	}
	
	
	private static Map<String,String> createParam1(String orderNo,String price,String subject
			,String appId) throws UnsupportedEncodingException{
		
		Map<String,String> param = new ConcurrentHashMap<String, String>();
		// app_id
		param.put("app_id",appId);
		// 服务接口名称， 固定值
		param.put("method", AppAlipayConfig.method);
		
		// 参数编码， 固定值
		param.put("format", "JSON");
		// 参数编码， 固定值
		param.put("charset", "utf-8");
		//签名类型
		param.put("sign_type", "RSA");
		//发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
		param.put("timestamp", DateStr(new Date()) );
		//调用的接口版本，固定为：1.0
		param.put("version", "1.0");
		// 服务器异步通知页面路径
		param.put("notify_url",PropertiesUtil.getProperty("rate", "alipay_notify_url"));
		//String biz_content = {"timeout_express":"30m","seller_id":"","product_code":"QUICK_MSECURITY_PAY","total_amount":"0.01","subject":"1","body":"我是测试数据","out_trade_no":"IQJZSRC1YMQB5HU"};
		//业务扩展参数    系统商编号，该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
		String extend_params = "{" +
				"\"sys_service_provider_id\":\""+AppAlipayConfig.sys_pid+"\"" +
		        "}";
		//System.out.println("##########"+AppAlipayConfig.sys_pid);
		String biz_content = "{" +
				 /*"\"timeout_express\":\""+"30m"+"\"," +
				 "\"seller_id\":\""+""+"\"," +
				 "\"product_code\":\""+"QUICK_MSECURITY_PAY"+"\"," +
				 "\"total_amount\":\""+"1.0"+"\"," +
				 "\"subject\":\""+subject+"\"," +
				 "\"body\":\""+"1"+"\"," +
				 "\"out_trade_no\":\""+orderNo+"\"" +   */ 
	        "\"subject\":\""+subject+"\"," +
	        "\"out_trade_no\":\""+orderNo+"\"," +
	        "\"total_amount\":\""+price+"\"," +
	        "\"seller_id\":\""+AppAlipayConfig.seller_id+"\"," +
	        "\"extend_params\":"+extend_params+"," +
	        "\"product_code\":\"QUICK_MSECURITY_PAY\"" +
	        "}";
		//业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
		param.put("biz_content", biz_content);
		return param;
	}
	
	/**
	 * 支付退款
	 * @param orderNum
	 * @param refundMoney
	 * @param map
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayTradeRefundResponse alipayRefund(String orderNum,Double refundMoney
			,Map<String,Object> map,String refundNum,String appId,String privateKey,String publicKey) throws AlipayApiException{
		AlipayClient alipayClient = new DefaultAlipayClient(PayConfigApi.MOBILEPAY_SERVICE,
				appId,privateKey,"json",AppAlipayConfig.input_charset,
				publicKey);
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent(alipayBizContent(orderNum, refundMoney, map,refundNum));
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		return response;
	}
	
	/**
	 * 退款参数拼接
	 * @param orderNum
	 * @param refundMoney
	 * @param map
	 * @return
	 */
	private static String alipayBizContent(String orderNum,Double refundMoney,Map<String,Object> map
			,String refundNum){
		String str = "";
		String refundReason = map.get("refundReason")==null?"正常退款":map.get("refundReason").toString();
		/*if(map.get("refundType").equals("Y")){
			str = "{" +
					"\"out_trade_no\":\""+orderNum+"\"," +
					"\"refund_amount\":"+refundMoney+"," +
					"\"refund_reason\":\""+refundReason+"\"" +
					"}";
		}else{*/
			//部分退款out_request_no每次的值都不一样，保证唯一
			str = "{" +
					"\"out_trade_no\":\""+orderNum+"\"," +
					"\"refund_amount\":"+refundMoney+"," +
					"\"refund_reason\":\""+refundReason+"\"," +
					"\"out_request_no\":\""+IdUtils.createRandomString(9)+"\"" +
					"}";
//		}
		
		return str;
	}
	
	private static String DateStr(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		return str;
	}
	
	public static void main(String[] args) {
		 SimpleMailSender sim = new SimpleMailSender();
		 MailSenderInfo mailInfo = new MailSenderInfo();
		 mailInfo.setMailServerHost("smtp.zywamail.com");
		 mailInfo.setMailServerPort("25");
		 mailInfo.setUserName("");
		 mailInfo.setPassword("");
		 mailInfo.setContent("123231231");
		 // mailInfo.setFromAddress("");
		 mailInfo.setToAddress("slilead@qq.com");
		 mailInfo.setSubject("123");
		int i=  sim.sendHtmlMail(mailInfo);
		System.out.println(i);
	}
}
