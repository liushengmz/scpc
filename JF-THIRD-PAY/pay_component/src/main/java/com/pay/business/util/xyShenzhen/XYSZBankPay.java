package com.pay.business.util.xyShenzhen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.core.teamwork.base.util.encrypt.MD5;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.MapUtil;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.RandomUtil;
import com.pay.business.util.alipay.xyBank.XmlUtils;
import com.pay.business.util.wftpay.SignUtils;
import com.pay.business.util.wftpay.WftMD5;
import com.pay.business.util.wftpay.WftWechatConfig;
import com.pay.business.util.wftpay.weChatSubscrPay.swiftpassConfig.SwiftpassConfig;
import com.pay.business.util.xyBankWeChatPay.SslUtils;

public class XYSZBankPay {
	private static Logger log = Logger.getLogger(XYSZBankPay.class);
	
	private static final String FAIL_CODE = "code";
	private static final String FAIL_CODE_VALUE = "500";//错误状态码
	
	private static final String ERROR_KEY = "msg";//错误消息的key
	private static final String WX_ERROR_CODE = "wx_error_code";//微信错误的代码
	private static Map<String, String> commonErrorMap = new HashMap<String, String>();
	
	static{
		commonErrorMap.put(FAIL_CODE, FAIL_CODE_VALUE);
		commonErrorMap.put(ERROR_KEY, "服务器异常");
		commonErrorMap.put(WX_ERROR_CODE, "");
	}
	
	/**
	 * 兴业深圳公众号支付
	 * @param body	商品描述
	 * @param orderid	订单号
	 * @param money	金额分
	 * @param ip	
	 * @param openId	公众号openid
	 * @param appId		公众号appid
	 * @param mchId		
	 * @param key
	 */
	public static Map<String,String> xyszWXGzhPay(String body, String orderid, String money, String ip, String openId, String appId, String mchId, String key) throws Exception{
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> sginMap = new HashMap<String, String>();
        String sign = "";
        //随机字符串
        sginMap.put("nonce_str",new Date().getTime()+ RandomUtil.generateString(4));
        // 商品描述
        sginMap.put("body", body);
        // 商户订单号
        sginMap.put("out_trade_no", orderid);
        // 总金额，单位是分
        sginMap.put("total_fee", money);
        //客户端ip
        sginMap.put("mch_create_ip", ip);
        //openid
        sginMap.put("sub_openid", openId);
		//接口类型
        sginMap.put("service", XYBankConfig.WFT_GZH);
        //商户ID
        sginMap.put("mch_id",mchId);
        //异步回调URL
        sginMap.put("notify_url", XYBankConfig.notify_url);
        //
        sginMap.put("callback_url", SwiftpassConfig.CALLBACK_URL);
        //appid
        sginMap.put("sub_appid", appId);
        
        sign = signMd5(key, sginMap);
		sginMap.put("sign", sign);
		
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		try {
			String info = sendPost(XYBankConfig.WFT_url, xmlInfo);
			log.info("=====>兴业深圳公众号返回的预支付订单信息:\n" + info + " \n");
			System.out.println("=====>兴业深圳公众号返回的预支付订单信息:\n" + info + " \n");
			resultMap = XmlUtils.toMap(info);
			log.info("=====>转换为map值为：" + resultMap);
			System.out.println("=====>转换为map值为：" + resultMap);
			if (!SignUtils.checkParam(resultMap,key)) {
                resultMap.put("code","10001");
                resultMap.put("msg","失败原因："+"验证签名不通过");
                log.error("=====>兴业深圳公众号支付调起失败原因：验证签名不通过");
            } else {
                if ("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))) {
                	//组装可以访问的URL
                	String url=SwiftpassConfig.WFT_PAY_URL+resultMap.get("token_id");
                	resultMap.put("pay_url",url);
                    log.info("=====>兴业深圳公众号支付调起成功!访问的URL为："+url);
                }else{
                	resultMap.put("code","10001");
                	resultMap.put("msg","失败原因："+resultMap);
                	 log.error("=====>兴业深圳公众号支付调起失败原因："+resultMap);
                } 
            }
		} catch (Exception e) {
			log.error("=====>兴业深圳公众号支付操作失败，原因：", e);
			resultMap.put("code","10001");
			resultMap.put("msg", e.toString());
		}
		return resultMap;
	}
	
	/**
     * 兴业深圳微信wap支付（wap支付，app也可以用）
     * @param body       商品信息
     * @param orderid    唯一订单号
     * @param money      价格
     * @param ip         ip地址
     * @param openid     下单openid
     * @param notify_url 异步通知地址
     * @return
     */
    public static Map<String, String> xySZWFTWXWapPay(String body, String orderid, String money, String ip
    		,String appType,String mchId,String key, Integer payType) {
        Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
           
            map.put("mch_id", mchId);
            //随机字符串
            map.put("nonce_str",new Date().getTime()+ RandomUtil.generateString(4));
            // 商品描述
            map.put("body", body);
            // 商户订单号
            map.put("out_trade_no", orderid);
            // 总金额，单位是分
            map.put("total_fee", money);
            //客户端ip
            map.put("mch_create_ip", ip);
            // 异步通知地址
            map.put("notify_url", XYBankConfig.notify_url);
            //app支付(sdk)
            if(new Integer(1).equals(payType)){
            	// 接口类型
                map.put("service", WftWechatConfig.PAY_SERVICE_APP);
                //应用名
                map.put("mch_app_name", WftWechatConfig.APP_NAME);
                //安卓1
                if("1".equals(appType)){
                	//应用类型 安卓sdk
                	map.put("device_info", WftWechatConfig.AND_SDK);
                }else{
                	//应用类型 ios sdk
                	map.put("device_info", WftWechatConfig.IOS_SDK);
                }
                //应用标识
                map.put("mch_app_id", WftWechatConfig.APP_PAGEAGE);
            }else{//h5（wap）
            	// 接口类型
                map.put("service", WftWechatConfig.PAY_SERVICE_WAP);
                //应用名
                map.put("mch_app_name", WftWechatConfig.APP_NAME_WEB);
                //安卓1
                if("1".equals(appType)){
                	//应用类型 安卓sdk
                	map.put("device_info", WftWechatConfig.AND_WAP);
                }else{
                	//应用类型 ios sdk
                	map.put("device_info", WftWechatConfig.IOS_WAP);
                }
                //应用标识
                map.put("mch_app_id", WftWechatConfig.APP_PAGEAGE_WEB);
            }
            
            Map<String,String> params = SignUtils.paraFilter(map);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            /*String sign = signMd5(key, params);
            System.out.println(signMd5);*/
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            /*System.out.println(sign);*/
            map.put("sign", sign);
          
            
            Map<String, String> resultMap = com.pay.business.util.wftpay.XmlUtils.post(XYBankConfig.WFT_url, map);
            
            System.out.println("info2" + resultMap.toString());
            /*Map<String, String> resultMap = XmlUtils.toMap(info);*/
            
            if(resultMap.containsKey("sign")){
                if(!SignUtils.checkParam(resultMap, key)){
                    return commonErrorMap;
                }else{
                    if("0".equals(resultMap.get("status"))){
                    	//app支付(sdk)
                        if(new Integer(1).equals(payType)){
                        	String token_id = resultMap.get("token_id");
                            String services = resultMap.get("services");
                            returnMap.put("token_id", token_id);
                            returnMap.put("services", services);
                        }else{
                        	String pay_info = resultMap.get("pay_info");
                            returnMap.put("pay_info", pay_info);
                        }
                        return returnMap;
                    }else{
                    	return commonErrorMap;
                    }
                }
            } else{
            	return commonErrorMap;
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
        	return commonErrorMap;
        }
    }
	
	/**
	 * 兴业深圳微信扫码支付
	 * @param out_trade_no	商户订单号
	 * @param total_fee	总金额
	 * @param body	商品描述
	 * @param ip	终端IP
	 * @param mch_id	商户号
	 * @param key
	 * @return
	 */
	public static Map<String, String> xySZWFTWXScanPay(String out_trade_no,String total_fee, String body, String ip, String mch_id, String key) {
		Map<String, String> resultMap = new HashMap<String, String>();
		//随机字符串
		String nonce_str=new Date().getTime()+ RandomUtil.generateString(4);
		//签名
		String sign="";
		// 签名
		Map<String, String> sginMap = new HashMap<String, String>();
		sginMap.put("service", XYBankConfig.WFTmethod_WX);
		sginMap.put("mch_id", mch_id);
		sginMap.put("nonce_str", nonce_str);
		sginMap.put("body", body);
		sginMap.put("mch_create_ip", ip);
		sginMap.put("out_trade_no", out_trade_no);
		sginMap.put("total_fee",total_fee);
		sginMap.put("notify_url", XYBankConfig.notify_url);
		sign = signMd5(key, sginMap);
		sginMap.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=====>提交参数转换为XML格式：\n" + xmlInfo);
		try {
			String info = sendPost(XYBankConfig.WFT_url, xmlInfo);
			log.info("=====>兴业银行威富通返回的预支付订单信息:\n" + info + " \n");
			System.out.println("=====>兴业银行威富通返回的预支付订单信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			log.info("=====>转换为map值为：" + toxmlMap);
			System.out.println("=====>转换为map值为：" + toxmlMap);
			if ("0".equals(toxmlMap.get("status")) && "0".equals(toxmlMap.get("result_code"))) {
				resultMap.put("qr_code", toxmlMap.get("code_url"));
				resultMap.put("code","10000");
				resultMap.put("out_trade_no",out_trade_no);
				log.info("=====>兴业银行支付宝威富通扫码支付调起成功:qr_code : " + toxmlMap.get("code_url"));
				System.out.println("=====>兴业银行支付宝威富通扫码支付调起成功:qr_code : " + toxmlMap.get("code_url"));
			} else {
				resultMap.put("code","10001");
				resultMap.put("msg", info);
				log.error("=====>兴业银行支付宝威富通扫码支付失败原因:" + info);
				System.out.println("=====>兴业银行支付宝威富通扫码支付失败原因:" + info);
			}
		} catch (Exception e) {
			log.error("=====>兴业银行支付宝威富通扫码支付操作失败，原因：", e);
			resultMap.put("code","10001");
			resultMap.put("msg", e.toString());
		}
		return resultMap;
	}
	
	/**
	 * 兴业深圳支付宝扫码支付
	 * @param out_trade_no	商户订单号
	 * @param total_fee	总金额
	 * @param body	商品描述
	 * @param ip	终端IP
	 * @param mch_id	商户号
	 * @param key
	 * @return
	 */
	public static Map<String, String> xySZWFTAliaScanPay(String out_trade_no,String total_fee, String body, String ip, String mch_id, String key) {
		Map<String, String> resultMap = new HashMap<String, String>();
		//随机字符串
		String nonce_str=new Date().getTime()+ RandomUtil.generateString(4);
		//签名
		String sign="";
		// 签名
		Map<String, String> sginMap = new HashMap<String, String>();
		sginMap.put("service", XYBankConfig.WFTmethod_ZFB);	//接口类型
		sginMap.put("mch_id", mch_id);
		sginMap.put("nonce_str", nonce_str);
		sginMap.put("body", body);
		sginMap.put("mch_create_ip", ip);
		sginMap.put("out_trade_no", out_trade_no);
		sginMap.put("total_fee",total_fee);
		sginMap.put("notify_url", XYBankConfig.notify_url);
		sign = signMd5(key, sginMap);
		sginMap.put("sign", sign);
		SortedMap<String, String> xmlMap = new TreeMap<String, String>(sginMap);
		String xmlInfo = XmlUtils.parseXML(xmlMap);
		log.info("=====>提交参数转换为XML格式：\n" + xmlInfo);
		try {
			String info = sendPost(XYBankConfig.WFT_url, xmlInfo);
			log.info("=====>兴业银行威富通返回的预支付订单信息:\n" + info + " \n");
			System.out.println("=====>兴业银行威富通返回的预支付订单信息:\n" + info + " \n");
			Map<String, String> toxmlMap = XmlUtils.toMap(info);
			log.info("=====>转换为map值为：" + toxmlMap);
			System.out.println("=====>转换为map值为：" + toxmlMap);
			if ("0".equals(toxmlMap.get("status")) && "0".equals(toxmlMap.get("result_code"))) {
				resultMap.put("qr_code", toxmlMap.get("code_url"));
				resultMap.put("code","10000");
				resultMap.put("out_trade_no",out_trade_no);
				log.info("=====>兴业银行支付宝威富通扫码支付调起成功:qr_code : " + toxmlMap.get("code_url"));
				System.out.println("=====>兴业银行支付宝威富通扫码支付调起成功:qr_code : " + toxmlMap.get("code_url"));
			} else {
				resultMap.put("code","10001");
				resultMap.put("msg", info);
				log.error("=====>兴业银行支付宝威富通扫码支付失败原因:" + info);
				System.out.println("=====>兴业银行支付宝威富通扫码支付失败原因:" + info);
			}
		} catch (Exception e) {
			log.error("=====>兴业银行支付宝威富通扫码支付操作失败，原因：", e);
			resultMap.put("code","10001");
			resultMap.put("msg", e.toString());
		}
		return resultMap;
	}
	
	/**
	 * 兴业深圳账单下载
	 */
	public static String [] xyszOderFind(String billDate,String mchId,String key){
		try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
           
            map.put("mch_id", mchId);
           
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",new Date().getTime()+ RandomUtil.generateString(4));
            
            
            // 接口类型
            map.put("service", XYBankConfig.DOWNLOAD_SERVICE);
            
            // 账单日期
            map.put("bill_date", billDate);
            
            // 账单类型
            map.put("bill_type", XYBankConfig.BILL_TYPE);
            
            
            Map<String,String> params = SignUtils.paraFilter(map);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            
            System.out.println("reqParams:" + XmlUtils.parseXML(map));
            String [] str = downPost(XYBankConfig.DOWNLOAD_URL, map);
            return str;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
	}
	
	/**
     *	兴业深圳退款
     * @param orderid  (订单号)
     * @param refundOrderNum (退款订单号)
     * @param money	(总金额)
     * @param refundFee (退款金额)
     * @return
     */
    public static Map<String,String> refund(String orderid,String refundOrderNum, String money
    		,String refundFee,String mchId,String key){
		Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
           
            map.put("mch_id", mchId);
            //随机字符串
            map.put("nonce_str",new Date().getTime()+ RandomUtil.generateString(4));
            // 商户订单号
            map.put("out_trade_no", orderid);
            // 退款訂單
            map.put("out_refund_no", refundOrderNum);
            // 总金额，单位是分
            map.put("total_fee", money);
            // 退款金额，单位是分
            map.put("refund_fee", refundFee);
            //操作员帐号, 默认为商户号
            map.put("op_user_id", mchId);
            // 接口类型
            map.put("service", XYBankConfig.REFUND_SERVICE);
            
            Map<String,String> params = SignUtils.paraFilter(map);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            //System.out.println("reqUrl：" + reqUrl);
            
            String info = sendPost(WftWechatConfig.REQ_URL, XmlUtils.parseXML(map));
            Map<String, String> resultMap = XmlUtils.toMap(info);
            
            if(resultMap.containsKey("sign")){
                if(!SignUtils.checkParam(resultMap, key)){
                    return commonErrorMap;
                }else{
                    if("0".equals(resultMap.get("status"))&&"0".equals(resultMap.get("result_code"))){
                    	String out_trade_no = resultMap.get("out_trade_no");//商户订单号
						//String trade_status = wechatMap.get("trade_state");//交易状态
						String transaction_id = resultMap.get("transaction_id");//支付金额
						String refund_fee = resultMap.get("refund_fee");//退款金额
                        
						returnMap.put("refund_fee", refund_fee);
                        returnMap.put("out_trade_no", out_trade_no);
                        returnMap.put("transaction_id", transaction_id);
                        return returnMap;
                    }else{
                    	return commonErrorMap;
                    }
                }
            } else{
            	return commonErrorMap;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return commonErrorMap;
        }
	}
	
    /**
     * 兴业深圳订单查询
     * @param orderid
     * @param mchId
     * @param key
     * @return
     */
    public static Map<String,String> queryOrder(String orderid,String mchId,String key){
    	Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
    	try {
    		 // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
            map.put("mch_id", mchId);
           
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str", new Date().getTime()+ RandomUtil.generateString(4));
            
            // 商户订单号
            map.put("out_trade_no", orderid);
            
            // 接口类型
            map.put("service", XYBankConfig.QUERY_SERVICE);
            
            Map<String,String> params = SignUtils.paraFilter(map);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            
            String info = sendPost(WftWechatConfig.REQ_URL, XmlUtils.parseXML(map));
            Map<String, String> resultMap = XmlUtils.toMap(info);
            
            if(resultMap.containsKey("sign")){
                if(!SignUtils.checkParam(resultMap, key)){
                    return commonErrorMap;
                }else{
                    if("0".equals(resultMap.get("status"))&&"0".equals(resultMap.get("result_code"))
                    		&&("SUCCESS".equals(resultMap.get("trade_state"))
                    		||"REFUND".equals(resultMap.get("trade_state")))){
                    	String out_trade_no = resultMap.get("out_trade_no");//商户订单号
						//String trade_status = wechatMap.get("trade_state");//交易状态
                    	String centsToYuan = DecimalUtil.centsToYuan(resultMap.get("total_fee"));
						//String total_fee = resultMap.get("total_fee");//支付金额
						String transaction_id = resultMap.get("transaction_id");//支付金额
                        
                        returnMap.put("out_trade_no", out_trade_no);
                        returnMap.put("total_fee", centsToYuan);
                        returnMap.put("transaction_id", transaction_id);
                        return returnMap;
                    }else{
                    	return commonErrorMap;
                    }
                }
            } else{
            	return commonErrorMap;
            }
		} catch (Exception e) {
			e.printStackTrace();
        	return commonErrorMap;
		}
    }
    
	public static void main(String[] args) {
		/*String[] xyszOderFind = xyszOderFind("20170814", "101580128398", "e6125eed9c516dd01fd382d569787e65");
		for (String string : xyszOderFind) {
			System.out.println(string);
		}*/
		/*Map<String, String> refund = refund("DD2017081616104425817573", "12314512", "1", "1", "101580128398", "e6125eed9c516dd01fd382d569787e65");
		System.out.println(refund.toString());*/
		
		/*
		Map<String, String> queryOrder = queryOrder("DD2017081616104425817573", "101580128398", "e6125eed9c516dd01fd382d569787e65");
		System.out.println(queryOrder.toString());
		*/
	}
	
	private void testPay()
	{
		//xyszWXGzhPay(body, orderid, money, ip, openId, appId, mchId, key)
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
	
	public static String [] downPost(String reqUrl,SortedMap<String,String> map){
    	CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        try {
        	HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            //httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                String [] str = toStr(response.getEntity());
                return str;
            }else{
                return null;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
	
    public static String [] toStr(HttpEntity entity){
    	try {
            //getResponse    
            InputStream in=entity.getContent();    
            int count = 0;    
            while (count == 0) {    
             count = Integer.parseInt(""+entity.getContentLength());//in.available();    
            }    
            byte[] bytes = new byte[count];    
            int readCount = 0; // 已经成功读取的字节的个数    
            while (readCount <= count) {    
             if(readCount == count)break;    
             readCount += in.read(bytes, readCount, count - readCount);    
            }    
                
            //转换成字符串    
            String readContent= new String(bytes, 0, readCount, "UTF-8"); // convert to string using bytes    

            System.out.println("2.Get Response Content():\n"+readContent);
            
            String[] str = readContent.split("\n");
            
            return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return null;
    }
}
