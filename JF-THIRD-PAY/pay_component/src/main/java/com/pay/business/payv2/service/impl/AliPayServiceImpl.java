package com.pay.business.payv2.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.service.Payv2PayGoodsService;
import com.pay.business.order.service.Payv2PayOrderGroupService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.service.AliPayService;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.PayFinalUtil;
import com.pay.business.util.PayRateDictValue;
import com.pay.business.util.RandomUtil;
import com.pay.business.util.alipay.AliPay;
import com.pay.business.util.alipay.AliPayConfig;
import com.pay.business.util.kftpay.KftPay;
import com.pay.business.util.kftpay.KftUrlConfig;
import com.pay.business.util.mail.MailRun;
import com.pay.business.util.minshengbank.HttpMinshengBank;
import com.pay.business.util.pinganbank.config.TestParams;
import com.pay.business.util.pinganbank.pay.PABankPay;
import com.pay.business.util.swt.SwtPayOrder;
import com.pay.business.util.swt.SwtUrlConfig;
import com.pay.business.util.tcpay.pay.TcPay;
import com.pay.business.util.StringUtil;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;
import com.pay.business.util.xyShenzhen.XYSZBankPay;
import com.pay.business.util.ympay.YmPay;

/**
 * @Title: AliPayServiceImpl.java
 * @Package com.pay.business.payv2.service.impl
 * @Description:扫码支付:支付宝扫码，微信扫码
 * @author ZHOULIBO
 * @date 2017年6月30日 上午10:05:22
 * @version V1.0
 */
@Service("aliPayService")
public class AliPayServiceImpl implements AliPayService {
	private static final Log logger = LogFactory.getLog(AliPayServiceImpl.class);
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;// 支付通道
	@Autowired
	private Payv2PayOrderMapper payv2PayOrderMapper;
	@Autowired
	private Payv2PayGoodsService payv2PayGoodsService;
	@Autowired
	private Payv2PayOrderGroupService payv2PayOrderGroupService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;

	/**
	 * 扫码支付-创建订单与返回商户调起的支付宝的参数条件
	 */
	@Override
	public Map<String, String> aliPaycreatePayAndOreder(Payv2BussCompanyApp pbca, Integer payViewType, Map<String, Object> map) throws Exception {
		// 创建订单或者查询订单是否存在再给支付宝
		//3为扫码
		Map<String, String> orderMap = payv2PayOrderService.dyCreateOrder(map, pbca, payViewType, 3);
		// 成功
		if (orderMap.get("status").equals(PayFinalUtil.PAY_STATUS_SUSSESS)) {
			/**
			 * 支付宝扫码支付通道
			 */
			if (orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_ALIPAY_SMZL)) {
				logger.info("=======>扫码支付：走了支付宝扫码支付通道<=======");
				// 调起支付宝预下单接口
				String APPID = orderMap.get("rateKey1");
				String PKCS8_PRIVATE =orderMap.get("rateKey2");
				String ALIPAY_RSA_PUBLIC =orderMap.get("rateKey3");
				if (APPID != null && PKCS8_PRIVATE != null && ALIPAY_RSA_PUBLIC != null) {
					// 支付集订单号
					String orderNum =orderMap.get("orderNum");
					// 订单名字:改为订单号
//					String orderName = orderMap.get("orderName");
					String orderName =orderNum;
					// 金额
					double payMoney = Double.valueOf(orderMap.get("payMoney"));
					// 30分失效
					int timeOut = 30;
					Map<String, String> aliPayMap = AliPay.aliTradePrecreatePay(orderNum, orderName, payMoney, timeOut, APPID, PKCS8_PRIVATE, ALIPAY_RSA_PUBLIC);
					if (Integer.valueOf(aliPayMap.get("code").toString()) != 10000) {
						MailRun.send(orderMap.get("dictName"), aliPayMap.get("msg")
								,orderNum,orderMap.get("rateId"),pbca.getAppName()
								,orderMap.get("payMoney"),orderMap.get("companyName"),orderMap.get("rateCompanyName")
								,orderMap.get("payWayName"));
					}
					return aliPayMap;
				} else {
					logger.error("=======>支付宝扫码支付：相关数据库配置不完整：rateKey1，rateKey2，rateKey3");
					return orderMap;
				}
			}
			/**
			 * 兴业银行支付宝扫码支付通道
			 */
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_XYBANk_ALI_SCAN)){
				logger.info("=======>扫码支付：走了兴业银行支付宝扫码支付通道<=======");
				String appid=orderMap.get("rateKey1");
				String mch_id=orderMap.get("rateKey2");
				String key=orderMap.get("rateKey3");
				if(appid!=null&&mch_id!=null&&key!=null){
					// 支付集订单号
					String out_trade_no =orderMap.get("orderNum");
					// 金额
					String total_fee= DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
					// 订单名字:改为订单号
//					String body =orderMap.get("orderName");
					String body =out_trade_no;
					Map<String, String> aliPayMap =AliPay.xyBankAliaScanPay(out_trade_no, total_fee, body, appid, mch_id, key);
					if (Integer.valueOf(aliPayMap.get("code").toString()) != 10000) {
						MailRun.send(orderMap.get("dictName"), aliPayMap.get("msg")
								,out_trade_no,orderMap.get("rateId"),pbca.getAppName()
								,orderMap.get("payMoney"),orderMap.get("companyName"),orderMap.get("rateCompanyName")
								,orderMap.get("payWayName"));
					}
					return aliPayMap;
				}else{
					logger.error("=======>兴业银行支付宝扫码支付通道：相关数据库配置不完整：rateKey1，rateKey2，rateKey3");
					return orderMap;
				}
			}
			/**
			 * 兴业银行：微信扫码支付
			 */
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_SCAN)){
				logger.info("=====》扫码支付：支付走了：兴业银行：微信扫码支付通道");
				String appid=String.valueOf(orderMap.get("rateKey1"));
				String mch_id=String.valueOf(orderMap.get("rateKey2"));
				String key=String.valueOf(orderMap.get("rateKey3"));
				if(appid!=null&&mch_id!=null&&key!=null){
					String orderNum = String.valueOf(orderMap.get("orderNum"));
					String ip = String.valueOf(map.get("address"));
					String totalFee = DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
//					String body= String.valueOf(orderMap.get("orderName"));
					String body=orderNum;
					//支付来源：1:公众号支付：2扫码支付
					int fromType=2;
					Map<String, String> aliPayMap=XyBankPay.xYBankWechatGzhPay(orderNum, ip, totalFee, body, appid, mch_id, key,null,null,fromType);
					//成功
					if (Integer.valueOf(aliPayMap.get("code").toString()) != 10000) {
						MailRun.send(orderMap.get("dictName"), aliPayMap.get("msg")
								,orderNum,orderMap.get("rateId"),pbca.getAppName()
								,orderMap.get("payMoney"),orderMap.get("companyName"),orderMap.get("rateCompanyName")
								,orderMap.get("payWayName"));
					}
					return aliPayMap;
				}else{
					logger.error("=======>兴业银行：微信扫码支付通道：相关数据库配置不完整：rateKey1，rateKey2，rateKey3");
					return orderMap;
				}
			}
			/**
			 * 民生银行支付宝扫码支付
			 */
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_MSBANk_ALI_SCAN)){
				logger.info("=======>扫码支付：走了民生银行支付宝扫码支付通道<=======");
				Map<String,Object> paramMap=new HashMap<String, Object>();
				//商户号
				String merNo=orderMap.get("rateKey1");
				//商户key
				String bankSecretKey=orderMap.get("rateKey2");
				if(merNo!=null&&bankSecretKey!=null){
					//商户id
					paramMap.put("merNo",merNo);
					//支付集订单号
					paramMap.put("orderNo", orderMap.get("orderNum"));
					//支付宝扫码支付渠道
					paramMap.put("channelFlag","01");
					//商品描述
//					paramMap.put("goodsName",orderMap.get("orderName"));
					paramMap.put("goodsName",orderMap.get("orderNum"));
					// 金额
					String amount=DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
					paramMap.put("amount",amount);
					//交易流水号
					paramMap.put("reqId",new Date().getTime()+RandomUtil.generateString(4));
					//交易时间
					paramMap.put("reqTime",DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"));
					Map<String, String> aliPayMap=HttpMinshengBank.minshengBankCallBack(paramMap, bankSecretKey);
					if (Integer.valueOf(aliPayMap.get("code").toString()) != 10000) {
						MailRun.send(orderMap.get("dictName"), aliPayMap.get("msg")
								,orderMap.get("orderNum"),orderMap.get("rateId"),pbca.getAppName()
								,orderMap.get("payMoney"),orderMap.get("companyName"),orderMap.get("rateCompanyName")
								,orderMap.get("payWayName"));
					}
					return aliPayMap;
				}else{
					logger.error("=======>民生银行支付宝扫码支付通道：相关数据库配置不完整：rateKey1，rateKey2，rateKey3");
					return orderMap;
				}
			}
			/**
			 * 民生银行微信扫码支付
			 */
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_SCAN)){
				logger.info("=======>扫码支付：走了民生银行微信扫码支付支付通道<=======");
				//商户号
				String merNo=orderMap.get("rateKey1");
				//商户key
				String bankSecretKey=orderMap.get("rateKey2");
				if(merNo!=null&&bankSecretKey!=null){
					Map<String,Object> paramMap=new HashMap<String, Object>();
					//商户id
					paramMap.put("merNo",merNo);
					//支付集订单号
					paramMap.put("orderNo", orderMap.get("orderNum"));
					//微信扫码支付渠道
					paramMap.put("channelFlag","00");
					//商品描述
//					paramMap.put("goodsName",orderMap.get("orderName"));
					paramMap.put("goodsName",orderMap.get("orderNum"));
					// 金额
					String amount=DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
					paramMap.put("amount",amount);
					//交易流水号
					paramMap.put("reqId",new Date().getTime()+RandomUtil.generateString(4));
					//交易时间
					paramMap.put("reqTime",DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"));
					Map<String, String> aliPayMap=HttpMinshengBank.minshengBankCallBack(paramMap, bankSecretKey);
					if (Integer.valueOf(aliPayMap.get("code").toString()) != 10000) {
						MailRun.send(orderMap.get("dictName"), aliPayMap.get("msg")
								,orderMap.get("orderNum"),orderMap.get("rateId"),pbca.getAppName()
								,orderMap.get("payMoney"),orderMap.get("companyName"),orderMap.get("rateCompanyName")
								,orderMap.get("payWayName"));
					}
					return aliPayMap;
				}else{
					logger.error("=======>民生银行微信扫码支付通道：相关数据库配置不完整：rateKey1，rateKey2，rateKey3");
					
					return orderMap;
				}
			}
			/**
			 * 民生银行QQ扫码支付
			 */
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_MSBANk_QQ_SCAN)){
				logger.info("=======>扫码支付：走了民生银行QQ扫码支付支付通道<=======");
				//商户号
				String merNo=orderMap.get("rateKey1");
				//商户key
				String bankSecretKey=orderMap.get("rateKey2");
				if(merNo!=null&&bankSecretKey!=null){
					Map<String,Object> paramMap=new HashMap<String, Object>();
					//商户id
					paramMap.put("merNo",merNo);
					//支付集订单号
					paramMap.put("orderNo", orderMap.get("orderNum"));
					//微信扫码支付渠道
					paramMap.put("channelFlag","04");
					//商品描述
					paramMap.put("goodsName",orderMap.get("orderNum"));
					// 金额
					String amount=DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
					paramMap.put("amount",amount);
					//交易流水号
					paramMap.put("reqId",new Date().getTime()+RandomUtil.generateString(4));
					//交易时间
					paramMap.put("reqTime",DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"));
					Map<String, String> aliPayMap=HttpMinshengBank.minshengBankCallBack(paramMap, bankSecretKey);
					if (Integer.valueOf(aliPayMap.get("code").toString()) != 10000) {
						MailRun.send(orderMap.get("dictName"), aliPayMap.get("msg")
								,orderMap.get("orderNum"),orderMap.get("rateId"),pbca.getAppName()
								,orderMap.get("payMoney"),orderMap.get("companyName"),orderMap.get("rateCompanyName")
								,orderMap.get("payWayName"));
					}
					return aliPayMap;
				}else{
					logger.error("=======>民生银行微信扫码支付通道：相关数据库配置不完整：rateKey1，rateKey2，rateKey3");
					
					return orderMap;
				}
			}
			
			//这里加进兴业深圳微信扫码支付
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_SCAN))
			{
				String OPEN_ID=orderMap.get("rateKey1");
				String OPEN_KEY=orderMap.get("rateKey2");
				String out_trade_no = orderMap.get("orderNum");
				String total_fee = DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
				String body = orderMap.get("orderName");
				return XYSZBankPay.xySZWFTWXScanPay(out_trade_no, total_fee, body, "119.137.35.50", OPEN_ID, OPEN_KEY);
			}
			
			//这里加进兴业深圳支付宝扫码支付
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_XYSZ_ALI_SCAN))
			{
				String OPEN_ID=orderMap.get("rateKey1");
				String OPEN_KEY=orderMap.get("rateKey2");
				String out_trade_no = orderMap.get("orderNum");
				String total_fee = DecimalUtil.yuanToCents(orderMap.get("payMoney").toString());
				String body = orderMap.get("orderName");
				return XYSZBankPay.xySZWFTAliaScanPay(out_trade_no, total_fee, body, "119.137.35.50", OPEN_ID, OPEN_KEY);
			}
			
			/**
			 * 走了通财支付的QQ扫码
			 */
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_TCPAY_QQ_SCAN))
			{
				String payType = "QQR"; //QQR 微信扫码
				String orderNo =  orderMap.get("orderNum");
				int price = StringUtil.getInteger(DecimalUtil.yuanToCents(orderMap.get("payMoney").toString()),1);
				String appID = orderMap.get("rateKey1");
				String openKey  = orderMap.get("rateKey2");
				String productNo = orderMap.get("rateKey3");
				String returnUrl = "http://www.baidu.com/";
				return TcPay.tpPayOrder(payType, orderNo, productNo, price, appID, openKey, returnUrl);
			}
			
			/**
			 * 走了溢美支付
			 */
			if (orderMap.get("dictName")
					.equals(PayRateDictValue.PAY_TYPE_YM_WEIXIN_SCAN)
					|| orderMap.get("dictName")
							.equals(PayRateDictValue.PAY_TYPE_YM_ALI_SCAN)
					|| orderMap.get("dictName")
							.equals(PayRateDictValue.PAY_TYPE_YM_QQ_SCAN))
			{
				String orderNo =  orderMap.get("orderNum");
				
				int price = StringUtil.getInteger(DecimalUtil.yuanToCents(orderMap.get("payMoney").toString()),1);
				
				String merCode = orderMap.get("rateKey1");
				
				String skey  = orderMap.get("rateKey2");
				
				String returnUrl = "http://www.baidu.com/";
				
				int payType = 1;
				
				if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_YM_WEIXIN_SCAN))
					payType= 1;
				else if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_YM_ALI_SCAN))
					payType= 2;
				else if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_YM_QQ_SCAN))
					payType = 3;
				
				
				return YmPay.queryOrder(merCode, skey, payType, orderNo, price, returnUrl);
			}
			
			/**
			 * 走了商务通的扫码 
			 */
			if (orderMap.get("dictName")
					.equals(PayRateDictValue.PAY_TYPE_SWT_WEIXIN_SCAN)
					|| orderMap.get("dictName")
							.equals(PayRateDictValue.PAY_TYPE_SWT_ALI_SCAN)
					|| orderMap.get("dictName")
							.equals(PayRateDictValue.PAY_TYPE_SWT_QQ_SCAN))
			{
				String merchantId = orderMap.get("rateKey1");
				String subChnMerno = orderMap.get("rateKey2");
				String skey = orderMap.get("rateKey3");
				int payType = 1;
				
				if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_SWT_WEIXIN_SCAN))
					payType= 1;
				else if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_SWT_ALI_SCAN))
					payType= 2;
				else if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_SWT_QQ_SCAN))
					payType = 3;
				
				String orderNo = orderMap.get("orderNum");
				String price = "" + StringUtil.getInteger(DecimalUtil.yuanToCents(orderMap.get("payMoney").toString()),1);;
				String product = orderMap.get("orderNum");
				String ip = String.valueOf(map.get("ip"));
				String notifyUrl = SwtUrlConfig.NOTIFY_URL; 
				String returnUrl = ""; 
				String userCode = "";
				return SwtPayOrder.queryOrder(merchantId, subChnMerno, skey, payType, orderNo, price, product, ip, notifyUrl, returnUrl, userCode);
			}
			
			/**
			 * 走了快付通的扫码 
			 */
			if (orderMap.get("dictName")
					.equals(PayRateDictValue.PAY_TYPE_KFT_QQ_SCAN)
					|| orderMap.get("dictName")
							.equals(PayRateDictValue.PAY_TYPE_KFT_WEIXIN_SCAN)
					|| orderMap.get("dictName")
							.equals(PayRateDictValue.PAY_TYPE_KFT_ALI_SCAN))
			{
				String merchantId = orderMap.get("rateKey1");
				
				String secMerchantId = orderMap.get("rateKey2");
				
				int payType = 1;
				
				if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_KFT_QQ_SCAN))
					payType= 1;
				else if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_KFT_WEIXIN_SCAN))
					payType= 2;
				else if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_KFT_ALI_SCAN))
					payType = 3;
				
				String orderNo = orderMap.get("orderNum");
				
				String price = "" + StringUtil.getInteger(DecimalUtil.yuanToCents(orderMap.get("payMoney").toString()),1);
				
				String product = orderMap.get("orderNum");
				
				String ip = String.valueOf(map.get("ip"));
				
				String returnUrl = ""; 
				
				String userCode = "";
				
				return KftPay.queryOrder(merchantId, secMerchantId, payType, orderNo, price, product, ip, returnUrl, userCode);
			}
			
			
			/**
			 * 平安银行：微信 ，支付宝，扫码支付,公众号特殊支付
			 */
			if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_SCAN)
					||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_ALI_SCAN)
					||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN)
					||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_GZH_WEIXIN_SCAN)
					||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PA_BANK_QQ_SCAN)
					||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PA_BANK_WX_H5_SCAN)) 
			{
				
				String OPEN_ID=orderMap.get("rateKey1");
				String OPEN_KEY=orderMap.get("rateKey2");
				if(OPEN_ID!=null&&OPEN_KEY!=null){
					String outNo = orderMap.get("orderNum");
					String pmtTag = "";
					if (orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_SCAN)
							||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN)
							||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_GZH_WEIXIN_SCAN)
							||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PA_BANK_QQ_SCAN)
							||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PA_BANK_WX_H5_SCAN)) {
						// 微信:这里是微信扫码
						pmtTag = "Weixin";
					}
					if (orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_ALI_SCAN)) {
						// 支付宝为：这里是支付宝扫码
						pmtTag = "AlipayPAZH";
					}
					if (orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PA_BANK_QQ_SCAN)) {
						// 支付宝为：这里是支付宝扫码
						pmtTag = "QQ_SCAN";
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
					if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PABANk_GZH_WEIXIN_SCAN)
							||orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN)){
						logger.info("=======>走了：平安银行特殊公众号支付通道<=======");
						jumpUrl=TestParams.JUMP_URL;
					}else{
						logger.info("=======>扫码支付： 平安银行微信 ，支付宝，扫码支付通道<=======");
					}
					
					// 3、是Andy增加进去扫码破解的类型
					int qxPayType = 1;
					
					if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN))
					{
						qxPayType = 3;
					}
					
					String ip = String.valueOf(map.get("ip"));
					
					if(orderMap.get("dictName").equals(PayRateDictValue.PAY_TYPE_PA_BANK_WX_H5_SCAN))
					{
						qxPayType = 4;
						if(ip==null || "".equals(ip))
							ip = String.valueOf(map.get("address"));
						System.out.println("Andy Tag IP:" + ip);
					}
					
					
					Map<String, String> paMap = PABankPay.queryOrder(outNo, pmtTag, null, ordName, Integer.valueOf(originalAmount), null, null,
							Integer.valueOf(tradeAmount), null, null, null, null, null, null, jumpUrl, notifyUrl,OPEN_ID,OPEN_KEY,null,null,ip,qxPayType);
					if (Integer.valueOf(paMap.get("code").toString()) != 10000) {
						MailRun.send(orderMap.get("dictName"), paMap.get("msg"), orderMap.get("orderNum"), orderMap.get("rateId"), pbca.getAppName(),
								orderMap.get("payMoney"), orderMap.get("companyName"), orderMap.get("rateCompanyName"), orderMap.get("payWayName"));
					}
					return paMap;
				}else{
					logger.error("=======>平安银行：微信 ，支付宝，扫码支付付通道：相关数据库配置不完整：rateKey1，rateKey2，rateKey5");
					return orderMap;
				}
			}
		} else {
			// 直接返回错误信息
			return orderMap;
		}
		return orderMap;
	}
	/**
	 * 回调验签
	 */
	public boolean verify(Map<String, String> map,String ALIPAY_RSA_PUBLIC) {
		try {
			return AlipaySignature.rsaCheckV1(map,ALIPAY_RSA_PUBLIC, AliPayConfig.CHARSET, AliPayConfig.SIGN_TYPE);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return false;
	}
}
