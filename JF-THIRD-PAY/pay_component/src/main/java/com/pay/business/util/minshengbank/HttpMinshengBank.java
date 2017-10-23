package com.pay.business.util.minshengbank;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.core.teamwork.base.util.MapRemoveNullUtil;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.ReadProChange;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesla.tunguska.cuppay.util.CipherSignUtil;

public class HttpMinshengBank {
	
	private static Logger logger = Logger.getLogger(HttpMinshengBank.class);
	/**
	 * 
	 * minshengBankCallBack 
	 * 民生银行扫码支付
	 * @param paramMap:	merNo 			商户号 						String(15)	 商户号 N *
	 *             		orderNo 		商户订单号 						String(30) 	商户订单号,同一商户号和终端号下唯一 N *
	 *             		channelFlag 	支付渠道 						String(2) 	支付渠道00：微信APP，01：支付宝App，02：百付包，03翼支付,04:qq 05京东 N *
	 *             		goodsName 		商品名 						String(30) 	商品名 Y *
	 *             		amount 			订单金额						String(12) 	单位为分 N  *
	 *             		reqId 			请求交易的流水号 					String(32) 	请求交易的流水号，同一商户号和终端号下唯一 N  *
	 *             		reqTime 		请求时间，格式: yyyyMMddHHmmss 	String(14) 	请求时间，格式: yyyyMMddHHmmss N  *
	 *             		currency 		币种 String 币种，默认 CNY Y
	 *             		extraDesc 		附加信息
	 * @param bankSecretKey
	 * @return
	 * @throws Exception    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> minshengBankCallBack(Map<String, Object> paramMap,String bankSecretKey) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String> rMap = new HashMap<String, String>();
		if(ObjectUtil.checkObjectFileIsEmpty(new String[] {"merNo","orderNo","channelFlag","amount","reqId","reqTime"}, paramMap) && StringUtils.isNotBlank(bankSecretKey)){
			logger.info("-----------------------------------------------------------------------------------");
			logger.info("minShengBank-->bankSecretKey:"+bankSecretKey+",parmMap:"+paramMap);
			// 移除空值
			MapRemoveNullUtil.removeNullEntry(paramMap);
			paramMap.put("notifyUrl", MSBankConfig.NOTIFY_URL);
			// 得到加密sgin
			String sgin = MinShengBankSignUtil.getSign(paramMap, bankSecretKey);
			paramMap.put("signIn", sgin);
			String result = HttpUtil.sendRequest(MSBankConfig.URL, paramMap);
			resultMap =  JSON.parseObject(result, new TypeReference<Map<String, Object>>(){});
			if(resultMap.containsKey("result")){
				if("0000".equals(resultMap.get("result"))){
					rMap.put("code","10000");
					rMap.put("qr_code",resultMap.get("codeUrl").toString());
					rMap.put("out_trade_no",resultMap.get("orderNo").toString());
					logger.info("======》民生银行扫码支付成功");
				}else{
					//失败
					rMap.put("code","10001");
					rMap.put("msg","失败原因"+resultMap);
					logger.error("======》民生银行扫码支付失败：原因"+resultMap);
				}
			}else{
				//失败
				rMap.put("code","10001");
				rMap.put("msg","失败原因"+resultMap);
				logger.error("======》民生银行扫码支付失败：原因"+resultMap);
			}
			logger.info("minShengBank-->resultMap:"+resultMap);
			logger.info("-----------------------------------------------------------------------------------");
		}else{
			rMap.put("code","10001");
			rMap.put("msg", "参数异常-merNo,orderNo,channelFlag,amount,reqId,reqTime==>"+JSON.toJSONString(paramMap));
			logger.error("======》民生银行扫码支付失败：原因"+JSON.toJSONString(paramMap));
		}
		return rMap;
	}
	
	
	/**
	 * @Title: queryMSOrder 
	 * @Description: 民生银行查询接口
	 * @param paramMap:merNo		商户号				String	MAX(15)		是	商户号
	 * 				   orderNo		商户订单号或平台方流水号	String	MAX(30)		否	商户订单号或平台方流水号
	 * 				   termNo		终端编码				String	MAX(200)	否	终端编码
	 * 				   orgReqId		商户请求交易的流水号		String	String(32)	否	商户请求交易的流水号
	 * 				   orgTransId	平台方唯一交易请求流水号	String	String(20)	否	平台方唯一交易请求流水号
	 * 				   operatorId	操作员编号				String	String(30)	否	商户操作员编号
	 * @param bankSecretKey 商户KEY
	 * @return Map<String,Object>    返回类型 
	 * @throws Exception 
	 */
	public static Map<String, String> queryMSOrder(Map<String, Object> paramMap,String bankSecretKey) throws Exception{
		Map<String, String> resultMap = new HashMap<String, String>();
		if(ObjectUtil.checkObjectFileIsEmpty(new String[] {"merNo"}, paramMap) && StringUtils.isNotBlank(bankSecretKey)){
			// 移除空值
			MapRemoveNullUtil.removeNullEntry(paramMap);
			// 得到加密sgin
			String sgin = MinShengBankSignUtil.getSign(paramMap, bankSecretKey);
			paramMap.put("signIn", sgin);
			String result = HttpUtil.sendRequest(MSBankConfig.QUERY_URL, paramMap);
			Map<String, Object> resultMap1=  JSON.parseObject(result, new TypeReference<Map<String, Object>>(){});
			if(resultMap1.containsKey("result")){
				if("0000".equals(resultMap1.get("result"))){
					String transStatus=String.valueOf(resultMap1.get("transStatus"));
					switch (transStatus) {
					case "0":
						resultMap.put("payStatus","1");//成功
						break;
					case "1":
						resultMap.put("payStatus","2");//失败
						break;
					case "2":
						resultMap.put("payStatus","6");//未知
						break;
					case "3":
						resultMap.put("payStatus","7");//撤销
						break;
					case "4":
						resultMap.put("payStatus","2");//关闭	视为失败			
						break;
					case "5":
						resultMap.put("payStatus","1");//有退款（之前消费成功）视为成功		
						break;
					default:
						break;
					}
					resultMap.put("code","10000");
				}else{
					//失败
					resultMap.put("code","10001");
					resultMap.put("msg","失败原因"+resultMap1);
					logger.error("======>民生银行查询接口失败原因："+resultMap1);
				}
				
			}else{
				resultMap.put("code","10001");
				resultMap.put("msg", "参数异常-merNo==>"+result);
				logger.error("======>民生银行查询接口失败原因：参数异常-merNo");
			}
		}else{
			resultMap.put("code","10001");
			resultMap.put("msg", "参数异常-merNo==>"+JSON.toJSONString(paramMap));
			logger.error("======>民生银行查询接口失败原因："+JSON.toJSONString(paramMap));
		}
		return resultMap;
	}
	
	/**
	 * @Title: refundMSOrder 
	 * @Description: 民生银行退款
	 * @param paramMap 	merNo				商户号				String	MAX(15)		是	商户号
	 *        			orderNo				商户订单号或平台方流水号	String	MAX(30)		否	商户订单号或平台方流水号（原消费交易）
	 *        			termNo				终端编码				String	MAX(200)	否	终端编码
	 *        			money				退款金额				String	String(12)	否	退款金额(如不传则视为全部可退款，总退款金额不能超过原始消费金额)
	 *        			reqId				商户请求交易的流水号		String	String(32)	是	商户请求交易的流水号
	 *        			orgTransId			平台方唯一交易请求流水号	String	String(20)	否	平台方唯一交易请求流水号（原消费交易）
	 *        			orgReqId			原始请求交易流水号		String	String(32)	否	原始请求交易流水号（原消费交易）
	 *        			reqTime				请求时间				String	MAX(14)		是	格式: yyyyMMDDhhmmss
	 *        			operatorId			操作员编号				String	String(30)	否	商户操作员编号
	 *        			extraDesc			附加信息				String	MAX(2048)	否	附加信息
	 * @param bankSecretKey 商户KEY
	 * @return Map<String,Object>    返回类型 
	 * @throws Exception 
	 */
	public static Map<String, Object> refundMSOrder(Map<String, Object> paramMap,String bankSecretKey) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(ObjectUtil.checkObjectFileIsEmpty(new String[] {"merNo","orderNo","money","reqId","reqTime"}, paramMap) && StringUtils.isNotBlank(bankSecretKey)){
			// 移除空值
			MapRemoveNullUtil.removeNullEntry(paramMap);
			// 得到加密sgin
			String sgin = MinShengBankSignUtil.getSign(paramMap, bankSecretKey);
			paramMap.put("signIn", sgin);
			logger.info("======>民生银行支付宝扫码退款提交参数："+paramMap);
			String result = HttpUtil.sendRequest(MSBankConfig.REFUND_URL, paramMap);
			Map<String, Object> resultHttpMap =  JSON.parseObject(result, new TypeReference<Map<String, Object>>(){});
			if(resultHttpMap.containsKey("result")){
				if("0000".equals(resultHttpMap.get("result"))){
					resultMap.put("code","10000");
					logger.info("======>民生银行退款成功");
				}else{
					//失败
					resultMap.put("code","10001");
					resultMap.put("msg","失败原因"+resultHttpMap);
					logger.error("======>民生银行支退款失败原因："+resultHttpMap);
				}
			}else{
				//失败
				resultMap.put("code","10001");
				resultMap.put("msg","失败原因"+resultHttpMap);
				logger.error("======>民生银行退款失败原因："+resultHttpMap);
			}
		}else{
			resultMap.put("code","10001");
			resultMap.put("msg", "参数异常-merNo==>"+JSON.toJSONString(paramMap));
			logger.error("======>民生银行退款失败原因：参数异常-merNo"+JSON.toJSONString(paramMap));
		}
		return resultMap;
	}

	/**
	 * 民生银行-支付宝扫码支付-对账接口查询
	 * @param paramMap
	 * @param bankSecretKey
	 * @param 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	public static String  msBankAliStatement(Map<String, Object> paramMap) {
			try {
				String responseBody=post(MSBankConfig.STATEMENT_URL, paramMap);
				logger.info("======>民生银行对账接口查询结果为："+responseBody);
				return responseBody;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("======>民生银行对账接口查询失败原因为："+e);
			}
		return null;

	}
	public static Map<String, Object> getParams(Map<String, Object> paramMap,String thdSysNme) throws Exception {
		//格式：L ==》aijinfu_jks_path=/zywa/jinfu/aijinfu.jks   w:==》 aijinfu_jks_path=D\:\\aijinfu.jks
		String ksPath =ReadProChange.getValue("aijinfu_jks_path")+thdSysNme+".jks";
//		String thdSysNme = "yizhan";//
//		String ksPath = "D:\\yizhan.jks";//
//		String password = "yizhan";// ks
//		String alias = "yizhan";// ks
		
		String password = thdSysNme;// ks
		String alias = thdSysNme;// ks
		// start
		// end
		String timestamp = String.valueOf(System.currentTimeMillis() + 1 * 60 * 1000);
		paramMap.put("timestamp", timestamp);
//		InputStream ksInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ksPath);
		InputStream ksInputStream = new FileInputStream(ksPath);
		// CPOS
		String encryptedSign = CipherSignUtil.paramSignAndEncryptBase64String(paramMap, password, alias, ksInputStream);
		// SHA1, ,
		String summaryPlain = CipherSignUtil.paramMap2SHA1Base64String(paramMap);
		paramMap.put("thdSysNme", thdSysNme);// +50
		paramMap.put("timestamp", timestamp);// GMT
		paramMap.put("encryptedSign", encryptedSign);// ,base64
		paramMap.put("summaryPlain", summaryPlain);// base64
		return paramMap;
	}

	public static String post(String url, Map<String, Object> params) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		String responseBody="";
		try {
			ObjectMapper om = new ObjectMapper();
			String requestInfo = om.writeValueAsString(params);
			System.out.println(requestInfo);
			method.setRequestEntity(new StringRequestEntity(requestInfo, "application/json", "UTF-8"));
			int code = client.executeMethod(method);
			System.out.println("Response code: " + code);
			System.out.println(new String(method.getResponseBody()));
			responseBody=new String(method.getResponseBody());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
			client.getHttpConnectionManager().closeIdleConnections(0);
		}
		return responseBody;
	}
	/**
	 * MSWeChatGzhPay 
	 * 民生银行-微信公众号支付 
	 * @param merNo         	商户号
	 * @param key				商户key
	 * @param orderNo			商户订单号
	 * @param amount			订单金额：以分为单位
	 * @param reqId				商户请求交易的流水号
	 * @param reqTime			请求时间:格式：yyyyMMDDhhmmss
	 * @param subAppId			接入方微信公众号 id（于微信分配）
	 * @param subOpenId			用户在subAppId下的唯一标识
	 * @return    设定文件 
	 * Map<String,Object>    返回类型
	 */
	public static Map<String,String> MSWeChatGzhPay(String merNo,String key,String orderNo,String amount,String reqId,String reqTime,String subAppId,String subOpenId){
		Map<String, Object> paramMap=new HashMap<String, Object>();
		Map<String, String> resultMap=new HashMap<String, String>();
		paramMap.put("merNo", merNo);
		paramMap.put("orderNo", orderNo);
		paramMap.put("amount", amount);
		paramMap.put("reqId", reqId);
		paramMap.put("reqTime", reqTime);
		paramMap.put("subAppId", subAppId);
		paramMap.put("subOpenId", subOpenId);
		paramMap.put("notifyUrl", MSBankConfig.NOTIFY_URL);
		try {
			// 得到加密sgin
			String signIn = MinShengBankSignUtil.getSign(paramMap, key);
			paramMap.put("signIn", signIn);
			logger.info("======>民生银行-微信公众号支付 提交参数：" + paramMap);
			String result = HttpUtil.sendRequest(MSBankConfig.GZHPAY_URL, paramMap);
			Map<String, Object> resultHttpMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
			});
			if (resultHttpMap.containsKey("result")) {
				if ("0000".equals(resultHttpMap.get("result"))) {
					resultMap.put("jsapi_appid", resultHttpMap.get("appId").toString());
					resultMap.put("jsapi_timestamp", resultHttpMap.get("timeStamp").toString());
					resultMap.put("jsapi_noncestr", resultHttpMap.get("nonceStr").toString());
					resultMap.put("jsapi_package", resultHttpMap.get("pack").toString());
					resultMap.put("jsapi_signtype", resultHttpMap.get("signType").toString());
					resultMap.put("jsapi_paysign", resultHttpMap.get("paySign").toString());
					resultMap.put("code", "10000");
					logger.info("======>民生银行-微信公众号支付 成功");
				} else {
					// 失败
					resultMap.put("code", "10001");
					resultMap.put("msg", "失败原因" + resultHttpMap);
					logger.error("======>民生银行-微信公众号支付 失败原因：" + resultHttpMap);
				}
			} else {
				// 失败
				resultMap.put("code", "10001");
				resultMap.put("msg", "失败原因" + resultHttpMap);
				logger.error("======>民生银行-微信公众号支付 失败原因：" + resultHttpMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "10001");
			resultMap.put("msg", "失败原因" + e);
			logger.error("======>民生银行-微信公众号支付 失败原因：" + e);
		}
		return resultMap;
		
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Date as = new Date(new Date().getTime()-24*60*60*1000*2);
		String alipayTime = DateUtil.DateToString(as,"yyyy-MM-dd");
		paramMap.put("expanderId","0199980907");
		paramMap.put("customerId", "MS0000001887443");
		// 查询开始时间
		paramMap.put("beginDate", alipayTime);
		// 结束时间
		paramMap.put("endDate", alipayTime);
		// 服务商类型
		paramMap.put("serviceProviderId", "YF");
		// 日期类型:1-交易日期,2-清算日期
		paramMap.put("dateType", "1");
		paramMap.put("channelId", "WX6");
		paramMap=getParams(paramMap,"aijinfu");
		//对账查询
		String info=msBankAliStatement(paramMap);
		System.out.println(info);
//		queryMSOrder(paramMap, "c3f55181de5a4732ad7e19aa938b4d54");
//		System.out.println(map);
//		Map<String, Object> map =minshengBankCallBack(paramMap, "611d28c87f1d4c61ab0c5042071effc3");
//		String responseBody="{'reply':{'paginator':{'recordsPerPage':2,'pageNo':1,'total':42,'doPagination':true,'queryTotal':true,'queryLastIfPageNoExceed':true,'beginIndex':0,'endIndex':2,'totalPage':21},'returnCode':{'domain':null,'type':'S','code':'AAAAAA'},'cupCheckDetailList':[{'serviceProviderId':'YF','customerName':'北京K酷国际影城','customerId':'MS0000000008680','deviceId':'','merchantId':null,'posId':null,'transDate':1492617600000,'transTime':40663000,'settleDate':1492617600000,'transType':'1','draweAcct':'*羽晨(185****9575)','transAmt':10.00,'transFee':0.0400,'merchantOrderId':'1201704201917411001','chnlRetrivlRef':'20170420191743528594','chnlOrderId':'2017042021001004230259900646','channelId':'HSBANK','sumTransAmt':null,'sumNumber':null,'sl':null,'expanderId':'T000023474','expanderName':null,'settleFlag':null,'requestFlowNo':'21420170619214940574963'}]}}";
//		JSONObject jsonObject = JSONObject.fromObject(responseBody); 
//		JSONObject json_to_data = jsonObject.getJSONObject("reply");//即可
//		JSONArray json_to_strings=json_to_data.getJSONArray("cupCheckDetailList");
//		if(json_to_strings.size()>0){
//			 for (int i = 0; i < json_to_strings.size(); i++) {//循环读取即可
//			      JSONObject json_to_string1 = json_to_strings.getJSONObject(i);
//			      if(json_to_string1!=null){
//			    	  //获取支付是否成功与退款
//			    	  String transType=(String) json_to_string1.get("serviceProviderId");
//			    	  if(transType.equals("1")){//成功
//			    		  
//			    	  }
//			    	  if(transType.equals("3")){//退款
//			    		  
//			    	  }
//			      }
//			      System.out.println(json_to_string1);
//			    } 
//		}
//		MSWeChatGzhPay("MS0000001097514", "c3f55181de5a4732ad7e19aa938b4d54",String.valueOf( new Date().getTime()), "1",String.valueOf( new Date().getTime()), "20170725142322", "wxfb28dce18bb0b0f4", "ozv12whzTsoD8AynIXGMSaIJtP5E");
	}
	
}
