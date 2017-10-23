package com.pay.manger.controller.payv2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.util.PayFinalUtil;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.wftpay.weChatSubscrPay.utils.XmlUtils;

@Controller
@RequestMapping("/returnUrl/*")
public class ReturnUrlController {
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	@Autowired
	private Payv2BussCompanyAppService Payv2BussCompanyAppService;
	
	@RequestMapping("/alipay.do")
	public ModelAndView alipay(@RequestParam Map<String, String> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("pay/return");
		/*String aliStr = "{total_amount=0.02, "
				+ "timestamp=2017-07-14 14:42:41, "
				+ "sign=Emruyqvwc9XHm4N55GITG05EFKDYu+NIivy+JGfKOIUt5EmBpkYSu8EPwJWLLs3j86Zh5VEeLwyMIno8XgBVm0wPgTIBpYXERbWg+HFNwNrvsaT96j82ilt9+vzWZAiblDKNpSjHnx+4wb50//VjTuiSeyXA+yNQ6+3evlI7Ifw=, "
				+ "trade_no=2017071421001004440229034319, "
				+ "sign_type=RSA, auth_app_id=2017021005611761, "
				+ "charset=utf-8, "
				+ "seller_id=2088621176118104, "
				+ "method=alipay.trade.wap.pay.return, "
				+ "app_id=2017021005611761, "
				+ "out_trade_no=DD2017071414421982067525, "
				+ "version=1.0}";*/
		try {
			Map<String,Object> paramMap = new HashMap<>();
			String orderNum = map.get("out_trade_no");
			Payv2PayOrder order = payv2PayOrderService.getOrderInfo(orderNum);
			if(order!=null){
				paramMap.put("id", order.getAppId());
				Payv2BussCompanyApp pbca = Payv2BussCompanyAppService.detail(paramMap);
				paramMap.clear();
				paramMap.put("result_code", PayFinalUtil.SUSSESS_CODE); // 成功
				paramMap.put("pay_money", map.get("total_amount"));
				paramMap.put("pay_discount_money", map.get("total_amount"));
				paramMap.put("pay_time", DateStr(order.getPayTime()==null?order.getCreateTime():order.getPayTime()));
				paramMap.put("order_num", map.get("out_trade_no"));
				paramMap.put("buss_order_num", order.getMerchantOrderNum());
				// 参数签名
				String sign = PaySignUtil.getSign(paramMap, pbca.getAppSecret());
				paramMap.put("sign", sign);
				
				String returnUrl = order.getReturnUrl();
				if(returnUrl!=null&&returnUrl.length()>5){
					if(returnUrl.substring(0,5).equals("http:")
							||returnUrl.substring(0,6).equals("https:")){
						returnUrl = "redirect:"+returnUrl;
					}else{
						returnUrl = "redirect://"+returnUrl;
					}
					returnUrl = returnUrl+"?"+PaySignUtil.getParamStr(paramMap);
					mv.setViewName(returnUrl);
				}
				paramMap.put("returnUrl", returnUrl);
			}
			mv.addObject("map", paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			mv = new ModelAndView("pay/return");
		}
		
		return mv;
	}
	
	@RequestMapping("/hfb.do")
	public ModelAndView hfb(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		//String zf="{pay_amt=0.01, sign=4b12115d722aeaf5701052bdb069b425, result=1, jnet_bill_no=H1707143553305AL, pay_type=30, remark=jinfu, agent_bill_id=DD2017071410222107699496, fbtn=, agent_id=2105871, pay_message=}";
		ModelAndView mv = new ModelAndView("pay/return");
		try {
			Map<String,Object> paramMap = new HashMap<>();
			String orderNum = map.get("agent_bill_id").toString();
			Payv2PayOrder order = payv2PayOrderService.getOrderInfo(orderNum);
			if(order!=null){
				paramMap.put("id", order.getAppId());
				Payv2BussCompanyApp pbca = Payv2BussCompanyAppService.detail(paramMap);
				paramMap.clear();
				if("1".equals(map.get("result"))){
					paramMap.put("result_code", PayFinalUtil.SUSSESS_CODE); // 成功
					paramMap.put("pay_money", order.getPayMoney());
					paramMap.put("pay_discount_money", map.get("pay_amt"));
					paramMap.put("pay_time", DateStr(order.getPayTime()==null?order.getCreateTime():order.getPayTime()));
				}else{
					paramMap.put("result_code", PayFinalUtil.CANCEL_CODE); // 失败
					paramMap.put("pay_money", order.getPayMoney());
					paramMap.put("pay_discount_money", "0.00");
					paramMap.put("pay_time", "");
				}
				paramMap.put("order_num", map.get("agent_bill_id"));
				paramMap.put("buss_order_num", order.getMerchantOrderNum());
				// 参数签名
				String sign = PaySignUtil.getSign(paramMap, pbca.getAppSecret());
				paramMap.put("sign", sign);
				
				String returnUrl = order.getReturnUrl();
				if("1".equals(map.get("result"))){
					paramMap.put("payPrice", map.get("pay_amt"));
					if(returnUrl!=null&&returnUrl.length()>5){
						if(returnUrl.substring(0,5).equals("http:")
								||returnUrl.substring(0,6).equals("https:")){
							returnUrl = "redirect:"+returnUrl;
						}else{
							returnUrl = "redirect://"+returnUrl;
						}
						returnUrl = returnUrl+"?"+PaySignUtil.getParamStr(paramMap);
						mv.setViewName(returnUrl);
					}
				}else{
					if(returnUrl!=null&&returnUrl.length()>5){
						if(!returnUrl.substring(0,5).equals("http:")
								&&!returnUrl.substring(0,6).equals("https:")){
							returnUrl = "http://"+returnUrl;
						}
					}
					paramMap.put("payPrice", order.getPayMoney());
				}
				if(returnUrl!=null&&!"".equals(returnUrl)){
					System.out.println(returnUrl);
					returnUrl = returnUrl+"?"+PaySignUtil.getParamStr(paramMap);
					paramMap.put("returnUrl", returnUrl);
				}
			}
			mv.addObject("map", paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			mv = new ModelAndView("pay/return");
		}
		return mv;
	}
	
	private String DateStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
	}
	/**
	* @Title: wft 
	* @Description: 威富通：微信公众号同步通知URL
	* @param @param map
	* @param @param request
	* @param @return    设定文件 
	* @return ModelAndView    返回类型 
	* @throws
	*/
	@RequestMapping("/wft.do")
	public ModelAndView wft(@RequestParam Map<String, Object> map,HttpServletRequest req, HttpServletResponse resp){
		/**
		 * 因：威富通不给返回任何东西··故没有什么用这个接口：暂保留
		 */
		ModelAndView mv = new ModelAndView("pay/return");
		map.put("result_code", PayFinalUtil.SUSSESS_CODE); // 成功
		mv.addObject("map", map);
		System.out.println(map);
		System.out.println("=========》威富通微信公众号支付结果回调开始");
		String resString = XmlUtils.parseRequst(req);
		System.out.println("========》回调通知内容：" + resString);
		return mv;
		
	}
}