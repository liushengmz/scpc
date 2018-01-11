package com.pay.manger.controller.payv2;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.order.service.Payv2PayOrderRefundService;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PaySignUtil;
import com.pay.manger.util.StringHandleUtil;

/**
* @ClassName: PayWayController 
* @Description:支付集控制器
* @author qiuguojie
* @date 2016年12月9日 下午8:21:52
*/
@Controller
@RequestMapping("/payOrderRefundNew/*")
public class PayOrderRefundNewController{
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayOrderRefundService payv2PayOrderRefundService;
	
	public static void main(String[] args) throws Exception
	{
		testPayRefund();
	}
	
	public static void testPayRefund()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_name", "苹果手机");
		map.put("pay_money", 0.01);
		map.put("refund_num", "RR20171023122551239479");
		map.put("order_num", "DD20171023102500820697138");
		map.put("refund_type", "Y");
		map.put("refund_time", 1508732752000F);
		map.put("refund_money", 0.01);
		map.put("refund_reason", "");
		map.put("pay_discount_money", 0.01);
		map.put("buss_order_num", "20171023002");
		//map.put("sign", "c361b34755ad225700aa548d6a91eeb6");
		
		String app = "3e2e917fcab840f08a2ec904931a388f";
		String key = "BGxoSzVgrNKAclakrE5S0ajQI1Wo00qHTXpreM6k";
		
		try
		{
			System.out.println(PaySignUtil.getSign(map, key));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(PaySignUtil.checkSign(map, key));
	}
	
	public static void test1()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_name", "苹果手机");
		map.put("pay_money", 0.01);
		map.put("refund_num", "RR20171023122551239479");
		map.put("order_num", "DD20171023102500820697138");
		map.put("refund_type", "Y");
		map.put("refund_time", 1508732752000F);
		map.put("refund_money", 0.01);
		map.put("refund_reason", "");
		map.put("pay_discount_money", 0.01);
		map.put("buss_order_num", "20171023002");
		//map.put("sign", "c361b34755ad225700aa548d6a91eeb6");
		
		String app = "af4c1c5afbe9a1ce5a8e30745803a051";
		String key = "YiCfUyuVx1dxshpSh6GG9QPNWjpZFkDdlxwH2yjd";
		
		try
		{
			System.out.println(PaySignUtil.getSign(map, key));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(PaySignUtil.checkSign(map, key));
	}
	
	/**
	 * 支付退款
	 * @param request
	 * @param response
	 * @throws AlipayApiException 
	 * @throws Exception 
	 */
	@RequestMapping(value="/payRefund")
	@ResponseBody
	public Map<String, Object> payRefund(@RequestParam Map<String, Object> map) throws Exception{
		
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
		
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "refundMoney","refundType"}, paramMap);
		try {
			if(isNotNull){
				//商家订单和支付集订单同时不存在
				if(!paramMap.containsKey("orderNum")&&!paramMap.containsKey("bussOrderNum")){
					logger.debug("参数不能为空,或者有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
				}else{
					Map<String, Object> m = payv2PayOrderRefundService.payRefund(paramMap,pbca.getId(),pbca.getAppSecret());
					if(!m.containsKey("order_num")){
						logger.debug("交易不存在");
						resultMap = ReMessage.resultBack(ParameterEunm.TRANSACTION_NOT_EXIST,null);
					}else{
						//参数签名
						String sign = PaySignUtil.getSign(resultMap, pbca.getAppSecret());
						m.put("sign", sign);
						
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
					}
				}
			}else{
				logger.debug("商户签名错误");
				resultMap = ReMessage.resultBack(ParameterEunm.SIGNATURE_ERROR,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("服务器异常,请稍后再试");
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		
		return resultMap;
	}
	
	/**
	 * 查询退款
	 * @param request
	 * @param response
	 * @throws AlipayApiException 
	 * @throws Exception 
	 */
	@RequestMapping(value="/queryRefund")
	@ResponseBody
	public Map<String, Object> queryRefund(@RequestParam Map<String, Object> map) throws Exception{
		
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
		
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"refundNum"}, paramMap);
		try {
			if(isNotNull){
				if(!ObjectUtil.checkObject(new String[] {"refundNum"}, paramMap)){
					logger.debug("参数不能为空,或者有误");
					resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
				}else{
					Map<String,Object> m =payv2PayOrderRefundService.queryRefund(paramMap, pbca);
					if(m.containsKey("error_code")){
						if(m.get("error_code").equals("ORDER_ERROR")){
							resultMap = ReMessage.resultBack(ParameterEunm.ORDER_ERROR,null);
						}else{
							resultMap = ReMessage.resultBack(ParameterEunm.REFUND_NOT_EXIST,null);
						}
					}else{
						//参数签名
						String sign = PaySignUtil.getSign(m, pbca.getAppSecret());
						m.put("sign", sign);
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
					}
				}
			}else{
				logger.debug("参数不能为空,或者有误");
				resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("服务器异常,请稍后再试");
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		
		return resultMap;
	}
	
}
