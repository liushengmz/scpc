package com.pay.manger.controller.payv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.service.AliPayService;
import com.pay.business.payv2.service.Payv2BankAppKeyService;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.IpAddressUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PayFinalUtil;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.ServiceUtil;
import com.pay.business.util.StringUtil;
import com.pay.business.util.minshengbank.MinShengBankSignUtil;
import com.pay.business.util.pinganbank.util.TLinx2Util;
import com.pay.business.util.wftpay.weChatSubscrPay.utils.XmlUtils;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;
import com.pay.manger.util.StringHandleUtil;

/**
 * @Title: AliPayController.java
 * @Package com.pay.manger.controller.payv2
 * @Description: 扫码支付全集
 * @author ZHOULIBO
 * @date 2017年6月30日 下午2:49:52
 * @version V1.0
 */
@Controller
@RequestMapping("/aiJinFuPay/*")
public class AliPayController {
	private static final Log logger = LogFactory.getLog(AliPayController.class);
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	@Autowired
	private Payv2BankAppKeyService payv2BankAppKeyService;
	@Autowired
	private AliPayService aliPayService;

	/**
	 * alipayPage 
	 * 扫码支付-获取提供给商户调起支付宝支付的参数接口
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception    设定文件 
	 * Map<String,Object>    返回类型
	 */
	@ResponseBody
	@RequestMapping("/aliScanPay")
	public Map<String, Object> alipayPage(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> map) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		logger.info("=======================================");
		logger.info("==========》欢迎商户来调起扫码支付《===========");
		logger.info("=======================================");
		long time1 = System.currentTimeMillis();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		System.out.println("Andy Add map:" + map);
		
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		
		if (!paramStrCon) {
			logger.error("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
			return resultMap;
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if (paramMap.keySet().size() == 0) {
			logger.error("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
			return resultMap;
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if (!appKeyCon) {
			logger.error("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID, null);
			return resultMap;
		}
		// 检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if (pbca == null) {
			logger.error("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID, null);
			return resultMap;
		}
		// 验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if (!con) {
			logger.error("商户签名错误");
			resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR, null);
			return resultMap;
		}
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum", "orderName", "payMoney", "notifyUrl" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
					// 调起支付宝扫码支付
					//渠道：主要看商户想拉起那个通道的扫码支付：2:微信支付  1：支付宝支付：此字段可以商户不传；默认为支付宝扫码支付
					boolean channel = paramMap.containsKey("channel");
					int fromChannel=1;
					if(channel){
						fromChannel=Integer.valueOf(paramMap.get("channel").toString());
						paramMap.remove("channel");
					}
					String address = IpAddressUtil.getIpAddress(request);
					paramMap.put("address", address);
					Map<String, String> backMap = aliPayService.aliPaycreatePayAndOreder(pbca, fromChannel, paramMap);
					boolean flag = backMap.containsKey("status");
					if (flag == true && backMap.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)) {
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
					} else if (flag == true && null != backMap.get("status") & backMap.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)) {
						// 已超过限额,请检查支付宝通道单笔额度和每日额度
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_ORDER_ERROR, null);
					} else {
						boolean oYes=backMap.containsKey("code");
						if(oYes){
							int code = Integer.valueOf(backMap.get("code").toString());
							if (code == 10000) {
								backMap.remove("msg");
								backMap.remove("code");
								backMap.put("order_num", backMap.get("out_trade_no"));
								backMap.remove("out_trade_no");
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, backMap);
								logger.info("=====>扫码支付返回结果：" + backMap);
							} else {
								resultMap = ReMessage.resultBack(ParameterEunm.PAY_ALI_CODE, null);
								logger.error("=====>调起扫码支付预下单接口失败：扫码支付返回原因：msg=" + backMap.get("msg") + "扫码支付返回code:" + backMap.get("code"));
							}
						}else{
							resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null);
							logger.info("=====>扫码支付返回结果：失败，有可能预下单错误或者获取拉起支付参数失败");
						}
					}
				}else {
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
				}
			} else {
				logger.error("=====>参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("=====>扫码支付失败原因："+e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null, null);
		}
		logger.info("=====>请求耗时:" + (System.currentTimeMillis() - time1));
		return resultMap;
	}

	/**
	 * aliPayCallBack 
	 * 支付宝支付扫码回调
	 * @param map
	 * @param request
	 * @param response    设定文件 
	 * void    返回类型
	 */
	@RequestMapping(value = "/aliPayCallBack")
	public void aliPayCallBack(@RequestParam Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		logger.info("=====>支付宝扫码支付回调开始传参为：" + map);
		String out_trade_no = map.get("out_trade_no");// 商户订单号
		// 查询订单详情
		Payv2PayOrder payv2PayOrder = payv2PayOrderService.getOrderInfo(out_trade_no);
		PrintWriter out = null;
		boolean veryfy = false;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long time1 = System.currentTimeMillis();
		if (payv2PayOrder == null) {
			logger.error("=====》订单为空！");
			out.write("error");
		} else {
			// 支付宝公钥(用于验签)
			String ALIPAY_RSA_PUBLIC = payv2PayOrder.getRateKey3();
			veryfy = aliPayService.verify(map, ALIPAY_RSA_PUBLIC);
		}
		if (veryfy) {
			// 为真验证没问题,接下来修改数据库状态
			boolean con = false;
			try {
				// 支付宝回调有三种类型 , 正在支付、支付成功、交易取消 , 此处只处理支付成功的逻辑 ,
				// 其他两种类型,直接out.write("success");给支付宝
				if (map.get("trade_status").toString().equals("TRADE_SUCCESS")) {
					// 这里的逻辑应该处理:解析支付宝字段、更新数据库订单状态、回调商户(如果回调商户失败,则不要返回支付宝success,等待支付宝继续回调)
					con = payv2PayOrderService.aliPayCallBack(map, payv2PayOrder);
				} else {
					con = true;
				}
			} catch (Exception e) {
				logger.info("=====>服务器错误：" + e);
			}
			if (con) {
				logger.info("=====>支付宝回调,验签,更改订单状态:" + con);
				out.write("success");
			} else {
				logger.info("=====>支付宝回调,更改订单状态:" + con);
				out.write("error");
			}
		} else {
			logger.error("=====>支付宝回调,验签失败");
			out.write("error");
		}
		if (out != null) {
			out.close();
		}
		logger.info("=====>回调请求耗时:" + (System.currentTimeMillis() - time1));
	}

	/**
	 * xyBankAliScanPayCallBack 
	 * 兴业银行-支付宝-扫码支付-支付结果异步回调
	 * @param request
	 * @param response    设定文件 
	 * void    返回类型
	 */
	@RequestMapping(value = "/xyBankAliScanPayCallBack")
	public void xyBankAliScanPayCallBack(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		long time1 = System.currentTimeMillis();
		String info = "ereer";
		try {
			String resString = XmlUtils.parseRequst(request);
			logger.debug("========》回调通知内容xml：" + resString);
			if (resString != null && !"".equals(resString)) {
				Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
				String res = XmlUtils.toXml(map);
				logger.debug("========》通知内容：" + res);
				if (map.get("result_code").equals("SUCCESS") && map.get("return_code").equals("SUCCESS")) {
					if (map.containsKey("sign")) {
						String orderNum = map.get("out_trade_no");
						// 查询订单详情
						Payv2PayOrder payv2PayOrder = payv2PayOrderService.getOrderInfo(orderNum);
						if (payv2PayOrder != null) {
							if (!XyBankPay.xyBankcheck(map, payv2PayOrder.getRateKey3())) {
								logger.error("=======》验证签名不通过");
								info = "<xml><return_code>FALL</return_code><return_msg>验证签名不通过</return_msg></xml>";
							} else {
								// 此处可以在添加相关处理业务
								Map<String, String> params = new HashMap<String, String>();
								params.put("out_trade_no", orderNum);
								params.put("trade_status", "TRADE_SUCCESS");
								params.put("trade_no", map.get("transaction_id").toString());
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								String time=map.get("time_end");
								Date date=sdf.parse(time);
								params.put("gmt_payment",DateUtil.DateToString(date, "yyyy-MM-dd HH:mm:ss"));
								String moeny = BigDecimal.valueOf(Long.valueOf(map.get("total_fee").toString())).divide(new BigDecimal(100)).toString();
								params.put("total_amount", moeny);
								boolean con = payv2PayOrderService.aliPayCallBack(params, payv2PayOrder);
								if (con) {
									info = "<xml><return_code>SUCCESS</return_code><return_msg></return_msg></xml>";
									logger.info("=======》兴业银行支付宝扫码支付结果异步回调业务接口处理完毕状态：" + con);
								} else {
									logger.error("=======》兴业银行支付宝扫码支付结果异步回调业务接口处理失败状态：" + con);
								}
							}
						} else {
							info = "<xml><return_code>FALL</return_code><return_msg>没有查询到此订单</return_msg></xml>";
							logger.error("=======》兴业银行支付宝扫码支付结果异步回调：<xml><return_code>FALL</return_code><return_msg>没有查询到此订单</return_msg></xml>");
						}
					}
				}
			}
			response.getWriter().write(info);
		} catch (Exception e) {
			logger.error("=======》兴业银行支付宝扫码支付回调操作失败，原因:", e);
		}
		logger.info("=======》兴业银行支付宝扫码支付回调请求耗时:" + (System.currentTimeMillis() - time1));
	}

	/**
	 * msBankAliScanPayCallBack 
	 * 民生银行-异步回调
	 * @param request
	 * @param response
	 * @param map
	 * @return    设定文件 
	 * Map<String,Object>    返回类型
	 */
	@ResponseBody
	@RequestMapping(value = "/msBankAliScanPayCallBack")
	public Map<String, Object> msBankAliScanPayCallBack(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> map) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		logger.info("========》民生银行-异步回调开始");
		long time1 = System.currentTimeMillis();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", "FAIL");
		logger.info("minShengBank call back:" + JSON.toJSONString(map));
		try {
			if (ObjectUtil.checkObjectFileIsEmpty(new String[] { "orderNo", "merNo", "signIn", "amount" }, map)) {
				String orderNo = map.get("orderNo").toString();
				Payv2PayOrder payOrder = payv2PayOrderService.getOrderInfo(orderNo);
				if (null != payOrder) {
					if (MinShengBankSignUtil.verifySign(map, payOrder.getRateKey2())) {
						// 此处可以在添加相关处理业务
						Map<String, String> params = new HashMap<String, String>();
						params.put("out_trade_no", orderNo);
						params.put("trade_status", "TRADE_SUCCESS");
						params.put("trade_no", map.get("outOrderNo").toString());
						params.put("gmt_payment", map.get("payTime").toString());
						String moeny = BigDecimal.valueOf(Long.valueOf(map.get("amount").toString())).divide(new BigDecimal(100)).toString();
						params.put("total_amount", moeny);
						boolean bool = payv2PayOrderService.aliPayCallBack(params, payOrder);
						if (bool) {
							resultMap.put("result", "SUCCESS");
							resultMap.put("desc", "订单成功orderNo：" + orderNo);
							logger.info("=======》民生银行-支付结果异步回调业务接口处理完毕状态：" + bool);
						} else {
							resultMap.put("desc", "订单成功修改状态报错");
							logger.error("=======》民生银行-支付结果异步回调业务接口处理完毕状态：" + bool);
						}

					} else {
						resultMap.put("desc", "验签失败");
						logger.error("=======》民生银行-支付回调验签失败");
					}
				} else {
					resultMap.put("desc", "订单信息无法查询");
					logger.error("=======》民生银行-支付订单信息无法查询");
				}

			} else {
				resultMap.put("desc", "参数不能为空orderNo,merNo,signIn");
				logger.error("=======》民生银行-支付参数不能为空orderNo,merNo,signIn");
			}
		} catch (Exception e) {
			logger.error("=======》回调发生异常" + e);
			resultMap.put("desc", "服务器异常");
		}
		logger.info("=======》民生银行支付回调请求耗时:" + (System.currentTimeMillis() - time1));
		return resultMap;
	}
	
	@ResponseBody
    @RequestMapping("tcPayCallBack")
	public String TcPayCallback(HttpServletRequest request, HttpServletResponse response)
	{
		
		try
		{
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");

			String orderNo =  request.getParameter("orderNo");
			String userName = request.getParameter("userName");
			String result = request.getParameter("result");
			String amount = request.getParameter("amount");
			String pay_time = request.getParameter("pay_time");
			String pay_channel = request.getParameter("pay_channel");
			
			String sign = request.getParameter("sign");
			
			System.out.println("通财支付回调:" + orderNo + "-" + userName + "-" + result + "-" + amount + "-" + pay_time  + "-" + pay_channel + "-" + sign);
			
			if(StringUtil.isNullOrEmpty(orderNo)
					||StringUtil.isNullOrEmpty(userName)
					||StringUtil.isNullOrEmpty(result)
					||StringUtil.isNullOrEmpty(amount)
					||StringUtil.isNullOrEmpty(pay_time)
					||StringUtil.isNullOrEmpty(sign))
			{
				System.out.println("通财支付回调参数不齐全");
				return "no";
			}
			
			Payv2PayOrder payOrder = payv2PayOrderService.getOrderInfo(orderNo);
			
			if (null != payOrder)
			{
				String sKey = payOrder.getRateKey2();
				String md5Value = StringUtil.getMd5String(orderNo + userName + result + amount + pay_time + pay_channel + sKey, 32);
				System.out.println("md5Value:" + md5Value);
				if (md5Value.equalsIgnoreCase(sign))
				{
					Map<String, String> params = new HashMap<String, String>();
					params.put("out_trade_no", orderNo);
					params.put("trade_status", "TRADE_SUCCESS");
					params.put("trade_no", StringUtil.getString(request.getParameter("payNo"),""));
					params.put("gmt_payment", pay_time);
					params.put("total_amount", amount);
					
					boolean success = payv2PayOrderService.aliPayCallBack(params, payOrder);
					
					System.out.println("通财支付接收回调:" + success);
					
					if(success)
					{
						return "ok";
					}
				}
				else
				{
					System.out.println("通财支付回调验签失败");
				}
			}
			else
			{
				System.out.println("通财支付回调订单[" + orderNo + "]不存在");
				return "no";
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return "no";
	}
	
	/**
	 * PayCallback 
	 * 平安银行各种支付方式异步回调
	 * @param request
	 * @return    设定文件 
	 * String    返回类型
	 */
	@ResponseBody
    @RequestMapping("PABankScanPayCallBack")
	public String PayCallback(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		logger.info("========》民生银行-异步回调开始");
		Map<String, String> params = new TreeMap<String, String>();
		// 取出所有参数是为了验证签名
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			params.put(parameterName, request.getParameter(parameterName));
		}
		
		JSONObject paramsObject = JSONObject.fromObject(params);
		System.out.println("===========回调参数是：" + paramsObject.toString());
		try {
			// 1:成功 2取消
			String status = paramsObject.getString("status");
			if (status.equals("1")) {
				
				System.out.println("订单支付成功");
				
				final String orderNo = paramsObject.getString("out_no");
				
				Payv2PayOrder payOrder = payv2PayOrderService.getOrderInfo(orderNo);
				if (payOrder != null) {// 验签
					String OPEN_KEY = payOrder.getRateKey2();
					if (TLinx2Util.verifySign(paramsObject, OPEN_KEY)) {
						// 此处可以在添加相关处理业务
						Map<String, String> params1 = new HashMap<String, String>();
						// 支付集订单号
						params1.put("out_trade_no", orderNo);
						// 状态
						params1.put("trade_status", "TRADE_SUCCESS");
						// 上游订单号
						params1.put("trade_no", paramsObject.getString("ord_no"));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						// 支付时间
						String time = paramsObject.getString("pay_time");
						Date date = sdf.parse(time);
						
						params1.put("gmt_payment", DateUtil.DateToString(date, "yyyy-MM-dd HH:mm:ss"));
						// 金额
						final String moeny = BigDecimal.valueOf(Long.valueOf(paramsObject.getString("amount"))).divide(new BigDecimal(100)).toString();
						
						new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								ServiceUtil.sendGet("http://wx-jump.iquxun.cn/notify.jsp?orderNo=" + orderNo + "&money=" + moeny, null, null);
							}
						}).start();
						
						params1.put("total_amount", moeny);
						boolean bool = payv2PayOrderService.aliPayCallBack(params1, payOrder);
						if (bool) {
							logger.info("=======》平安银行-支付结果异步回调业务接口处理完毕状态：" + bool);
							return "notify_success";
						} else {
							logger.error("=======》平安银行-支付结果异步回调业务接口处理完毕状态：" + bool);
							return null;
						}
						
						
					} else {
						return null;
					}
				} else {
					System.out.println("订单不存在");
					return null;
				}
			} else {
				// 取消
				System.out.println("订单支付取消");
				return "notify_success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * companyCallBack 
	 * 模拟回调商户服务器
	 * @param map
	 * @param request
	 * @param response    设定文件 
	 * void    返回类型
	 */
	@RequestMapping(value = "/companyCallBack")
	public void companyCallBack(@RequestParam Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		logger.info("模拟回调商户开始传参为：" + map);
		long time1 = System.currentTimeMillis();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("回调请求耗时:" + (System.currentTimeMillis() - time1));
	}
	
	public static void main(String[] args)
	{
		String md5Value = StringUtil.getMd5String("DD20171204141927656294280" + "wendy@dataeye.com" + "1" + "0.01" + "2017-12-04 14:19:55" + "152" + "02aaa7a41e59a2a14445017029a50e24", 32);
		System.out.println(md5Value);
	}
	
}
