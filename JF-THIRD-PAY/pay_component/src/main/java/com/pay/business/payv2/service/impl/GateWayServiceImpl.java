package com.pay.business.payv2.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.service.Payv2PayGoodsService;
import com.pay.business.order.service.Payv2PayOrderGroupService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.service.GateWayService;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.MapUtil;
import com.pay.business.util.PayRateDictValue;
import com.pay.business.util.RandomUtil;
import com.pay.business.util.hfbpay.WeChatSubscrip.pay.HfbWeChatPay;
import com.pay.business.util.mail.MailRun;
import com.pay.business.util.minshengbank.HttpMinshengBank;
import com.pay.business.util.pinganbank.config.TestParams;
import com.pay.business.util.pinganbank.pay.PABankPay;
import com.pay.business.util.wftpay.weChatSubscrPay.pay.SwiftWechatGzhPay;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;
import com.pay.business.util.xyShenzhen.XYSZBankPay;

/**
 * @Title: GateWayServiceImpl.java
 * @Package com.pay.business.payv2.service.impl
 * @Description:公众号支付实现类
 * @author 周立波
 * @date 2017年6月27日 下午5:28:11
 * @version V1.0
 */
@Service("gateWayService")
public class GateWayServiceImpl implements GateWayService {
	private static Logger log = Logger.getLogger(GateWayServiceImpl.class);
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;// 支付通道
	@Autowired
	private Payv2PayOrderMapper payv2PayOrderMapper;
	@Autowired
	private Payv2PayGoodsService payv2PayGoodsService;
	@Autowired
	private Payv2PayOrderGroupService payv2PayOrderGroupService;
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	/**
	 * 微信公众支付参组装接口：预下订单
	 */
	public Map<String, String> wechatGzhPay(Map<String, Object> map, Payv2BussCompanyApp pbca, Integer payViewType) throws Exception {
		//预下订单
		Map<String, String> orderMap = payv2PayOrderService.dyCreateOrder(map, pbca, payViewType, 5);
		if(orderMap.containsKey("orderNum")){
			orderMap.put("address", String.valueOf(map.get("ip")));
			String orderNum=orderMap.get("orderNum");
			//存入redis
			RedisDBUtil.redisDao.hmset(orderNum, orderMap);
		}
		return orderMap;
	}
	
	/**
	 * 微信公众号支付
	 */
	public Map<String, String> wechatGzhPayByCrack(Map<String,String> map) throws Exception {
		//===========================type:1为URL拉起支付；2：为原生JS拉起支付================================================//
		Map<String, String> returnMap = new HashMap<String, String>();
		String dictName=String.valueOf(map.get("dictName"));
		//汇付宝微信公众号支付
		if(dictName.equals(PayRateDictValue.PAY_TYPE_HFB_WEIXIN_GZH)){
			log.info("=====》公众号支付：支付走了：汇付宝微信公众号支付通道");
			String agentId=String.valueOf(map.get("rateKey1"));
			String key=String.valueOf(map.get("rateKey2"));
			if (agentId != null && key != null) {
				// 支付集订单号
				String agent_bill_id = String.valueOf(map.get("orderNum"));
				// 订单名字
//				String goods_name = String.valueOf(map.get("orderName"));
				String goods_name = agent_bill_id;
				// 金额
				String pay_amt = String.valueOf(map.get("payMoney"));
				// 支付时间
				String agent_bill_time = dateToString(new Date());
				// IP
				String user_ip = String.valueOf(map.get("address"));
				// 商品个数
				String goods_num = String.valueOf(map.get("goodsNum"));
				// 支付说明
				String goods_note = String.valueOf(map.get("goodsNote"));
				// 商户自定义 原样返回
				String remark = String.valueOf(map.get("remark"));
				// 同步返回URL
				// String return_url = String.valueOf(map.get("returnUrl"));
				returnMap = HfbWeChatPay.pay(agent_bill_id, agent_bill_time, pay_amt, user_ip, goods_num, goods_name, goods_note, remark, agentId, key);
				// 统一组装数据返回前端
				returnMap.put("orderNum", agent_bill_id);
				returnMap.put("code", "10000");
				returnMap.put("type", "1");
			}else{
				log.error("汇付宝微信公众号支付:数据库参数配置不完整：rateKey1,rateKey2");
			}
			
		}
		//威富通微信公众号支付
		if(dictName.equals(PayRateDictValue.PAY_TYPE_WFT_WEIXIN_GZH)){
			log.info("=====》公众号支付：支付走了：威富通微信公众号支付通道");
			SortedMap<String, String> Sortedmap=new TreeMap<String,String>();
			//获取通道默认的参数值
			String mchId=String.valueOf(map.get("rateKey1"));
			String key=String.valueOf(map.get("rateKey2"));
			//公众号appid
			String sub_appid=String.valueOf(map.get("gzhAppId"));
			if (mchId != null && key != null&&sub_appid!=null) {
				// 支付集订单号
				Sortedmap.put("out_trade_no", String.valueOf(map.get("orderNum")));
				// 商品描述
//				Sortedmap.put("body", String.valueOf(map.get("orderName")));
				Sortedmap.put("body", String.valueOf(map.get("orderNum")));
				// 微信用户关注商家公众号的openid（注：使用测试号时此参数置空，即不要传这个参数，使用正式商户号时才传入，参数名是sub_openid，具体请看文档最后注意事项第7点）
				// 正式环境下用到
				Sortedmap.put("sub_openid", String.valueOf(map.get("openid")));
				// 当发起公众号支付时，值是微信公众平台基本配置中的AppID(应用ID)；当发起小程序支付时，值是对应小程序的AppID
				// 正式环境下用到
				Sortedmap.put("sub_appid",sub_appid);
				// 总金额，以分为单位，不允许包含任何字、符号
				String total_fee=DecimalUtil.yuanToCents(map.get("payMoney").toString());
				Sortedmap.put("total_fee",total_fee);
				// 订单生成的机器 IP
				Sortedmap.put("mch_create_ip", String.valueOf(map.get("address")));
				// 此处：is_raw为1的时候是调起原生JS支付；不填或者为0的时候是非原生JS支付
				// map.put("is_raw", "1");
				Map<String, String> SwiftMap = SwiftWechatGzhPay.swiftWechatGzhPay(Sortedmap, mchId, key);
				if (String.valueOf(SwiftMap.get("status")).equals("0")) {// 成功
					String jsonMap = MapUtil.mapToJson(SwiftMap);
					Map<String, Object> toMap = MapUtil.parseJsonToMap(jsonMap);
					// 统一组装数据返回前端
					returnMap.put("orderNum", String.valueOf(map.get("orderNum")));
					returnMap.put("address", toMap.get("pay_url").toString());
					returnMap.put("code", "10000");// 成功
					returnMap.put("type", "1");
					log.info("威富通微信公众号支付通道调起成功！");
				} else {
					returnMap.put("msg", SwiftMap.get("msg"));// 失败
					returnMap.put("code", "10001");// 失败
					returnMap.put("type", "1");
					log.error("威富通微信公众号支付通道调起失败！");
					// 发送邮件:失败原因
					MailRun.send(dictName, returnMap.get("msg")
							,map.get("orderNum"),map.get("rateId"),map.get("appName")
							,map.get("payMoney"),map.get("companyName"),map.get("rateCompanyName")
							,map.get("payWayName"));
				}
			} else {
				log.error("威富通微信公众号支付数据库配置不完整：rateKey1,rateKey2,公众号appid");
			}
		}
		//兴业银行微信公众号支付
		if(dictName.equals(PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_GZH)){
			log.info("=====》公众号支付：支付走了：兴业银行微信公众号支付通道");
			String appid=String.valueOf(map.get("rateKey1"));
			String mch_id=String.valueOf(map.get("rateKey2"));
			String key=String.valueOf(map.get("rateKey3"));
			//公众号appid
			String sub_appid=String.valueOf(map.get("gzhAppId"));
			if(appid!=null&&mch_id!=null&&key!=null&&sub_appid!=null){
				String orderNum = String.valueOf(map.get("orderNum"));
				String ip = String.valueOf(map.get("address"));
				String totalFee = DecimalUtil.yuanToCents(map.get("payMoney").toString());
//				String body= String.valueOf(map.get("orderName"));
				String body= String.valueOf(map.get("orderNum"));
				//微信APPID
				String wx_appid=sub_appid;
				//用户openid
				String openid=String.valueOf(map.get("openid"));
				//拉起支付来源:1为公众号 2：为微信扫码支付
				int fromType=1;
				returnMap=XyBankPay.xYBankWechatGzhPay(orderNum, ip, totalFee, body, appid, mch_id, key,wx_appid,openid,fromType);
				//成功
				if(returnMap.get("code").toString().equals("10000")){
					returnMap.put("orderNum",orderNum);
					returnMap.put("type", "2");
				}else{
					//发送邮件:失败原因
					MailRun.send(dictName, returnMap.get("msg")
							,map.get("orderNum"),map.get("rateId"),map.get("appName")
							,map.get("payMoney"),map.get("companyName"),map.get("rateCompanyName")
							,map.get("payWayName"));
					returnMap.put("msg",returnMap.get("msg"));//失败
					returnMap.put("code","10001");//失败
					returnMap.put("type", "2");
				}
			}else{
				log.error("兴业银行微信公众号支付数据库配置参数不完整：rateKey1，rateKey2，rateKey3");
			}
		}
		
		//民生银行微信公众号支付
		if(dictName.equals(PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_GZH)){
			log.info("=====》公众号支付：支付走了：民生银行微信公众号支付通道");
			String merNo=String.valueOf(map.get("rateKey1"));
			String key=String.valueOf(map.get("rateKey2"));
			//公众号appid
			String sub_appid=String.valueOf(map.get("gzhAppId"));
			if(merNo!=null&&key!=null&&sub_appid!=null){
				String orderNo=String.valueOf(map.get("orderNum"));
				//金额
				String amount=DecimalUtil.yuanToCents(map.get("payMoney").toString());
				String reqId=new Date().getTime()+RandomUtil.generateString(4);
				String reqTime=DateUtil.DateToString(new Date(), "yyyyMMddHHmmss");
				//微信APPID
				String subAppId=sub_appid;
				//用户openid
				String subOpenId=String.valueOf(map.get("openid"));
				//发起支付
				returnMap=HttpMinshengBank.MSWeChatGzhPay(merNo, key, orderNo, amount, reqId, reqTime, subAppId, subOpenId);
				if (returnMap.get("code").toString().equals("10000")) {
					returnMap.put("orderNum",orderNo);
					returnMap.put("type", "2");
				} else {
					returnMap.put("msg", returnMap.get("msg"));// 失败
					returnMap.put("code", "10001");// 失败
					returnMap.put("type", "2");
					if(returnMap.containsKey("msg")&&!returnMap.get("msg").contains("desc=订单号重复")){
						returnMap.put("code", "10002");// 订单号重复
						// 发送邮件:失败原因
						MailRun.send(dictName, returnMap.get("msg")
								,map.get("orderNum"),map.get("rateId"),map.get("appName")
								,map.get("payMoney"),map.get("companyName"),map.get("rateCompanyName")
								,map.get("payWayName"));
					}
					
				}
			}else{
				log.error("民生银行微信公众号支付数据库配置参数不完整：rateKey1，rateKey2");
			}
		}
		//兴业深圳微信公众号
		if(dictName.equals(PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_GZH)){
			log.info("=====》公众号支付：兴业深圳微信公众号支付通道");
			String mchId=String.valueOf(map.get("rateKey1"));
			String key=String.valueOf(map.get("rateKey2"));
			//公众号appid
			String sub_appid=String.valueOf(map.get("gzhAppId"));
			if(null!=mchId&&null!=key&&null!=sub_appid){
				String total_fee=DecimalUtil.yuanToCents(map.get("payMoney").toString());
				Map<String, String> xyszWXGzhPay = XYSZBankPay.xyszWXGzhPay(map.get("orderNum"), map.get("orderNum"), total_fee, map.get("address"), map.get("openid"), map.get("gzhAppId"), 
						mchId, key);
				if (String.valueOf(xyszWXGzhPay.get("status")).equals("0")) {// 成功
					String jsonMap = MapUtil.mapToJson(xyszWXGzhPay);
					Map<String, Object> toMap = MapUtil.parseJsonToMap(jsonMap);
					// 统一组装数据返回前端
					returnMap.put("orderNum", String.valueOf(map.get("orderNum")));
					returnMap.put("address", toMap.get("pay_url").toString());
					returnMap.put("code", "10000");// 成功
					returnMap.put("type", "1");
					log.info("兴业深圳微信公众号支付通道调起成功！");
				} else {
					returnMap.put("msg", xyszWXGzhPay.get("msg"));// 失败
					returnMap.put("code", "10001");// 失败
					returnMap.put("type", "1");
					log.error("兴业深圳微信公众号支付通道调起失败！");
					// 发送邮件:失败原因
					MailRun.send(dictName, returnMap.get("msg")
							,map.get("orderNum"),map.get("rateId"),map.get("appName")
							,map.get("payMoney"),map.get("companyName"),map.get("rateCompanyName")
							,map.get("payWayName"));
				}
			}
		}
		/**
		 * 平安银行公众号支付（原生JS）
		 */
		if (dictName.equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_GZH)) 
		{
			log.info("=======>走了：平安银行特殊公众号支付通道<=======");
			String OPEN_ID = map.get("rateKey1");
			String OPEN_KEY = map.get("rateKey2");
			//公众号appid
			String sub_appid=String.valueOf(map.get("gzhAppId"));
			//用户在子商户appid下的唯一标识
			String sub_openid=String.valueOf(map.get("openid"));
			if (OPEN_ID != null && OPEN_KEY != null&&sub_appid!=null&&sub_openid!=null) {
				String outNo = map.get("orderNum");
				String pmtTag = "Weixin";
				String ordName = map.get("orderNum");
				// 金额
				String originalAmount = DecimalUtil.yuanToCents(map.get("payMoney").toString());
				// 实际金额
				String tradeAmount = DecimalUtil.yuanToCents(map.get("payMoney").toString());
				// 异步回调URL
				String notifyUrl = TestParams.NOTIFY_URL;
				// 这个参数是区别是否是公众号支付：公众号支付必传参数
				String jumpUrl =null;
				returnMap = PABankPay.queryOrder(outNo, pmtTag, null, ordName, Integer.valueOf(originalAmount), null, null, Integer.valueOf(tradeAmount), null,
						null, null, null, null, null, jumpUrl, notifyUrl, OPEN_ID, OPEN_KEY,sub_appid,sub_openid,2);
				// 成功
				if (returnMap.get("code").toString().equals("10000")) {
					returnMap.put("orderNum", outNo);
					returnMap.put("type", "2");
				} else {
					// 发送邮件:失败原因
					MailRun.send(dictName, returnMap.get("msg"), map.get("orderNum"), map.get("rateId"), map.get("appName"), map.get("payMoney"),
							map.get("companyName"), map.get("rateCompanyName"), map.get("payWayName"));
					returnMap.put("msg", returnMap.get("msg"));// 失败
					returnMap.put("code", "10001");// 失败
					returnMap.put("type", "2");
				}
			} else {
				log.error("=======>平安银行：平安银行公众号支付（原生JS）通道：相关数据库配置不完整：rateKey1，rateKey2，rateKey5,公众号APPID与用户openid有可能为空");
			}
		}
		
		return returnMap;
	}
	/**
	 * 年月日时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	/**
	 * 汇付宝：微信公众号支付回调验签
	 */
	public boolean hfbWxGzhSign(Map<String, String> map, Payv2PayOrder payv2PayOrder) throws Exception {
		boolean sign = HfbWeChatPay.hfbWxGzhSign(map, payv2PayOrder);
		return sign;
	}
}
