package com.pay.manger.controller.merchantApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.core.teamwork.base.util.ObjectUtil;
import com.core.teamwork.base.util.ReadPro;
import com.core.teamwork.base.util.http.HttpUtil;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.payv2.entity.Payv2BussCompanyShop;
import com.pay.business.payv2.entity.Payv2PayOrder;
import com.pay.business.payv2.entity.Payv2PayWay;
import com.pay.business.payv2.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.service.Payv2PayOrderService;
import com.pay.business.payv2.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.annotation.LoginValidate;
import com.pay.manger.controller.admin.Payv2BussCompanyShopVo;
import com.pay.manger.controller.admin.Payv2PayOrderVo;
import com.pay.manger.controller.base.InterfaceBaseController;

/**
 * 
 * @ClassName: ReceivablesController
 * @Description:商户APP收款栏业务处理Controller
 * @author zhoulibo
 * @date 2017年3月3日 下午4:55:54
 */
@Controller
@RequestMapping("/receivables/")
public class ReceivablesController extends InterfaceBaseController {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;// 订单表
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;// 商户商铺表
	@Autowired
	private Payv2PayWayService payv2PayWayService;// 支付通道表

	/**
	 * 
	 * @Title: getHomePageInfo
	 * @Description:获取收款首页数据
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getHomePageInfo")
	public Map<String, Object> getHomePageInfo(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 获取当前登录商户ID
			Long companyId = getSessionUserId();
			// 获取商户今日的流水总金额数
			map.put("companyId", companyId);
			map.put("payStatus", 1);
			double daySuccessMoney = payv2PayOrderService.getDayMoneySum(map);
			// 获取活动
			List<Object> eventList = new ArrayList<Object>();
			resultMap.put("daySuccessMoney", daySuccessMoney);
			resultMap.put("eventList", eventList);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：getHomePageInfo", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getShopCollectionCode
	 * @Description:获取商户门店收款码
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getShopCollectionCode")
	public Map<String, Object> getShopCollectionCode(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取当前登录商户ID
		Long companyId = getSessionUserId();
		// 获取商户旗下店铺收款码
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		payv2BussCompanyShop.setCompanyId(companyId);
		payv2BussCompanyShop.setIsDelete(2);
		payv2BussCompanyShop.setShopStatus(2);
		List<Payv2BussCompanyShop> shopList = payv2BussCompanyShopService.selectByObject(payv2BussCompanyShop);
		List<Payv2BussCompanyShopVo> shopVoList = new ArrayList<Payv2BussCompanyShopVo>();
		for (Payv2BussCompanyShop payv2BussCompanyShop2 : shopList) {
			Payv2BussCompanyShopVo vo = new Payv2BussCompanyShopVo();
			vo.setId(payv2BussCompanyShop2.getId());
			vo.setCompanyId(payv2BussCompanyShop2.getCompanyId());
			// 收款码
			vo.setShopTwoCodeUrl(payv2BussCompanyShop2.getShopTwoCodeUrl());
			vo.setShopName(payv2BussCompanyShop2.getShopName());
			shopVoList.add(vo);
		}
		resultMap.put("shopVoList", shopVoList);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;
	}

	/**
	 * @Title: getDaySuccessMoney
	 * @Description:收款首页--》刷新今日流水收益
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getDaySuccessMoney")
	public Map<String, Object> getDaySuccessMoney(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 获取当前登录商户ID
			Long companyId = getSessionUserId();
			// 获取商户今日的流水总金额数
			map.put("companyId", companyId);
			map.put("payStatus", 1);
			double daySuccessMoney = payv2PayOrderService.getDayMoneySum(map);
			resultMap.put("daySuccessMoney", daySuccessMoney);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：getDaySuccessMoney", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getReportsInfo
	 * @Description:收款首页--》报表数据统计处理详情
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getReportsInfo")
	public Map<String, Object> getReportsInfo(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "choiceType", "payTime" }, map);
		if (isNotNull) {
			Long companyId = getSessionUserId();
			// 获取商户所有旗下的门店
			Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
			payv2BussCompanyShop.setCompanyId(companyId);
			payv2BussCompanyShop.setIsDelete(2);
			payv2BussCompanyShop.setShopStatus(2);
			List<Payv2BussCompanyShop> shopList = payv2BussCompanyShopService.selectByObject(payv2BussCompanyShop);
			List<Payv2BussCompanyShopVo> shopVoList = new ArrayList<Payv2BussCompanyShopVo>();
			for (Payv2BussCompanyShop payv2BussCompanyShop2 : shopList) {
				Payv2BussCompanyShopVo vo = new Payv2BussCompanyShopVo();
				vo.setId(payv2BussCompanyShop2.getId());
				vo.setCompanyId(payv2BussCompanyShop2.getCompanyId());
				vo.setShopName(payv2BussCompanyShop2.getShopName());
				shopVoList.add(vo);
			}
			int choiceType = Integer.valueOf(map.get("choiceType").toString());
			map.put("orderType", 2);// 店铺
			Payv2PayOrderVo vo = new Payv2PayOrderVo();
			List<Payv2PayOrderVo> listVo = new ArrayList<Payv2PayOrderVo>();
			Payv2PayOrder payv2PayOrder = new Payv2PayOrder();
			List<Payv2PayOrder> orderList = new ArrayList<Payv2PayOrder>();
			map.put("companyId", companyId);
			// 日
			if (choiceType == 1) {
				// 获取某一个日期的数据统计
				payv2PayOrder = payv2PayOrderService.getDayOrderInfos(map);
				// 查询在某个时间里每一个支付通道的数据统计
				orderList = payv2PayOrderService.getDayOrderInfoByPayId(map);
			}
			// 月
			if (choiceType == 2) {
				// 获取某一个月期的数据统计
				payv2PayOrder = payv2PayOrderService.getMonthOrderInfos(map);
				// 查询在某个时间里每一个支付通道的数据统计
				orderList = payv2PayOrderService.getMonthOrderInfoByPayId(map);
			}
			if (payv2PayOrder != null) {
				// 交易笔数
				vo.setNumber(Integer.valueOf(payv2PayOrder.getId().toString()));
				// 实收金融
				if (payv2PayOrder.getPayDiscountMoney() == null) {
					vo.setPayDiscountMoney(0.00);
				} else {
					vo.setPayDiscountMoney(payv2PayOrder.getPayDiscountMoney());
				}
				// 到账金额
				if (payv2PayOrder.getPayDiscountMoney() == null) {
					payv2PayOrder.setPayDiscountMoney(0.00);
				}
				if (payv2PayOrder.getPayWayMoney() == null) {
					payv2PayOrder.setPayWayMoney(0.00);
				}
				vo.setAmountOfMoney(payv2PayOrder.getPayDiscountMoney() - payv2PayOrder.getPayWayMoney());
			} else {
				vo.setNumber(0);
				// 实收金融
				vo.setPayDiscountMoney(0.00);
				// 到账金额
				vo.setAmountOfMoney(0.00);
			}
			for (Payv2PayOrder payv2PayOrder2 : orderList) {
				Payv2PayOrderVo vo1 = new Payv2PayOrderVo();
				// 查询支付通道名字
				Payv2PayWay p = new Payv2PayWay();
				p.setId(payv2PayOrder2.getPayWayId());
				p = payv2PayWayService.selectSingle(p);
				if (p != null) {
					// 通道名字
					vo1.setWayName(p.getWayName());
					vo1.setWayIcon(p.getWayIcon());
				}
				// 交易统计
				// 交易金额
				vo1.setPayDiscountMoney(payv2PayOrder.getPayDiscountMoney());
				// 交易笔数
				vo1.setNumber(Integer.valueOf(payv2PayOrder.getId().toString()));
				listVo.add(vo1);
			}
			// 每日或者每月数据详情返回
			resultMap.put("dayInfo", vo);
			// 交易统计数据返回
			resultMap.put("dayPayWayList", listVo);
			// 商户商铺下拉数据数据返回
			resultMap.put("shopList", shopVoList);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
		}
		return resultMap;
	}

	/**
	 * @Title: setSweepCollection
	 * @Description:收款---》扫码扣款数据提交
	 * @param @param map authCode：条形码 payMoney：钱 id：商户ID
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("setSweepCollection")
	public Map<String, Object> setSweepCollection(@RequestParam Map<String, Object> map) {
		logger.info("调用收款支付接口开始--------------------------------》");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "authCode", "payMoney", "id" }, map);
			if (isNotNull) {
				String httpUrl=ReadPro.getValue("sweepCollection_url");
				logger.info("调用收款支付接口URL-----------"+httpUrl+"---------------------》");
				if(httpUrl!=null){
					String result=HttpUtil.httpPost(httpUrl,map);
					JSONObject json = JSONObject.parseObject(result);
					if (json.get("resultCode")!=null&&"200".equals(json.get("resultCode").toString())) {
						JSONObject data = json.getJSONObject("Data");
						String orderId = data.get("orderId").toString();
						String orderNume = data.get("orderNume").toString();
						resultMap.put("tradingType", 3);
						resultMap.put("orderId",orderId);
						resultMap.put("orderNume",orderNume);
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
						logger.info("调用收款支付接口结束-----------成功支付---------------------》");
					}else{
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
						logger.info("调用收款支付接口结束-----------失败支付---------------------》");
					}
				}else{
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null,"获取支付URL失败");
					logger.info("调用收款支付接口结束-----------获取支付URL失败---------------------》");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
				logger.info("调用收款支付接口结束-----------失败支付---------------------》");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：setSweepCollection", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}
}
