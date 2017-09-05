package com.pay.manger.controller.payv2;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.date.DateStyle;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.IpAddressUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PayFinalUtil;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.alipay.AppAlipayNotify;
import com.pay.business.util.guofu.GuoFuPay;
import com.pay.business.util.wftpay.SignUtils;
import com.pay.business.util.wftpay.XmlUtils;
import com.pay.manger.util.ReturnMsgTips;

/**
* @ClassName: PayWayController 
* @Description:支付集控制器
* @author qiuguojie
* @date 2016年12月9日 下午8:21:52
*/
@Controller
@RequestMapping("/payDetail/*")
public class PayWayController{
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	
	public static void main(String[] args) throws Exception {
		
		/*//支付宝退款
		AlipayClient alipayClient = new DefaultAlipayClient(PayConfigApi.MOBILEPAY_SERVICE,
				AppAlipayConfig.app_id,AppAlipayConfig.private_key,"json",AppAlipayConfig.input_charset,
				AppAlipayConfig.ali_public_key);
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent("{" +
		"    \"out_trade_no\":\"DD2017060919433124869316\"," +
		"    \"refund_amount\":5," +
		"    \"refund_reason\":\"正常退款\"," +
		"    \"out_request_no\":\"HZ01RF001\"" +
		"  }");
		AlipayTradeRefundResponse response = alipayClient.execute(request);
		if(response.isSuccess()){
			System.out.println(response.getGmtRefundPay());
			System.out.println("调用成功");
		} else {
			System.out.println("调用失败");
		}*/
	}
	
	/**
	* @throws Exception 
	* @Description:支付信息
	* @param request
	* @param response
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2016年9月2日 下午4:37:51 
	* @throws
	*/
	@ResponseBody
	@RequestMapping(value = "/payOrderDetail")
	public Map<String,Object> payOrderDetail(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> map) throws Exception{
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, map);
		if(appKeyCon){
			
			//经纬度和用户手机标识不进行签名  --------------------
			String userDeviceToken = null;
			if(map.containsKey("userDeviceToken")){
				userDeviceToken = map.get("userDeviceToken").toString();
				map.remove("userDeviceToken");
			}
			String lon = null;
			String lat = null;
			if(map.containsKey("lon")&&map.containsKey("lat")){
				lon = map.get("lon").toString();
				map.remove("lon");
				lat = map.get("lat").toString();
				map.remove("lat");
			}
			//---------------------------------------
			
			//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
			Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(map.get("appKey").toString());
			if(pbca!=null){
				//验签
				boolean con = PaySignUtil.checkSign(map, pbca.getAppSecret());
				if(con){
					//验签后把经纬度和用户手机标识加上-------------------
					if(userDeviceToken!=null){
						map.put("userDeviceToken", userDeviceToken);
					}
					if(lon!=null&&lat!=null){
						map.put("lon", lon);
						map.put("lat", lat);
					}
					//-------------------------------------------
					
					boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","appKey","payMoney","orderName","notifyUrl" }, map);
					try {
						if (isNotNull&&DecimalUtil.isBigDecimal(map.get("payMoney").toString())) {
							Map<String, Object> m = payv2PayOrderService.payOrderDetail(map,pbca);
							if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
								resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
							}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
								resultMap = ReMessage.resultBack(ParameterEunm.ORDER_ERROR, null);
							}else{
								m.remove("status");
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
							}
						} else {
							logger.debug("参数不能为空,或者有误");
							resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.debug("服务器异常,请稍后再试");
						resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
					}
				}else{
					logger.debug("商户签名错误");
					resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
				}
			}else{
				logger.debug("appKey无效");
				resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			}
		}else{
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
		}
		return resultMap;
	}
	
	/**
	* @throws Exception 
	* @Description:支付信息   h5支付
	* @param request
	* @param response
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2016年9月2日 下午4:37:51 
	* @throws
	*/
	@ResponseBody
	@RequestMapping(value = "/payOrderH5Detail")
	public Map<String,Object> payOrderH5Detail(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> map) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, map);
		if(appKeyCon){
			//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
			Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(map.get("appKey").toString());
			if(pbca!=null){
				//验签
				boolean con = PaySignUtil.checkSign(map, pbca.getAppSecret());
				if(con){
					
					boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","appKey","payMoney","orderName" }, map);
					try {
						if (isNotNull&&DecimalUtil.isBigDecimal(map.get("payMoney").toString())) {
							Map<String, Object> m = payv2PayOrderService.payOrderH5Detail(map,pbca);
							if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
								resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
							}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
								resultMap = ReMessage.resultBack(ParameterEunm.ORDER_ERROR, null);
							}else{
								m.remove("status");
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
							}
						} else {
							logger.debug("参数不能为空,或者有误");
							resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
						}
					} catch (Exception e) {
						resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
					}
				}else{
					logger.debug("商户签名错误");
					resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
				}
			}else{
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		}else{
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
		}
		return resultMap;
	}
	
	/**
	 * 支付信息签名
	 * */
	@RequestMapping("/payForOrder")
	@ResponseBody
	public Map<String,Object> payForOrder(@RequestParam Map<String, Object> map,HttpServletResponse response
			,HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"orderNum","payWayId" }, map);
		try {
			if(isNotNull){
				map.put("ip", IpAddressUtil.getIpAddress(request));
				Map<String,String> m = payv2PayOrderService.payForOrder(map);
				if("-1".equals(m.get("status").toString())){
					logger.debug("参数不能为空,或者有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
					return resultMap;
				}
				m.remove("status");
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
			}else{
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null,
					ReturnMsgTips.ERROR_SERVER_ERROR);
		}
		return resultMap;
	}
	
	/**
	 * 手机网站h5支付
	 * @param request
	 * @param response
	 * @throws AlipayApiException 
	 * @throws IOException 
	 * @throws Exception 
	 */
	@RequestMapping(value="/payH5Alipay")
	@ResponseBody
	public Map<String,Object> payH5Alipay(@RequestParam Map<String, Object> map,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		response.setHeader("Access-Control-Allow-Origin", "*");
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"orderNum","payWayId","appType" }, map);
		if(isNotNull){
			try {
				map.put("ip", IpAddressUtil.getIpAddress(request));
				Map<String, String> m = payv2PayOrderService.payH5Alipay(map);
				if("-1".equals(m.get("status").toString())){
					logger.debug("参数不能为空,或者有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
					return resultMap;
				}
				m.remove("status");
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
			} catch (Exception e) {
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
			}
		}else{
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
		}
		return resultMap;
	}
	
	/**
	 * 支付宝回调
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/aliPayCallBack")
	public void aliPayCallBack(@RequestParam Map<String, String> map,HttpServletRequest request,HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		String out_trade_no = map.get("out_trade_no");// 商户订单号
		Payv2PayOrder payv2PayOrder = payv2PayOrderService.getOrderInfo(out_trade_no);
		if(payv2PayOrder!=null){
			boolean veryfy = AppAlipayNotify.verify(map,payv2PayOrder.getRateKey3());
			System.out.println("用于将支付宝回调的数据验签:"+veryfy);
			if(veryfy){//为真验证没问题
				boolean con = payv2PayOrderService.aliPayCallBack(map,payv2PayOrder);
				if(con){
					System.out.println("通知商户成功"+con);
					out.write("success");
				}else{
					System.out.println("通知商户失败"+con);
					out.write("error");
				}
			}else{
				out.write("error");
			}
		}else{
			out.write("error");
		}
		if(out!=null){
			out.close();
		}
	}
	
	/**
	 * 支付宝h5回调
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/aliPayH5CallBack")
	public void aliPayH5CallBack(@RequestParam Map<String, String> map,HttpServletRequest request,HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		String out_trade_no = map.get("out_trade_no");// 商户订单号
		Payv2PayOrder payv2PayOrder = payv2PayOrderService.getOrderInfo(out_trade_no);
		if(payv2PayOrder!=null){
			boolean veryfy = AlipaySignature.rsaCheckV1(map, payv2PayOrder.getRateKey3(), "utf-8");
			System.out.println("h5用于将支付宝回调的数据验签:"+veryfy);
			if(veryfy){//为真验证没问题
				boolean con = payv2PayOrderService.aliPayCallBack(map,payv2PayOrder);
				if(con){
					System.out.println("通知商户成功"+con);
					out.write("success");
				}else{
					System.out.println("通知商户失败"+con);
					out.write("error");
				}
			}else{
				out.write("error");
			}
		}else{
			out.write("error");
		}
		if(out!=null){
			out.close();
		}
	}
	
	/**
	 * 威富通微信回调
	 * @param map
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/wftWechatPayCallBack")
	public void wftWechatPayCallBack(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
            String resString = XmlUtils.parseRequst(request);
            //System.out.println("通知内容：" + resString);
            
            String respString = "fail";
            if(resString != null && !"".equals(resString)){
                Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
                String out_trade_no = map.get("out_trade_no");// 商户订单号
                Payv2PayOrder payv2PayOrder = payv2PayOrderService.getOrderInfo(out_trade_no);
                if(payv2PayOrder!=null){
	                String res = XmlUtils.toXml(map);
	                //System.out.println("通知内容：" + res);
	                if(map.containsKey("sign")){
	                    if(!SignUtils.checkParam(map, payv2PayOrder.getRateKey2())){
	                        res = "验证签名不通过";
	                        logger.debug(res);
	                        respString = "fail";
	                    }else{
	                        String status = map.get("status");
	                        if(status != null && "0".equals(status)){
	                            String result_code = map.get("result_code");
	                            if(result_code != null && "0".equals(result_code)){
	                            	//后台数据订单状态更新等操作
	                            	boolean con = payv2PayOrderService.wftWechatPayCallBack(map);
	                            	if(con){
	                            		respString = "success";
	                            	}
	                            } 
	                        } 
	                    }
	                }
                }
            }
            response.getWriter().write(respString);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 兴业深圳回调
	 * @param map
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/aliScanCallBack")
	public void ms(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String resString = XmlUtils.parseRequst(request);
		Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
		System.out.println("兴业深圳回调：" + map.toString());
		try {
			String orderNum = map.get("out_trade_no");
			Payv2PayOrder payOrder = payv2PayOrderService.getOrderInfo(orderNum);
			if(null != payOrder){
				Map<String, String> params = new HashMap<String, String>();
				params.put("out_trade_no", orderNum);
				if(SignUtils.checkParam(map, payOrder.getRateKey2())){
					System.out.println("================兴业深圳验签通过================");
					if("0".equals(map.get("pay_result").toString())){
						params.put("trade_status", "TRADE_SUCCESS");
					}
					params.put("trade_no", map.get("transaction_id"));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					String time = map.get("time_end");
					Date date = sdf.parse(time);
					params.put("gmt_payment", DateUtil.DateToString(date, "yyyy-MM-dd HH:mm:ss"));
					//params.put("gmt_payment",DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
					params.put("total_amount", DecimalUtil.centsToYuan(map.get("total_fee")));
					
					boolean bool = payv2PayOrderService.aliPayCallBack(params, payOrder);
					System.out.println("================兴业深圳写数据库================");
					if(!bool){
						System.out.println("================兴业深圳写数据库或者回调商户失败================");
						response.getWriter().write("fail");
					}
				}else{
					System.out.println("验签失败");
					response.getWriter().write("fail");
				}
			}else{
				response.getWriter().write("fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("fail");
		}
		response.getWriter().write("success");
	}
	
	/**
	 * 支付宝回调（点游）
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/dyAliPayCallBack")
	public void dyAliPayCallBack(@RequestParam Map<String, String> map,HttpServletRequest request,HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		String out_trade_no = map.get("out_trade_no");// 商户订单号
		Payv2PayOrder payv2PayOrder = payv2PayOrderService.getOrderInfo(out_trade_no);
		if(payv2PayOrder!=null){
			boolean veryfy = AppAlipayNotify.verify(map,payv2PayOrder.getRateKey3());
			System.out.println("用于将支付宝回调的数据验签:"+veryfy);
			if(veryfy){//为真验证没问题
				boolean con = payv2PayOrderService.dyAliPayCallBack(map,payv2PayOrder);
				if(con){
					System.out.println("通知商户成功"+con);
					out.write("success");
				}else{
					System.out.println("通知商户失败"+con);
					out.write("error");
				}
			}else{
				out.write("error");
			}
		}else{
			out.write("error");
		}
		if(out!=null){
			out.close();
		}
	}
	
	/**
	 * 订单查询
	 * */
	@RequestMapping("/queryOrderNum")
	@ResponseBody
	public Map<String,Object> queryOrderNum(@RequestParam Map<String, Object> map,HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap = payv2PayOrderService.queryOrderNum(map);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null,
					ReturnMsgTips.ERROR_SERVER_ERROR);
		}
		return resultMap;
	}
	
	/**
	 * 订单查询  （提供给商户服务器查询订单）
	 * */
	@RequestMapping("/queryOrder")
	@ResponseBody
	public Map<String,Object> queryOrder(@RequestParam Map<String, Object> map,HttpServletResponse response) {
		long time1 = System.currentTimeMillis();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"appKey"}, map);
		try {
			if(isNotNull){
				//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
				Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(map.get("appKey").toString());
				if(pbca!=null){
					//验签
					boolean con = PaySignUtil.checkSign(map, pbca.getAppSecret());
					if(con){
						//商家订单和支付集订单同时不存在
						if(!map.containsKey("orderNum")&&!map.containsKey("bussOrderNum")){
							logger.debug("参数不能为空,或者有误");
							resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
						}else{
							Map<String, Object> m = payv2PayOrderService.queryOrder(map,pbca);
							
							if(m.get("status").equals("1")){
								Payv2PayOrder payOrder = (Payv2PayOrder)m.get("payOrder");
								resultMap.put("order_name", payOrder.getOrderName());
								resultMap.put("buss_order_num", payOrder.getMerchantOrderNum());
								resultMap.put("order_num", payOrder.getOrderNum());
								resultMap.put("pay_money", payOrder.getPayMoney());
								resultMap.put("pay_discount_money", payOrder.getPayDiscountMoney());
								resultMap.put("pay_time", DateStr(payOrder.getPayTime()==null?payOrder.getCreateTime():payOrder.getPayTime()));
								resultMap.put("create_time", DateStr(payOrder.getCreateTime()));
								//参数签名
								String sign = PaySignUtil.getSign(resultMap, pbca.getAppSecret());
								resultMap.put("sign", sign);
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
							}else if(m.get("status").equals("2")){ //订单未支付
								resultMap = ReMessage.resultBack(ParameterEunm.NOTPAY_FAILED_CODE,null);
							}else if(m.get("status").equals("3")){//参数不能为空,或者有误
								resultMap = ReMessage.resultBack(ParameterEunm.TRANSACTION_NOT_EXIST,null);
							}else{  //支付失败
								resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_CODE,null);
							}
						}
					}else{
						logger.debug("商户签名错误");
						resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
					}
				}else{
					logger.debug("appKey无效");
					resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
				}
			}else{
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		logger.debug("订单查询请求耗时："+(System.currentTimeMillis()-time1));
		return resultMap;
	}
	
	/**
	* @Description:支付信息   线下扫码支付
	* @param request
	* @param response
	* @param map
	* @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @date 2016年9月2日 下午4:37:51 
	* @throws
	*/
	@ResponseBody
	@RequestMapping(value = "/payOrderShopDetail")
	public Map<String,Object> payOrderShopDetail(HttpServletRequest request,HttpServletResponse response,@RequestParam Map<String, Object> map){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, map);
		if(appKeyCon){
			try {
				Map<String, Object> m = payv2PayOrderService.payOrderShopDetail(map);
				if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
					resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
				}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
					resultMap = ReMessage.resultBack(ParameterEunm.ORDER_ERROR, null);
				}else{
					m.remove("status");
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
			}
		}else{
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
		}
		return resultMap;
	}
	
	/**
	 * 天付宝回调
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/tfbCallBack")
	public void tfbCallBack(@RequestParam Map<String, String> map,HttpServletRequest request,HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		out.write("success");
		if(out!=null){
			out.close();
		}
	}
	
	/**
	 * 支付宝h5回调
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="/test")
	public void test(@RequestParam Map<String, String> map,HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("回调！：" + map.toString());
	}
	
	/**
	 * 国付回调
	 * @throws IOException 
	 */
	@RequestMapping("/guofuCallBack")
	public void guofuCallBack(@RequestParam Map<String, String> map,HttpServletRequest request,HttpServletResponse response) throws IOException{
		System.out.println("国付回调：" + map.toString());
		//amount=0.01, channelOrderno=13590497016011201708221119501018, channelTraceno=199520378478201708226176402891, merchName=一站网络(测试), merchno=820440348160001, orderno=82170822100000350828, payType=8, 
		//signature=AB514D06C70B9D5E533DBBF7EBAC68E8, status=1, traceno=1503367297796, transDate=2017-08-22, transTime=10:02:45
		try {
			String orderNum = map.get("traceno");
			Payv2PayOrder payOrder = payv2PayOrderService.getOrderInfo(orderNum);
			if(null != payOrder){
				Map<String, String> params = new HashMap<String, String>();
				String signature = GuoFuPay.signature(map, payOrder.getRateKey2(), "GBK");
				//验签
				if (signature.equals(map.get("signature"))) {
					//支付成功
					if("1".equals(map.get("status").toString())){
						params.put("trade_status", "TRADE_SUCCESS");
					}
					String date = map.get("transDate") + " " + map.get("transTime");
					
					params.put("gmt_payment", date);
					params.put("trade_no", map.get("orderno"));
					params.put("total_amount", map.get("amount"));
					boolean bool = payv2PayOrderService.aliPayCallBack(params, payOrder);
					System.out.println("================国付回调写数据库================");
					if(!bool){
						System.out.println("================国付回调写数据库或者回调商户失败================");
						response.getWriter().write("fail");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("fail");
		}
		response.getWriter().write("success");
	}
	
	private String DateStr(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
	}
	
	/// 根据 Agent 判断是否是智能手机    
	///</summary>    
	///<returns></returns>    
	@RequestMapping(value="/aaaaaaa")
	public static void CheckAgent(@RequestParam Map<String, String> map,HttpServletRequest request,HttpServletResponse response)    
	{    
	    String agent = request.getHeader("User-Agent");
	    String[] keywords = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser" };    
	  
	       //排除 Windows 桌面系统    
	        if (agent.indexOf("Windows NT")==-1 || (agent.indexOf("Windows NT")!=-1 && agent.indexOf("compatible; MSIE 9.0;")!=-1))    
	        {    
	            //排除 苹果桌面系统    
	            if (agent.indexOf("Windows NT")==-1 && agent.indexOf("Macintosh")==-1)    
	            {    
	            	for (String string : keywords) 
	                {    
	                    if (agent.indexOf(string)!=-1)    
	                    {    
	                        break;    
	                    }    
	                }    
	            }    
	        }    
	  
	}
}
