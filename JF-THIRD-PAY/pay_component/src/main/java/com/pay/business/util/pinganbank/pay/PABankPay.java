package com.pay.business.util.pinganbank.pay;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;

import net.sf.json.JSONObject;

import com.pay.business.util.PaySignUtil;
import com.pay.business.util.RandomUtil;
import com.pay.business.util.pinganbank.config.TestParams;
import com.pay.business.util.pinganbank.util.HttpUtil;
import com.pay.business.util.pinganbank.util.TLinx2Util;
import com.pay.business.util.pinganbank.util.TLinxAESCoder;
import com.pay.business.util.pinganbank.util.TLinxSHA1;
import com.pay.business.util.pinganbank.util.TLinxUtil;

/**
 * 
 * @Title: PABankPay.java
 * @Package com.pay.business.util.pinganbank.pay
 * @Description: 平安银行：商户支付业务核心接口
 * @author ZHOULIBO
 * @date 2017年8月8日 下午3:12:30
 * @version V1.0
 */
public class PABankPay {
	/**
	 * queryOrder 预下订单接口
	 * 
	 * @param outNo
	 *            开发者流水号，确认同一门店内唯一 Y
	 * @param pmtTag
	 *            付款方式：举例如：Cash,WeixinBERL,AlipayCS,Diy，其中Cash为现金，Diy为自定义，注意大小写，
	 *            首字母大写(填写一种)可以调用paylist接口查看商户开通了哪些 必填
	 * @param pmtName
	 *            商户自定义付款方式名称 N
	 * @param ordName
	 *            订单名字（描述） N
	 * @param originalAmount
	 *            原始交易金额（以分为单位，没有小数点） Y
	 * @param discountAmount
	 *            折扣金额（以分为单位，没有小数点） N
	 * @param ignoreAmount
	 *            抹零金额（以分为单位，没有小数点） N
	 * @param tradeAmount
	 *            实际交易金额（以分为单位，没有小数点） Y
	 * @param tradeAccount
	 *            交易帐号（收单机构交易的银行卡号，手机号等，可为空） N
	 * @param tradeNo
	 *            交易号（收单机构交易号，可为空）
	 * @param tradeResult
	 * @param remark
	 *            订单备注 N
	 * @param authCode
	 *            被扫（二维码支付）:条码支付的授权码（条码抢扫手机扫到的一串数字）
	 * @param tag
	 *            订单标记，订单附加数据 N
	 * @param jumpUrl
	 *            支付方式：微信公众号、支付宝服务窗支付:公众号/服务窗支付必填参数，支付结果跳转地址
	 * @param notifyUrl
	 *            异步通知地址
	 * @return 设定文件 Map<String,Object> 返回类型
	 */
	public static Map<String, String> queryOrder(String outNo, String pmtTag, String pmtName, String ordName, Integer originalAmount, Integer discountAmount,
			Integer ignoreAmount, Integer tradeAmount, String tradeAccount, String tradeNo, String tradeResult, String remark, String authCode, String tag,
			String jumpUrl, String notifyUrl,String OPEN_ID,String OPEN_KEY,String sub_appid,String sub_openid,String ip,int type) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(((type==3||type==1 || type==4)&&"Weixin".equalsIgnoreCase(pmtTag)))
		{
			jumpUrl = "https://pay.iquxun.cn/page/pay/re.html";
		}
		
		String s = "outNo=" + outNo +";pmtTag=" + pmtTag +";pmtName=" + pmtName +";ordName=" + ordName +";originalAmount=" + originalAmount 
				+";discountAmount=" + discountAmount +";ignoreAmount=" + ignoreAmount +";tradeAmount=" + tradeAmount +";tradeAccount=" + tradeAccount 
				+";tradeNo=" + tradeNo +";tradeResult=" + tradeResult +";remark=" + remark +";authCode=" + authCode +";tag=" + tag 
				+";jumpUrl=" + jumpUrl +";notifyUrl=" + notifyUrl +";OPEN_ID=" + OPEN_ID +";OPEN_KEY=" + OPEN_KEY +";sub_appid=" + sub_appid 
				+";sub_openid=" + sub_openid +";type=" + type;
		
		System.out.println("PARAMS:" + s);
		
		String paTag = pmtTag;
		
		if("QQ_SCAN".equals(pmtTag))
		{
			paTag = "Qpay";
		}
		
		// 初始化参数
		String timestamp = new Date().getTime() / 1000 + ""; // 时间

		try {

			// 固定参数
			TreeMap<String, String> postmap = new TreeMap<String, String>(); // 请求参数的map

			postmap.put("open_id",OPEN_ID);
			postmap.put("timestamp", timestamp);

			TreeMap<String, String> datamap = new TreeMap<String, String>(); // data参数的map
			//原生JS需要的参数
			if(type==2){
				datamap.put("JSAPI", "1");
				datamap.put("sub_appid", sub_appid);
				datamap.put("sub_openid", sub_openid);
			}
			datamap.put("out_no", outNo);
			datamap.put("pmt_tag", paTag);
			datamap.put("pmt_name", pmtName);
			datamap.put("ord_name", ordName);
			datamap.put("original_amount", originalAmount + "");
			datamap.put("discount_amount", discountAmount + "");
			datamap.put("ignore_amount", ignoreAmount + "");
			datamap.put("trade_amount", tradeAmount + "");
			datamap.put("trade_account", tradeAccount);
			datamap.put("trade_no", tradeNo);
			datamap.put("trade_result", tradeResult);
			datamap.put("remark", remark);
			datamap.put("auth_code", authCode);
			datamap.put("tag", tag);
			datamap.put("jump_url", jumpUrl);
			datamap.put("notify_url", notifyUrl);
			
			//微信H5
			if(type==4)
			{
				String info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://pay.qq.com\",\"wap_name\": \"腾讯充值\"}}";
				
				datamap.clear();
				datamap.put("notify_url", notifyUrl);
				datamap.put("original_amount", originalAmount + "");
				datamap.put("trade_amount", tradeAmount + "");
				datamap.put("ord_name", ordName);
				datamap.put("out_no", outNo);
				datamap.put("spbill_create_ip", ip);
				datamap.put("trade_type", "MWEB");
				datamap.put("scene_info", info);
				datamap.put("pmt_tag", "WeixinOL");
				
				System.out.println(datamap);
			}

			/**
			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
			 */
			TLinx2Util.handleEncrypt(datamap, postmap,OPEN_KEY);
			

			/**
			 * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
			 */
			TLinx2Util.handleSign(postmap,OPEN_KEY);
			

			/**
			 * 3 请求、响应
			 */
			String rspStr = TLinx2Util.handlePost(postmap, TestParams.PAYORDER,1);
			rspStr = TLinxUtil.decodeUnicode(rspStr);
			System.out.println("返回字符串=" + rspStr);

			/**
			 * 4 验签 有data节点时才验签
			 */
			JSONObject respObject = JSONObject.fromObject(rspStr);
			Object dataStr = respObject.get("data");
			if (!rspStr.isEmpty() && (dataStr != null)) {
				if (TLinx2Util.verifySign(respObject,OPEN_KEY)) { // 验签成功
					/**
					 * 5 AES解密，并hex2bin
					 */
					String respData = TLinxAESCoder.decrypt(dataStr.toString(),OPEN_KEY);
					// 成功
					System.out.println("==================响应data内容:" + respData);
					com.alibaba.fastjson.JSONObject o = com.alibaba.fastjson.JSONObject.parseObject(respData);
					System.out.println("JSONObject:" + o);
					if(type==1 || type==3){//扫码与特殊公众号支付
						String ord_no = String.valueOf(o.get("ord_no"));
						String trade_qrcode =null;
						if(jumpUrl!=null){
							trade_qrcode= String.valueOf(o.get("jsapi_pay_url"));
						}else{
							
							trade_qrcode = String.valueOf(o.get("trade_qrcode"));
						}
						System.out.println("上游订单号====>" + ord_no);
						System.out.println("上游订单URL====>" + trade_qrcode);
						
						String encodeUrl = "";
						
						try
						{
							encodeUrl = URLEncoder.encode(trade_qrcode, "UTF-8");
						}
						catch(Exception ex)
						{
							
						}
						
						int needCrack = 0;
						
						if(type==3 && "Weixin".equalsIgnoreCase(pmtTag))
						{
							needCrack = 1;
						}
						
						String newTradeQrcode = HttpUtil.httpMethodPost("http://wx-jump.iquxun.cn/tpUrlReJump.do?url=" + encodeUrl + "&crack=" + needCrack + "&order_no=" + outNo + "&money=" + tradeAmount, "", "UTF-8");
						
						//resultMap.put("qr_code", trade_qrcode);
						
						resultMap.put("qr_code", newTradeQrcode);
						resultMap.put("code", "10000");
						resultMap.put("out_trade_no", outNo);
					}
					//微信公众号支付
					if(type==2){
						resultMap.put("jsapi_appid", String.valueOf(o.get("appId")));
						resultMap.put("jsapi_timestamp",String.valueOf( o.get("timeStamp")));
						resultMap.put("jsapi_noncestr", String.valueOf(o.get("nonceStr")));
						resultMap.put("jsapi_package", String.valueOf(o.get("package")));
						resultMap.put("jsapi_signtype", String.valueOf(o.get("signType")));
						resultMap.put("jsapi_paysign", String.valueOf(o.get("paySign")));
						resultMap.put("code","10000");
						System.out.println("=====>平安银行微信公众号支付调起成功");
					}
					//微信H5
					if(type==4)
					{
						resultMap.put("qr_code", String.valueOf(o.get("mweb_url")));
						resultMap.put("code", "10000");
						resultMap.put("out_trade_no", outNo);
					}
				} else {
					resultMap.put("code", "10001");
					resultMap.put("msg", "验签失败");
					System.out.println("=====验签失败=====");
				}
			} else {
				// 返回错误信息
				resultMap.put("code", "10001");
				resultMap.put("msg", rspStr);
				System.out.println("=====没有返回data数据=====");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "10001");
			resultMap.put("msg", e.toString());
		}
		return resultMap;
	}

	/**
	 * payCancel 订单取消接口
	 * 
	 * @param ordNo
	 *            订单号， 二者必须填一个 Y
	 * @param outNo
	 *            开发者流水号 二者必须填一个 Y
	 * @return 设定文件 Map<String,Object> 返回类型
	 */
	public static Map<String, Object> payCancel(String ordNo, String outNo,String OPEN_ID,String OPEN_KEY,String PRIVATE_KEY) {
		// 初始化参数
		String timestamp = new Date().getTime() / 1000 + ""; // 时间
		try {

			// 固定参数
			TreeMap<String, String> postmap = new TreeMap<String, String>(); // 请求参数的map

			postmap.put("open_id",OPEN_ID);
			postmap.put("timestamp", timestamp);

			TreeMap<String, String> datamap = new TreeMap<String, String>(); // data参数的map

			datamap.put("out_no", outNo);
			datamap.put("ord_no", ordNo);

			/**
			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
			 */
			TLinx2Util.handleEncrypt(datamap, postmap,OPEN_KEY);

			// 签名方式
			postmap.put("sign_type", "RSA");

			/**
			 * 2 请求参数签名 按A~z排序，串联成字符串，先进行RSA签名
			 */
			TLinx2Util.handleSignRSA(postmap,OPEN_KEY,PRIVATE_KEY);

			/**
			 * 3 请求、响应
			 */
			String rspStr = TLinx2Util.handlePost(postmap, TestParams.PAYCANCEL,1);

			System.out.println("返回字符串=" + rspStr);
			/**
			 * 4 验签 有data节点时才验签
			 */
			JSONObject respObject = JSONObject.fromObject(rspStr);
			Object dataStr = respObject.get("data");

			if (!rspStr.isEmpty() || (dataStr != null)) {
				if (TLinx2Util.verifySign(respObject,OPEN_KEY)) { // 验签成功
					/**
					 * 5 AES解密，并hex2bin
					 */
					String respData = TLinxAESCoder.decrypt(dataStr.toString(),OPEN_KEY);

					System.out.println("==================响应data内容:" + respData);
				} else {
					System.out.println("=====验签失败=====");
				}
			} else {
				System.out.println("=====没有返回data数据=====");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * payRefund 订单退款接口
	 * 
	 * @param outNo
	 *            原始订单的开发者交易流水号 Y
	 * @param refundOutNo
	 *            新退款订单的开发者流水号，同一门店内唯一 Y
	 * @param refundOrdName
	 *            退款订单名称，可以为空 N
	 * @param refundAmount
	 *            退款金额（以分为单位，没有小数点） Y
	 * @param tradeAccount
	 *            交易帐号（收单机构交易的银行卡号，手机号等，可为空） N
	 * @param tradeNo
	 *            交易号（收单机构交易号，可为空） N
	 * @param tradeResult
	 *            收单机构原始交易信息，请转换为json数据 N
	 * @param tmlToken
	 *            终端令牌，终端上线后获得的令牌 N
	 * @param remark
	 *            退款备注
	 * @param shopPass
	 *            主管密码，对密码进行sha1加密，默认为123456 Y
	 * @return 设定文件 Map<String,Object> 返回类型
	 */
	public static Map<String, String> payRefund(String outNo, String refundOutNo, String refundOrdName, Integer refundAmount, String tradeAccount,
			String tradeNo, String tradeResult, String tmlToken, String remark, String shopPass,String OPEN_ID,String OPEN_KEY,String PRIVATE_KEY) {
		Map<String, String> resultMap = new HashMap<String, String>();
		// 初始化参数
		String timestamp = new Date().getTime() / 1000 + ""; // 时间
		try {
			// 固定参数
			TreeMap<String, String> postmap = new TreeMap<String, String>(); // 请求参数的map
			postmap.put("open_id", OPEN_ID);
			postmap.put("timestamp", timestamp);
			TreeMap<String, String> datamap = new TreeMap<String, String>(); // data参数的map
			datamap.put("out_no", outNo);
			datamap.put("refund_out_no", refundOutNo);
			datamap.put("refund_ord_name", refundOrdName);
			datamap.put("refund_amount", refundAmount + "");
			datamap.put("trade_account", tradeAccount);
			datamap.put("trade_no", tradeNo);
			datamap.put("trade_result", tradeResult);
			datamap.put("tml_token", tmlToken);
			datamap.put("remark", remark);
			datamap.put("shop_pass", shopPass);
			/**
			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
			 */
			TLinx2Util.handleEncrypt(datamap, postmap,OPEN_KEY);

			postmap.put("sign_type", "RSA");

			/**
			 * 2 请求参数签名 按A~z排序，串联成字符串，进行RSA签名
			 */
			TLinx2Util.handleSignRSA(postmap,OPEN_KEY,PRIVATE_KEY);

			/**
			 * 3 请求、响应
			 */
			String rspStr = TLinx2Util.handlePost(postmap, TestParams.PAYREFUND,1);

			/**
			 * 4 验签 有data节点时才验签
			 */
			JSONObject respObject = JSONObject.fromObject(rspStr);
			Object dataStr = respObject.get("data");
			rspStr = TLinxUtil.decodeUnicode(rspStr);
			System.out.println("返回字符串=" + rspStr);

			if (!rspStr.isEmpty() && (dataStr != null)) {
				if (TLinx2Util.verifySign(respObject,OPEN_KEY)) { // 验签成功
					/**
					 * 5 AES解密，并hex2bin
					 */
					String respData = TLinxAESCoder.decrypt(dataStr.toString(), OPEN_KEY);
					com.alibaba.fastjson.JSONObject o = com.alibaba.fastjson.JSONObject.parseObject(respData);
					System.out.println("==================响应data内容:" + respData);
					int status = o.getInteger("status");
					if (status == 1) {
						// 退款成功
						resultMap.put("code", "10000");
					} else {
						// 失败
						resultMap.put("code", "10001");
						resultMap.put("msg", "失败原因" + respData);
					}
				} else {
					// 退款失败
					resultMap.put("code", "10001");
					resultMap.put("msg", "验签失败");
					System.out.println("=====验签失败=====");
				}
			} else {
				// 退款失败
				resultMap.put("code", "10001");
				resultMap.put("msg", rspStr);
				System.out.println("=====没有返回data数据=====");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "10001");
			resultMap.put("msg", e.toString());
		}
		return resultMap;

	}

	/**
	 * 
	 * queryOrderList 获取订单列表(此接口用于对账接口)
	 * 
	 * @param outNo
	 *            说明：开发者流水号 可为空
	 * @param tradeNo
	 *            说明：交易单号（收单机构交易号，可为空）
	 * @param ordNo
	 *            说明：付款订单号
	 * @param pmtTag
	 *            说明：支付方式标签
	 * @param ordType
	 *            说明：交易方式1消费，2辙单（退款）
	 * @param status
	 *            说明：订单状态（1交易成功，2待支付，4已取消，9等待用户输入密码确认）
	 * @param sdate
	 *            说明：订单开始日期
	 * @param edate
	 *            说明：订单结束日期
	 * @return 设定文件 Map<String,Object> 返回类型
	 */
	public static List<PANOrderVo> queryOrderList(String outNo, String tradeNo, String ordNo, String pmtTag, String ordType, String status, String sdate,
			String edate,String OPEN_ID,String OPEN_KEY) {
		List<PANOrderVo> voList = new ArrayList<PANOrderVo>();
		// 初始化参数
		String timestamp = new Date().getTime() / 1000 + ""; // 时间

		try {
			// 固定参数
			TreeMap<String, String> postmap = new TreeMap<String, String>();// 请求参数的map
			postmap.put("open_id", OPEN_ID);
			postmap.put("timestamp", timestamp);

			TreeMap<String, String> datamap = new TreeMap<String, String>();// data参数的map
			datamap.put("out_no", outNo);
			datamap.put("trade_no", tradeNo);
			datamap.put("ord_no", ordNo);
			datamap.put("pmt_tag", pmtTag);
			datamap.put("ord_type", ordType);
			datamap.put("status", status);
			datamap.put("sdate", sdate);
			datamap.put("edate", edate);
			datamap.put("pagesize", "100");
			/**
			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
			 */
			TLinx2Util.handleEncrypt(datamap, postmap,OPEN_KEY);

			/**
			 * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
			 */
			TLinx2Util.handleSign(postmap,OPEN_KEY);

			/**
			 * 3 请求、响应
			 */
			String rspStr = TLinx2Util.handlePost(postmap, TestParams.ORDERLIST,1);

			System.out.println("返回字符串=" + rspStr);

			/**
			 * 4 验签 有data节点时才验签
			 */
			JSONObject respObject = JSONObject.fromObject(rspStr);
			Object dataStr = respObject.get("data");
			if (!rspStr.isEmpty() || (dataStr != null)) {
				if (TLinx2Util.verifySign(respObject,OPEN_KEY)) { // 验签成功
					/**
					 * 5 AES解密，并hex2bin
					 */
					String respData = TLinxAESCoder.decrypt(dataStr.toString(),OPEN_KEY);
					System.out.println("==================响应data内容:" + respData);
					com.alibaba.fastjson.JSONObject o = com.alibaba.fastjson.JSONObject.parseObject(respData);
					System.out.println(o);
					System.out.println(o.get("pages"));
					com.alibaba.fastjson.JSONObject pagesObject = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(o.get("pages")));
					int totalpage = pagesObject.getInteger("totalpage");
					System.out.println("总页数为：" + totalpage);
					// 如果大于1页的时候；需要每页去请求数据
					if (totalpage == 1) {
						com.alibaba.fastjson.JSONArray list = com.alibaba.fastjson.JSONArray.parseArray(String.valueOf(o.get("list")));
						System.out.println("数据集合为" + list);
						Iterator<Object> it = list.iterator();
						PANOrderVo vo = null;
						while (it.hasNext()) {
							com.alibaba.fastjson.JSONObject ob = (com.alibaba.fastjson.JSONObject) it.next();
							//status:订单状态（1交易成功，2待支付，4已取消，9等待用户输入密码确认）
							//这里只获取交易成功的订单
							if (ob.getString("status").equals("1")) {
								vo = new PANOrderVo();
								// 下单时间
								vo.setAdd_time(ob.getString("add_time"));
								// 上游订单号
								vo.setOrd_mct_id(ob.getString("ord_mct_id"));
								// 交易类型：1交易，2辙单【退款】
								vo.setOrd_type(ob.getString("ord_type"));
								// 原始金额
								vo.setOriginal_amount(ob.getString("original_amount"));
								// 商户订单号
								vo.setOut_no(ob.getString("out_no"));
								// 订单状态（1交易成功，2待支付，9待输入密码，4已取消）
								vo.setStatus(ob.getString("status"));
								// 实际金额 分
								vo.setTrade_amount(ob.getString("trade_amount"));
								// 收单机构返回的交易号
								vo.setTrade_no(ob.getString("trade_no"));
								// 实际交易时间
								vo.setTrade_pay_time(ob.getString("trade_pay_time"));
								voList.add(vo);
							}
						}
					} else {
						// 每页请求数据
						for (int i = 1; i <= totalpage; i++) {
							// 初始化参数
							String timestamp1 = new Date().getTime() / 1000 + ""; // 时间
							// 固定参数
							TreeMap<String, String> postmap1 = new TreeMap<String, String>();// 请求参数的map
							postmap1.put("open_id",OPEN_ID);
							postmap1.put("timestamp", timestamp1);

							TreeMap<String, String> datamap1 = new TreeMap<String, String>();// data参数的map
							datamap1.put("out_no", outNo);
							datamap1.put("trade_no", tradeNo);
							datamap1.put("ord_no", ordNo);
							datamap1.put("pmt_tag", pmtTag);
							datamap1.put("ord_type", ordType);
							datamap1.put("status", status);
							datamap1.put("sdate", sdate);
							datamap1.put("edate", edate);
							datamap1.put("pagesize", "100");
							// 页码
							datamap1.put("page", String.valueOf(i));
							/**
							 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
							 */
							TLinx2Util.handleEncrypt(datamap1, postmap1,OPEN_KEY);

							/**
							 * 2 请求参数签名
							 * 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
							 */
							TLinx2Util.handleSign(postmap1,OPEN_KEY);

							/**
							 * 3 请求、响应
							 */
							String rspStr1 = TLinx2Util.handlePost(postmap1, TestParams.ORDERLIST,1);

							System.out.println("返回字符串=" + rspStr1);
							/**
							 * 4 验签 有data节点时才验签
							 */
							JSONObject respObject1 = JSONObject.fromObject(rspStr1);
							Object dataStr1 = respObject1.get("data");
							if (!rspStr1.isEmpty() || (dataStr1 != null)) {
								if (TLinx2Util.verifySign(respObject1,OPEN_KEY)) { // 验签成功
									/**
									 * 5 AES解密，并hex2bin
									 */
									String respData1 = TLinxAESCoder.decrypt(dataStr1.toString(),OPEN_KEY);
									System.out.println("==================响应data内容:" + respData1);
									com.alibaba.fastjson.JSONObject o1 = com.alibaba.fastjson.JSONObject.parseObject(respData1);
									com.alibaba.fastjson.JSONArray list = com.alibaba.fastjson.JSONArray.parseArray(String.valueOf(o1.get("list")));
									System.out.println("数据集合为" + list);
									Iterator<Object> it = list.iterator();
									PANOrderVo vo = null;
									while (it.hasNext()) {
										com.alibaba.fastjson.JSONObject ob = (com.alibaba.fastjson.JSONObject) it.next();
										vo = new PANOrderVo();
										//status:订单状态（1交易成功，2待支付，4已取消，9等待用户输入密码确认）
										//这里只获取交易成功的订单
										if (ob.getString("status").equals("1")) {
											// 下单时间
											vo.setAdd_time(ob.getString("add_time"));
											// 上游订单号
											vo.setOrd_mct_id(ob.getString("ord_mct_id"));
											// 交易类型：1交易，2辙单【退款】
											vo.setOrd_type(ob.getString("ord_type"));
											// 原始金额
											vo.setOriginal_amount(ob.getString("original_amount"));
											// 商户订单号
											vo.setOut_no(ob.getString("out_no"));
											// 订单状态（1交易成功，2待支付，9待输入密码，4已取消）
											vo.setStatus(ob.getString("status"));
											// 实际金额 分
											vo.setTrade_amount(ob.getString("trade_amount"));
											// 收单机构返回的交易号
											vo.setTrade_no(ob.getString("trade_no"));
											// 实际交易时间
											vo.setTrade_pay_time(ob.getString("trade_pay_time"));
											voList.add(vo);
										}

									}
								} else {
									System.out.println("=====验签失败=====");
								}
							} else {
								System.out.println("=====没有返回data数据=====");
							}
						}
					}
				} else {
					System.out.println("=====验签失败=====");
				}
			} else {
				System.out.println("=====没有返回data数据=====");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return voList;

	}

	/**
	 * 
	 * queryOrderView 查看订单详情
	 * 
	 * @param outNo
	 *            开发者流水号 Y
	 * @return 设定文件 Map<String,Object> 返回类型
	 */
	public static Map<String, Object> queryOrderView(String outNo,String OPEN_ID,String OPEN_KEY) {

		// 初始化参数
		String timestamp = new Date().getTime() / 1000 + ""; // 时间

		try {

			// 固定参数
			TreeMap<String, String> postmap = new TreeMap<String, String>(); // 请求参数的map

			postmap.put("open_id",OPEN_ID);
			postmap.put("timestamp", timestamp);

			TreeMap<String, String> datamap = new TreeMap<String, String>(); // data参数的map

			datamap.put("out_no", outNo);

			/**
			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
			 */
			TLinx2Util.handleEncrypt(datamap, postmap,OPEN_KEY);

			/**
			 * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
			 */
			TLinx2Util.handleSign(postmap,OPEN_KEY);

			/**
			 * 3 请求、响应
			 */
			String rspStr = TLinx2Util.handlePost(postmap, TestParams.ORDERVIEW,1);

			System.out.println("返回字符串=" + rspStr);

			/**
			 * 4 验签 有data节点时才验签
			 */
			JSONObject respObject = JSONObject.fromObject(rspStr);
			Object dataStr = respObject.get("data");

			if (!rspStr.isEmpty() || (dataStr != null)) {
				if (TLinx2Util.verifySign(respObject,OPEN_KEY)) { // 验签成功

					/**
					 * 5 AES解密，并hex2bin
					 */
					String respData = TLinxAESCoder.decrypt(dataStr.toString(),OPEN_KEY);

					System.out.println("==================响应data内容:" + respData);
				} else {
					System.out.println("=====验签失败=====");
				}
			} else {
				System.out.println("=====没有返回data数据=====");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * queryPayList 获取商户签约支付方式列表
	 * 
	 * @return 设定文件 Map<String,Object> 返回类型
	 */
	public static Map<String, Object> queryPayList(String OPEN_ID,String OPEN_KEY) {
		// 初始化参数
		String pmtType = "0,1,2,3,4,5";
		String timestamp = new Date().getTime() / 1000 + ""; // 时间

		try {
			// 固定参数
			TreeMap<String, String> postmap = new TreeMap<String, String>();// 请求参数的map
			postmap.put("open_id",OPEN_ID);
			postmap.put("timestamp", timestamp);

			TreeMap<String, String> datamap = new TreeMap<String, String>();// data参数的map
			datamap.put("pmt_type", pmtType);

			/**
			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
			 */
			TLinx2Util.handleEncrypt(datamap, postmap,OPEN_KEY);

			/**
			 * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
			 */
			TLinx2Util.handleSign(postmap,OPEN_KEY);

			/**
			 * 3 请求、响应
			 */
			String rspStr = TLinx2Util.handlePost(postmap, TestParams.PAYLIST,1);
			System.out.println("返回字符串=" + rspStr);

			/**
			 * 4 验签 有data节点时才验签
			 */
			JSONObject respObject = JSONObject.fromObject(rspStr);
			Object dataStr = respObject.get("data");

			if (!rspStr.isEmpty() || (dataStr != null)) {
				if (TLinx2Util.verifySign(respObject,OPEN_KEY)) { // 验签成功
					/**
					 * 5 AES解密，并hex2bin
					 */
					String respData = TLinxAESCoder.decrypt(dataStr.toString(),OPEN_KEY);
					com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respData);
					System.out.println("==================响应Map内容:" + jsonObject);
					for (Object map : jsonObject.entrySet()) {
						System.out.println(((Map.Entry) map).getKey() + "  " + ((Map.Entry) map).getValue());
					}
				} else {
					System.out.println("=====验签失败=====");
				}
			} else {
				System.out.println("=====没有返回data数据=====");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * queryPayStatus 查看订单状态
	 * 
	 * @param ordNo
	 *            说明：订单号 二者必须填一个
	 * @param outNo
	 *            说明：开发者流水号 二者必须填一个
	 * @return 设定文件 Map<String,Object> 返回类型
	 */
	public static Map<String, String> queryPayStatus(String ordNo, String outNo,String OPEN_ID,String OPEN_KEY) {
		Map<String, String> returnMap = new HashMap<String, String>();
		// 初始化参数
		String timestamp = new Date().getTime() / 1000 + ""; // 时间
		try {
			// 固定参数
			TreeMap<String, String> postmap = new TreeMap<String, String>(); // 请求参数的map
			postmap.put("open_id",OPEN_ID);
			postmap.put("timestamp", timestamp);
			TreeMap<String, String> datamap = new TreeMap<String, String>(); // data参数的map
			datamap.put("out_no", outNo);
			datamap.put("ord_no", ordNo);
			/**
			 * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
			 */
			TLinx2Util.handleEncrypt(datamap, postmap,OPEN_KEY);
			/**
			 * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
			 */
			TLinx2Util.handleSign(postmap,OPEN_KEY);
			/**
			 * 3 请求、响应
			 *
			 */
			String rspStr = TLinx2Util.handlePost(postmap, TestParams.QUERYPAYSTATUS,1);
			/**
			 * 4 验签 有data节点时才验签
			 */
			JSONObject respObject = JSONObject.fromObject(rspStr);
			Object dataStr = respObject.get("data");
			rspStr = TLinxUtil.decodeUnicode(rspStr);
			System.out.println("返回字符串=" + rspStr);
			if (!rspStr.isEmpty() || (dataStr != null)) {
				if (TLinx2Util.verifySign(respObject,OPEN_KEY)) { // 验签成功
					/**
					 * 5 AES解密，并hex2bin
					 */
					String respData = TLinxAESCoder.decrypt(dataStr.toString(),OPEN_KEY);
					System.out.println("==================响应data内容:" + respData);
					com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(respData);
					// 订单状态（1交易成功，2待支付，4已取消，9等待用户输入密码确认）:
					String status = jsonObject.getString("status");
					// 交易类型：1交易 ；退款
					String ord_type = jsonObject.getString("ord_type");
					//金额 分
					String	trade_amount=jsonObject.getString("trade_amount");
					System.out.println("订单上游状态为：" + status);
					System.out.println("订单交易类型为：" + ord_type);
					System.out.println("订单实际金额为："+trade_amount);
					returnMap.put("code", "10000");
					returnMap.put("status", status);
					returnMap.put("ord_type", ord_type);
					returnMap.put("trade_amount", trade_amount);
				} else {
					System.out.println("=====验签失败=====");
					returnMap.put("code", "10001");
					returnMap.put("msg", "验签失败");
				}
			} else {
				returnMap.put("code", "10001");
				returnMap.put("msg", rspStr);
				System.out.println("=====没有返回data数据=====");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("code", "10001");
			returnMap.put("msg", e.toString());
		}
		return returnMap;

	}
	
	private static void test()
	{
		String outNo= "DD" + System.currentTimeMillis();
		String pmtTag= "Weixin";
		String pmtName=null;
		String ordName=outNo;
		Integer originalAmount=1;
		Integer discountAmount=null;
		Integer ignoreAmount=null;
		Integer tradeAmount=1;
		String tradeAccount=null;
		String tradeNo=null;
		String tradeResult=null;
		String remark=null;
		String authCode=null;
		String tag=null;
		String jumpUrl= "https://pay.iquxun.cn/page/pay/re.html";
		String notifyUrl="https://pay.iquxun.cn/aiJinFuPay/PABankScanPayCallBack.do";
		String OPEN_ID="112ffe903993b9816fbf9a92e453e849";
		String OPEN_KEY="ba3a2500774ce7d399974eee7c645c9b";
		String sub_appid=null;
		String sub_openid=null;
		int type=3;
		String ip = "113.88.178.80";
		
		OPEN_ID = "bc4bfb2f228548fbe4ff60185fb1047c";
		OPEN_KEY = "9cb91951114ccfd2c5975ae79e9cb6bd";
		
		Map<String, String> reMap = PABankPay.queryOrder(outNo, pmtTag, pmtName, ordName, originalAmount,
				discountAmount, ignoreAmount, tradeAmount, tradeAccount,
				tradeNo, tradeResult, remark, authCode, tag, jumpUrl, notifyUrl,
				OPEN_ID, OPEN_KEY, sub_appid, sub_openid, ip, type);
		
		System.out.println(reMap);
		
	}
	
	// 测试main
	public static void main(String[] args) {
		/**
		 * 下单测试
		 */
		 String date=String.valueOf(new Date().getTime());
		
		 
		 String orderNo = "AD" + String.valueOf(new Date().getTime());
		 
		 System.out.println("测试订单号为："+ orderNo);
		 
		 test();
		 
//		 Map<String, String> map=queryOrder(orderNo, "Weixin", null, "微信公众号测试", 
//				 //1是price
//				 1, null, null, 1, null, null, null, null,null, null, "https://www.baidu.com/","http://aijinfupay.tunnel.echomod.cn/aiJinFuPay/PABankScanPayCallBack.do","e9d825cabb3c15230d20df841882deae","abc38f0827c52dc45634da1d2aee767e",null,null,1);
		 
		 /*
		 Map<String, String> map = queryOrder(orderNo, "QQ_SCAN", null, orderNo, 1, null, 
				 null, 1, null, null, null, 
				 null, null, null, "https://pay.iquxun.cn/page/pay/re.html", "https://pay.iquxun.cn/aiJinFuPay/PABankScanPayCallBack.do", "43fa7c0d5418ee8ae95587fe557d2121", "f8b701174807c0e15c9daa1c642a9975", 
				 null, null, "14.28.136.229" ,1);
		 
		 System.out.println(map);
		 
		 */
		/**
		 * 取消订单测试
		 */
		// payCancel("", "66666"); // 二者填一个参数就行
		/**
		 * 退款测试
		 */
//		 String fundN=String.valueOf(new Date().getTime());
//		 System.out.println("退款订单号为："+fundN);
//		 payRefund("1502249397103", fundN, "退款测试", 1, null, null,null,null,null, TLinxSHA1.SHA1("123456"));
		/**
		 * 订单列表测试
		 */
//		 queryOrderList(null,null,null,null,null,null,null,null,"35fa0f123ea5f7718d23eba85370af8d","0f3bd97cc3e28bfeb733918f71a8be68");
		/**
		 * 订单详情测试
		 */
		// queryOrderView("81685225590");
		/**
		 * 获取商户签约支付方式列表
		 */
		// queryPayList();
		/**
		 * 查看订单状态
		 */
//		queryPayStatus("", "1502249397103"); // 二者填一个参数就行
	}
}
