package com.pay.manger.controller.payv2;

import java.net.URLEncoder;
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
import com.core.teamwork.base.util.http.HttpUtil;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.util.OrderUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.hfbpay.HfbPay;

@Controller
@RequestMapping("/demo/*")
public class DemoController {
	//Logger logger = Logger.getLogger(this.getClass());
	private static final Log logger = LogFactory.getLog(Payv2PayController.class);
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	
	
	@ResponseBody
	@RequestMapping(value = "/demoPay")
	public Map<String,Object> demoPay(HttpServletRequest request,HttpServletResponse response
			,@RequestParam Map<String, Object> map) throws Exception{
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean con = ObjectUtil.checkObject(new String[] { "appKey"}, map);
		if(con){
			try {
				//检查appKey是否有效!先查询app审核是否通过，再查询app所属商户状态是否通过
				Payv2BussCompanyApp pbca = payv2BussCompanyAppService.checkApp(map.get("appKey").toString());
				if(pbca==null){
					logger.info("appKey无效");
					resultMap = ReMessage.resultBack(ParameterEunm.APP_KEY_INVALID,null);
					return resultMap;
				}
				//String keyValue="be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b";//内网密钥
				//String keyValue="5abc744cf47f1a40595331e1d6f616e799bd30aa2344bc08db9e77b25f1d4e04";//外网密钥
				String keyValue = pbca.getAppSecret();
				String sign = PaySignUtil.getSign(map, keyValue);
				String paramStr = PaySignUtil.getParamStr(map)+"&sign="+sign;
				resultMap.put("paramStr", paramStr);
				resultMap.put("paramStrEncode", URLEncoder.encode(paramStr));
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
			} catch (Exception e) {
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
			}
		}else{
			resultMap = ReMessage.resultBack(ParameterEunm.PARAM_ERROR_MONEY,null);
		}
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/test")
	public Map<String,String> test(HttpServletRequest request,HttpServletResponse response
			,@RequestParam Map<String, String> map) throws Exception{
		
		map.put("aa", "aaa");
		map.put("bb", "bbb");
		map.put("cc", "ccc");
		map.put("dd", "ddd");
		RedisDBUtil.redisDao.hmset("sss", map);
		map = null;
		map = RedisDBUtil.redisDao.getStringMapAll("sss");
		RedisDBUtil.redisDao.delete("sss");
		
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value = "/test3")
	public Map<String,Object> test3(HttpServletRequest request,HttpServletResponse response
			,@RequestParam Map<String, Object> map) throws Exception{
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String result = HfbPay.webPay(OrderUtil.getOrderNum(), 0.01, "192_168_1_172"
				, URLEncoder.encode("支付测试"), new Date(),Integer.valueOf(map.get("payType").toString())
				,"","");
		
		resultMap.put("result", result);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/test6")
	public Map<String,Object> test6(HttpServletRequest request,HttpServletResponse response
			,@RequestParam Map<String, Object> map) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String result = HfbPay.sdkPay(OrderUtil.getOrderNum(), 0.01, "192_168_1_172"
				, URLEncoder.encode("支付测试"), new Date(),Integer.valueOf(map.get("payType").toString())
				,"","");
		
		if(result.equals("")){
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
			return resultMap;
		}
		resultMap.put("result", result);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		
		return resultMap;
	}
	
	@RequestMapping("/returnUrl")
	public ModelAndView returnUrl(@RequestParam Map<String, Object> map, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("page/pay/test");
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "/testPublicPay")
	public Map<String,Object> testPublicPay(HttpServletRequest request,HttpServletResponse response
			,@RequestParam Map<String, Object> pMap) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<>();
		long num=new Date().getTime();
//		System.out.println("======订单号为："+num+"==========");
//		map.put("bussOrderNum",String.valueOf(num));
//		map.put("payMoney","0.01");
//		map.put("appKey", "6413f866b558d3e2b6ccf4f0d865f9df");//270461df13a412f373ae6c2771ccd926
//		map.put("notifyUrl", "http://aijinfupay.tunnel.echomod.cn/aiJinFuPay/companyCallBack.do");
//		map.put("orderName", "公众号支付测试");
//		map.put("returnUrl", "http://www.baidu.com");
//		map.put("goodsNote", "公众号支付测试");
//		map.put("remark", "1111111");
//		map.put("goods_num","100");
//		String s = PaySignUtil.getSign(map, "u4smNesRMrDAIU62HXNy1eoeP9uD8yaUKCcd103j");// 外网密钥
		//be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b
//		String paramStr = PaySignUtil.getParamStr(map) + "&sign=" + s;
		// 参数进行URLEncoder转码
//		paramStr = URLEncoder.encode(paramStr);
		
		String lmUrl = "https://testpayapi.aijinfu.cn/page/pay/publicPay.html?"+pMap.get("paramStr");
		// http://wx.api-export.com/api/waptoweixin?key=授权key&f=json&url=http%3a%2f%2fwww.baidu.com
		String result = HttpUtil.HttpGet("http://wx.api-export.com/api/waptoweixin?key=bc8231705e3965376fc063d4959a9dde&f=json&url="+lmUrl, null);
		
		if(result.equals("")){
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
			return resultMap;
		}
		JSONObject jsonObject = JSONObject.parseObject(result);
		String lmResult = jsonObject.getString("ticket_url");
		
		System.out.println(lmResult);
		resultMap.put("result", lmResult);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		
		return resultMap;
	}
}
