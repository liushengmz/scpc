package com.pay.manger.controller.payv2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.properties.PropertiesUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.service.PaymentService;
import com.pay.business.payv2.service.Payv2BankAppKeyService;
import com.pay.business.record.service.Payv2BillDownService;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.IpAddressUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PayFinalUtil;
import com.pay.business.util.PaySignUtil;
import com.pay.manger.util.StringHandleUtil;

/**
* @ClassName: Payv2PayController 
* @Description:支付集控制器
* @author qiuguojie
* @date 2016年12月9日 下午8:21:52
*/
@Controller
@RequestMapping("/pay/*")
public class Payv2PayController{
	
	//Logger logger = Logger.getLogger(this.getClass());
	private static final Log logger = LogFactory.getLog(Payv2PayController.class);
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	@Autowired
	private Payv2BankAppKeyService payv2BankAppKeyService;
	@Autowired
	private Payv2BillDownService payv2BillDownService;
	@Autowired
	private PaymentService paymentService;
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
	public Map<String,Object> payOrderDetail(HttpServletRequest request,HttpServletResponse response
			,@RequestParam Map<String, Object> map) throws Exception{
		response.setHeader("Access-Control-Allow-Origin", "*");
		//System.out.println(Thread.currentThread().getId());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if(!paramStrCon){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if(paramMap.keySet().size()==0){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			logger.debug("商户签名错误");
			resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
			return resultMap;
		}
		
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","appKey","payMoney"
				,"orderName","notifyUrl" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
					Map<String, Object> m = payv2PayOrderService.payOrderDetail(paramMap,pbca);
					if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
					}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
						resultMap = ReMessage.resultBack(ParameterEunm.ORDER_ERROR, null);
					}else{
						m.remove("status");
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
					}
				}else{
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
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
	public Map<String,Object> payOrderH5Detail(HttpServletRequest request,HttpServletResponse response
			,@RequestParam Map<String, Object> map) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if(!paramStrCon){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if(paramMap.keySet().size()==0){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			logger.debug("商户签名错误");
			resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
			return resultMap;
		}
		
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","appKey","payMoney"
				,"orderName" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
					Map<String, Object> m = payv2PayOrderService.payOrderH5Detail(paramMap,pbca);
					if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
					}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
						resultMap = ReMessage.resultBack(ParameterEunm.ORDER_ERROR, null);
					}else{
						m.remove("status");
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
					}
				}else{
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
				}	
			} else {
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		} catch (Exception e) {
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
		}
		
		return resultMap;
	}
	
	/**
	 * 支付宝接口(提供点游)
	 * @throws Exception 
	 * */
	@RequestMapping("/alipayPage")
	@ResponseBody
	public Map<String,Object> alipayPage(@RequestParam Map<String, Object> map
			,HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if(!paramStrCon){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if(paramMap.keySet().size()==0){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			logger.debug("商户签名错误");
			resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
			return resultMap;
		}
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","payMoney"
				,"orderName","notifyUrl" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
					Map<String, String> m = payv2PayOrderService
							.payPage(IpAddressUtil.getIpAddress(request),null,paramMap,pbca,1);
					if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
					}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
						//已超过限额,请检查支付宝通道单笔额度和每日额度
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_ORDER_ERROR, null);
					}else if(PayFinalUtil.PAY_TYPE_FAIL.equals(m.get("status"))){
						//未配置支付通道或支付类型不支持
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_TYPE_ERROR, null);
					}else if(PayFinalUtil.PAYORDER_QUERY_FAIL.equals(m.get("status"))){
						logger.debug("参数不能为空,或者有误");
						resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
						return resultMap;
					}else if(PayFinalUtil.RATE_FAIL.equals(m.get("status"))){
						//支付通道下单出错
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_FAIL, null);
					}else{
						m.remove("status");
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
					}
				}else{
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
				}
			}else {
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
		}
		
		return resultMap;
	}
	
	/**
	 * 微信接口（提供点游）
	 * @throws Exception 
	 * */
	@RequestMapping("/wxpayPage")
	@ResponseBody
	public Map<String,Object> wxpayPage(@RequestParam Map<String, Object> map
			,HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr","appType" }, map);
		if(!paramStrCon){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if(paramMap.keySet().size()==0){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			logger.debug("商户签名错误");
			resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
			return resultMap;
		}
		
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","payMoney"
				,"orderName","notifyUrl" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
					Map<String, String> m = payv2PayOrderService.payPage(IpAddressUtil.getIpAddress(request)
							,map.get("appType").toString(),paramMap,pbca,2);
					if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
					}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
						//已超过限额,请检查微信通道单笔额度和每日额度
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_ORDER_ERROR, null);
					}else if(PayFinalUtil.PAY_TYPE_FAIL.equals(m.get("status"))){
						//未配置支付通道或支付类型不支持
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_TYPE_ERROR, null);
					}else if(PayFinalUtil.PAYORDER_QUERY_FAIL.equals(m.get("status"))){
						logger.debug("参数不能为空,或者有误");
						resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
						return resultMap;
					}else if(PayFinalUtil.RATE_FAIL.equals(m.get("status"))){
						//支付通道下单出错
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_FAIL, null);
					}else{
						m.remove("status");
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
					}
				}else{
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
				}
			}else {
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
		}
		return resultMap;
	}
	
	/**
	 * 对账接口
	 * @throws Exception 
	 * */
	@RequestMapping("/payBillDown")
	@ResponseBody
	public Map<String,Object> payBillDown(@RequestParam Map<String, Object> map
			,HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if(!paramStrCon){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if(paramMap.keySet().size()==0){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			logger.debug("商户签名错误");
			resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
			return resultMap;
		}
		
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "billTime"}, paramMap);
		try {
			if (isNotNull&&checkTime(paramMap.get("billTime").toString())) {
				String url = payv2BillDownService.billDownUrl(paramMap.get("billTime").toString()
						, pbca.getId());
				if(StringUtils.isBlank(url)){
					resultMap = ReMessage.resultBack(ParameterEunm.CLEAR_ORDER_ERROR,null);
				}else{
					resultMap.put("billDownUrl", url);
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
				}
			}else {
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
		}
		return resultMap;
	}
	
	/**
	 * 检查是否是时间格式yyyyMMdd
	 * @return
	 */
	private boolean checkTime(String time){
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		// 设置日期转化成功标识
		boolean dateflag=true;
		// 这里要捕获一下异常信息
		try
		{
			Date date = format.parse(time);
		} catch (Exception e)
		{
			dateflag=false;
		}
		return dateflag;
	}
	
	/**
	 * 订单查询  （提供给商户服务器查询订单）
	 * @throws Exception 
	 * */
	@RequestMapping("/queryOrder")
	@ResponseBody
	public Map<String,Object> queryOrder(@RequestParam Map<String, Object> map) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if(!paramStrCon){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if(paramMap.keySet().size()==0){
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			return resultMap;
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			logger.debug("appKey无效");
			resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
			return resultMap;
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			logger.debug("商户签名错误");
			resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
			return resultMap;
		}
		
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"appKey"}, paramMap);
		try {
			if(isNotNull){
				//商家订单和支付集订单同时不存在
				if(!paramMap.containsKey("orderNum")&&!paramMap.containsKey("bussOrderNum")){
					logger.debug("参数不能为空,或者有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
				}else{
					Map<String, Object> m = payv2PayOrderService.queryOrder(paramMap,pbca);
					
					if(m.get("status").equals("1")){
						Payv2PayOrder payOrder = (Payv2PayOrder)m.get("payOrder");
						resultMap.put("order_name", payOrder.getOrderName());
						resultMap.put("buss_order_num", payOrder.getMerchantOrderNum());
						resultMap.put("order_num", payOrder.getOrderNum());
						resultMap.put("pay_money", payOrder.getPayMoney().toString());
						resultMap.put("pay_discount_money", payOrder.getPayDiscountMoney().toString());
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
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		return resultMap;
	}
	
	/**
	 * 纯h5支付接口
	 * @throws Exception 
	 * */
	@RequestMapping("/payment")
	public ModelAndView payment(@RequestParam Map<String, Object> map
			,HttpServletRequest request) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		String redirect = PropertiesUtil.getProperty("rate", "redirect_url")+"?resultCode=";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if(!paramStrCon){
			return paramError(resultMap, redirect);
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		if(paramMap.keySet().size()==0){
			return paramError(resultMap, redirect);
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			return appKeyError(resultMap, redirect);
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			return appKeyError(resultMap, redirect);
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			return signError(resultMap, redirect);
		}
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","payMoney"
				,"orderName","notifyUrl","appType","ip","payPlatform" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
					Map<String, String> m = paymentService.payment(paramMap, pbca, 2);
					if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
					}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
						//已超过限额,请检查微信通道单笔额度和每日额度
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_ORDER_ERROR, null);
					}else if(PayFinalUtil.PAY_TYPE_FAIL.equals(m.get("status"))){
						//未配置支付通道或支付类型不支持
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_TYPE_ERROR, null);
					}else if(PayFinalUtil.PAYORDER_QUERY_FAIL.equals(m.get("status"))){
						logger.debug("参数不能为空,或者有误");
						resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
					}else if(PayFinalUtil.RATE_FAIL.equals(m.get("status"))){
						//支付通道下单出错
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_FAIL, null);
					}else{
						if(m.containsKey("alipayStr")){
							mv = new ModelAndView("pay/h5AliPay");
							mv.addObject("alipayStr", m.get("alipayStr"));
							return mv;
							//mv.setViewName("redirect:http://qiuguojie.wicp.net/page/pay/h5Pay.html?alipayStr="+URLEncoder.encode(m.get("alipayStr"),"UTF-8"));	
						}
						if(m.containsKey("webStr")){
							mv.setViewName("redirect:"+m.get("webStr"));
						}
						m.remove("status");
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
						resultMap.remove("message");
						return mv;
					}
				}else{
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
				}
			}else {
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
		}
		redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message");
		mv.setViewName(redirect);
		return mv;
	}
	
	private String DateStr(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
	}
	
	/**
	 * sdk支付(需包名)
	 */
	@RequestMapping("/sdkPayment")
	public ModelAndView sdkPayment(@RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();
		String redirect = PropertiesUtil.getProperty("rate", "redirect_url")+"?resultCode=";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr","package" }, map);
		if(!paramStrCon){
			return paramError(resultMap, redirect);
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		
		if(paramMap.keySet().size()==0){
			return paramError(resultMap, redirect);
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			return appKeyError(resultMap, redirect);
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			return appKeyError(resultMap, redirect);
		}
		
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			return signError(resultMap, redirect);
		}
		paramMap.put("ip", IpAddressUtil.getIpAddress(request));
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","payMoney"
				,"orderName","notifyUrl","appType","ip","payPlatform" }, paramMap);
		if(isNotNull){
			if(pbca.getAndroidAppPackage()!=null&&!"".equals(pbca.getAndroidAppPackage())&&"1".equals(paramMap.get("appType").toString())) {
				if(!pbca.getAndroidAppPackage().equals(map.get("package").toString())){
					logger.debug("包名验证有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_PACKAGE_FAIL,null);
					redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message");
					mv.setViewName(redirect);
					return mv;
				}
			}else if (pbca.getIosIphoneId()!=null&&!"".equals(pbca.getAndroidAppPackage())&&"2".equals(paramMap.get("appType").toString())) {
				if(!pbca.getIosIphoneId().equals(map.get("package").toString())){
					logger.debug("包名验证有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_PACKAGE_FAIL,null);
					redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message");
					mv.setViewName(redirect);
					return mv;
				}
			}
			if (DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
					&&DecimalUtil.isZero(paramMap.get("payMoney").toString())) {

				Map<String, String> m = paymentService.payment(paramMap, pbca, null);

				if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
					resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
				}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
					//已超过限额,请检查微信通道单笔额度和每日额度
					resultMap = ReMessage.resultBack(ParameterEunm.RATE_ORDER_ERROR, null);
				}else if(PayFinalUtil.PAY_TYPE_FAIL.equals(m.get("status"))){
					//未配置支付通道或支付类型不支持
					resultMap = ReMessage.resultBack(ParameterEunm.RATE_TYPE_ERROR, null);
				}else if(PayFinalUtil.PAYORDER_QUERY_FAIL.equals(m.get("status"))){
					logger.debug("参数不能为空,或者有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
				}else if(PayFinalUtil.RATE_FAIL.equals(m.get("status"))){
					//支付通道下单出错
					resultMap = ReMessage.resultBack(ParameterEunm.RATE_FAIL, null);
				}else{
					mv = new ModelAndView("pay/transfer");
					if(m.containsKey("alipayStr")){
						mv.addObject("alipayStr", m.get("alipayStr"));
						return mv;
					}
					if(m.containsKey("webStr")){
						mv.addObject("webStr", m.get("webStr"));
					}
					m.remove("status");
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
					resultMap.remove("message");
					return mv;
				}
			}else {
				logger.debug("支付金额错误");
				resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
			}
		}else{
			logger.debug("参数不能为空,或者有误");
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
		}
		
		//包名检测
		
		redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message");
		mv.setViewName(redirect);
		
		return mv;
	}
	
	/**
	 * 多功能支付
	 */
	@RequestMapping("/multifunctionPayment")
	public ModelAndView multifunctionPayment(@RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();
		String redirect = PropertiesUtil.getProperty("rate", "redirect_url")+"?resultCode=";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if(!paramStrCon){
			return paramError(resultMap, redirect);
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		
		if(paramMap.keySet().size()==0){
			return paramError(resultMap, redirect);
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			return appKeyError(resultMap, redirect);
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			return appKeyError(resultMap, redirect);
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		if(!con){
			return signError(resultMap, redirect);
		}
		//paramMap.put("ip", IpAddressUtil.getIpAddress(request));
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","payMoney"
				,"orderName","notifyUrl","appType","ip","payPlatform" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
				
					Map<String, String> m = paymentService.payment(paramMap, pbca, null);
					
					if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
					}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
						//已超过限额,请检查微信通道单笔额度和每日额度
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_ORDER_ERROR, null);
					}else if(PayFinalUtil.PAY_TYPE_FAIL.equals(m.get("status"))){
						//未配置支付通道或支付类型不支持
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_TYPE_ERROR, null);
					}else if(PayFinalUtil.PAYORDER_QUERY_FAIL.equals(m.get("status"))){
						logger.debug("参数不能为空,或者有误");
						resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
					}else if(PayFinalUtil.RATE_FAIL.equals(m.get("status"))){
						//支付通道下单出错
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_FAIL, null);
					}else{
						mv = new ModelAndView("pay/transfer");
						if(m.containsKey("alipayStr")){
							mv.addObject("alipayStr", m.get("alipayStr"));
							return mv;
						}
						if(m.containsKey("webStr")){
							mv.addObject("webStr", m.get("webStr"));
						}
						m.remove("status");
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
						resultMap.remove("message");
						return mv;
					}
				}else{
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
				}
			}else {
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
		}
		redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message");
		mv.setViewName(redirect);
		
		return mv;
	}
	
	/**
	 * 微信公众号破解支付
	 */
	@RequestMapping("/wxAccountPayment")
	public ModelAndView wxAccountPayment(@RequestParam Map<String, Object> map, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView();
		String redirect = PropertiesUtil.getProperty("rate", "redirect_url")+"?resultCode=";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean paramStrCon = ObjectUtil.checkObject(new String[] { "paramStr" }, map);
		if(!paramStrCon){
			return paramError(resultMap, redirect);
		}
		Map<String, Object> paramMap = StringHandleUtil.toMap(map.get("paramStr").toString());
		
		if(paramMap.keySet().size()==0){
			return paramError(resultMap, redirect);
		}
		boolean appKeyCon = ObjectUtil.checkObject(new String[] { "appKey" }, paramMap);
		if(!appKeyCon){
			return appKeyError(resultMap, redirect);
		}
		//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
		Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(paramMap.get("appKey").toString());
		if(pbca==null){
			return appKeyError(resultMap, redirect);
		}
		//验签
		boolean con = PaySignUtil.checkSign(paramMap, pbca.getAppSecret());
		paramMap.put("payPlatform", 2);
		if(!con){
			return signError(resultMap, redirect);
		}
		//paramMap.put("ip", IpAddressUtil.getIpAddress(request));
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "bussOrderNum","payMoney"
				,"orderName","notifyUrl","ip","payPlatform" }, paramMap);
		try {
			if (isNotNull) {
				if(DecimalUtil.isBigDecimal(paramMap.get("payMoney").toString())
						&&DecimalUtil.isZero(paramMap.get("payMoney").toString())){
					Map<String, String> m = paymentService.payment(paramMap, pbca, 5);
					
					if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL_OK)){
						resultMap = ReMessage.resultBack(ParameterEunm.PAY_FAILED_SUCCESS, null);
					}else if(m.get("status").equals(PayFinalUtil.PAY_STATUS_FAIL)){
						//已超过限额,请检查微信通道单笔额度和每日额度
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_ORDER_ERROR, null);
					}else if(PayFinalUtil.PAY_TYPE_FAIL.equals(m.get("status"))){
						//未配置支付通道或支付类型不支持
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_TYPE_ERROR, null);
					}else if(PayFinalUtil.PAYORDER_QUERY_FAIL.equals(m.get("status"))){
						logger.debug("参数不能为空,或者有误");
						resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
					}else if(PayFinalUtil.RATE_FAIL.equals(m.get("status"))){
						//支付通道下单出错
						resultMap = ReMessage.resultBack(ParameterEunm.RATE_FAIL, null);
					}else{
						mv = new ModelAndView("pay/transfer");
						if(m.containsKey("alipayStr")){
							mv.addObject("alipayStr", m.get("alipayStr"));
							return mv;
						}
						if(m.containsKey("webStr")){
							mv.addObject("webStr", m.get("webStr"));
						}
						m.remove("status");
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
						resultMap.remove("message");
						return mv;
					}
				}else{
					logger.debug("支付金额错误");
					resultMap = ReMessage.resultBack(ParameterEunm.MOENY_ERROR,null);
				}
			}else {
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null, null);
		}
		redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message");
		mv.setViewName(redirect);
		
		return mv;
	}
	
	/**
	 * 参数错误通用返回
	 */
	private ModelAndView paramError(Map<String, Object> resultMap, String redirect){
		ModelAndView mv = new ModelAndView();
		logger.debug("参数不能为空,或者有误");
		resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
		redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message");
		mv.setViewName(redirect);
		return mv;
	}
	
	/**
	 * appkey错误通用返回
	 */
	private ModelAndView appKeyError(Map<String, Object> resultMap, String redirect){
		ModelAndView mv = new ModelAndView();
		logger.debug("appKey无效");
		resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
		redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message").toString();
		mv.setViewName(redirect);
		return mv;
	}
	
	/**
	 * 签名错误通用返回
	 */
	private ModelAndView signError(Map<String, Object> resultMap, String redirect){
		ModelAndView mv = new ModelAndView();
		logger.debug("商户签名错误");
		resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
		redirect=redirect+resultMap.get("resultCode")+"&message="+resultMap.get("message").toString();
		mv.setViewName(redirect);
		return mv;
	}
	
}
