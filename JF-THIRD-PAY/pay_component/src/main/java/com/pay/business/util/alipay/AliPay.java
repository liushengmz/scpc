package com.pay.business.util.alipay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.core.teamwork.base.util.encrypt.MD5;
import com.core.teamwork.base.util.pay.alipay.config.AlipayConfig;
import com.core.teamwork.base.util.pay.alipay.sign.RSA;
import com.core.teamwork.base.util.pay.alipay.util.AlipayCore;
import com.core.teamwork.base.util.properties.PropertiesUtil;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.util.MapUtil;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.RandomUtil;
import com.pay.business.util.alipay.xyBank.XmlUtils;
import com.pay.business.util.alipay.xyBank.XyBankAliPayConfig;
import com.pay.business.util.xyBankWeChatPay.SslUtils;

/**
 * 
 * @ClassName: AliPay 
 * @Description: 支付宝 用于调用支付(移动支付)
 * @author yangyu
 * @date 2016年11月11日 下午5:05:34
 */
public class AliPay {
	private static Logger log = Logger.getLogger(AliPay.class);
	private static final String QUOTES = "\""; //引号 应对参数值的
	
	public static String sign(final String orderNo,final String price,final String subject) throws UnsupportedEncodingException{
		return sign(orderNo, price, subject, null,null);
	}
	
	public static String sign(final String orderNo,final String price,final String subject,final String notify_url) throws UnsupportedEncodingException{
		return sign(orderNo, price, subject, null,notify_url);
	}
	
	/**
	 * 支付宝签名
	 * @param orderNo 订单号
	 * @param price 金额(以元为单位)
	 * @param subject 商品简介
	 * @param body 商品详情 (可空)
	 * @param notify_url 回调地址 (可空,默认调用常量类的)
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String sign(final String orderNo,final String price,final String subject,final String body,final String notify_url) throws UnsupportedEncodingException{
		if(StringUtils.isBlank(subject)){
			throw new NullPointerException("支付宝支付不允许商品简介为空");
		}
		Map<String, String> params = createParam(orderNo, price, body, subject,notify_url); 
		String goods = AlipayCore.createLinkString(params);
		//System.out.println(AlipayConfig.private_key);
		String sign = RSA.sign(goods, AlipayConfig.private_key,"UTF-8");
		String signs = URLEncoder.encode(sign, "UTF-8");
		return createPayInfo(goods, signs);
	}
	
	private static String createPayInfo(String goods,String sign){
		String payInfo = goods
				+ "&sign=" + QUOTES + sign + QUOTES + "&sign_type=" + QUOTES + "RSA" + QUOTES;
		return payInfo;
	}
	
	private static Map<String,String> createParam(String orderNo,String price,String body,String subject,String notify_url){
		
		Map<String,String> param = new ConcurrentHashMap<String, String>();
		// 签约合作者身份ID
		param.put("partner", QUOTES + AlipayConfig.partner + QUOTES);
		// 签约卖家支付宝账号
		param.put("seller_id", QUOTES + AlipayConfig.seller_id + QUOTES);
		// 商户网站唯一订单号
		param.put("out_trade_no",QUOTES + orderNo + QUOTES);
		// 商品名称
		param.put("subject", QUOTES + subject + QUOTES);
		
		// 商品详情
		if(StringUtils.isNotBlank(body)){
			param.put("body", QUOTES + body + QUOTES);
		}
		
		// 商品金额	
		param.put("total_fee",QUOTES + price + QUOTES);
		
		// 服务器异步通知页面路径
		if(StringUtils.isNotBlank(notify_url)){
			param.put("notify_url",QUOTES + notify_url + QUOTES);
		}else{
			param.put("notify_url",QUOTES + PropertiesUtil.getProperty("rate", "alipay_notify_url") + QUOTES);
		}
		
		// 服务接口名称， 固定值
		param.put("service",QUOTES + AlipayConfig.service + QUOTES);
		// 支付类型， 固定值
		param.put("payment_type",QUOTES + "1" + QUOTES);
		// 参数编码， 固定值
		param.put("_input_charset",QUOTES + "utf-8" + QUOTES);
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		//param.put("it_b_pay", "30m"); 
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		//param.put("return_url", "m.alipay.com");
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		//param.put("paymethod", "expressGateway");
		return param;
	}
	
	/**
	 * 支付宝查询接口
	 * @param orderNum
	 * @return
	 * @throws AlipayApiException
	 */
	public static JSONObject alipayQuery(String orderNum,String appId,String privateKey
			,String publicKey,String singType) throws AlipayApiException{
		AlipayClient alipayClient = new DefaultAlipayClient(PayConfigApi.MOBILEPAY_SERVICE
				, appId, privateKey, PayConfigApi.FORMAT, PayConfigApi.CHARSET
				, publicKey,singType);
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent("{" +
		"\"out_trade_no\":\""+orderNum+"\"" +
//		"\"out_trade_no\":\"1481706155863\"" +
		"}");
		AlipayTradeQueryResponse response1 = alipayClient.execute(request);
		String ss = response1.getBody();
		JSONObject json = (JSONObject) JSONObject.parse(ss);
		
		//System.out.println(json.get("sign"));
		//System.out.println(json.get("alipay_trade_query_response"));
		
		JSONObject result = (JSONObject)json.get("alipay_trade_query_response");
		return result;
	}
	
	/**
	 * h5支付  获取表单
	 * @return
	 * @throws AlipayApiException 
	 */
	public static String alipayH5(String returnUrl,String orderNum,String orderName,Double payMoeny
			,String appId,String privateKey,String publicKey) throws AlipayApiException{
		AlipayClient alipayClient = new DefaultAlipayClient(PayConfigApi.MOBILEPAY_SERVICE
				, appId, privateKey, PayConfigApi.FORMAT, PayConfigApi.CHARSET
				, publicKey);
			    AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
		alipayRequest.setNotifyUrl(PayConfigApi.H5_NOTIFY_URL);//在公共参数中设置回跳和通知地址
		
		alipayRequest.setReturnUrl(PayConfigApi.H5_RETURN_URL);
		
		String extend_params = "{" +
				"\"sys_service_provider_id\":\""+AppAlipayConfig.sys_pid+"\"" +
		        "}";
		alipayRequest.setBizContent("{" +
		        "\"out_trade_no\":\""+orderNum+"\"," +
		        "\"total_amount\":\""+payMoeny+"\"," +
		        "\"subject\":\""+orderName+"\"," +
		        "\"extend_params\":"+extend_params+"," +
		        "\"product_code\":\"QUICK_WAP_PAY\"" +
		        "}");//填充业务参数
		String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
		return form;
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
				appId,privateKey,PayConfigApi.FORMAT,PayConfigApi.CHARSET,
				publicKey);
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent(alipayBizContent(orderNum, refundMoney, map,refundNum));
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		return response;
	}
	
	/**
	 * 支付宝条码支付
	 * @param orderNum
	 * @param authCode
	 * @param orderName
	 * @param payMoney
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayTradePayResponse scanPay(String orderNum,String authCode,String orderName
			,Double payMoney,String appId,String privateKey,String publicKey) throws AlipayApiException{
		//条码支付
		AlipayClient alipayClient = new DefaultAlipayClient(PayConfigApi.MOBILEPAY_SERVICE, appId,
				privateKey, PayConfigApi.FORMAT, PayConfigApi.CHARSET, publicKey); //获得初始化的AlipayClient
		AlipayTradePayRequest request = new AlipayTradePayRequest(); //创建API对应的request类
		request.setBizContent("{" +
		"    \"out_trade_no\":\""+orderNum+"\"," +
		"    \"scene\":\"bar_code\"," +
		"    \"auth_code\":\""+authCode+"\"," +
		"    \"subject\":\""+orderName+"\"," +
		"    \"total_amount\":\""+payMoney+"\"" +
		"  }"); //设置业务参数
		AlipayTradePayResponse response = alipayClient.execute(request); //通过alipayClient调用API，获得对应的response类
		//System.out.print(response.getBody());
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
					"\"out_request_no\":\""+refundNum+"\"" +
					"}";
//		}
		
		return str;
	}
	
	/**
	* @Title: aliScanRefund 
	* @Description: 支付宝扫码退款
	* @param @param orderNum 支付集订单号
	* @param @param refundMoney 退款金额
	* @param @param map
	* @param @param refundNum 退款订单号
	* @param @return
	* @param @throws AlipayApiException    设定文件 
	* @return AlipayTradeRefundResponse    返回类型 
	* @throws
	* AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
	*/
	public static AlipayTradeRefundResponse aliScanRefund(Payv2PayOrder payv2PayOrder,Double refundMoney
			,Map<String,Object> map,String refundNum,String appId,String privateKey,String publicKey) throws AlipayApiException{
		AlipayClient alipayClient = new DefaultAlipayClient(
				AliPayConfig.REQUEST_ALIPAY_URL,
				appId,
				privateKey,
				AliPayConfig.FORMAT,
				AliPayConfig.CHARSET,
				publicKey,
				AliPayConfig.SIGN_TYPE);
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent(alipayBizContent(payv2PayOrder.getOrderNum(), refundMoney, map,refundNum));
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			System.out.println("调用支付宝扫码退款成功");
			} else {
			System.out.println("调用支付宝扫码退款调用失败");
			}
		return response;
	}
	
	/**
	 * aliTradePrecreatePay 
	 * 支付宝扫码支付-扫码支付预下订单接口 
	 * @param orderNum
	 * @param goodsName
	 * @param amount
	 * @param timeOut
	 * @param APPID
	 * @param PKCS8_PRIVATE
	 * @param ALIPAY_RSA_PUBLIC
	 * @return
	 * @throws Exception    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> aliTradePrecreatePay(String orderNum, String goodsName, double amount, int timeOut,String APPID,String PKCS8_PRIVATE,String ALIPAY_RSA_PUBLIC) throws Exception {
		// 先去做一系列判断
		Map<String, String> returnMap = new HashMap<String, String>();
		AlipayClient alipayClient = new DefaultAlipayClient(
				AliPayConfig.REQUEST_ALIPAY_URL, 
				APPID, 
				PKCS8_PRIVATE,
				AliPayConfig.FORMAT, 
				AliPayConfig.CHARSET, 
				ALIPAY_RSA_PUBLIC, 
				AliPayConfig.SIGN_TYPE);
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setNotifyUrl(AliPayConfig.NOTIFY_URL);
		request.setBizContent("{" + "    \"out_trade_no\":\"" + orderNum + "\"," + "    \"total_amount\":" + amount + "," + "    \"subject\":\"" + goodsName
				+ "\"," + "    \"extend_params\":{" + "      \"sys_service_provider_id\":\"" + AliPayConfig.SERVICE_PID + "\"" + "    },"
				+ "    \"timeout_express\":\"" + timeOut + "m\"" + "  }");

		AlipayTradePrecreateResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			returnMap.put("code","10001");
			returnMap.put("msg", "调用支付宝发生错误,可能因为key错误");
			return returnMap;

		}
		JSONObject jsonObject = JSON.parseObject(response.getBody());
		JSONObject json = jsonObject.getJSONObject("alipay_trade_precreate_response");
		Set<String> it = json.keySet();
		for (String string : it) {
			returnMap.put(string, json.getString(string));
		}
		return returnMap;
	}
	
	/**
	 * xyBankAliaScanPay 
	 * 兴业银行-支付宝-扫码支付
	 * @param out_trade_no 订单号
	 * @param total_fee 金额
	 * @param body 商品名字
	 * @param appid 商户appid
	 * @param mch_id 商户号
	 * @param key 商户key
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String,String> xyBankAliaScanPay(String out_trade_no,String total_fee, String body,String appid,String mch_id,String key){
		Map<String, String> resultMap = new HashMap<String, String>();
		String method=XyBankAliPayConfig.method;
		//随机字符串
		String nonce_str=new Date().getTime()+ RandomUtil.generateString(4);
		//签名
		String sign="";
		//回调URL
		String notify_url=XyBankAliPayConfig.notify_url;
		// 签名
		Map<String, String> sginMap = new HashMap<String, String>();
		sginMap.put("method", method);
		sginMap.put("appid", appid);
		sginMap.put("mch_id", mch_id);
		sginMap.put("nonce_str", nonce_str);
		sginMap.put("body", body);
		sginMap.put("out_trade_no", out_trade_no);
		sginMap.put("total_fee",total_fee);
		sginMap.put("notify_url", notify_url);
		sign = signMd5(key, sginMap);
		sginMap.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=====>提交参数转换为XML格式：\n" + xmlInfo);
		try {
			String info = sendPost(XyBankAliPayConfig.req_url, xmlInfo);
			log.info("=====>兴业银行返回的预支付订单信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			log.info("=====>转换为map值为：" + toxmlMap);
			if ("SUCCESS".equals(toxmlMap.get("return_code")) && "SUCCESS".equals(toxmlMap.get("result_code"))) {
				resultMap.put("qr_code", toxmlMap.get("code_url"));
				resultMap.put("code","10000");
				resultMap.put("out_trade_no",out_trade_no);
				String code_url = toxmlMap.get("code_url");
				log.info("=====>兴业银行支付宝扫码支付调起成功:qr_code : " + code_url);
			} else {
				resultMap.put("code","10001");
				resultMap.put("msg", info);
				log.error("=====>兴业银行支付宝扫码支付失败原因:" + info);
			}
		} catch (Exception e) {
			log.error("=====>兴业银行支付宝扫码支付操作失败，原因：", e);
			resultMap.put("code","10001");
			resultMap.put("msg", e.toString());
		}
		return resultMap;
	}
	
	/**
	 * xyBankOrderSelect 
	 * 兴业银行：支付宝-扫码支付：订单查询 
	 * @param out_trade_no 商户订单号
	 * @param appid 商户appid
	 * @param mch_id 商户号
	 * @param key 商户key
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> xyBankOrderSelect(String out_trade_no, String appid, String mch_id, String key) {
		Map<String, String> resultMap = new HashMap<String, String>();
		String method = "dcorepay.alipay.query";
		// 随机字符串
		String nonce_str = new Date().getTime()+ RandomUtil.generateString(4);
		// 签名
		String sign = "";
		// 签名
		Map<String, String> sginMap = new HashMap<String, String>();
		sginMap.put("method", method);
		sginMap.put("appid", appid);
		sginMap.put("mch_id", mch_id);
		sginMap.put("nonce_str", nonce_str);
		sginMap.put("out_trade_no", out_trade_no);
		sign = signMd5(key, sginMap);
		sginMap.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=====>提交参数转换为XML格式：\n" + xmlInfo);
		try {
			String info = sendPost("https://api.cib.dcorepay.com/pay/gateway", xmlInfo);
			log.info("=====>兴业银行查询订单返回的订单信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			if ("SUCCESS".equals(toxmlMap.get("return_code")) && "SUCCESS".equals(toxmlMap.get("result_code"))) {
				String trade_state=toxmlMap.get("trade_state");
				switch (trade_state) {
				case "SUCCESS":
					resultMap.put("payStatus","1");//成功
					break;
				case "CLOSED":
					resultMap.put("payStatus","4");//订单已经关闭：超时
					break;
				case "USERPAYING":
					resultMap.put("payStatus","3");//未支付
					break;	
				default:
					break;
				}
				resultMap.put("code","10000");
				log.info("=====>兴业银行：支付宝-扫码支付：订单查询状态为："+trade_state);
			}else{
				resultMap.put("code","10001");//获取订单信息失败
				resultMap.put("msg","失败原因为："+toxmlMap.get("return_msg"));//获取订单信息失败
				log.error("=====>兴业银行：支付宝-扫码支付：订单查询状态为："+toxmlMap.get("return_msg"));
			}
		}catch(Exception e){
			log.error("=====>兴业银行查询订单失败：原因为："+e);
			resultMap.put("code","10001");//获取订单信息失败
			resultMap.put("msg","失败原因为："+e);//获取订单信息失败
		}
		
		return resultMap;
	}

	/**
	 * xyBankScanPayRefund 
	 * 兴业银行-支付宝扫码支付-退款申请 
	 * @param out_trade_no 订单号
	 * @param appid
	 * @param mch_id
	 * @param key
	 * @param refund_fee 退款金额 以分为单位
	 * @param op_user_id 操作人默认商户mch_id
	 * @param out_refund_no  退款订单号
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String,String> xyBankScanPayRefund(String out_trade_no, String appid, String mch_id, String key, int refund_fee, String op_user_id,
			String out_refund_no) {
		Map<String,String>  response =new HashMap<String, String>();
		String method = "dcorepay.alipay.refund";
		// 随机字符串
		String nonce_str = new Date().getTime()+ RandomUtil.generateString(4);
		// 签名
		String sign = "";
		// 签名
		Map<String, String> sginMap = new HashMap<String, String>();
		sginMap.put("method", method);
		sginMap.put("appid", appid);
		sginMap.put("mch_id", mch_id);
		sginMap.put("nonce_str", nonce_str);
		sginMap.put("out_trade_no", out_trade_no);
		sginMap.put("refund_fee", String.valueOf(refund_fee));
		sginMap.put("op_user_id", mch_id);
		sginMap.put("out_refund_no", out_refund_no);
		sign = signMd5(key, sginMap);
		sginMap.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=====>提交参数转换为XML格式：\n" + xmlInfo);
		try {
			String info = sendPost(XyBankAliPayConfig.req_url, xmlInfo);
			log.info("=====>兴业银行扫码支付退款订单信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			if ("SUCCESS".equals(toxmlMap.get("return_code")) && "SUCCESS".equals(toxmlMap.get("result_code"))) {
					response.put("code", "10000");
					response.put("gmtRefundPay", toxmlMap.get("gmt_refund_pay"));
					log.info("=====>兴业银行扫码支付退款成功");
			} else {
				response.put("code", "10001");
				response.put("msg", "兴业银行扫码支付退款接口失败："+toxmlMap);
				log.error("=====>兴业银行扫码支付退款失败原因："+toxmlMap);
			}
		} catch (Exception e) {
			log.error("=====>兴业银行扫码支付退款失败：原因为：" + e);
			response.put("code", "10001");
			response.put("msg", "兴业银行扫码支付退款接口失败："+e);
		}
		return response;
	}

	/**
	 * 
	 * xyBankAliStatement 
	 * 兴业银行-支付宝扫码-对账接口
	 * @param appid
	 * @param mch_id
	 * @param key
	 * @param bill_date 查询时间
	 * @return    设定文件 
	 * String[]    返回类型
	 */
	public static  String [] xyBankAliStatement(String appid,String mch_id,String key,String bill_date){
		String method="dcorepay.alipay.bill";
		String nonce_str=new Date().getTime()+ RandomUtil.generateString(4);
		String sign="";
		// 签名
		Map<String, String> sginMap = new HashMap<String, String>();
		sginMap.put("method", method);
		sginMap.put("appid", appid);
		sginMap.put("mch_id", mch_id);
		sginMap.put("nonce_str", nonce_str);
		sginMap.put("bill_date", bill_date);
		sign = signMd5(key, sginMap);
		sginMap.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=====>提交参数转换为XML格式：\n" + xmlInfo);
		try{
			String info = sendPost(XyBankAliPayConfig.req_url, xmlInfo);
			log.info("=====>兴业银行支付宝对账结果为:"+info);
			int yes=info.indexOf("<xml>");
			if(yes<0){
				String str[]=info.split("`");
				return str;
			}else{
				log.error("=====>兴业银行支付宝对账失败：原因为:"+info);
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("=====>兴业银行支付宝对账失败：原因为：" + e);
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: signMd5
	 * @Description: MD5字符串拼接加密
	 * @param @param key
	 * @param @param map
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String signMd5(String key, Map<String, String> map) {
		Map<String, Object> sginMap = new HashMap<String, Object>();
		String newMap = MapUtil.mapToJson(map);
		sginMap = MapUtil.parseJsonToMap(newMap);
		String sgin = PaySignUtil.getParamStr(sginMap);
		sgin = sgin + "&key=" + key;
		log.info("=====>兴业银行支付宝扫码支付参数加密拼接为：" + sgin);
		return MD5.GetMD5Code(sgin).toUpperCase();
	}
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			if ("https".equalsIgnoreCase(realUrl.getProtocol())) {
				SslUtils.ignoreSsl();
			}
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "text/xml;charset=ISO-8859-1");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流finally{
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	/*public static void main(String[] args) {
//		xyBankOrderSelect("DD20170712143447202138429","a20170628000004866", "m20170628000004866", "68c9276022648432996a685a71333c25");
		String [] str=xyBankAliStatement("a20170628000004866", "m20170628000004866", "68c9276022648432996a685a71333c25", "20170721");
		String vv="商户ID,支付宝交易号,商户订单号,业务类型,商品名称,创建时间,完成时间,门店编号,门店名称,操作员,终端号,对方账户,订单金额（元）,商家实收（元）,支付宝红包（元）,集分宝（元）,支付宝优惠（元）,商家优惠（元）,券核销金额（元）,券名称,商家红包消费金额（元）,卡消费金额（元）,退款批次号,手续费（元）,费率,实收净额（元）,交易方式,备注`m20170628000004866,`2017071221001004660224927787,`DD20170712143447202138429,`退款,`测试支付宝扫码支付,`,`2017-07-12 16:03:30,`0,`北京全民金服科技有限公司,`,`,`,`0.01,`0.01,`0,`0,`0,`0,`0,`,`0,`0,`RR20170712160028098723,`0,`0.0038,`0.01,`ALI_NATIVE,``m20170628000004866,`2017071221001004660224927787,`DD20170712143447202138429,`交易,`测试支付宝扫码支付,`2017-07-12 14:38:20,`2017-07-12 14:38:30,`0,`北京全民金服科技有限公司,`,`,`130****3212,`0.01,`0.01,`0,`0,`0,`0,`0,`,`0,`0,`,`0,`0.0038,`0.01,`ALI_NATIVE,`总交易单数,总交易实收额,交易手续费总金额,总退款笔数,总退款金额,退款手续费总金额`1,`0.01,`0,`1,`0.01,`0";
//		String [] str=vv.split("`");
//		System.out.println(str);
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> list1 = new ArrayList<String>();
		
		int num = str.length/28;
		
		for (int i = 1; i < str.length; i++) {
			if(i%28==0){
				list1.add(str[i]);
				list.add(list1);
				if(i==num*28){
					int n = list1.get(list1.size()-1).indexOf("总交易单数");
					list1.set(list1.size()-1, list1.get(list1.size()-1).substring(0,n));
				}
				list1 = new ArrayList<String>();
			}else{
				list1.add(str[i]);
			}
			if(i==str.length-1){
				list.add(list1);
			}
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			for (List<String> list2 : list) {
//				System.out.println(list2);
				for (int i = 0; i < list2.size(); i++) {
					String yes = getOrderStr(list2.get(i));
					//成功
					if(yes.equals("交易")){
						// 获取订单号
						System.out.println("交易状态："+yes);
						System.out.println("支付集订单号：" + getOrderStr(list2.get(2)));
						System.out.println("总金额：" + getOrderStr(list2.get(25)));
						Date finishedTimeDate = df.parse(getOrderStr(list2.get(6)));
						System.out.println("交易时间："+finishedTimeDate);
					}
					if(yes.equals("退款")){
						System.out.println("交易状态："+yes);
						System.out.println("退款订单号：" + getOrderStr(list2.get(22)));
						System.out.println("支付集订单号：" + getOrderStr(list2.get(2)));
						System.out.println("退款金额：" + getOrderStr(list2.get(25)));
						Date finishedTimeDate = df.parse(getOrderStr(list2.get(6)));
						System.out.println("交易时间："+finishedTimeDate);
					}
				}
			}
		}catch(Exception e){
			
		}
	
		
	}*/
	private static String getOrderStr(String str){
		return str.replace(",", "");
	}
}
