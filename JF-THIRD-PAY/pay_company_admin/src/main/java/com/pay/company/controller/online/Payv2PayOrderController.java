package com.pay.company.controller.online;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.teamwork.base.util.date.DateUtil;
import com.core.teamwork.base.util.page.PageObject;
import com.core.teamwork.base.util.returnback.ReMessage;
import com.pay.business.merchant.entity.Payv2BussCompany;
import com.pay.business.merchant.entity.Payv2BussCompanyApp;
import com.pay.business.merchant.entity.Payv2BussSupportPayWay;
import com.pay.business.merchant.service.Payv2BussCompanyAppService;
import com.pay.business.merchant.service.Payv2BussCompanyService;
import com.pay.business.merchant.service.Payv2BussSupportPayWayService;
import com.pay.business.order.entity.Payv2PayGoods;
import com.pay.business.order.entity.Payv2PayOrder;
import com.pay.business.order.entity.Payv2PayOrderRefund;
import com.pay.business.order.mapper.Payv2PayOrderMapper;
import com.pay.business.order.service.Payv2PayGoodsService;
import com.pay.business.order.service.Payv2PayOrderRefundService;
import com.pay.business.order.service.Payv2PayOrderService;
import com.pay.business.payv2.service.Payv2BussAppSupportPayWayService;
import com.pay.business.payway.entity.Payv2PayWay;
import com.pay.business.payway.service.Payv2PayWayRateService;
import com.pay.business.payway.service.Payv2PayWayService;
import com.pay.business.util.ParameterEunm;
import com.pay.company.ExportExcel.CsvWriter;
import com.pay.company.controller.admin.BaseManagerController;

/**
 * @ClassName: Payv2PayOrderController
 * @Description:订单管理
 * @author mofan
 * @date 2017年2月17日 09:36:42
 */
@Controller
@RequestMapping("/online/payv2PayOrder/*")
public class Payv2PayOrderController extends BaseManagerController<Payv2PayOrder, Payv2PayOrderMapper> {

	private static final Logger logger = Logger.getLogger(Payv2PayOrderController.class);
	@Autowired
	private Payv2PayOrderService payv2PayOrderService;
	@Autowired
	private Payv2BussCompanyService payv2BussCompanyService;
	@Autowired
	private Payv2BussCompanyAppService payv2BussCompanyAppService;
	@Autowired
	private Payv2PayWayService payv2PayWayService;
	@Autowired
	private Payv2BussSupportPayWayService payv2BussSupportPayWayService;
	@Autowired
	private Payv2BussAppSupportPayWayService payv2BussAppSupportPayWayService;
	@Autowired
	private Payv2PayGoodsService payv2PayGoodsService;
	@Autowired
	private Payv2PayOrderRefundService payv2PayOrderRefundService;
	@Autowired
	private Payv2PayWayRateService payv2PayWayRateService;

	/**
	 * @Title: getPayv2PayOrderList
	 * @Description:获取订单管理列表
	 * @param map
	 * @return 设定文件
	 * @return ModelAndView 返回类型
	 * @date 2017年2月21日 上午09:12:21
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("getPayv2PayOrderList")
	public Map<String, Object> getPayv2PayOrderList(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		map.put("orderType", 1);// 线上
		validateTimeType(map);
		PageObject<Payv2PayOrder> pageList = null;
		resultMap.put("map", map);
		Payv2BussCompany company = getAdmin();
		map.put("companyId", company.getId());
		try {
			pageList = payv2PayOrderService.getPayOrderPageByObject(map);
			// 获取支付通道
			Payv2BussSupportPayWay supportPayWay = new Payv2BussSupportPayWay();
			supportPayWay.setParentId(company.getId());
			List<Payv2BussSupportPayWay> supportPayWayList = payv2BussSupportPayWayService.selectByObject(supportPayWay);
			resultMap.put("supportPayWayList", supportPayWayList);
			resultMap.put("list", pageList);
			resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = ReMessage.resultBack(ParameterEunm.ERROR_500_CODE, resultMap);
		}
		return resultMap;
	}

	/**
	 * 查看订单详情
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/toEditPayv2PayOrder")
	public Map<String, Object> toEditPayv2PayOrder(@RequestParam Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.get("id") != null) {
			try {
				// 根据订单ID获取订单详情
				Payv2PayOrder order = payv2PayOrderService.detail(map);
				if (order != null) {
					if (order.getAppId() != null) {
						map.put("id", order.getAppId());
						Payv2BussCompanyApp app = payv2BussCompanyAppService.detail(map);
						if (app != null) {
							order.setAppName(app.getAppName());
						}

					}
					if (order.getGoodsId() != null) {
						map.put("id", order.getGoodsId());
						Payv2PayGoods goods = payv2PayGoodsService.detail(map);
						if (goods != null) {
							order.setGoodsName(goods.getGoodsName());
						}

					}
					// 获取支付渠道
					// if (order.getRateId() != null) {
					// map.put("id", order.getRateId());
					// Payv2PayWayRate payv2PayWayRate =
					// payv2PayWayRateService.detail(map);
					// if (payv2PayWayRate != null) {
					// order.setWayName(payv2PayWayRate.getPayWayName());
					// }
					// }
					// 获取支付方式：应产品要求修改成获取它的支付方式：20170601修改
					if (order.getPayWayId() != null) {
						map.put("id", order.getPayWayId());
						Payv2PayWay payv2PayWay = payv2PayWayService.detail(map);
						if (payv2PayWay != null) {
							order.setWayName(payv2PayWay.getWayName());
						}
					}

					if (map.get("id") != null) {
						map.remove("id");
					}
					map.put("orderId", order.getId());
					map.put("orderType", order.getOrderType());
					// 获取退款详情
					Payv2PayOrderRefund payv2PayOrderRefund = payv2PayOrderRefundService.detail(map);
					if (payv2PayOrderRefund != null) {
						//set 退款金额
						order.setReturnMoney(payv2PayOrderRefund.getRefundMoney());
					} else {
						//set 退款金额
						order.setReturnMoney(0.00);
					}

					if (order.getPayWayMoney() == null) {
						order.setPayWayMoney(0.00);// 通道费（手续费）
					}
					// 如果是成功或者回调失败才会有值
					if (order.getPayStatus() != null && (order.getPayStatus().intValue() == 1 || order.getPayStatus().intValue() == 5)) {
						//订单金额
						double payMoney = order.getPayMoney() == null ? 0.00 : order.getPayMoney();
						//退款金额
						double returnMoney = order.getReturnMoney() == null ? 0.00 : order.getReturnMoney();
						//通道手续费
						double paywayMoney = order.getPayWayMoney() == null ? 0.00 : order.getPayWayMoney();
						//优惠金额
						double discountMoney=order.getDiscountMoney() == null ? 0.00 : order.getDiscountMoney();
						//计算：实收金额=订单金额-优惠金额
						BigDecimal bg = new BigDecimal(payMoney-discountMoney);
						double actuallyPayMoney = bg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						//计算：到账金额=实收金额-手续费-退款金额
						BigDecimal bg1 = new BigDecimal(actuallyPayMoney - paywayMoney-returnMoney);
						double arrivalMoney = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						// set到账金额
						order.setArrivalMoney(arrivalMoney);
						// set实收金额
						order.setActuallyPayMoney(actuallyPayMoney);
					} else {
						order.setActuallyPayMoney(0.00);// 实收金额
						order.setArrivalMoney(0.00);// 到账金额
					}
					// 判断退款类型
					if (payv2PayOrderRefund != null) {
						if (payv2PayOrderRefund.getRefundType() == 1) {
							// 全部退款模式
							order.setPayStatus(1);
						} else {
							//部分退款
							order.setPayStatus(1);
						}
					}
				}
				resultMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, order);
			} catch (Exception e) {
				logger.error("查看订单详情报错：", e);
				e.printStackTrace();
				resultMap = ReMessage.resultBack(ParameterEunm.FAILED_CODE, null);
			}
		} else {
			resultMap = ReMessage.resultBack(ParameterEunm.PARA_FAILED_CODE, null);
		}
		return resultMap;
	}

	/**
	 * 导出
	 * 
	 * @param map
	 * @param workbook
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exportExcelOrder")
	public Map<String, Object> exportExcelOrder(@RequestParam Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) {
		PageObject<Payv2PayOrder> pageObject;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		OutputStream out = null;
		String displayColNames = null;
		String matchColNames = null;
		String fileName = null;
		String content = "";
		try {
			// 搜索
			map.put("curPage", 1);
			map.put("pageData", 999999999);
			map.put("orderType", 1);// 线上
			Payv2BussCompany company = getAdmin();
			map.put("companyId", company.getId());
			validateTimeType(map);
			pageObject = payv2PayOrderService.getPayOrderPageByObject(map);
			// 获取LIST集合
			List<Map<String, Object>> dataList = fillData(pageObject.getDataList());
			// 完成数据csv文件的封装
			displayColNames = "交易日期,支付集订单号,支付方式,商品名称,商户订单号,交易状态,交易金额,费率%,手续费,结算金额,应用名称";
			matchColNames = "payTime,orderNum,wayName,goodsName,merchantOrderNum,payStatus,payMoney,rate,counterFee,settlementMoney,appName";
			fileName = "订单管理列表";
			content = fillTheme(getAdmin(), map);
			content += CsvWriter.formatCsvData(dataList, displayColNames, matchColNames);
			content += "#------------------交易明细列表结束------------------#\r\n";
			CsvWriter.exportCsv(fileName, content, response);
			returnMap = ReMessage.resultBack(ParameterEunm.SUCCESSFUL_CODE, null);// 201
																					// 标示没有查询到数据
		} catch (Exception e) {
			logger.error("导出订单管理列表.csv错误", e);
			e.printStackTrace();
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnMap;
	}

	/**
	 * 数据封装
	 * 
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> fillData(List<Payv2PayOrder> list) {
		/*
		 * displayColNames =
		 * "交易日期,时间,支付集订单号,支付方式,商品名称,商户内部订单号,商户订单号,收款通道订单号,交易类型," +
		 * "交易状态,付款账号,货币类型,交易金额,商户优惠,支付集优惠,收款通道优惠,消费者实付金额," +
		 * "费率%,手续费,实收金额,结算金额,应用号,商户应用号,应用名称"; matchColNames =
		 * "payTime,createTime,orderNum,payType,goodsName,merchantOrderSeftNum,merchantOrderNum,payWayOrderNum,tradeType,"
		 * +
		 * "payStatus,payUserName,currencyType,payMoney,merchantDiscount,discount,payWayDiscount,customerPayMoney,"
		 * +
		 * "rate,counterFee,actuallyPayMoney,settlementMoney,appNumber,merchantName,appName"
		 * ;
		 */
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Payv2PayOrder order : list) {
			Map<String, Object> orderMap = new HashMap<String, Object>();
			orderMap.put("payTime", order.getPayTime() == null ? "" : DateUtil.DateToString(order.getPayTime(), "yyyy-MM-dd HH:mm:ss") + "\t");// 交易日期
			orderMap.put("orderNum", order.getOrderNum() == null ? "" : order.getOrderNum() + "\t");// 支付集订单号
			orderMap.put("wayName", order.getWayName() == null ? "" : order.getWayName());// 支付方式
			orderMap.put("goodsName", order.getGoodsName() == null ? "" : order.getGoodsName());
			orderMap.put("merchantOrderNum", order.getMerchantOrderNum() == null ? "" : order.getMerchantOrderNum() + "\t");// 商户订单号
			if (order.getPayStatus().intValue() == 1) {
				orderMap.put("payStatus", "支付成功");// 交易状态
			} else if (order.getPayStatus().intValue() == 2) {
				orderMap.put("payStatus", "支付失败");// 交易状态
			} else if (order.getPayStatus().intValue() == 3) {
				orderMap.put("payStatus", "未支付");// 交易状态
			} else if (order.getPayStatus().intValue() == 4) {
				orderMap.put("payStatus", "超时");// 交易状态
			} else {
				orderMap.put("payStatus", "扣款成功回调失败");// 交易状态
			}		
			orderMap.put("payMoney", order.getPayMoney() == null ? "" : order.getPayMoney());// 交易金额		
			orderMap.put("rate", order.getBussWayRate() == null ? "" : order.getBussWayRate() + "\t");// 比率
			orderMap.put("counterFee", order.getPayWayMoney()== null ? "" : order.getPayWayMoney() + "\t");// 手续费
			orderMap.put("settlementMoney", order.getActuallyPayMoney() -order.getRefundMoney() + "\t");// 结算金额		
			orderMap.put("appName", order.getAppName() == null ? "" : order.getAppName());// 应用名称
			dataList.add(orderMap);
		}
		return dataList;
	}

	/**
	 * 封装查询日期
	 * 
	 * @param map
	 */
	private void validateTimeType(Map<String, Object> map) {
		if (map.get("dateType") != null) {// 如果查询天数的订单，则要进行天数判断
			String dateType = map.get("dateType").toString();
			int days = 1;
			if ("1".equals(dateType)) {
				days = 0;
			} else if ("2".equals(dateType)) {
				days = 1;
			} else if ("3".equals(dateType)) {
				days = 7;
			} else if ("4".equals(dateType)) {
				days = 30;
			}

			if ("5".equals(dateType)) {

			} else {

				Date startDate = new Date(new Date().getTime() - (long) days * 24 * 60 * 60 * 1000);
				SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = matter1.format(startDate);
				startTime += " 00:00:00";
				Date endDate = null;
				if (days == 0) {
					endDate = new Date(new Date().getTime() - 0 * 24 * 60 * 60 * 1000);
				} else {
					endDate = new Date(new Date().getTime() - 1 * 24 * 60 * 60 * 1000);
				}

				String endTime = matter1.format(endDate);
				endTime += " 23:59:59";
				map.put("payTimeBegin", startTime);
				map.put("payTimeEnd", endTime);
			}

		} else if (map.get("payTimeBegin") != null || map.get("payTimeEnd") != null) {

		} else {
			// 如果不输入查询日期类型，默认取一个月的订单数据
			Date startDate = new Date(new Date().getTime() - (long) 30 * 24 * 60 * 60 * 1000);
			SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
			String startTime = matter1.format(startDate);
			startTime += " 00:00:00";
			Date endDate = new Date(new Date().getTime() - 1 * 24 * 60 * 60 * 1000);
			String endTime = matter1.format(endDate);
			endTime += " 23:59:59";
			map.put("payTimeBegin", startTime);
			map.put("payTimeEnd", endTime);
		}
	}

	/**
	 * 标题封装
	 * 
	 * @param company
	 * @param map
	 * @return
	 */
	public static String fillTheme(Payv2BussCompany company, Map<String, Object> map) {
		StringBuffer buf = new StringBuffer();
		buf.append("交易明细").append(",").append("\r\n");
		buf.append("商户号：").append(",").append(company.getUserName() + "\t").append(",").append("\r\n");
		buf.append("商户名称：").append(",").append(company.getCompanyName()).append(",").append("\r\n");
		buf.append("起始日期：").append(",").append(map.get("payTimeBegin") == null ? "" : map.get("payTimeBegin").toString()).append(",").append("终止日期：")
				.append(",").append(map.get("payTimeEnd") == null ? "" : map.get("payTimeEnd").toString()).append(",").append("\r\n");
		buf.append("#------------------交易明细列表开始------------------#").append(",").append("\r\n");
		return buf.toString();
	}
}
