package com.pay.business.util.hfbpay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.ReadProChange;
import com.core.teamwork.base.util.properties.PropertiesUtil;

/**
 * 汇付宝支付
 * @author Administrator
 *
 */
public class HfbPay {
	
	public static String version = "1";	//当前接口版本号 1  
	public static String is_phone = "1";//是否使用手机端微信支付，1=是，微信扫码支付不用传本参数(支付宝支付=1时，支付宝wap支付，不传为扫码支付。不参与签名)
	public static String is_frame = "0";//1（默认值）=使用微信公众号支付，0=使用wap微信支付
	public static String wx_pay_type ="30";//微信支付30，支付宝22（数据类型：int）
	public static String ali_pay_type ="22";//微信支付30，支付宝22（数据类型：int）
	//public static String agent_id ="1664502";//商户编号 如1234567（汇付宝商户编号：七位整数数字）
	//public static String agent_id ="2105434";//正式的
	//public static String agent_id ="2105871";////趣讯
	
	public static String notify_url = PropertiesUtil.getProperty("rate", "hfb_alipay_return_url");//异步通知地址，用户支付成功后会将支付结果发送到该页面，商户做后续的业务处理（该地址要外网可访问）
	public static String return_url = PropertiesUtil.getProperty("rate", "hfb_return_url");
	public static String remark = "jinfu";//商户自定义 原样返回,长度最长50字符，可以为空。（不参加签名）
	public static String is_test ="1";//是否测试，1=测试，非测试请不用传本参数(如传了此参数，则必须参加MD5的验证)
	
	//public static String key = "4B05A95416DB4184ACEE4313";//商户密钥
	
	//public static String key = "4CAE7B1B5ACD4FC8BDCB7D61";//正式的
	
	//public static String key = "086670960AE24441AB9018A3";//趣讯
	
	/**
	 * 微信支付
	 * @param agent_bill_id	订单号
	 * @param pay_amt	订单金额
	 * @param user_ip	ip地址
	 * @param goods_name	订单名称
	 * @param agent_bill_time	订单支付时间
	 * @return
	 */
	public static String webPay(String agent_bill_id,double pay_amt,String user_ip,String goods_name
			,Date agent_bill_time,Integer pay_type,String agent_id,String key){
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("version", version);
		paramMap.put("agent_id", agent_id);
		paramMap.put("agent_bill_id", agent_bill_id);
		paramMap.put("agent_bill_time", dateToString(agent_bill_time));
		if(pay_type==1){
			//支付宝
			paramMap.put("pay_type", ali_pay_type);
			System.out.println("支付宝");
		}else{
			//微信
			paramMap.put("pay_type", wx_pay_type);
			paramMap.put("meta_option", URLEncoder.encode("{\"s\":\"Android\",\"n\":\"dskfhgi\",\"id\":\"dfsgksdf\"}"));
			System.out.println("微信");
		}
		paramMap.put("pay_amt", pay_amt+"");
		paramMap.put("notify_url", notify_url);
		paramMap.put("return_url", return_url);
		paramMap.put("user_ip", user_ip);
		paramMap.put("goods_name", goods_name);
		paramMap.put("is_phone", is_phone);
		paramMap.put("is_frame", is_frame);
		paramMap.put("remark", remark);
		String sign=WeiXinHelper.signMd5(key, paramMap);
		paramMap.put("sign", sign);
		String url = WeiXinHelper.GatewaySubmitUrl(sign, paramMap);
		return url;
	}
	
	/**
	 * sdk支付(支付宝和微信)
	 * @param agent_bill_id	订单号
	 * @param pay_amt	订单金额
	 * @param user_ip	ip地址
	 * @param goods_name	订单名称
	 * @param agent_bill_time	订单支付时间
	 * @return
	 * @throws Exception 
	 */
	public static String sdkPay(String agent_bill_id,double pay_amt,String user_ip,String goods_name
			,Date agent_bill_time,Integer pay_type,String agent_id,String key) throws Exception{
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("version", version);
		paramMap.put("agent_id", agent_id);
		paramMap.put("agent_bill_id", agent_bill_id);
		paramMap.put("agent_bill_time", dateToString(agent_bill_time));
		if(pay_type==1){
			//支付宝
			paramMap.put("pay_type", ali_pay_type);
			System.out.println("支付宝");
		}else{
			//微信
			paramMap.put("pay_type", wx_pay_type);
			paramMap.put("meta_option", URLEncoder.encode("[{\"s\":\"Android\",\"n\":\"dfgsdfg\",\"id\":\"com.tencent.sgame\"},{\"s\":\"IOS\",\"n\":\"dsfgsd\",\"id\":\"com.tencent.tmgp.sgame\"}]"));
			System.out.println("微信");
		}
		paramMap.put("pay_amt", pay_amt+"");
		paramMap.put("notify_url", notify_url);
		paramMap.put("return_url", return_url);
		paramMap.put("user_ip", user_ip);
		paramMap.put("goods_name", goods_name);
		paramMap.put("remark", remark);
		//paramMap.put("is_test", is_test);
		String sign=WeiXinHelper.sdkSignMd5(key, paramMap);
		paramMap.put("sign", sign);
		String paramStr = WeiXinDataHelper.GetQueryString(paramMap);
		String url = "https://pay.heepay.com/Phone/SDK/PayInit.aspx";
		String result  = WeiXinDataHelper.sdkGetPostUrl(paramStr, url, "post");
		String token = WeiXinHelper.toMap(result);
		if(token.equals("")){
			return "";
		}
		return token+","+agent_id+","+agent_bill_id+","+paramMap.get("pay_type");
	}
	
	/**
	 * 查询订单
	 * @param agent_bill_id	订单号
	 * @param pay_amt	订单金额
	 * @param user_ip	ip地址
	 * @param goods_name	订单名称
	 * @param agent_bill_time	订单支付时间
	 * @return
	 */
	public static Map<String,String> query(String agent_bill_id,String agent_bill_time
			,String agent_id,String key){
		
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("version", version);
		paramMap.put("agent_id", agent_id);
		paramMap.put("agent_bill_id", agent_bill_id);
		paramMap.put("agent_bill_time", agent_bill_time);
		paramMap.put("remark", remark);
		paramMap.put("return_mode", "1");
		//paramMap.put("is_test", is_test);
		String sign=WeiXinHelper.qsignMd5(key, paramMap);
		paramMap.put("sign", sign);
		String paramStr = WeiXinDataHelper.GetQueryString(paramMap);
		String result  = WeiXinDataHelper.GetPostUrl(paramStr, "https://query.heepay.com/Payment/Query.aspx", "post");
		
		paramMap = WeiXinHelper.strToMap(result);
		return paramMap;
	}
	
	/**
	 * 年月日时分秒
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}
	
	public static void main(String[] args) throws Exception {
		//public static String agent_id ="2105434";//正式的
		//public static String agent_id ="2105871";////趣讯
		//public static String key = "4CAE7B1B5ACD4FC8BDCB7D61";//正式的
		//public static String key = "086670960AE24441AB9018A3";//趣讯
		
		//wxPay("DD5649747585865330", 0.01, "192_168_1_172", "asdfa", new Date());
		//webPay("DD569899875665530", 0.01, "192_168_1_172", "asdfa", new Date(),2);
		//aliPay1("DD56497475889223530", 0.01, "192_168_1_172", "asdfa", new Date());
		//query("DD20170616153141097123554", "20170616153141");
		//sdkPay("DD5697841315646132530", 0.01, "192_168_1_172", "asdfa", new Date(),1);
	}
}
