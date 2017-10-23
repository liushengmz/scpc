package pay_website;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.core.teamwork.base.util.http.HttpUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;

public class Test {
	/**
	* @Title: main 
	* @Description: 支付宝扫码支付测试类
	* @param @param args
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @throws
	*/
	public static void main(String[] args) throws Exception {
//		aliScanpay();
//		htbweixinpay();
//		payRefund();
//		wftweixinpay();、
//		selectOrder();
//		htbweixinpay();
//		payRefund();
//		wftweixinpay();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<>();
		long num=new Date().getTime();
		System.out.println("======订单号为："+num+"==========");
		map.put("bussOrderNum",String.valueOf(num));
		map.put("payMoney","0.01");
		map.put("appKey", "6413f866b558d3e2b6ccf4f0d865f9df");//270461df13a412f373ae6c2771ccd926
		map.put("notifyUrl", "http://aijinfupay.tunnel.echomod.cn/aiJinFuPay/companyCallBack.do");
		map.put("orderName", "公众号支付测试");
		map.put("returnUrl", "http://www.baidu.com");
		map.put("goodsNote", "公众号支付测试");
		map.put("remark", "1111111");
		map.put("goods_num","100");
		String s = PaySignUtil.getSign(map, "u4smNesRMrDAIU62HXNy1eoeP9uD8yaUKCcd103j");// 外网密钥
		//be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b
		String paramStr = PaySignUtil.getParamStr(map) + "&sign=" + s;
		// 参数进行URLEncoder转码
		paramStr = URLEncoder.encode(paramStr);
		String lmUrl = "https://testpayapi.aijinfu.cn/page/pay/publicPay.html?"+paramStr;
		System.out.println(lmUrl);
		// http://wx.api-export.com/api/waptoweixin?key=授权key&f=json&url=http%3a%2f%2fwww.baidu.com
		String result = HttpUtil.HttpGet("http://wx.api-export.com/api/waptoweixin?key=bc8231705e3965376fc063d4959a9dde&f=json&url="+lmUrl, null);
		
		if(result.equals("")){
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE,null);
		}
		JSONObject jsonObject = JSONObject.parseObject(result);
		String lmResult = jsonObject.getString("ticket_url");
		
		System.out.println(lmResult);
	}
	// 支付宝扫码支付
	public static void aliScanpay() {
		try {
			// "bussOrderNum","orderName","payMoney","appKey","returnUrl","notifyUrl","sign"
			Map<String, Object> map = new HashMap<>();
			long num=new Date().getTime();
			System.out.println("======订单号为："+num+"==========");
			map.put("bussOrderNum",String.valueOf(num));
			map.put("payMoney", "0.01");
			map.put("appKey", "270461df13a412f373ae6c2771ccd926");
			map.put("notifyUrl", "http://aijinfupay.tunnel.echomod.cn/aiJinFuPay/companyCallBack.do");
			map.put("orderName", "测试支付宝扫码支付");
			map.put("returnUrl", "");
//			map.put("channel", 1);
			String s = PaySignUtil.getSign(map, "be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b");// 外网密钥
			String paramStr = PaySignUtil.getParamStr(map) + "&sign=" + s;
			// 参数进行URLEncoder转码
//			paramStr = URLEncoder.encode(paramStr);
//			System.out.println(paramStr);
			map.clear();
			map.put("paramStr", paramStr);
			// 接口请求
			String result = HttpUtil.httpPost("http://aijinfupay.tunnel.echomod.cn/aiJinFuPay/aliScanPay.do", map);
			System.out.println(result);
			JSONObject json = JSONObject.parseObject(result);
			System.out.println("返回结果为========>"+json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 支付宝扫码退款
	public static void payRefund() {
		try {
			Map<String, Object> map = new HashMap<>();
			// 支付签名
			map.put("bussOrderNum", "267689785666504");
			map.put("refundMoney", "0.02");
			map.put("refundType", "Y");
			map.put("appKey", "270461df13a412f373ae6c2771ccd926");
			String s = PaySignUtil.getSign(map, "be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b");
			map.put("sign", s);
			// 接口请求
			String result = HttpUtil.httpPost("http://aijinfupay.tunnel.echomod.cn/payOrderRefund/payRefund.do", map);
			System.out.println(result);
			JSONObject json = JSONObject.parseObject(result);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* @Title: htbweixinpay 
	* @Description:汇付宝-微信公众号支付
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	*/
	public static void htbweixinpay() {
		try {
//			"bussOrderNum","orderName","payMoney","notifyUrl","returnUrl","goodsNote","remark","sign";
			Map<String, Object> map = new HashMap<>();
			long num=new Date().getTime();
			System.out.println("======订单号为："+num+"==========");
			map.put("bussOrderNum",String.valueOf(num));
			map.put("payMoney","1");
			map.put("appKey", "6413f866b558d3e2b6ccf4f0d865f9df");//270461df13a412f373ae6c2771ccd926
			map.put("notifyUrl", "http://aijinfupay.tunnel.echomod.cn/aiJinFuPay/companyCallBack.do");
			map.put("orderName", "GZHPAY");
			map.put("returnUrl", "http://aijinfupay.tunnel.echomod.cn/success.html");
			map.put("goodsNote", "GZHPAY");
			map.put("remark", "GZH");
			map.put("goods_num","100");
			String s = PaySignUtil.getSign(map, "u4smNesRMrDAIU62HXNy1eoeP9uD8yaUKCcd103j");// 外网密钥
			//be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b
			String paramStr = PaySignUtil.getParamStr(map) + "&sign=" + s;
			// 参数进行URLEncoder转码
//			paramStr = URLEncoder.encode(paramStr);
			System.out.println("https://testpayapi.aijinfu.cn/page/pay/publicPay.html?"+paramStr);
			map.clear();
			map.put("paramStr", paramStr);
			// 接口请求
			//String result = HttpUtil.httpPost("http://aijinfupay.tunnel.echomod.cn/GateWay/hfbWxGzhPay.do", map);
			//System.out.println(result);
			//JSONObject json = JSONObject.parseObject(result);
			//System.out.println("返回结果为========>"+json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* @Title: wftweixinpay 
	* @Description: 威富通：微信公众号支付
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	*/
	public static void wftweixinpay() {
		try {
//			"bussOrderNum", "orderName", "payMoney", "notifyUrl","sign";
			Map<String, Object> map = new HashMap<>();
			// 支付签名
			long num=new Date().getTime();
			//System.out.println("======订单号为："+num+"==========");
			map.put("bussOrderNum",String.valueOf(num));
			map.put("payMoney",0.01);
			map.put("appKey", "270461df13a412f373ae6c2771ccd926");
			map.put("notifyUrl", "http://aijinfupay.tunnel.echomod.cn/aiJinFuPay/companyCallBack.do");
			map.put("orderName", "威富通公众号支付测试");
			String s = PaySignUtil.getSign(map, "be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b");// 外网密钥
			String paramStr = PaySignUtil.getParamStr(map) + "&sign=" + s;
			// 参数进行URLEncoder转码
//			paramStr = URLEncoder.encode(paramStr);
//			System.out.println(paramStr);
			map.clear();
			map.put("paramStr", paramStr);
			// 接口请求
			String result = HttpUtil.httpPost("http://aijinfupay.tunnel.echomod.cn/GateWay/swiftWechatGzhPay.do", map);
			//System.out.println(result);
			JSONObject json = JSONObject.parseObject(result);
			//System.out.println("返回结果为========>"+json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* @Title: selectOrder 
	* @Description: 订单查询
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public static void selectOrder(){
		try{
			Map<String, Object> map = new HashMap<>();
			// 支付签名
			map.put("bussOrderNum", "1499915365087");
			map.put("appKey", "270461df13a412f373ae6c2771ccd926");
			String s = PaySignUtil.getSign(map, "be29c66b2d0b166c90d7a346209259149470faf76e53bf52b39d1a98a8d9250b");
			String paramStr = PaySignUtil.getParamStr(map) + "&sign=" + s;
			map.clear();
			map.put("paramStr", paramStr);
			// 接口请求
			String result = HttpUtil.httpPost("http://aijinfupay.tunnel.echomod.cn/pay/queryOrder.do", map);
			System.out.println(result);
			JSONObject json = JSONObject.parseObject(result);
			System.out.println(json);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
