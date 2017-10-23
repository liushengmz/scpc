package com.pay.manger.controller.payv2;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alipay.api.AlipayApiException;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.order.service.Payv2PayOrderRefundService;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PaySignUtil;

/**
* @ClassName: PayWayController 
* @Description:支付集控制器
* @author qiuguojie
* @date 2016年12月9日 下午8:21:52
*/
@Controller
@RequestMapping("/payOrderRefund/*")
public class PayOrderRefundController{
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayOrderRefundService payv2PayOrderRefundService;
	
	public static void main(String[] args) {
		String discountRegion = "[{\"pro\":1,\"city\":2},{\"pro\":3,\"city\":4}]";
		JSONArray json = JSONArray.parseArray(discountRegion);
		for (Object object : json) {
			Map<String,Long> map = (Map<String,Long>)object;
			System.out.println(map.get("city"));
		}
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
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"appKey","refundMoney"}, map);
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
							resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
						}else{
							//发起退款操作
							Map<String, Object> m = payv2PayOrderRefundService.payRefund(map,pbca.getId(),pbca.getAppSecret());
							if(!m.containsKey("order_num")){
								logger.debug("交易不存在");
								resultMap = ReMessage.resultBack(ParameterEunm.TRANSACTION_NOT_EXIST,null);
							}else{
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
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
						if(!ObjectUtil.checkObject(new String[] {"refundNum"}, map)){
							logger.debug("参数不能为空,或者有误");
							resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
						}else{
							Map<String,Object> m =payv2PayOrderRefundService.queryRefund(map, pbca);
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
			logger.debug("服务器异常,请稍后再试");
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		
		return resultMap;
	}
	
	/**
	 * 支付退款（点游）
	 * @param request
	 * @param response
	 * @throws AlipayApiException 
	 * @throws Exception 
	 */
	@RequestMapping(value="/dyPayRefund")
	@ResponseBody
	public Map<String, Object> dyPayRefund(@RequestParam Map<String, Object> map) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"appKey","refundMoney"}, map);
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
							resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
						}else{
							Map<String, Object> m = payv2PayOrderRefundService.dyPayRefund(map,pbca);
							if(!m.containsKey("order_num")){
								logger.debug("交易不存在");
								resultMap = ReMessage.resultBack(ParameterEunm.TRANSACTION_NOT_EXIST,null);
							}else{
								resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, m);
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
			logger.debug("服务器异常,请稍后再试");
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		
		return resultMap;
	}
	
	/**
	 * 查询退款(点游)
	 * @param request
	 * @param response
	 * @throws AlipayApiException 
	 * @throws Exception 
	 */
	@RequestMapping(value="/dyQueryRefund")
	@ResponseBody
	public Map<String, Object> dyQueryRefund(@RequestParam Map<String, Object> map) throws Exception{
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
						if(!ObjectUtil.checkObject(new String[] {"refundNum"}, map)){
							logger.debug("参数不能为空,或者有误");
							resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
						}else{
							Map<String,Object> m =payv2PayOrderRefundService.dyQueryRefund(map, pbca);
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
			logger.debug("服务器异常,请稍后再试");
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		
		return resultMap;
	}
	
	/**
	 * 商户app退款
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/payRefundApp")
	@ResponseBody
	public Map<String, Object> payRefundApp(@RequestParam Map<String, Object> map){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] {"appId","refundMoney","orderNum","refundType"}, map);
		if(isNotNull){
			Long appId = Long.valueOf(map.get("appId").toString());
			try {
				map.remove("appId");
				resultMap = payv2PayOrderRefundService.payRefund(map, appId, null);
				if(!resultMap.containsKey("order_num")){
					logger.debug("交易不存在");
					resultMap = ReMessage.resultBack(ParameterEunm.TRANSACTION_NOT_EXIST,null);
				}else if(!resultMap.containsKey("refund_num")){
					logger.debug("退款失败");
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE,null);
				}else{
					resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.debug("服务器异常,请稍后再试");
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
			}
		}else{
			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE,null);
		}
		return resultMap;
	}
}
