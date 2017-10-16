package com.pay.business.util.xyBankWeChatPay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.core.teamwork.base.util.encrypt.MD5;
import com.pay.business.util.MapUtil;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.RandomUtil;
import com.pay.business.util.alipay.xyBank.XmlUtils;

/**
 * @Title: IndustrialBankPay.java
 * @Package com.pay.business.util.IndustrialBankPay
 * @Description: 兴业银行支付通道
 * @author ZHOULIBO
 * @date 2017年7月6日 下午3:37:18
 * @version V1.0
 */
public class XyBankPay {
	private static Logger log = Logger.getLogger(XyBankPay.class);
	
	
	

	/**
	 * xYBankWechatGzhPay 
	 * 兴业银行微信公众号支付/兴业银行扫码支付
	 * @param orderNum 			商户订单号
	 * @param ip 				ip地址
	 * @param totalFee 			金额
	 * @param body 				商品名字
	 * @param appid 			appid
	 * @param mch_id  			商户id
	 * @param key 				商户key
	 * @return
	 * @throws Exception    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> xYBankWechatGzhPay(String orderNum, String ip, String totalFee, String body, String appid, String mch_id, String key,String wx_appid,String openid,int fromType)
			throws Exception {
		// 附加数据
		String attach = XyBankWechatPayConfig.attach;
		// 随机数
		String nonce_str = String.valueOf(new Date().getTime());
		// 异步返回URL
		String notify_url = XyBankWechatPayConfig.notify_url;
		// 支付方式
		String trade_type ="";
		if(fromType==1){//公众号支付
			trade_type = XyBankWechatPayConfig.JSAPI;
		}
		if(fromType==2){//微信扫码支付
			trade_type = XyBankWechatPayConfig.NATIVE;
		}
		// 签名
		Map<String, String> sginMap = new HashMap<String, String>();
		sginMap.put("appid", appid);
		sginMap.put("attach", attach);
		sginMap.put("body", body);
		sginMap.put("mch_id", mch_id);
		sginMap.put("nonce_str", nonce_str);
		sginMap.put("notify_url", notify_url);
		sginMap.put("out_trade_no", orderNum);
		sginMap.put("spbill_create_ip", ip);
		sginMap.put("total_fee",totalFee);
		sginMap.put("trade_type", trade_type);
		//公众号需要的字段
		if(fromType==1){
			sginMap.put("wx_appid",wx_appid);
			sginMap.put("openid",openid);
		}
		String sign = signMd5(key, sginMap);
		log.info("=====>提交参数加密结果为：" + sign);
		sginMap.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=====>提交参数转换为XML格式为：" + xmlInfo);
		String res = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			String info = sendPost(XyBankWechatPayConfig.req_url, xmlInfo);
			log.info("=====>兴业银行返回的预支付订单信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			log.info("=====>转换为map值为：" + toxmlMap);
			if ("SUCCESS".equals(toxmlMap.get("return_code")) && "SUCCESS".equals(toxmlMap.get("result_code"))) {
				//公众号支付
				if(fromType==1){
					resultMap.put("jsapi_appid", toxmlMap.get("jsapi_appid"));
					resultMap.put("jsapi_timestamp", toxmlMap.get("jsapi_timestamp"));
					resultMap.put("jsapi_noncestr", toxmlMap.get("jsapi_noncestr"));
					resultMap.put("jsapi_package", toxmlMap.get("jsapi_package"));
					resultMap.put("jsapi_signtype", toxmlMap.get("jsapi_signtype"));
					resultMap.put("jsapi_paysign", toxmlMap.get("jsapi_paysign"));
					resultMap.put("code","10000");
					log.info("=====>兴业银行微信公众号支付调起成功");
				}
				//扫码支付
				if(fromType==2){
					resultMap.put("code","10000");
					resultMap.put("qr_code",toxmlMap.get("code_url"));
					resultMap.put("out_trade_no",orderNum);
					log.info("======》兴业银行微信扫码支付成功");
				}
			} else {
				resultMap.put("code","10001");
				resultMap.put("msg", info);
				log.error("=====>兴业银行微信公众号/扫码支付失败原因:" + info);
			}
		} catch (Exception e) {
			log.error("=====>兴业银行微信公众号/扫码支付操作失败，原因：", e);
			res = "系统异常";
			resultMap.put("code","10001");
			resultMap.put("msg", res);
		}
		return resultMap;
	}
	
	private static void andyTest1()
	{
		//("DD" + System.currentTimeMillis(), "119.137.34.36", 10, "GOOD PRODUCT", "101590135078", "3db2491490a21b8fc7bbb5229bd2b6d0", "", null, null, 2);
		try
		{
			Map<String, String> ss = xYBankWechatGzhPay(
					"DD" + System.currentTimeMillis(), "119.137.34.36", "40",
					"GOOD PRODUCT", "101590135078",
					"3db2491490a21b8fc7bbb5229bd2b6d0", "", null, null, 2);
			System.out.println(ss);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		andyTest1();
	}
	

	/**
	 * xyBankGzhRefund 
	 * 兴业银行-微信公众号支付-申请退款
	 * @param out_trade_no  	商户订单号
	 * @param appid 			appid
	 * @param mch_id 			商户号
	 * @param key 				商户key
	 * @param refund_fee		 退款金额
	 * @param total_fee 		总金额
	 * @param op_user_id 		操作人
	 * @param out_refund_no 	退款订单号
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String,String> xyBankGzhRefund(String out_trade_no, String appid, String mch_id, String key, String refund_fee,String total_fee, String op_user_id,
			String out_refund_no){
		Map<String,String> response=new HashMap<String, String>();
		// 随机字符串
		String nonce_str = String.valueOf(new Date().getTime());
		// 签名
		String sign = "";
		// 签名
		Map<String, String> sginMap = new HashMap<String, String>();
		sginMap.put("appid", appid);
		sginMap.put("mch_id", mch_id);
		sginMap.put("nonce_str", nonce_str);
		sginMap.put("out_trade_no", out_trade_no);
		sginMap.put("refund_fee", refund_fee);
		sginMap.put("total_fee", total_fee);
		sginMap.put("op_user_id", mch_id);
		sginMap.put("out_refund_no", out_refund_no);
		sign = signMd5(key, sginMap);
		sginMap.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=====>提交参数转换为XML格式：\n" + xmlInfo);
		try {
			String info = sendPost(XyBankWechatPayConfig.refund_url, xmlInfo);
			log.info("=====>兴业银行-微信公众号支付-申请退款订单信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			if ("SUCCESS".equals(toxmlMap.get("return_code")) && "SUCCESS".equals(toxmlMap.get("result_code"))) {
					response.put("code", "10000");
					log.info("=====>兴业银行-微信公众号支付-申请退款成功");
			} else {
				response.put("code", "10001");
				response.put("msg", "兴业银行-微信公众号支付-申请退款接口失败："+toxmlMap);
				log.error("=====>兴业银行-微信公众号支付-申请退款失败：原因为：" + toxmlMap);
			}
		} catch (Exception e) {
			log.error("=====>兴业银行-微信公众号支付-申请退款失败：原因为：" + e);
			response.put("code", "10001");
			response.put("msg", "兴业银行-微信公众号支付-申请退款失败："+e);
		}
		return response;
		
	}
	// MD5字符串拼接加密
	public static String signMd5(String key, Map<String, String> map) {
		Map<String, Object> sginMap = new HashMap<String, Object>();
		String newMap = MapUtil.mapToJson(map);
		sginMap = MapUtil.parseJsonToMap(newMap);
		String sgin = PaySignUtil.getParamStr(sginMap);
		sgin = sgin + "&key=" + key;
		log.info("=======》兴业银行微信公众号支付参数加密拼接为：" + sgin);
		return MD5.GetMD5Code(sgin).toUpperCase();
	}

	/**
	 * checkParam 
	 * 验证返回参数 
	 * @param params
	 * @param key
	 * @return    设定文件 
	 * boolean    返回类型
	 */
	public static boolean checkParam(Map<String, String> params, String key) {
		boolean result = false;
		if (params.containsKey("sign")) {
			String sign = params.get("sign");
			log.info("=======》兴业银行微信公众号支付返回参数自带加密参数为：" + sign);
			params.remove("sign");
			params.remove("return_msg");
			params.remove("device_info");
			params.remove("err_code");
			params.remove("err_code_des");
			params.remove("coupon_fee");
			String signRecieve =getParamStr(params);
			signRecieve=signRecieve+"&key=" + key;
			log.info("=======>参与加密的参数进行字符串拼接为："+signRecieve);
			String newSgin=MD5.GetMD5Code(signRecieve).toUpperCase();
			log.info("=======》兴业银行微信公众号支付返回参数加密结果为：" + newSgin);
			result = sign.equalsIgnoreCase(newSgin);
			log.info("=======》兴业银行回调数据验签结果为："+result);
		}
		return result;
	}
	
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
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
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
	/**
	 * xyBankcheck 
	 * 兴业银行-支付宝扫码支付：结果回调
	 * @param params
	 * @param key
	 * @return    设定文件 
	 * boolean    返回类型
	 */
	public static boolean xyBankcheck(Map<String, String> params, String key) {
		boolean result = false;
		if (params.containsKey("sign")) {
			String sign = params.get("sign");
			log.info("=======》兴业银行微信公众号支付返回参数自带加密参数为：" + sign);
			params.remove("sign");
//			Map<String, Object> sginMap = new HashMap<String, Object>();
//			String newMap = MapUtil.mapToJson(params);
//			sginMap = MapUtil.parseJsonToMap(newMap);
			String signRecieve =getParamStr(params);
			signRecieve=signRecieve+"&key=" + key;
			log.info("=======>参与加密的参数进行字符串拼接为："+signRecieve);
			String newSgin=MD5.GetMD5Code(signRecieve).toUpperCase();
			log.info("=======》兴业银行微信公众号支付返回参数加密结果为：" + newSgin);
			result = sign.equalsIgnoreCase(newSgin);
			log.info("=======》兴业银行回调数据验签结果为："+result);
		}
		return result;
	}
	
	/**
	 * 参数签名串拼接
	 * @param map
	 * @return
	 */
	public static String getParamStr(Map<String,String> map){
		StringBuffer buffer = new StringBuffer();
		List<String> keys = new ArrayList<String>(map.keySet());
		//排序
        Collections.sort(keys);
		//参数值拼接进行加密
        for (int i = 0; i < keys.size(); i++) {
        	String key = keys.get(i);
			if(!"sign".equals(key)&&!"keyValue".equals(key)){
				String value = map.get(key)==null?"":map.get(key).toString();
				if(i==0){
					buffer.append(key + "=" + value);
				}else{
					buffer.append("&"+key + "=" + value);
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * xyWechatWebPay 
	 * 兴业银行微信H5支付
	 * @param appid 商户APPID
	 * @param mch_id 商户 mch_id
	 * @param key  商户 key
	 * @param body 商品名字
	 * @param out_trade_no 商户订单号
	 * @param total_fee 金额
	 * @param spbill_create_ip ip地址
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String,String> xyWechatWebPay(String appid,String mch_id,String key,String body,String out_trade_no,int total_fee,String spbill_create_ip){
		/**
		 * 此接口暂无测试2017.07.13
		 */
		Map<String, String> resultMap = new HashMap<String, String>();
		//交易类型
		String trade_type="MWEB";
		//附加数据
		String attach=XyBankWechatPayConfig.attach;
		//支付结果异步通知
		String notify_url=XyBankWechatPayConfig.notify_url;
		//随机数
		String nonce_str=RandomUtil.generateString(13);
		Map<String, String> signMpa=new HashMap<String, String>();
		signMpa.put("appid", appid);
		signMpa.put("mch_id", mch_id);
		signMpa.put("body", body);
		signMpa.put("out_trade_no", out_trade_no);
		signMpa.put("total_fee", String.valueOf(total_fee));
		signMpa.put("spbill_create_ip", spbill_create_ip);
		signMpa.put("attach", attach);
		signMpa.put("notify_url", notify_url);
		signMpa.put("nonce_str", nonce_str);
		signMpa.put("trade_type", trade_type);
		String signInfo=getParamStr(signMpa);
		signInfo=signInfo+"&key="+key;
		log.info("=======>参与兴业银行微信H5支付加密的参数进行字符串拼接为："+signInfo);
		//加密
		String sign=MD5.GetMD5Code(signInfo).toUpperCase();
		log.info("=======>参与兴业银行微信H5支付加密的为："+sign);
		signMpa.put("sign",sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(signMpa);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=======>提交参数转换为XML格式：\n" + xmlInfo);
		String res = null;
		try {
			//提交
			String info = sendPost(XyBankWechatPayConfig.req_url, xmlInfo);
			log.info("=======>兴业银行返回的预支付订单信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			log.info("=======>转换为map值为：" + toxmlMap);
			if ("SUCCESS".equals(toxmlMap.get("return_code")) && "SUCCESS".equals(toxmlMap.get("result_code"))) {
				resultMap.put("address", toxmlMap.get("mweb_url"));
				resultMap.put("code","10000");
				log.info("=======>兴业银行微信H5支付调起成功");
			} else {
				res = "操作失败";
				resultMap.put("code","10001");
				resultMap.put("msg", info);
				log.error("=======>兴业银行微信H5支付调起失败原因:" + info);
			}
		} catch (Exception e) {
			log.error("=======>兴业银行微信H5支付调起失败原因：", e);
			res = "系统异常";
			resultMap.put("code","10001");
			resultMap.put("msg", res);
		}
		return resultMap;
	}

	/**
	 * xyBankweChatOrderQuery 
	 * 兴业银行：微信支付：订单查询 
	 * @param appid 			商户appid
	 * @param mch_id 			商户号
	 * @param key 				商户key
	 * @param out_trade_no 		订单号
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> xyBankweChatOrderQuery(String appid,String mch_id,String key,String out_trade_no){
		Map<String, String> resultMap = new HashMap<String, String>();
		// 随机数
		String nonce_str =new Date().getTime()+ RandomUtil.generateString(4);
		Map<String, String> signMpa = new HashMap<String, String>();
		signMpa.put("appid", appid);
		signMpa.put("mch_id", mch_id);
		signMpa.put("out_trade_no", out_trade_no);
		signMpa.put("nonce_str", nonce_str);
		String signInfo = getParamStr(signMpa);
		signInfo = signInfo + "&key=" + key;
		log.info("=======>参与兴业银行：微信支付：订单查询参数进行字符串拼接为：" + signInfo);
		// 加密
		String sign = MD5.GetMD5Code(signInfo).toUpperCase();
		log.info("=======>参与兴业银行：微信支付：订单查询加密的结果为：" + sign);
		signMpa.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(signMpa);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=======>参与兴业银行：微信支付：订单查询参数转换为XML格式：\n" + xmlInfo);
		String res = null;
		try {
			// 提交
			String info = sendPost(XyBankWechatPayConfig.order_url, xmlInfo);
			log.info("=======>参与兴业银行：微信支付：订单查询返回信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			log.info("=======>参与兴业银行：微信支付：订单查询转换为map值为：" + toxmlMap);
			if ("SUCCESS".equals(toxmlMap.get("return_code")) && "SUCCESS".equals(toxmlMap.get("result_code"))) {
				//trade_state状态:
				//SUCCESS--支付成功
				//REFUND--转入退款
				//NOTPAY--未支付
				//CLOSED--已关闭
				//REVOKED--已撤销（刷卡支付）
				//USERPAYING--用户支付中
				//NOPAY--未支付(确认支付超时)
//				PAYERROR--支付失败(其他原因，如银 行返回失败)
				String trade_state=toxmlMap.get("trade_state");
				switch (trade_state) {
				case "SUCCESS":
					resultMap.put("payStatus","1");//成功
					break;
				case "REFUND":
					resultMap.put("payStatus","1");//转入退款 之前消费成功：视为订单成功
					break;
				case "NOTPAY":
					resultMap.put("payStatus","3");//未支付
					break;	
				case "CLOSED":
					resultMap.put("payStatus","7");//已关闭
					break;	
				case "USERPAYING":
					resultMap.put("payStatus","8");//支付中
					break;
				case "NOPAY":
					resultMap.put("payStatus","4");//超时
					break;
				case "PAYERROR":
					resultMap.put("payStatus","2");//支付失败
					break;	
				default:
					break;
				}
				resultMap.put("code","10000");
				log.info("=======>参与兴业银行：微信支付：订单查询状态：" + trade_state);
			} else {
				res = "操作失败";
				resultMap.put("code", "10001");
				resultMap.put("msg", info);
				log.error("=======>兴业银行：微信支付：订单查询失败原因:" + info);
			}
		} catch (Exception e) {
			log.error("业银行：微信支付：订单查询失败原因：", e);
			res = "系统异常";
			resultMap.put("code", "10001");
			resultMap.put("msg", res);
		}
		return resultMap;
		
	}
	/**
	 * xyWechatStatement 
	 * 兴业银行-微信公众号，微信扫码-对账单查询 
	 * @param appid
	 * @param mch_id
	 * @param key
	 * @param bill_date 对账时间
	 * @return    设定文件 
	 * String[]    返回类型
	 */
	public static String[] xyWechatStatement(String appid,String mch_id,String key,String bill_date){
		//随机数
		String nonce_str=new Date().getTime()+ RandomUtil.generateString(4);
		//加密
		Map<String, String> signMpa = new HashMap<String, String>();
		signMpa.put("appid", appid);
		signMpa.put("mch_id", mch_id);
		signMpa.put("bill_date", bill_date);
		signMpa.put("nonce_str", nonce_str);
		String signInfo = getParamStr(signMpa);
		signInfo = signInfo + "&key=" + key;
		log.info("=======>参与兴业银行-微信对账单查询参数进行字符串拼接为：" + signInfo);
		// 加密
		String sign = MD5.GetMD5Code(signInfo).toUpperCase();
		log.info("=======>参与兴业银行-微信对账单查询加密的结果为：" + sign);
		signMpa.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(signMpa);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		System.out.println("=======>参与兴业银行-微信对账单查询参数转换为XML格式：\n" + xmlInfo);
		try{
			String info = sendPost(XyBankWechatPayConfig.STATEMENT_URL, xmlInfo);
			int yes=info.indexOf("<xml>");
			if(yes<0){
				log.info("=====>参与兴业银行-微信对账单查询结果为：" + info + " \n");
				String str[] = info.split("`");
				return str;
			}else{
				log.error("=====>参与兴业银行-微信对账单查询结果失败原因:"+info);
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("=====>参与兴业银行-微信对账单查询结果失败原因："+e);
		}
		return null;
	}
	
	public static void testOri()
	{
		//订单查x
//		xyBankweChatOrderQuery("a20170628000004866", "m20170628000004866", "68c9276022648432996a685a71333c25", "DD2017071211533023733918");
		
//		String str[]=xyWechatStatement("a20170628000004866", "m20170628000004866", "68c9276022648432996a685a71333c25", "20170712");

//		String vv="交易时间,公众账号ID,商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,代金券或立减优惠金额,微信退款单号,商户退款单号,退款金额,代金券或立减优惠退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率`2017-07-07 18:24:48,`a20170628000004866,`m20170628000004866,`,`4001882001201707079384130108,`DD20170707182118007834988,`ogGWTvylhPoXYh21FXuELkBJQkzM,`NATIVE,`SUCCESS,`CFT,`CNY,`1,`0,`0,`,`0,`0,`,`,`公众号支付测试,``store_appid=s20170706000003611#store_name=金服公众号#op_user=,`0,`0.0038`2017-07-07 16:54:39,`a20170628000004866,`m20170628000004866,`,`4001882001201707079374558124,`DD20170707165050332337594,`ogGWTvylhPoXYh21FXuELkBJQkzM,`NATIVE,`SUCCESS,`CFT,`CNY,`1,`0,`0,`,`0,`0,`,`,`公众号支付测试,``store_appid=s20170706000003611#store_name=金服公众号#op_user=,`0,`0.0038`2017-07-07 17:14:36,`a20170628000004866,`m20170628000004866,`,`4001882001201707079378741725,`DD20170707171106827913525,`ogGWTvylhPoXYh21FXuELkBJQkzM,`NATIVE,`SUCCESS,`CFT,`CNY,`1,`0,`0,`,`0,`0,`,`,`公众号支付测试,``store_appid=s20170706000003611#store_name=金服公众号#op_user=,`0,`0.0038`2017-07-07 17:37:36,`a20170628000004866,`m20170628000004866,`,`4001882001201707079379552081,`DD20170707173413675241395,`ogGWTvylhPoXYh21FXuELkBJQkzM,`NATIVE,`SUCCESS,`CFT,`CNY,`1,`0,`0,`,`0,`0,`,`,`公众号支付测试,``store_appid=s20170706000003611#store_name=金服公众号#op_user=,`0,`0.0038`2017-07-07 18:01:05,`a20170628000004866,`m20170628000004866,`,`4001882001201707079381957190,`DD20170707175728870737422,`ogGWTvylhPoXYh21FXuELkBJQkzM,`NATIVE,`SUCCESS,`CFT,`CNY,`1,`0,`0,`,`0,`0,`,`,`公众号支付测试,``store_appid=s20170706000003611#store_name=金服公众号#op_user=,`0,`0.0038总交易单数,总交易额,总退款金额,总代金券或立减优惠退款金额,手续费总金额`5,`5,`0,`0,`0";
		
		
		
//		String str[] = vv.split("`");
		String str[]=null;
		if(str!=null){
			System.out.println("ss");
		}
				
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> list1 = new ArrayList<String>();
		
		int num = str.length/24;
		
		for (int i = 1; i < str.length; i++) {
			if(i%24==0){
				list1.add(str[i]);
				list.add(list1);
				if(i==num*24){
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
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (List<String> list2 : list) {
//				System.out.println(list2);
				for (int i = 0; i < list2.size(); i++) {
					String yes = getOrderStr(list2.get(i));
					if (yes.equals("SUCCESS")) {
						// 获取订单号
						System.out.println("交易状态："+yes);
						System.out.println("支付集订单号：" + getOrderStr(list2.get(5)));
						System.out.println("总金额：" + getOrderStr(list2.get(11)));
						Date finishedTimeDate = df.parse(getOrderStr(list2.get(0)));
						System.out.println("交易时间："+finishedTimeDate);
					}
					//退款
					if (yes.equals("REFUND")) {
						System.out.println("交易状态："+yes);
						System.out.println("退款订单号：" + getOrderStr(list2.get(14)));
						System.out.println("支付集订单号：" + getOrderStr(list2.get(5)));
						System.out.println("退款金额：" + getOrderStr(list2.get(15)));
						Date finishedTimeDate = df.parse(getOrderStr(list2.get(0)));
						System.out.println("交易时间："+finishedTimeDate);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*int indexbegin=vv.indexOf("率");
        int indexend=vv.indexOf("总交易单数");
 
        System.out.println("<TrmSeqNum>的位置==="+indexbegin);
        System.out.println("</TrmSeqNum>的位置==="+indexend);
        System.out.println("流水号=="+vv.substring(indexbegin+1, indexend)); //10是<TrmSeqNum>这个字符串的长度
        String vvv=    vv.substring(indexbegin+1, indexend);
        String []   vvvss= vvv.split("`");
        String []   vvaavss= vv.split("\n");*/
//		String jieguo = vv.substring(vv.indexOf("率")+1,vv.indexOf("总"));
//		System.out.println(jieguo);
	}
	
	
	private static String getOrderStr(String str){
		return str.replace(",", "");
	}
}
