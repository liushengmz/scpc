package com.pay.business.util.hfbpay.WeChatSubscrip.pay;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.core.teamwork.base.util.date.DateUtil;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.util.hfbpay.WeiXinDataHelper;
import com.pay.business.util.hfbpay.WeChatSubscrip.commond.Md5Tools;
import com.pay.business.util.hfbpay.WeChatSubscrip.commond.WeiXinHelper;
import com.pay.business.util.hfbpay.WeChatSubscrip.model.WeiXinPayModel;

/**
* @Title: WeChatScanPay.java 
* @Package com.pay.business.util.hfbpay.WeChatSubscrip.pay 
* @Description:微信公众号支付
* @author ZHOULIBO   
* @date 2017年7月3日 下午5:35:45 
* @version V1.0
*/
public class HfbWeChatPay {
	 static Logger  logger = Logger.getLogger(HfbWeChatPay.class);

	/**
	 * pay 
	 * 获取汇付宝微信公众号支付的相关参数 
	 * @param agent_bill_id 		商户系统内部的定单号（要保证唯一）。长度最长50字符 Y
	 * @param agent_bill_time 		提交单据的时间yyyyMMddHHmmss 如：20100225102000该参数共计14位，当时不满14位时，在后面加0补足14位 Y
	 * @param pay_amt 				订单总金额 不可为空，取值范围（0.1到3000.00），单位：元， Y
	 * @param user_ip 				IP地址
	 * @param goods_num 			产品数量,长度最长20字符（不参加签名） N
	 * @param goods_name 			商品名称, 长度最长50字符，不能为空（不参加签名） Y
	 * @param goods_note 			支付说明, 长度50字符（不参加签名）N
	 * @param remark 				商户自定义 原样返回,长度最长50字符，可以为空 Y
	 * @param agentId 				商户号
	 * @param key 					商户秘钥
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> pay(String agent_bill_id,String agent_bill_time,String pay_amt,String user_ip,String goods_num,String goods_name,String goods_note,String remark,String agentId,String key) {
		logger.info("=======》调起汇付宝-微信公众号支付开始");
		Map<String, String> returnMap = new HashMap<String, String>();
		try {
			// 将数据初始化WeiXinPayModel
			WeiXinPayModel model = new WeiXinPayModel();
			model.set_agent_bill_id(agent_bill_id);
			model.set_agent_bill_time(agent_bill_time);
			model.set_agent_id(agentId);
			model.set_goods_name(goods_name);
			model.set_goods_note(goods_note);
			model.set_goods_num(goods_num);
			model.set_is_frame(HfbWxGzhConfig.is_frame);
			model.set_notify_url(HfbWxGzhConfig.notify_url);
			model.set_pay_amt(pay_amt);
			model.set_pay_type(HfbWxGzhConfig.pay_type);
			model.set_remark(remark);
			model.set_return_url(HfbWxGzhConfig.RETURN_URL);
			model.set_user_ip(user_ip);
			model.set_is_frame(HfbWxGzhConfig.is_frame);
			model.set_is_phone(HfbWxGzhConfig.is_phone);
			model.set_version(HfbWxGzhConfig.version);
//			model.set_meta_option(_meta_option);
			String sign = WeiXinHelper.signMd5(key, model);
			logger.info("调起汇付宝-微信公众号支付加密："+sign);
			// 获取提交地址
			String addrss = WeiXinHelper.GatewaySubmitUrl(sign, model);
			returnMap.put("address", addrss);
			// 得到MD5签名数据串
//			String signStr = WeiXinHelper.GetsignString(key, model);
//			returnMap.put("sign", signStr);
			
		} catch (Exception e) {
			logger.error("=======》调起汇付宝-微信公众号支付失败原因："+e);
			e.printStackTrace();
		}
		return returnMap;
	}
	/**
	 * 
	 * hfbWxGzhSign 
	 * 汇付宝微信公众号支付：回调验签
	 * @param map
	 * @param payv2PayOrder 
	 * @return    设定文件 
	 * boolean    返回类型
	 */
	public static boolean hfbWxGzhSign(Map<String, String> map,Payv2PayOrder payv2PayOrder) {
		// md5(result=1&agent_id=1234567&jnet_bill_no=B20100225132210&agent_bill_id=20100225132210&pay_type=10&pay_amt=15.33&remark=test_remark&key=CC08C5E3E69F4E6B85F1DC0B)
		// 验签
		StringBuilder _sbString = new StringBuilder();
		_sbString.append("result=" + map.get("result"))
				.append("&agent_id=" + map.get("agent_id"))
				.append("&jnet_bill_no=" + map.get("jnet_bill_no"))
				.append("&agent_bill_id=" + map.get("agent_bill_id"))
				.append("&pay_type=" + map.get("pay_type"))
				.append("&pay_amt=" + map.get("pay_amt"))
				.append("&remark=" + map.get("remark"))
				.append("&key=" +payv2PayOrder.getRateKey2());
		logger.info("汇付宝微信公众号支付验签参数拼接为：=========》" + _sbString.toString());
		String s = Md5Tools.MD5(_sbString.toString()).toLowerCase();
		logger.info("汇付宝微信公众号支付回调加密签名：===》"+s);
		String sign = String.valueOf(map.get("sign"));
		logger.info("汇付宝微信公众号支付加密签名：===》"+sign);
		if (s.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * selectHfbWeChatGzhPayOrder 
	 * 汇付宝：微信公众号支付：订单查询接口 
	 * @param agent_id 商户号
	 * @param agent_bill_id 商户订单号
	 * @param key 商户key
	 * @param agent_bill_time 查询时间
	 * @param remark 自定参数，原样返回
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> selectHfbWeChatGzhPayOrder(String agent_id,String agent_bill_id,String key,String agent_bill_time,String remark){
		Map<String, String> returnMap=new HashMap<String, String>();
		//参数加密：
		StringBuilder _sbString = new StringBuilder();
		_sbString.append("version="+HfbWxGzhConfig.version)
		.append("&agent_id="+agent_id)
		.append("&agent_bill_id="+agent_bill_id)
		.append("&agent_bill_time="+agent_bill_time)
		.append("&return_mode=1")
		.append("&key="+key);
		String sign=Md5Tools.MD5(_sbString.toString()).toLowerCase();
		logger.info("======》查询汇付宝订单加密值为："+sign);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("version",HfbWxGzhConfig.version);
		map.put("agent_id",agent_id);
		map.put("agent_bill_id",agent_bill_id);
		map.put("agent_bill_time",agent_bill_time);
		map.put("remark",remark);
		map.put("return_mode","1");
		map.put("sign", sign);
		String result = httpPost(HfbWxGzhConfig.select_order,map);
		Map<String, Object> infoMap = new HashMap<String, Object>();
		if (result != null) {
			String info[] = result.split("\\|");
			for (String string : info) {
				String info1[] = string.split("=");
				if (info1.length!=2) {
					infoMap.put(info1[0], "");
				} else {
					infoMap.put(info1[0], info1[1]);
				}
			}
		}
		logger.info("=======》查询接口返回信息为："+result);
		if (infoMap.containsKey("result")) {
			String results = String.valueOf(infoMap.get("result"));
			if (results.equals("1")) {
				//验签
				boolean bool = hfbWxGzhselectOrderSign(infoMap, key);
				if (bool) {
					// 成功
					returnMap.put("payStatus", "1");
					logger.info("=======》查询接口状态成功");
				} else {
					returnMap.put("code", "10001");
					returnMap.put("msg", "验签失败");
					logger.error("=======》查询接口状态失败：原因"+returnMap);
				}
			} else {
				// 失败
				returnMap.put("payStatus", "2");
				returnMap.put("msg","失败原因"+infoMap.get("pay_message"));
				logger.error("=======》查询接口状态失败：原因"+infoMap.get("pay_message"));
			}
			returnMap.put("code", "10000");
		} else {
			returnMap.put("code", "10001");
			returnMap.put("msg", "失败原因" + result);
			logger.error("=======》查询接口状态失败：原因"+result);
		}
		return returnMap;
	}
	/**
	 * hfbWxGzhselectOrderSign 
	 * 汇付宝：微信公众号支付：查询订单返回的参数进行验签
	 * @param map
	 * @param key
	 * @return    设定文件 
	 * boolean    返回类型
	 */
	public static boolean hfbWxGzhselectOrderSign(Map<String,Object> map,String key) {
		// sign=md5（商户编号=xxxxxxx|商户订单号=xxxxxxx|汇付宝定单号=xxxxx|支付类型=xx|支付结果=x|订单实际金额=xx.xx|交易结果描述=xxx|商家数据包=xxxx |商户密钥=CC08C5E3E69F4E6B85F1DC0B)
		// 验签
		StringBuilder _sbString = new StringBuilder();
		_sbString.append("agent_id=" + map.get("agent_id"))
				.append("|agent_bill_id=" + map.get("agent_bill_id"))
				.append("|jnet_bill_no=" + map.get("jnet_bill_no"))
				.append("|pay_type=" + map.get("pay_type"))
				.append("|result=" + map.get("result"))
				.append("|pay_amt=" + map.get("pay_amt"))
				.append("|pay_message=" + map.get("pay_message"))
				.append("|remark=" + map.get("remark"))
				.append("|key=" +key);
		logger.info("汇付宝微信公众号支付验签参数拼接为：=========》" + _sbString.toString());
		String s = Md5Tools.MD5(_sbString.toString()).toLowerCase();
		logger.info("汇付宝微信公众号支付订单查询加密签名：===》"+s);
		String sign = String.valueOf(map.get("sign"));
		logger.info("汇付宝微信公众号支付加密签名：===》"+sign);
		if (s.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * hfbWxGzhRefund 
	 * 汇付宝-微信公众号-退款申请
	 * @param agent_id 商户号
	 * @param agent_bill_id 商户订单号
	 * @param notify_url 通知的URL
	 * @param key 商户key
	 * @return    设定文件 
	 * Map<String,String>    返回类型
	 */
	public static Map<String, String> hfbWxGzhRefund(String agent_id, String refund_details, String notify_url, String key) {
		Map<String, String> returnMap = new HashMap<String, String>();
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("version", HfbWxGzhConfig.version);
			map.put("agent_id", agent_id);
			map.put("refund_details", refund_details);
			map.put("notify_url", notify_url);
			map.put("key", key);
			String sign=signMd5(null, map);
			logger.info("======》汇付宝微信公众号退款参数加密结果为：" +sign);
			map.put("sign", sign);
			map.remove("key");
			String postData=WeiXinDataHelper.GetSortQueryToLowerString(map);
			logger.info("======》汇付宝微信公众号退款提交参数为：" + postData);
			String result =WeiXinDataHelper.sdkGetPostUrl(postData, HfbWxGzhConfig.refund_order, "post");
			logger.info("======》汇付宝微信公众号退款返回结果为：" + result);
			boolean xml=XmlUtil.jiexiXml(result);
			logger.info("======》汇付宝微信公众号退款返回解析结果为：" + xml);
			if (xml) {
				//成功
				returnMap.put("code", "10000");
				logger.info("======>汇付宝-微信公众号-退款申请成功");
			} else {
				// 失败
				returnMap.put("code", "10001");
				returnMap.put("msg", "失败原因" + result);
				logger.error("======>汇付宝-微信公众号-退款申请失败原因为："+result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("======>汇付宝-微信公众号-退款申请失败原因为："+e);
			returnMap.put("code", "10001");
			returnMap.put("msg", "失败原因" + e);
		}
		return returnMap;
	}
	
	// MD5字符串拼接加密
	public static String signMd5(String key, Map<String, String> map) {
		//转换为小写
		String sginInfo =WeiXinDataHelper.GetSortQueryToLowerString(map);
		logger.info("======>汇付宝微信公众号支付参数加密拼接为：" + sginInfo);
		String sgin=Md5Tools.MD5(sginInfo).toLowerCase();
		return sgin;
	}
	private static Map<String, String> headers = new HashMap<String, String>();
	/**
	 * httpPost 
	 * httpPost请求
	 * @param url
	 * @param param
	 * @return    设定文件 
	 * String    返回类型
	 */
	public static String httpPost(String url, Map<String, Object> param) {
//		LOGGER.info("HTTP POST ==> URL:"+url+"\tPARAMS:"+param.toString());
		DefaultHttpClient httpclient = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		String result = "";
		StringBuffer suf = new StringBuffer();
		try {
			httpclient = new DefaultHttpClient();
			// 设置cookie的兼容性---考虑是否需要
			httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
					CookiePolicy.BROWSER_COMPATIBILITY);
			httpPost = new HttpPost(url);
			// 设置各种头信息
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPost.setHeader(entry.getKey(), entry.getValue());
			}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			// 传入各种参数
			if (null != param) {
				for (Entry<String, Object> set : param.entrySet()) {
					String key = set.getKey();
					String value = set.getValue() == null ? "" : set.getValue()
							.toString();
					nvps.add(new BasicNameValuePair(key, value));
					suf.append(" [" + key + "-" + value + "] ");
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			// 设置连接超时时间
			HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
					1000);
			// 设置读数据超时时间
			HttpConnectionParams.setSoTimeout(httpPost.getParams(), 5000);
			response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				return "";
			} else {
				entity = response.getEntity();
				if (null != entity) {
					Header encodingHeader = entity.getContentEncoding();
					if(encodingHeader!=null&&encodingHeader.getValue()!=null&&encodingHeader.getValue().indexOf("gzip")>-1){
						entity = new GzipDecompressingEntity(entity);
					}
					byte[] bytes = EntityUtils.toByteArray(entity);
					result = new String(bytes, "gb2312");
				} else {
				}
				return result;
			}
		} catch (Exception e) {
			logger.error("HTTP POST ERROR:",e);
			return "";
		} finally {
			if (null != httpclient) {
				httpclient.getConnectionManager().shutdown();
			}
		}
	}
	/**
	 * @Title: hfbStatement
	 * @Description: 汇付宝-对账接口
	 * @param  version 接口版本号         2
	 * @param  agent_id 商户ID
	 * @param  pay_type 支付类型        62=认证支付，20=网银支付，18=快捷支付，0=所有支付
	 * @param  begin_time 开始时间  格式为yyyyMMddHHmmss，比如 20140701110000
	 * @param  end_time 结束时间       格式为yyyyMMddHHmmss，比如 20140701110000
	 * @param  page_size 显示个数    最大500
	 * @param  page_index 页码
	 * @param  设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String[] hfbStatement(int version,String agent_id,String key,int pay_type,String begin_time,String end_time,int page_size,int page_index){
		Map<String, String> map = new HashMap<String, String>();
		map.put("version",String.valueOf(version));
		map.put("agent_id", agent_id);
		map.put("pay_type", String.valueOf(pay_type));
		map.put("begin_time", begin_time);
		map.put("end_time", end_time);
		map.put("page_size", String.valueOf(page_size));
		map.put("page_index",String.valueOf( page_index));
		map.put("key", key);
		String sign=signMd52(null, map);
		logger.info("======》汇付宝-对账接口参数加密结果为：" +sign);
		map.put("sign", sign);
		map.remove("key");
		String postData=WeiXinDataHelper.GetSortQueryToLowerString(map);
		logger.info("======》汇付宝-对账接口提交参数为：" + postData);
		String result2=sendGet(HfbWxGzhConfig.statement_url, postData);
		logger.info("======》汇付宝-对账接口返回结果为：" + result2);
		Map<String,Object> xmlMap=XmlUtil.jiexiXml1(result2);
		logger.info("======》汇付宝-对账接口返回解析结果为：" + xmlMap);
		if(xmlMap.containsKey("ret_code")){
			if(xmlMap.get("ret_code").equals("10000")){//成功
				//获取页码：
				int total_page=Integer.valueOf(xmlMap.get("total_page").toString());
				if(total_page!=0){
					if(total_page==1){//不用再去取数据：已为全部数据
						String textInfo=String.valueOf(xmlMap.get("textInfo"));
						String returnInfo[]=textInfo.split("\n");
						return returnInfo;
					}else{
						//否则利用页码需要再去获取全部数据
						map.put("page_index",String.valueOf(total_page));
						String postData2=WeiXinDataHelper.GetSortQueryToLowerString(map);
						logger.info("======》汇付宝-对账接口提交参数为：" + postData2);
						String result3=sendGet(HfbWxGzhConfig.statement_url, postData2);
						logger.info("======》汇付宝-对账接口返回结果为：" + result3);
						Map<String,Object> xmlMap1=XmlUtil.jiexiXml1(result3);
						if(xmlMap1.get("ret_code").equals("10000")){//成功
							String textInfo=(String) xmlMap1.get("textInfo");
							String returnInfo[]=textInfo.split("\n");
							return returnInfo;
						}else{
							logger.error("======》汇付宝-对账接口失败原因："+xmlMap1);
							return null;
						}
					}
				}else{
					logger.error("======》汇付宝-对账接口失败原因：对账单为空");
				}
			}else{//失败
				logger.error("======》汇付宝-对账接口失败原因："+xmlMap.get("ret_msg"));
			}
		}else{
			logger.error("======》汇付宝-对账接口失败原因:"+result2);
		}
		return null;
		
	}

	// MD5字符串拼接加密
	public static String signMd52(String key, Map<String, String> map) {
		// 转换为小写
		String sginInfo = WeiXinDataHelper.GetSortQueryToLowerString(map);
		logger.info("=======>sgin参数:"+sginInfo);
		String sgin = Md5Tools.MD5(sginInfo).toLowerCase();
		return sgin;
	}
	
	public static String sendGet(String url, String param) {
		String result = "";
		// BufferedReader in = null;
		DataInputStream in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
//			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义 BufferedReader输入流来读取URL的响应
			// in = new BufferedReader(new
			// InputStreamReader(connection.getInputStream(),"gb2312"));
			// 读取响应
			in = new DataInputStream(connection.getInputStream());
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int c;
			while ((c = in.read()) >= 0) {
				buffer.write(c);
			}
			buffer.close();
			byte[] bytes = buffer.toByteArray();
			result = new String(bytes, "GBK");
			//
			//
			// String line;
			// while ((line = in.readLine()) != null) {
			// result += line;
			// }
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流finally
		{
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	public static void main(String[] args) {
		//查询订单
//		selectHfbWeChatGzhPayOrder("2105434", "DD20170713110926180142073", "4CAE7B1B5ACD4FC8BDCB7D61", DateUtil.DateToString(new Date(), "yyyyMMddHHmmss"), "H");
		Date as = new Date(new Date().getTime()-24*60*60*1000*2);
		String weixinTime = DateUtil.DateToString(as,"yyyyMMdd");
		hfbStatement(2, "2105434","BFF7C2079F0546979EF1E7A9", 0, weixinTime+"000000", weixinTime+"235959",500,1);
//		String info=Md5Tools.MD5("agent_id=1664502&begin_time=20170719000000&end_time=20170719235959&key=bd845910e83044958f50d118&page_index=1&page_size=500&pay_type=0&sign_type=md5&version=2").toLowerCase();
		//System.out.println(info);
	}
}
