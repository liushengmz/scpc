package com.pay.business.util.wxpay;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.pay.business.util.DecimalUtil;

/**
 * 微信支付
 * @ClassName: WeChatPay 
 * @Description: 
 * @author yangyu
 * @date 2016年11月11日 下午5:14:56
 */
public class WeChatPay {

	private static Logger logger = Logger.getLogger(WeChatPay.class);
	
	private static final String SUCCESS = "success";
	
	private static final String SUCCESS_KEY = "success_key";
	private static final String SUCCESS_CODE = "200";
	
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
	
	public static Map<String,String> appPay(String body, String orderid, String money, String ip){
		return appPay(body, orderid, money, ip, null);
	}
	
	/**
	 * app支付调用,返回到前端
	 * @param body 商品描述
	 * @param orderid 自有系统订单ID
	 * @param money 金额(微信是以分为单位的)
	 * @param ip (ip)
	 * @param notify_url 回调URL(默认是在常量类)
	 * @return 返回的结果里面如果存在code的key就证明失败,获取key为msg的原因解释
	 */
	public static Map<String,String> appPay(String body, String orderid, String money, String ip,String notify_url){
		if(StringUtils.isBlank(notify_url)){
			notify_url = WeChatConstant.WX_NOTIFY_URL;
		}
		SortedMap<String, String> returnMap = new TreeMap<String, String>();
		Collections.synchronizedSortedMap(returnMap);
		Map<String,String> resultMap = pay(WeChatConstant.APPPAY_APPID,WeChatConstant.APPPAY_MCHID,body, orderid, money, ip, null, notify_url, WeChatConstant.TRADE_TYPE_APP);
		final String app_package = "Sign=WXPay";
		// 获取返回的预支付ID
		if(resultMap!=null && resultMap.containsKey(SUCCESS_KEY) && resultMap.get(SUCCESS_KEY).equals(SUCCESS_CODE)){
			returnMap.put("appid",WeChatConstant.APPPAY_APPID);
			returnMap.put("partnerid",resultMap.get("mch_id"));
			returnMap.put("prepayid",resultMap.get("prepay_id"));
			returnMap.put("noncestr", resultMap.get("nonce_str"));
			returnMap.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
			returnMap.put("package", app_package);
	        // 第二次签名加密
	        try {
	        	returnMap.put("sign",WeChatUtil.createSign(returnMap));
	        	return returnMap;
			} catch (Exception e) {
				e.printStackTrace();
				return commonErrorMap;
			}
		}else{
			return resultMap;
		}
	}
	/**
     * 微信支付
     *
     * @param body       商品信息
     * @param orderid    唯一订单号
     * @param money      价格
     * @param ip         ip地址
     * @param openid     下单openid
     * @param notify_url 异步通知地址
     * @return
     */
    private static Map<String, String> pay(String appid,String mch_id,String body, String orderid, String money, String ip, String openid, String notify_url, String type) {
        Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
            map.put("appid", appid); 
           
            map.put("mch_id", mch_id);
            
            map.put("trade_type", type);
           
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",WeChatUtil.create_nonce_str().replaceAll("-", ""));
            
            // 商品描述
            map.put("body", body);
            
            // 商户订单号
            map.put("out_trade_no", orderid);
            
            // 总金额，单位是分
            map.put("total_fee", money);
            
            //客户端ip
            map.put("spbill_create_ip", ip);
            
            // 异步通知地址
            map.put("notify_url", notify_url);
            
            // 用户openid
            if (StringUtils.isNotBlank(openid)) {
                map.put("openid", openid);
            }
            
            // 生成sign
            map.put("sign", WeChatUtil.createSign(map));
            // 把参数转换为xml数据
            String params = XMLUtil.toXML(map);
            logger.info("=====微信提交预支付订单:\n"+params+" \n");
            // 提交数据，执行预支付
            //URLHanlder.post(wxConfig.getProperty("wx_unifiedorder"), params)
            returnMap = XMLUtil.toMap(WeChatUtil.post(WeChatConstant.UNIFIED_ORDER, params));
            
            logger.info("=====微信返回的预支付订单信息:\n"+returnMap +" \n");
            
            if (returnMap!=null && SUCCESS.equalsIgnoreCase(returnMap.get("return_code")) && SUCCESS.equalsIgnoreCase(returnMap.get("result_code"))) {
            	returnMap.put(SUCCESS_KEY, SUCCESS_CODE);
            	return returnMap;
            }else{
            	 Map<String, String> errorMap = new HashMap<String, String>();
            	 errorMap.put(WX_ERROR_CODE, returnMap.get("return_msg"));
            	 errorMap.put(ERROR_KEY, returnMap.get("err_code_des"));
            	 errorMap.put(FAIL_CODE, FAIL_CODE_VALUE);
            	 return errorMap;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return commonErrorMap;
        }
    }

    public static Map<String,String> jsapiPay(String body, String orderid, String money, String ip,String openid){
		return jsapiPay(body, orderid, money, ip, null,null,openid);
	}
	
    public static Map<String,String> jsapiServicePay(String body, String orderid, String money, String ip,String sub_mch_id,String openid){
		return jsapiPay(body, orderid, money, ip, null,sub_mch_id,openid);
	}
    
    public static Map<String,String> jsapiPay(String body, String orderid, String money, String ip,String notify_url,String sub_mch_id,String openid){
    	return jsapiPay(body, orderid, money, ip, notify_url, sub_mch_id, openid, "", "");
    }
	/**
	 * 微信内页面支付  (默认签名使用的 WeChatConstant.KEY_SERVICE)
	 * @param body
	 * @param orderid
	 * @param money
	 * @param ip
	 * @param notify_url
	 * @param sub_mch_id 子商户
	 * @param appId 微信应用的ID(默认使用 WeChatConstant.APPPAY_APPID_SERVICE)
	 * @param mchId 商户ID (默认使用 WeChatConstant.APPPAY_MCHID_SERVICE)
	 * @return
	 */
	public static Map<String,String> jsapiPay(String body, String orderid, String money, String ip,String notify_url,String sub_mch_id,String openid,String appId,String mchId){
		if(StringUtils.isBlank(notify_url)){
			notify_url = WeChatConstant.WX_NOTIFY_URL;
		}
		SortedMap<String, String> returnMap = new TreeMap<String, String>();
		Collections.synchronizedSortedMap(returnMap);
		if(StringUtils.isBlank(appId)){
			appId = WeChatConstant.APPPAY_APPID_SERVICE;
		}
		if(StringUtils.isBlank(mchId)){
			mchId = WeChatConstant.APPPAY_MCHID_SERVICE;
		}
		Map<String, String> resultMap = servicePay(appId, mchId, sub_mch_id,body, orderid, money, ip, notify_url,WeChatConstant.TRADE_TYPE_JSAPI, openid);
		// 获取返回的预支付ID
		if(resultMap!=null && resultMap.containsKey(SUCCESS_KEY) && resultMap.get(SUCCESS_KEY).equals(SUCCESS_CODE)){
			returnMap.put("appId",resultMap.get("appid"));
			returnMap.put("nonceStr", resultMap.get("nonce_str"));
			returnMap.put("timeStamp", String.valueOf(System.currentTimeMillis()/1000));
			returnMap.put("package", "prepay_id="+resultMap.get("prepay_id"));
			returnMap.put("signType", "MD5");
	        // 第二次签名加密
	        try {
	        	returnMap.put("paySign",WeChatUtil.createSignService(returnMap));
	        	return returnMap;
			} catch (Exception e) {
				e.printStackTrace();
				return commonErrorMap;
			}
		}else{
			return resultMap;
		}
	}
	
	private static Map<String,String> servicePay(String appid,String mchid,String sub_mch_id,String body,
    		String orderid,String money,String ip,String notify_url,String type,String openid){
		return servicePay(appid, mchid,sub_mch_id,body, orderid, money, ip, notify_url, type, openid,"");
	}
	
	private static Map<String,String> servicePay(String appid,String mchid,String sub_mch_id,String body,
    		String orderid,String money,String ip,String notify_url,String type,String openid,String key){
		return servicePay(appid, mchid,null,sub_mch_id,body, orderid, money, ip, notify_url, type, openid,key,null);
	}
	
	/**
	 * 服务商支付
	 * @param appid 微信分配的公众账号ID
	 * @param mchid 微信支付分配的商户号
	 * @param sub_appid //微信分配的子商户公众账号ID，如需在支付完成后获取sub_openid则此参数必传。
	 * @param sub_mch_id //微信支付分配的子商户号
	 * @param body 商品描述交易
	 * @param orderid 自己的订单号
	 * @param money 以分为单位
	 * @param ip 请求端的IP
	 * @param notify_url 回调地址
	 * @param type 交易类型 取值如下：JSAPI，NATIVE，APP，
	 * @param openid 用户在主商户appid下的唯一标识
	 * @param sub_openid 用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid
	 * @return
	 */
    private static Map<String, String> servicePay(String appid, String mchid,String sub_appid, String sub_mch_id,String body,
			String orderid, String money, String ip, String notify_url,String type, String openid, String key,String sub_openid) {
		Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
		try {
			// 构造下单所需要的参数
			SortedMap<String, String> map = new TreeMap<String, String>();
			Collections.synchronizedSortedMap(map);
			map.put("appid", appid); // 微信分配的公众账号ID
			map.put("mch_id", mchid);// 微信支付分配的商户号
			if (org.apache.commons.lang3.StringUtils.isNotBlank(sub_appid)) {
				map.put("sub_appid", sub_appid);// 微信分配的子商户公众账号ID，如需在支付完成后获取sub_openid则此参数必传。
			}
			if (org.apache.commons.lang3.StringUtils.isNotBlank(sub_mch_id)) {
				map.put("sub_mch_id", sub_mch_id);// 微信支付分配的子商户号
			}
			
			// 随机字符串
			map.put("nonce_str",WeChatUtil.create_nonce_str().replaceAll("-", ""));

			// 商品描述
			map.put("body", body);

			// 商户订单号
			map.put("out_trade_no", orderid);

			// 总金额，单位是分
			map.put("total_fee", money);

			// 客户端ip
			map.put("spbill_create_ip", ip);

			// 异步通知地址
			map.put("notify_url", notify_url);

			map.put("trade_type", type);

			map.put("openid", openid);//用户openid
			
			// 生成sign
			if(org.apache.commons.lang3.StringUtils.isBlank(key)){
				map.put("sign", WeChatUtil.createSignService(map));
			}else{
				map.put("sign", WeChatUtil.createSignService(map,key));
			}
			// 把参数转换为xml数据
			String params = XMLUtil.toXML(map);
			logger.info("=====微信服务商提交预支付订单:\n" + params + " \n");
			// 提交数据，执行预支付
			// URLHanlder.post(wxConfig.getProperty("wx_unifiedorder"), params)
			returnMap = XMLUtil.toMap(WeChatUtil.post(
					WeChatConstant.UNIFIED_ORDER, params));

			logger.info("=====微信返回的预支付订单信息:\n" + returnMap + " \n");

			if (returnMap != null && SUCCESS.equalsIgnoreCase(returnMap.get("return_code"))
					&& SUCCESS.equalsIgnoreCase(returnMap.get("result_code"))) {
				returnMap.put(SUCCESS_KEY, SUCCESS_CODE);
				return returnMap;
			} else {
				Map<String, String> errorMap = new HashMap<String, String>();
				errorMap.put(WX_ERROR_CODE, returnMap.get("return_msg"));
				errorMap.put(ERROR_KEY, returnMap.get("err_code_des"));
				errorMap.put(FAIL_CODE, FAIL_CODE_VALUE);
				return errorMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return commonErrorMap;
		}
	}
    
    /**
     * 刷卡支付（被扫场景）
     * @param body
     * @param orderid
     * @param money
     * @param ip
     * @param authCode
     * @return
     */
    public static Map<String,String> scanPay(String body, String orderid, String money, String ip,String authCode){
		Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
            map.put("appid", WeChatConstant.APPPAY_APPID); 
           
            map.put("mch_id", WeChatConstant.APPPAY_MCHID);
           
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",WeChatUtil.create_nonce_str().replaceAll("-", ""));
            
            // 商品描述
            map.put("body", body);
            
            // 商户订单号
            map.put("out_trade_no", orderid);
            
            // 总金额，单位是分
            map.put("total_fee", money);
            
            //客户端ip
            map.put("spbill_create_ip", ip);
            
            //授权码
            map.put("auth_code", authCode);
            
            // 生成sign
            map.put("sign", WeChatUtil.createSign(map));
            // 把参数转换为xml数据
            String params = XMLUtil.toXML(map);
            logger.info("=====微信提交支付订单:\n"+params+" \n");
            // 提交数据，执行预支付
            //URLHanlder.post(wxConfig.getProperty("wx_unifiedorder"), params)
            returnMap = XMLUtil.toMap(WeChatUtil.post(WeChatConstant.SCAN_PAY_URL, params));
            
            logger.info("=====微信返回的支付订单信息:\n"+returnMap +" \n");
            
            if (returnMap!=null && SUCCESS.equalsIgnoreCase(returnMap.get("return_code")) && SUCCESS.equalsIgnoreCase(returnMap.get("result_code"))) {
            	returnMap.put(SUCCESS_KEY, SUCCESS_CODE);
            	return returnMap;
            }else{
            	 Map<String, String> errorMap = new HashMap<String, String>();
            	 errorMap.put(WX_ERROR_CODE, returnMap.get("return_msg"));
            	 errorMap.put(ERROR_KEY, returnMap.get("err_code_des"));
            	 errorMap.put(FAIL_CODE, FAIL_CODE_VALUE);
            	 return errorMap;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return commonErrorMap;
        }
	}
    
    /**
     * 
     *	微信退款证书认证
     * @param orderid  (订单号)
     * @param refundOrderNum (退款订单号)
     * @param money	(总金额)
     * @param refundFee (退款金额)
     * @return
     */
    public static Map<String,String> refund(String orderid,String refundOrderNum, String money, String refundFee){
		Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
            map.put("appid", WeChatConstant.APPPAY_APPID); 
           
            map.put("mch_id", WeChatConstant.APPPAY_MCHID);
           
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",WeChatUtil.create_nonce_str().replaceAll("-", ""));
            
            // 商户订单号
            map.put("out_trade_no", orderid);
            
            // 退款訂單
            map.put("out_refund_no", refundOrderNum);
            
            // 总金额，单位是分
            map.put("total_fee", money);
            
            // 退款金额，单位是分
            map.put("refund_fee", refundFee);
            
            //操作员帐号, 默认为商户号
            map.put("op_user_id", WeChatConstant.APPPAY_MCHID);
            
            // 生成sign
            map.put("sign", WeChatUtil.createSign(map));
            // 把参数转换为xml数据
            String params = XMLUtil.toXML(map);
            
            //退款认证
            String jsonStr = WeChatUtil.certPost(WeChatConstant.REFUND_URL, params,WeChatConstant.REFUND_CERT_URL);
            returnMap = XMLUtil.toMap(jsonStr);
            if (returnMap!=null && SUCCESS.equalsIgnoreCase(returnMap.get("return_code")) && SUCCESS.equalsIgnoreCase(returnMap.get("result_code"))) {
            	returnMap.put(SUCCESS_KEY, SUCCESS_CODE);
            	return returnMap;
            }else{
            	 Map<String, String> errorMap = new HashMap<String, String>();
            	 errorMap.put(WX_ERROR_CODE, returnMap.get("return_msg"));
            	 errorMap.put(ERROR_KEY, returnMap.get("err_code_des"));
            	 errorMap.put(FAIL_CODE, FAIL_CODE_VALUE);
            	 return errorMap;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return commonErrorMap;
        }
	}
    
    /**
     * 刷卡支付（被扫场景）
     * @param orderid
     * @return
     */
    public static Map<String,String> queryOrder(String orderid){
		Map<String, String> returnMap = new ConcurrentHashMap<String, String>();
        try {
            // 构造下单所需要的参数
            SortedMap<String, String> map = new TreeMap<String, String>();
            Collections.synchronizedSortedMap(map);
            map.put("appid", WeChatConstant.APPPAY_APPID); 
           
            map.put("mch_id", WeChatConstant.APPPAY_MCHID);
           
            //随机字符串
            //String nonce_str = create_nonce_str().replaceAll("-", "");
            map.put("nonce_str",WeChatUtil.create_nonce_str().replaceAll("-", ""));
            
            // 商户订单号
            map.put("out_trade_no", orderid);
            
            
            // 生成sign
            map.put("sign", WeChatUtil.createSign(map));
            // 把参数转换为xml数据
            String params = XMLUtil.toXML(map);
            logger.info("=====微信提交支付订单:\n"+params+" \n");
            // 提交数据，执行预支付
            //URLHanlder.post(wxConfig.getProperty("wx_unifiedorder"), params)
            returnMap = XMLUtil.toMap(WeChatUtil.post(WeChatConstant.QUERY_ORDER_URL, params));
            
            logger.info("=====微信返回的支付订单信息:\n"+returnMap +" \n");
            
            if (returnMap!=null && SUCCESS.equalsIgnoreCase(returnMap.get("return_code")) && SUCCESS.equalsIgnoreCase(returnMap.get("result_code"))) {
            	returnMap.put(SUCCESS_KEY, SUCCESS_CODE);
            	return returnMap;
            }else{
            	 Map<String, String> errorMap = new HashMap<String, String>();
            	 errorMap.put(WX_ERROR_CODE, returnMap.get("return_msg"));
            	 errorMap.put(ERROR_KEY, returnMap.get("err_code_des"));
            	 errorMap.put(FAIL_CODE, FAIL_CODE_VALUE);
            	 return errorMap;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	return commonErrorMap;
        }
	}
    
    public static void main(String[] args) throws Exception {
//    	scanPay("被扫支付测试"
//				,"20170302123464",""+DecimalUtil.yuanToCents("0.01"),"192.168.1.172","130229605326517962");
    	
    	refund("20170302123463", "RR5641345813223", ""+DecimalUtil.yuanToCents("0.01"), ""+DecimalUtil.yuanToCents("0.01"));
    
    }
}
