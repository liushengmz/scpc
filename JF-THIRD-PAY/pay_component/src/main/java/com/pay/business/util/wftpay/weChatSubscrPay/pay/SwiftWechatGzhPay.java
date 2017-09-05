package com.pay.business.util.wftpay.weChatSubscrPay.pay;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.pay.business.util.RandomUtil;
import com.pay.business.util.wftpay.weChatSubscrPay.swiftpassConfig.SwiftpassConfig;
import com.pay.business.util.wftpay.weChatSubscrPay.utils.MD5;
import com.pay.business.util.wftpay.weChatSubscrPay.utils.SignUtils;
import com.pay.business.util.wftpay.weChatSubscrPay.utils.XmlUtils;

/**
* @Title: SwiftWechatGzhPay.java 
* @Package com.pay.business.util.weChatSubscrPay.pay 
* @Description:威富通：微信公众号支付
* @author ZHOULIBO   
* @date 2017年7月5日 下午5:52:51 
* @version V1.0
*/
public class SwiftWechatGzhPay {
		private static Logger log = Logger.getLogger(SwiftWechatGzhPay.class);
	    private final static String version = "1.1";
	    private final static String charset = "UTF-8";
	    private final static String sign_type = "MD5";
	    public static void main(String[] args) {
	    	SortedMap<String, String> map =new TreeMap<String, String>();
	    	try {
	    		map.put("out_trade_no", "DD201707121149088912545455");
				query(map, "7551000001", "9d101c97133837e13dde2d32a5054abb");
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	/**
	 * swiftWechatGzhPay 
	 * 威富通：微信公众号支付调起
	 * @param map
	 * @param mchId 商户号
	 * @param key   商户key
	 * @return
	 * @throws Exception    设定文件 
	 * Map<String,String>    返回类型
	 */
	@SuppressWarnings("null")
	public static Map<String,String> swiftWechatGzhPay(SortedMap<String, String> map,String mchId,String key) throws Exception{
		//接口类型
        map.put("service", "pay.weixin.jspay");
        //版本号
        map.put("version", version);
        //字符集
        map.put("charset", charset);
        //签名方式
        map.put("sign_type", sign_type);
        //商户ID
        map.put("mch_id",mchId);
        //异步回调URL
        map.put("notify_url", SwiftpassConfig.NOTIFY_URL);
        //
        map.put("callback_url", SwiftpassConfig.CALLBACK_URL);
        //随机数
        map.put("nonce_str", String.valueOf(new Date().getTime())+RandomUtil.generateString(4));
        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" +key, "utf-8");
        log.info("=====>威富通：微信公众号支付调起提交参数加密结果为:" + sign);
        map.put("sign", sign);
        String xmlMap= XmlUtils.parseXML(map);
        log.info("=====>威富通：微信公众号支付调起提交参数转换为XML格式为:" +xmlMap);
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = null;
        Map<String, String> resultMap = null;
        try {
            HttpPost httpPost = new HttpPost(SwiftpassConfig.REQ_URL);
            StringEntity entityParams = new StringEntity(xmlMap, "utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if (response != null && response.getEntity() != null) {
                resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                log.info("=====>威富通：微信公众号支付调起请求结果：" + res);
                if (!SignUtils.checkParam(resultMap,key)) {
                    res = "验证签名不通过";
                    resultMap.put("code","10001");
                    resultMap.put("msg","失败原因："+res);
                    log.error("=====>威富通：微信公众号支付调起失败原因："+res);
                } else {
                    if ("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))) {
                    	//组装可以访问的URL
                    	String url=SwiftpassConfig.WFT_PAY_URL+resultMap.get("token_id");
                    	resultMap.put("pay_url",url);
                        log.info("=====>威富通：微信公众号支付调起成功!访问的URL为："+url);
                    }else{
                    	resultMap.put("code","10001");
                    	resultMap.put("msg","失败原因："+resultMap);
                    	 log.error("=====>威富通：微信公众号支付调起失败原因："+resultMap);
                    } 
                }
            } else {
                res = "操作失败";
                resultMap.put("code","10001");
            	resultMap.put("msg","失败原因："+res);
            	log.error("=====>威富通：微信公众号支付调起失败原因："+res);
            }
        } catch (Exception e) {
            log.error("操作失败，原因：",e);
            resultMap.put("code","10001");
        	resultMap.put("msg","失败原因："+e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return resultMap;
	}
	
	/**
	 * query 
	 * 订单查询 
	 * @param map
	 * @param mchId 商户号
	 * @param key   商户key
	 * @return
	 * @throws ServletException
	 * @throws IOException    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> query(SortedMap<String, String> map, String mchId, String key) throws ServletException, IOException {
		log.info("====订单查询服务开始...");
		Map<String, String> result = new HashMap<String, String>();
		map.put("service", "trade.single.query");
		map.put("version", version);
		map.put("charset", charset);
		map.put("sign_type", sign_type);
		map.put("mch_id", mchId);

		String reqUrl = SwiftpassConfig.REQ_URL;
		map.put("nonce_str", String.valueOf(new Date().getTime()));

		Map<String, String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
		SignUtils.buildPayParams(buf, params, false);
		String preStr = buf.toString();
		String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
		map.put("sign", sign);

		log.info("======>威富通reqUrl:" + reqUrl);

		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		String res = null;
		try {
			HttpPost httpPost = new HttpPost(reqUrl);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map), "utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);

			if (response != null && response.getEntity() != null) {
				Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
				res = XmlUtils.toXml(resultMap);
				log.info("请求结果：" + res);

				if (!SignUtils.checkParam(resultMap, key)) {
					res = "验证签名不通过";
				} else {
					if ("0".equals(resultMap.get("status"))) {
						if ("0".equals(resultMap.get("result_code"))) {
							String trade_state = resultMap.get("trade_state");
							// SUCCESS—支付成功
							// REFUND—转入退款
							// NOTPAY—未支付
							// CLOSED—已关闭
							// REVERSE—已冲正
							// REVOK—已撤销
							switch (trade_state) {
							case "SUCCESS":
								result.put("payStatus", "1");
								break;
							case "REFUND":
								result.put("payStatus", "1");//退款：之前消费成功 视为成功状态
								break;
							case "NOTPAY":
								result.put("payStatus", "3");
								break;
							case "REVERSE":
								result.put("payStatus", "7");
								break;
							case "REVOK":
								result.put("payStatus", "8");
								break;
							case "CLOSED":
								result.put("payStatus", "9");
								break;
							default:
								break;
							}
							result.put("code", "10000");
						} else {
							result.put("code", "10001");
							result.put("msg", "失败原因"+resultMap.get("err_msg"));
							log.error("业务失败，尝试重新请求，并查看错误代码描叙"+resultMap);
						}
					} else {
						result.put("code", "10001");
						result.put("msg", "失败原因"+res);
						log.error("参数有问题...");
					}
				}
			} else {
				res = "操作失败!";
				result.put("code", "10001");
				result.put("msg", "失败原因"+res);
				log.error("操作失败，原因："+res);
			}
		} catch (Exception e) {
			log.error("操作失败，原因：", e);
			res = "操作失败";
			result.put("code", "10001");
			result.put("msg", "失败原因"+e);
		}
        return result;
    }
    
    /**
     * refundQuery 
     * 退款查询:暂未使用此接口 
     * @param map
     * @param mchId
     * @param key
     * @return
     * @throws ServletException
     * @throws IOException    设定文件 
     * Map<String,String>    返回类型
     */
    public Map<String,String> refundQuery(SortedMap<String, String> map,String mchId,String key) throws ServletException, IOException{
        log.info("======退款查询开始...");
        map.put("service", "trade.refund.query");
        map.put("version", version);
        map.put("charset", charset);
        map.put("sign_type", sign_type);
        String reqUrl = SwiftpassConfig.REQ_URL;
        map.put("mch_id",mchId);
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        log.info("reqUrl:" + reqUrl);
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                log.info("请求结果：" + res);
                
                if(!SignUtils.checkParam(resultMap, key)){
                    res = "验证签名不通过";
                }
            }else{
                res = "操作失败!";
            }
        } catch (Exception e) {
            log.error("操作失败，原因：",e);
            res = "操作失败";
        } finally {
            if(response != null){
                response.close();
            }
            if(client != null){
                client.close();
            }
        }
        Map<String,String> result = new HashMap<String,String>();
        if(res.startsWith("<")){
            result.put("code", "10000");
            result.put("msg", "操作成功，请在日志文件中查看");
        }else{
            result.put("code", "10001");
            result.put("msg", res);
        }
        return result;
    }

	/**
	 * refund 
	 * 威富通微信公众号支付退款申请 
	 * @param mch_id 商户号
	 * @param key    商户key
	 * @param out_trade_no 商户订单号
	 * @param out_refund_no 商户退款订单号
	 * @param total_fee 总金额
	 * @param refund_fee 退款金额
	 * @param op_user_id 操作人
	 * @return
	 * @throws ServletException
	 * @throws IOException    设定文件 
	 * Map<String,String>    返回类型
	 */
    public static Map<String,String> refund(String mch_id,String key,String out_trade_no,String out_refund_no,String total_fee,String refund_fee,String op_user_id) throws ServletException, IOException{
        log.info("=====>威富通微信公众号支付退款开始...");
        SortedMap<String,String> map =new TreeMap<String,String>();
        String reqUrl = SwiftpassConfig.REQ_URL;
        map.put("service", "unified.trade.refund");
        map.put("mch_id", mch_id);
        map.put("out_trade_no", out_trade_no);
        map.put("out_refund_no", out_refund_no);
        map.put("total_fee",total_fee);
        map.put("refund_fee",refund_fee);
        map.put("op_user_id",mch_id);
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        log.info("=====>威富通微信公众号支付退款申请reqUrl:" + reqUrl);
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        String res = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                log.info("=====>请求结果：" + res);
                
                if(!SignUtils.checkParam(resultMap, key)){
                    res = "验证签名不通过";
                }
            }else{
                res = "操作失败!";
            }
        } catch (Exception e) {
            log.error("操作失败，原因：",e);
            res = "操作失败";
        } finally {
            if(response != null){
                response.close();
            }
            if(client != null){
                client.close();
            }
        }
        Map<String,String> result = new HashMap<String,String>();
        if(res.startsWith("<")){
            result.put("code", "10000");
            result.put("msg", "操作成功，请在日志文件中查看");
        }else{
            result.put("code", "10001");
            result.put("msg", res);
        }
        return result;
    }
}
