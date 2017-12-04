package com.pay.business.order.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.response.AlipayTradeRefundResponse;
import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.pay.UtilDate;
import com.core.teamwork.base.util.properties.PropertiesUtil;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.entity.Payv2PayOrderRefundVO;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.mapper.Payv2PayOrderRefundMapper;
import com.pay.business.order.service.Payv2PayOrderRefundService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.mapper.Payv2PayWayMapper;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.record.entity.Payv2DayCompanyClear;
import com.pay.business.sysconfig.entity.SysConfigDictionary;
import com.pay.business.sysconfig.mapper.SysConfigDictionaryMapper;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.PayFinalUtil;
import com.pay.business.util.PayRateDictValue;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.alipay.AliPay;
import com.pay.business.util.alipay.AppAliPay;
import com.pay.business.util.hfbpay.WeChatSubscrip.pay.HfbWeChatPay;
import com.pay.business.util.minshengbank.HttpMinshengBank;
import com.pay.business.util.pinganbank.pay.PABankPay;
import com.pay.business.util.pinganbank.util.TLinxSHA1;
import com.pay.business.util.wftpay.WftWeChatPay;
import com.pay.business.util.wftpay.weChatSubscrPay.pay.SwiftWechatGzhPay;
import com.pay.business.util.wxpay.WeChatPay;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;
import com.pay.business.util.xyShenzhen.XYSZBankPay;

/**
 * @author cyl
 * @version
 */
@Service("payv2PayOrderRefundService")
public class Payv2PayOrderRefundServiceImpl extends BaseServiceImpl<Payv2PayOrderRefund, Payv2PayOrderRefundMapper> implements Payv2PayOrderRefundService {
	// 注入当前dao对象
	@Autowired
	private Payv2PayOrderRefundMapper payv2PayOrderRefundMapper;
	@Autowired
	private Payv2PayOrderMapper payv2PayOrderMapper;
	@Autowired
	private Payv2PayWayMapper payv2PayWayMapper;
	@Autowired
	private SysConfigDictionaryMapper sysConfigDictionaryMapper;
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;

	public Payv2PayOrderRefundServiceImpl() {
		setMapperClass(Payv2PayOrderRefundMapper.class, Payv2PayOrderRefund.class);
	}

	public Payv2PayOrderRefund selectSingle(Payv2PayOrderRefund t) {
		return payv2PayOrderRefundMapper.selectSingle(t);
	}

	public List<Payv2PayOrderRefund> selectByObject(Payv2PayOrderRefund t) {
		return payv2PayOrderRefundMapper.selectByObject(t);
	}

	/**
	 * 支付退款操作
	 * 
	 * @throws Exception
	 */
	public Map<String, Object> payRefund(Map<String, Object> map, Long appId, String appSecret) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Map<String, String> responseMap = new HashMap<>();
		Payv2PayOrderRefund orderRefund = new Payv2PayOrderRefund();
		orderRefund.setAppId(appId);

		double refundMoney = Double.valueOf(map.get("refundMoney").toString());
		Payv2PayOrder payOrder = new Payv2PayOrder();
		// 商户传了支付集订单号
		if (map.containsKey("orderNum")) {
			payOrder.setOrderNum(map.get("orderNum").toString());
			payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		} else {// 商户传了商户订单号
			payOrder.setAppId(appId);
			payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString());
			payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		}

		if (payOrder == null) {
			resultMap.put("error_code", "TRANSACTION_DOES_NOT_EXIST"); // 交易不存在
			resultMap.put("error_msg", "交易不存在");
			// resultMap =
			// ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE,null,
			// ReturnMsgTips.ERROR_PARAM_DATA);
			logger.error("参数不能为空,或者有误");
			return resultMap;
		}

		resultMap.put("order_num", payOrder.getOrderNum()); // 支付集订单
		resultMap.put("buss_order_num", payOrder.getMerchantOrderNum()); // 商户订单
		resultMap.put("pay_money", payOrder.getPayMoney().toString()); // 支付金额
		resultMap.put("pay_discount_money", payOrder.getPayDiscountMoney().toString()); // //最终支付金额
		if(null!=payOrder.getPayTime()){
			resultMap.put("pay_time", DateStr(payOrder.getPayTime()==null?payOrder.getCreateTime():payOrder.getPayTime())); // 支付时间// yyyyMMddHHmmss
		}
		resultMap.put("order_name", payOrder.getOrderName()); // 订单名称

		if (payOrder.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS && payOrder.getPayStatus() != 5) {
			resultMap.put("error_code", "ORDER_CANNOT_BE_REFUNDED"); // 订单不可退款
			resultMap.put("error_msg", "订单不可退款");
			// resultMap.put("payOrder", payOrder);
			logger.error("订单未支付或支付失败");
			return resultMap;
		}
		if (payOrder.getRateId() == null) {
			resultMap.put("error_code", "ORDER_ERROR"); // 订单错误
			resultMap.put("error_msg", "订单错误,请联系技术支持");
			// resultMap.put("payOrder", payOrder);
			logger.error("订单错误");
			return resultMap;
		}
		// 查询支付的通道
		Map<String, Object> param = new HashMap<>();
		param.put("id", payOrder.getRateId());
		Payv2PayWayRate ppwr = payv2PayWayRateService.detail(param);
		if (ppwr == null) {
			resultMap.put("error_code", "ORDER_ERROR"); // 订单错误
			resultMap.put("error_msg", "订单错误,请联系技术支持");
			// resultMap.put("payOrder", payOrder);
			logger.error("订单错误");
			return resultMap;
		}

		SysConfigDictionary scd = new SysConfigDictionary();
		scd.setId(ppwr.getDicId().intValue());
		scd = sysConfigDictionaryMapper.selectSingle(scd);
		if (scd == null) {
			resultMap.put("error_code", "PARAMETER_ERROR"); // 参数错误
			resultMap.put("error_msg", "参数错误");
			logger.error("参数不能为空,或者有误");
			return resultMap;
		}
		// 全部退款
		if (map.get("refundType").equals("Y")) {
			orderRefund.setRefundType(1);
			if (payOrder.getPayDiscountMoney().doubleValue() != refundMoney) {
				resultMap.put("error_code", "REFUND_AMOUNT_ERROR"); // 退款金额错误
				resultMap.put("error_msg", "退款金额错误");
				return resultMap;
			}
		} else {
			orderRefund.setRefundType(2);
		}

		param.clear();
		param.put("orderNum", payOrder.getOrderNum());
		// 已退款总金额
		double refundSum = payv2PayOrderRefundMapper.refundSum(param);
		// 判断退款金额是否合法
		if (payOrder.getPayDiscountMoney() - refundSum <= 0) {
			resultMap.put("error_code", "REFUND_AMOUNT_ERROR"); // 退款金额错误
			resultMap.put("error_msg", "退款金额错误");
			logger.error("退款总金额大于订单金额!");
			return resultMap;
		}
		System.out.println("支付方式：" + scd.getDictValue());
		// 支付宝
		boolean yes = false;
		if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_APP) 
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_WEB) 
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_SMZL) 
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_ALI_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_ALI_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_GZH)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_QQ_SCAN)) {

			// 生成退款订单号 RR+appId+orderNum
			orderRefund = createRefundOrder(orderRefund, payOrder, refundMoney, map, null);

			/**
			 * 支付宝扫码退款
			 */
			AlipayTradeRefundResponse response = null;
			if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_SCAN) || scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_WEB)) {
				// 调用支付宝退款
				if (ppwr.getRateKey1() != null && ppwr.getRateKey2() != null && ppwr.getRateKey3() != null) {
					response = AliPay.alipayRefund(payOrder.getOrderNum(), refundMoney, map, orderRefund.getRefundNum(), ppwr.getRateKey1(),
							ppwr.getRateKey2(), ppwr.getRateKey3());
					if (response.isSuccess()) {
						yes = true;
					}
				} else {
					logger.error("调用支付宝退款失败：数据库参数配置不完整-->RateKey1,RateKey2,RateKey3");
				}

			}
			// 支付宝扫码支付退款接口
			else if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_SMZL)) {
				if (ppwr.getRateKey1() != null && ppwr.getRateKey2() != null && ppwr.getRateKey3() != null) {
					response = AliPay.aliScanRefund(payOrder, refundMoney, resultMap, orderRefund.getRefundNum(), ppwr.getRateKey1(), ppwr.getRateKey2(),
							ppwr.getRateKey3());
					if (response.isSuccess()) {
						yes = true;
					}
				} else {
					logger.error("支付宝扫码支付退款失败：数据库参数配置不完整-->RateKey1,RateKey2,RateKey3");
				}
			}
			
			// 兴业银行支付宝扫码支付：退款申请接口
			else if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_ALI_SCAN)) {
				if (null != ppwr.getRateKey1() && null != ppwr.getRateKey2() && null != ppwr.getRateKey3()) {
					// 以分为单位
//					double refundMoney1 = refundMoney * 100;
					// 金额 以分为单位
					String amount=DecimalUtil.yuanToCents(String.valueOf(refundMoney));
					responseMap = AliPay.xyBankScanPayRefund(payOrder.getOrderNum(), ppwr.getRateKey1(), ppwr.getRateKey2(), ppwr.getRateKey3(),
							Integer.valueOf(amount), ppwr.getRateKey2(), orderRefund.getRefundNum());
					if (responseMap.get("code").equals("10000")) {
						yes = true;
					}
				} else {
					logger.error("兴业银行支付宝退款失败：数据库参数配置不完整-->RateKey1,RateKey2,RateKey3");
				}
			}
			// 民生银行支付：退款申请接口
			else if(scd.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_ALI_SCAN)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_SCAN)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_GZH)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_QQ_SCAN)){
				Map<String, Object> paramMap=new HashMap<String, Object>();
				//商户号
				String merNo=ppwr.getRateKey1();
				//key
				String bankSecretKey= ppwr.getRateKey2();
				if(bankSecretKey!=null&&merNo!=null){
					paramMap.put("merNo",merNo);
					//支付集订单号
					paramMap.put("orderNo",payOrder.getOrderNum());
					//渠道
//					paramMap.put("channelFlag","01");
					//金额
					// 以分为单位
//					double refundMoney1 = refundMoney * 100;
//					int refundMoney2=(int)refundMoney1;
					// 金额 以分为单位
					String amount=DecimalUtil.yuanToCents(String.valueOf(refundMoney));
					paramMap.put("money",amount);
					//流水号：这里传我们的退款订单号··以备后面对账需要这个订单号
					paramMap.put("reqId",orderRefund.getRefundNum());
					//请求时间
					paramMap.put("reqTime",DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"));
					
					Map<String, Object> msMap=	HttpMinshengBank.refundMSOrder(paramMap, bankSecretKey);
					if(String.valueOf(msMap.get("code")).equals("10000")){
						yes = true;
						responseMap.put("gmtRefundPay", String.valueOf(paramMap.get("reqTime")));
					}
				}else{
					logger.error("民生银行支付退款失败：数据库参数配置不完整-->RateKey1,RateKey2");
				}
			}
			
			else {
				// 调用支付宝退款
				response = AppAliPay.alipayRefund(payOrder.getOrderNum(), refundMoney, map, orderRefund.getRefundNum(), ppwr.getRateKey1(), ppwr.getRateKey2(),
						ppwr.getRateKey3());
				if (response.isSuccess()) {
					yes = true;
				}
			}
			//成功
			if (yes) {
				logger.info("=======>调用成功");
				// //生成退款订单号 RR+appId+orderNum
				// orderRefund = createRefundOrder(orderRefund, payOrder,
				// refundMoney, map,response.getGmtRefundPay(),1);
				if (response != null) {
					orderRefund.setRefundTime(response.getGmtRefundPay());
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					Date date = sdf.parse(responseMap.get("gmtRefundPay"));
					orderRefund.setRefundTime(date);
				}
				orderRefund.setRefundStatus(1);// 退款成功
				payv2PayOrderRefundMapper.insertByEntity(orderRefund);
				resultMap.put("refund_num", orderRefund.getRefundNum()); // 退款单号
				resultMap.put("refund_money", orderRefund.getRefundMoney()); // 退款金额
				resultMap.put("refund_time", DateStr(orderRefund.getRefundTime())); // 退款时间
																					// yyyyMMddHHmmss
				// 回调商户
			}
			//失败
			else {
				//orderRefund.setRefundStatus(2);
				//payv2PayOrderRefundMapper.updateByEntity(orderRefund);
				resultMap.put("error_code", "REFUND_FAILED"); // 退款失败
				resultMap.put("error_msg", "退款失败");
				System.out.println("调用失败");
				
			}
		}
		/**
		 * 微信退款
		 */
		else if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_WFT_WEIXIN) 
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_WEIXIN_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_WFT_WEIXIN_GZH)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_WEIXIN_GZH)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_WX_WEB)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_WX_SDK)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_ALI_SDK)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_GZH)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYSZ_ALI_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_WAP)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_GZH)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_ALI_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_GZH_WEIXIN_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_GZH)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN)
				||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PA_BANK_QQ_SCAN)) {
			// 生成退款订单号 RR+appId+orderNum
			orderRefund = createRefundOrder(orderRefund, payOrder, refundMoney, map, null);
			//兴业深圳退款
			if (PayRateDictValue.PAY_TYPE_XYSZ_ALI_SCAN.equals(scd.getDictName()) || PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_SCAN.equals(scd.getDictName())
					|| PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_WAP.equals(scd.getDictName()) || PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_GZH.equals(scd.getDictName())) {
				if(ppwr.getRateKey1()!=null&&ppwr.getRateKey2()!=null){
					Map<String, String> refund = XYSZBankPay.refund(payOrder.getOrderNum(), orderRefund.getRefundNum(),
							DecimalUtil.yuanToCents("" + payOrder.getPayDiscountMoney()), "" + DecimalUtil.yuanToCents("" + refundMoney), ppwr.getRateKey1(),
							ppwr.getRateKey2());
					if (refund.containsKey("refund_fee")) {
						// 成功
						publicRefundYes(resultMap, orderRefund);
						
					} else {
						// 退款失败
						publicRefundNo(resultMap, orderRefund);
					}
				}else{
					logger.error("威富通微信wap支付退款失败：数据库参数配置不完整-->RateKey1,RateKey2");
				}
			}
			// 威富通微信wap支付退款
			if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_WFT_WEIXIN)) {
				if(ppwr.getRateKey1()!=null&&ppwr.getRateKey2()!=null){
					Map<String, String> weChatMap = WftWeChatPay.refund(payOrder.getOrderNum(), orderRefund.getRefundNum(),
							DecimalUtil.yuanToCents("" + payOrder.getPayDiscountMoney()), "" + DecimalUtil.yuanToCents("" + refundMoney), ppwr.getRateKey1(),
							ppwr.getRateKey2());
					if (weChatMap.containsKey("refund_fee")) {
						// 成功
						publicRefundYes(resultMap, orderRefund);
						
					} else {
						// 退款失败
						publicRefundNo(resultMap, orderRefund);
					}
				}else{
					logger.error("威富通微信wap支付退款失败：数据库参数配置不完整-->RateKey1,RateKey2");
				}
			}
			// 微信刷卡支付退款
			if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_WEIXIN_SCAN)) {
				Map<String, String> weChatMap = WeChatPay.refund(payOrder.getOrderNum(), orderRefund.getRefundNum(),
						DecimalUtil.yuanToCents("" + payOrder.getPayDiscountMoney()), DecimalUtil.yuanToCents("" + refundMoney));
				if (weChatMap.containsKey("success_key")) {
					// 成功
					publicRefundYes(resultMap, orderRefund);
				} else {
					// 退款失败
					publicRefundNo(resultMap, orderRefund);
				}
			}
			// 兴业银行微信公众号，扫码支付支付退款
			if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_GZH)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_SCAN)) {
				if(ppwr.getRateKey1()!=null&&null!=ppwr.getRateKey2()&&null!=ppwr.getRateKey3()){
					// 以分为单位：
//					double refundMoney1 = refundMoney * 100;
					double refundMoney2 = payOrder.getPayDiscountMoney() * 100;
					String amount=DecimalUtil.yuanToCents(String.valueOf(refundMoney));
					String amount1=DecimalUtil.yuanToCents(String.valueOf(refundMoney2));
					Map<String, String> weChatMap = XyBankPay.xyBankGzhRefund(payOrder.getOrderNum(), ppwr.getRateKey1(), ppwr.getRateKey2(),ppwr.getRateKey3(),amount, amount1, ppwr.getRateKey2(), orderRefund.getRefundNum());
					if (weChatMap.get("code").equals("10000")) {
						// 成功
						publicRefundYes(resultMap, orderRefund);
					} else {
						// 失败
						publicRefundNo(resultMap, orderRefund);
					}
				}else{
					logger.error("兴业银行微信公众号，扫码支付支付退款失败：数据库参数配置不完整-->RateKey1,RateKey2,RateKey3");
				}
			}
			// 威富通微信公众号支付退款
			if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_WFT_WEIXIN_GZH)) {
				if(ppwr.getRateKey1()!=null&&null!=ppwr.getRateKey2()){
					// 以分为单位：
					// 退款的钱
//					double refundMoney1 = refundMoney * 100;
					// 使用支付的钱
					double refundMoney2 = payOrder.getPayDiscountMoney() * 100;
					String amount=DecimalUtil.yuanToCents(String.valueOf(refundMoney));
					String amount1=DecimalUtil.yuanToCents(String.valueOf(refundMoney2));
					Map<String, String> weChatMap = SwiftWechatGzhPay.refund(ppwr.getRateKey1(), ppwr.getRateKey2(), payOrder.getOrderNum(),orderRefund.getRefundNum(), amount, amount1, ppwr.getRateKey1());
					if (weChatMap.get("code").equals("10000")) {
						// 成功
						publicRefundYes(resultMap, orderRefund);
					} else {
						// 失败
						publicRefundNo(resultMap, orderRefund);
					}
				}else{
					logger.error("威富通微信公众号支付退款失败：数据库参数配置不完整-->RateKey1,RateKey2");
				}
			}
			//汇付宝微信公众号支付退款
			if(scd.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_WEIXIN_GZH)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_WX_WEB)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_WX_SDK)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_ALI_SDK)){
				if(ppwr.getRateKey1()!=null&&ppwr.getRateKey3()!=null){
					String notify_url=PropertiesUtil.getProperty("rate", "hfb_weixin_refund_notify_url");
					//格式为：商户系统内部的定单号（要保证唯一），
					//商户退款单号可为空，如果传了要保证唯一，
					//支持批量退款和部分退款，
					//商户原支付单号,金额,商户退款单号|商户原支付单号,金额,商户退款单号  
					//分割最多支持 50 笔
					//注意：金额传0或为空默认做全额退款
					//样例(63548281250,0.01,5232112|6358281251,0,5232113)
					String refund_details=payOrder.getOrderNum()+","+refundMoney+","+orderRefund.getRefundNum();
					Map<String, String> weChatMap =HfbWeChatPay.hfbWxGzhRefund(ppwr.getRateKey1(),refund_details, notify_url, ppwr.getRateKey3());
					if (weChatMap.get("code").equals("10000")) {
						// 成功
						publicRefundYes(resultMap, orderRefund);
					} else {
						// 失败
						publicRefundNo(resultMap, orderRefund);
					}
				}else{
					logger.error("汇付宝微信公众号支付退款失败：数据库参数配置不完整-->RateKey1,RateKey3");
				}
			}
			//平安银行退款接口
			if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_ALI_SCAN)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_SCAN)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_GZH_WEIXIN_SCAN)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_GZH)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN)
					||scd.getDictName().equals(PayRateDictValue.PAY_TYPE_PA_BANK_QQ_SCAN)) {
				System.out.println("平安银行退款接口调起");
				String OPEN_ID = ppwr.getRateKey1();
				String OPEN_KEY = ppwr.getRateKey2();
				String PRIVATE_KEY = ppwr.getRateKey5();
				if (OPEN_ID != null && OPEN_KEY != null && PRIVATE_KEY != null) {
					String outNo = payOrder.getOrderNum();
					String refundOutNo = orderRefund.getRefundNum();
					String refundOrdName = orderRefund.getRefundNum();
					// 金额：以分为单位
					String refundAmount = DecimalUtil.yuanToCents(String.valueOf(refundMoney));
					Map<String, String> paMap = PABankPay.payRefund(outNo, refundOutNo, refundOrdName, Integer.valueOf(refundAmount), null, null, null, null,
							null, TLinxSHA1.SHA1("123456"), OPEN_ID, OPEN_KEY, PRIVATE_KEY);
					if (paMap.get("code").equals("10000")) {
						// 成功
						publicRefundYes(resultMap, orderRefund);
					} else {
						// 失败
						publicRefundNo(resultMap, orderRefund);
					}
				} else {
					logger.error("平安银行退款接口退款失败：数据库参数配置不完整-->RateKey1,RateKey3,RateKey5");
				}
			}
		}
		return resultMap;
	}

	// 成功
	public Map<String, Object> publicRefundYes(Map<String, Object> resultMap, Payv2PayOrderRefund orderRefund) {
		orderRefund.setRefundTime(new Date());
		orderRefund.setRefundStatus(1);// 退款成功
		//payv2PayOrderRefundMapper.updateByEntity(orderRefund);
		payv2PayOrderRefundMapper.insertByEntity(orderRefund);
		resultMap.put("refund_num", orderRefund.getRefundNum()); // 退款单号
		resultMap.put("refund_money", orderRefund.getRefundMoney()); // 退款金额
		resultMap.put("refund_time", DateStr(orderRefund.getRefundTime())); // 退款时间
		logger.info("=======>退款成功");																	// yyyyMMddHHmmss
		return resultMap;

	}

	// 失败
	public Map<String, Object> publicRefundNo(Map<String, Object> resultMap, Payv2PayOrderRefund orderRefund) {
		//orderRefund.setRefundStatus(2);
		//payv2PayOrderRefundMapper.updateByEntity(orderRefund);
		resultMap.put("error_code", "REFUND_FAILED"); // 退款失败
		resultMap.put("error_msg", "退款失败");
		logger.info("=======>退款失败");		
		return resultMap;
	}

	/**
	 * 创建退款订单
	 * 
	 * @param orderRefund
	 * @param payOrder
	 * @param refundMoney
	 * @return
	 */
	private Payv2PayOrderRefund createRefundOrder(Payv2PayOrderRefund orderRefund, Payv2PayOrder payOrder, Double refundMoney, Map<String, Object> map,
			Date refundTime) {
		String refundNum = "RR" + DateStr1(new Date()) + UtilDate.getThree();
		orderRefund.setCompanyId(payOrder.getCompanyId()); // 商户id
		orderRefund.setPayWayId(payOrder.getPayWayId()); // 支付通道id
		orderRefund.setRefundNum(refundNum); // 退款订单号
		orderRefund.setOrderType(payOrder.getOrderType());// 线上、线下类型
		orderRefund.setChannelId(payOrder.getChannelId()); // 渠道商id
		orderRefund.setOrderId(payOrder.getId()); // 支付订单id
		orderRefund.setOrderNum(payOrder.getOrderNum()); // 支付订单号
		orderRefund.setMerchantOrderNum(payOrder.getMerchantOrderNum()); // 商家订单号
		orderRefund.setRefundMoney(refundMoney); // 退款金额
		orderRefund.setRefundReason(map.get("refundReason") == null ? "" : map.get("refundReason").toString()); // 退款原因
		if (refundTime != null) {
			orderRefund.setRefundTime(refundTime); // 退款时间
		}
		//orderRefund.setRefundStatus(refundStatus); // 退款状态
		orderRefund.setCreateTime(new Date());
		//payv2PayOrderRefundMapper.insertByEntity(orderRefund);
		return orderRefund;
	}

	private String DateStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 查询退款订单
	 */
	public Map<String, Object> queryRefund(Map<String, Object> map, Payv2BussCompanyApp pbca) {
		Map<String, Object> resultMap = new HashMap<>();
		Payv2PayOrder payOrder = new Payv2PayOrder();
		Payv2PayOrderRefund orderRefund = new Payv2PayOrderRefund();
		orderRefund.setAppId(pbca.getId());

		// 查询退款列表
		orderRefund.setRefundNum(map.get("refundNum").toString());
		orderRefund = payv2PayOrderRefundMapper.selectSingle(orderRefund); // 退款列表
		if (orderRefund != null) {
			payOrder.setId(orderRefund.getOrderId());
			/*
			 * if(map.containsKey("orderNum")){
			 * payOrder.setOrderNum(map.get("orderNum").toString()); }
			 * if(map.containsKey("bussOrderNum")){
			 * payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString());
			 * }
			 */
			payOrder = payv2PayOrderMapper.selectSingle(payOrder); // 支付订单
			if (payOrder != null) {
				resultMap.put("refund_num", orderRefund.getRefundNum()); // 退款单号
				resultMap.put("order_num", orderRefund.getOrderNum()); // 支付集订单号
				resultMap.put("buss_order_num", orderRefund.getMerchantOrderNum()); // 商户订单号
				resultMap.put("refund_money", orderRefund.getRefundMoney().toString()); // 退款金额
				resultMap.put("refund_reason", orderRefund.getRefundReason()); // 退款原因
				resultMap.put("refund_time", orderRefund.getRefundTime()); // 退款时间
				resultMap.put("refund_type", orderRefund.getRefundType() == 1 ? "Y" : "N"); // 退款类型
																							// Y全额退款，N部分退款
				resultMap.put("pay_discount_money", payOrder.getPayDiscountMoney().toString()); // 最终支付金额
				resultMap.put("pay_money", payOrder.getPayMoney().toString()); // 支付金额
				resultMap.put("order_name", payOrder.getOrderName()); // 订单名称
			} else {
				resultMap.put("error_code", "ORDER_ERROR");
				resultMap.put("error_msg", "订单错误,请联系技术支持");
				logger.error("订单错误,请联系支付集技术");
			}
		} else { // 退款单号/商户订单号/支付订单号 不匹配
			resultMap.put("error_code", "REFUND_NOT_EXIST");
			resultMap.put("error_msg", "退款单号不存在");
			logger.error("退款单号不存在");
		}

		return resultMap;
	}

	/**
	 * 支付退款操作(点游)
	 * 
	 * @throws Exception
	 */
	public Map<String, Object> dyPayRefund(Map<String, Object> map, Payv2BussCompanyApp pbca) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Payv2PayOrderRefund orderRefund = new Payv2PayOrderRefund();
		orderRefund.setAppId(pbca.getId());

		double refundMoney = Double.valueOf(map.get("refundMoney").toString());
		Payv2PayOrder payOrder = new Payv2PayOrder();
		// 商户传了支付集订单号
		if (map.containsKey("orderNum")) {
			payOrder.setOrderNum(map.get("orderNum").toString());
			payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		} else {// 商户传了商户订单号
			payOrder.setAppId(pbca.getId());
			payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString());
			payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		}
		if (payOrder != null) {
			resultMap.put("order_num", payOrder.getOrderNum()); // 支付集订单
			resultMap.put("buss_order_num", payOrder.getMerchantOrderNum()); // 商户订单
			resultMap.put("pay_money", payOrder.getPayMoney()); // 支付金额
			resultMap.put("pay_discount_money", payOrder.getPayDiscountMoney()); // //最终支付金额
			resultMap.put("pay_time", DateStr(payOrder.getPayTime()==null?payOrder.getCreateTime():payOrder.getPayTime())); // 支付时间
																		// yyyyMMddHHmmss
			resultMap.put("order_name", payOrder.getOrderName()); // 订单名称

			// 支付成功
			if (payOrder.getPayStatus() == PayFinalUtil.PAY_ORDER_SUCCESS || payOrder.getPayStatus() == 5) {
				// 支付方式
				if (payOrder.getPayWayId() != null) {
					Payv2PayWay payWay = new Payv2PayWay();
					payWay.setId(payOrder.getPayWayId());
					payWay = payv2PayWayMapper.selectSingle(payWay);
					if (payWay != null) {
						// 第三方支付
						if (payWay.getWayType() == 2) {
							// 根据策略获取支付通道路由
							Payv2PayWayRate ppwr = payv2PayWayRateService.getPayWayRate(payOrder.getRateType(), payWay.getId(), payOrder.getCompanyId(),
									payOrder.getAppId());
							if (ppwr == null) {
								resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
								return resultMap;
							}
							SysConfigDictionary scd = new SysConfigDictionary();
							scd.setId(ppwr.getDicId().intValue());
							scd = sysConfigDictionaryMapper.selectSingle(scd);
							// 支付集支持的支付方式（字典）
							if (scd != null) {
								// 全部退款
								if (map.get("refundType").equals("Y")) {
									orderRefund.setRefundType(1);
									if (payOrder.getPayDiscountMoney().doubleValue() != refundMoney) {
										resultMap.put("error_code", "REFUND_AMOUNT_ERROR"); // 退款金额错误
										resultMap.put("error_msg", "退款金额错误");
										return resultMap;
									}
								} else {
									orderRefund.setRefundType(2);
								}

								Map<String, Object> param = new HashMap<>();
								param.put("orderNum", payOrder.getOrderNum());
								// 已退款总金额
								double refundSum = payv2PayOrderRefundMapper.refundSum(param);
								// 判断退款金额是否合法
								if (payOrder.getPayDiscountMoney() - refundSum > 0) {

									// 支付宝
									if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_APP)) {
										// 生成退款订单号 RR+appId+orderNum
										orderRefund = createRefundOrder(orderRefund, payOrder, refundMoney, map, null);
										// 调用支付宝退款
										AlipayTradeRefundResponse response = AliPay.alipayRefund(payOrder.getOrderNum(), refundMoney, map,
												orderRefund.getRefundNum(), ppwr.getRateKey1(), ppwr.getRateKey2(), ppwr.getRateKey3());
										if (response.isSuccess()) {
											System.out.println("调用成功");
											orderRefund.setRefundTime(response.getGmtRefundPay());
											orderRefund.setRefundStatus(1);// 退款成功
											payv2PayOrderRefundMapper.insertByEntity(orderRefund);

											resultMap.put("refund_num", orderRefund.getRefundNum()); // 退款单号
											resultMap.put("refund_money", orderRefund.getRefundMoney()); // 退款金额
											resultMap.put("refund_time", DateStr(orderRefund.getRefundTime())); // 退款时间
																												// yyyyMMddHHmmss

											// 回调商户

										} else {
											resultMap.put("error_code", "REFUND_FAILED"); // 退款失败
											resultMap.put("error_msg", "退款失败");
											System.out.println("调用失败");
										}
									} else if (scd.getDictName().equals(PayRateDictValue.PAY_TYPE_WEIXIN)) {
										// 生成退款订单号 RR+appId+orderNum
										orderRefund = createRefundOrder(orderRefund, payOrder, refundMoney, map, null);
										Map<String, String> weChatMap = WeChatPay.refund(payOrder.getOrderNum(), orderRefund.getRefundNum(),
												"" + payOrder.getPayDiscountMoney(), "" + refundMoney);
										if (weChatMap.get("success_key") != null && weChatMap.get("success_key").equals("200")) {
											orderRefund.setRefundTime(new Date());
											orderRefund.setRefundStatus(1);// 退款成功
											payv2PayOrderRefundMapper.insertByEntity(orderRefund);
											resultMap.put("refund_num", orderRefund.getRefundNum()); // 退款单号
											resultMap.put("refund_money", orderRefund.getRefundMoney()); // 退款金额
											resultMap.put("refund_time", DateStr(orderRefund.getRefundTime())); // 退款时间
																												// yyyyMMddHHmmss

										} else {
											resultMap.put("error_code", "REFUND_FAILED"); // 退款失败
											resultMap.put("error_msg", "退款失败");
										}
									}

								} else {
									resultMap.put("error_code", "REFUND_AMOUNT_ERROR"); // 退款金额错误
									resultMap.put("error_msg", "退款金额错误");
									logger.error("退款总金额大于订单金额!");
								}
							} else {
								resultMap.put("error_code", "PARAMETER_ERROR"); // 参数错误
								resultMap.put("error_msg", "参数错误");
								logger.error("参数不能为空,或者有误");
							}
						}
					} else {
						resultMap.put("error_code", "PARAMETER_ERROR"); // 参数错误
						resultMap.put("error_msg", "参数错误");
						logger.error("参数不能为空,或者有误");
					}
				} else {
					resultMap.put("error_code", "ORDER_ERROR"); // 订单错误
					resultMap.put("error_msg", "订单错误,请联系技术支持");
					// resultMap.put("payOrder", payOrder);
					logger.error("订单错误");
				}
			} else {
				resultMap.put("error_code", "ORDER_CANNOT_BE_REFUNDED"); // 订单不可退款
				resultMap.put("error_msg", "订单不可退款");
				// resultMap.put("payOrder", payOrder);
				logger.error("订单未支付或支付失败");
			}
		} else {
			resultMap.put("error_code", "TRANSACTION_DOES_NOT_EXIST"); // 交易不存在
			resultMap.put("error_msg", "交易不存在");
			// resultMap =
			// ReMessage.resultBack(ParameterEunm.ERROR_PARAMS_CODE,null,
			// ReturnMsgTips.ERROR_PARAM_DATA);
			logger.error("参数不能为空,或者有误");
		}
		// 参数签名
		String sign = PaySignUtil.getSign(resultMap, pbca.getAppSecret());
		resultMap.put("sign", sign);

		return resultMap;
	}

	/**
	 * 查询退款订单(点游)
	 */
	public Map<String, Object> dyQueryRefund(Map<String, Object> map, Payv2BussCompanyApp pbca) {
		Map<String, Object> resultMap = new HashMap<>();
		Payv2PayOrder payOrder = new Payv2PayOrder();
		Payv2PayOrderRefund orderRefund = new Payv2PayOrderRefund();
		orderRefund.setAppId(pbca.getId());

		// 查询退款列表
		orderRefund.setRefundNum(map.get("refundNum").toString());
		orderRefund = payv2PayOrderRefundMapper.selectSingle(orderRefund); // 退款列表
		if (orderRefund != null) {
			payOrder.setId(orderRefund.getOrderId());
			/*
			 * if(map.containsKey("orderNum")){
			 * payOrder.setOrderNum(map.get("orderNum").toString()); }
			 * if(map.containsKey("bussOrderNum")){
			 * payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString());
			 * }
			 */
			payOrder = payv2PayOrderMapper.selectSingle(payOrder); // 支付订单
			if (payOrder != null) {
				resultMap.put("refund_num", orderRefund.getRefundNum()); // 退款单号
				resultMap.put("order_num", orderRefund.getOrderNum()); // 支付集订单号
				resultMap.put("buss_order_num", orderRefund.getMerchantOrderNum()); // 商户订单号
				resultMap.put("refund_money", orderRefund.getRefundMoney().toString()); // 退款金额
				resultMap.put("refund_reason", orderRefund.getRefundReason()); // 退款原因
				resultMap.put("refund_time", orderRefund.getRefundTime()); // 退款时间
				resultMap.put("refund_type", orderRefund.getRefundType() == 1 ? "Y" : "N"); // 退款类型
																							// Y全额退款，N部分退款
				resultMap.put("pay_discount_money", payOrder.getPayDiscountMoney().toString()); // 最终支付金额
				resultMap.put("pay_money", payOrder.getPayMoney().toString()); // 支付金额
				resultMap.put("order_name", payOrder.getOrderName()); // 订单名称
			} else {
				resultMap.put("error_code", "ORDER_ERROR");
				resultMap.put("error_msg", "订单错误,请联系技术支持");
				logger.error("订单错误,请联系支付集技术");
			}
		} else { // 退款单号/商户订单号/支付订单号 不匹配
			resultMap.put("error_code", "REFUND_NOT_EXIST");
			resultMap.put("error_msg", "退款单号不存在");
			logger.error("退款单号不存在");
		}

		return resultMap;
	}

	// 精确到毫秒
	private String DateStr1(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String str = sdf.format(date);
		return str;
	}
	/**
	 * 获取本地时间段的所有退款单
	 */
	public List<Payv2PayOrderRefund> selectByRefundTime(Map<String, Object> map) throws Exception {
		List<Payv2PayOrderRefund> payv2PayOrderRefundList = payv2PayOrderRefundMapper.selectByRefundTime(map);
		return payv2PayOrderRefundList;
	}
	
	public PageObject<Payv2PayOrderRefundVO> queryRefunds(Map<String, Object> map) {
		int totalData = payv2PayOrderRefundMapper.getRefundsCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2PayOrderRefundVO> list = payv2PayOrderRefundMapper.queryRefunds(pageHelper.getMap());
		PageObject<Payv2PayOrderRefundVO> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	
	public Payv2PayOrderRefundVO refundDetail(Map<String, Object> map) {
		
		return payv2PayOrderRefundMapper.refundDetail(map);
	}
}
