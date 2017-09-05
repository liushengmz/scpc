package com.pay.manger.controller.payv2;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.http.HttpUtil;
import com.core.teamwork.base.util.properties.PropertiesUtil;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.service.GateWayService;
import com.pay.business.payv2.service.Payv2BankAppKeyService;
import com.pay.business.sysconfig.service.SysConfigDictionaryService;
import com.pay.business.util.CommonUtil;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PayFinalUtil;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.wftpay.weChatSubscrPay.utils.SignUtils;
import com.pay.business.util.wftpay.weChatSubscrPay.utils.XmlUtils;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;
import com.pay.manger.util.StringHandleUtil;

/**
 * @Title: WeChatPayController.java
 * @Package com.pay.manger.controller.payv2
 * @Description: 微信公众号通道支付控制器
 * @author ZHOULIBO
 * @date 2017年7月6日 下午3:10:03
 * @version V1.0
 */
@Controller
@RequestMapping("/GateWay/*")
public class WeChatPayController {
	private static final Log logger = LogFactory.getLog(WeChatPayController.class);
	// 授权后跳转地址
	private static String baseUrl = PropertiesUtil.getProperty("rate", "wechat_gzh_pay_huidiao_url");
	// 获取openId地址
	private static String openIdUrl = "https://open.weixin.qq.com/connect/oauth2/authorize";
	@Autowired
	private GateWayService gateWayService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	@Autowired
	private Payv2BankAppKeyService payv2BankAppKeyService;
	@Autowired
	private SysConfigDictionaryService sysConfigDictionaryService;

	/**
	 * hfbWxGzhPay 微信公众号号支付：微信内部拉起支付入口
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 *             设定文件 Map<String,Object> 返回类型
	 */
	@RequestMapping("/wxGzhPay")
	public ModelAndView hfbWxGzhPay(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> map) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		logger.info("=======================================");
		logger.info("=========》欢迎商户来调起微信公众号支付《========");
		logger.info("=======================================");
		ModelAndView mv = new ModelAndView();
		long time1 = System.currentTimeMillis();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 返回错误信息
		String redirect = PropertiesUtil.getProperty("rate", "redirect_url") + "?resultCode=";
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if (!paramStrCon) {
			logger.error("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
			redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
			mv.setViewName(redirect);
			return mv;
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if (paramMap.keySet().size() == 0) {
			logger.error("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
			redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
			mv.setViewName(redirect);
			return mv;
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if (!appKeyCon) {
			logger.error("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID, null);
			redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
			mv.setViewName(redirect);
			return mv;
		}
		// 检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if (pbca == null) {
			logger.error("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID, null);
			redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
			mv.setViewName(redirect);
			return mv;
		}
		// 验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if (!con) {
			logger.error("商户签名错误");
			resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR, null);
			redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
			mv.setViewName(redirect);
			return mv;
		}
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum", "orderName", "payMoney", "notifyUrl", "ip" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
					// 微信公众号支付：本地系统预下单
					Map<String, String> banckMap = gateWayService.wechatGzhPay(paramMap, pbca, 2);
					boolean flag = banckMap.containsKey("status");
					// 订单已经支付
					if (flag == true && banckMap.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)) {
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
						logger.error(" 订单已经支付");
						redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
						mv.setViewName(redirect);
						return mv;
					}
					// 已超过限额,请检查微信通道单笔额度和每日额度
					else if (flag == true && null != banckMap.get("status") & banckMap.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)) {
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_ORDER_ERROR, null);
						logger.error("已超过限额,请检查微信通道单笔额度和每日额度");
						redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
						mv.setViewName(redirect);
						return mv;
					}
					// 未配置支付通道或支付类型不支持
					else if (flag == true && banckMap.get("status").equals(PayFinalUtil.PAY_TYPE_FAIL)) {
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_TYPE_ERROR, null);
						logger.error("未配置支付通道或支付类型不支持");
						redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
						mv.setViewName(redirect);
						return mv;
					}
					// 预下单成功
					else if (flag == true && banckMap.get("status").equals(PayFinalUtil.PAY_STATUS_SUSSESS)) {
						// 去调起支付：
						// 微信appid
						String appid = banckMap.get("gzhAppId");
						String orderNum = banckMap.get("orderNum");
						// 授权后要跳转的链接
						String backUri = baseUrl;
						backUri = backUri + "?orderNum=" + orderNum;
						// URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
						backUri = URLEncoder.encode(backUri);
						// scope 参数视各自需求而定，这里用scope=snsapi_base
						// 不弹出授权页面直接授权目的只获取统一支付接口的openid snsapi_userinfo是弹出窗口
						String url = openIdUrl + "?appid=" + appid + "&redirect_uri=" + backUri + "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
						System.out.println("url:" + url);
						url = "redirect:" + url;
						mv.setViewName(url);
						return mv;
					}
				}else {
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
					redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
					mv.setViewName(redirect);
					return mv;
				}
			} else {
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
				redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
				mv.setViewName(redirect);
				return mv;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null, null);
			redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
			mv.setViewName(redirect);
			return mv;
		}
		logger.info("=====>请求耗时:" + (System.currentTimeMillis() - time1));
		redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
		mv.setViewName(redirect);
		return mv;
	}

	/**
	 * weChatGZHPayCallBack 汇付宝微信公众号支付异步回调接口
	 * 
	 * @param map
	 * @param request
	 * @param response
	 *            设定文件 void 返回类型
	 */
	@RequestMapping("weChatGZHPayCallBack")
	public void weChatGZHPayCallBack(@RequestParam Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		logger.info("=========》汇付宝微信公众号支付异步回调接口传参为：" + map);
		String out_trade_no = map.get("agent_bill_id");// 商户订单号
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
			logger.error("=========》订单为空！");
			out.write("error");
		} else {
			try {
				// 汇付宝验签
				veryfy = gateWayService.hfbWxGzhSign(map, payv2PayOrder);
				logger.info("=========》回调验签结果为:" + veryfy);
			} catch (Exception e1) {
				logger.info("=========》服务器错误:" + e1);
				e1.printStackTrace();
			}
		}
		if (veryfy) {
			// 为真验证没问题,接下来修改数据库状态
			boolean con = false;
			try {
				// 成功：汇付宝返回给1
				if (map.get("result").toString().equals("1")) {
					// 这里的逻辑应该处理:解析支付宝字段、更新数据库订单状态、回调商户(如果回调商户失败：返回错误)
					Map<String, String> params = new HashMap<String, String>();
					params.put("out_trade_no", map.get("agent_bill_id"));
					params.put("trade_status", "TRADE_SUCCESS");
					params.put("trade_no", map.get("jnet_bill_no").toString());
					params.put("gmt_payment", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
					String moeny = map.get("pay_amt").toString();
					params.put("total_amount", moeny);
					con = payv2PayOrderService.aliPayCallBack(params, payv2PayOrder);
				}
			} catch (Exception e) {
				logger.info("=========》服务器错误:" + e);
			}
			if (con) {
				logger.info("=========》汇付宝回调,验签,更改订单状态:" + con);
				out.write("ok");
			} else {
				logger.info("=========》汇付宝回调,更改订单状态:" + con);
				out.write("error");
			}
		} else {
			logger.error("=========》汇付宝回调,验签失败");
			out.write("error");
		}
		if (out != null) {
			out.close();
		}
		logger.info("=========》回调请求耗时:" + (System.currentTimeMillis() - time1));
	}

	/**
	 * xyBankPayCallback 兴业银行：微信公众号支付，H5支付，结果异步回调
	 * 
	 * @param req
	 * @param resp
	 *            设定文件 void 返回类型
	 */
	@ResponseBody
	@RequestMapping("xyBankPayCallback")
	public void xyBankPayCallback(HttpServletRequest req, HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		long time1 = System.currentTimeMillis();
		String info = "ereer";
		try {
			logger.info("=========》兴业银行微信公众号支付结果异步回调开始");
			String resString = XmlUtils.parseRequst(req);
			logger.debug("========》回调通知内容：" + resString);
			if (resString != null && !"".equals(resString)) {
				Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
				String res = XmlUtils.toXml(map);
				logger.debug("通知内容：" + res);
				if (map.get("result_code").equals("SUCCESS") && map.get("return_code").equals("SUCCESS")) {
					if (map.containsKey("sign")) {
						String orderNum = map.get("out_trade_no");
						// 查询订单详情
						Payv2PayOrder payv2PayOrder = payv2PayOrderService.getOrderInfo(orderNum);
						if (payv2PayOrder != null) {
							if (!XyBankPay.checkParam(map, payv2PayOrder.getRateKey3())) {
								res = "验证签名不通过";
								info = "<xml><return_code>FALL</return_code><return_msg>验证签名不通过</return_msg></xml>";
							} else {
								// 此处可以在添加相关处理业务
								Map<String, String> params = new HashMap<String, String>();
								params.put("out_trade_no", map.get("out_trade_no"));
								params.put("trade_status", "TRADE_SUCCESS");
								params.put("trade_no", map.get("transaction_id").toString());
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								String time = map.get("time_end");
								Date date = sdf.parse(time);
								params.put("gmt_payment", DateUtil.DateToString(date, "yyyy-MM-dd HH:mm:ss"));
								String moeny = BigDecimal.valueOf(Long.valueOf(map.get("total_fee").toString())).divide(new BigDecimal(100)).toString();
								params.put("total_amount", moeny);
								boolean con = payv2PayOrderService.aliPayCallBack(params, payv2PayOrder);
								if (con) {
									info = "<xml><return_code>SUCCESS</return_code><return_msg></return_msg></xml>";
									// 返回格式
									logger.info(info);
									logger.info("=======》兴业银行微信公众号支付结果异步回调业务接口处理完毕状态：" + con);
								} else {
									logger.info("=======》兴业银行微信公众号支付结果异步回调业务接口处理失败状态：" + con);
								}
							}
						} else {
							info = "<xml><return_code>FALL</return_code><return_msg>没有查询到此订单</return_msg></xml>";
						}
					}
				}
			}
			resp.getWriter().write(info);
		} catch (Exception e) {
			logger.error("=======》兴业银行微信公众号支付回调操作失败，原因:", e);
		}
		logger.info("=======》兴业银行微信公众号支付回调请求耗时:" + (System.currentTimeMillis() - time1));
	}

	/**
	 * payResultCallback
	 * 威富通微信公众号支付结果回调:此接口是威富通公众号支付是否成功回调，商户确定成功需要返回success给威富通，
	 * 同时自己订单业务处理，如果出现漏单，威富通会人工核对补单
	 * 
	 * @param req
	 * @param resp
	 *            设定文件 void 返回类型
	 */
	@ResponseBody
	@RequestMapping("payResultCallback")
	public void payResultCallback(HttpServletRequest req, HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		try {
			logger.info("=========》威富通微信公众号支付结果回调开始");
			String resString = XmlUtils.parseRequst(req);
			logger.debug("========》回调通知内容：" + resString);
			String respString = "error";
			if (resString != null && !"".equals(resString)) {
				Map<String, String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
				String res = XmlUtils.toXml(map);
				logger.debug("=======》通知内容：" + res);
				if (map.containsKey("sign")) {
					String orderNum = map.get("out_trade_no");
					// 查询订单详情
					Payv2PayOrder payv2PayOrder = payv2PayOrderService.getOrderInfo(orderNum);
					if (payv2PayOrder != null) {
						if (!SignUtils.checkParam(map, payv2PayOrder.getRateKey2())) {
							res = "=======》验证签名不通过";
							respString = "error";
						} else {
							String status = map.get("status");
							if (status != null && "0".equals(status)) {
								String result_code = map.get("result_code");
								if (result_code != null && "0".equals(result_code)) {
									// 此处可以在添加相关处理业务，校验通知参数中的商户订单号out_trade_no和金额total_fee是否和商户业务系统的单号和金额是否一致，一致后方可更新数据库表中的记录。
									Map<String, String> params = new HashMap<String, String>();
									params.put("out_trade_no", map.get("out_trade_no"));
									params.put("trade_status", "TRADE_SUCCESS");
									params.put("trade_no", map.get("transaction_id").toString());
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
									String time = map.get("time_end");
									Date date = sdf.parse(time);
									params.put("gmt_payment", DateUtil.DateToString(date, "yyyy-MM-dd HH:mm:ss"));
									String moeny = BigDecimal.valueOf(Long.valueOf(map.get("total_fee").toString())).divide(new BigDecimal(100)).toString();
									params.put("total_amount", moeny);
									boolean con = payv2PayOrderService.aliPayCallBack(params, payv2PayOrder);
									// boolean
									// con=payv2PayOrderService.wftWechatPayCallBack(map);
									if (con) {
										respString = "success";
										logger.info("=======》威富通微信公众号支付回调添加相关处理业务接口处理成功");
									} else {
										logger.info("=======》威富通微信公众号支付回调添加相关处理业务接口处理失败");
									}
								}
							}
						}
					}
				}
			}
			resp.getWriter().write(respString);
		} catch (Exception e) {
			logger.error("=======》操作失败，原因：", e);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/publicPay")
	public Map<String, Object> testPublicPay(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> pMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, pMap);
		if (!paramStrCon) {
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
			return resultMap;
		}
		String paramStr = URLEncoder.encode(pMap.get("paramStr").toString());
		String lmUrl = PropertiesUtil.getProperty("rate", "wachat_wft_pay_callback_url") + "?" + paramStr;
		// http://wx.api-export.com/api/waptoweixin?key=授权key&f=json&url=http%3a%2f%2fwww.baidu.com
		String result = HttpUtil.HttpGet("http://wx.api-export.com/api/waptoweixin?key=bc8231705e3965376fc063d4959a9dde&f=json&url=" + lmUrl, null);

		if (result.equals("")) {
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
			return resultMap;
		}
		JSONObject jsonObject = JSONObject.parseObject(result);
		String lmResult = jsonObject.getString("ticket_url");

		System.out.println(lmResult);
		resultMap.put("result", lmResult);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);

		return resultMap;
	}

	@RequestMapping("/getCode")
	public String userAuth(HttpServletRequest request, HttpServletResponse response) {
		logger.info("---------------------->调起用户openId开始");
		try {
			Map<String, Object> map = new HashMap<>();
			long num = new Date().getTime();
			System.out.println("======订单号为：" + num + "==========");
			String bussOrderNum = String.valueOf(num);
			String payMoney = "0.01";
			String appKey = "270461df13a412f373ae6c2771ccd926";
			String notifyUrl = "http://aijinfupay.tunnel.echomod.cn/aiJinFuPay/companyCallBack.do";
			String orderName = "公众号支付测试";
			String returnUrl = "http://aijinfupay.tunnel.echomod.cn/success.html";
			String goodsNote = "111111";
			String remark = "111111";
			String goods_num = "100";
			map.put("bussOrderNum", bussOrderNum);
			map.put("payMoney", payMoney);
			map.put("appKey", appKey);
			map.put("notifyUrl", notifyUrl);
			map.put("orderName", orderName);
			map.put("returnUrl", returnUrl);
			map.put("goodsNote", goodsNote);
			map.put("remark", remark);
			map.put("goods_num", goods_num);
			String sgin = PaySignUtil.getSign(map, "be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b");// 外网密钥
			// 授权后要跳转的链接
			String backUri = baseUrl;
			backUri = backUri + "?bussOrderNum=" + bussOrderNum + "&payMoney=" + payMoney + "&appKey=" + appKey + "&notifyUrl=" + notifyUrl + "&orderName="
					+ orderName + "&returnUrl=" + returnUrl + "&goodsNote=" + goodsNote + "&remark=" + remark + "&goods_num=" + goods_num + "&sgin=" + sgin;
			// URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
			backUri = URLEncoder.encode(backUri);
			// scope 参数视各自需求而定，这里用scope=snsapi_base
			// 不弹出授权页面直接授权目的只获取统一支付接口的openid snsapi_userinfo是弹出窗口
//			String url = openIdUrl + "?appid=" + WxPayConfig.appid + "&redirect_uri=" + backUri
//					+ "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
//			System.out.println("url:" + url);
//			response.sendRedirect(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getInfo 发起支付：此方法获取相关参数拉起支付
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @return 设定文件 ModelAndView 返回类型
	 */
	@ResponseBody
	@RequestMapping("getInfo")
	public ModelAndView getInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> map) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		ModelAndView mv = new ModelAndView("pay/pubPay");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 返回错误信息
		String redirect = PropertiesUtil.getProperty("rate", "redirect_url") + "?resultCode=";
		logger.info("===========》>>>>>><<<<<<<<<《=========");
		logger.info("===========》拉起微信公众号支付开始《=========");
		logger.info("===========》>>>>>><<<<<<<<<《=========");
		logger.info("===========》拉起微信公众号支付传参为:" + map);
		// 获取订单号
		String orderNum = String.valueOf(map.get("orderNum"));
		try {
			// -------------------订单重复发起处理---------------------//
			// map-key
			String mapkey = "MAP" + orderNum;
			// 存入次数key
			String keyCount = "COUNT" + orderNum;
			// 获取次数
			String countValue = RedisDBUtil.redisDao.getString(keyCount);
			// 如果不为空：证明已经请求过;直接获取缓存数据返回前端；为空走正常流程；
			if (countValue != null) {
				Map<String, String> mapValue = RedisDBUtil.redisDao.getStringMapAll(mapkey);
				// 判断MAP是否为空:如果为空等待1S 再次获取Map值
				if (mapValue.containsKey("jsapi_appid")) {
					mapValue.remove("code");
					mapValue.put("orderNum", orderNum);
					logger.info("=========》微信公众号支付返回结果:" + mapValue);
					mv.addObject("map", mapValue);
					return mv;
				} else {
					Thread.sleep(1000);
					System.out.println("====程序等待1S结束====");
					//再次获取缓存数据
					mapValue = RedisDBUtil.redisDao.getStringMapAll(mapkey);
					if(mapValue.containsKey("jsapi_appid")){
						mapValue.put("orderNum", orderNum);
						logger.info("=========》微信公众号支付返回结果:" + mapValue);
						mv.addObject("map", mapValue);
						System.out.println("====程序等待1秒后，获取到了数据，成功返回====");
						return mv;
					}else{
						logger.error("====失败====");
						System.out.println("====缓存数据中2次没有获取到数据；从而导致失败直接返回====");
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
						redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
						mv.setViewName(redirect);
						return mv;
					}
				}
			} else {
				// 网页授权后获取传递的参数code
				String code = request.getParameter("code");
				logger.info("===========》获取code:" + code);
				// 获取缓存相关的数据

				// 获取缓存预下单信息
				Map<String, String> redisMap = RedisDBUtil.redisDao.getStringMapAll(orderNum);

				logger.info("===========》获取缓存预下单信息Map参数:" + redisMap);
				// 支付说明
				String goodsNote = "GZH";
				// 商品个数
				String goodsNum = "1";
				// 附加参数
				String remark = "GZH" + new Date().getTime();
				redisMap.put("goodsNote", goodsNote);
				redisMap.put("goodsNum", goodsNum);
				redisMap.put("remark", remark);
				// 微信appid
				String appid = redisMap.get("gzhAppId");
				// 微信秘钥
				String appsecret = redisMap.get("gzhKey");

				if (appid != null && appsecret != null) {
					// 获取需要的openid
					String openid = "";
					String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + appsecret + "&code=" + code
							+ "&grant_type=authorization_code";
					logger.info("===========》获取openidURL:" + URL);
					net.sf.json.JSONObject jsonObject = CommonUtil.httpsRequest(URL, "GET", null);
					if (null != jsonObject) {
						openid = jsonObject.getString("openid");
						logger.info("==========》用户openid为:" + openid);
						redisMap.put("openid", openid);
						// 将商品名字替换为订单号，让上游不知道是什么商品
						redisMap.put("orderName", orderNum);
						// 判断传参是否完整：
						boolean isNotNull = ObjectUtil.checkObject(new String[] { "orderNum", "orderName", "payMoney", "address", "goodsNum", "goodsNote",
								"remark", "gzhAppId", "openid" }, redisMap);
						if (isNotNull) {
							Map<String, String> banckMap = gateWayService.wechatGzhPayByCrack(redisMap);
							boolean flag = banckMap.containsKey("code");
							if (flag) {
								int code1 = Integer.valueOf(banckMap.get("code").toString());
								if (code1 == 10000) {
									banckMap.remove("code");
									banckMap.put("orderNum", orderNum);
									logger.info("=========》微信公众号支付返回结果:" + banckMap);
									mv.addObject("map", banckMap);
									// 删除缓存
									RedisDBUtil.redisDao.delete(orderNum);
									// 订单重复处理
									int type = Integer.valueOf(banckMap.get("type"));
									if (type == 2) {// 原生JS支付类型
										// 存入redis
										RedisDBUtil.redisDao.hmset(mapkey, banckMap);
										// 1小时后失效
										RedisDBUtil.redisDao.expire(mapkey, 3600);
										//存入次数； 1小时后失效
										RedisDBUtil.redisDao.setString(keyCount, "1", 3600);
									}
									return mv;
								} else {
									if (code1 == 10002) {
										// 民生银行订单重复
										resultMap = ReMessage.resultBack(ParameterEunm.PAY_REPEAT_FAIL, null);
									} else {
										resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
										logger.error("=========》微信公众号支付预下单接口失败:返回原因：msg=" + banckMap);
									}
								}
							} else {
								logger.error("失败");
								resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
							}
						} else {
							logger.error("参数不能为空,或者有误");
							resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
						}
					}
				} else {
					logger.error("参数不能为空,或者有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
		mv.setViewName(redirect);
		return mv;
	}

	/**
	 * wachatGzhByCrack 微信公众号支付破解版支付入口
	 * @param request
	 * @param response
	 * @param map
	 * @return    设定文件 
	 * Map<String,Object>    返回类型
	 */
	@ResponseBody
	@RequestMapping("/wachatGzhByCrack")
	public ModelAndView wachatGzhByCrack(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> map) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		logger.info("===========================================");
		logger.info("=========》欢迎商户来调起微信公众号破解版支付《========");
		logger.info("===========================================");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ModelAndView mv = new ModelAndView();
		// 返回错误信息
		String redirect = PropertiesUtil.getProperty("rate", "redirect_url") + "?resultCode=";
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "orderNum" }, map);
		if (isNotNull) {
			String orderNum = String.valueOf(map.get("orderNum"));
			try {
				//获取缓存预下单信息
				Map<String, String> redisMap=RedisDBUtil.redisDao.getStringMapAll(orderNum);
				String appid=redisMap.get("gzhAppId");
				if(appid!=null){
					//授权后要跳转的链接
					String backUri = baseUrl;
					backUri = backUri + "?orderNum="+orderNum;
					backUri = URLEncoder.encode(backUri);
					// scope 参数视各自需求而定，这里用scope=snsapi_base
					// 不弹出授权页面直接授权目的只获取统一支付接口的openid snsapi_userinfo是弹出窗口
					String url = openIdUrl + "?appid=" +appid+ "&redirect_uri=" + backUri
							+ "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
					logger.info("=========>获取codeUrl:" + url);
					url = "redirect:" + url;
					mv.setViewName(url);
					return mv;
				}else{
					System.out.println("=========>参数不能为空,或者有误,或者罗马数据请求缓存未存入");
					logger.debug("=========>参数不能为空,或者有误,或者罗马数据请求缓存未存入");
					resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.debug("=========>参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY, null);
		}
		redirect = redirect + resultMap.get("resultCode") + "&message=" + resultMap.get("message");
		mv.setViewName(redirect);
		return mv;
	}
}
