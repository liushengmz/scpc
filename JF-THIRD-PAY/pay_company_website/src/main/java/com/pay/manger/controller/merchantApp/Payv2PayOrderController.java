package com.pay.manger.controller.merchantApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.core.teamwork.base.util.encrypt.MD5;
import com.core.teamwork.base.util.http.HttpUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.payv2.entity.Payv2BussCompany;
import com.pay.business.payv2.entity.Payv2BussCompanyShop;
import com.pay.business.payv2.entity.Payv2PayOrder;
import com.pay.business.payv2.entity.Payv2PayOrderRefund;
import com.pay.business.payv2.entity.Payv2PayWay;
import com.pay.business.payv2.entity.shopAppOrderVo;
import com.pay.business.payv2.service.Payv2BussCompanyService;
import com.pay.business.payv2.service.Payv2BussCompanyShopService;
import com.pay.business.payv2.service.Payv2PayOrderRefundService;
import com.pay.business.payv2.service.Payv2PayOrderService;
import com.pay.business.payv2.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.manger.annotation.LoginValidate;
import com.pay.manger.controller.admin.OrderInfoVo;
import com.pay.manger.controller.admin.Payv2BussCompanyShopVo;
import com.pay.manger.controller.admin.Payv2PayWayVo;
import com.pay.manger.controller.base.InterfaceBaseController;

/**
 * @ClassName: Payv2PayOrderController
 * @Description:订单相关业务处理类Controller
 * @author zhoulibo
 * @date 2017年3月4日 下午3:00:52
 */
@Controller
@RequestMapping("/payv2PayOrder/")
public class Payv2PayOrderController extends InterfaceBaseController {
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;// 订单表
	@Autowired
	private Payv2BussCompanyShopService payv2BussCompanyShopService;// 商户商铺表
	@Autowired
	private Payv2PayWayService payv2PayWayService;// 支付通道表
	@Autowired
	private Payv2PayOrderRefundService payv2PayOrderRefundService;// 退款订单表
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;// 商户表

	/**
	 * @Title: getShopOrderList
	 * @Description:获取商户商铺订单列表
	 * @param @param map orderNum 订单 companyId商户ID orderType 1线上2线下 appId 商铺ID
	 *        payWayId 支付通道ID payStatus 状态 payTimeBegin开始时间 payTimeEnd结束时间
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getShopOrderList")
	public Map<String, Object> getShopOrderList(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 获取当前登录商户ID
			Long companyId = getSessionUserId();
			map.put("companyId", companyId);
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
			SimpleDateFormat sdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm:ss");
			if (map.get("createTime") == null) {
				Date date = new Date();
				resultMap.put("createTime", sdate.format(date));
				map.put("createTime", date);
			} else {
				String date = map.get("createTime").toString();
				Date newdate = sdate.parse(date);
				map.put("createTime", newdate);
			}
			PageObject<shopAppOrderVo> shopOrderPage = payv2PayOrderService.getShopOrderPageByApp(map);
			// 商户商铺下拉数据数据返回
			resultMap.put("shopList", shopVoList);
			resultMap.put("shopOrderPage", shopOrderPage);

			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生错误，方法名字：getShopOrderList", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getOrderInfoById
	 * @Description:获取订单详情
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getOrderInfoById")
	public Map<String, Object> getOrderInfoById(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isNotNull = ObjectUtil.checkObject(new String[] { "orderId", "tradingType" }, map);
		try {
			if (isNotNull) {
				Long id = Long.valueOf(map.get("orderId").toString());
				int tradingType = Integer.valueOf(map.get("tradingType").toString());
				OrderInfoVo vo = new OrderInfoVo();
				if (tradingType == 3) {
					// 查询总订单里面的订单详情
					Payv2PayOrder payv2PayOrder = new Payv2PayOrder();
					payv2PayOrder.setId(id);
					payv2PayOrder = payv2PayOrderService.selectSingle(payv2PayOrder);
					if (payv2PayOrder != null) {
						vo.setRefundMoney(0.00);
						// 状态
						vo.setPayStatus(payv2PayOrder.getPayStatus());
						vo.setRefundTime(null);
						vo = getOrderInfoVoInfo(payv2PayOrder, vo);
						//查询是否有退款记录
						Payv2PayOrderRefund payv2PayOrderRefund = new Payv2PayOrderRefund();
						payv2PayOrderRefund.setOrderId(payv2PayOrder.getId());
						payv2PayOrderRefund=payv2PayOrderRefundService.selectSingle(payv2PayOrderRefund);
						if(payv2PayOrderRefund!=null){
							vo.setYesOrNoRefund(1);//是
						}else{
							vo.setYesOrNoRefund(2);//否
						}
						// 成功返回
						resultMap.put("orderInfo", vo);
						resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "订单不存在");
					}
				} else {
					// 查询退款表里面的订单
					Payv2PayOrderRefund payv2PayOrderRefund = new Payv2PayOrderRefund();
					payv2PayOrderRefund.setId(id);
					payv2PayOrderRefund = payv2PayOrderRefundService.selectSingle(payv2PayOrderRefund);
					if (payv2PayOrderRefund != null) {
						Payv2PayOrder payv2PayOrder = new Payv2PayOrder();
						payv2PayOrder.setId(payv2PayOrderRefund.getOrderId());
						payv2PayOrder = payv2PayOrderService.selectSingle(payv2PayOrder);
						if (payv2PayOrder != null) {
							//退款金额
							vo.setRefundMoney(payv2PayOrderRefund.getRefundMoney());
							// 状态
							vo.setPayStatus(payv2PayOrderRefund.getRefundStatus());
							//退款时间
							vo.setRefundTime(payv2PayOrderRefund.getRefundTime());
							vo = getOrderInfoVoInfo(payv2PayOrder, vo);
							//退款订单号
							vo.setOrderNum(payv2PayOrderRefund.getRefundNum());
							//是否有退款记录
							vo.setYesOrNoRefund(1);//是
							// 成功返回
							resultMap.put("orderInfo", vo);
							resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
						} else {
							resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "订单不存在");
						}
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "订单不存在");
					}
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：getOrderInfoById", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 
	 * @Title: getOrderInfoVoInfo
	 * @Description:公共类
	 * @param @param payv2PayOrder
	 * @param @return 设定文件
	 * @return OrderInfoVo 返回类型
	 * @throws
	 */
	public OrderInfoVo getOrderInfoVoInfo(Payv2PayOrder payv2PayOrder, OrderInfoVo vo) {
		// 订单金额
		vo.setPayMoney(payv2PayOrder.getPayMoney());
		vo.setPayDiscountMoney(payv2PayOrder.getPayDiscountMoney());
		vo.setPayTime(payv2PayOrder.getPayTime());
		// 查询支付通道名字
		Payv2PayWay p = new Payv2PayWay();
		p.setId(payv2PayOrder.getPayWayId());
		p = payv2PayWayService.selectSingle(p);
		if (p != null) {
			// 通道名字
			vo.setWayName(p.getWayName());
		}
		vo.setPayUserName(payv2PayOrder.getPayUserName());
		vo.setPayWayUserName(payv2PayOrder.getPayWayUserName());
		vo.setOrderNum(payv2PayOrder.getOrderNum());
		// 获取交易门店名字
		Payv2BussCompanyShop payv2BussCompanyShop = new Payv2BussCompanyShop();
		payv2BussCompanyShop.setId(payv2PayOrder.getAppId());
		payv2BussCompanyShop = payv2BussCompanyShopService.selectSingle(payv2BussCompanyShop);
		if (payv2BussCompanyShop != null) {
			vo.setShopName(payv2BussCompanyShop.getShopName());
		}
		// 到账金额
		if (payv2PayOrder.getPayDiscountMoney() != null) {
			if (payv2PayOrder.getPayWayMoney() == null) {
				payv2PayOrder.setPayWayMoney(0.00);
			}
			vo.setAmountOfMoney(payv2PayOrder.getPayDiscountMoney() - payv2PayOrder.getPayWayMoney());
		} else {
			vo.setAmountOfMoney(0.00);
		}
		// 手续费
		if (payv2PayOrder.getPayWayMoney() != null) {
			vo.setPayWayMoney(payv2PayOrder.getPayWayMoney());
		} else {
			vo.setPayWayMoney(0.00);
		}
		return vo;
	}

	/**
	 * @Title: setOrderRefund
	 * @Description:订单发起退款申请数据提交
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unused")
	@LoginValidate
	@ResponseBody
	@RequestMapping("setOrderRefund")
	public Map<String, Object> setOrderRefund(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			boolean isNotNull = ObjectUtil.checkObject(new String[] { "orderId", "refundMoney", "payPassword" }, map);
			if (isNotNull) {
				double refundMoney = Double.valueOf(map.get("refundMoney").toString());
				Long id = Long.valueOf(map.get("orderId").toString());
				Payv2PayOrder payv2PayOrder = new Payv2PayOrder();
				payv2PayOrder.setId(id);
				payv2PayOrder = payv2PayOrderService.selectSingle(payv2PayOrder);
				if (payv2PayOrder != null) {
					Long companyId = getSessionUserId();
					Payv2BussCompany payv2BussCompany = new Payv2BussCompany();
					payv2BussCompany.setId(companyId);
					payv2BussCompany.setIsDelete(2);
					payv2BussCompany.setCompanyStatus(2);
					payv2BussCompany = payv2BussCompanyService.selectSingle(payv2BussCompany);
					if (payv2BussCompany != null) {
						// 判断是否被锁定
						int lockNumber = payv2BussCompanyService.lockNumber(companyId, 110);
						if (lockNumber == 0) {
							String payPassword = map.get("payPassword").toString();
							payPassword = MD5.GetMD5Code(payPassword);
							if (payPassword.equals(payv2BussCompany.getPayPassWord())) {
								Map<String, Object> orderMap = new HashMap<String, Object>();
								orderMap.put("orderNum", payv2PayOrder.getOrderNum());
								orderMap.put("refundMoney", refundMoney);
								orderMap.put("appId", payv2PayOrder.getAppId());
								double payDiscountMoney = payv2PayOrder.getPayDiscountMoney();
								if (refundMoney <= payDiscountMoney) {
									if (refundMoney == payDiscountMoney) {
										// 全额
										orderMap.put("refundType", "Y");
										resultMap.put("tradingType", 1);
									} else {
										// 部分
										orderMap.put("refundType", "N");
										resultMap.put("tradingType", 2);
									}
									String httpUrl = ReadPro.getValue("orderRefund_url");
									logger.info("调用退款接口开始接口URL-----------" + httpUrl + "---------------------》");
									if (httpUrl != null) {
										logger.info("调用退款接口开始--------------------------------》");
										String result = HttpUtil.httpPost(httpUrl, orderMap);
										if (result != null && !result.equals("")) {
											JSONObject json = JSONObject.parseObject(result);
											if (json.get("resultCode") != null && "200".equals(json.get("resultCode").toString())) {
												JSONObject data = json.getJSONObject("Data");
												String orderNume = data.get("refund_num").toString();
												String refundTime = data.get("refund_time").toString();
												SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
												Date date1 = dateFormat.parse(refundTime);
												SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												refundTime = f.format(date1);
												// 退款时间
												resultMap.put("refundTime", refundTime);
												// 退款单号
												resultMap.put("orderNume", orderNume);
												Payv2PayOrderRefund payv2PayOrderRefund = new Payv2PayOrderRefund();
												payv2PayOrderRefund.setRefundNum(orderNume);
												payv2PayOrderRefundService.selectSingle(payv2PayOrderRefund);
												if (payv2PayOrderRefund != null) {
													resultMap.put("refundId", payv2PayOrderRefund.getId());
													resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
													// 删除redis存在的记录
													payv2BussCompanyService.delKey(companyId, 110);
													logger.info("调用退款接口开始接口结束----------退款成功---------------------》");
												} else {
													resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "订单不存在");
												}
											} else {
												resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
												logger.info("调用退款接口开始接口结束-----------退款支付失败---------------------》");
											}
										} else {
											resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
											logger.info("调用退款接口开始接口结束:调起退款接口失败-----------退款支付失败---------------------》");
										}
									} else {
										resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "获取退款URL失败");
										logger.info("调用退款接口开始接口结束-----------获取退款URL失败---------------------》");
									}
								} else {
									resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "退款金额大于实际支付金额");
									logger.info("调用退款接口开始接口结束-----------退款支付失败---------------------》");
								}
							} else {
								int number = payv2BussCompanyService.lockPayPassWord(companyId, 110);
								if (number == 0) {
									resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "支付密码错误");
								}
								if (number == 5) {
									resultMap = ReMessage.resultBack(ParameterEunm.ERROR_LOCK, null, "支付密码5次错误，将锁定1小时");
								}
							}
						} else {
							resultMap = ReMessage.resultBack(ParameterEunm.ERROR_LOCK, null, "支付密码5次错误，已被锁定1小时");
						}
					} else {
						resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "商户无效或者不存在");
					}
				} else {
					resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "订单不存在");
				}
			} else {
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null, "参数有误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("程序发生异常--》方法名：setOrderRefund", e);
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, null);
		}
		return resultMap;
	}

	/**
	 * @Title: getOrderScreenByPay
	 * @Description: 订单筛选获取支付方式接口
	 * @param @param map
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@LoginValidate
	@ResponseBody
	@RequestMapping("getOrderScreenByPay")
	public Map<String, Object> getOrderScreenByPay(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Payv2PayWay payv2PayWay = new Payv2PayWay();
		payv2PayWay.setIsDelete(2);
		payv2PayWay.setStatus(1);
		List<Payv2PayWay> payWayList = payv2PayWayService.selectByObject(payv2PayWay);
		List<Payv2PayWayVo> payWayListVo = new ArrayList<Payv2PayWayVo>();
		for (Payv2PayWay payv2PayWay2 : payWayList) {
			Payv2PayWayVo vo = new Payv2PayWayVo();
			vo.setId(payv2PayWay2.getId());
			vo.setWayName(payv2PayWay2.getWayName());
			payWayListVo.add(vo);
		}
		resultMap.put("payWayList", payWayListVo);
		resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		return resultMap;

	}
}
