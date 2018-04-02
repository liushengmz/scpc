package com.pay.business.order.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradePayResponse;
import com.core.teamwork.base.service.impl.BaseServiceImpl;
import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.http.HttpUtil;
import com.core.teamwork.base.util.page.PageHelper;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.redis.RedisDBUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussCompanyShop;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.mapper.Payv2BussCompanyAppMapper;
import com.pay.business.merchant.mapper.Payv2BussCompanyMapper;
import com.pay.business.merchant.mapper.Payv2BussCompanyShopMapper;
import com.pay.business.merchant.mapper.Payv2BussSupportPayWayMapper;
import com.pay.business.merchant.mapper.Payv2ChannelMapper;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.order.entity.OrderVo;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderCountBean;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.entity.shopAppOrderVo;
import com.pay.business.order.mapper.Payv2PayGoodsMapper;
import com.pay.business.order.mapper.Payv2PayOrderClearMapper;
import com.pay.business.order.mapper.Payv2PayOrderGroupMapper;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.mapper.Payv2PayOrderRefundMapper;
import com.pay.business.order.service.Payv2PayGoodsService;
import com.pay.business.order.service.Payv2PayOrderGroupService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.entity.Payv2AppDiscount;
import com.pay.business.payv2.mapper.Pavy2PlatformAppMapper;
import com.pay.business.payv2.mapper.Payv2AppDiscountMapper;
import com.pay.business.payv2.mapper.Payv2BussAppSupportPayWayMapper;
import com.pay.business.payv2.mapper.Payv2PayUserMapper;
import com.pay.business.payv2.mapper.Payv2ProvincesCityMapper;
import com.pay.business.payv2.service.Payv2BankAppKeyService;
import com.pay.business.payv2.service.Payv2PayUserService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.entity.Payv2PayWayRate;
import com.pay.business.payway.mapper.Payv2PayWayMapper;
import com.pay.business.payway.mapper.Payv2PayWayRateMapper;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.record.entity.Payv2CompanyQuota;
import com.pay.business.record.entity.Payv2StatisticsDayChannel;
import com.pay.business.record.entity.Payv2StatisticsDayTimeBean;
import com.pay.business.record.entity.Payv2StatisticsDayWayBean;
import com.pay.business.record.mapper.Payv2CompanyQuotaMapper;
import com.pay.business.record.mapper.Payv2StatisticsDayChannelMapper;
import com.pay.business.record.mapper.Payv2StatisticsDayTimeMapper;
import com.pay.business.record.mapper.Payv2StatisticsDayWayMapper;
import com.pay.business.sysconfig.entity.SysConfigDictionary;
import com.pay.business.sysconfig.mapper.SysConfigDictionaryMapper;
import com.pay.business.sysconfig.service.SysConfigDictionaryService;
import com.pay.business.util.DecimalUtil;
import com.pay.business.util.OrderUtil;
import com.pay.business.util.ParameterEunm;
import com.pay.business.util.PayFinalUtil;
import com.pay.business.util.PayRateDictValue;
import com.pay.business.util.PaySignUtil;
import com.pay.business.util.ServiceUtil;
import com.pay.business.util.alipay.AliPay;
import com.pay.business.util.alipay.PayConfigApi;
import com.pay.business.util.guofu.GuoFuPay;
import com.pay.business.util.hfbpay.WeChatSubscrip.pay.HfbWeChatPay;
import com.pay.business.util.httpsUtil.HttpsUtil;
import com.pay.business.util.mail.MailRun;
import com.pay.business.util.minshengbank.HttpMinshengBank;
import com.pay.business.util.pinganbank.pay.PABankPay;
import com.pay.business.util.wftpay.WftWeChatPay;
import com.pay.business.util.wftpay.weChatSubscrPay.pay.SwiftWechatGzhPay;
import com.pay.business.util.wxpay.WeChatPay;
import com.pay.business.util.xyBankWeChatPay.XyBankPay;
import com.pay.business.util.xyShenzhen.XYSZBankPay;

/**
 * @author cyl
 * @version
 */
@Service("payv2PayOrderService")
public class Payv2PayOrderServiceImpl extends BaseServiceImpl<Payv2PayOrder, Payv2PayOrderMapper> implements Payv2PayOrderService {
	// 注入当前dao对象
	@Autowired
	private Payv2PayOrderMapper payv2PayOrderMapper;
	@Autowired
	private Payv2PayWayMapper payv2PayWayMapper;
	@Autowired
	private Payv2BussAppSupportPayWayMapper payv2BussAppSupportPayWayMapper;
	@Autowired
	private Payv2AppDiscountMapper payv2AppDiscountMapper;
	@Autowired
	private Payv2BussSupportPayWayMapper payv2BussSupportPayWayMapper;
	@Autowired
	private SysConfigDictionaryMapper sysConfigDictionaryMapper;
	@Autowired
	private Payv2BussCompanyAppMapper payv2BussCompanyAppMapper;
	@Autowired
	private Payv2ProvincesCityMapper payv2ProvincesCityMapper;
	@Autowired
	private Payv2ChannelMapper payv2ChannelMapper;
	@Autowired
	private Pavy2PlatformAppMapper pavy2PlatformAppMapper;
	@Autowired
	private Payv2BussCompanyShopMapper payv2BussCompanyShopMapper;
	@Autowired
	private Payv2BussCompanyMapper payv2BussCompanyMapper;
	@Autowired
	private Payv2StatisticsDayChannelMapper payv2StatisticsDayChannelMapper;
	@Autowired
	private Payv2StatisticsDayTimeMapper payv2StatisticsDayTimeMapper;
	@Autowired
	private Payv2StatisticsDayWayMapper payv2StatisticsDayWayMapper;
	@Autowired
	private Payv2PayOrderGroupMapper payv2PayOrderGroupMapper;
	@Autowired
	private Payv2PayGoodsMapper payv2PayGoodsMapper;
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;
	@Autowired
	private Payv2PayWayRateMapper payv2PayWayRateMapper;
	@Autowired
	private Payv2PayOrderRefundMapper payv2PayOrderRefundMapper;
	@Autowired
	private Payv2BankAppKeyService payv2BankAppKeyService;
	@Autowired
	private Payv2PayUserService payv2PayUserService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	@Autowired
	private SysConfigDictionaryService sysConfigDictionaryService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2PayGoodsService payv2PayGoodsService;
	@Autowired
	private Payv2PayUserMapper payv2PayUserMapper;
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
	@Autowired
	private Payv2CompanyQuotaMapper payv2CompanyQuotaMapper;
	@Autowired
	private Payv2PayOrderGroupService payv2PayOrderGroupService;
	@Autowired
	private Payv2PayOrderClearMapper payv2PayOrderClearMapper;
	public Payv2PayOrderServiceImpl() {
		setMapperClass(Payv2PayOrderMapper.class, Payv2PayOrder.class);
	}

	public Payv2PayOrder selectSingle(Payv2PayOrder t) {
		return payv2PayOrderMapper.selectSingle(t);
	}

	public List<Payv2PayOrder> selectByObject(Payv2PayOrder t) {
		return payv2PayOrderMapper.selectByObject(t);
	}

	@SuppressWarnings("unchecked")
	public PageObject<Payv2PayOrder> getPageObject(Map<String, Object> map, Integer type) {
		/*map.put("sortName", "create_time");
		map.put("sortOrder", "DESC");*/
		long start = System.currentTimeMillis();
		int totalData = payv2PayOrderMapper.getCount2(map);
		PageHelper helper = new PageHelper(totalData, map);
		
		List<Payv2PayOrder> orderList = payv2PayOrderMapper.pageQueryByObject2(helper.getMap());
		if(1==type && orderList.size()>0){
			List<Map<String, Object>> refundInfo = new ArrayList<Map<String,Object>>();
			if(totalData>100){
				String ids = "";
				for (int i = 0; i < orderList.size(); i++) {
					ids+=orderList.get(i).getId() + "";
					if(i <= orderList.size()-2){
						ids+=",";
					}
				}
				refundInfo = payv2PayOrderMapper.getRefundInfo(ids);
			}else {
				refundInfo = payv2PayOrderMapper.getRefundInfo(null);
			}
			for (Map<String, Object> maps : refundInfo) {
				try {
					Long orderId = Long.valueOf(maps.get("order_id").toString());
					for (Payv2PayOrder order : orderList) {
						if(order.getId()==orderId){
							order.setRefundMoney(Double.valueOf(maps.get("money").toString()));
							
						}
					}
				} catch (Exception e) {
					continue;
				}
				
			}
		}
		
		PageObject<Payv2PayOrder> pageList = helper.getPageObject();
		pageList.setDataList(orderList);
		long end = System.currentTimeMillis();
		System.out.println("sql耗时："+(end-start));
		return pageList;
	}
	
	
	public List<Payv2PayOrder> getImportOrder(Map<String, Object> map) {
		return payv2PayOrderMapper.getImportOrder(map);
	}
	
	/**
	 * 向上取整
	 * @param d
	 * @param n 保留位数
	 * @return
	 */
	public double getCeil(double d,int n){
		BigDecimal b = new BigDecimal(String.valueOf(d));
		b = b.divide(BigDecimal.ONE,n,BigDecimal.ROUND_CEILING);
		return b.doubleValue();
	}

	/**
	 * 根据店铺查询渠道id
	 * 
	 * @param pbca
	 * @return
	 */
	private Long getChannel(Payv2BussCompanyShop pbcs) {
		Payv2BussCompany pbc = new Payv2BussCompany();
		pbc.setId(pbcs.getCompanyId());
		pbc = payv2BussCompanyMapper.selectSingle(pbc);
		if (pbc != null) {
			return pbc.getChannelId();
		} else {
			return null;
		}
	}

	/**
	 * 获取通道费率
	 * 
	 * @param appId
	 * @param payWayId
	 * @param type
	 * @return
	 */
	private Double getPayWayRate(Long companyId, Long payWayId, Long rateId) {
		Payv2BussSupportPayWay pbspw = new Payv2BussSupportPayWay();
		pbspw.setParentId(companyId);
		pbspw.setPayWayId(payWayId);
		pbspw.setRateId(rateId);
		pbspw.setIsDelete(2);
		pbspw = payv2BussSupportPayWayService.selectSingle1(pbspw);
		if (pbspw != null) {
			return pbspw.getPayWayRate();
		}
		return 0.00;
	}

	/**
	 * 支付详情（保存用户，查询支付方式信息） h5网站支付
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> payOrderH5Detail(Map<String, Object> map, Payv2BussCompanyApp pbca) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Integer rateType = 2; // web
		Double payMoney = Double.valueOf(map.get("payMoney").toString());
		Long companyId = pbca.getCompanyId();
		// 创建订单
		Payv2PayOrder payOrder = new Payv2PayOrder();

		payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString());
		payOrder.setAppId(pbca.getId());
		
		payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		if(payOrder!=null){
			if(payOrder.getPayStatus()!=1 && payOrder.getPayStatus()!=5){
				payOrder.setReturnUrl(map.get("returnUrl") == null ? null : map.get("returnUrl").toString()); // 回调的页面
				payOrder.setNotifyUrl(map.get("notifyUrl").toString()); // 回调地址（服务器）
				payv2PayOrderMapper.updateUser(payOrder);
			}else{
				// 支付已完成
				resultMap.put("status", PayFinalUtil.PAY_STATUS_FAIL_OK);
				return resultMap;
			}
		}else{
			payOrder = new Payv2PayOrder();
			// 创建订单
			payOrder = createOrder(map, payOrder, pbca, payMoney, rateType);
		}

		// 钱包中最高优惠
		double maxDiscountMoney = 0;
		resultMap.put("maxDiscountMoney", maxDiscountMoney);
		resultMap.put("payMoney", payMoney);
		resultMap.put("orderName", payOrder.getOrderName());
		// 返回的第三方支付信息
		List<Payv2PayWay> otherList = payv2PayWayService.getWalletConfig(payMoney, pbca.getCompanyId()
				, 2,rateType);
		//过滤封装后的支付方式
		List<Payv2PayWay> duplicateList = getResultList(otherList, payMoney, companyId, rateType);
		resultMap.put("otherList", duplicateList);
		// 返回的订单信息
		resultMap.put("orderNum", payOrder.getOrderNum());
		resultMap.put("status", PayFinalUtil.PAY_STATUS_SUSSESS);
		return resultMap;
	}

	/**
	 * 支付详情（保存用户，查询支付方式信息） 线下扫码支付
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> payOrderShopDetail(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		Payv2BussCompanyShop pbcs = new Payv2BussCompanyShop();
		pbcs.setShopKey(map.get("shopKey").toString());
		// 店铺审核通过状态
		pbcs.setShopStatus(2);
		// 未删除状态
		pbcs.setIsDelete(2);
		// 根据shopKey查询应用
		pbcs = payv2BussCompanyShopMapper.selectSingle(pbcs);
		if (pbcs != null) {
			Payv2PayWay ppw = new Payv2PayWay();
			ppw.setId(pbcs.getPayWayId());
			ppw.setStatus(1);
			ppw.setIsDelete(2);
			ppw = payv2PayWayMapper.selectSingle(ppw); // 店铺支持的钱包
			if (ppw != null) {
				//String codeWayId = pbcs.getCodeWayId(); // 店铺支持二维码通道
				/*
				 * if(codeWayId.contains("1")){ //店铺支持二维码通道包含扫码的
				 * if(ppw.getId()!=1){ Payv2PayWay p = new Payv2PayWay();
				 * p.setId(1); p.setStatus(1); p.setIsDelete(2); p =
				 * payv2PayWayMapper.selectSingle(p); resultMap.put("other", p);
				 * } resultMap.put("wallet", ppw); }
				 */
			}
		}
		return resultMap;
	}

	/**
	 * 创建订单
	 * 
	 * @param map
	 * @param payOrder
	 * @param pbca
	 * @param payUser
	 * @param payMoney
	 * @return
	 */
	private Payv2PayOrder createOrder(Map<String, Object> map, Payv2PayOrder payOrder, Payv2BussCompanyApp pbca, Double payMoney,Integer rateType) {

		payOrder.setRateType(rateType);
		// 保存商品(先查询是否存在，没有就保存！返回商品id)
		Long goodsId = payv2PayGoodsService.saveGoods(pbca.getId(), map.get("orderName").toString(), 1);
		payOrder.setGoodsId(goodsId);
		// 获取订单分组id(先查询是否存在，没有就保存！订单分组id)
		Long groupId = payv2PayOrderGroupService.getGroup(map, pbca);
		//Long channelId = payv2BussCompanyService.getChannel(pbca.getCompanyId());
		payOrder.setGroupId(groupId);
		payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString()); // 商家订单号
		payOrder.setChannelId(pbca.getChannelId()); // 渠道商id
		payOrder.setAppId(pbca.getId());
		payOrder.setCompanyId(pbca.getCompanyId()); // 商户id
		payOrder.setCreateTime(new Date());
		payOrder.setOrderName(map.get("orderName") != null ? map.get("orderName").toString() : null); // 订单名称/商品名称
		payOrder.setOrderDescribe(map.get("orderDescribe") != null ? map.get("orderDescribe").toString() : null); // 订单描述信息
		payOrder.setPayMoney(payMoney);
		payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_NOT); // 未支付

		payOrder.setOrderNum(OrderUtil.getOrderNum()); // 订单生产（规则？）
		payOrder.setReturnUrl(map.get("returnUrl") == null ? null : map.get("returnUrl").toString()); // 回调的页面
		payOrder.setNotifyUrl(map.get("notifyUrl").toString()); // 回调地址（服务器）
		if(map.get("remark")!=null){
			payOrder.setRemark(map.get("remark").toString());
		}
		payv2PayOrderMapper.insertByEntity(payOrder);
		return payOrder;
	}
	/**
	 * 创建订单(提供点游)
	 * 
	 * @param map
	 * @param payOrder
	 * @param pbca
	 * @param payUser
	 * @param payMoney
	 * @return
	 */
	public Payv2PayOrder createOrder(Map<String, Object> map, Payv2PayOrder payOrder, Payv2BussCompanyApp pbca, Double payMoney,
			Long payWayId, Long rateId,Double companyRate,Double costRate) {
		// 保存商品(先查询是否存在，没有就保存！返回商品id)
		Long goodsId = payv2PayGoodsService.saveGoods(pbca.getId(), map.get("orderName").toString(), 1);
		payOrder.setGoodsId(goodsId);
		// 获取订单分组id(先查询是否存在，没有就保存！订单分组id)
		Long groupId = payv2PayOrderGroupService.getGroup(map, pbca);
		//Long channelId = payv2BussCompanyService.getChannel(pbca.getCompanyId());
		payOrder.setGroupId(groupId);
		payOrder.setPayWayId(payWayId);
		payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString()); // 商家订单号
		payOrder.setChannelId(pbca.getChannelId()); // 渠道商id
		payOrder.setAppId(pbca.getId());
		payOrder.setCompanyId(pbca.getCompanyId()); // 商户id
		payOrder.setCreateTime(new Date());
		payOrder.setOrderName(map.get("orderName") != null ? map.get("orderName").toString() : null); // 订单名称/商品名称
		payOrder.setOrderDescribe(map.get("orderDescribe") != null ? map.get("orderDescribe").toString() : null); // 订单描述信息
		payOrder.setPayMoney(payMoney);
		
		payOrder.setRateId(rateId);
		// 通道费用
		payOrder.setPayWayMoney(getPayWayMoney(payMoney, companyRate));
		//费率
		payOrder.setBussWayRate(companyRate);
		//成本费率
		payOrder.setCostRate(costRate);
		//成本手续费
		payOrder.setCostRateMoney(getPayWayMoney(payMoney, companyRate));
		payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_NOT); // 未支付
		// userId和商户订单组成
		payOrder.setOrderNum(OrderUtil.getOrderNum()); // 订单生产（规则？）
		payOrder.setReturnUrl(map.get("returnUrl") == null ? null : map.get("returnUrl").toString()); // 回调的页面
		payOrder.setNotifyUrl(map.get("notifyUrl").toString()); // 回调地址（服务器）
		if(map.get("remark")!=null){
			payOrder.setRemark(map.get("remark").toString());
		}
		payv2PayOrderMapper.insertByEntity(payOrder);
		return payOrder;
	}

	/**
	 * 支付参数签名
	 * 
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Map<String, String> payForOrder(Map<String, Object> map) throws Exception {
		Map<String, String> resultMap = new HashMap<>();
		Payv2PayOrder payOrder = new Payv2PayOrder();
		payOrder.setOrderNum(map.get("orderNum").toString());
		payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		if (payOrder != null) {
			if (payOrder.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS && payOrder.getPayStatus() != 5) {
				
				Long payWayId = Long.valueOf(map.get("payWayId").toString());
				
				//查询商户
				Payv2BussCompany pbc = payv2BussCompanyService.getComById(payOrder.getCompanyId());
				
				//根据支付方式和商户查询商户支持支付通道
				List<Payv2BussSupportPayWay> wayList= payv2BussSupportPayWayService.queryByCompany(
						payOrder.getCompanyId(), payWayId, payOrder.getRateType(),payOrder.getPayMoney());
				
				//路由获取支付方式、通道
				Payv2BussSupportPayWay pbspw = payv2BussSupportPayWayService.getWayRate(wayList
						, payOrder.getPayMoney(), payOrder.getCompanyId(),payWayId.intValue(),pbc.getIsRandom(),1);
				
				if (pbspw == null) {
					resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
					return resultMap;
				}
				// 优惠后的需付款的金额
				Double lastPayMoeny = payOrder.getPayMoney();
				Map<String,String> paramMap = new HashMap<>();
				paramMap.put("orderNum", payOrder.getOrderNum());
				paramMap.put("orderName", payOrder.getOrderName());
				paramMap.put("payMoney", ""+lastPayMoeny);
				paramMap.put("rateKey1", pbspw.getRateKey1());
				paramMap.put("rateKey2", pbspw.getRateKey2());
				paramMap.put("rateKey3", pbspw.getRateKey3());
				paramMap.put("rateKey4", pbspw.getRateKey4());
				
				//调用支付通道
				resultMap = PayRateDictValue.ratePay(pbspw.getDictName(), paramMap, map.get("ip").toString()
						,map.get("appType").toString(),payOrder.getRateType());
				
				if(PayFinalUtil.PAYORDER_QUERY_FAIL.equals(resultMap.get("status"))){
					logger.debug("参数不能为空,或者有误");
					return resultMap;
				}
				// 支付通道路由id
				payOrder.setRateId(pbspw.getRateId());
				// 通道费
				payOrder.setPayWayMoney(getPayWayMoney(lastPayMoeny, pbspw.getPayWayRate()));
				// 订单选择的支付通道更新
				payOrder.setPayWayId(payWayId);
				
				//费率
				payOrder.setBussWayRate(pbspw.getPayWayRate());
				//成本费率
				payOrder.setCostRate(pbspw.getCostRate());
				//成本手续费
				payOrder.setCostRateMoney(getPayWayMoney(lastPayMoeny, pbspw.getCostRate()));
				
				payv2PayOrderMapper.updateByEntity(payOrder);
				return resultMap;
			} else {// 订单已支付
				logger.debug("参数不能为空,或者有误");
				resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
			}
		} else {
			logger.debug("参数不能为空,或者有误");
			resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
		}
		return resultMap;
	}

	/**
	 * 支付宝回调操作
	 * 
	 * @param map
	 * @param createSign
	 */
	public boolean aliPayCallBack(Map<String, String> params,Payv2PayOrder order) throws Exception {
		//String out_trade_no = params.get("out_trade_no");// 商户订单号
		String trade_status = params.get("trade_status");// 交易状态
		String trade_no = params.get("trade_no"); // 支付宝交易号
		String gmt_payment = params.get("gmt_payment"); // 支付宝交易时间
		if ("TRADE_SUCCESS".equals(trade_status)) {
//			Payv2PayOrder order = new Payv2PayOrder();
//			order.setOrderNum(out_trade_no);
//			order = payv2PayOrderMapper.selectSingle(order);
			if (order != null && order.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS) {
				// 查询商家app 获取回调地址
				Payv2BussCompanyApp pbca = new Payv2BussCompanyApp();
				pbca.setId(order.getAppId());
				pbca = payv2BussCompanyAppMapper.selectSingle(pbca);

				// 交易支付成功
				String total_amount = params.get("total_amount").toString();// 支付金额

				Double payDiscountMoney = NumberUtils.createDouble(total_amount);
				// order.setPayType(PayFinalUtil.PAYTYPE_ORDER_ALIPAY); //支付类型
				// 支付宝
				
				Date date = getDate1(gmt_payment);
				
				order.setBankTransaction(trade_no);
				order.setPayTime(date);
				order.setCreateTime(date);
				order.setPayDiscountMoney(payDiscountMoney);
				order.setDiscountMoney(order.getPayMoney() - payDiscountMoney);
				// order.setOtherOrderNum(trade_no);

				// 通知商户
				return notifyMerchant(order, pbca);
				/*
				 * final Payv2PayOrder order1 = order; final Payv2BussCompanyApp
				 * pbca1 = pbca;
				 * 
				 * //RedisDBUtil.redisDao.setString(order.getOrderNum(),
				 * "1000"); new Thread(new Runnable() {
				 * 
				 * @Override public void run() { boolean con = false; do{ try {
				 * //String numStr =
				 * RedisDBUtil.redisDao.get(order1.getOrderNum()); con =
				 * notifyMerchant1(order1, pbca1); if(con){ //int num =
				 * Integer.valueOf(numStr);
				 * //RedisDBUtil.redisDao.setString(order1.getOrderNum(), (num +
				 * 2000)+""); Thread.sleep(300000); } } catch (Exception e) {
				 * e.printStackTrace(); } }while(con); } }).start();
				 */
			}
		} else {
			logger.debug("支付集-支付宝回调暂不处理的交易状态:" + trade_status);
		}
		return true;
	}

	/**
	 * 支付宝回调操作(点游)
	 * 
	 * @param map
	 * @param createSign
	 */
	public boolean dyAliPayCallBack(Map<String, String> params,Payv2PayOrder order) throws Exception {
		//String out_trade_no = params.get("out_trade_no");// 商户订单号
		String trade_status = params.get("trade_status");// 交易状态
		String trade_no = params.get("trade_no"); // 支付宝交易号
		String gmt_payment = params.get("gmt_payment");//支付宝支付时间
		if ("TRADE_SUCCESS".equals(trade_status)) {
			if (order != null && order.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS) {
				// 查询商家app 获取回调地址
				Payv2BussCompanyApp pbca = new Payv2BussCompanyApp();
				pbca.setId(order.getAppId());
				pbca = payv2BussCompanyAppMapper.selectSingle(pbca);

				// 交易支付成功
				String total_amount = "0";
				total_amount = params.get("total_amount").toString();// 支付金额

				Double payDiscountMoney = NumberUtils.createDouble(total_amount);
				// order.setPayType(PayFinalUtil.PAYTYPE_ORDER_ALIPAY); //支付类型
				// 支付宝
				
				//支付时间取支付宝支付时间
				Date date = getDate1(gmt_payment);
				order.setBankTransaction(trade_no);
				order.setPayTime(date);
				order.setCreateTime(date);
				order.setPayDiscountMoney(payDiscountMoney);
				order.setDiscountMoney(order.getPayMoney() - payDiscountMoney);
				// order.setOtherOrderNum(trade_no);

				// 通知商户
				return notifyMerchant(order, pbca);
			}
		} else {
			logger.debug("支付集-支付宝回调暂不处理的交易状态:" + trade_status);
		}
		return true;
	}

	/**
	 * 通知商户
	 * 
	 * @param order
	 * @param pbca
	 * @throws Exception
	 */
	private boolean notifyMerchant(Payv2PayOrder order, Payv2BussCompanyApp pbca) throws Exception {
		Long companyId=order.getCompanyId();
		Long rateId=order.getRateId();
		Date as = new Date();
		String time = DateUtil.DateToString(as, "yyyyMMdd");
		String companyRedisKey=companyId+"CID"+rateId+"RID"+time;
		//将统计订单号存入redis:以防重复统计
		String TJRedis="TJ"+order.getOrderNum();
		String TJRedisValue=RedisDBUtil.redisDao.getString(TJRedis);
		if(TJRedisValue==null){
			//商户每天限额
			Map<String,String>companyMap = RedisDBUtil.redisDao.getStringMapAll(companyRedisKey);
			if(companyMap!=null&&companyMap.containsKey("companyTotalMoney")){
				double companyTotalMoney=Double.valueOf(companyMap.get("companyTotalMoney"));
				companyTotalMoney=companyTotalMoney+order.getPayDiscountMoney();
				
				BigDecimal b = new BigDecimal(companyTotalMoney);
				companyTotalMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				companyMap.put("companyTotalMoney",String.valueOf( companyTotalMoney));
				RedisDBUtil.redisDao.hmset(companyRedisKey, companyMap);
			}
			//官方的每天限额
			String redisKey=rateId+"RID"+time;
			Map<String,String> map = RedisDBUtil.redisDao.getStringMapAll(redisKey);
			if(map!=null&&map.containsKey("totalMoney")){
				double totalMoney=Double.valueOf(map.get("totalMoney"));
				totalMoney=totalMoney+order.getPayDiscountMoney();
				BigDecimal b = new BigDecimal(totalMoney);
				totalMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				map.put("totalMoney",String.valueOf( totalMoney));
				RedisDBUtil.redisDao.hmset(redisKey, map);
			}
			//每天实时流水总额存入redis:以每天为redis-key
			Map<String,String> dateMap = RedisDBUtil.redisDao.getStringMapAll(time);
			if(dateMap!=null&&dateMap.containsKey("dateMoney")){
				double dateMoney=Double.valueOf(dateMap.get("dateMoney"));
				dateMoney=dateMoney+order.getPayDiscountMoney();
				BigDecimal b = new BigDecimal(dateMoney);
				dateMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				dateMap.put("dateMoney",String.valueOf(dateMoney));
				//订单总数记录
				if(dateMap.containsKey("dateCount")){
					int dateCount=Integer.valueOf(dateMap.get("dateCount"));
					dateCount=dateCount+1;
					dateMap.put("dateCount",String.valueOf(dateCount));
				}else{
					dateMap.put("dateCount","1");
				}
				RedisDBUtil.redisDao.hmset(time, dateMap);
			}else{
				dateMap.put("dateMoney",String.valueOf(order.getPayDiscountMoney()));
				RedisDBUtil.redisDao.hmset(time, dateMap);
			}
			//----------------------------------------APP移动统计--------------------------//
			//1:根据通道ID+时间存钱：redis
			//添加区别以上存储的redis的key
			int redisType=1;
			double money=order.getPayDiscountMoney();
			String rateRedisKey=rateId+"RID"+time+"TYPE"+redisType;
			String rateValue=RedisDBUtil.redisDao.getString(rateRedisKey);
			if(null!=rateValue){
				double rateMoney=Double.valueOf(rateValue);
				rateMoney=rateMoney+money;
				BigDecimal b = new BigDecimal(rateMoney);
				rateMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				//86405失效
				RedisDBUtil.redisDao.setString(rateRedisKey,String.valueOf(rateMoney), 86405);
			}else{
				//直接添加到缓存里面
				RedisDBUtil.redisDao.setString(rateRedisKey,String.valueOf(money), 86405);
			}
			
			//2:根据商户id+时间存钱:redis
			String companyRedisKey1=companyId+"CID"+time+"TYPE"+redisType;
			String companyValue=RedisDBUtil.redisDao.getString(companyRedisKey1);
			if(null!=companyValue){
				double companyMoney=Double.valueOf(companyValue);
				companyMoney=companyMoney+money;
				BigDecimal b = new BigDecimal(companyMoney);
				companyMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				//86405失效
				RedisDBUtil.redisDao.setString(companyRedisKey1,String.valueOf(companyMoney), 86405);
			}else{
				//直接添加到缓存里面
				RedisDBUtil.redisDao.setString(companyRedisKey1,String.valueOf(money), 86405);
			}
			//3:根据商户ID+通道ID+时间存钱:redis
			String companyIdAndRateIdKey=companyId+"CID"+rateId+"RID"+time+"TYPE"+redisType;
			String companyIdAndRateValue=RedisDBUtil.redisDao.getString(companyIdAndRateIdKey);
			if(null!=companyIdAndRateValue){
				double companyIdAndRateMoney=Double.valueOf(companyIdAndRateValue);
				companyIdAndRateMoney=companyIdAndRateMoney+money;
				BigDecimal b = new BigDecimal(companyIdAndRateMoney);
				companyIdAndRateMoney = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				//86405失效
				RedisDBUtil.redisDao.setString(companyIdAndRateIdKey,String.valueOf(companyIdAndRateMoney), 86405);
			}else{
				//直接添加到缓存里面
				RedisDBUtil.redisDao.setString(companyIdAndRateIdKey,String.valueOf(money), 86405);
			}
			//如果TJRedisValue为null：将此订单号存入redis:以此来判断值在下一次回调来判断是否有过统计记录：统计记录原则只统计一次
			RedisDBUtil.redisDao.setString(TJRedis,TJRedis,86400);
			//----------------------------------------------------end--------------------------------------------------//
		}
		//业务处理
		Map<String, Object> param = new HashMap<>();
		param.put("result_code", ""+PayFinalUtil.SUSSESS_CODE); // 成功
		param.put("order_num", order.getOrderNum()); // 订单
		param.put("buss_order_num", order.getMerchantOrderNum()); // 商户订单
		param.put("pay_money", ""+order.getPayMoney()); // 支付金额
		param.put("pay_discount_money", ""+order.getPayDiscountMoney()); // 最终支付金额
		param.put("pay_time", DateStr(order.getCreateTime())); // 支付时间
															// yyyyMMddHHmmss
		//商户不传或传空，回调不返回
		if(order.getRemark()!=null&&!"".equals(order.getRemark())){
			param.put("remark", order.getRemark()); // 自定义字段
		}
		
		// 参数签名
		String sign = PaySignUtil.getSign(param, pbca.getAppSecret());
		param.put("sign", sign);
		
		String notifyUrl=order.getNotifyUrl() == null ? pbca.getCallbackUrl() : order.getNotifyUrl();
		
		System.out.println("notifyUrl:" + notifyUrl + ";param:" + param);
		
		//新接入商户回调方式
		if(pbca.getIsNew()==1){
			String result = "";
			if(notifyUrl.startsWith("https")){
				result = HttpsUtil.doPostString(notifyUrl, param, "utf-8");
			}else{
				// 通知商户
				result = HttpUtil.httpPost(notifyUrl, param);
			}
			
			result = result.trim().toUpperCase();
			
			System.out.println("回调商户接收："+result);
			// 通知商户请求失败
			if (!"SUCCESS".equals(result.toUpperCase())) {
				order.setCallbackTime(new Date());
				order.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS_BACKFAIL); // 支付成功回调失败
				payv2PayOrderMapper.updateByEntity(order);
				logger.info("=========》通知商户接口访问失败，已启动重新传送机制");
				
				final String orderNo = order.getOrderNum();
				
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						ServiceUtil.sendGet("http://hpay.iquxun.cn/Pay_CallBack.ashx?order_num=" + orderNo, null, null);
					}
				}).start();
				
				return true;
			} else {
				order.setCallbackTime(new Date());
				order.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS); // 支付成功
				payv2PayOrderMapper.updateByEntity(order);
				logger.info("========》通知商户接口访问成功");
				return true;
			}
		}else{
			Integer code = 200;
			if(notifyUrl.startsWith("https")){
				code = HttpsUtil.doPost(notifyUrl, param, "utf-8");
			}else{
				// 通知商户
				code = HttpUtil.getCode(notifyUrl, param);
			}
			System.out.println("回调商户接收http状态："+code);
			// 通知商户请求失败
			if (code != 200) {
				order.setCallbackTime(new Date());
				order.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS_BACKFAIL); // 支付成功回调失败
				payv2PayOrderMapper.updateByEntity(order);
				logger.info("=========》通知商户接口访问失败");
				return false;
			} else {
				order.setCallbackTime(new Date());
				order.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS); // 支付成功
				payv2PayOrderMapper.updateByEntity(order);
				logger.info("========》通知商户接口访问成功");
				return true;
			}
		}
	}

	private String DateStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 支付宝网站h5支付 获取表单
	 * 
	 * @param map
	 * @return
	 * @throws AlipayApiException
	 */
	public Map<String, String> payH5Alipay(Map<String, Object> map) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();

		Payv2PayOrder payOrder = new Payv2PayOrder();
		payOrder.setOrderNum(map.get("orderNum").toString());
		payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		if (payOrder != null) {
			if (payOrder.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS && payOrder.getPayStatus() != 5) {
				Long payWayId = Long.valueOf(map.get("payWayId").toString());
				
				Payv2BussCompany pbc = payv2BussCompanyService.getComById(payOrder.getCompanyId());
				
				//商户支持的支付方式、通道
				List<Payv2BussSupportPayWay> wayList= payv2BussSupportPayWayService.queryByCompany(
						payOrder.getCompanyId(), payWayId, payOrder.getRateType(),payOrder.getPayMoney());
				
				//路由获取支付方式、通道
				Payv2BussSupportPayWay pbspw = payv2BussSupportPayWayService.getWayRate(wayList, payOrder.getPayMoney()
						, payOrder.getCompanyId(), payWayId.intValue(),pbc.getIsRandom(),2);
				
				if (pbspw == null) {
					logger.debug("参数不能为空,或者有误");
					resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
					return resultMap;
				}
				
				// 优惠后的需付款的金额
				Double lastPayMoeny = payOrder.getPayMoney();
				Map<String,String> paramMap = new HashMap<>();
				paramMap.put("orderNum", payOrder.getOrderNum());
				paramMap.put("orderName", payOrder.getOrderName());
				paramMap.put("payMoney", ""+lastPayMoeny);
				paramMap.put("returnUrl", payOrder.getReturnUrl() == null ? 
						PayConfigApi.H5_RETURN_URL : payOrder.getReturnUrl());
				paramMap.put("rateKey1", pbspw.getRateKey1());
				paramMap.put("rateKey2", pbspw.getRateKey2());
				paramMap.put("rateKey3", pbspw.getRateKey3());
				paramMap.put("rateKey4", pbspw.getRateKey4());
				
				//调用支付通道
				resultMap = PayRateDictValue.ratePay(pbspw.getDictName(), paramMap, map.get("ip").toString()
						,map.get("appType").toString(),payOrder.getRateType());
				
				if(PayFinalUtil.PAYORDER_QUERY_FAIL.equals(resultMap.get("status"))){
					logger.debug("参数不能为空,或者有误");
					return resultMap;
				}
				
				// 支付通道路由id
				payOrder.setRateId(pbspw.getRateId());
				// 通道费
				payOrder.setPayWayMoney(getPayWayMoney(lastPayMoeny, pbspw.getPayWayRate()));
				// 订单选择的支付通道更新
				payOrder.setPayWayId(payWayId);
				
				//费率
				payOrder.setBussWayRate(pbspw.getPayWayRate());
				//成本费率
				payOrder.setCostRate(pbspw.getCostRate());
				//成本手续费
				payOrder.setCostRateMoney(getPayWayMoney(lastPayMoeny, pbspw.getCostRate()));
				
				payv2PayOrderMapper.updateByEntity(payOrder);
			} else {// 订单已支付
				logger.debug("参数不能为空,或者有误");
				resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
			}
		} else {
			logger.debug("参数不能为空,或者有误");
			resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
		}

		return resultMap;
	}

	/**
	 * 查询订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryOrderNum(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		
		Payv2PayOrder payOrder = new Payv2PayOrder();
		// 商户传了支付集订单号
		if (map.containsKey("orderNum")) {
			payOrder.setOrderNum(map.get("orderNum").toString());
			payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		} else {// 商户传了商户订单号
			Payv2BussCompanyApp pbca = new Payv2BussCompanyApp();
			pbca.setAppKey(map.get("appKey").toString());
			pbca = payv2BussCompanyAppMapper.selectSingle(pbca);
			if(pbca!=null){
				payOrder.setAppId(pbca.getId());
			}
			payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString());
			payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		}
		
		if (payOrder != null) {
			// 支付成功状态
			if (payOrder.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS && payOrder.getPayStatus() != 5) {
				if (payOrder.getRateId() != null) {
					// 查询支付的通道
					Map<String, Object> param = new HashMap<>();
					param.put("id", payOrder.getRateId());
					Payv2PayWayRate ppwr = payv2PayWayRateService.detail(param);
					
					if (ppwr == null) {
						resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
						return resultMap;
					}
					
					resultMap.put("payOrder", payOrder);
					if (payOrder.getPayStatus() == 3) {
						resultMap.put("status", "2"); // 未支付
					} else {
						resultMap.put("status", "4"); // 支付失败
					}
					
				} else {
					resultMap.put("payOrder", payOrder);
					logger.debug("订单未支付");
				}
			} else {
				resultMap.put("payOrder", payOrder);
				logger.debug("订单支付成功");
			}
		} else {
			resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
			logger.debug("参数不能为空,或者有误");
		}

		return resultMap;
	}

	/**
	 * 查询订单
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryOrder(Map<String, Object> map, Payv2BussCompanyApp pbca) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		Payv2PayOrder payOrder = new Payv2PayOrder();
		// 商户传了支付集订单号
		if (map.containsKey("orderNum")) {
			payOrder.setOrderNum(map.get("orderNum").toString());
			payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		} else {// 商户传了商户订单号
			payOrder.setAppId(pbca.getId());
			payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString());
			payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		}
		if (payOrder != null) {
			// 支付成功
			if (payOrder.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS && payOrder.getPayStatus() != 5) {
				// 支付方式
				if (payOrder.getRateId() != null) {
					
					Payv2PayWayRate ppwr = payv2PayWayRateService.queryByid(payOrder.getRateId());
					
					if (ppwr == null) {
						resultMap.put("status", PayFinalUtil.PAYORDER_QUERY_FAIL);
						return resultMap;
					}
					
					resultMap.put("payOrder", payOrder);
					if (payOrder.getPayStatus() == 3) {
						resultMap.put("status", "2"); // 未支付
					} else {
						resultMap.put("status", "4"); // 支付失败
					}
					// 支付宝
					if (ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_APP) 
							|| ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_SCAN) 
							|| ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_WEB)
							|| ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY)
							|| ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_SMZL)) {

						String signType = "RSA";
						/**
						 * 支付宝订单查询
						 */
						if(ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_ALIPAY_SMZL)){
							signType = "RSA2";
						}
						JSONObject result = AliPay.alipayQuery(payOrder.getOrderNum()
								,ppwr.getRateKey1(),ppwr.getRateKey2(),ppwr.getRateKey3(),signType);

						if (result.get("code").toString().equals("10000")) {
							String trade_status= result.get("trade_status").toString();
							if("TRADE_SUCCESS".equals(trade_status)||"TRADE_FINISHED".equals(trade_status)){
								//String out_trade_no = result.get("out_trade_no").toString();// 商户订单号
								//String trade_status = result.get("trade_status").toString();// 交易状态
								String total_amount = result.get("total_amount").toString();// 支付金额
								
								// 订单更新
								payOrder.setPayDiscountMoney(Double.valueOf(total_amount));
								payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
								resultMap.put("payOrder", payOrder);
								payv2PayOrderMapper.updateByEntity(payOrder);
								resultMap.put("status", "1"); // 支付成功
							}
						} 
					} 
					//兴业深圳订单查询
					else if (PayRateDictValue.PAY_TYPE_XYSZ_ALI_SCAN.equals(ppwr.getDictName()) 
							|| PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_SCAN.equals(ppwr.getDictName())
							|| PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_WAP.equals(ppwr.getDictName()) 
							|| PayRateDictValue.PAY_TYPE_XYSZ_WEIXIN_GZH.equals(ppwr.getDictName())) {
						Map<String, String> queryOrder = XYSZBankPay.queryOrder(payOrder.getOrderNum() , ppwr.getRateKey1(), ppwr.getRateKey2());
						if (queryOrder.containsKey("out_trade_no")) {
							String total_fee = queryOrder.get("total_fee");// 支付金额
							// 订单更新
							payOrder.setPayDiscountMoney(Double.valueOf(total_fee));
							payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
							resultMap.put("payOrder", payOrder);
							payv2PayOrderMapper.updateByEntity(payOrder);
							resultMap.put("status", "1"); // 支付成功
						}
					}
					//国付订单查询
					else if (PayRateDictValue.PAY_TYPE_GUOFU_PASSIVE_QQ_SCAN.equals(ppwr.getDictName())) {
						Map<String, String> qrcodeQuery = GuoFuPay.qrcodeQuery(ppwr.getRateKey1(), payOrder.getOrderNum(), ppwr.getRateKey2());
						if ("10000".equals(qrcodeQuery.get("code"))) {
							String total_fee = qrcodeQuery.get("total_fee");// 支付金额
							// 订单更新
							payOrder.setPayDiscountMoney(Double.valueOf(total_fee));
							payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
							resultMap.put("payOrder", payOrder);
							payv2PayOrderMapper.updateByEntity(payOrder);
							resultMap.put("status", "1"); // 支付成功
						}
					}
					/**
					 * 威富通微信wap支付订单查询
					 */
					else if (ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_WFT_WEIXIN)) {
						
						Map<String, String> wechatMap = WftWeChatPay.queryOrder(payOrder.getOrderNum()
								,ppwr.getRateKey1(),ppwr.getRateKey2());
						if (wechatMap.containsKey("out_trade_no")) {
							//String out_trade_no = wechatMap.get("out_trade_no");// 商户订单号
							// String trade_status =
							// wechatMap.get("trade_state");//交易状态
							String total_fee = wechatMap.get("total_fee");// 支付金额
							//String transaction_id = wechatMap.get("transaction_id");// 威富通订单
							// 订单更新
							payOrder.setPayDiscountMoney(Double.valueOf(total_fee));
							payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
							resultMap.put("payOrder", payOrder);
							payv2PayOrderMapper.updateByEntity(payOrder);
							resultMap.put("status", "1"); // 支付成功
						} 
					}
					/**
					 * 汇付宝：微信公众号支付：订单查询
					 */
					else if(ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_HFB_WEIXIN_GZH)){
						String agent_id=ppwr.getRateKey1();
						String key=ppwr.getRateKey2();
						if(agent_id!=null&&key!=null){
							String agent_bill_id=payOrder.getOrderNum();
							String agent_bill_time=DateUtil.DateToString(new Date(), "yyyyMMddHHmmss");
							String remark="HFBWXGZHORDERQUERY";
							Map<String, String> wechatMap=HfbWeChatPay.selectHfbWeChatGzhPayOrder(agent_id, agent_bill_id, key, agent_bill_time, remark);
							if(wechatMap.get("code").equals("10000")){
								if (wechatMap.get("payStatus").equals("1")){
									// 订单更新
									payOrder.setPayDiscountMoney(payOrder.getPayMoney());
									payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
									resultMap.put("payOrder", payOrder);
									payv2PayOrderMapper.updateByEntity(payOrder);
									resultMap.put("status", "1"); // 支付成功
								}
							}else if(wechatMap.get("code").equals("10001")){
								//查询接口失败
								resultMap.put("status", "2");
							}
						}else{
							logger.error("汇付宝：微信公众号支付：订单查询失败：请检查数据库配置参数完整--》RateKey1,RateKey2");
						}
					}
					/**
					 * 兴业银行微信公众号，微信扫码支付：订单查询
					 */
					else if(ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_GZH)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_WEIXIN_SCAN)){
						String appid=ppwr.getRateKey1();
						String mch_id=ppwr.getRateKey2();
						String key=ppwr.getRateKey3();
						if(appid!=null&&mch_id!=null&&key!=null){
							String out_trade_no=payOrder.getOrderNum();
							Map<String, String> wechatMap=XyBankPay.xyBankweChatOrderQuery(appid, mch_id, key, out_trade_no);
							if(wechatMap.get("code").equals("10000")){
								if (wechatMap.get("payStatus").equals("1")){
									// 订单更新
									payOrder.setPayDiscountMoney(payOrder.getPayMoney());
									payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
									resultMap.put("payOrder", payOrder);
									payv2PayOrderMapper.updateByEntity(payOrder);
									resultMap.put("status", "1"); // 支付成功
								}
							}else if(wechatMap.get("code").equals("10001")){
								//查询接口失败
								resultMap.put("status", "2");
							}
						}else{
							logger.error("兴业银行微信公众号，微信扫码支付：订单查询失败：请检查数据库配置参数完整--》RateKey1,RateKey2,RateKey3");
						}
						
					}
					/**
					 * 兴业银行支付宝扫码支付：订单查询
					 */
					else if(ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_XYBANk_ALI_SCAN)){
						String appid=ppwr.getRateKey1();
						String mch_id=ppwr.getRateKey2();
						String key=ppwr.getRateKey3();
						if(appid!=null&&mch_id!=null&&key!=null){
							String out_trade_no=payOrder.getOrderNum();
							Map<String, String> wechatMap=AliPay.xyBankOrderSelect(out_trade_no, appid, mch_id, key);
							if(wechatMap.get("code").equals("10000")){
								if (wechatMap.get("payStatus").equals("1")){
									// 订单更新
									payOrder.setPayDiscountMoney(payOrder.getPayMoney());
									payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
									resultMap.put("payOrder", payOrder);
									payv2PayOrderMapper.updateByEntity(payOrder);
									resultMap.put("status", "1"); // 支付成功
								}
							}else if(wechatMap.get("code").equals("10001")){
								//查询接口失败
								resultMap.put("status", "2");
							}
						}else{
							logger.error("兴业银行支付宝扫码支付：订单查询失败：请检查数据库配置参数完整--》RateKey1,RateKey2,RateKey3");
						}
					}
					/**
					 * 民生银行：支付宝扫码支付,微信扫码支付,微信公众号支付-订单查询
					 */
					else if(ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_ALI_SCAN)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_SCAN)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_WEIXIN_GZH)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_MSBANk_QQ_SCAN)){
						
						if(ppwr.getRateKey1()!=null&&ppwr.getRateKey2()!=null){
							Map<String,Object> paramMap=new HashMap<String,Object>();
							paramMap.put("merNo", ppwr.getRateKey1());
							String bankSecretKey=ppwr.getRateKey2();
							paramMap.put("orderNo",payOrder.getOrderNum());
							Map<String, String> wechatMap=HttpMinshengBank.queryMSOrder(paramMap, bankSecretKey);
							if(wechatMap.get("code").equals("10000")){
								if (wechatMap.get("payStatus").equals("1")){
									// 订单更新
									payOrder.setPayDiscountMoney(payOrder.getPayMoney());
									payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
									resultMap.put("payOrder", payOrder);
									payv2PayOrderMapper.updateByEntity(payOrder);
									resultMap.put("status", "1"); // 支付成功
								}
							}else if(wechatMap.get("code").equals("10001")){
								//查询接口失败
								resultMap.put("status", "2");
							}
						}else{
							logger.error("民生银行：支付宝扫码支付,微信扫码支付,微信公众号支付-订单查询失败：请检查数据库配置参数完整--》RateKey1,RateKey2");
						}
					}
					/**
					 * 威富通：微信公众号支付-订单查询
					 */
					else if(ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_WFT_WEIXIN_GZH)){
						String mchId=ppwr.getRateKey1();
						String key=ppwr.getRateKey2();
						if(mchId!=null&&key!=null){
							SortedMap<String, String> smap =new TreeMap<String, String>();
							smap.put("out_trade_no", payOrder.getOrderNum());
							Map<String, String> wechatMap=SwiftWechatGzhPay.query(smap, mchId, key);
							if(wechatMap.get("code").equals("10000")){
								if (wechatMap.get("payStatus").equals("1")){
									// 订单更新
									payOrder.setPayDiscountMoney(payOrder.getPayMoney());
									payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
									payv2PayOrderMapper.updateByEntity(payOrder);
									resultMap.put("payOrder", payOrder);
									resultMap.put("status", "1"); // 支付成功
								}
							}else if(wechatMap.get("code").equals("10001")){
								//查询接口失败
								resultMap.put("status", "2");
							}
						}else{
							logger.error("威富通订单查询失败：请检查数据库配置参数完整--》RateKey1,RateKey2");
						}
					}
					/**
					 * 平安银行：订单查询接口
					 */
					else if(ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_SCAN)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_ALI_SCAN)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_GZH_WEIXIN_SCAN)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_PABANk_WEIXIN_GZH)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_PINGAN_BANK_WEIXIN_GZH_QX_SCAN)
							||ppwr.getDictName().equals(PayRateDictValue.PAY_TYPE_PA_BANK_QQ_SCAN))
					{
						String OPEN_ID=ppwr.getRateKey1();
						String OPEN_KEY=ppwr.getRateKey2();
						if(OPEN_ID!=null&&OPEN_KEY!=null){
							String outNo=payOrder.getOrderNum();
							//查询某个订单状态
							Map<String, String>  paMap=	PABankPay.queryPayStatus("", outNo,OPEN_ID,OPEN_KEY);
							if(paMap.get("code").equals("10000")){
								//交易成功
								if (paMap.get("status").equals("1")){
									// 订单金额
									String trade_amount=paMap.get("trade_amount");
									double money=Integer.valueOf(trade_amount)/100.00;
									payOrder.setPayDiscountMoney(money);
									payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS);
									payv2PayOrderMapper.updateByEntity(payOrder);
									resultMap.put("payOrder", payOrder);
									resultMap.put("status", "1"); // 支付成功
								}
							}else if(paMap.get("code").equals("10001")){
								//查询接口失败
								resultMap.put("status", "2");
							}
						}else{
							logger.error("平安银行：订单查询接口失败：请检查数据库配置参数完整--》RateKey1,RateKey2");
						}
					}
				} else {
					resultMap.put("status", "2");
					resultMap.put("payOrder", payOrder);
					logger.debug("订单未支付");
				}
			} else {
				resultMap.put("status", "1");
				resultMap.put("payOrder", payOrder);
				logger.debug("订单支付成功");
			}
		} else {
			resultMap.put("status", "3");
			logger.debug("参数不能为空,或者有误");
		}

		return resultMap;
	}
	public Map<String, Object> buildOrder(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 根据shopKey获取店铺信息
		Payv2BussCompanyShop shop = new Payv2BussCompanyShop();
		shop.setShopKey(map.get("shopKey").toString());
		shop = payv2BussCompanyShopMapper.selectSingle(shop);
		if (shop != null) {
			Long goodsId = payv2PayGoodsService.saveGoods(shop.getId(), shop.getShopName(), 2);
			Payv2BussCompany company = new Payv2BussCompany();
			company.setId(shop.getCompanyId());
			company = payv2BussCompanyMapper.selectSingle(company);
			if (company != null) {
				Payv2PayOrder payv2PayOrder = new Payv2PayOrder();
				payv2PayOrder.setChannelId(company.getChannelId());// 渠道id
				payv2PayOrder.setOrderNum(OrderUtil.getOrderNum()); // 订单生产（规则？）
				payv2PayOrder.setOrderName(shop.getShopName() + "的消费信息");// 订单名称
				payv2PayOrder.setOrderDescribe(shop.getShopName() + "的下单信息");// 订单描述
				payv2PayOrder.setCompanyId(company.getId());// 商户ID
				// payv2PayOrder.setUserId(userId);//用户ID
				payv2PayOrder.setOrderType(2);// 订单类型1.app（线上）2.店铺（线下）',
				payv2PayOrder.setAppId(shop.getId());// 应用ID
				payv2PayOrder.setPayWayId(1l);// 订单的支付方式
				/*
				 * //通道费用
				 * payv2PayOrder.setPayWayMoney(getPayWayMoney(Double.valueOf
				 * (map.get("discountMoney").toString()) ,
				 * getPayWayRate(shop.getCompanyId())));
				 */
				payv2PayOrder.setGoodsId(goodsId);// 商品id
				payv2PayOrder.setPayMoney(Double.valueOf(map.get("payMoney").toString()));
				payv2PayOrder.setDiscountMoney(Double.valueOf(map.get("discountMoney").toString()));
				double payDiscountMoney = Double.valueOf(map.get("payMoney").toString()) - Double.valueOf(map.get("discountMoney").toString());
				payv2PayOrder.setPayDiscountMoney(payDiscountMoney);// 实际支付金额
				// 获取优惠信息
				Payv2AppDiscount payv2AppDiscount = new Payv2AppDiscount();
				payv2AppDiscount.setAppId(shop.getPayWayId());
				// 获取店铺优惠信息
				payv2AppDiscount = payv2AppDiscountMapper.selectSingle(payv2AppDiscount);
				if (payv2AppDiscount != null) {
					payv2PayOrder.setDiscountId(payv2AppDiscount.getId());// 优惠ID
				}
				payv2PayOrder.setPayStatus(3);// 未支付
				payv2PayOrder.setCreateTime(new Date());
				payv2PayOrderMapper.insertByEntity(payv2PayOrder);

				// h5支付 获取表单
//				String form = AliPay.alipayH5(null, payv2PayOrder.getOrderNum(), payv2PayOrder.getOrderName(), payDiscountMoney);
				String form = "";
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, form);
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.COMPANY_NOT_EXIST, null);
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.SHOP_NOT_EXIST, null);
		}
		return resultMap;
	}

	/**
	 * 创建订单(提供点游)
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, String> dyCreateOrder(Map<String, Object> map, Payv2BussCompanyApp pbca
			, Integer payViewType,Integer payType) {
		Map<String, String> resultMap = new HashMap<>();
		Double payMoney = Double.valueOf(map.get("payMoney").toString());
		//根据支付类型和商户查询商户支持支付通道
		List<Payv2BussSupportPayWay> payWayList = payv2BussSupportPayWayService
				.queryByCompany(pbca.getCompanyId(), payViewType, payType,payMoney);
		
		if(payWayList==null||payWayList.size()==0){
			resultMap.put("status", PayFinalUtil.PAY_TYPE_FAIL);
			MailRun.send("获取不到通道","未配置支付通道或支付类型不支持",null,null,pbca.getAppName()
					,""+payMoney,pbca.getCompanyName(),null,null);
			return resultMap;
		}
		
		//获取支付方式、通道
		Payv2BussSupportPayWay pbspw = payv2BussSupportPayWayService.getWayRate(payWayList, payMoney, pbca.getCompanyId()
				, payViewType,pbca.getIsRandom(),payType);
		
		if(pbspw==null){
			resultMap.put("status", PayFinalUtil.PAY_STATUS_FAIL);
			MailRun.send("超过限额","已超过限额,请检查支付通道单笔额度和每日额度",null,null,pbca.getAppName()
					,""+payMoney,pbca.getCompanyName(),null,null);
			return resultMap;
		}
		//支付通道id
		resultMap.put("rateId", ""+pbspw.getRateId());
		resultMap.put("dictName", pbspw.getDictName());
		resultMap.put("rateKey1", pbspw.getRateKey1());
		resultMap.put("rateKey2", pbspw.getRateKey2());
		resultMap.put("rateKey3", pbspw.getRateKey3()==null?"":pbspw.getRateKey3());
		resultMap.put("rateKey4", pbspw.getRateKey4()==null?"":pbspw.getRateKey4());
		resultMap.put("rateKey5", pbspw.getRateKey5()==null?"":pbspw.getRateKey5());
		resultMap.put("rateKey6", pbspw.getRateKey6()==null?"":pbspw.getRateKey6());
		resultMap.put("gzhAppId", pbspw.getGzhAppId()==null?"":pbspw.getGzhAppId());
		resultMap.put("gzhKey", pbspw.getGzhKey()==null?"":pbspw.getGzhKey());
		resultMap.put("rateCompanyName", pbspw.getRateName());
		resultMap.put("payWayName", pbspw.getPayWayName());
		resultMap.put("appName", pbca.getAppName());
		resultMap.put("companyName", pbca.getCompanyName());
		
		// 创建订单
		Payv2PayOrder payOrder = new Payv2PayOrder();

		payOrder.setMerchantOrderNum(map.get("bussOrderNum").toString());
		payOrder.setAppId(pbca.getId());
		payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		if(payOrder!=null){
			if(payOrder.getPayStatus()!=1&&payOrder.getPayStatus()!=5){
				payOrder.setReturnUrl(map.get("returnUrl") == null ? null : map.get("returnUrl").toString()); // 回调的页面
				payOrder.setNotifyUrl(map.get("notifyUrl").toString()); // 回调地址（服务器）
				payOrder.setRateId(pbspw.getRateId()); // 支付通道
				payOrder.setPayWayId(pbspw.getPayWayId());//支付方式
				if(map.get("remark")!=null){
					payOrder.setRemark(map.get("remark").toString());
				}
				// 通道费用
				payOrder.setPayWayMoney(getPayWayMoney(payMoney, pbspw.getPayWayRate()));
				//费率
				payOrder.setBussWayRate(pbspw.getPayWayRate());
				//成本费率
				payOrder.setCostRate(pbspw.getCostRate());
				//成本手续费
				payOrder.setCostRateMoney(getPayWayMoney(payMoney, pbspw.getCostRate()));
				
				payv2PayOrderMapper.updateByEntity(payOrder);
			}else{
				// 支付已完成
				resultMap.put("status", PayFinalUtil.PAY_STATUS_FAIL_OK);
				return resultMap;
			}
		}else{
			payOrder = new Payv2PayOrder();
			// 创建订单
			payOrder = createOrder(map, payOrder, pbca, payMoney, pbspw.getPayWayId()
					, pbspw.getRateId(),pbspw.getPayWayRate(),pbspw.getCostRate());
		}

		resultMap.put("status", PayFinalUtil.PAY_STATUS_SUSSESS);
		resultMap.put("orderNum", payOrder.getOrderNum());
		resultMap.put("payMoney", ""+payOrder.getPayMoney());
		resultMap.put("orderName", payOrder.getOrderName());

		return resultMap;
	}

	/**
	 * 获取各个维度的数据详情
	 */
	public Map<String, Object> getDayStatistics(Map<String, Object> map) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (Integer.valueOf(map.get("selectDayType").toString()) == 1) {// 今日
			OrderVo vo = new OrderVo();
			// 1:关键指标
			// 今日总订单数
			int dayCount = payv2PayOrderMapper.getStatisticsCount(map);
			// 今日成功订单数
			map.put("payStatus", 1);
			int daySuccessCount = payv2PayOrderMapper.getStatisticsCount(map);
			// 今日失败订单数
			int dayFailCount = dayCount - daySuccessCount;
			// 今日成功交易总金额数
			double daySuccessMoney = payv2PayOrderMapper.getMoneySum(map);
			// 今日失败金额
			map.put("payStatus", 2);
			double dayFailMoney = payv2PayOrderMapper.getMoneySum(map);
			// 今日订单成功率
			double daySuccessRateOrder = (double) daySuccessCount / daySuccessMoney * 100;
			// 查询昨日的数据，来计算今日与昨日的百分比:根据渠道商ID查询
			Payv2StatisticsDayChannel payv2StatisticsDayChannel = payv2StatisticsDayChannelMapper.getYesterdayStatistics(map);
			if (payv2StatisticsDayChannel != null && dayCount != 0) {
				// 计算：今日累积与昨日累积百分比
				double dayCountPercentage = (double) payv2StatisticsDayChannel.getOrderCount() / dayCount * 100;
				// 计算：今日成功订单数与昨日成功订单数百分比
				double dayStatisticsPercentage = (double) payv2StatisticsDayChannel.getSuccessOrderCount() / daySuccessCount * 100;
				// 计算：今日成功交易金额与昨日成功交易金额百分比
				double dayMoneyPercentage = (double) payv2StatisticsDayChannel.getSuccessOrderMoney() / daySuccessMoney * 100;
				// 计算：今日失败订单数与昨日订单数的百分比
				double dayFailCountPercentage = (double) payv2StatisticsDayChannel.getFailOrderCount() / dayFailCount * 100;
				// 计算：今日失败金额与昨日失败金额的百分比
				double dayFailMonePercentage = (double) payv2StatisticsDayChannel.getFailOrderMoney() / dayFailMoney * 100;
				// 计算: 昨日成功率
				double dayYesterdayStatisticsPercentage = (double) payv2StatisticsDayChannel.getSuccessOrderCount() / payv2StatisticsDayChannel.getOrderCount()
						* 100;
				// 计算：今日与昨日的日增长成功率
				double aysp = dayYesterdayStatisticsPercentage / daySuccessRateOrder * 100;
				vo.setDayCountPercentage(dayCountPercentage);
				vo.setDayFailCountPercentage(dayFailCountPercentage);
				vo.setDayFailMonePercentage(dayFailMonePercentage);
				vo.setDayMoneyPercentage(dayMoneyPercentage);
				vo.setDayStatisticsPercentage(dayStatisticsPercentage);
				vo.setDayYesterdayStatisticsPercentage(aysp);
			}

			vo.setDayCount(dayCount);
			vo.setDayFailCount(dayFailCount);
			vo.setDayFailMoney(dayFailMoney);
			vo.setDaySuccessCount(daySuccessCount);
			vo.setDaySuccessMoney(daySuccessMoney);
			vo.setDaySuccessRateOrder(daySuccessRateOrder);
			Map<String, Object> orderVoMap = new HashMap<String, Object>();
			orderVoMap.put("orderDay", vo);
			// 2:获取时间段曲线图数据
			// 今日
			map.put("dayType", 1);
			List<Payv2StatisticsDayTimeBean> dayTimeSlot = payv2StatisticsDayTimeMapper.getDayStatisticsInfo(map);
			// 昨日
			map.put("dayType", 2);
			List<Payv2StatisticsDayTimeBean> yesterdayTimeSlot = payv2StatisticsDayTimeMapper.getDayStatisticsInfo(map);
			// 7天前
			map.put("dayType", 3);
			List<Payv2StatisticsDayTimeBean> day7TimeSlot = payv2StatisticsDayTimeMapper.getDayStatisticsInfo(map);

			Map<String, Object> dayTimeMap = new HashMap<String, Object>();
			dayTimeMap.put("dayTimeSlot", dayTimeSlot);
			dayTimeMap.put("yesterdayTimeSlot", yesterdayTimeSlot);
			dayTimeMap.put("day7TimeSlot", day7TimeSlot);
			// 3:今日支付渠道数据柱状图：
			List<Payv2StatisticsDayWayBean> dayPay = payv2StatisticsDayWayMapper.getDayStatisticsWayInfo(map);
			// 查询支付渠道名字
			for (Payv2StatisticsDayWayBean payv2StatisticsDayWayBean : dayPay) {
				Payv2PayWay payv2PayWay = new Payv2PayWay();
				payv2PayWay.setIsDelete(2);
				payv2PayWay.setStatus(1);
				payv2PayWay.setId(payv2StatisticsDayWayBean.getPayId());
				payv2PayWay = payv2PayWayMapper.selectSingle(payv2PayWay);
				if (payv2PayWay != null) {
					payv2StatisticsDayWayBean.setPayName(payv2PayWay.getWayName());
				}
			}
			Map<String, Object> dayPayMap = new HashMap<String, Object>();
			dayPayMap.put("dayWayList", dayPay);
			// 4:获取今日关键详情数据
			List<Payv2PayOrder> orderInfoCountList = payv2PayOrderMapper.getDayStatisticsInfoCount(map);
			List<Payv2PayOrderCountBean> dayInfoCountList = new ArrayList<Payv2PayOrderCountBean>();
			for (Payv2PayOrder payv2PayOrder : orderInfoCountList) {
				Payv2PayOrderCountBean bean = new Payv2PayOrderCountBean();
				// 获取接入商名字
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany.setId(payv2PayOrder.getCompanyId());
				payv2BussCompany = payv2BussCompanyMapper.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					bean.setCompanyName(payv2BussCompany.getCompanyName());
				}
				bean.setOrderCount(payv2PayOrder.getOrderCount());
				bean.setPayDiscountMoney(payv2PayOrder.getPayDiscountMoney());
				dayInfoCountList.add(bean);
			}
			Map<String, Object> dayInfoCountMap = new HashMap<String, Object>();
			dayInfoCountMap.put("dayInfoCountList", dayInfoCountList);
			// 返回数据
			returnMap.put("orderVoMap", orderVoMap);
			returnMap.put("dayTimeMap", dayTimeMap);
			returnMap.put("dayPayMap", dayPayMap);
			returnMap.put("derInfoCountMap", dayInfoCountMap);
		}
		if (Integer.valueOf(map.get("selectDayType").toString()) == 2) {// 昨天
			OrderVo vo = new OrderVo();
			// 1:关键指标：
			// 查询昨日数据详情
			Payv2StatisticsDayChannel yesterdayInfo = payv2StatisticsDayChannelMapper.getYesterdayStatistics(map);
			// 查询前天数据
			Payv2StatisticsDayChannel beforeYesterdayInfo = payv2StatisticsDayChannelMapper.getBeforeYesterdayStatistics(map);
			// 计算日增长率
			if (beforeYesterdayInfo != null && yesterdayInfo != null) {
				// 计算：昨日积与前日累积百分比
				if (beforeYesterdayInfo.getOrderCount() != null && beforeYesterdayInfo.getOrderCount() != 0) {
					double dayCountPercentage = (double) yesterdayInfo.getOrderCount() / beforeYesterdayInfo.getOrderCount() * 100;
					vo.setDayCountPercentage(dayCountPercentage);
				}
				// 计算：昨日成功订单数与前日成功订单数百分比
				if (beforeYesterdayInfo.getSuccessOrderCount() != null && beforeYesterdayInfo.getSuccessOrderCount() != 0) {
					double dayStatisticsPercentage = (double) yesterdayInfo.getSuccessOrderCount() / beforeYesterdayInfo.getSuccessOrderCount() * 100;
					vo.setDayStatisticsPercentage(dayStatisticsPercentage);
				}
				// 计算：昨日成功交易金额与前日成功交易金额百分比
				if (beforeYesterdayInfo.getSuccessOrderMoney() != null && beforeYesterdayInfo.getSuccessOrderMoney() != 0) {
					double dayMoneyPercentage = (double) yesterdayInfo.getSuccessOrderMoney() / beforeYesterdayInfo.getSuccessOrderMoney() * 100;
					vo.setDayMoneyPercentage(dayMoneyPercentage);
				}
				// 计算：昨日失败订单数与前日订单数的百分比
				if (beforeYesterdayInfo.getFailOrderCount() != null && beforeYesterdayInfo.getFailOrderCount() != 0) {
					double dayFailCountPercentage = (double) yesterdayInfo.getFailOrderCount() / beforeYesterdayInfo.getFailOrderCount() * 100;
					vo.setDayFailCountPercentage(dayFailCountPercentage);
				}
				// 计算：昨日失败金额与前日失败金额的百分比
				if (beforeYesterdayInfo.getFailOrderMoney() != null && beforeYesterdayInfo.getFailOrderMoney() != 0) {
					double dayFailMonePercentage = (double) yesterdayInfo.getFailOrderMoney() / beforeYesterdayInfo.getFailOrderMoney() * 100;
					vo.setDayFailMonePercentage(dayFailMonePercentage);
				}
				// 计算: 昨日成功率
				if (yesterdayInfo.getOrderCount() != null && yesterdayInfo.getOrderCount() != 0 && beforeYesterdayInfo.getOrderCount() != null
						&& beforeYesterdayInfo.getOrderCount() != 0) {
					double dayYesterdayStatisticsPercentage = (double) yesterdayInfo.getSuccessOrderCount() / yesterdayInfo.getOrderCount() * 100;
					// 计算: 前日成功率
					double beforeYesterdayStatisticsPercentage = (double) beforeYesterdayInfo.getSuccessOrderCount() / beforeYesterdayInfo.getOrderCount()
							* 100;
					// 计算：昨日与前日的增长成功率
					double successRate = dayYesterdayStatisticsPercentage / beforeYesterdayStatisticsPercentage * 100;
					vo.setDayYesterdayStatisticsPercentage(successRate);
				}

			}
			vo.setDayCount(yesterdayInfo.getOrderCount());
			vo.setDayFailCount(yesterdayInfo.getFailOrderCount());
			vo.setDayFailMoney(yesterdayInfo.getFailOrderMoney());
			vo.setDaySuccessCount(yesterdayInfo.getSuccessOrderCount());
			vo.setDaySuccessMoney(yesterdayInfo.getSuccessOrderMoney());
			if (yesterdayInfo.getOrderCount() != 0) {
				vo.setDaySuccessRateOrder(yesterdayInfo.getSuccessOrderCount() / yesterdayInfo.getOrderCount() * 100);
			} else {
				vo.setDaySuccessRateOrder(0);
			}
			Map<String, Object> orderVoMap = new HashMap<String, Object>();
			orderVoMap.put("orderDay", vo);
			// 2:获取昨日时间段曲线图数据
			// 昨日
			map.put("dayType", 2);
			List<Payv2StatisticsDayTimeBean> yesterdayTimeSlot = payv2StatisticsDayTimeMapper.getDayStatisticsInfo(map);
			// 前日
			map.put("dayType", 4);
			List<Payv2StatisticsDayTimeBean> yesterday4TimeSlot = payv2StatisticsDayTimeMapper.getDayStatisticsInfo(map);
			// 7天前
			map.put("dayType", 5);
			List<Payv2StatisticsDayTimeBean> yesterday7TimeSlot = payv2StatisticsDayTimeMapper.getDayStatisticsInfo(map);
			Map<String, Object> dayTimeMap = new HashMap<String, Object>();
			dayTimeMap.put("dayTimeSlot", yesterdayTimeSlot);
			dayTimeMap.put("yesterdayTimeSlot", yesterday4TimeSlot);
			dayTimeMap.put("day7TimeSlot", yesterday7TimeSlot);

			// 3:昨日支付渠道数据柱状图：
			List<Payv2StatisticsDayWayBean> yesterdayWay = payv2StatisticsDayWayMapper.getYesterdayStatisticsWayInfo(map);
			// 查询支付渠道名字
			for (Payv2StatisticsDayWayBean payv2StatisticsDayWayBean : yesterdayWay) {
				Payv2PayWay payv2PayWay = new Payv2PayWay();
				payv2PayWay.setIsDelete(2);
				payv2PayWay.setStatus(1);
				payv2PayWay.setId(payv2StatisticsDayWayBean.getPayId());
				payv2PayWay = payv2PayWayMapper.selectSingle(payv2PayWay);
				if (payv2PayWay != null) {
					payv2StatisticsDayWayBean.setPayName(payv2PayWay.getWayName());
				}
			}
			Map<String, Object> dayPayMap = new HashMap<String, Object>();
			dayPayMap.put("dayWayList", yesterdayWay);
			// 4：获取昨日关键详情数据
			List<Payv2StatisticsDayChannel> yesterdayInfoList = payv2StatisticsDayChannelMapper.getYesterdayInfoList(map);
			List<Payv2PayOrderCountBean> dayInfoCountList = new ArrayList<Payv2PayOrderCountBean>();
			for (Payv2StatisticsDayChannel payv2StatisticsDayChannel : yesterdayInfoList) {
				Payv2PayOrderCountBean bean = new Payv2PayOrderCountBean();
				// 获取接入商名字
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany.setId(payv2StatisticsDayChannel.getCompanyId());
				payv2BussCompany = payv2BussCompanyMapper.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					bean.setCompanyName(payv2BussCompany.getCompanyName());
				}
				// 昨日成功订单数
				bean.setOrderCount(payv2StatisticsDayChannel.getSuccessOrderCount());
				// 昨日成功交易金额
				bean.setPayDiscountMoney(payv2StatisticsDayChannel.getSuccessOrderMoney());
				dayInfoCountList.add(bean);
			}
			Map<String, Object> dayInfoCountMap = new HashMap<String, Object>();
			dayInfoCountMap.put("dayInfoCountList", dayInfoCountList);
			// 返回数据
			returnMap.put("orderVoMap", orderVoMap);
			returnMap.put("dayTimeMap", dayTimeMap);
			returnMap.put("dayPayMap", dayPayMap);
			returnMap.put("derInfoCountMap", dayInfoCountMap);
		}

		if (Integer.valueOf(map.get("selectDayType").toString()) == 7) {// 近7天后
			getPublicInfo(7, returnMap, map);
		}

		if (Integer.valueOf(map.get("selectDayType").toString()) == 15) {// 近15天后
			getPublicInfo(15, returnMap, map);
		}

		if (Integer.valueOf(map.get("selectDayType").toString()) == 30) {// 近30天后
			getPublicInfo(30, returnMap, map);
		}

		if (Integer.valueOf(map.get("selectDayType").toString()) == 3) {// 按时间段来计算数据
																		// 或者按照接入商来计算
																		// 二者也可以同时查询
			OrderVo vo = new OrderVo();
			// 1:关键指标：
			// 获取各项指标数据总和
			Payv2StatisticsDayChannel timeStatisticsList = payv2StatisticsDayChannelMapper.getTimeStatistics(map);
			vo.setDayCount(timeStatisticsList.getOrderCount());
			vo.setDayFailCount(timeStatisticsList.getFailOrderCount());
			vo.setDayFailMoney(timeStatisticsList.getFailOrderMoney());
			vo.setDaySuccessCount(timeStatisticsList.getSuccessOrderCount());
			vo.setDaySuccessMoney(timeStatisticsList.getSuccessOrderMoney());
			if (timeStatisticsList.getOrderCount() != 0 && timeStatisticsList.getOrderCount() != null) {
				vo.setDaySuccessRateOrder(timeStatisticsList.getSuccessOrderCount() / timeStatisticsList.getOrderCount() * 100);
			} else {
				vo.setDaySuccessRateOrder(0);
			}
			Map<String, Object> orderVoMap = new HashMap<String, Object>();
			orderVoMap.put("orderDay", vo);
			// 2:获取日曲线图数据
			List<Payv2StatisticsDayChannel> timeInfoList = payv2StatisticsDayChannelMapper.getTimeInfoList(map);
			Map<String, Object> dayTimeMap = new HashMap<String, Object>();
			dayTimeMap.put("dayTimeSlot", timeInfoList);
			// 3:支付渠道数据柱状图：
			List<Payv2StatisticsDayWayBean> timeWayList = payv2StatisticsDayWayMapper.getTimeWayInfo(map);
			// 查询支付渠道名字
			for (Payv2StatisticsDayWayBean payv2StatisticsDayWayBean : timeWayList) {
				Payv2PayWay payv2PayWay = new Payv2PayWay();
				payv2PayWay.setIsDelete(2);
				payv2PayWay.setStatus(1);
				payv2PayWay.setId(payv2StatisticsDayWayBean.getPayId());
				payv2PayWay = payv2PayWayMapper.selectSingle(payv2PayWay);
				if (payv2PayWay != null) {
					payv2StatisticsDayWayBean.setPayName(payv2PayWay.getWayName());
				}
			}
			Map<String, Object> dayPayMap = new HashMap<String, Object>();
			dayPayMap.put("dayWayList", timeWayList);
			// 4：获取关键详情数据
			List<Payv2StatisticsDayChannel> timeInfoByCompanyList = payv2StatisticsDayChannelMapper.getTimeInfoByCompanyList(map);
			List<Payv2PayOrderCountBean> dayInfoCountList = new ArrayList<Payv2PayOrderCountBean>();
			for (Payv2StatisticsDayChannel payv2StatisticsDayChannel : timeInfoByCompanyList) {
				Payv2PayOrderCountBean bean = new Payv2PayOrderCountBean();
				// 获取接入商名字
				Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
				payv2BussCompany.setIsDelete(2);
				payv2BussCompany.setCompanyStatus(2);
				payv2BussCompany.setId(payv2StatisticsDayChannel.getCompanyId());
				payv2BussCompany = payv2BussCompanyMapper.selectSingle(payv2BussCompany);
				if (payv2BussCompany != null) {
					bean.setCompanyName(payv2BussCompany.getCompanyName());
				}
				// 成功订单数
				bean.setOrderCount(payv2StatisticsDayChannel.getSuccessOrderCount());
				// 成功交易金额
				bean.setPayDiscountMoney(payv2StatisticsDayChannel.getSuccessOrderMoney());
				dayInfoCountList.add(bean);
			}
			Map<String, Object> dayInfoCountMap = new HashMap<String, Object>();
			dayInfoCountMap.put("dayInfoCountList", dayInfoCountList);
			// 返回数据
			returnMap.put("orderVoMap", orderVoMap);
			returnMap.put("dayTimeMap", dayTimeMap);
			returnMap.put("dayPayMap", dayPayMap);
			returnMap.put("derInfoCountMap", dayInfoCountMap);
		}
		return returnMap;
	}

	/**
	 * @Title: getPublicInfo
	 * @Description:7 15 30 公共类
	 * @param type
	 * @param returnMap
	 * @param map
	 * @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @date 2017年2月14日 下午2:48:56
	 * @throws
	 */
	public Map<String, Object> getPublicInfo(int type, Map<String, Object> returnMap, Map<String, Object> map) {
		OrderVo vo = new OrderVo();
		map.put("latelyDayType", type);
		// 1:关键指标：
		// 获取各项指标数据总和
		Payv2StatisticsDayChannel latelyDay7 = payv2StatisticsDayChannelMapper.getLatelyDayStatistics(map);
		vo.setDayCount(latelyDay7.getOrderCount());
		vo.setDayFailCount(latelyDay7.getFailOrderCount());
		vo.setDayFailMoney(latelyDay7.getFailOrderMoney());
		vo.setDaySuccessCount(latelyDay7.getSuccessOrderCount());
		vo.setDaySuccessMoney(latelyDay7.getSuccessOrderMoney());
		vo.setDaySuccessRateOrder(latelyDay7.getSuccessOrderCount() / latelyDay7.getOrderCount() * 100);
		Map<String, Object> orderVoMap = new HashMap<String, Object>();
		orderVoMap.put("orderDay", vo);
		// 2:获取近7天每日：日曲线图数据
		List<Payv2StatisticsDayChannel> latelyDayInfoList = payv2StatisticsDayChannelMapper.getLatelyDayInfoList(map);
		Map<String, Object> dayTimeMap = new HashMap<String, Object>();
		dayTimeMap.put("dayTimeSlot", latelyDayInfoList);
		// 3:近7日支付渠道数据柱状图：
		List<Payv2StatisticsDayWayBean> latelyDayWay = payv2StatisticsDayWayMapper.getlatelyDayWayInfo(map);
		// 查询支付渠道名字
		for (Payv2StatisticsDayWayBean payv2StatisticsDayWayBean : latelyDayWay) {
			Payv2PayWay payv2PayWay = new Payv2PayWay();
			payv2PayWay.setIsDelete(2);
			payv2PayWay.setStatus(1);
			payv2PayWay.setId(payv2StatisticsDayWayBean.getPayId());
			payv2PayWay = payv2PayWayMapper.selectSingle(payv2PayWay);
			if (payv2PayWay != null) {
				payv2StatisticsDayWayBean.setPayName(payv2PayWay.getWayName());
			}
		}
		Map<String, Object> dayPayMap = new HashMap<String, Object>();
		dayPayMap.put("dayWayList", latelyDayWay);
		// 4：获取近7日关键详情数据：为接入商为维度
		List<Payv2StatisticsDayChannel> LatelyDayInfoByCompanyList = payv2StatisticsDayChannelMapper.getLatelyDayInfoByCompanyList(map);
		List<Payv2PayOrderCountBean> dayInfoCountList = new ArrayList<Payv2PayOrderCountBean>();
		for (Payv2StatisticsDayChannel payv2StatisticsDayChannel : LatelyDayInfoByCompanyList) {
			Payv2PayOrderCountBean bean = new Payv2PayOrderCountBean();
			// 获取接入商名字
			Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
			payv2BussCompany.setIsDelete(2);
			payv2BussCompany.setCompanyStatus(2);
			payv2BussCompany.setId(payv2StatisticsDayChannel.getCompanyId());
			payv2BussCompany = payv2BussCompanyMapper.selectSingle(payv2BussCompany);
			if (payv2BussCompany != null) {
				bean.setCompanyName(payv2BussCompany.getCompanyName());
			}
			// 成功订单数
			bean.setOrderCount(payv2StatisticsDayChannel.getSuccessOrderCount());
			// 成功交易金额
			bean.setPayDiscountMoney(payv2StatisticsDayChannel.getSuccessOrderMoney());
			dayInfoCountList.add(bean);
		}
		Map<String, Object> dayInfoCountMap = new HashMap<String, Object>();
		dayInfoCountMap.put("dayInfoCountList", dayInfoCountList);
		// 返回数据
		returnMap.put("orderVoMap", orderVoMap);
		returnMap.put("dayTimeMap", dayTimeMap);
		returnMap.put("dayPayMap", dayPayMap);
		returnMap.put("derInfoCountMap", dayInfoCountMap);
		return returnMap;
	}

	public List<Long> queryBetweenPayOrder(Map<String, Object> map) {
		return payv2PayOrderMapper.queryBetweenPayOrder(map);
	}

	public Map<String, Object> queryBetweenPayOrderByCompanyId(Map<String, Object> map) {
		return payv2PayOrderMapper.queryBetweenPayOrderByCompanyId(map);
	}

	public Map<String, Object> queryPayOrderTime(Map<String, Object> map) {
		return payv2PayOrderMapper.queryPayOrderTime(map);
	}

	public List<Map<String, Object>> getPayOrderSuccessPayWay(Map<String, Object> map) {
		return payv2PayOrderMapper.getPayOrderSuccessPayWay(map);
	}

	@SuppressWarnings("unchecked")
	public PageObject<Payv2PayOrder> getPayOrderPageByObject(Map<String, Object> map) {
		int totalData = payv2PayOrderMapper.getPayOrderPageByObjectCount(map);
		PageHelper pageHelper = new PageHelper(totalData, map);
		List<Payv2PayOrder> list = payv2PayOrderMapper.getPayOrderPageByObject(pageHelper.getMap());
		for (Payv2PayOrder payv2PayOrder : list) {
			//计算实收金额:实收金额=订单金额-优惠金额
			if(null!=payv2PayOrder.getPayMoney()&&null!=payv2PayOrder.getDiscountMoney()){
				payv2PayOrder.setActuallyPayMoney(payv2PayOrder.getPayMoney()-payv2PayOrder.getDiscountMoney());
			}
			//判断是否是退款状态
			if(null!=payv2PayOrder.getReturnMoney()&&payv2PayOrder.getReturnMoney()!=0){
				//如果退款金额等于实际支付金额：则全部退款
				if(payv2PayOrder.getReturnMoney().doubleValue()==payv2PayOrder.getPayDiscountMoney().doubleValue()){
					payv2PayOrder.setPayStatus(7);
				}else{
					payv2PayOrder.setPayStatus(6);
				}
			}
		}
		PageObject<Payv2PayOrder> pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	public Map<String, Object> getCurrentStatisticsDayCompany(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.getCurrentStatisticsDayCompany(paramMap);
	}

	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompany(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.queryOrderInfoToStatisticsDayCompany(paramMap);
	}

	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompanyGoods(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.queryOrderInfoToStatisticsDayCompanyGoods(paramMap);
	}

	public List<Map<String, Object>> getCurrentStatisticsDayCompanyGoods(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.getCurrentStatisticsDayCompanyGoods(paramMap);
	}

	public List<Map<String, Object>> queryOrderInfoToDayCompanyTime(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.queryOrderInfoToDayCompanyTime(paramMap);
	}

	public List<Map<String, Object>> queryOrderInfoToStatisticsDayCompanyClear(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.queryOrderInfoToStatisticsDayCompanyClear(paramMap);
	}

	public List<Map<String, Object>> getCurrentStatisticsDayCompanyClear(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.getCurrentStatisticsDayCompanyClear(paramMap);
	}

	public List<Payv2PayOrder> queryByPayTimeBetween(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.queryByPayTimeBetween(paramMap);
	}

	public List<Payv2PayOrder> selectDetailList(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.selectDetailList(paramMap);
	}

	/**
	 * 商户app条码支付
	 * 
	 * @param map
	 * @return
	 * @throws AlipayApiException
	 */
	public Map<String, Object> barCodePay(Map<String, Object> map) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Payv2BussCompanyShop pbcs = new Payv2BussCompanyShop();
		pbcs.setId(Long.valueOf(map.get("id").toString()));
		pbcs = payv2BussCompanyShopMapper.selectSingle(pbcs);
		if (pbcs != null) {
			Payv2PayOrder payOrder = new Payv2PayOrder();
			// 保存商品(先查询是否存在，没有就保存！返回商品id)
			Long goodsId = payv2PayGoodsService.saveGoods(pbcs.getId(), pbcs.getShopName(), 2);
			// 获取订单分组id(先查询是否存在，没有就保存！订单分组id)
			Long channelId = getChannel(pbcs);
			payOrder.setChannelId(channelId); // 渠道商id
			payOrder.setAppId(pbcs.getId());
			payOrder.setGoodsId(goodsId);
			payOrder.setCompanyId(pbcs.getCompanyId()); // 商户id
			payOrder.setCreateTime(new Date());
			payOrder.setOrderName(pbcs.getShopName()); // 订单名称/商品名称
			payOrder.setOrderDescribe(pbcs.getShopName()); // 订单描述信息
			payOrder.setPayMoney(Double.valueOf(map.get("payMoney").toString()));
			payOrder.setPayDiscountMoney(Double.valueOf(map.get("payMoney").toString()));
			payOrder.setOrderType(2);// 线下店铺
			// userId和商户订单组成
			String orderNum = OrderUtil.getOrderNum();
			payOrder.setOrderNum(orderNum); // 订单生产（规则？）
			// payOrder.setReturnUrl(map.get("returnUrl")==null?null:map.get("returnUrl").toString());
			// //回调的页面
			// payOrder.setNotifyUrl(map.get("notifyUrl").toString());
			// //回调地址（服务器）

			String authCode = map.get("authCode").toString();
			// 支付宝
			if (payType(authCode) == 1) {
				Payv2PayWayRate ppwr = getPayWayId(PayRateDictValue.PAY_TYPE_ALIPAY_SCAN);
				
				//判断商户是否支持支付宝条码支付
				Payv2BussSupportPayWay pbspw = new Payv2BussSupportPayWay();
				pbspw.setParentId(pbcs.getCompanyId());
				pbspw.setRateId(ppwr.getId());
				pbspw.setIsDelete(2);
				pbspw.setPayWayStatus(1);
				pbspw = payv2BussSupportPayWayMapper.selectSingle(pbspw);
				if(pbspw!=null){
					payOrder.setPayWayId(ppwr.getPayWayId());
					payOrder.setRateId(ppwr.getId());
					// 通道费用
					payOrder.setPayWayMoney(getPayWayMoney(Double.valueOf(map.get("payMoney").toString()),
							getPayWayRate(pbcs.getCompanyId(), ppwr.getPayWayId(), ppwr.getId())));
					AlipayTradePayResponse response = AliPay.scanPay(orderNum, authCode
							, payOrder.getOrderName(), payOrder.getPayMoney()
							,ppwr.getRateKey1(),ppwr.getRateKey2(),ppwr.getRateKey3());
					if (response.isSuccess()) {
						payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS); // 成功
						payOrder.setPayTime(new Date());
						payv2PayOrderMapper.insertByEntity(payOrder);
						resultMap.put("orderNume", orderNum);
						resultMap.put("orderId", payOrder.getId());
					}
				}else{
					System.out.println("错误信息：商户【不支持】支付宝条码支付");
				}
			} else if (payType(authCode) == 2) { // 微信
				Payv2PayWayRate ppwr = getPayWayId(PayRateDictValue.PAY_TYPE_WEIXIN_SCAN);
				
				//判断商户是否支持支付宝条码支付
				Payv2BussSupportPayWay pbspw = new Payv2BussSupportPayWay();
				pbspw.setParentId(pbcs.getCompanyId());
				pbspw.setRateId(ppwr.getId());
				pbspw.setIsDelete(2);
				pbspw.setPayWayStatus(1);
				pbspw = payv2BussSupportPayWayMapper.selectSingle(pbspw);
				if(pbspw!=null){
					payOrder.setRateId(ppwr.getId());
					payOrder.setPayWayId(ppwr.getPayWayId());
					payOrder.setPayWayMoney(getPayWayMoney(Double.valueOf(map.get("payMoney").toString()),
							getPayWayRate(pbcs.getCompanyId(), ppwr.getPayWayId(), ppwr.getId())));
					Map<String, String> weChatMap = WeChatPay.scanPay(payOrder.getOrderName(), payOrder.getOrderNum(),
							DecimalUtil.yuanToCents("" + payOrder.getPayMoney()), map.get("ip").toString(), authCode);
					if (weChatMap.get("success_key") != null && weChatMap.get("success_key").equals("200")) {
						payOrder.setPayStatus(PayFinalUtil.PAY_ORDER_SUCCESS); // 成功
						payOrder.setPayTime(new Date());
						payv2PayOrderMapper.insertByEntity(payOrder);
						resultMap.put("orderNume", orderNum);
						resultMap.put("orderId", payOrder.getId());
					}
				}else{
					System.out.println("错误信息：商户【不支持】微信刷卡支付");
				}
			}
		}
		return resultMap;
	}

	/**
	 * 获取支付方式
	 * 
	 * @param wayName
	 * @return
	 */
	public Payv2PayWayRate getPayWayId(String wayName) {
		SysConfigDictionary scd = new SysConfigDictionary();
		scd.setDictName(PayRateDictValue.PAY_TYPE);
		scd.setDictPvalue(-1);
		scd = sysConfigDictionaryMapper.selectSingle(scd);

		SysConfigDictionary scd1 = new SysConfigDictionary();
		scd1.setDictName(wayName);
		scd1.setDictPvalue(scd.getId());
		scd1 = sysConfigDictionaryMapper.selectSingle(scd1);
		Payv2PayWayRate ppwr = new Payv2PayWayRate();
		ppwr.setDicId(scd1.getId().longValue());
		ppwr.setStatus(1);// 启用状态
		ppwr.setIsDelete(2);// 未删除状态
		ppwr = payv2PayWayRateMapper.selectSingle(ppwr);
		return ppwr;
	}

	/**
	 * 扫码类型
	 * 
	 * @param authCode
	 * @return
	 */
	private int payType(String authCode) {
		// 支付宝授权码以28开头,微信授权码以10、11、12、13、14、15开头
		if (authCode.startsWith("28")) {
			return 1;
		} else if (authCode.startsWith("10") || authCode.startsWith("11") || authCode.startsWith("12") || authCode.startsWith("13")
				|| authCode.startsWith("14") || authCode.startsWith("15")) {
			return 2;
		}
		return 3;
	}
	
	// yyyyMMddHHmmss
	private Date getDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.parse(date);
	}
	
	// yyyy-MM-dd HH:mm:ss
	private Date getDate1(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(date);
	}

	/**
	 * 通道费用最小值0.01分
	 * 
	 * @param payMoney
	 * @param payWayRate
	 * @return
	 */
	public Double getPayWayMoney(Double payMoney, Double payWayRate) {
		
		return getCeil(payMoney.doubleValue() * payWayRate.doubleValue() / 1000, 2);
		
		/*if (payMoney.doubleValue() * payWayRate / 1000 >= 0.01) {
			return payMoney.doubleValue() * payWayRate.doubleValue() / 1000;
		} else {
			return 0.00;
		}*/
	}

	/**
	 * 获取某一个商户今日的流程总金额
	 */
	public double getDayMoneySum(Map<String, Object> map) throws Exception {
		// 今日成功交易总金额数
		double daySuccessMoney = payv2PayOrderMapper.getMoneySum(map);
		return daySuccessMoney;
	}

	/**
	 * 查询某一个日期的数据统计
	 */
	public Payv2PayOrder getDayOrderInfos(Map<String, Object> map) {
		return payv2PayOrderMapper.getDayOrderInfos(map);
	}

	/**
	 * 查询某一个月的数据统计
	 */
	public Payv2PayOrder getMonthOrderInfos(Map<String, Object> map) {
		return payv2PayOrderMapper.getMonthOrderInfos(map);
	}

	/**
	 * 按照支付通道分组查询某一个日期的数据统计
	 */
	public List<Payv2PayOrder> getDayOrderInfoByPayId(Map<String, Object> map) {
		return payv2PayOrderMapper.getDayOrderInfoByPayId(map);
	}

	/**
	 * 按照支付通道分组查询某一个月的数据统计
	 */
	public List<Payv2PayOrder> getMonthOrderInfoByPayId(Map<String, Object> map) {
		return payv2PayOrderMapper.getMonthOrderInfoByPayId(map);
	}

	/**
	 * 获取商户商品APP订单列表
	 */
	@SuppressWarnings("unchecked")
	public PageObject<shopAppOrderVo> getShopOrderPageByApp(Map<String, Object> map) throws Exception {
		map.put("orderType", 2);
		int totalData = 0;
		PageObject<shopAppOrderVo> pageObject = null;
		List<shopAppOrderVo> list = null;
		PageHelper pageHelper=null;
		int  curPage=Integer.valueOf(map.get("curPage").toString());
		int  pageData=Integer.valueOf(map.get("pageData").toString());
		map.put("curPage", curPage);
		map.put("pageData", pageData);
		//筛选有交易状态的；
		if (map.get("tradingType") != null) {
			int tradingType = Integer.valueOf(map.get("tradingType").toString());
			if (tradingType == 3) {
				totalData = payv2PayOrderMapper.getSuccessOrderCount(map);
				pageHelper = new PageHelper(totalData, map);
				list = payv2PayOrderMapper.getSuccessOrderPageByAll(map);
			} else if (tradingType == 1 || tradingType == 2) {
				// 1：全部退款 2 部分退款
				map.put("refundType", tradingType);
				totalData = payv2PayOrderMapper.getSuccessOrderRefundCount(map);
				pageHelper = new PageHelper(totalData, map);
				list = payv2PayOrderMapper.getSuccessOrderRefundPageByAll(map);
			}
			
		} 
		//无交易状态的
		else {
			List<shopAppOrderVo> countList = payv2PayOrderMapper.getOrderPageByCount(map);
			for (shopAppOrderVo shopAppOrderVo : countList) {
				totalData = totalData + Integer.valueOf(shopAppOrderVo.getId().toString());
			}
			pageHelper = new PageHelper(totalData, map);
			list = payv2PayOrderMapper.getOrderPageByAll(pageHelper.getMap());
		}
		for (shopAppOrderVo shopAppOrderVo : list) {
			if (shopAppOrderVo.getTradingType() == 1 || shopAppOrderVo.getTradingType() == 2) {
				// 获取交易总金额
				Payv2PayOrder Payv2PayOrder = new Payv2PayOrder();
				Payv2PayOrder.setId(shopAppOrderVo.getOrderId());
				Payv2PayOrder = payv2PayOrderMapper.selectSingle(Payv2PayOrder);
				if (Payv2PayOrder != null) {
					shopAppOrderVo.setTradingMoney(Payv2PayOrder.getPayDiscountMoney());
				}
				shopAppOrderVo.setYesOrNoRefund(0);//退款订单
			}
			//查询原订单是否已经退款过···
			if(shopAppOrderVo.getTradingType() == 3){
				Payv2PayOrderRefund payv2PayOrderRefund=new Payv2PayOrderRefund();
				payv2PayOrderRefund.setOrderId(shopAppOrderVo.getId());
				List<Payv2PayOrderRefund> orderRefundList=	payv2PayOrderRefundMapper.selectByObject(payv2PayOrderRefund);
				if(orderRefundList.size()>0){
					shopAppOrderVo.setYesOrNoRefund(1);//已经退过款了
				}else{
					shopAppOrderVo.setYesOrNoRefund(2);//没有发起退款
				}
			}
			// 获取支付通道图像与名字
			Payv2PayWay payv2PayWay = new Payv2PayWay();
//			payv2PayWay.setIsDelete(2);
//			payv2PayWay.setStatus(1);
			payv2PayWay.setId(shopAppOrderVo.getPayWayId());
			payv2PayWay = payv2PayWayMapper.selectSingle(payv2PayWay);
			if (payv2PayWay != null) {
				shopAppOrderVo.setWayIcon(payv2PayWay.getWayIcon());
				shopAppOrderVo.setWayName(payv2PayWay.getWayName());
			}
			
		}
		pageObject = pageHelper.getPageObject();
		pageObject.setDataList(list);
		return pageObject;
	}

	public List<Map<String, Object>> sumMoney(Map<String, Object> paramMap) {
		return payv2PayOrderMapper.sumMoney(paramMap);
	}

	/**
	 * 威富通微信回调操作
	 * 
	 * @param map
	 * @param createSign
	 */
	public boolean wftWechatPayCallBack(Map<String, String> map) throws Exception {
		String out_trade_no = map.get("out_trade_no");// 商户订单号
		String pay_result = map.get("pay_result");// 交易状态
		String transaction_id = map.get("transaction_id"); // 威富通订单号
		//String out_transaction_id = map.get("out_transaction_id"); // 微信订单号
		String time_end = map.get("time_end"); // 威富通支付时间
		if ("0".equals(pay_result)) {
			Payv2PayOrder order = new Payv2PayOrder();
			order.setOrderNum(out_trade_no);
			order = payv2PayOrderMapper.selectSingle(order);
			if (order != null && order.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS) {
				// 查询商家app 获取回调地址
				Payv2BussCompanyApp pbca = new Payv2BussCompanyApp();
				pbca.setId(order.getAppId());
				pbca = payv2BussCompanyAppMapper.selectSingle(pbca);

				// 交易支付成功
				String total_fee = map.get("total_fee").toString();// 支付金额

				Double payDiscountMoney = NumberUtils.createDouble(total_fee);
				
				//支付时间保存为威富通支付时间
				Date date = getDate(time_end);
				order.setBankTransaction(transaction_id);
				order.setPayTime(date);
				order.setCreateTime(date);
				order.setPayDiscountMoney(payDiscountMoney / 100);
				order.setDiscountMoney(order.getPayMoney() - payDiscountMoney / 100);

				// 通知商户
				return notifyMerchant(order, pbca);
			}else if(order != null && order.getPayStatus() == PayFinalUtil.PAY_ORDER_SUCCESS){
				return true;
			}
		} else {
			logger.debug("支付集-支付宝回调暂不处理的交易状态:" + pay_result);
		}
		return false;
	}

	/**
	 * 支付详情（保存用户，查询支付方式信息）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> payOrderDetail(Map<String, Object> paramMap, Payv2BussCompanyApp pbca) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		Integer rateType = 1;// app支付
		Double payMoney = Double.valueOf(paramMap.get("payMoney").toString());
		Long companyId = pbca.getCompanyId();
		// 创建订单
		Payv2PayOrder payOrder = new Payv2PayOrder();

		payOrder.setMerchantOrderNum(paramMap.get("bussOrderNum").toString());
		payOrder.setAppId(pbca.getId());
		payOrder = payv2PayOrderMapper.selectSingle(payOrder);
		if(payOrder!=null){
			if(payOrder.getPayStatus()!=1 && payOrder.getPayStatus() != 5){
				payOrder.setReturnUrl(paramMap.get("returnUrl") == null ? null : paramMap.get("returnUrl").toString()); // 回调的页面
				payOrder.setNotifyUrl(paramMap.get("notifyUrl").toString()); // 回调地址（服务器）
				payv2PayOrderMapper.updateByEntity(payOrder);
			}else{
				// 支付已完成
				resultMap.put("status", PayFinalUtil.PAY_STATUS_FAIL_OK);
				return resultMap;
			}
		}else{
			payOrder = new Payv2PayOrder();
			// 创建订单
			payOrder = createOrder(paramMap, payOrder, pbca, payMoney, rateType);
		}

		// 钱包中最高优惠
		double maxDiscountMoney = 0;
		resultMap.put("maxDiscountMoney", maxDiscountMoney);
		resultMap.put("payMoney", payMoney);
		resultMap.put("orderName", payOrder.getOrderName());
		// 返回商户支持的所有第三方支付信息
		List<Payv2PayWay> otherList = payv2PayWayService.getWalletConfig(payMoney, companyId
				,2,rateType);
		//过滤封装后的支付方式
		List<Payv2PayWay> duplicateList = getResultList(otherList, payMoney, companyId, rateType);
		
		resultMap.put("otherList", duplicateList);
		// 返回的订单信息
		resultMap.put("orderNum", payOrder.getOrderNum());
		resultMap.put("status", PayFinalUtil.PAY_STATUS_SUSSESS);
		return resultMap;
	}
	
	/**
	 * 根据商户ID 查询 payv2_company_quota,然后循环判断过滤掉第三方支付满足单额和当天金额限制的支付
	 * @param otherList
	 * @param payMoney
	 * @param list
	 * @return
	 */
	private List<Payv2PayWay> getResultList(List<Payv2PayWay> otherList,Double payMoney
			,Long companyId,Integer payType){
		
		//TODO根据商户ID 查询 payv2_company_quota,然后循环判断过滤掉第三方支付满足单额和当天金额限制的支付
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("companyId", companyId);
		paramMap.put("payType", payType);
		paramMap.put("recordTime", new Date());
		//TODO Payv2CompanyQuota 关联 payv2_pay_way_rate 然后条件and (ppwr.pay_type = #{payType} or ppwr.pay_type=4)
		List<Payv2CompanyQuota> list = payv2CompanyQuotaMapper.selectPayv2CompanyQuotaForCompany(paramMap);
		
		//每个通道当天总额度
		paramMap.remove("companyId");
		List<Payv2CompanyQuota> allList = payv2CompanyQuotaMapper.selectTotal(paramMap);
		
		forway:for(Payv2PayWay way : otherList){
			way.setIsShow(1);
			//1.过滤单笔订单金额大于单笔金额或者当天金额大于商户支付通道限额当天金额 
			Double oneMaxMoney = way.getOneMaxMoney() == null ? 0 : way.getOneMaxMoney();
			
			//官方通道设置的总限额
			Double rateOneMaxMoney = way.getRateOneMaxMoney() == null ? 0 : way.getRateOneMaxMoney();
			
			//超过单笔限额(总通道)
			if(rateOneMaxMoney > 0 && payMoney > rateOneMaxMoney){
				//置灰
				way.setIsShow(2);
				continue;
			}
			//超过单笔限额
			if(oneMaxMoney > 0 && payMoney > oneMaxMoney){
				//置灰
				way.setIsShow(2);
				continue;
			}
			
			//总通道限额
			if(allList != null && allList.size() > 0){
				for(Payv2CompanyQuota quota : allList){
					if(way.getRateId()==quota.getRateId()){
						Double totalMoney = quota.getTotalMoney() == null ? 0 : quota.getTotalMoney();//当天支付总金额
						//当天限额
						Double maxMoney = way.getMaxMoney() == null ? 0 : way.getMaxMoney();
						//超过当天限额
						if(maxMoney > 0 && totalMoney+payMoney > maxMoney){
							way.setIsShow(2);
							continue forway;
						}
					}
				}
			}
			
			//商户支持通道限额
			if(list != null && list.size() > 0){
				for(Payv2CompanyQuota quota : list){
					if(way.getRateId()==quota.getRateId()){
						Double totalMoney = quota.getTotalMoney() == null ? 0 : quota.getTotalMoney();//当天支付总金额
						//当天限额
						Double maxMoney = way.getMaxMoney() == null ? 0 : way.getMaxMoney();
						//超过当天限额
						if(maxMoney > 0 && totalMoney+payMoney > maxMoney){
							way.setIsShow(2);
							continue forway;
						}
					}
				}
			}
		}
		//去重
		List<Payv2PayWay> duplicateList = new ArrayList<Payv2PayWay>();
		Set<Long> nameSet = new HashSet<Long>();
		for(Payv2PayWay way : otherList){
			if(!nameSet.contains(way.getId())){
				for(Payv2PayWay way1 : otherList){
					if(way.getId().equals(way1.getId())){
						if(way1.getIsShow()==1){
							nameSet.add(way1.getId());
							duplicateList.add(way1);
							break;
						}
					}
				}
				if(!nameSet.contains(way.getId())){
					nameSet.add(way.getId());
					duplicateList.add(way);
				}
			}
		}
		return duplicateList;
	}

	/**
	 * 创建订单(无界面支付)
	 * 
	 * @param map
	 * @param paramMap
	 * @param pbca
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> payPage(String ip,String appType, Map<String, Object> paramMap
			, Payv2BussCompanyApp pbca,Integer payViewType) throws Exception {
		
		Integer payType = 1;//app支付

		// 创建订单
		Map<String, String> orderMap = dyCreateOrder(paramMap, pbca, payViewType,payType);
		if (orderMap.get("status").equals(PayFinalUtil.PAY_STATUS_SUSSESS)) {
			
			return PayRateDictValue.ratePay(orderMap.get("dictName").toString(), orderMap, ip, appType,payType);
		}
		return orderMap;
	}
	
	/**
	 * 支付集回调操作(有氧)
	 * 
	 * @param map
	 * @param createSign
	 */
	public boolean yyPayCallBack(Map<String, Object> params) throws Exception {
		//String trade_no = params.get("order_num").toString();// 有氧订单号
		String trade_status = params.get("result_code").toString();// 交易状态
		String out_trade_no = params.get("buss_order_num").toString(); // 商户订单号
		if ("200".equals(trade_status)) {
			Payv2PayOrder order = new Payv2PayOrder();
			order.setOrderNum(out_trade_no);
			order = payv2PayOrderMapper.selectSingle(order);
			if (order != null && order.getPayStatus() != PayFinalUtil.PAY_ORDER_SUCCESS) {
				// 查询商家app 获取回调地址
				Payv2BussCompanyApp pbca = new Payv2BussCompanyApp();
				pbca.setId(order.getAppId());
				pbca = payv2BussCompanyAppMapper.selectSingle(pbca);

				// 交易支付成功
				String total_amount = "0";
				total_amount = params.get("pay_discount_money").toString();// 支付金额

				Double payDiscountMoney = NumberUtils.createDouble(total_amount);
				// order.setPayType(PayFinalUtil.PAYTYPE_ORDER_ALIPAY); //支付类型
				// 支付宝
				Date date = new Date();
				order.setPayTime(date);
				order.setCreateTime(date);
				order.setPayDiscountMoney(payDiscountMoney);
				order.setDiscountMoney(order.getPayMoney() - payDiscountMoney);
				// order.setOtherOrderNum(trade_no);

				// 通知商户
				return notifyMerchant(order, pbca);
			}
		} else {
			logger.debug("支付集-支付宝回调暂不处理的交易状态:" + trade_status);
		}
		return true;
	}

	public Payv2PayOrder selectSingleDetail(Payv2PayOrder t) {
		return payv2PayOrderMapper.selectSingleDetail(t);
	}
	/**
	* @Title: getOrderInfo 
	* @Description: 获取订单详情与回调支付通道的参数
	* @param @param orderNum
	* @param @return    设定文件 
	* @return Payv2PayOrder    返回类型 
	* @throws
	*/
	public Payv2PayOrder getOrderInfo(String orderNum) {
		Payv2PayOrder payv2PayOrder=new Payv2PayOrder();
		payv2PayOrder.setOrderNum(orderNum);
		payv2PayOrder=payv2PayOrderMapper.selectSingleDetail2(payv2PayOrder);
		if(payv2PayOrder==null||payv2PayOrder.getRateId()==null){
			return null;
		}
		return payv2PayOrder;
	}
	/**
	 * 获取本地时间段的所有订单
	 */
	public List<Payv2PayOrder> selectByObjectDate(Map<String, Object> map) throws Exception {
		List<Payv2PayOrder> payv2PayOrderList = payv2PayOrderMapper.selectByObjectDate(map);
		if(map.containsKey("createTimeBegin")){
			//将我们的订单表与退款订单数据插入，对账表里面；以备对账可用
			Map<String, Object> rMap=new HashMap<String, Object>();
			rMap.put("createTimeBegin", map.get("createTimeBegin"));
			payv2PayOrderClearMapper.addOrderClearByOrder(rMap);
			payv2PayOrderClearMapper.addOrderClearByRefundOrder(rMap);
		}	
		return payv2PayOrderList;
	}
	/**
	 * 获取各个项目总数
	 */
	public Map<String,Object> selectOrderSum(Map<String, Object> map){
		return payv2PayOrderMapper.selectOrderSum(map);
	}
	public static void main(String[] args) {
//		Long companyId=1l;
//		Long rateId=2l;
//		Date as = new Date();
//		String time = DateUtil.DateToString(as, "yyyyMMdd");
//		String companyRedisKey=companyId+rateId+time;
//		System.out.println(companyRedisKey);
//		String companyRedisKey1=companyId+"CID"+rateId+"RID"+time;
//		System.out.println(companyRedisKey1);
		
		String result = "﻿SUCCESS";
		System.out.println("SUCCESS".equals(result.toUpperCase()));
		System.out.println("SUCCESS".equalsIgnoreCase(result));
	}
}
