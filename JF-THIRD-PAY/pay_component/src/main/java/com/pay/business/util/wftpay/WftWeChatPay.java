package com.pay.business.util.wftpay;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.pay.business.util.DecimalUtil;

/**
 * 威富通微信支付相关类
 * @author Administrator
 *
 */
public class WftWeChatPay {
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
     * 威富通微信wap支付（wap支付，app也可以用）
     *
     * @param body       商品信息
     * @param orderid    唯一订单号
     * @param money      价格
     * @param ip         ip地址
     * @param openid     下单openid
     * @param notify_url 异步通知地址
     * @return
     */
    public static Map<String, String> pay(String body, String orderid, String money, String ip
    		,String appType,String mchId,String key,String dictName,Integer payType) {
        Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
           
            map.put("mch_id", mchId);
            
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",create_nonce_str().replaceAll("-", ""));
            
            // 商品描述
            map.put("body", body);
            
            // 商户订单号
            map.put("out_trade_no", orderid);
            
            // 总金额，单位是分
            map.put("total_fee", money);
            
            //客户端ip
            map.put("mch_create_ip", ip);
            
            // 异步通知地址
            map.put("notify_url", WftWechatConfig.NOTIFY_URL);
            
            //map.put("callback_url", "https://testpayapi.aijinfu.cn/page/pay/result.html");
            
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
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            String reqUrl = WftWechatConfig.REQ_URL;
            //System.out.println("reqUrl：" + reqUrl);
            
            System.out.println("reqParams:" + XmlUtils.parseXML(map));
            Map<String,String> resultMap = XmlUtils.post(reqUrl, map);
            String res = XmlUtils.toXml(resultMap);
            //System.out.println("请求结果：" + res);
            
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
     * 威富通微信支付订单查询
     * @param orderid
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
            map.put("nonce_str",create_nonce_str().replaceAll("-", ""));
            
            // 商户订单号
            map.put("out_trade_no", orderid);
            
            // 接口类型
            map.put("service", WftWechatConfig.QUERY_SERVICE);
            
            Map<String,String> params = SignUtils.paraFilter(map);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            String reqUrl = WftWechatConfig.REQ_URL;
            //System.out.println("reqUrl：" + reqUrl);
            
            //System.out.println("reqParams:" + XmlUtils.parseXML(map));
            Map<String,String> resultMap = XmlUtils.post(reqUrl, map);
            String res = XmlUtils.toXml(resultMap);
            System.out.println("请求结果：" + res);
            
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
    
    /**
     * 
     *	威富通退款
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
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",create_nonce_str().replaceAll("-", ""));
            
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
            map.put("service", WftWechatConfig.REFUND_SERVICE);
            
            Map<String,String> params = SignUtils.paraFilter(map);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            String reqUrl = WftWechatConfig.REQ_URL;
            //System.out.println("reqUrl：" + reqUrl);
            
            //System.out.println("reqParams:" + XmlUtils.parseXML(map));
            Map<String,String> resultMap = XmlUtils.post(reqUrl, map);
            String res = XmlUtils.toXml(resultMap);
            //System.out.println("请求结果：" + res);
            
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
     * 
     *	威富通查询退款
     * @param orderid  (订单号)
     * @param refundOrderNum (退款订单号)
     * @param money	(总金额)
     * @param refundFee (退款金额)
     * @return
     */
    public static Map<String,String> queryRefund(String refundOrderNum,String mchId,String key){
		Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
           
            map.put("mch_id", mchId);
           
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",create_nonce_str().replaceAll("-", ""));
            
            
            // 退款訂單
            map.put("out_refund_no", refundOrderNum);
            
            
            // 接口类型
            map.put("service", WftWechatConfig.QUERY_REFUND_SERVICE);
            
            Map<String,String> params = SignUtils.paraFilter(map);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            String reqUrl = WftWechatConfig.REQ_URL;
            //System.out.println("reqUrl：" + reqUrl);
            
            //System.out.println("reqParams:" + XmlUtils.parseXML(map));
            Map<String,String> resultMap = XmlUtils.post(reqUrl, map);
            String res = XmlUtils.toXml(resultMap);
            //System.out.println("请求结果：" + res);
            
            if(resultMap.containsKey("sign")){
                if(!SignUtils.checkParam(resultMap, key)){
                    return commonErrorMap;
                }else{
                    if("0".equals(resultMap.get("status"))&&"0".equals(resultMap.get("result_code"))){
                    	String out_trade_no = resultMap.get("out_trade_no");//商户订单号
                        
                        returnMap.put("out_trade_no", out_trade_no);
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
    
    public static String [] downloadOrder(String billDate,String mchId,String key){
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
           
            map.put("mch_id", mchId);
           
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",create_nonce_str().replaceAll("-", ""));
            
            
            // 接口类型
            map.put("service", WftWechatConfig.DOWNLOAD_SERVICE);
            
            // 账单日期
            map.put("bill_date", billDate);
            
            // 账单类型
            map.put("bill_type", WftWechatConfig.BILL_TYPE);
            
            
            Map<String,String> params = SignUtils.paraFilter(map);
            StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String sign = WftMD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            String reqUrl = WftWechatConfig.DOWNLOAD_URL;
            System.out.println("reqUrl：" + reqUrl);
            
            System.out.println("reqParams:" + XmlUtils.parseXML(map));
            String [] str = XmlUtils.downPost(reqUrl, map);
            return str;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 随机算法
     * @return
     */
    protected static String create_nonce_str() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    public static void main(String[] args) throws Exception {
    	//appPay("测试支付", "125461472589156", ""+DecimalUtil.yuanToCents("0.01"), "192.168.1.172","1");
    	//queryOrder("DD20170610142323682582897");
    	//refund("DD20170609194308048213532", "5654786325456", ""+DecimalUtil.yuanToCents("10"), ""+DecimalUtil.yuanToCents("10"));
    	//queryRefund("56496156");
    }
}
