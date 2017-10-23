package com.pay.business.util;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.http.HttpUtil;
import com.core.teamwork.base.util.properties.PropertiesUtil;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.pay.business.util.alipay.AliPay;
import com.pay.business.util.alipay.AppAliPay;
import com.pay.business.util.alipay.PayConfigApi;
import com.pay.business.util.guofu.GuoFuPay;
import com.pay.business.util.hfbpay.HfbPay;
import com.pay.business.util.mail.MailRun;
import com.pay.business.util.minshengbank.HttpMinshengBank;
import com.pay.business.util.pinganbank.config.TestParams;
import com.pay.business.util.pinganbank.pay.PABankPay;
import com.pay.business.util.wftpay.WftWeChatPay;
import com.pay.business.util.xyShenzhen.XYSZBankPay;

/**
 * 支付字典的值（对应数据库）
 * @author Administrator
 *
 */
public class PayRateDictValue {
	/**
	 * 接入支付类型
	 */
	public static final String PAY_TYPE ="PAY_TYPE";
	/**
	 * 支付宝
	 */
	public static final String PAY_TYPE_ALIPAY ="PAY_TYPE_ALIPAY";
	/**
	 * 微信支付
	 */
	public static final String PAY_TYPE_WEIXIN ="PAY_TYPE_WEIXIN";
	/**
	 * 威富通微信wap支付
	 */
	public static final String PAY_TYPE_WFT_WEIXIN ="PAY_TYPE_WFT_WEIXIN_WAP";
	/**
	 * 支付宝app支付
	 */
	public static final String PAY_TYPE_ALIPAY_APP ="PAY_TYPE_ALIPAY_APP";
	/**
	 * 支付宝条码支付
	 */
	public static final String PAY_TYPE_ALIPAY_SCAN ="PAY_TYPE_ALIPAY_SCAN";
	/**
	 * 微信刷卡支付
	 */
	public static final String PAY_TYPE_WEIXIN_SCAN ="PAY_TYPE_WEIXIN_SCAN";
	/**
	 * 支付宝web支付
	 */
	public static final String PAY_TYPE_ALIPAY_WEB ="PAY_TYPE_ALIPAY_WEB";
	/**
	 * 有氧支付宝app支付
	 */
	public static final String PAY_TYPE_ALIPAY_YY ="PAY_TYPE_ALIPAY_YY";
	/**
	 * 支付宝扫码支付直连通道一
	 */
	public static final String PAY_TYPE_ALIPAY_SMZL ="PAY_TYPE_ALIPAY_SCAN";
	/**
	 * 汇付宝微信h5支付
	 */
	public static final String PAY_TYPE_HFB_WX_WEB ="PAY_TYPE_HFB_WEIXIN_WEB";
	/**
	 * 汇付宝微信app支付
	 */
	public static final String PAY_TYPE_HFB_WX_SDK ="PAY_TYPE_HFB_WEIXIN_APP";
	/**
	 * 汇付宝支付宝APP支付
	 */
	public static final String PAY_TYPE_HFB_ALI_SDK ="PAY_TYPE_HFB_ALI_APP";
	/**
	 * 汇付宝微信公众号支付
	 */
	public static final String PAY_TYPE_HFB_WEIXIN_GZH ="PAY_TYPE_HFB_WEIXIN_GZH";
	/**
	 * 威富通微信公众号支付
	 */
	public static final String PAY_TYPE_WFT_WEIXIN_GZH ="PAY_TYPE_WFT_WEIXIN_GZH";
	/**
	 * 兴业银行微信公众号支付
	 */
	public static final String PAY_TYPE_XYBANk_WEIXIN_GZH ="PAY_TYPE_XYBANk_WEIXIN_GZH";
	/**
	 * 兴业银行支付宝扫码支付
	 */
	public static final String PAY_TYPE_XYBANk_ALI_SCAN ="PAY_TYPE_XYBANk_ALI_SCAN";
	/**
	 * 兴业银行微信扫码支付
	 */
	public static final String PAY_TYPE_XYBANk_WEIXIN_SCAN ="PAY_TYPE_XYBANk_WEIXIN_SCAN";
	/**
	 * 民生银行支付宝扫码支付
	 */
	public static final String PAY_TYPE_MSBANk_ALI_SCAN="PAY_TYPE_MSBANk_ALI_SCAN";
	/**
	 * 民生银行微信扫码支付
	 */
	public static final String PAY_TYPE_MSBANk_WEIXIN_SCAN="PAY_TYPE_MSBANk_WEIXIN_SCAN";
	/**
	 * 民生银行微信公众号支付
	 */
	public static final String PAY_TYPE_MSBANk_WEIXIN_GZH="PAY_TYPE_MSBANk_WEIXIN_GZH";
	/**
	 * 平安银行微信扫码支付
	 */
	public static final String PAY_TYPE_PABANk_WEIXIN_SCAN="PAY_TYPE_PABANk_WEIXIN_SCAN";
	/**
	 * 平安银行支付宝扫码支付
	 */
	public static final String PAY_TYPE_PABANk_ALI_SCAN="PAY_TYPE_PABANk_ALI_SCAN";
	/**
	 * 平安银行公众号支付（特殊）
	 */
	public static final String PAY_TYPE_PABANk_GZH_WEIXIN_SCAN="PAY_TYPE_PABANk_GZH_WEIXIN_SCAN";
	/**
	 * 平安银行公众号支付
	 */
	public static final String PAY_TYPE_PABANk_WEIXIN_GZH="PAY_TYPE_PABANk_WEIXIN_GZH";
	/**
	 * 平安银行公众号支付(趣讯特殊)
	 */
	public static final String PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN = "PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN";
	/**
	 * 民生银行QQ扫码支付
	 */
	public static final String PAY_TYPE_MSBANk_QQ_SCAN="PAY_TYPE_MSBANk_QQ_SCAN";
	
	/**
	 * 兴业深圳支付宝扫码
	 */
	public static final String PAY_TYPE_XYSZ_ALI_SCAN="PAY_TYPE_XYSZ_ALI_SCAN";
	
	/**
	 * 兴业深圳微信扫码
	 */
	public static final String PAY_TYPE_XYSZ_WEIXIN_SCAN = "PAY_TYPE_XYSZ_WEIXIN_SCAN";
	
	/**
	 * 兴业深圳微信WAP
	 */
	public static final String PAY_TYPE_XYSZ_WEIXIN_WAP = "PAY_TYPE_XYSZ_WEIXIN_WAP";
	
	/**
	 * 兴业深圳微信公众号
	 */
	public static final String PAY_TYPE_XYSZ_WEIXIN_GZH = "PAY_TYPE_XYSZ_WEIXIN_GZH";
	
	/**
	 * 国付QQ被扫码
	 */
	public static final String PAY_TYPE_GUOFU_PASSIVE_QQ_SCAN = "PAY_TYPE_GUOFU_PASSIVE_QQ_SCAN";
	
	/**
	 * 调用支付通道
	 * @param dictName
	 * @param orderMap
	 * @param appId
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> ratePay(String dictName,Map<String,String> orderMap
			,String ip,String appType,Integer payType) throws Exception{
		Map<String, String> resultMap = new HashMap<>();
		String orderNum = orderMap.get("orderNum").toString();
		double payMoney = Double.valueOf(orderMap.get("payMoney"));
		String yuanToCents = DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
		//String orderName = orderMap.get("orderName").toString();
		String returnUrl = PayConfigApi.H5_RETURN_URL;
		String hfbStr = "";
		//是否公众号支付
		Integer is_GZH = 0;
		switch (dictName) {
			case PAY_TYPE_ALIPAY_APP:
				BigDecimal b = new BigDecimal(payMoney);
				double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				// 调用支付宝支付
				String aliPay = AppAliPay.sign(orderNum, "" + f1
						, orderNum,orderMap.get("rateKey1"),orderMap.get("rateKey2"));
				resultMap.put("aliPay", aliPay);
				break;
			case PAY_TYPE_WFT_WEIXIN:
				
				Map<String, String> wxMap = WftWeChatPay.pay(orderNum,
						orderNum, "" + DecimalUtil.yuanToCents(payMoney+""), ip,appType
						,orderMap.get("rateKey1"),orderMap.get("rateKey2"),dictName,payType);
				if (wxMap.containsKey("token_id")||wxMap.containsKey("pay_info")) {
					resultMap.put("token_id", wxMap.get("token_id"));
					resultMap.put("services", wxMap.get("services"));
					resultMap.put("webStr", wxMap.get("pay_info"));
				} else {
					//发送邮件
					MailRun.send(dictName,"威富通wap下单请求错误",orderNum,resultMap.get("rateId"),resultMap.get("appName")
							,""+payMoney,resultMap.get("companyName"),resultMap.get("rateCompanyName")
							,resultMap.get("payWayName"));
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
			case PAY_TYPE_ALIPAY_WEB:
				String form = AliPay.alipayH5(returnUrl,orderNum, orderNum, payMoney
						,orderMap.get("rateKey1"),orderMap.get("rateKey2"),orderMap.get("rateKey3"));
				resultMap.put("alipayStr", form);
				break;
			case PAY_TYPE_HFB_WX_SDK:
				hfbStr = HfbPay.sdkPay(orderNum, payMoney, ip, orderNum, new Date(),2
						,orderMap.get("rateKey1"),orderMap.get("rateKey2"));
				if("".equals(hfbStr)){
					//发送邮件
					MailRun.send(dictName,"汇付宝微信sdk下单请求错误",orderNum,resultMap.get("rateId"),resultMap.get("appName")
							,""+payMoney,resultMap.get("companyName"),resultMap.get("rateCompanyName")
							,resultMap.get("payWayName"));
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				resultMap.put("hfbStr", hfbStr);
				break;
			case PAY_TYPE_HFB_WX_WEB:
				hfbStr = HfbPay.webPay(orderNum, payMoney, ip, orderNum, new Date(),2
						,orderMap.get("rateKey1"),orderMap.get("rateKey2"));
				if("".equals(hfbStr)){
					//发送邮件
					MailRun.send(dictName,"汇付宝微信web下单请求错误",orderNum,resultMap.get("rateId"),resultMap.get("appName")
							,""+payMoney,resultMap.get("companyName"),resultMap.get("rateCompanyName")
							,resultMap.get("payWayName"));
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				resultMap.put("webStr", hfbStr);
				break;
			case PAY_TYPE_HFB_ALI_SDK:
				hfbStr = HfbPay.sdkPay(orderNum, payMoney, ip, orderNum, new Date(),1
						,orderMap.get("rateKey1"),orderMap.get("rateKey2"));
				if("".equals(hfbStr)){
					//发送邮件
					MailRun.send(dictName,"汇付宝支付宝sdk下单请求错误",orderNum,resultMap.get("rateId"),resultMap.get("appName")
							,""+payMoney,resultMap.get("companyName"),resultMap.get("rateCompanyName")
							,resultMap.get("payWayName"));
					
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				resultMap.put("hfbStr", hfbStr);
				break;
			case PAY_TYPE_ALIPAY_SMZL:
				// 30分失效
				int timeOut = 30;
				String APPID = orderMap.get("rateKey1");
				String PKCS8_PRIVATE =orderMap.get("rateKey2");
				String ALIPAY_RSA_PUBLIC =orderMap.get("rateKey3");
				if (APPID != null && PKCS8_PRIVATE != null && ALIPAY_RSA_PUBLIC != null) {
					Map<String, String> aliPayMap = AliPay.aliTradePrecreatePay(orderNum, orderNum, payMoney, timeOut, APPID, PKCS8_PRIVATE, ALIPAY_RSA_PUBLIC);
					if(!StringUtils.isEmpty(aliPayMap.get("qr_code"))){
						resultMap.put("webStr", aliPayMap.get("qr_code"));
					}else{
						resultMap.put("status", PayFinalUtil.RATE_FAIL);
						return resultMap;
					}
				}
				if(resultMap.get("webStr")==null||resultMap.get("webStr").equals("")){
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
			case PAY_TYPE_WFT_WEIXIN_GZH:
				is_GZH = 1;
				/*SortedMap<String, String> Sortedmap=new TreeMap<String,String>();
				//支付集订单号
				Sortedmap.put("out_trade_no",orderMap.get("orderNum"));
				//商品描述
				Sortedmap.put("body",orderMap.get("orderName"));
				double money=Double.valueOf(orderMap.get("payMoney"))*100;
				int money1=(int)money;
				Sortedmap.put("total_fee",String.valueOf(money1));
				//订单生成的机器 IP
				Sortedmap.put("mch_create_ip", ip);
				String mchId = orderMap.get("rateKey1");
				String key = orderMap.get("rateKey2");
				Map<String,String> SwiftMap=SwiftWechatGzhPay.swiftWechatGzhPay(Sortedmap,mchId,key);
				if(String.valueOf(SwiftMap.get("status")).equals("0")){//成功
					String jsonMap=	MapUtil.mapToJson(SwiftMap);
					Map<String,Object> toMap=MapUtil.parseJsonToMap(jsonMap);
					resultMap.put("webStr",toMap.get("pay_url").toString());
				}else{
					resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
					return resultMap;
				}
				if(resultMap.get("webStr")==null||resultMap.get("webStr").equals("")){
					resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
					return resultMap;
				}*/
				break;
			case PAY_TYPE_HFB_WEIXIN_GZH:
				is_GZH = 1;
				/*String agent_bill_id =orderMap.get("orderNum");
				// 订单名字
				String goods_name =orderMap.get("orderName");
				// 金额
				String pay_amt =orderMap.get("payMoney");
				// 支付时间
				String agent_bill_time = GateWayServiceImpl.dateToString(new Date());
				// 商品个数
				String goods_num = "1";
				// 支付说明
				String goods_note = "";
				// 商户自定义 原样返回
				String remark = "";
				
				String rateKey1 = orderMap.get("rateKey1");
				
				String rateKey2 = orderMap.get("rateKey2");
				
				Map<String, String> pay = HfbWeChatPay.pay(agent_bill_id, agent_bill_time, pay_amt, ip, goods_num, goods_name, goods_note, remark, rateKey1,rateKey2);
				if(!StringUtils.isEmpty(pay.get("address"))){
					resultMap.put("webStr", pay.get("address"));
				} else {
					resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
					return resultMap;
				}*/
				break;
			case PAY_TYPE_XYBANk_WEIXIN_GZH:
				is_GZH = 1;
				/*payMoney = payMoney * 100;
				int totalFee = (int)payMoney;
				String appid = orderMap.get("rateKey1");
				String mch_id = orderMap.get("rateKey2");
				String keys = orderMap.get("rateKey3");
				Map<String, String> xYBankWechatGzhPay = XyBankPay.xYBankWechatGzhPay(orderNum, ip, totalFee, orderNum, appid, mch_id, keys);
				if(!StringUtils.isEmpty(xYBankWechatGzhPay.get("address"))){
					resultMap.put("webStr", xYBankWechatGzhPay.get("address"));
				} else {
					resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
					return resultMap;
				}*/
				break;
			case PAY_TYPE_MSBANk_WEIXIN_GZH:
				is_GZH = 1;
				break;
			case PAY_TYPE_XYBANk_ALI_SCAN:
				// 金额
//				payMoney = payMoney * 100;
				//金额
				String total_fee=DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
				Map<String, String> xYBankWechatGzhPay =AliPay.xyBankAliaScanPay(orderNum, total_fee, orderNum, 
						orderMap.get("rateKey1"), orderMap.get("rateKey2"), orderMap.get("rateKey3"));
				
				if(!StringUtils.isEmpty(xYBankWechatGzhPay.get("qr_code"))){
					resultMap.put("webStr", xYBankWechatGzhPay.get("qr_code"));
				} else {
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				
				
				break;
			case PAY_TYPE_MSBANk_ALI_SCAN:	
				
				
			case PAY_TYPE_MSBANk_QQ_SCAN:
				Map<String,Object> paramMap=new HashMap<String, Object>();
				//商户id
				paramMap.put("merNo", orderMap.get("rateKey1"));
				//商户key
				String bankSecretKey=orderMap.get("rateKey2");
				//支付集订单号
				paramMap.put("orderNo", orderMap.get("orderNum"));
				//支付宝扫码支付渠道
				if(PAY_TYPE_MSBANk_QQ_SCAN.equals(dictName)){
					paramMap.put("channelFlag","04");	//qq
				}else {
					paramMap.put("channelFlag","01");
				}
				//商品描述
				paramMap.put("goodsName",orderNum);
				// 金额
//				payMoney = payMoney * 100;
				//金额
				String payMoney1=DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
				paramMap.put("amount",payMoney1);
				//交易流水号
				paramMap.put("reqId",RandomUtil.generateString(13));
				//交易时间
				paramMap.put("reqTime",DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"));
				Map<String, String> minshengBankCallBack = HttpMinshengBank.minshengBankCallBack(paramMap, bankSecretKey);
				
				if(!StringUtils.isEmpty(minshengBankCallBack.get("qr_code"))){
					resultMap.put("webStr", minshengBankCallBack.get("qr_code"));
				}else{
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				if(resultMap.get("webStr")==null||resultMap.get("webStr").equals("")){
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
			case PAY_TYPE_MSBANk_WEIXIN_SCAN:	
				Map<String,Object> paramMaps=new HashMap<String, Object>();
				//商户id
				paramMaps.put("merNo", orderMap.get("rateKey1"));
				//支付集订单号
				paramMaps.put("orderNo", orderMap.get("orderNum"));
				//支付宝扫码支付渠道
				paramMaps.put("channelFlag","00");
				//商品描述
				paramMaps.put("goodsName",orderNum);
				// 金额
//				payMoney = payMoney * 100;
				//金额
				String payMoney2=DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
				paramMaps.put("amount",payMoney2);
				//交易流水号
				paramMaps.put("reqId",RandomUtil.generateString(13));
				//交易时间
				paramMaps.put("reqTime",DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"));
				Map<String, String> minshengBankCallBack2 = HttpMinshengBank.minshengBankCallBack(paramMaps, orderMap.get("rateKey2"));
				
				if(!StringUtils.isEmpty(minshengBankCallBack2.get("qr_code"))){
					resultMap.put("webStr", minshengBankCallBack2.get("qr_code"));
				}else{
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				if(resultMap.get("webStr")==null||resultMap.get("webStr").equals("")){
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
			case PAY_TYPE_PABANk_ALI_SCAN:
			case PAY_TYPE_PABANk_WEIXIN_SCAN:
			case PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN:
			case PAY_TYPE_PABANk_GZH_WEIXIN_SCAN:
				String OPEN_ID=orderMap.get("rateKey1");
				String OPEN_KEY=orderMap.get("rateKey2");
				String pmtTag = "";
				if (orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_SCAN)
						|| orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_GZH_WEIXIN_SCAN)) {
					// 微信:这里是微信扫码
					pmtTag = "Weixin";
				}
				if (orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_ALI_SCAN)) {
					// 支付宝为：这里是支付宝扫码
					pmtTag = "AlipayPAZH";
				}
				String ordName = orderMap.get("orderNum");
				// 金额
				String originalAmount = DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
				// 实际金额
				String tradeAmount = DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
				// 异步回调URL
				String notifyUrl = TestParams.NOTIFY_URL;
				//这个参数是区别是否是公众号支付：公众号支付必传参数
				String jumpUrl=null;
				if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_GZH_WEIXIN_SCAN)){
					jumpUrl=TestParams.JUMP_URL;
				}
				
				//3、是Andy增加进去扫码破解的类型
				int qxPayType = 1;
				
				if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN))
				{
					qxPayType = 3;
				}
				
				Map<String, String> paMap = PABankPay.queryOrder(orderNum, pmtTag, null, ordName, Integer.valueOf(originalAmount), null, null,
						Integer.valueOf(tradeAmount), null, null, null, null, null, null, jumpUrl, notifyUrl,OPEN_ID,OPEN_KEY,null,null,qxPayType);
				if("10000".equals(paMap.get("code"))){
					resultMap.put("webStr", paMap.get("qr_code"));
				}else {
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
				
			case PAY_TYPE_XYSZ_ALI_SCAN:
				Map<String, String> xyBankWFTAliaScanPay = XYSZBankPay.xySZWFTAliaScanPay(orderNum, yuanToCents, orderNum, ip, orderMap.get("rateKey1"), orderMap.get("rateKey2"));
				System.out.println("兴业深圳阿里扫码" + xyBankWFTAliaScanPay.toString());
				if("10000".equals(xyBankWFTAliaScanPay.get("code"))){
					resultMap.put("webStr", xyBankWFTAliaScanPay.get("qr_code"));
				}else {
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
			case PAY_TYPE_XYSZ_WEIXIN_SCAN:
				Map<String, String> xyBankWFTWXScanPay = XYSZBankPay.xySZWFTWXScanPay(orderNum, yuanToCents, orderNum, ip, orderMap.get("rateKey1"), orderMap.get("rateKey2"));
				System.out.println("兴业深圳微信扫码" + xyBankWFTWXScanPay.toString());
				if("10000".equals(xyBankWFTWXScanPay.get("code"))){
					resultMap.put("webStr", xyBankWFTWXScanPay.get("qr_code"));
				}else {
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
			case PAY_TYPE_XYSZ_WEIXIN_WAP:
				Map<String, String> xySZWFTWXWapPay = XYSZBankPay.xySZWFTWXWapPay(orderNum, orderNum, yuanToCents, ip, appType, orderMap.get("rateKey1"), orderMap.get("rateKey2"), payType);
				System.out.println("兴业深圳微信WAP" + xySZWFTWXWapPay.toString());
				if (xySZWFTWXWapPay.containsKey("token_id")||xySZWFTWXWapPay.containsKey("pay_info")) {
					resultMap.put("token_id", xySZWFTWXWapPay.get("token_id"));
					resultMap.put("services", xySZWFTWXWapPay.get("services"));
					resultMap.put("webStr", xySZWFTWXWapPay.get("pay_info"));
				}else {
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
			case PAY_TYPE_XYSZ_WEIXIN_GZH:
				is_GZH = 1;
				break;
				
			case PAY_TYPE_PABANk_WEIXIN_GZH:
				is_GZH = 1;
				break;
				
			case PAY_TYPE_GUOFU_PASSIVE_QQ_SCAN:
				Map<String, String> passivePay = GuoFuPay.passivePay(orderMap.get("rateKey1"), orderMap.get("payMoney"), orderNum, "8", "1", orderMap.get("rateKey2"));
				System.out.println("国付QQ被扫：" + passivePay.toString());
				if("10000".equals(passivePay.get("code"))){
					resultMap.put("webStr", passivePay.get("qr_code"));
				}else {
					resultMap.put("msg", passivePay.get("msg"));
					resultMap.put("status", PayFinalUtil.RATE_FAIL);
					return resultMap;
				}
				break;
			default:
				break;
		}
		//公众号支付时调用加密
		if(is_GZH == 1) {
			orderMap.put("address", ip);
			String property = PropertiesUtil.getProperty("rate", "wechat_gzh_pj_url");
			property += "?orderNum=" + orderNum;
			String result = HttpUtil.HttpGet("http://wx.api-export.com/api/waptoweixin?key=bc8231705e3965376fc063d4959a9dde&f=json&url=" 
					+ URLEncoder.encode(property), null);
			if(result.equals("")){
				resultMap.put("status", PayFinalUtil.RATE_FAIL);
				return resultMap;
			}
			RedisDBUtil.redisDao.hmset(orderNum, orderMap);
			JSONObject jsonObject = JSONObject.parseObject(result);
			String lmResult = jsonObject.getString("ticket_url");
			resultMap.put("webStr", lmResult);
		}
		resultMap.put("orderNum", orderNum);
		resultMap.put("status", PayFinalUtil.PAY_STATUS_SUSSESS);
		return resultMap;
	}
	
}
